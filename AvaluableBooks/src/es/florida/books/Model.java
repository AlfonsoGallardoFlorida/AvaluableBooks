package es.florida.books;

import java.io.File;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Classe Model que gestiona les interaccions amb la base de dades i altres
 * funcionalitats del sistema.
 */
public class Model {
	private static Connection con;
	public Vista vista;

	/**
	 * Constructor del Model.
	 */
	public Model() {

	}

	// Inicialització estàtica que llegix la configuració de connexió des d'un
	// fitxer XML
	static {
		llegirXML("client.xml");
	}

	/**
	 * Llegeix la configuració de connexió des d'un fitxer XML.
	 * 
	 * @param xmlFile El nom del fitxer XML que conté la configuració.
	 */
	public static void llegirXML(String xmlFile) {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		try {

			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document document = dBuilder.parse(new File(xmlFile));

			NodeList nodeList = document.getElementsByTagName("connexio");
			Node node = nodeList.item(0);

			Element eElement = (Element) node;
			String url = eElement.getElementsByTagName("url").item(0).getTextContent();
			String user = eElement.getElementsByTagName("user").item(0).getTextContent();
			String password = eElement.getElementsByTagName("password").item(0).getTextContent();

			con = DriverManager.getConnection(url, user, password);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Comprova la connexió d'un usuari amb les credencials proporcionades.
	 * 
	 * @param user Nom d'usuari.
	 * @param pass Contrasenya de l'usuari.
	 * @return Tipus d'usuari si les credencials són correctes, null altrament.
	 */
	public String comprovarConn(String user, String pass) {
		try {
			String sql = "SELECT type FROM users WHERE user = ? AND pass = ?";
			PreparedStatement pStmt = con.prepareStatement(sql);
			pStmt.setString(1, user);
			pStmt.setString(2, transformarContra(pass));

			ResultSet rs = pStmt.executeQuery();

			if (rs.next()) {
				String userType = rs.getString("type");

				if (userType.equals("admin")) {
					connBDAdmin();
				}

				return userType;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Estableix la connexió com a administrador.
	 */
	public void connBDAdmin() {
		try {
			llegirXML("admin.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Tanca la connexió amb la base de dades.
	 */

	public void tancarConn() {
		try {
			if (con != null && !con.isClosed()) {
				con.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Obre connexió amb la base de dades.
	 */
	public void obrirConexion() {
		try {
			llegirXML("client.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Transforma la contrasenya en un hash
	 * MD5.
	 * 
	 * @param pass Contrasenya a transformar.
	 * @return hash de la contrasenya.
	 */
	public String transformarContra(String pass) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] messageDigest = md.digest(pass.getBytes());
			StringBuilder hexString = new StringBuilder();

			for (byte b : messageDigest) {
				hexString.append(String.format("%02x", b));
			}

			return hexString.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Executa una consulta SQL i retorna el resultat com a cadena de text.
	 * 
	 * @param consulta Consulta SQL a executar.
	 * @param userType Tipus d'usuari que realitza la consulta.
	 * @return Resultat de la consulta com a cadena de text.
	 */
	public String executarConsulta(String consulta, String userType) {
	    if (consulta == null || consulta.trim().isEmpty()) {
	        return "La consulta SQL no pot estar buida.";
	    }
	    try {
	        PreparedStatement pStmt = con.prepareStatement(consulta);
	        if (consulta.toUpperCase().startsWith("SELECT")) {
				ResultSet rs = pStmt.executeQuery();
				ResultSetMetaData rsmd = rs.getMetaData();
				int columnCount = rsmd.getColumnCount();
				StringBuilder result = new StringBuilder();

				for (int i = 1; i <= columnCount; i++) {
					result.append(rsmd.getColumnName(i)).append("\t");
				}
				result.append("\n");

				while (rs.next()) {
					for (int i = 1; i <= columnCount; i++) {
						result.append(rs.getString(i)).append("\t");
					}
					result.append("\n");
				}

				return result.toString();
	        } else if ((consulta.toUpperCase().startsWith("INSERT") || consulta.toUpperCase().startsWith("DELETE") || consulta.toUpperCase().startsWith("UPDATE"))
	                && esAdmin(userType)) {
	            if (consulta.toUpperCase().contains("USERS")) {
	                return "Acció no permesa: la taula 'users' no pot ser modificada.";
	            }
	            int dialogResult = JOptionPane.showConfirmDialog(null, "¿Estás segur/a?");
	            if (dialogResult == JOptionPane.YES_OPTION) {
	                try {
	                    int registrosAfectados = pStmt.executeUpdate();
	                    return "Consulta exitosa.\nRegistres afectats: " + registrosAfectados;
	                } catch (SQLException e) {
	                    return "Error al executar la consulta.";
	                }
	            } else {
	                return "Consulta cancelada per l'usuari.";
	            }
	        } else if (!consulta.toUpperCase().startsWith("INSERT") && !consulta.toUpperCase().startsWith("DELETE")
	                && !consulta.toUpperCase().startsWith("UPDATE") && !consulta.toUpperCase().startsWith("SELECT")) {
	            return "Error de sintaxi en la consulta SQL";
	        } else {
	            return "Acció no permesa per a l'usuari actual.";
	        }
	    } catch (SQLSyntaxErrorException e) {
	        return "Error de sintaxi en la consulta SQL";
	    } catch (SQLException e) {
	        StringBuilder errorMessage = new StringBuilder("Error al executar la consulta.");
	        if (e.getMessage().equals("No tables used")) {
	            errorMessage.append("\nNo n'hi han taules en la consulta.");
	        }
	        return errorMessage.toString();
	    }
	}

	/**
	 * Verifica si un usuari és d'administrador.
	 * 
	 * @param userType Tipus d'usuari a verificar.
	 * @return true si és administrador, false en cas contrari.
	 */
	private boolean esAdmin(String userType) {
		return userType.equals("admin");
	}

}

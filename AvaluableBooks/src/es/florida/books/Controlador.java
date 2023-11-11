package es.florida.books;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

/**
 * Controlador que gestiona les interaccions entre la vista i el model.
 */
public class Controlador {
	private Model model;
	private Vista vista;
	private ActionListener actionListenerBtnIniciarSesio;
	private ActionListener actionListenerBtnExecutar;
	private String userType;
	private ActionListener actionListenerBtnTancarSesio;
	private ActionListener actionListenerBtnTancarConnexio;
	private ActionListener actionListenerBtnObrirConnexio;
	private ActionListener actionListenerBtnEixir;

	/**
	 * Verifica si el tipus d'usuari és administrador.
	 * 
	 * @param userType El tipus d'usuari a verificar.
	 * @return true si és administrador, false si és d'un altre tipus.
	 */
	private static boolean esAdmin(String userType) {
		return userType.equals("admin");
	}

	/**
	 * Constructor del Controlador.
	 * 
	 * @param vista La vista associada al controlador.
	 * @param model El model associat al controlador.
	 */
	Controlador(Vista vista, Model model) {
		this.vista = vista;
		this.model = model;
		control();
	}

	/**
	 * Configura els ActionListeners per als diferents botons de la interfície.
	 */
	public void control() {
		// ActionListener per al botó d'iniciar sessió
		actionListenerBtnIniciarSesio = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nomUsuari = vista.getTextUsuari();
				String contrasenya = vista.getTextContra();

				userType = model.comprovarConn(nomUsuari, contrasenya);

				if (userType != null && ("admin".equals(userType) || "client".equals(userType))) {
					vista.mostrarMissatge("Éxit.\nIniciant Sesió com: " + userType);
					vista.getBtnIniciarSesio().setVisible(false);
					vista.getUsuari().setEditable(false);
					vista.getContra().setEditable(false);
					vista.getLblTipus().setText(userType);
					vista.mostrarTot();
					if (esAdmin(userType)) {
						vista.getBtnTancarConn().setVisible(true);
						vista.getBtnObrirConn().setVisible(false);
					} else {
						vista.getBtnTancarConn().setVisible(false);
						vista.getBtnObrirConn().setVisible(false);
					}
				} else {
					vista.mostrarMissatge("Credencials incorrectes\nUsuari i/o contrasenya incorrecta");
				}
			}
		};
		// ActionListener per al botó d'executar consulta
		actionListenerBtnExecutar = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String consulta = vista.getTextConsulta();
				try {
					String resultado = model.executarConsulta(consulta, userType);

					if (resultado.startsWith("Error")) {
						JOptionPane.showMessageDialog(null, resultado);
					} else if (consulta.startsWith("SELECT")) {
						String[] lines = resultado.split("\n");
						String[] columnNames = lines[0].split("\t");

						vista.getTableModel().setColumnIdentifiers(columnNames);

						vista.getTableModel().setRowCount(0);

						for (int i = 1; i < lines.length; i++) {
							String[] row = lines[i].split("\t");
							vista.getTableModel().addRow(row);
						}
					} else {
						vista.mostrarMissatge(resultado);
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Error al executar la consulta: " + ex.getMessage());
				}
			}
		};
		// ActionListener per al botó de tancar sessió
		actionListenerBtnTancarSesio = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog(null, "Estás segur de que vols tancar la sesió?");
				if (dialogResult == JOptionPane.YES_OPTION) {
					userType = null;
					vista.ocultarTot();
					vista.getBtnIniciarSesio().setVisible(true);
					vista.getUsuari().setText("");
					vista.getContra().setText("");
					vista.getUsuari().setEditable(true);
					vista.getContra().setEditable(true);
					vista.getConsulta().setText("");
					vista.getTableModel().setRowCount(0);
					vista.mostrarMissatge("Sesió tancada.");
				}
			}
		};
		// ActionListener per al botó de tancar connexió
		actionListenerBtnTancarConnexio = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog(null, "Estás segur de que vols tancar la connexió?");
				if (dialogResult == JOptionPane.YES_OPTION) {
					model.tancarConn();
					vista.ocultarTot();
					vista.getUsuari().setText("");
					vista.getContra().setText("");
					vista.getBtnObrirConn().setVisible(true);
					vista.mostrarMissatge("Sesió Tancada\nConnexió a la base de dades tancada.");
					vista.getBtnTancarConn().setVisible(false);
				}
			}
		};
		// ActionListener per al botó d'obrir connexió
		actionListenerBtnObrirConnexio = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog(null, "Estás segur de que vols obrir la connexió?");
				if (dialogResult == JOptionPane.YES_OPTION) {
					model.obrirConexion();
					vista.mostrarMissatge("Conexió a la base de dades oberta.");
					vista.getBtnObrirConn().setVisible(false);
					vista.getBtnTancarConn().setVisible(true);

					userType = null;
					vista.ocultarTot();
					vista.getBtnIniciarSesio().setVisible(true);
					vista.getUsuari().setText("");
					vista.getContra().setText("");
					vista.getUsuari().setEditable(true);
					vista.getContra().setEditable(true);
					vista.getConsulta().setText("");
					vista.getTableModel().setRowCount(0);
				}
			}
		};
		actionListenerBtnEixir = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (vista.getBtnTancarSesio().isVisible()) {
					int dialogResult = JOptionPane.showConfirmDialog(null,
							"Estás segur de que vols eixir?\nEs tancará la teua sessió.");
					if (dialogResult == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
				} else {
					int dialogResult = JOptionPane.showConfirmDialog(null, "Vols eixir?");
					if (dialogResult == JOptionPane.YES_OPTION) {
						System.exit(0);
					}
				}
			}
		};

		// Assignar ActionListeners als botons de la interfície

		vista.getBtnIniciarSesio().addActionListener(actionListenerBtnIniciarSesio);
		vista.getBtnExecutar().addActionListener(actionListenerBtnExecutar);
		vista.getBtnTancarSesio().addActionListener(actionListenerBtnTancarSesio);
		vista.getBtnTancarConn().addActionListener(actionListenerBtnTancarConnexio);
		vista.getBtnObrirConn().addActionListener(actionListenerBtnObrirConnexio);
		vista.getBtnEixir().addActionListener(actionListenerBtnEixir);
	}
}

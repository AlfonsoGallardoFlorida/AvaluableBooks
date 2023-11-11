package es.florida.books;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.MouseEvent;

import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class Vista {

	private JFrame frmA;
	private JTextField txtUsuari;
	private JButton btnIniciarSesio;
	private JLabel lblUsuari;
	private JLabel lblContra;
	private JButton btnExecutar;
	private JTextField txtConsulta;
	private JLabel lblConsulta;
	private JPasswordField txtContra;
	private JButton btnTancarSesio;
	private JButton btnTancarConnexio;
	private JTable taula;
	private JScrollPane scrollPane;
	private DefaultTableModel tableModel;
	private JButton btnObrirConnexio;
	private JLabel lblConnectat;
	private JLabel lblType;
	private JButton btnEixir;
	public Vista() {
		initialize();
		ocultarTot();
	}

	private void initialize() {
		frmA = new JFrame();
		frmA.setTitle("Llibreria de llibres");
		frmA.setBounds(100, 100, 1108, 331);
		frmA.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmA.getContentPane().setLayout(null);

		lblUsuari = new JLabel("Nom d'usuari");
		lblUsuari.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblUsuari.setBounds(30, 57, 96, 29);
		frmA.getContentPane().add(lblUsuari);

		lblContra = new JLabel("Contrasenya");
		lblContra.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblContra.setBounds(30, 151, 96, 29);
		frmA.getContentPane().add(lblContra);

		txtUsuari= new JTextField();
		txtUsuari.setBounds(30, 96, 96, 19);
		frmA.getContentPane().add(txtUsuari);
		txtUsuari.setColumns(10);

		btnIniciarSesio = new JButton("Iniciar Sesió");
		btnIniciarSesio.setBounds(22, 233, 116, 21);
		frmA.getContentPane().add(btnIniciarSesio);

		btnExecutar = new JButton("Executar");
		btnExecutar.setBounds(269, 233, 116, 21);
		frmA.getContentPane().add(btnExecutar);

		txtConsulta = new JTextField();
		txtConsulta.setColumns(10);
		txtConsulta.setBounds(269, 204, 469, 19);
		frmA.getContentPane().add(txtConsulta);

		lblConsulta = new JLabel("Consulta:");
		lblConsulta.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblConsulta.setBounds(196, 197, 74, 29);
		frmA.getContentPane().add(lblConsulta);

		txtContra= new JPasswordField();
		txtContra.setBounds(30, 191, 96, 19);
		frmA.getContentPane().add(txtContra);

		btnTancarSesio = new JButton("Tancar Sesió");
		btnTancarSesio.setBounds(22, 233, 116, 21);
		frmA.getContentPane().add(btnTancarSesio);

		btnTancarConnexio = new JButton("Tancar Connexió");
		btnTancarConnexio.setBounds(22, 263, 130, 21);
		frmA.getContentPane().add(btnTancarConnexio);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(269, 10, 815, 170);
		frmA.getContentPane().add(scrollPane);
		
		
		
		tableModel = new DefaultTableModel() {
			private static final long serialVersionUID = 1L;
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		};
		taula = new JTable(tableModel) {
			private static final long serialVersionUID = 1L;
			@Override
			public String getToolTipText(MouseEvent e) {
				int row = rowAtPoint(e.getPoint());
				int column = columnAtPoint(e.getPoint());
				Object value = getValueAt(row, column);
				if (value == null) {
					return null;
				} else {
					String tooltipText = value.toString();
					return "<html><p width=\"200\">" + tooltipText + "</p></html>";
				}
			}
		};

		scrollPane.setViewportView(taula);
		taula.getTableHeader().setReorderingAllowed(false);
		
		btnObrirConnexio = new JButton("Obrir Connexió");
		btnObrirConnexio.setBounds(22, 264, 130, 21);
		frmA.getContentPane().add(btnObrirConnexio);
		
		lblConnectat = new JLabel("Connectat com:");
		lblConnectat.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblConnectat.setBounds(748, 203, 107, 16);
		frmA.getContentPane().add(lblConnectat);
		
		lblType = new JLabel("type");
		lblType.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblType.setBounds(865, 203, 107, 16);
		frmA.getContentPane().add(lblType);
		
		btnEixir = new JButton("Eixir");
		btnEixir.setBounds(999, 203, 85, 21);
		frmA.getContentPane().add(btnEixir);

		this.frmA.setVisible(true);

	}

	public String getTextUsuari() {
		return txtUsuari.getText();
	}

	@SuppressWarnings("deprecation")
	public String getTextContra() {
		return txtContra.getText();
	}

	public String getTextConsulta() {
		return txtConsulta.getText();
	}

	public JTextField getUsuari() {
		return txtUsuari;
	}

	public JTextField getContra() {
		return txtContra;
	}

	public JTextField getConsulta() {
		return txtConsulta;
	}

	public JLabel getLblConsulta() {
		return lblConsulta;
	}

	public JButton getBtnIniciarSesio() {
		return btnIniciarSesio;
	}

	public JButton getBtnExecutar() {
		return btnExecutar;
	}

	public void mostrarMissatge(String mensaje) {
		JOptionPane.showMessageDialog(null, mensaje);
	}

	public JTable getTaula() {
		return taula;
	}

	public DefaultTableModel getTableModel() {
		return tableModel;
	}

	public JScrollPane getScrollPanel() {
		return scrollPane;
	}

	public JButton getBtnTancarSesio() {
		return btnTancarSesio;
	}

	public JButton getBtnTancarConn() {
		return btnTancarConnexio;
	}

	public JButton getBtnObrirConn() {
		return btnObrirConnexio;
	}
	public JLabel getLblTipus() {
		return lblType;
	}
	public JButton getBtnEixir() {
		return btnEixir;
	}
	public void ocultarTot() {
		btnExecutar.setVisible(false);
		txtConsulta.setVisible(false);
		taula.setVisible(false);
		lblConsulta.setVisible(false);
		scrollPane.setVisible(false);
		btnTancarSesio.setVisible(false);
		btnTancarConnexio.setVisible(false);
		btnObrirConnexio.setVisible(false);
		lblConnectat.setVisible(false);
		lblType.setVisible(false);
	}

	public void mostrarTot() {
		btnExecutar.setVisible(true);
		txtConsulta.setVisible(true);
		taula.setVisible(true);
		lblConsulta.setVisible(true);
		scrollPane.setVisible(true);
		btnTancarSesio.setVisible(true);
		btnTancarConnexio.setVisible(true);
		lblConnectat.setVisible(true);
		lblType.setVisible(true);
	}
}

package application;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;

/**
 * Fenêtre obligatoire pour les applications qui utilisent les touches du clavier
 * 
 * @author Caroline Houle
 */
public class FenetreTouchesClavier extends JFrame {
	/**Identifiant de classe **/
	private static final long serialVersionUID = 1L;
	/**Panneau qui contient les informations **/
	private JPanel contentPane;
	/**Le JTable qui contient l'information des touches. **/
	private JTable table;
	
	/**
	 * Lancement de la fenêtre 
	 * @param args argument de création d'application
	 */
	//Caroline Houle
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FenetreTouchesClavier frame = new FenetreTouchesClavier();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Création de la fenêtre
	 * 
	 */
	//Caroline Houle
	public FenetreTouchesClavier() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 351, 210);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		contentPane.setLayout(null);
		
		JLabel lblTitre = new JLabel("Utilisastion des touches du clavier");
		lblTitre.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblTitre.setHorizontalAlignment(SwingConstants.LEFT);
		lblTitre.setBounds(38, 26, 244, 17);
		contentPane.add(lblTitre);
		
		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		//
		//si plus que 3 lignes, agrandir le composant sur la fenêtre!
		//
		
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{"backspace", "supprimer un obstacle sélectionné"},
				{"clic droit", "Changer la couleur obstacle/balle"},
			},
			new String[] {
				"New column", "New column"
			}
		) {
			/**
			 * Identifiant de classe
			 */
			private static final long serialVersionUID = 1L;
			Class[] columnTypes = new Class[] {
				String.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(30);
		table.setBounds(10, 92, 317, 50);
		contentPane.add(table);
		
		JLabel lblTitreCol1 = new JLabel("Touche");
		lblTitreCol1.setForeground(Color.BLUE);
		lblTitreCol1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTitreCol1.setBounds(38, 67, 46, 14);
		contentPane.add(lblTitreCol1);
		
		JLabel lblTitreCol2 = new JLabel("Effet");
		lblTitreCol2.setForeground(Color.BLUE);
		lblTitreCol2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTitreCol2.setBounds(149, 67, 46, 14);
		contentPane.add(lblTitreCol2);

	}
}

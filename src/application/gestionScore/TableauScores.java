package application.gestionScore;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import application.configuration.Theme;
import gestionFichiers.GestionnaireFichiers;

/**
 * Cette classe représente le tableau des cinq meilleurs scores de l'utilisateur. À l'ajout d'un score, 
 * il est mis à jour et la moins bonne sera supprimée.
 * @author Elias Kassas
 */
public class TableauScores extends JFrame {
	/** Le coefficient de sérialisation. **/
	private static final long serialVersionUID = 1L;
	/** La zone de regroupement principale. **/
	private JPanel contentPane ;
	/** La table contenant toutes les informations concernant les scores. **/
	private JTable tableScores ;
	/** La liste contenant les objets Score triés par ordre de leur score obtenu. **/
	private ArrayList<Score> tableauScore ;
	/** La taille maximale de la liste des objets Score triée. **/
	private int TAILLE_MAX = 5 ; 
	/** Gestionnaire de fichiers pour récuperer le thème voulu de l'application. **/
	private GestionnaireFichiers gest ;
	/** Liste des composants. **/
	private ArrayList<Component> lesComposants ;
	/** Nom de la police. **/
	private String nomPolice ;

	/**
	 * Créer l'interface.
	 */
	// Par Elias Kassas
	public TableauScores() {
		gest = new GestionnaireFichiers() ;
		tableauScore = new ArrayList<Score>() ;
		lesComposants = new ArrayList<Component>() ;
		
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 770, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitre = new JLabel("Meilleurs scores");
		lblTitre.setBounds(6, 6, 758, 16);
		lblTitre.setFont(new Font("SimSong", Font.PLAIN, 20));
		lblTitre.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblTitre);
		lesComposants.add(lblTitre);

		tableauScore = gest.obtenirLesScores() ;
		
		tableScores = new JTable();
		tableScores.setFont(new Font(nomPolice, Font.PLAIN, 12));
		tableScores.setBounds(6, 31, 758, 109);
		tableScores.setModel(new DefaultTableModel(
				new Object[][] {
					{"Position sur votre podium", "Date et heure", "Nombre de points"},
					{1, tableauScore.get(0).getDateEtHeure(), tableauScore.get(0).getValeurScore()},
					{2, tableauScore.get(1).getDateEtHeure(), tableauScore.get(1).getValeurScore()},
					{3, tableauScore.get(2).getDateEtHeure(), tableauScore.get(2).getValeurScore()},
					{4, tableauScore.get(3).getDateEtHeure(), tableauScore.get(3).getValeurScore()},
					{5, tableauScore.get(4).getDateEtHeure(), tableauScore.get(4).getValeurScore()},
				},
				new String[] {
						"Position sur votre podium", "Date et heure", "Nombre de points"
				}) {
			private static final long serialVersionUID = 2664729906411306407L;
			Class[] columnTypes = new Class[] {
				String.class, Object.class, String.class
			};
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
			boolean[] columnEditables = new boolean[] {
				false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tableScores.getColumnModel().getColumn(0).setResizable(false);
		tableScores.getColumnModel().getColumn(1).setResizable(false);
		tableScores.getColumnModel().getColumn(2).setResizable(false);
		tableScores.setSurrendersFocusOnKeystroke(true);
		contentPane.add(tableScores);
		lesComposants.add(tableScores);
	}

	/**
	 * Méthode permettant d'ajouter un score à la liste des scores.
	 * @param score : Le score à ajouter.
	 */
	// Par Elias Kassas
	public void ajouterUnScore(Score score) {
		tableauScore.add(score) ;
		Collections.sort(tableauScore) ;

		int i = tableauScore.size() - 1 ;
		while (tableauScore.size() > TAILLE_MAX) {
			tableauScore.remove(i) ;
			i-- ;
		}
		
		gest.modifierLesScores(tableauScore) ;
		
		if (tableauScore.size() >= TAILLE_MAX && tableScores != null) {
			tableScores.setModel(new DefaultTableModel(
					new Object[][] {
						{"Position sur votre podium", "Date et heure", "Nombre de points"},
						{1, tableauScore.get(0).getDateEtHeure(), tableauScore.get(0).getValeurScore()},
						{2, tableauScore.get(1).getDateEtHeure(), tableauScore.get(1).getValeurScore()},
						{3, tableauScore.get(2).getDateEtHeure(), tableauScore.get(2).getValeurScore()},
						{4, tableauScore.get(3).getDateEtHeure(), tableauScore.get(3).getValeurScore()},
						{5, tableauScore.get(4).getDateEtHeure(), tableauScore.get(4).getValeurScore()},
					},
					new String[] {
							"Position sur votre podium", "Date et heure", "Nombre de points"
					}
					) {
				Class[] columnTypes = new Class[] {
						String.class, Object.class, String.class
				};
				public Class getColumnClass(int columnIndex) {
					return columnTypes[columnIndex];
				}
				boolean[] columnEditables = new boolean[] {
						false, false, false
				};
				public boolean isCellEditable(int row, int column) {
					return columnEditables[column];
				}
			});
		}
	}
	
	/**
	 * Méthode privée servant à appliquer un thème au tableau des scores.
	 */
	// Par Elias Kassas
	public void appliquerTheme(Theme theme) {
		for (Component composant : lesComposants) {
			Font policeAct = composant.getFont() ;
			if (policeAct == null)
				revalidate() ;
			else
				composant.setFont(new Font(policeAct.getName() /*nomPolice*/, policeAct.getStyle(), 
						policeAct.getSize())) ;
			if (!(composant instanceof JMenuItem) && !(composant instanceof JMenu) && 
					!(composant instanceof JMenuBar)) {
				composant.setForeground(theme.getCouleurPolice()) ;
			}

			if (composant instanceof JPanel) {
				composant.setBackground(theme.getCouleurSecondaire()) ;
				((JComponent) composant).setBorder(new LineBorder(theme.getCouleurTertiaire(), 
						1, true)) ;
			}
		}

		contentPane.setBackground(theme.getCouleurPrimaire()) ;
		//gest.modifierThemeActuel(theme) ;
		revalidate() ;
		repaint() ;
	}
	
	
}

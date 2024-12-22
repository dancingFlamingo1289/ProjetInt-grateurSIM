package application;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import application.configuration.Theme;
import application.gestionScore.Score;
import application.gestionScore.TableauScores;
import application.logo.Logo;
import gestionFichiers.GestionnaireFichiers;
import instructions.FenetreInstructions;
/**
 * Menu de FrissonBoum servant d'interface de départ avant de jouer.
 * Menu est la classe fille de JFrame.
 * 
 * @author Aimé Melançon
 * @author Elias Kassas
 */
public class AppPrincipale36 extends JFrame {
	/** Identifiant de classe. **/
	private static final long serialVersionUID = 1L;
	/**Le panneau englobant tout composant du menu. **/
	private JPanel contentPane;
	/** L'étiquette qui représente le titre du jeu. **/
	private JLabel lblTitreJeu;
	/** Le bouton pour jouer à la simulation. **/
	private JButton btnJouer;
	/** Le bouton pour charger un niveau puis jouer à celui-ci. **/
	private JButton btnChargerEtJouer;
	/** Le bouton pour voir le tableau des records qui va peut-être caché un oeuf de pâque ? **/
	private JButton btnTableauDesRecords;
	/** Le bouton pour afficher les instructions de comment utiliser la simulation. **/
	private JButton btnInstruction;
	/** Application à ouvrir. **/
	private Application app ;
	/** Fenêtre d'instructions de l'application. **/
	private FenetreInstructions fenetreInstructions ;
	/** Gestionnaire de fichier. **/
	private GestionnaireFichiers gest ;
	/** Liste contenant tous les composants de l'application pour l'application d'un thème. **/
	private ArrayList<Component> lesComposants ;
	/** Fenêtre des meilleurs scores. **/
	private TableauScores tableauScores ;

	/**
	 * Lancement de la fenêtre du menu.
	 * @param args L'argument de lancement
	 */
	//Aimé Melançon
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppPrincipale36 frame = new AppPrincipale36();
					frame.setVisible(true);
					frame.requestFocusInWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Création du panneau du menu.
	 */
	//Aimé Melançon
	public AppPrincipale36() {
		lesComposants = new ArrayList<Component>() ;
		gest = new GestionnaireFichiers() ;
		
		setTitle("FrissonBoum");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 507, 635);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		lblTitreJeu = new JLabel("FrissonBoum : Le frisson de la victoire !");
		lblTitreJeu.setFont(new Font("Rockwell Extra Bold", Font.PLAIN, 20));
		lblTitreJeu.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitreJeu.setBounds(9, 10, 472, 73);
		contentPane.add(lblTitreJeu);
		enregistrerUnComposant(lblTitreJeu) ;

		btnJouer = new JButton("JOUER");
		btnJouer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				creerEtAfficherAppPrincipale() ;
			}
		});
		btnJouer.setFont(new Font("ROG Fonts", Font.PLAIN, 20));
		btnJouer.setBounds(112, 260, 265, 73);
		contentPane.add(btnJouer);
		enregistrerUnComposant(btnJouer) ;

		btnChargerEtJouer = new JButton("Charger un niveau et jouer !");
		btnChargerEtJouer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				creerEtAfficherAppPrincipaleAvecNiveau() ;
			}
		});
		btnChargerEtJouer.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnChargerEtJouer.setBounds(112, 342, 265, 73);
		contentPane.add(btnChargerEtJouer);
		enregistrerUnComposant(btnChargerEtJouer) ;

		btnTableauDesRecords = new JButton("Tableau des records");
		btnTableauDesRecords.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				creerEtAfficherMeilleursScores() ;
			}
		});
		btnTableauDesRecords.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnTableauDesRecords.setBounds(112, 427, 265, 73);
		contentPane.add(btnTableauDesRecords);
		enregistrerUnComposant(btnTableauDesRecords) ;
		
		fenetreInstructions = new FenetreInstructions() ;
		enregistrerUnComposant(fenetreInstructions) ;
		
		btnInstruction = new JButton("Instructions");
		btnInstruction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fenetreInstructions.setVisible(true);
			}
		});
		btnInstruction.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnInstruction.setBounds(112, 512, 265, 73);
		contentPane.add(btnInstruction);
		enregistrerUnComposant(btnInstruction) ;
	
		Logo logo = new Logo();
		logo.setBounds(14, 93, 462, 155);
		contentPane.add(logo);
		enregistrerUnComposant(logo) ;
		
		tableauScores = new TableauScores() ;
		enregistrerUnComposant(tableauScores) ;
		
		appliquerTheme(gest.obtenirThemeActuel()) ;
	}

	/**
	 * Méthode privée servant à appliquer un thème au menu.
	 */
	// Par Elias Kassas
	public void appliquerTheme(Theme theme) {
		for (Component composant : lesComposants) {
			Font policeAct = composant.getFont() ;
			if (policeAct == null)
				revalidate() ;
			else
				composant.setFont(new Font(policeAct.getName() /*nomPolice*/, policeAct.getStyle(), policeAct.getSize())) ;
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

		tableauScores.appliquerTheme(theme) ;
		contentPane.setBackground(theme.getCouleurPrimaire()) ;
		//gest.modifierThemeActuel(theme) ;
		revalidate() ;
		repaint() ;
	}
	
	/**
	 * Méthode servant à ajouter un composant dans la liste des composants pour la modification.
	 * @param composant : Le composant à ajouter dans la liste.
	 */
	// Par Elias Kassas
	private void enregistrerUnComposant(Component composant) {
		lesComposants.add(composant) ;
	}
	
	/**
	 * Méthode privée servant à appliquer un thème au menu.
	 */
	// Par Elias Kassas
	/*public void appliquerTheme(InfoConfiguration ic) {
		for (Component composant : lesComposants) {
			Font policeAct = composant.getFont() ;
			if (policeAct == null)
				revalidate() ;
			else {
				composant.setFont(new Font(ic.getPoliceDeCaractere(), policeAct.getStyle(), policeAct.getSize())) ;
				composant.setForeground(ic.getTheme().getCouleurPolice()) ;
			}

			if (composant instanceof JPanel) {
				composant.setBackground(ic.getTheme().getCouleurSecondaire()) ;
				((JComponent) composant).setBorder(new LineBorder(ic.getTheme().getCouleurTertiaire(), 
						1, true)) ;
			}
		}

		contentPane.setBackground(ic.getTheme().getCouleurPrimaire()) ;
		//gest.modifierThemeActuel(ic.getTheme()) ;
		revalidate() ;
		repaint() ;
	}*/
	
	/**
	 * Méthode privée permettant de créer et afficher l'application principale
	 * et disposer de la fenêtre de menu.
	 */
	// Par Elias Kassas
	private void creerEtAfficherAppPrincipale() {
		if (app == null) {
			requestFocusInWindow();
			app = new Application() ;
			app.setVisible(true) ;
			// On enlève le menu du champ de vision de l'utilisateur.
			dispose() ;
		}
	}

	/**
	 * Méthode privée permettant de créer et afficher l'application principale avec un fichier de
	 * table prétéléchargé.
	 */
	// Par Elias Kassas
	private void creerEtAfficherAppPrincipaleAvecNiveau() {
		JFileChooser jfc = new JFileChooser() ;
		jfc.setCurrentDirectory(gest.getDossierUtilisateur()) ;
		int resultat = jfc.showDialog(null, "Choisir") ;
		
		if (resultat == JFileChooser.APPROVE_OPTION) {
			File fichierTable = jfc.getSelectedFile() ;
			app = new Application() ;
			//JOptionPane.showMessageDialog(null, "On charge le fichier du niveau !") ;
			app.setVisible(true) ;
			app.chargerUneTable(fichierTable) ;
			dispose() ;
		}
	}
	
	/**
	 * Méthode privée permettant de créer et afficher la fenêtre de configuration
	 * sans "supprimer" la fenêtre de menu.
	 */
	// Par Elias Kassas
	private void creerEtAfficherMeilleursScores() {
		tableauScores.revalidate() ;
		tableauScores.setVisible(true) ;
	}
	
	/**
	 * Méthode permettant d'ajouter à l'interface du tableau des meilleurs scores un score.
	 * @param scoreAAjouter : Le score à ajouter à l'interface des meilleurs scores.
	 */
	// Par Elias Kassas
	public void ajouterScore(Score scoreAAjouter) {
		tableauScores.ajouterUnScore(scoreAAjouter) ;
	}
	
	
}

package application;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeSupport;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import gestionFichier.GestionnaireFichier;
import instructions.FenetreInstructions;
/**Menu de FrissonBoum servant d'interface de départ avant de jouer.
 * Menu est la classe fille de JFrame.
 * 
 * @author Aimé Melançon
 */
public class Menu extends JFrame {
	/** Identifiant de classe. **/
	private static final long serialVersionUID = 1L;
	/**Le panneau englobant tout composant du menu. **/
	private JPanel cPMenu;
	/** Un bouton pour illustrer le logo et peut-être un oeuf de pâque prochainement aussi ? **/
	private JButton btnLogo;
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
	private AppPrincipale36 app ;
	/** Fenêtre d'instructions de l'application. **/
	private FenetreInstructions fenetreInstructions ;
	/** Gestionnaire de fichier. **/
	private GestionnaireFichier gest ;

	/**
	 * Lancement de la fenêtre du menu.
	 * @param args L'argument de lancement
	 */
	//Aimé Melançon
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Menu frame = new Menu();
					frame.setVisible(true);
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
	public Menu() {
		setTitle("menu.FrissonBoum");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 496, 619);
		cPMenu = new JPanel();
		cPMenu.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(cPMenu);
		cPMenu.setLayout(null);

		btnLogo = new JButton("LOGO");
		btnLogo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnLogo.setFont(new Font("Serif", Font.BOLD, 40));
		btnLogo.setBounds(52, 93, 391, 104);
		cPMenu.add(btnLogo);

		lblTitreJeu = new JLabel("FrissonBoum : Le frisson de la victoire !");
		lblTitreJeu.setFont(new Font("Rockwell Extra Bold", Font.PLAIN, 20));
		lblTitreJeu.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitreJeu.setBounds(10, 10, 472, 73);
		cPMenu.add(lblTitreJeu);

		btnJouer = new JButton("JOUER");
		btnJouer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				creerEtAfficherAppPrincipale() ;
			}
		});
		btnJouer.setFont(new Font("ROG Fonts", Font.PLAIN, 20));
		btnJouer.setBounds(153, 222, 161, 54);
		cPMenu.add(btnJouer);

		btnChargerEtJouer = new JButton("Charger un niveau et jouer !");
		btnChargerEtJouer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				creerEtAfficherAppPrincipaleAvecNiveau() ;
			}
		});
		btnChargerEtJouer.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnChargerEtJouer.setBounds(52, 300, 391, 73);
		cPMenu.add(btnChargerEtJouer);

		btnTableauDesRecords = new JButton("Tableau des records");
		btnTableauDesRecords.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnTableauDesRecords.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnTableauDesRecords.setBounds(111, 389, 265, 73);
		cPMenu.add(btnTableauDesRecords);

		fenetreInstructions = new FenetreInstructions() ;
		
		btnInstruction = new JButton("Instructions");
		btnInstruction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fenetreInstructions.setVisible(true);
			}
		});
		btnInstruction.setFont(new Font("Times New Roman", Font.PLAIN, 20));
		btnInstruction.setBounds(153, 499, 161, 54);
		cPMenu.add(btnInstruction);
	}

	/**
	 * Méthode privée permettant de créer et afficher l'application principale
	 * et disposer de la fenêtre de menu.
	 */
	// Par Elias Kassas
	private void creerEtAfficherAppPrincipale() {
		if (app == null) {
			app = new AppPrincipale36() ;
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
		int resultat = jfc.showDialog(null, "Choisir") ;
		
		if (resultat == JFileChooser.APPROVE_OPTION) {
			File fichierTable = jfc.getSelectedFile() ;
			app = new AppPrincipale36() ;
			JOptionPane.showMessageDialog(null, "On charge le fichier du niveau !") ;
			app.setVisible(true) ;
			app.chargerUneTable(fichierTable) ;
			dispose() ;
		}
	}
}

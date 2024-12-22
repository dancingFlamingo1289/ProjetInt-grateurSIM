package application;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

import application.appliSatellite.AppSatelliteElias;
import composantDeJeu.Balle;
import composantDeJeu.Jauge;
import composantDeJeu.Table;
import gestionFichier.GestionnaireFichier;
import instructions.FenetreInstructions;
import instructions.PanelAPropos;
import math.vecteurs.Vecteur3D;
import obstacles.Obstacle;
import physique.MoteurPhysique;
import scene.Scene;
import statistiques.graphique.Graphique ;
import statistiques.schemaDesForces.SchemaDesForces;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

/**
 * Application scientifique permettant de simuler un jeu de flipper tout en pouvant voir la 
 * physique derrière.
 * Classe qui dérive de JFrame.
 * @author Aimé Melançon
 * @author Elias Kassas
 */
public class AppPrincipale36 extends JFrame {
	/**  Identifiant de classe **/
	private static final long serialVersionUID = 1L;
	/** Variable de type JPanel qui instancie la fenêtre principale.**/
	private JPanel cPPRincipale;
	/** L'item de menu lié à la page d'instructions d'utilisation de l'application. **/
	private JMenuItem menuItemInstructions;
	/** Le menuItem qui contient le a propos. **/
	private JMenuItem menuItemAPropos;
	/**Étiquette du titre de temps simulé **/
	private JLabel lblTempsSimulé;
	/**Variable de type JLabel qui permet de voir le temps simulé.**/
	private JLabel lblTemps;
	/**Variable de type JLabel affichant l'unité de secondes.**/
	private JLabel lblSec;
	/**Variable de type JButton qui permet d'accéder au mode édition.**/
	private JToggleButton btnModeEdition;
	/**Variable de type JSpinner qui permet de modifier la constante de rappel.**/
	private JSpinner spnK;
	/**Variable de type Graphique qui montre l'énergie en fonction du temps.**/
	private Graphique graphiqueEnergieFonctionTemps;
	/**Variable de type Graphique qui montre la vitesse en fonction du temps.**/
	private Graphique graphiqueVitesseFonctionTemps;
	/**Variable de type Graphique qui montre l'accélération en fonction du temps.**/
	private Graphique graphiqueAccelerationFonctionTemps;
	/**Variable de type Scene affichant la table de jeu.**/
	private Scene scene;
	/**Variable de type JComboBox qui permet de choisir la constante de gravitation.**/
	private JComboBox<Font> cboxG;
	/**Variable de type JComboBox qui permet de choisir le type de bille.**/
	private JComboBox<Font> cBoxBilles;
	/**Variable de type JComboBox qui permet de modifier le type de sol.**/
	private JComboBox<Font> cBoxSol;
	/**Variable permettant de changer la force de lancer de la balle.**/
	private JSpinner spnForces;
	/**Variable permettant de changer la masse.**/
	private JSpinner spnMasse;
	/**Variable permettant de changer l'inclinaison.**/
	private JSpinner spnInclinaison;
	/**Variable de type JLabel permettant d'afficher modification de la constante K. **/
	private JLabel lblModConstanteK;
	/**Variable affichant modification de la constante de gravitation.**/
	private JLabel lblModG;
	/**Variable affichant le titre de modification utilisateur.**/
	private JLabel lblModUtilisateur;
	/**Variable de type JLabel affichant choix de bille.**/
	private JLabel lblChoixBilles;
	/**Variable de type JLabel affichant type de sol.**/
	private JLabel lblTypeSol;
	/**Variable de type JLabel affichant Force de lancer.**/
	private JLabel lblForceLancer;
	/**Variable de type JLabel affichant N.**/
	private JLabel lblNewtons;
	/**Variable de type JLabel affichant Inclinaison.**/
	private JLabel lblInclinaison;
	/**Variable de type JLabel affichant le choix de masse de la balle.**/
	private JLabel lblMasseBalle;
	/**Variable de type JLabel qui affiche kg.**/
	private JLabel lblKg;
	/**Variable de type JPanel qui permet d'afficher un panneau.**/
	private JPanel panelMod;
	/**Variable de type JButton qui permet de lancer la balle.**/
	private JButton btnLancer;
	/**Variable de type Jauge qui permet d'afficher une jauge de puissance.**/
	private Jauge jauge = new Jauge();
	/**Variable de type JButton qui permet d'arrêter ou de continuer l'annimation de la simulation.**/
	private static JToggleButton btnArreterJouer;
	/**Variable de type JButton qui permet de remettre à zéro et recommencer la simulation.**/
	private JButton btnRecommencer;
	/**Variable de type JButton qui permet de trouver un oeuf de pâque ?**/
	private JButton btnF;
	/**Variable de type JButton qui permet d'avancer d'un pas. **/
	private static JButton btnPas;
	/**Variable de type JLabel qui affiche l'énergie. **/
	private JLabel lblEnergie;
	/**Variable de type JLabel qui affiche le nombre de Joule.**/
	private JLabel lblNbJoule;
	/**Variable de type JLabel affichant Somme des force x.**/
	private JLabel lblSommeFX;
	/**Variable de type JLabel affichant le nombre de force appliqué à la balle.**/
	private JLabel lblNbForce;
	/**Variable de type JLabel affichant Vitesse Module.**/
	private JLabel lblVitesseModule;
	/**Variable de type JLabel affichant Vitesse en x.**/
	private JLabel lblVitesseX;
	/**Variable de type JLabel affichant Vitesse en y.**/
	private JLabel lblVitesseY;
	/**Variable de type JLabel affichant la vitesse du module en m/s.**/
	private JLabel lblModuleVit;
	/**Variable de type JLabel montrant la vitesse en x.**/
	private JLabel lblVitX;
	/**Variable de type JLabel montrant la vitesse en y.**/
	private JLabel lblVitY;
	/**Variable de type JLabel affichant accélération.**/
	private JLabel lblAcceleration;
	/**Variable de type JLabel affichant accélération en x.**/
	private JLabel lblAccelerationX;
	/**Variable de type JLabel affichant accélération en y.**/
	private JLabel lblAccelerationY;
	/**Variable de type JLabel montrant l'accélération en m/s^2.**/
	private JLabel lblAccel;
	/**Variable de type JLabel montrant l'accélération en x en m/s^2  **/
	private JLabel lblAccelX;
	/**Variable de type JLabel montrant l'accélération en y en m/s^2**/
	private JLabel lblAccelY;
	/**Variable de type JLabel affichant le nom de l'application plus le slogan.**/
	private JLabel lblTitre;
	/**Variable de type menu pour la catégorie fichier **/	
	private JMenu menuBarFichier;
	/** Variable de type menuItem qui permet de sauvegarder une scène.**/
	private JMenuItem menuItemSauvegarder;
	/**Variable de type menuItem qui permet de charger une autre scène. **/
	private JMenuItem menuItemChargeFichier;
	/**Variable de type menuItem qui permet d'aller dans la configuration. **/
	private JMenuItem menuItemConfiguration;
	/**Variable de type MenuItem qui permet d'aller dans la fenêtre se nommant menu. **/
	private JMenuItem menuItemMenu;
	/**Variable de type RadioButtonmenuItem qui permet de charger la scène préfaite 1. **/
	private JRadioButtonMenuItem rdbtnScene1;
	/**Variable de type RadioButtonmenuItem qui permet de charger la scène préfaite 3. **/
	private JRadioButtonMenuItem rdbtnScene3;
	/**Variable de type RadioButtonmenuItem qui permet de charger la scène préfaite 2. **/
	private JRadioButtonMenuItem rdbtnScene2;
	/** Temps total pris de la scène. **/
	private double tempsTotalScene ;
	/** Fenêtre de configuration de l'application. **/
	private Configuration config = new Configuration() ;
	/**Fenêtre d'instruction de l'application. **/
	private FenetreInstructions fenetreInstructions = new FenetreInstructions();
	private GestionnaireFichier gest ;
	private int nbFoisAppui = 0 ;
	private SchemaDesForces schemaDesForces;

	/**
	 * Lancement de la fenêtre principale.
	 * @param args L'argument de lancement
	 */
	//Aimé Melançon
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppPrincipale36 frame = new AppPrincipale36();
					frame.setVisible(true);
					frame.scene.requestFocusInWindow();
					if (!System.getProperty("os.name").toLowerCase().contains("mac")) {
						UIManager.setLookAndFeel(new javax.swing.plaf.nimbus.NimbusLookAndFeel());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Création du panneau
	 */
	//Aimé Melançon
	public AppPrincipale36() {
		setTitle("36 FrissonBoum");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1554,882);

		gest = new GestionnaireFichier() ;

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menuBarInformation = new JMenu("Information");
		menuBarInformation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
			}
		});
		menuBar.add(menuBarInformation);


		PanelAPropos APropos= new PanelAPropos();

		menuItemAPropos = new JMenuItem("À propos");
		menuItemAPropos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
				JOptionPane.showMessageDialog(null, APropos,"À propos de cette application",
						JOptionPane.PLAIN_MESSAGE) ;
			}
		});
		menuBarInformation.add(menuItemAPropos);

		menuItemInstructions = new JMenuItem("Instructions");
		menuItemInstructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
				fenetreInstructions.setVisible(true);
			}
		});
		menuBarInformation.add(menuItemInstructions);

		menuBarFichier = new JMenu("Fichier");
		menuBarFichier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
			}
		});
		menuBar.add(menuBarFichier);

		menuItemSauvegarder = new JMenuItem("Sauvegarder");
		menuItemSauvegarder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
				sauvegarderTable() ;
			}
		});
		menuBarFichier.add(menuItemSauvegarder);

		menuItemChargeFichier = new JMenuItem("Charger");
		menuItemChargeFichier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
				chargerUneTable() ;
			}
		});
		menuBarFichier.add(menuItemChargeFichier);

		JMenu mnScenePrefaite = new JMenu("Scènes préfaites");
		mnScenePrefaite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
			}
		});

		JMenuItem mntmNouvTable = new JMenuItem("Nouvelle table");
		mntmNouvTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.nouvelleTable() ;
			}
		});
		menuBarFichier.add(mntmNouvTable);
		menuBarFichier.add(mnScenePrefaite);

		rdbtnScene1 = new JRadioButtonMenuItem("Scène 1");
		rdbtnScene1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
			}
		});
		mnScenePrefaite.add(rdbtnScene1);


		rdbtnScene2 = new JRadioButtonMenuItem("Scène 2");
		rdbtnScene2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
			}
		});
		mnScenePrefaite.add(rdbtnScene2);

		rdbtnScene3 = new JRadioButtonMenuItem("Scène 3");
		rdbtnScene3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
			}
		});
		mnScenePrefaite.add(rdbtnScene3);

		JMenu menuBarFenetre = new JMenu("Fenêtre");
		menuBarFenetre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
			}
		});
		menuBar.add(menuBarFenetre);

		menuItemConfiguration = new JMenuItem("Configuration");
		menuItemConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				creerEtAfficherConfig() ;
			}
		});
		menuBarFenetre.add(menuItemConfiguration);

		menuItemMenu = new JMenuItem("Menu");
		menuItemMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
				creerMenuEtDisposer() ;
			}
		});
		menuBarFenetre.add(menuItemMenu);

		JMenuItem mnTest = new JMenuItem("Application satellite");
		mnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
				creerAppliSatellite() ;
			}
		});
		menuBarFenetre.add(mnTest);

		cPPRincipale = new JPanel();
		cPPRincipale.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(cPPRincipale);
		cPPRincipale.setLayout(null);

		lblTempsSimulé = new JLabel("Temps simulé :");
		lblTempsSimulé.setBounds(10, 29, 206, 35);
		lblTempsSimulé.setFont(new Font("Tahoma", Font.PLAIN, 30));
		cPPRincipale.add(lblTempsSimulé);

		lblTemps = new JLabel(String.format("%.3f", 0d));
		lblTemps.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				recevoirEtModifierParametres(evt) ;
			}
		});
		lblTemps.setBounds(226, 33, 94, 35);
		lblTemps.setFont(new Font("Tahoma", Font.PLAIN, 20));
		cPPRincipale.add(lblTemps);

		lblSec = new JLabel("s");
		lblSec.setBounds(330, 30, 70, 32);
		lblSec.setFont(new Font("Tahoma", Font.PLAIN, 30));
		cPPRincipale.add(lblSec);

		btnModeEdition = new JToggleButton("Mode édition");
		btnModeEdition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
				gestionModes() ;
			}
		});
		btnModeEdition.setFont(new Font("Times New Roman", Font.BOLD, 25));
		btnModeEdition.setBounds(1259, 0, 281, 55);
		cPPRincipale.add(btnModeEdition);

		scene = new Scene();
		scene.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				scene.requestFocusInWindow();
				recevoirObstacle(evt);
				recevoirEtModifierParametres(evt) ;
			}
		});

		scene.setBounds(10, 74, 400, 739);
		cPPRincipale.add(scene);

		graphiqueEnergieFonctionTemps = new Graphique();
		graphiqueEnergieFonctionTemps.setTitre("Énergie totale de la balle en fonction du temps") ;
		//GraphiqueEnergieFonctionTemps.setRatioAffichageX(1.000) ;
		graphiqueEnergieFonctionTemps.setBounds(410, 56, 324, 304);
		//graphiqueEnergieFonctionTemps.setVisible(false) ;
		cPPRincipale.add(graphiqueEnergieFonctionTemps);

		graphiqueVitesseFonctionTemps = new Graphique();
		graphiqueVitesseFonctionTemps.setTitre("Vitesse de la balle en fonction du temps") ;
		//GraphiqueVitesseFonctionTemps.setRatioAffichageX(0.5) ;
		graphiqueVitesseFonctionTemps.setBounds(744, 56, 365, 304);
		//graphiqueVitesseFonctionTemps.setVisible(false) ; 
		cPPRincipale.add(graphiqueVitesseFonctionTemps);

		graphiqueAccelerationFonctionTemps = new Graphique();
		graphiqueAccelerationFonctionTemps.setTitre("Accélération de la balle en fonction du temps") ;
		//GraphiqueAccelerationFonctionTemps.setRatioAffichageX(0.5) ;
		graphiqueAccelerationFonctionTemps.setBounds(1125, 56, 415, 304);
		cPPRincipale.add(graphiqueAccelerationFonctionTemps);

		panelMod = new JPanel();
		panelMod.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panelMod.setBounds(553, 418, 620, 321);
		cPPRincipale.add(panelMod);
		panelMod.setLayout(null);

		spnK = new JSpinner();
		spnK.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				scene.requestFocusInWindow();
			}
		});
		spnK.setFont(new Font("Tahoma", Font.PLAIN, 15));
		spnK.setModel(new SpinnerNumberModel(50.0, 10.0, 1000.0, 0.0)) ;
		spnK.setBounds(436, 94, 162, 35);
		panelMod.add(spnK);

		cBoxBilles = new JComboBox();
		cBoxBilles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
				getTypeDeBalle();
			}
		});
		cBoxBilles.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cBoxBilles.setModel(new DefaultComboBoxModel(new String[] {"Bois","Fer","Aluminium","Glace","Carbone","Or","Verre"}));
		cBoxBilles.setBounds(436, 139, 162, 31);
		panelMod.add(cBoxBilles);

		cBoxSol = new JComboBox();
		cBoxSol.setModel(new DefaultComboBoxModel(new String[] {"Sable", "Terre", "Glace"}));
		cBoxSol.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
				/*	try {
					MoteurPhysique.setCoefFrottement(cboxSol.getSelectedItem().toString());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}*/
			}

		});

		cBoxSol.setFont(new Font("Tahoma", Font.PLAIN, 15));
		cBoxSol.setBounds(436, 186, 162, 35);
		panelMod.add(cBoxSol);

		cboxG = new JComboBox();
		cboxG.setFont(new Font("Tahoma", Font.PLAIN, 20));
		cboxG.setModel(new DefaultComboBoxModel(new String[]{"Terre","Lune","Mercure","Vénus","Mars",
				"Jupiter","Saturne","Uranus","Neptune","Pluton"}));
		cboxG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
				try {
					MoteurPhysique.setG(cboxG.getSelectedItem().toString());
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		cboxG.setBounds(436, 49, 162, 35);
		panelMod.add(cboxG);

		spnForces = new JSpinner();
		spnForces.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				scene.requestFocusInWindow();
			}
		});
		spnForces.setFont(new Font("Tahoma", Font.PLAIN, 15));
		spnForces.setModel(new SpinnerNumberModel(Integer.valueOf(45), Integer.valueOf(0), null, Integer.valueOf(1)));
		spnForces.setBounds(469, 269, 70, 35);
		panelMod.add(spnForces);

		spnMasse = new JSpinner();
		spnMasse.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				scene.changerMasseDesBalles((double) spnMasse.getValue());
				scene.requestFocusInWindow();
			}
		});
		spnMasse.setModel(new SpinnerNumberModel(Integer.valueOf(1), Integer.valueOf(1), null, Integer.valueOf(1)));
		spnMasse.setBounds(132, 190, 99, 35);
		panelMod.add(spnMasse);

		spnInclinaison = new JSpinner();
		spnInclinaison.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				scene.requestFocusInWindow();
			}
		});
		spnInclinaison.setFont(new Font("Tahoma", Font.PLAIN, 15));
		spnInclinaison.setModel(new SpinnerNumberModel(20, 0, 360, 1));
		spnInclinaison.setBounds(486, 231, 84, 35);
		panelMod.add(spnInclinaison);

		lblModG = new JLabel("Modification de la gravité : ");
		lblModG.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblModG.setBounds(180, 60, 246, 24);
		panelMod.add(lblModG);

		lblModConstanteK = new JLabel("Modification de la constante de rappel (k) :");
		lblModConstanteK.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblModConstanteK.setBounds(33, 105, 399, 24);
		panelMod.add(lblModConstanteK);

		lblChoixBilles = new JLabel("Choix de type de billes :");
		lblChoixBilles.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblChoixBilles.setBounds(199, 146, 227, 24);
		panelMod.add(lblChoixBilles);

		lblTypeSol = new JLabel("Type de sol :");
		lblTypeSol.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTypeSol.setBounds(298, 187, 128, 24);
		panelMod.add(lblTypeSol);

		lblForceLancer = new JLabel("Force du lancer (si pas le bouton n'est pas utilisé) :");
		lblForceLancer.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblForceLancer.setBounds(10, 270, 456, 24);
		panelMod.add(lblForceLancer);

		lblInclinaison = new JLabel("Inclinaison en degrés de la table :");
		lblInclinaison.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblInclinaison.setBounds(142, 235, 334, 24);
		panelMod.add(lblInclinaison);

		lblNewtons = new JLabel("N");
		lblNewtons.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblNewtons.setBounds(556, 270, 24, 24);
		panelMod.add(lblNewtons);

		lblModUtilisateur = new JLabel("Modification utilisateur de la structure du flipper");
		lblModUtilisateur.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 20));
		lblModUtilisateur.setBounds(43, 15, 555, 24);
		panelMod.add(lblModUtilisateur);

		lblMasseBalle = new JLabel("Masse Balle :");
		lblMasseBalle.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMasseBalle.setBounds(10, 189, 128, 28);
		panelMod.add(lblMasseBalle);

		lblKg = new JLabel("Kg");
		lblKg.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblKg.setBounds(232, 197, 45, 24);
		panelMod.add(lblKg);

		schemaDesForces = new SchemaDesForces();
		schemaDesForces.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				recevoirEtModifierParametres(evt) ;
			}
		});
		schemaDesForces.setBounds(1175, 417, 365, 396);
		cPPRincipale.add(schemaDesForces);

		btnLancer = new JButton("Lancer");
		btnLancer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
				nbFoisAppui++ ;
				if (nbFoisAppui == 1) {
					jauge.demarrer() ;
				} else if (nbFoisAppui == 2) {
					jauge.arreter() ;
					nbFoisAppui = 0 ;
				}
			}
		});
		btnLancer.setFont(new Font("Tahoma", Font.PLAIN, 30));
		btnLancer.setBounds(410, 686, 133, 127);
		cPPRincipale.add(btnLancer);

		btnF = new JButton("F");
		btnF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
			}
		});
		btnF.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnF.setBounds(594, 749, 63, 64);
		cPPRincipale.add(btnF);

		btnArreterJouer = new JToggleButton("Jouer");
		btnArreterJouer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
				if(btnArreterJouer.isSelected()) {
					btnArreterJouer.setText("Arrêter");
					scene.demarrerAnimation();
					btnPas.setEnabled(false);
				}else {
					scene.arreterAnimation();
					btnArreterJouer.setText("Jouer");
					btnPas.setEnabled(true);
				}
			}
		});
		btnArreterJouer.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnArreterJouer.setBounds(677, 749, 141, 64);
		cPPRincipale.add(btnArreterJouer);

		btnRecommencer = new JButton("Recommencer");
		btnRecommencer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
				scene.reinitialiser() ;
				reinitialiserGraphiques() ;
				btnArreterJouer.setSelected(false) ;
				btnArreterJouer.setText("Jouer") ;
				lblTemps.setText(String.format("%.3f", 0d));
			}
		});
		btnRecommencer.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnRecommencer.setBounds(859, 749, 148, 64);
		cPPRincipale.add(btnRecommencer);

		btnPas = new JButton("+1");
		btnPas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
				try {
					scene.sautDePas();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnPas.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnPas.setBounds(1043, 749, 95, 64);
		cPPRincipale.add(btnPas);
		jauge.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getPropertyName().equals("Etirement")) {
					double etirement = (double) evt.getNewValue();
					scene.changerEtirementRessortDepart(etirement);
				}

			}
		});
		jauge.setBounds(410, 418, 133, 258);
		cPPRincipale.add(jauge);

		lblEnergie = new JLabel("Énergie (J) =");
		lblEnergie.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblEnergie.setBounds(410, 370, 91, 13);
		cPPRincipale.add(lblEnergie);

		lblNbJoule = new JLabel("0");
		lblNbJoule.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNbJoule.setBounds(511, 370, 133, 20);
		cPPRincipale.add(lblNbJoule);

		lblSommeFX = new JLabel("ΣF (N) =");
		lblSommeFX.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblSommeFX.setBounds(410, 393, 84, 15);
		cPPRincipale.add(lblSommeFX);

		lblNbForce = new JLabel("0");
		lblNbForce.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNbForce.setBounds(511, 390, 133, 20);
		cPPRincipale.add(lblNbForce);

		lblVitesseModule = new JLabel("Vitesse (m/s) :");
		lblVitesseModule.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblVitesseModule.setBounds(712, 370, 105, 20);
		cPPRincipale.add(lblVitesseModule);

		lblVitesseX = new JLabel("Vitesse X (m/s):");
		lblVitesseX.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblVitesseX.setBounds(712, 396, 116, 20);
		cPPRincipale.add(lblVitesseX);

		lblVitesseY = new JLabel("Vitesse y (m/s):");
		lblVitesseY.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblVitesseY.setBounds(891, 396, 116, 20);
		cPPRincipale.add(lblVitesseY);

		lblModuleVit = new JLabel("0");
		lblModuleVit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblModuleVit.setBounds(828, 370, 63, 20);
		cPPRincipale.add(lblModuleVit);

		lblVitX = new JLabel("0");
		lblVitX.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblVitX.setBounds(828, 396, 63, 20);
		cPPRincipale.add(lblVitX);

		lblVitY = new JLabel("0");
		lblVitY.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblVitY.setBounds(1002, 396, 63, 20);
		cPPRincipale.add(lblVitY);

		lblAcceleration = new JLabel("Accélération (m/s^2):");
		lblAcceleration.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblAcceleration.setBounds(1064, 368, 148, 20);
		cPPRincipale.add(lblAcceleration);

		lblAccelerationX = new JLabel("Accélération x (m/s^2):");
		lblAccelerationX.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblAccelerationX.setBounds(1064, 396, 159, 20);
		cPPRincipale.add(lblAccelerationX);

		lblAccelerationY = new JLabel("Accélération y (m/s^2):");
		lblAccelerationY.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblAccelerationY.setBounds(1291, 370, 166, 20);
		cPPRincipale.add(lblAccelerationY);

		lblAccel = new JLabel("0,000");
		lblAccel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblAccel.setBounds(1218, 370, 63, 20);
		cPPRincipale.add(lblAccel);

		lblAccelX = new JLabel("0,000");
		lblAccelX.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblAccelX.setBounds(1228, 398, 63, 20);
		cPPRincipale.add(lblAccelX);

		lblAccelY = new JLabel("0,000");
		lblAccelY.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblAccelY.setBounds(1467, 370, 63, 20);
		cPPRincipale.add(lblAccelY);

		lblTitre = new JLabel("FrissonBoum : Le frisson de la victoire !");
		lblTitre.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				recevoirEtModifierParametres(evt) ;
			}
		});
		lblTitre.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitre.setFont(new Font("Lucida Bright", Font.BOLD | Font.ITALIC, 40));
		lblTitre.setBounds(344, 10, 984, 35);
		cPPRincipale.add(lblTitre);
	}
	/**
	 * Méthode permettant d'avoir des obstacles modifiés
	 * @param evt L'écouteur du changement d'évent.
	 */
	//Aimé Melançon
	private void recevoirObstacle(PropertyChangeEvent evt) {
		if("obstacle" == evt.getPropertyName()) {
			scene.getTableActuelle().ajouterObstacle((Obstacle)evt.getNewValue());
			repaint();
		}

	}

	/**
	 * Méthode privée permettant de créer et afficher la fenêtre de configuration
	 * sans "supprimer" la fenêtre de menu.
	 */
	// Par Elias Kassas
	private void creerEtAfficherConfig() {
		scene.requestFocusInWindow();
		config.setVisible(true) ;
	}

	/**
	 * Méthode privée servant à recevoir et modifier les paramètres de l'application.
	 * @param evt : L'écouteur de propertyChange.
	 */
	// Par Elias Kassas
	private void recevoirEtModifierParametres(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("temps")) {
			tempsTotalScene = (double) evt.getNewValue() ;
			lblTemps.setText(String.format("%.3f", tempsTotalScene)) ;
		}

		// Paramètres physiques
		if (evt.getPropertyName().equals("table")) {
			Table tableRecue = (Table) evt.getNewValue() ;
			Balle ballePhysique = tableRecue.getBalleDebut() ;
			
			miseAJourGraphiques(ballePhysique) ;

			// MàJ du Schéma des forces appliquées sur la balle
			/*System.out.println("Début partie = " + scene.isDebutPartie()) ;
			if (scene.isDebutPartie()) {
				schemaDesForces.poserUneBalle(ballePhysique) ;
			} else {
				schemaDesForces.lireDeNouvellesForces(ballePhysique) ;
			}*/
		}

		// Paramètres informatiques
		if (evt.getPropertyName().equals("configuration")) {
			InfoConfiguration ic = (InfoConfiguration) evt.getNewValue() ;
			miseAJourConfig(ic) ;
		}

		if (evt.getPropertyName().equals("etatAnimation")) {
			btnArreterJouer.setSelected(false) ;
			btnPas.setEnabled(true) ;
			reinitialiserGraphiques() ;
		}
	}

	/**
	 * Méthode privée servant à faire la mise à jour des graphiques à partir d'une balle passée en paramètre.
	 * @param balleRecue : La balle dont on veut extraire les informations.
	 */
	// Par Elias Kassas
	private void miseAJourGraphiques(Balle balleRecue) {
		// MàJ du Graphique de la vitesse de la balle en fonction du temps
		Vecteur3D vitesseRecue = convertirEnMetres(balleRecue.getVitesse()) ;
		graphiqueVitesseFonctionTemps.ajouterUnPointSurLaCourbe(tempsTotalScene, 
				vitesseRecue.module()) ;
		lblModuleVit.setText(String.format("%.3f", vitesseRecue.module())) ;
		lblVitX.setText(String.format("%.3f", vitesseRecue.getX())) ;
		lblVitY.setText(String.format("%.3f", -vitesseRecue.getY())) ;

		// MàJ du Graphique de l'accélération de la balle en fonction du temps
		Vecteur3D accelerationRecue = convertirEnMetres(balleRecue.getAcceleration()) ;
		graphiqueAccelerationFonctionTemps.ajouterUnPointSurLaCourbe(tempsTotalScene, 
				accelerationRecue.module()) ;
		lblAccel.setText(String.format("%.3f", accelerationRecue.module())) ;
		lblAccelX.setText(String.format("%.3f", accelerationRecue.getX())) ;
		lblAccelY.setText(String.format("%.3f", -accelerationRecue.getY())) ;

		// MàJ du Graphique de l'énergie en fonction du temps
		double sommeEnergies = balleRecue.calculSommeDesEnergies((int) spnInclinaison.
				getValue(), 0.0)/10000d ;
		lblNbJoule.setText(String.format("%.4f", sommeEnergies)) ;
		graphiqueEnergieFonctionTemps.ajouterUnPointSurLaCourbe(tempsTotalScene, sommeEnergies) ;
		lblNbForce.setText(String.format("%.4f", balleRecue.getSommeDesForces().module())) ;
	}
	
	/**
	 * Méthode permettant de convertir un vecteur ayant des unités avec des centimètres pour des mètres
	 * seulement si les centimètres sont au numérateur.
	 * @param vecteurEnCentimetres
	 * @return Un vecteur ayant des unités avec des mètres.
	 */
	// Par Elias Kassas
	private Vecteur3D convertirEnMetres(Vecteur3D vecteurEnCentimetres) {
		return vecteurEnCentimetres.multiplie(1/100) ;
	}
	
	/**
	 * Méthode privée servant à modifier la configuration de l'application.
	 * NOTE : Pour le moment, nous ne faisons qu'imprimer la configuration choisie par l'utilisateur.
	 * Nous appliquerons les changements lors de notre troisième mêlée.
	 * @param ic : L'objet contenant toutes les informations de configuration de l'application.
	 */
	// Par Elias Kassas
	private void miseAJourConfig(InfoConfiguration ic) {
		System.out.println(ic) ;
		System.out.println("NOTE : Les modifications de l'application en son apparence seront "
				+ "apportées à la dernière mêlée comme c'est à ce moment-là que nous aurons "
				+ "implémenté la configuration de l'esthétique de l'application. \nPour "
				+ "l'instant, nous les notons à la console.".toUpperCase()) ;
	}

	/**
	 * Méthode servant à réinitialiser les Graphiques ainsi que les valeurs affichées dans
	 * les étiquettes.
	 */
	// Par Elias Kassas
	private void reinitialiserGraphiques() {
		graphiqueVitesseFonctionTemps.reinitialiser() ;
		graphiqueAccelerationFonctionTemps.reinitialiser() ;
		graphiqueEnergieFonctionTemps.reinitialiser() ;

		lblModuleVit.setText(String.format("%.3f", 0d)) ;
		lblVitX.setText(String.format("%.3f", 0d)) ;
		lblVitY.setText(String.format("%.3f", 0d)) ;

		lblAccel.setText(String.format("%.3f", 0d)) ;
		lblAccelX.setText(String.format("%.3f", 0d)) ;
		lblAccelY.setText(0.000 + "") ;

		lblNbJoule.setText(String.format("%.3f", 0d)) ;
		lblNbForce.setText(String.format("%.3f", 0d)) ;

		lblTemps.setText(String.format("%.3f", 0d)) ;
	}

	/**
	 * Méthode permettant de manipuler la phase du bouton JouerArreter pour qu'il soit en mode jouer ou arrêter.
	 * 
	 * @param jouerArreter Vrai si c'est jouer et faux si c'est arrêter.
	 */
	//Aimé Melançon
	public static void setBouton(boolean jouerArreter) {

		if(!jouerArreter) {
			btnArreterJouer.setText("Jouer");
		}else {
			btnArreterJouer.setText("Arrêter");
		}
	}

	/**
	 * Méthode privée servant à créer et afficher l'application satellite.
	 * Cette méthode est temporaire pour la première mêlée.
	 */
	// Par Elias Kassas
	private void creerAppliSatellite() {
		AppSatelliteElias app = new AppSatelliteElias() ;
		app.setVisible(true) ;
		cPPRincipale.setEnabled(false) ;
	}

	/**
	 * Méthode privée servant à créer le menu et disposer de l'application.
	 */
	// Par Elias Kassas
	private void creerMenuEtDisposer() {
		Menu menu = new Menu() ;
		menu.setVisible(true) ;
		config.setVisible(false) ;
		dispose() ;
	}

	/**
	 * Méthode privée permettant la gestion des différents modes.
	 */
	// Par Elias Kassas
	private void gestionModes() {
		scene.requestFocusInWindow();
		if (btnModeEdition.isSelected()) {
			// Mode édition.
			scene.setBounds(10, 74, 1530, 739) ;
			graphiqueEnergieFonctionTemps.setVisible(false) ;
			graphiqueVitesseFonctionTemps.setVisible(false) ;
			graphiqueAccelerationFonctionTemps.setVisible(false) ;
			panelMod.setVisible(false) ;
			scene.setModeCreation(true);
			scene.reinitialiser() ;
			reinitialiserGraphiques() ;
			lblTemps.setText(String.format("%.3f", 0d)) ;

			btnModeEdition.setText("Mode scientifique") ;
		} else {
			// Mode scientifique.
			scene.setBounds(10, 74, 400, 739) ;
			graphiqueEnergieFonctionTemps.setVisible(true) ;
			graphiqueVitesseFonctionTemps.setVisible(true) ;
			graphiqueAccelerationFonctionTemps.setVisible(true) ;
			scene.setModeCreation(false);
			panelMod.setVisible(true) ;
			btnModeEdition.setText("Mode édition") ;
		}
	}

	/**Méthode permettant de forcer le bouton avancer un pas d'être activé ou désactivé.
	 * 
	 * @param reponse Si on veut désactivé le bouton ou on veut le désactivé.
	 */
	//Aimé Melançon
	public static void setAvancerUnPasBtn(boolean reponse) {
		btnPas.setEnabled(reponse);
	}

	/**
	 * Méthode privée permettant de sauvegarder une Table.
	 */
	// Par Elias Kassas
	private void sauvegarderTable() {
		scene.requestFocusInWindow();
		String nomFichier = JOptionPane.showInputDialog("Entrez le nom du fichier de la table "
				+ "à sauvegarder.") ;

		gest.sauvegarder(scene.getTableActuelle(), nomFichier) ;
		scene.nouvelleTable() ;
	}

	/**
	 * Méthode privée permettant de charger une table précédemment faite.
	 */
	// Par Elias Kassas
	private void chargerUneTable() {
		scene.requestFocusInWindow();
		JFileChooser jfc = new JFileChooser() ;
		int resultat = jfc.showDialog(null, "Choisir") ;

		// Si l'utilisateur a pesé sur le bouton "Choisir", on charge la table choisie.
		if (resultat == JFileChooser.APPROVE_OPTION) {
			File fichierChoisi = jfc.getSelectedFile() ;
			Table tableLue = (Table) gest.lire(fichierChoisi.getName()) ;
			tableLue.setDebutJeu(true) ;
			scene.changerTable(tableLue) ;
		}
	}

	/**
	 * Méthode permettant de charger une scène en entrant dans l'application.
	 * @param fichierTable : Le fichier de Table à charger sur l'application.
	 */
	// Par Elias Kassas
	public void chargerUneTable(File fichierTable) {
		scene.requestFocusInWindow();
		Table tableLue = (Table) gest.lire(fichierTable.getName()) ;
		scene.changerTable(tableLue) ;
	}

	/**
	 * Méthode permettant de connaître le type de balle.
	 * 
	 * @return Retourne le type de balle.
	 */
	//Aimé Melançon
	public String getTypeDeBalle() {
		scene.requestFocusInWindow();
		System.out.println("Balle sélectionné de type : " + cBoxBilles.getSelectedItem().toString());
		return	cBoxBilles.getSelectedItem().toString();
	}

	/**
	 * Méthode permettant d'avoir l'angle en radians de la table.
	 * @return Retourne l'angle en radians de la table
	 */
	//Aimé Melançon
	public double getAngle() {
		scene.requestFocusInWindow();
		return Math.toRadians(Double.parseDouble(spnInclinaison.getValue().toString()));
	}

	/**
	 * Méthode permettant d'avoir la force de lancer si le bouton lancer n'est pas utilisé.
	 * 
	 * @return Retourne la force de lancer plate.
	 */
	//Aimé Melançon
	public double getForceDeLancer() {
		scene.requestFocusInWindow();
		return	Double.parseDouble(spnForces.getValue().toString());
	}

	/**
	 * Méthode permettant d'avoir la force de rappel du ressort qui propulse la balle.
	 * 
	 * @return Retourne la force de rappel du ressort.
	 */
	//Aimé Melançon
	public double getConstanteRappel() {
		scene.requestFocusInWindow();
		return	Double.parseDouble(spnK.getValue().toString());
	}

	/**
	 * Méthode permettant d'avoir la masse de la balle choisis.
	 * 
	 * @return Retourne la masse sélectionné de la balle.
	 */
	//Aimé Melançon
	public double getMasseBalle() {
		scene.requestFocusInWindow();
		return Double.parseDouble(spnMasse.getValue().toString());
	}	
}

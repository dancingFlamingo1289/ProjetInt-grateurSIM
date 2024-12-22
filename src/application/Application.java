package application;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JToggleButton;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import application.configuration.Configuration;
import application.configuration.InfoConfiguration;
import application.configuration.Theme;
import application.gestionScore.Score;
import composantDeJeu.Balle;
import composantDeJeu.BarreDeProgression;
import composantDeJeu.Jauge;
import composantDeJeu.Table;
import composantDeJeu.TriangleInclinaison;
import gestionFichiers.GestionnaireFichiers;
import instructions.FenetreInstructions;
import instructions.PanelAPropos;
import math.vecteurs.Vecteur3D;
import outils.GererSon;
import outils.OutilsImage;
import outils.OutilsSon;
import physique.MoteurPhysique;
import scene.Scene;
import statistiques.Graphique;
import statistiques.SchemaDesForces;

import javax.swing.JCheckBox;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Application scientifique permettant de simuler un jeu de flipper tout en pouvant voir la 
 * physique derrière. Cette classe représente la fenêtre principale de l'application.
 * Classe qui dérive de JFrame.
 * @author Aimé Melançon
 * @author Elias Kassas
 * @author Félix Lefrançois
 */
public class Application extends JFrame {
	/** Identifiant de classe **/
	private static final long serialVersionUID = 1L;
	/** Variable de type JPanel qui instancie la fenêtre principale.**/
	private JPanel contentPane;
	/** Étiquette contenant le temps simulé écoulé pour l'animation. **/
	private JLabel lblTemps;
	/** Bouton qui permet d'accéder au mode édition. **/
	private JToggleButton btnModeEdition;
	/** Tourniquet qui permet de modifier la constante de rappel.**/
	private JSpinner spnK;
	/** Graphique qui montre l'énergie en fonction du temps. **/
	private Graphique graphiqueEnergieFonctionTemps;
	/** Graphique qui montre la vitesse en fonction du temps. **/
	private Graphique graphiqueVitesseFonctionTemps;
	/** Graphique qui montre l'accélération en fonction du temps. **/
	private Graphique graphiqueAccelerationFonctionTemps;
	/** Scène de l'application affichant la table de jeu. **/
	private Scene scene;
	/** Liste déroulante qui permet de choisir la constante de gravitation de la table. **/
	private JComboBox<Font> cboxG;
	/** Liste déroulante qui permet de modifier le type de sol. **/
	private JSpinner spnCoefFrottement;
	/** Curseur permettant de changer la masse. **/
	private JSlider curseurMasse;
	/**Tourniquet permettant de changer l'inclinaison. **/
	private JSpinner spnInclinaison;
	/**Variable de type JLabel qui affiche kg. **/
	private JLabel lblMasseBalleKg;
	/**Variable de type JPanel qui permet d'afficher un panneau. **/
	private JPanel panelMod;
	/**Variable de type JButton qui permet de lancer la balle. **/
	private JButton btnLancer;
	/**Variable de type Jauge qui permet d'afficher une jauge de puissance. **/
	private Jauge jauge = new Jauge();
	/**Variable de type JButton qui permet d'arrêter ou de continuer l'annimation de la simulation. **/
	private static JToggleButton btnArreterJouer;
	/** Variable de type JButton qui permet de remettre à zéro et recommencer la simulation. **/
	private JButton btnRecommencer;
	/** Bouton qui permet de trouver un oeuf de pâque. **/
	private static JButton btnF;
	/** Bouton qui permet d'avancer d'un pas. **/
	private static JButton btnPas;
	/** Étiquette qui affiche l'énergie. **/
	private JLabel lblEnergie;
	/** Étiquette qui affiche le nombre de Joule.**/
	private JLabel lblNbJoule;
	/** Étiquette affichant le nombre de force appliquées à la balle.**/
	private JLabel lblNbForce;
	/** Étiquette montrant la vitesse en x.**/
	private JLabel lblVitX;
	/** Étiquette montrant la vitesse en y.**/
	private JLabel lblVitY;
	/** Étiquette montrant la vitesse en module. **/
	private JLabel lblModuleVit ;
	/**Étiquette montrant l'accélération en m/s^2.**/
	private JLabel lblAccel;
	/**Étiquette montrant l'accélération en x en m/s^2  **/
	private JLabel lblAccelX;
	/**Étiquette montrant l'accélération en y en m/s^2**/
	private JLabel lblAccelY;
	/** Menu pour les composants afférant à la sauvegarde **/	
	private static JMenu menuBarFichier;
	/** Boutons radio de style d'item de menu qui permettent de charger la scène préfaite 1, 2 3t 3. **/
	private JRadioButtonMenuItem rdbtnScene1, rdbtnScene2, rdbtnScene3 ;
	/** Temps total pris de la scène. **/
	private double tempsTotalScene ;
	/** Fenêtre de configuration de l'application. **/
	private Configuration config = new Configuration() ;
	/**Fenêtre d'instructions de l'application. **/
	private FenetreInstructions fenetreInstructions = new FenetreInstructions();
	/** Gestionnaire de fichiers servant au système de sauvegarde. **/
	private GestionnaireFichiers gest ;
	/** Schéma des forces appliquées à la balle. **/
	private SchemaDesForces schemaDesForces ;
	/** Balle de base envoyée au schéma des forces **/
	private final Balle BALLE_DEFAUT = new Balle(new Vecteur3D(98, 90), 1.0, Color.white) ;
	/** ButtonGroup pour modifier l'affichage du graphique voulu. **/
	private final ButtonGroup btngrpGraphiques = new ButtonGroup() ;
	/** Liste contenant tous les graphiques à afficher. **/
	private ArrayList<Graphique> lesGraphiques ;
	/** Liste des zones de regroupement de l'application. **/
	private ArrayList<JPanel> lesPanels ;
	/** Zone de regroupement avec un graphique que l'utilisateur veut afficher. **/
	private JPanel panelVoulu ;
	/** On vérifie si c'est la première fois que l'application est 'dessinée'. **/
	private boolean premiereFois = true ;
	/** Bordure (bound) du panel contenant les graphiques et leurs affichages. **/
	private Rectangle bound = new Rectangle(1345, 75, 385, 360) ;
	/** Nom de la police d'écriture de l'application. **/
	private String nomPolice = "Tahoma" ;
	/** Liste contenant tous les composants de l'application pour l'application d'un thème. **/
	private ArrayList<Component> lesComposants ;
	/** Zone de regroupement des boutons radios servant à changer les graphiques. **/
	private JPanel panBoutonsRadio ;
	/** Le triangle représentant l'inclinaison de la table **/
	private TriangleInclinaison triangleInclinaison;
	/**Le menu de l'application **/
	private AppPrincipale36 menu;
	/** Curseur permettant de modifier le temps du sleep de l'animation. **/
	private JSlider sliderTempsDuSleep;
	/** Barre de progression de l'application. **/
	private BarreDeProgression barreDeProgression;
	/** Étiquette affichant la valeur du temps du sleep **/
	private JLabel lblValeurDuTempsDuSleep;
	/** Étiquette contenant les composants associés à la progression de l'utilisateur. **/
	private JPanel panProgression;
	/** Étiquette affichant le meilleur score à battre de l'utilisateur. **/
	private JLabel lblMeilleurScore;
	/** Case à cocher servant à activer ou désactiver tous les sons des obstacles.**/ 
	JCheckBox chckActiveDesactiveSon;
	/** Le générateur de musique **/
	private OutilsSon musique = new OutilsSon();
	/** Bouton servant à réinitialiser les scores enregistrés. **/
	private JButton btnReinitialiserLesScores;

	/**
	 * Création du panneau
	 */
	//Aimé Melançon
	public Application() {
		lesComposants = new ArrayList<Component>() ;

		setTitle("36 FrissonBoum");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1728, 1117);

		gest = new GestionnaireFichiers() ;

		menu = new AppPrincipale36() ;
		enregistrerUnComposant(menu) ;

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		enregistrerUnComposant(menuBar) ;

		JMenu menuBarInformation = new JMenu("Information");
		menuBarInformation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				requestFocusInWindow();
				scene.requestFocusInWindow();
			}
		});
		menuBar.add(menuBarInformation);
		enregistrerUnComposant(menuBarInformation) ;

		FenetreTouchesClavier fenetreTouchesClavier = new FenetreTouchesClavier();
		fenetreTouchesClavier.setVisible(true);
		PanelAPropos APropos= new PanelAPropos();
		enregistrerUnComposant(APropos) ;

		JMenuItem menuItemAPropos = new JMenuItem("À propos");
		menuItemAPropos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				requestFocusInWindow();
				scene.requestFocusInWindow();
				creerAPropos();
			}
		});
		menuBarInformation.add(menuItemAPropos);
		enregistrerUnComposant(menuItemAPropos) ;

		JMenuItem menuItemInstructions = new JMenuItem("Instructions");
		menuItemInstructions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				requestFocusInWindow();
				scene.requestFocusInWindow();
				fenetreInstructions.setVisible(true);
			}
		});
		menuBarInformation.add(menuItemInstructions);
		enregistrerUnComposant(menuItemInstructions) ;

		menuBarFichier = new JMenu("Sauvegarde");
		menuBarFichier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				requestFocusInWindow();
				scene.requestFocusInWindow();
			}
		});
		menuBar.add(menuBarFichier);
		enregistrerUnComposant(menuBarFichier) ;

		JMenuItem menuItemSauvegarder = new JMenuItem("Sauvegarder");
		menuItemSauvegarder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				requestFocusInWindow();
				scene.requestFocusInWindow();
				sauvegarderTable() ;
			}
		});
		menuBarFichier.add(menuItemSauvegarder);
		enregistrerUnComposant(menuItemSauvegarder) ;

		JMenuItem menuItemChargeFichier = new JMenuItem("Charger");
		menuItemChargeFichier.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				requestFocusInWindow();
				scene.requestFocusInWindow();
				chargerUneTable() ;
			}
		});
		menuBarFichier.add(menuItemChargeFichier);
		enregistrerUnComposant(menuItemChargeFichier) ;

		JMenu mnScenePrefaite = new JMenu("Scènes préfaites");
		mnScenePrefaite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				requestFocusInWindow();
				scene.requestFocusInWindow();
			}
		});
		menuBarFichier.add(mnScenePrefaite);
		enregistrerUnComposant(mnScenePrefaite) ;

		JMenuItem mntmNouvTable = new JMenuItem("Nouvelle table");
		mntmNouvTable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				scene.nouvelleTable() ;
				requestFocusInWindow();
				scene.requestFocusInWindow();
			}
		});
		menuBarFichier.add(mntmNouvTable);
		enregistrerUnComposant(menuBarFichier) ;

		rdbtnScene1 = new JRadioButtonMenuItem("Scène 1");
		rdbtnScene1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chargerUneTablePredefinie("table2.bin") ;
			}
		});
		mnScenePrefaite.add(rdbtnScene1);
		enregistrerUnComposant(rdbtnScene1) ;

		rdbtnScene2 = new JRadioButtonMenuItem("Scène 2");
		rdbtnScene2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chargerUneTablePredefinie("table3.bin") ;
			}
		});
		mnScenePrefaite.add(rdbtnScene2);
		enregistrerUnComposant(rdbtnScene2) ;

		rdbtnScene3 = new JRadioButtonMenuItem("Scène 3");
		rdbtnScene3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chargerUneTablePredefinie("table4.bin") ;
			}
		});
		mnScenePrefaite.add(rdbtnScene3);
		enregistrerUnComposant(rdbtnScene3) ;

		ButtonGroup scenePrefaite = new ButtonGroup();
		scenePrefaite.add(rdbtnScene1);
		scenePrefaite.add(rdbtnScene2);
		scenePrefaite.add(rdbtnScene3);


		JMenu menuBarFenetre = new JMenu("Fenêtre");
		menuBarFenetre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				requestFocusInWindow();
				scene.requestFocusInWindow();
			}
		});
		menuBar.add(menuBarFenetre);
		enregistrerUnComposant(menuBarFenetre) ;

		JMenuItem menuItemConfiguration = new JMenuItem("Configuration");
		menuItemConfiguration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				creerEtAfficherConfig() ;

			}
		});
		menuBarFenetre.add(menuItemConfiguration);
		enregistrerUnComposant(menuItemConfiguration) ;

		config.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				appliquerChangementAffichageTouches(evt);

				recevoirEtModifierParametresInformatiques(evt) ;
			}
		});
		enregistrerUnComposant(config) ;

		JMenuItem menuItemMenu = new JMenuItem("Menu");
		menuItemMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				requestFocusInWindow();
				scene.requestFocusInWindow();
				creerMenuEtDisposer() ;
			}
		});
		menuBarFenetre.add(menuItemMenu);
		enregistrerUnComposant(menuItemMenu) ;

		/*JMenuItem mnTest = new JMenuItem("Application satellite");
		mnTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
				creerAppliSatellite() ;
			}
		});
		menuBarFenetre.add(mnTest);*/

		contentPane = new JPanel();
		contentPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println("Une touche est capté pressé");
				propulsionBalle(e);
			}
			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println("Une touche est capté relâché");
				arretPropulsionBalle(e);
				scene.requestFocusInWindow();
			}
		});
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTempsSimule = new JLabel("Temps simulé :");
		lblTempsSimule.setBounds(10, 29, 206, 35);
		lblTempsSimule.setFont(new Font(nomPolice, Font.PLAIN, 30));
		contentPane.add(lblTempsSimule);
		enregistrerUnComposant(lblTempsSimule) ;

		lblTemps = new JLabel(0.000 + "");
		lblTemps.setHorizontalAlignment(SwingConstants.CENTER);
		lblTemps.setBounds(226, 29, 94, 35);
		lblTemps.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				recevoirEtModifierParametres(evt) ;
			}
		});
		lblTemps.setFont(new Font(nomPolice, Font.PLAIN, 20));
		contentPane.add(lblTemps);
		enregistrerUnComposant(lblTemps) ;

		btnModeEdition = new JToggleButton("Mode édition");
		btnModeEdition.setBounds(1343, 7, 359, 55);
		btnModeEdition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				requestFocusInWindow();
				scene.requestFocusInWindow();
				gestionModes() ;
			}
		});
		btnModeEdition.setFont(new Font("Times New Roman", Font.BOLD, 25));
		contentPane.add(btnModeEdition);
		enregistrerUnComposant(btnModeEdition) ;

		scene = new Scene();
		scene.setBounds(10, 74, 603, 904);
		scene.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				requestFocusInWindow();
				scene.requestFocusInWindow();
				recevoirEtModifierParametres(evt) ;
			}
		});
		contentPane.add(scene);
		enregistrerUnComposant(scene) ;

		schemaDesForces = new SchemaDesForces();
		schemaDesForces.poserUneBalle(BALLE_DEFAUT) ;
		schemaDesForces.setBounds(1400, 520, 365, 400);
		contentPane.add(schemaDesForces) ;

		lesGraphiques = new ArrayList<Graphique>() ;
		lesPanels = new ArrayList<JPanel>() ;

		JPanel panGraphiqueEnergie = new JPanel();
		panGraphiqueEnergie.setBounds(bound);
		lesPanels.add(panGraphiqueEnergie) ;
		panGraphiqueEnergie.setLayout(null);

		graphiqueEnergieFonctionTemps = new Graphique();
		graphiqueEnergieFonctionTemps.setBounds(21, 0, 324, 304);
		panGraphiqueEnergie.add(graphiqueEnergieFonctionTemps);
		graphiqueEnergieFonctionTemps.setTitre("Énergie totale de la balle en fonction du temps") ;
		lesGraphiques.add(graphiqueEnergieFonctionTemps) ;

		lblEnergie = new JLabel("Énergie (J) =");
		lblEnergie.setBounds(6, 320, 91, 13);
		panGraphiqueEnergie.add(lblEnergie);
		lblEnergie.setFont(new Font(nomPolice, Font.PLAIN, 15));
		enregistrerUnComposant(lblEnergie) ;

		lblNbJoule = new JLabel("0");
		lblNbJoule.setBounds(107, 316, 89, 20);
		panGraphiqueEnergie.add(lblNbJoule);
		lblNbJoule.setFont(new Font(nomPolice, Font.PLAIN, 15));
		enregistrerUnComposant(lblNbJoule) ;

		JLabel lblSommeFX = new JLabel("ΣF (N) =");
		lblSommeFX.setBounds(201, 318, 71, 15);
		panGraphiqueEnergie.add(lblSommeFX);
		lblSommeFX.setFont(new Font(nomPolice, Font.PLAIN, 15));
		enregistrerUnComposant(lblSommeFX) ;

		lblNbForce = new JLabel("0");
		lblNbForce.setBounds(274, 315, 89, 20);
		panGraphiqueEnergie.add(lblNbForce);
		lblNbForce.setFont(new Font(nomPolice, Font.PLAIN, 15));
		enregistrerUnComposant(lblNbForce) ;

		JPanel panGraphiqueVitesse = new JPanel();
		panGraphiqueVitesse.setBounds(bound);
		lesPanels.add(panGraphiqueVitesse) ;
		panGraphiqueVitesse.setLayout(null);

		graphiqueVitesseFonctionTemps = new Graphique();
		graphiqueVitesseFonctionTemps.setBounds(21, 0, 324, 304);
		panGraphiqueVitesse.add(graphiqueVitesseFonctionTemps);
		graphiqueVitesseFonctionTemps.setTitre("Vitesse de la balle en fonction du temps") ;
		lesGraphiques.add(graphiqueVitesseFonctionTemps) ;

		JPanel panInfoVit = new JPanel();
		panInfoVit.setBounds(0, 314, 365, 46);
		panGraphiqueVitesse.add(panInfoVit);
		panInfoVit.setLayout(null);
		enregistrerUnComposant(panInfoVit) ;

		JLabel lblVitesseModule = new JLabel("Vitesse (m/s) :");
		lblVitesseModule.setBounds(0, 0, 105, 20);
		panInfoVit.add(lblVitesseModule);
		lblVitesseModule.setFont(new Font(nomPolice, Font.PLAIN, 15));
		enregistrerUnComposant(lblVitesseModule) ;

		JLabel lblVitesseX = new JLabel("Vitesse X (m/s):");
		lblVitesseX.setBounds(0, 26, 116, 20);
		panInfoVit.add(lblVitesseX);
		lblVitesseX.setFont(new Font(nomPolice, Font.PLAIN, 15));
		enregistrerUnComposant(lblVitesseX) ;

		JLabel lblVitesseY = new JLabel("Vitesse Y (m/s):");
		lblVitesseY.setBounds(179, 26, 116, 20);
		panInfoVit.add(lblVitesseY);
		lblVitesseY.setFont(new Font(nomPolice, Font.PLAIN, 15));
		enregistrerUnComposant(lblVitesseY) ;

		lblModuleVit = new JLabel("0");
		lblModuleVit.setBounds(116, 0, 63, 20);
		panInfoVit.add(lblModuleVit);
		lblModuleVit.setFont(new Font(nomPolice, Font.PLAIN, 15));
		enregistrerUnComposant(lblModuleVit) ;

		lblVitX = new JLabel("0");
		lblVitX.setBounds(116, 26, 63, 20);
		panInfoVit.add(lblVitX);
		lblVitX.setFont(new Font(nomPolice, Font.PLAIN, 15));
		enregistrerUnComposant(lblVitX) ;

		lblVitY = new JLabel("0");
		lblVitY.setBounds(290, 26, 63, 20);
		panInfoVit.add(lblVitY);
		lblVitY.setFont(new Font(nomPolice, Font.PLAIN, 15));
		enregistrerUnComposant(lblVitY) ;

		JPanel panGraphiqueAcceleration = new JPanel(); 
		panGraphiqueAcceleration.setBounds(bound) ;
		lesPanels.add(panGraphiqueAcceleration) ;
		panGraphiqueAcceleration.setLayout(null);

		graphiqueAccelerationFonctionTemps = new Graphique();
		graphiqueAccelerationFonctionTemps.setBounds(21, 0, 324, 304);
		panGraphiqueAcceleration.add(graphiqueAccelerationFonctionTemps);
		graphiqueAccelerationFonctionTemps.setTitre("Accélération (m/s^2) de la balle en fonction du temps") ;
		lesGraphiques.add(graphiqueAccelerationFonctionTemps) ;

		JLabel lblAcceleration = new JLabel("Accélération :");
		lblAcceleration.setBounds(92, 334, 96, 20);
		panGraphiqueAcceleration.add(lblAcceleration);
		lblAcceleration.setFont(new Font(nomPolice, Font.PLAIN, 15));
		enregistrerUnComposant(lblAcceleration) ;

		JLabel lblAccelerationX = new JLabel("Accélération x :");
		lblAccelerationX.setBounds(10, 312, 110, 20);
		panGraphiqueAcceleration.add(lblAccelerationX);
		lblAccelerationX.setFont(new Font(nomPolice, Font.PLAIN, 15));
		enregistrerUnComposant(lblAccelerationX) ;

		JLabel lblAccelerationY = new JLabel("Accélération y :");
		lblAccelerationY.setBounds(227, 314, 110, 20);
		panGraphiqueAcceleration.add(lblAccelerationY);
		lblAccelerationY.setFont(new Font(nomPolice, Font.PLAIN, 15));
		enregistrerUnComposant(lblAccelerationY) ;

		lblAccel = new JLabel("0,000");
		lblAccel.setBounds(200, 334, 63, 20);
		panGraphiqueAcceleration.add(lblAccel);
		lblAccel.setFont(new Font(nomPolice, Font.PLAIN, 15));
		enregistrerUnComposant(lblAccel) ;

		lblAccelX = new JLabel("0,000");
		lblAccelX.setBounds(126, 312, 63, 20);
		panGraphiqueAcceleration.add(lblAccelX);
		lblAccelX.setFont(new Font(nomPolice, Font.PLAIN, 15));
		enregistrerUnComposant(lblAccelX) ;

		lblAccelY = new JLabel("0,000");
		lblAccelY.setBounds(338, 312, 63, 20);
		panGraphiqueAcceleration.add(lblAccelY);
		lblAccelY.setFont(new Font(nomPolice, Font.PLAIN, 15));
		enregistrerUnComposant(lblAccelY) ;

		if (premiereFois) {
			panelVoulu = lesPanels.get(0) ;
			premiereFois = false ;
		}
		contentPane.add(panelVoulu) ;
		enregistrerDesComposants(lesPanels) ;

		panelMod = new JPanel();
		panelMod.setBounds(766, 520, 620, 321);
		panelMod.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		contentPane.add(panelMod);
		enregistrerUnComposant(panelMod) ;

		spnK = new JSpinner();
		spnK.setBounds(436, 94, 162, 35);
		spnK.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				requestFocusInWindow();
				scene.requestFocusInWindow();
				scene.modifierConstanteDeRappel((double)spnK.getValue());
			}
		});
		panelMod.setLayout(null);
		spnK.setFont(new Font(nomPolice, Font.PLAIN, 15));
		spnK.setModel(new SpinnerNumberModel(50.0, 30.0, 150.0, 5.0)) ;
		panelMod.add(spnK);
		enregistrerUnComposant(spnK) ;

		spnCoefFrottement = new JSpinner();
		spnCoefFrottement.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				requestFocusInWindow();
				scene.requestFocusInWindow();
				scene.changerCoefFrott(Double.parseDouble( spnCoefFrottement.getValue().toString()));
			}
		});
		spnCoefFrottement.setModel(new SpinnerNumberModel(0.2, 0.01, 1.0, 0.01));
		spnCoefFrottement.setBounds(436, 142, 162, 35);
		//cBoxSol.setModel(new DefaultComboBoxModel(new String[] {"bois", "glace", "béton", "eau", "plastique", "fer"}));
		/*spnCoefFrottement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				requestFocusInWindow();
				scene.requestFocusInWindow();

				try {
					//setCoefFrottement(cBoxSol.getSelectedItem().toString());

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});*/
		enregistrerUnComposant(spnCoefFrottement) ;

		spnCoefFrottement.setFont(new Font(nomPolice, Font.PLAIN, 15));
		panelMod.add(spnCoefFrottement);

		cboxG = new JComboBox();
		cboxG.setBounds(436, 55, 162, 35);
		cboxG.setFont(new Font(nomPolice, Font.PLAIN, 20));
		cboxG.setModel(new DefaultComboBoxModel(new String[]{"Terre","Lune","Mercure","Vénus","Mars",
				"Jupiter","Saturne","Uranus","Neptune","Pluton"}));
		cboxG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				gererLaConstanteDeGravitation();
				requestFocusInWindow();
				scene.requestFocusInWindow();
			}
		});
		panelMod.add(cboxG);
		enregistrerUnComposant(cboxG) ;

		curseurMasse = new JSlider();
		curseurMasse.setMajorTickSpacing(5);
		curseurMasse.setName("");
		curseurMasse.setMinorTickSpacing(1);
		curseurMasse.setMinimum(1);
		curseurMasse.setValue(1);
		curseurMasse.setMaximum(18);
		curseurMasse.setPaintLabels(true);
		curseurMasse.setPaintTicks(true);
		curseurMasse.setBounds(154, 192, 360, 45);
		curseurMasse.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				changementDeMasse();
			}
		});
		//curseurMasse.setModel(new SpinnerNumberModel(1.0, 0.01, 18.0, 0.01));
		panelMod.add(curseurMasse);
		enregistrerUnComposant(curseurMasse) ;

		JLabel lblModConstanteK = new JLabel("Modification de la constante de rappel (k) :");
		lblModConstanteK.setBounds(17, 99, 413, 24);
		lblModConstanteK.setHorizontalAlignment(SwingConstants.TRAILING);
		lblModConstanteK.setFont(new Font(nomPolice, Font.PLAIN, 20));
		panelMod.add(lblModConstanteK);
		enregistrerUnComposant(lblModConstanteK) ;

		JLabel lblCoefFrottement = new JLabel("Coefficient de frottement choisi :");
		lblCoefFrottement.setBounds(17, 147, 413, 24);
		lblCoefFrottement.setHorizontalAlignment(SwingConstants.TRAILING);
		lblCoefFrottement.setFont(new Font(nomPolice, Font.PLAIN, 20));
		panelMod.add(lblCoefFrottement);
		enregistrerUnComposant(lblCoefFrottement) ;

		JLabel lblModUtilisateur = new JLabel("Modification utilisateur de la structure du flipper");
		lblModUtilisateur.setBounds(43, 15, 555, 24);
		lblModUtilisateur.setFont(new Font(nomPolice, Font.BOLD | Font.ITALIC, 20));
		panelMod.add(lblModUtilisateur);
		enregistrerUnComposant(lblModUtilisateur) ;

		JLabel lblMasseBalle = new JLabel("Masse Balle :");
		lblMasseBalle.setBounds(17, 200, 132, 28);
		lblMasseBalle.setHorizontalAlignment(SwingConstants.LEFT);
		lblMasseBalle.setFont(new Font(nomPolice, Font.PLAIN, 20));
		panelMod.add(lblMasseBalle);
		enregistrerUnComposant(lblMasseBalle) ;

		lblMasseBalleKg = new JLabel("1 kg");
		lblMasseBalleKg.setHorizontalAlignment(SwingConstants.CENTER);
		lblMasseBalleKg.setBounds(526, 202, 84, 24);
		lblMasseBalleKg.setFont(new Font(nomPolice, Font.PLAIN, 20));
		panelMod.add(lblMasseBalleKg);
		enregistrerUnComposant(lblMasseBalleKg) ;



		chckActiveDesactiveSon = new JCheckBox("Désactiver le son de la table et des obstacles.");
		chckActiveDesactiveSon.setSelected(true);
		chckActiveDesactiveSon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				allumerEtDesactiverLeSon();
			}
		});
		chckActiveDesactiveSon.setFont(new Font("Tahoma", Font.PLAIN, 20));
		chckActiveDesactiveSon.setBounds(43, 249, 509, 45);
		panelMod.add(chckActiveDesactiveSon);
		enregistrerUnComposant(chckActiveDesactiveSon);

		JLabel lblModG = new JLabel("Modification de la planète (g) : ");
		lblModG.setBounds(13, 60, 417, 24);
		panelMod.add(lblModG);
		lblModG.setHorizontalAlignment(SwingConstants.TRAILING);
		lblModG.setFont(new Font(nomPolice, Font.PLAIN, 20));
		enregistrerUnComposant(lblModG) ;


		btnLancer = new JButton("Lancer");
		btnLancer.setBounds(623, 852, 133, 127);
		btnLancer.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				btnF.setEnabled(false);
				menuBarFichier.setEnabled(false);
				lancerBalleMaintenir();
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				lancerBalleRelacher();
			}
		});
		btnLancer.setFont(new Font(nomPolice, Font.PLAIN, 30));
		OutilsImage.lireImageEtPlacerSurBouton("boutonLance.png", btnLancer);
		contentPane.add(btnLancer);
		enregistrerUnComposant(btnLancer) ;

		btnF = new JButton("F");
		btnF.setBounds(776, 851, 141, 127);
		btnF.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
				requestFocusInWindow();
				chargerUneTablePredefinie("table7.bin") ;
			}
		});
		btnF.setFont(new Font(nomPolice, Font.PLAIN, 20));
		OutilsImage.lireImageEtPlacerSurBouton("boutonF.png", btnF);
		contentPane.add(btnF);
		enregistrerUnComposant(btnF) ;

		btnArreterJouer = new JToggleButton("Jouer");
		btnArreterJouer.setBounds(927, 851, 141, 127);
		btnArreterJouer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				scene.requestFocusInWindow();
				requestFocusInWindow();
				gererBoutonJouerArreter();
			}
		});
		btnArreterJouer.setFont(new Font(nomPolice, Font.PLAIN, 15));
		OutilsImage.lireImageEtPlacerSurBouton("boutonJouer.png", btnArreterJouer);
		contentPane.add(btnArreterJouer);
		enregistrerUnComposant(btnArreterJouer) ;

		btnRecommencer = new JButton("Recommencer");
		btnRecommencer.setBounds(1078, 851, 141, 127);
		btnRecommencer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				recommencer() ;
			}
		});
		btnRecommencer.setFont(new Font(nomPolice, Font.PLAIN, 15));
		OutilsImage.lireImageEtPlacerSurBouton("boutonRecommence.png", btnRecommencer);
		contentPane.add(btnRecommencer);
		enregistrerUnComposant(btnRecommencer) ;

		btnPas = new JButton("+1");
		btnPas.setBounds(1245, 852, 141, 127);
		btnPas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				requestFocusInWindow();
				scene.requestFocusInWindow();
				gererSautDePas();
			}
		});
		btnPas.setFont(new Font(nomPolice, Font.PLAIN, 20));
		OutilsImage.lireImageEtPlacerSurBouton("boutonPas.png",btnPas);
		contentPane.add(btnPas);
		enregistrerUnComposant(btnPas); 
		jauge.setBounds(623, 583, 133, 258);

		jauge.setBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(0, 0, 0), null));
		jauge.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if(evt.getPropertyName().equals("Etirement")) {
					double etirement = (double) evt.getNewValue();
					scene.changerEtirementRessortDepart(etirement);
				}

			}
		});
		contentPane.add(jauge);

		JLabel lblTitre = new JLabel("FrissonBoum : Le frisson de la victoire !");
		lblTitre.setBounds(507, 10, 909, 35);
		lblTitre.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitre.setFont(new Font("Lucida Bright", Font.BOLD | Font.ITALIC, 40));
		contentPane.add(lblTitre);
		enregistrerUnComposant(lblTitre) ;

		panBoutonsRadio = new JPanel();
		panBoutonsRadio.setBounds(951, 121, 388, 171);
		contentPane.add(panBoutonsRadio) ;
		enregistrerUnComposant(panBoutonsRadio) ;

		JRadioButton radGraphEnergie = new JRadioButton(lesGraphiques.get(0).getTitre()) ;
		radGraphEnergie.setFont(new Font(nomPolice, Font.PLAIN, 13));
		radGraphEnergie.setBounds(6, 5, 376, 54);
		radGraphEnergie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modifierGraphique(radGraphEnergie.getText()) ;
			}
		});
		panBoutonsRadio.setLayout(null);
		btngrpGraphiques.add(radGraphEnergie);
		radGraphEnergie.setSelected(true);
		panBoutonsRadio.add(radGraphEnergie);
		enregistrerUnComposant(radGraphEnergie) ;

		JRadioButton radGraphVitesse = new JRadioButton(lesGraphiques.get(1).getTitre());
		radGraphVitesse.setFont(new Font(nomPolice, Font.PLAIN, 13));
		radGraphVitesse.setBounds(6, 59, 376, 54);
		radGraphVitesse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modifierGraphique(radGraphVitesse.getText()) ;
			}
		});
		btngrpGraphiques.add(radGraphVitesse);
		panBoutonsRadio.add(radGraphVitesse);
		enregistrerUnComposant(radGraphVitesse) ;

		JRadioButton radGraphAcceleration = new JRadioButton(lesGraphiques.get(2).getTitre());
		radGraphAcceleration.setFont(new Font(nomPolice, Font.PLAIN, 13));
		radGraphAcceleration.setBounds(6, 113, 376, 54);
		radGraphAcceleration.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modifierGraphique(radGraphAcceleration.getText()) ;
			}
		});
		btngrpGraphiques.add(radGraphAcceleration);
		panBoutonsRadio.add(radGraphAcceleration);
		enregistrerUnComposant(radGraphAcceleration) ;


		JPanel panelInclinaison = new JPanel();
		panelInclinaison.setBounds(621, 73, 320, 283);
		contentPane.add(panelInclinaison);
		panelInclinaison.setLayout(null);
		enregistrerUnComposant(panelInclinaison);


		spnInclinaison = new JSpinner();
		spnInclinaison.setBounds(226, 226, 84, 35);
		panelInclinaison.add(spnInclinaison);
		spnInclinaison.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				appliquerChangementTriangle();
				requestFocusInWindow();
				scene.requestFocusInWindow();
			}
		});
		spnInclinaison.setFont(new Font(nomPolice, Font.PLAIN, 15));
		spnInclinaison.setModel(new SpinnerNumberModel(20.0, -90.0, 90.0, 1.0));
		enregistrerUnComposant(spnInclinaison) ;

		JLabel lblInclinaison = new JLabel("Inclinaison de la table en degrés :");
		lblInclinaison.setBounds(10, 231, 206, 24);
		panelInclinaison.add(lblInclinaison);
		lblInclinaison.setFont(new Font("Tahoma", Font.PLAIN, 14));
		enregistrerUnComposant(lblInclinaison) ;

		triangleInclinaison = new TriangleInclinaison(20);
		triangleInclinaison.setBounds(44, 0, 237, 229);
		panelInclinaison.add(triangleInclinaison);

		triangleInclinaison.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));

		JPanel panelTempsDuSleep = new JPanel();
		panelTempsDuSleep.setBounds(977, 425, 320, 88);
		contentPane.add(panelTempsDuSleep);
		panelTempsDuSleep.setLayout(null);
		enregistrerUnComposant(panelTempsDuSleep);

		JLabel lblVitesseApplication = new JLabel("Modifier la vitesse de l'application (plus c'est petit, plus c'est rapide)");
		lblVitesseApplication.setBounds(10, 0, 319, 26);
		panelTempsDuSleep.add(lblVitesseApplication);
		lblVitesseApplication.setFont(new Font("Tahoma", Font.PLAIN, 10));
		enregistrerUnComposant(lblVitesseApplication);

		lblValeurDuTempsDuSleep = new JLabel("30");
		lblValeurDuTempsDuSleep.setFont(new Font("Tahoma", Font.PLAIN, 26));
		lblValeurDuTempsDuSleep.setBounds(264, 25, 49, 52);
		panelTempsDuSleep.add(lblValeurDuTempsDuSleep);
		enregistrerUnComposant(lblValeurDuTempsDuSleep);

		sliderTempsDuSleep = new JSlider();
		sliderTempsDuSleep.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				augmenterLaVitesseDeLApplication();
			}
		});
		sliderTempsDuSleep.setBounds(41, 32, 200, 45);
		panelTempsDuSleep.add(sliderTempsDuSleep);
		sliderTempsDuSleep.setValue(30);
		sliderTempsDuSleep.setPaintTicks(true);
		sliderTempsDuSleep.setPaintLabels(true);
		sliderTempsDuSleep.setMajorTickSpacing(10);
		sliderTempsDuSleep.setMinorTickSpacing(5);
		sliderTempsDuSleep.setMinimum(1);
		sliderTempsDuSleep.setMaximum(50);
		enregistrerUnComposant(sliderTempsDuSleep);

		panProgression = new JPanel();
		panProgression.setBounds(617, 384, 350, 125);
		contentPane.add(panProgression);
		panProgression.setLayout(null);
		enregistrerUnComposant(panProgression);

		barreDeProgression = new BarreDeProgression();
		barreDeProgression.setBounds(0, 0, 348, 97);
		panProgression.add(barreDeProgression);
		barreDeProgression.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Votre progression par rapport \u00E0 votre ancien score", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		barreDeProgression.poserUneValeurMaximale(gest.obtenirLesScores().get(0).getValeurScore()) ;

		JLabel lblIndicMeilleurScore = new JLabel("Meilleur score : ");
		lblIndicMeilleurScore.setHorizontalAlignment(SwingConstants.TRAILING);
		lblIndicMeilleurScore.setBounds(0, 101, 171, 16);
		panProgression.add(lblIndicMeilleurScore);
		enregistrerUnComposant(lblIndicMeilleurScore) ;

		lblMeilleurScore = new JLabel(gest.obtenirLesScores().get(0).getValeurScore() + " points") ;
		lblMeilleurScore.setBounds(183, 101, 161, 16);
		panProgression.add(lblMeilleurScore);
		enregistrerUnComposant(lblMeilleurScore) ;

		JLabel lblSec = new JLabel("s");
		lblSec.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblSec.setBounds(332, 30, 70, 32);
		contentPane.add(lblSec);
		enregistrerUnComposant(lblSec);

		btnReinitialiserLesScores = new JButton("Réinitialiser les scores");
		btnReinitialiserLesScores.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ArrayList<Score> justine = new ArrayList<Score>() ; 
				for (int i = 0 ; i < 5 ; i++)
					justine.add(new Score()) ;

				gest.modifierLesScores(justine) ;
				barreDeProgression.poserUneValeurMaximale(gest.obtenirLesScores().get(0).getValeurScore()) ;
				lblMeilleurScore.setText(gest.obtenirLesScores().get(0).getValeurScore() + " points") ;
			}
		});
		btnReinitialiserLesScores.setBounds(1041, 332, 178, 67);
		contentPane.add(btnReinitialiserLesScores);

		appliquerTheme(gest.obtenirThemeActuel()) ;
	}

	/**
	 * Méthode permettant d'effectuer un changement de masse de la balle et de mettre en aperçu sur une 
	 * étiquette la nouvelle masse.
	 */
	//Aimé Melançon
	private void changementDeMasse() {
		scene.changerMasseDesBalles((double) curseurMasse.getValue());
		lblMasseBalleKg.setText(curseurMasse.getValue() +" kg");
		requestFocusInWindow();
		scene.requestFocusInWindow();

	}

	/**
	 * Méthode permettant de propulser la balle avec une touche du clavier.
	 * @param e La touche du clavier
	 */
	//Aimé Melançon
	private void arretPropulsionBalle(KeyEvent e) {
		char code = e.getKeyChar();
		if(code == KeyEvent.VK_SPACE) {
			System.out.println("Je propoulse la balle");
			lancerBalleRelacher();
		}
	}

	/**
	 * Méthode permettant d'arrêter la propulsion la balle avec une touche du clavier.
	 * @param e La touche du clavier
	 */
	//Aimé Melançon
	private void propulsionBalle(KeyEvent e) {
		char code = e.getKeyChar();
		if(code == KeyEvent.VK_SPACE) {
			System.out.println("J'arrête la propulsion la balle");
			lancerBalleMaintenir();
		}
	}

	/**
	 * Méthode permettant de gérer le saut de pas de l'application
	 */
	//Aimé Melançon
	private void gererSautDePas() {
		try {
			scene.sautDePas();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	/**
	 * Méthode permettant de gérer la constante de gravitation en appelant MoteurPhysique.
	 */
	//Aimé Melançon
	private void gererLaConstanteDeGravitation() {
		try {
			MoteurPhysique.setG(cboxG.getSelectedItem().toString());
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	/**
	 * Méthode permettant de gérer le bouton "arreter et jouer"
	 */
	//Aimé Melançon
	private void gererBoutonJouerArreter() {
		if(btnArreterJouer.isSelected()) {
			btnArreterJouer.setText("Arrêter");
			OutilsImage.lireImageEtPlacerSurBouton("boutonArrete.png", btnArreterJouer);
			scene.demarrerAnimation();
			schemaDesForces.poserUneBalle(scene.getTableActuelle().getBalleDebut()) ;
			btnPas.setEnabled(false);
			menuBarFichier.setEnabled(false);
			btnF.setEnabled(false);
		} else {
			scene.arreterAnimation();
			btnArreterJouer.setText("Jouer");
			OutilsImage.lireImageEtPlacerSurBouton("boutonJouer.png", btnArreterJouer);
			btnPas.setEnabled(true);
			menuBarFichier.setEnabled(true);
			btnF.setEnabled(true);
		}

	}

	/**
	 * Méthode permettant de charger une table prédéfinie
	 * @param nomDeLaTable Le nom de la table (i.e "table0.bin")
	 */
	//Aimé Melançon
	private void chargerUneTablePredefinie(String nomDeLaTable) {
		scene.reinitialiser();
		config.reinitialiserTouches();
		scene.changerTable(gest.lireTableInit(nomDeLaTable)) ;
		requestFocusInWindow();
		scene.requestFocusInWindow();
	}

	/**
	 * Méthode permettant de faire afficher le panneau APropos
	 */
	//Aimé Melançon
	private void creerAPropos() {
		requestFocusInWindow();
		scene.requestFocusInWindow();

		JOptionPane.showMessageDialog(null, new PanelAPropos(), "À propos de cette application :",
				JOptionPane.PLAIN_MESSAGE) ;
	}

	/**
	 * Méthode permettant d'activer et de désactiver tous les sons de la table.
	 */
	//Aimé Melançon
	private void allumerEtDesactiverLeSon() {
		requestFocusInWindow();
		scene.requestFocusInWindow();

		GererSon.setAllumerFermer(chckActiveDesactiveSon.isSelected());

		if(chckActiveDesactiveSon.isSelected()) {
			chckActiveDesactiveSon.setText("Désactiver le son de la table et des obstacles.");
		} else {
			chckActiveDesactiveSon.setText("Activer le son de la table et des obstacles.");
		}
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
	 * Méthode servant à ajouter des composant dans la liste des composants pour la modification.
	 * @param lesPanels : Les composants à ajouter dans la liste.
	 */
	// Par Elias Kassas
	private void enregistrerDesComposants(ArrayList<JPanel> lesPanels) {
		lesComposants.addAll(lesPanels) ;
	}

	/**
	 * Méthode privée servant à réinitialiser l'animation dans la scène.
	 */
	private void recommencer() {
		menuBarFichier.setEnabled(true);
		btnF.setEnabled(true);
		btnPas.setEnabled(true);
		scene.requestFocusInWindow();
		reinitialiserGraphiques() ;
		btnArreterJouer.setSelected(false) ;
		btnArreterJouer.setText("Jouer") ;
		OutilsImage.lireImageEtPlacerSurBouton("boutonJouer.png", btnArreterJouer);
		lblTemps.setText(String.format("%.3f", 0d)) ;
		schemaDesForces.reinitialiser() ;

		menu.ajouterScore(new Score(Scene.getPoints().getPointsTotauxAffiches())) ;
		barreDeProgression.reinitialiser() ;
		barreDeProgression.poserUneValeurMaximale(gest.obtenirLesScores().get(0).getValeurScore()) ;
		lblMeilleurScore.setText(gest.obtenirLesScores().get(0).getValeurScore() + " points") ;
		scene.reinitialiser() ;
		requestFocusInWindow() ;
		scene.requestFocusInWindow() ;
	}

	/**
	 * Méthode permettant de changer le coefficient de frottement de la table.
	 * @param nomDuTypeDeSol Le type de sol sélectionné et envoyé comme un String à la méthode
	 * @throws Exception L'erreur si le sol n'existe pas dans les sols prédéfinis.
	 */
	//Aimé Melançon
	/*private void setCoefFrottement(String nomDuTypeDeSol) throws Exception {
		switch(nomDuTypeDeSol) {
		case "bois":
			scene.changerCoefFrott(0.58);
			scene.changerCouleurSurface(Color.RED.darker().darker().darker());
			break;
		case "glace":
			scene.changerCoefFrott(0.02);
			scene.changerCouleurSurface(Color.CYAN);
			break;
		case "béton":
			scene.changerCoefFrott(0.7);
			scene.changerCouleurSurface(Color.GRAY);
			break;
		case "eau":
			scene.changerCoefFrott(0.43);
			scene.changerCouleurSurface(Color.BLUE);		 
			break;
		case "plastique":
			scene.changerCoefFrott(0.49);
			scene.changerCouleurSurface(new Color(0,100,100,100));
			break;
		case "fer":
			scene.changerCoefFrott(0.45);
			scene.changerCouleurSurface(Color.DARK_GRAY);
			break;
		default :
			throw new Exception("Ce type de table n'existe pas.");
		}

		System.out.println("Le nouveau coefficient de frottement de la table est de "+ scene.getTableActuelle().getCoefDeFrottement());
	}*/

	/**
	 * Méthode privée permettant de créer et d'afficher la fenêtre de configuration
	 * sans "supprimer" la fenêtre de menu.
	 */
	// Par Elias Kassas
	private void creerEtAfficherConfig() {
		requestFocusInWindow();
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

			barreDeProgression.remplacerLActuelleValeur(Scene.getPoints().getPointsTotauxAffiches()) ;
		}

		// Paramètres physiques
		if (evt.getPropertyName().equals("table")) {
			majGraphiques(evt) ;
			majSchemaForces(evt) ;
		}

		// Paramètres informatiques
		recevoirEtModifierParametresInformatiques(evt) ;

		if (evt.getPropertyName().equals("etatAnimation")) {
			btnArreterJouer.setSelected(false) ;
			reinitialiserGraphiques() ;
			menu.ajouterScore(new Score(Scene.getPoints().getPointsTotauxAffiches())) ;
		}

		if (evt.getPropertyName().equals("etirement")) {
			try {
				scene.propulser(scene.getTableActuelle().getBalleDebut(), new Vecteur3D(0, 
						(Double) evt.getNewValue())) ;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * Méthode permettant de mettre à jour les paramètres informatiques de l'application.
	 * @param evt : L'écouteur de propertyChange.
	 */
	// Par Elias Kassas
	private void recevoirEtModifierParametresInformatiques(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("ic")) {
			InfoConfiguration ic = (InfoConfiguration) evt.getNewValue() ;
			System.out.println("ic = " + ic) ;

			nomPolice = ic.getPoliceDeCaractere() ;

			appliquerTheme(ic.getTheme()) ;
			toucheGauche(ic);
			toucheDroite(ic);
			musique(ic);

			revalidate() ;
			repaint() ;
		}
	}

	/**
	 * Méthode permettant de changer et de faire jouer de la musique au sein de la fenêtre principale.
	 * @param ic Objet InfoConfiguration dont on veut extraire les informations pour la musique.
	 */
	//Aimé Melançon
	private void musique(InfoConfiguration ic) {
		if(ic.isMusique()) {
			if(ic.getChoixMusique()!="Sélectionner une musique.") {
				//	musique= new OutilsSon();
				musique.arreterUneMusique();
				musique= new OutilsSon();
				musique.chargerUnSonOuUneMusique(ic.getChoixMusique());
				musique.jouerUneMusique();
			}else {
				musique.arreterUneMusique();
				//musique.chargerUneMusiqueEtJouer(ic.getChoixMusique());
			}
		}else {
			musique.arreterUneMusique();
		}
	}

	/**
	 * Méthode permettant de mettre à jour la touche associée au flipper gauche 
	 * @param ic Objet InfoConfiguration dont on veut extraire les informations pour les touches.
	 */
	//Aimé Melançon
	private void toucheGauche(InfoConfiguration ic) {
		scene.setCoteGauche(ic.getToucheFlipperGauche());
	}

	/**
	 * Méthode permettant de mettre à jour la touche associée au flipper droit 
	 * @param ic Objet InfoConfiguration dont on veut extraire les informations pour les touches.
	 */
	//Aimé Melançon
	private void toucheDroite(InfoConfiguration ic) {
		scene.setCoteDroit(ic.getToucheFlipperDroit());
	}

	/**
	 * Méthode privée servant à appliquer un thème à l'application.
	 * @param ic : L'objet InfoConfiguration dont on veut extraire les informations.
	 */
	// Par Elias Kassas
	private void appliquerTheme(Theme theme) {
		for (Component composant : lesComposants) {
			Font policeAct = composant.getFont() ;
			if (policeAct == null)
				revalidate() ;
			else
				composant.setFont(new Font(nomPolice, policeAct.getStyle(), policeAct.getSize())) ;
			if (!(composant instanceof JMenuItem) && !(composant instanceof JMenu) && 
					!(composant instanceof JMenuBar)) {
				composant.setForeground(theme.getCouleurPolice()) ;

				if (!(composant instanceof JComboBox))
					composant.setBackground(theme.getCouleurSecondaire()) ;
			}

			if (composant instanceof JPanel) {
				composant.setBackground(theme.getCouleurSecondaire()) ;
				((JComponent) composant).setBorder(new LineBorder(theme.getCouleurTertiaire(), 
						1, true)) ;
			}
		}

		menu.appliquerTheme(theme) ;
		contentPane.setBackground(theme.getCouleurPrimaire()) ;
		gest.modifierThemeActuel(theme) ;
		revalidate() ;
		repaint() ;
	}

	/**
	 * Méthode permettant de mettre à jour le schéma des forces.
	 * @param evt : L'écouteur de propertyChange.
	 */
	// Par Elias Kassas
	private void majSchemaForces(PropertyChangeEvent evt) {
		Table tRecue = (Table) evt.getNewValue() ;
		Balle bRecue = tRecue.getBalleDebut() ;
		schemaDesForces.poserUneBalle(bRecue);
	}

	/**
	 * Méthode privée pour la mise à jour des graphiques.
	 * @param evt : L'écouteur de propertyChange.
	 */
	// Par Elias Kassas
	private void majGraphiques(PropertyChangeEvent evt) {
		Table tableRecue = (Table) evt.getNewValue() ;
		Balle balleGraphiques = tableRecue.getBalleDebut() ;

		// MàJ du Graphique de la vitesse de la balle en fonction du temps
		Vecteur3D vitesseRecue = MoteurPhysique.convertirCentimetresEnMetres(balleGraphiques.
				getVitesse()) ;
		graphiqueVitesseFonctionTemps.ajouterUnPointSurLaCourbe(tempsTotalScene, 
				vitesseRecue.module()) ;
		lblModuleVit.setText(String.format("%.3f", vitesseRecue.module())) ;
		lblVitX.setText(String.format("%.3f", vitesseRecue.getX())) ;
		lblVitY.setText(String.format("%.3f", -vitesseRecue.getY())) ;

		// MàJ du Graphique de l'accélération de la balle en fonction du temps
		Vecteur3D accelerationRecue = MoteurPhysique.convertirCentimetresEnMetres(balleGraphiques.
				getAcceleration()) ;
		graphiqueAccelerationFonctionTemps.ajouterUnPointSurLaCourbe(tempsTotalScene, 
				accelerationRecue.module()) ;
		lblAccel.setText(String.format("%.3f", accelerationRecue.module())) ;
		lblAccelX.setText(String.format("%.3f", accelerationRecue.getX())) ;
		lblAccelY.setText(String.format("%.3f", -accelerationRecue.getY())) ;

		// MàJ du Graphique de l'énergie en fonction du temps
		double sommeEnergies = balleGraphiques.calculSommeDesEnergies((double) spnInclinaison.
				getValue(), 0.0)/10000d ;
		lblNbJoule.setText(String.format("%.3f", sommeEnergies)) ;
		graphiqueEnergieFonctionTemps.ajouterUnPointSurLaCourbe(tempsTotalScene, sommeEnergies) ;
		if (balleGraphiques.getSommeDesForces() != null)
			lblNbForce.setText(String.format("%.3f", balleGraphiques.getSommeDesForces().module())) ;
	}

	/**
	 * Méthode servant à réinitialiser les graphiques ainsi que les valeurs affichées dans
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
	 * Méthode permettant de manipuler la phase du bouton JouerArreter pour qu'il soit en mode 
	 * jouer ou arrêter.
	 * @param jouerArreter Vrai si c'est "jouer" et faux si c'est "arrêter".
	 */
	//Aimé Melançon
	public static void setBouton(boolean jouerArreter) {
		if(!jouerArreter) {
			menuBarFichier.setEnabled(true);
			btnF.setEnabled(true);
			btnArreterJouer.setText("Jouer");
			OutilsImage.lireImageEtPlacerSurBouton("boutonJouer.png", btnArreterJouer);
		}else {
			menuBarFichier.setEnabled(false);
			btnF.setEnabled(false);
			btnArreterJouer.setText("Arrêter");
			OutilsImage.lireImageEtPlacerSurBouton("boutonArrete.png", btnArreterJouer);
		}

	}

	/**
	 * Méthode privée servant à créer et afficher l'application satellite.
	 * Cette méthode est temporaire pour la première mêlée.
	 */
	// Par Elias Kassas
	/*private void creerAppliSatellite() {
		AppSatelliteElias app = new AppSatelliteElias() ;
		app.setVisible(true) ;
		contentPane.setEnabled(false) ;
	}*/

	/**
	 * Méthode privée servant à créer le menu et disposer de l'application.
	 */
	// Par Elias Kassas
	private void creerMenuEtDisposer() {
		menu.setVisible(true) ;
		scene.reinitialiser() ;
		scene.nouvelleTable() ;
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
			scene.setBounds(10, 74, 1700, 904) ;
			graphiqueEnergieFonctionTemps.setVisible(false) ;
			graphiqueVitesseFonctionTemps.setVisible(false) ;
			graphiqueAccelerationFonctionTemps.setVisible(false) ;
			panelMod.setVisible(false) ;
			scene.setModeCreation(true) ;
			panBoutonsRadio.setVisible(false) ;
			panelVoulu.setVisible(false) ;
			recommencer() ;
			lblTemps.setText(String.format("%.3f", 0d)) ;
			btnArreterJouer.setVisible(false);
			panProgression.setVisible(false) ;
			schemaDesForces.reinitialiser() ;
			schemaDesForces.setVisible(false) ;
			btnModeEdition.setText("Mode scientifique") ;
		} else {
			// Mode scientifique.
			scene.setBounds(10, 74, 604, 904) ;
			graphiqueEnergieFonctionTemps.setVisible(true) ;
			graphiqueVitesseFonctionTemps.setVisible(true) ;
			graphiqueAccelerationFonctionTemps.setVisible(true) ;
			scene.setModeCreation(false) ;
			panelMod.setVisible(true) ;
			panBoutonsRadio.setVisible(true) ;
			panelVoulu.setVisible(true) ;
			btnArreterJouer.setVisible(true);
			panProgression.setVisible(true) ;
			schemaDesForces.setVisible(true) ;
			scene.setNullObstaclePris();
			btnModeEdition.setText("Mode édition") ;
		}
	}

	/**
	 * Méthode permettant de forcer le bouton avancer un pas à être activé ou désactivé.
	 * @param reponse Si on veut activer le bouton ou on veut le désactiver.
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
		config.reinitialiserTouches();
		gest.sauvegarder(scene.getTableActuelle()) ;
	}

	/**
	 * Méthode privée permettant de charger une table précédemment faite.
	 */
	// Par Elias Kassas
	private void chargerUneTable() {
		scene.requestFocusInWindow();
		JFileChooser jfc = new JFileChooser() ;
		jfc.setCurrentDirectory(gest.getDossierUtilisateur()) ;
		int resultat = jfc.showDialog(null, "Choisir") ;

		// Si l'utilisateur a pesé sur le bouton "Choisir", on charge la table choisie.
		if (resultat == JFileChooser.APPROVE_OPTION) {
			File fichierChoisi = jfc.getSelectedFile() ;
			Table tableLue = (Table) gest.lire(fichierChoisi.getName()) ;
			tableLue.reinitialiserTable() ;
			config.reinitialiserTouches() ;
			scene.changerTable(tableLue) ;
		}
	}

	/**
	 * Méthode permettant de charger une scène en entrant dans l'application.
	 * @param fichierTable : Le fichier de Table à charger sur l'application.
	 */
	// Par Elias Kassas
	public void chargerUneTable(File fichierTable) {
		scene.requestFocusInWindow() ;
		Table tableLue = (Table) gest.lire(fichierTable.getName()) ;
		scene.changerTable(tableLue) ;
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
	 * Méthode permettant d'avoir la force de rappel du ressort qui propulse la balle.
	 * @return Retourne la force de rappel du ressort.
	 */
	//Aimé Melançon
	public double getConstanteRappel() {
		scene.requestFocusInWindow();
		return	Double.parseDouble(spnK.getValue().toString());
	}

	/**
	 * Méthode permettant d'avoir la masse de la balle choisie.
	 * @return Retourne la masse voulue de la balle.
	 */
	//Aimé Melançon
	public double getMasseBalle() {
		scene.requestFocusInWindow();
		return Double.parseDouble(curseurMasse.getValue()+"");
	}

	/**
	 * Méthode permettant de modifier le graphique affiché à partir de boutons radios
	 * @param titreGraphique : Le titre du graphique voulu.
	 */
	// Par Elias Kassas
	private void modifierGraphique(String titreGraphique) {
		boolean trouve = false ;

		for (int i = 0 ; i < lesGraphiques.size() ; i++) {
			if (lesGraphiques.get(i).getTitre().equalsIgnoreCase(titreGraphique) && !trouve) {
				trouve = true ;
				//System.out.println(titreGraphique) ;
				contentPane.remove(panelVoulu) ;
				lesComposants.remove(panelVoulu) ;
				panelVoulu = lesPanels.get(i) ;
				contentPane.add(panelVoulu) ;
				enregistrerUnComposant(panelVoulu) ;
				revalidate() ;
				repaint() ;
			}
		}
	}

	/**
	 * Méthode appliquant tous les changements à l'application lors du maintien du bouton "lancer"
	 */
	//Félix Lefrançois
	private void lancerBalleMaintenir() {
		jauge.demarrer();
		btnArreterJouer.setText("Arrêter");
		btnArreterJouer.setSelected(true);
		OutilsImage.lireImageEtPlacerSurBouton("boutonArrete.png", btnArreterJouer);
		scene.demarrerAnimation();
		schemaDesForces.poserUneBalle(scene.getTableActuelle().getBalleDebut()) ;
		btnPas.setEnabled(false);
	}

	/**
	 * Méthode appliquant tous les changements à l'application lors du relâchement du bouton "lancer"
	 */
	//Félix Lefrançois
	private void lancerBalleRelacher() {
		jauge.terminer();
		scene.changerEtirementRessortDepart(0.00);
	}

	/**
	 * Méthode permettant de changer le temps du sleep de scene gràce à un curseur
	 */
	//Félix Lefrançois
	private void augmenterLaVitesseDeLApplication() {
		scene.setTempsDuSleep(sliderTempsDuSleep.getValue());
		jauge.setTempsDuSleep(sliderTempsDuSleep.getValue());
		lblValeurDuTempsDuSleep.setText(sliderTempsDuSleep.getValue()+"");
		requestFocusInWindow();
		scene.requestFocusInWindow();
	}

	/**
	 * Méthode qui applique tous les changements à l'application lorsque l'inclinaison de la table est 
	 * changée
	 */
	//Félix Lefrançois
	private void appliquerChangementTriangle() {
		scene.requestFocusInWindow();
		scene.changerInclinaisonTable((double)spnInclinaison.getValue());
		triangleInclinaison.setAngle((double) spnInclinaison.getValue());
	}

	/**
	 * Méthode appliquant les changements visuels à l'affichage des touches sur les flippers
	 * @param evt Le property change utilisé
	 */
	//Félix Lefrançois
	private void appliquerChangementAffichageTouches(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("Gauche")) {
			String nouvelleLettre = (String) evt.getNewValue();
			scene.changerAffichageToucheGauche(nouvelleLettre);
		}

		if(evt.getPropertyName().equals("Droite")) {
			String nouvelleLettre = (String) evt.getNewValue();
			scene.changerAffichageToucheDroite(nouvelleLettre);
		}

	}

}

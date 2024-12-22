package application;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.KeyAdapter;

/**
 * Un panneau de configuration qui va permettre à l'utilisateur de configurer la fenêtre principale comme il le souhaite.
 * La classe Configuration hérite de la classe mère JFrame.
 * 
 * @author Aimé Melançon
 * @author Elias Kassas
 */
public class Configuration extends JFrame {
	/**  Identifiant de classe **/ 
	private static final long serialVersionUID = 1L;
	/**Le panneau englobant tout les composants. **/
	private JPanel cPConfiguration;
	/**Variable de type JTextField qui permet à l'utilisateur de sélectionner ses touches de claviers (Côté droit et Côté gauche) **/
	private JTextField txtFCoteDroit,txtFCoteGauche;
	/**Variable de type JLabel qui permet d'afficher le titre.**/
	private JLabel lblTitreConfig;
	/**Choix de musique libre de droit implémenté dans l'application. **/
	private JComboBox<Font> cBoxMusique;
	/**Choix de la police de caractère selon plusieurs choix mis à la disposition de l'utilisateur. **/
	private JComboBox<Font> cBoxPolice;
	/**Choix du thème de l'application. **/
	private JComboBox<Font> cBoxModeApp;
	/**Texte présentant le choix du thème. **/
	private JLabel lblApparence;
	/**Texte présentant le bouton radio de la musique. **/
	private JLabel lblActivationMusique;
	/** Texte présentant le choix de musique**/
	private JLabel lblMusiqueChoisis;
	/**Texte présentant le choix de la touche droite. **/
	private JLabel lblChoixDroit;
	/**Texte présentant le choix de la touche gauche. **/
	private JLabel lblChoixGauche;
	/**Texte présentant le choix de la police de caractère. **/
	private JLabel lblChoixPolice;
	/** Texte présentant l'avertissement du fonctionnement d'assignement de touche **/
	private JLabel lblAvertissementTouche;
	/** Support pour lancer des évènements de type PropertyChange. **/
	private PropertyChangeSupport pcs = new PropertyChangeSupport(this) ;
	/**Permet de sélectionner une musique libre de droit.**/
	private JCheckBox chckBoxMusique;
	/** Zone de regroupement munie d'une barre de défilement pour les logs. **/
	private JScrollPane spLogsConfig ;
	/** Zone de saisie à multiples lignes prévue pour les logs du panneau de configuration. **/
	private JTextArea txtaLogsConfig ;
	/** Objet contenant les paramètres de configuration du panneau de configuration. **/
	private InfoConfiguration ic = null ;
	/**Variable permettant de choisir d'utiliser ou non les flèches du clavier pour manipuler les flippers. **/
	private JCheckBox chckbxFleche;
	/** Variable de type boolean permettant de choisir si on veut manipuler les flippers avec les touches ou 
	 * les flèches. **/
	private static boolean fleche=false;
	/**Variable de type char qui possède la touche attribuée pour la droite. **/
	private static char coteDroit ='d' ;
	/**Variable de type char qui possède la touche attribuée pour la gauche. **/
	private static char coteGauche = 'a' ;

	/**
	 * Lancement de la fenêtre de configuration.
	 * @param args L'argument de lancement
	 */
	//Aimé Melançon
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Configuration frame = new Configuration();
					frame.setVisible(true);
					frame.cPConfiguration.requestFocusInWindow();
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
	@SuppressWarnings("unchecked")
	public Configuration() {
		setTitle("Configuration");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 568, 900);
		cPConfiguration = new JPanel();
		cPConfiguration.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				testKey( e);
			}
		});
		cPConfiguration.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(cPConfiguration);
		cPConfiguration.setLayout(null);

		lblTitreConfig = new JLabel("Configuration");
		lblTitreConfig.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitreConfig.setFont(new Font("Times New Roman", Font.BOLD, 30));
		lblTitreConfig.setBounds(50, 0, 422, 94);
		cPConfiguration.add(lblTitreConfig);

		txtFCoteDroit = new JTextField("d");
		txtFCoteDroit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cPConfiguration.requestFocusInWindow();
				int nbLettre = txtFCoteDroit.getText().length();

				if(nbLettre == 1) {
					char mot =	txtFCoteDroit.getText().charAt(0);
					coteDroit =mot;
					String correction = mot + "";
					txtFCoteDroit.setText(correction);

					afficherMessageEtLancerPropertyChange("Vous avez modifié la touche droite "
							+ "du flipper pour : " + correction) ;
				}
			}
		});
		txtFCoteDroit.setHorizontalAlignment(SwingConstants.CENTER);
		txtFCoteDroit.setFont(new Font("Tahoma", Font.PLAIN, 40));

		txtFCoteDroit.setBounds(371, 494, 89, 81);
		cPConfiguration.add(txtFCoteDroit);
		txtFCoteDroit.setColumns(10);

		txtFCoteGauche = new JTextField("a");
		txtFCoteGauche.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cPConfiguration.requestFocusInWindow();
				int nbLettre = txtFCoteGauche.getText().length();

				if(nbLettre == 1) {
					char mot =	txtFCoteGauche.getText().charAt(0);
					coteGauche= mot;
					String correction = mot+"";
					txtFCoteGauche.setText(correction);

					afficherMessageEtLancerPropertyChange("Vous avez modifié la touche gauche "
							+ "du flipper pour : " + correction) ;
				}
			}
		});
		txtFCoteGauche.setHorizontalAlignment(SwingConstants.CENTER);
		txtFCoteGauche.setFont(new Font("Tahoma", Font.PLAIN, 40));
		txtFCoteGauche.setColumns(10);
		txtFCoteGauche.setBounds(371, 585, 89, 81);
		cPConfiguration.add(txtFCoteGauche);

		lblChoixDroit = new JLabel("Changer la touche du flipper droit :");
		lblChoixDroit.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblChoixDroit.setBounds(10, 507, 319, 50);
		cPConfiguration.add(lblChoixDroit);

		lblChoixGauche = new JLabel("Changer la touche du flipper gauche :");
		lblChoixGauche.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblChoixGauche.setBounds(10, 602, 351, 50);
		cPConfiguration.add(lblChoixGauche);

		lblMusiqueChoisis = new JLabel("Choix de musique :");
		lblMusiqueChoisis.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMusiqueChoisis.setBounds(10, 377, 256, 50);
		cPConfiguration.add(lblMusiqueChoisis);

		lblActivationMusique = new JLabel("Activer la musique :");
		lblActivationMusique.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblActivationMusique.setBounds(10, 295, 256, 50);
		cPConfiguration.add(lblActivationMusique);

		lblChoixPolice = new JLabel("Police de caractère :");
		lblChoixPolice.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblChoixPolice.setBounds(10, 202, 256, 50);
		cPConfiguration.add(lblChoixPolice);

		lblApparence = new JLabel("Apparence de l'interface :");
		lblApparence.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblApparence.setBounds(10, 106, 256, 50);
		cPConfiguration.add(lblApparence);

		cBoxMusique = new JComboBox();
		cBoxMusique.setModel(new DefaultComboBoxModel(new String[] {"Joblo chante"}));
		cBoxMusique.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cPConfiguration.requestFocusInWindow();
				afficherMessageEtLancerPropertyChange("Vous avez modifié la musique de "
						+ "l'application pour : " + cBoxModeApp.getSelectedItem().toString()) ;
			}
		});
		cBoxMusique.setBounds(294, 382, 216, 40);
		cPConfiguration.add(cBoxMusique);

		cBoxPolice = new JComboBox<Font>();
		cBoxPolice.setModel(new DefaultComboBoxModel(new Font[] {
				new Font("New Times Romans", Font.PLAIN, 14), new Font("Comic Sans Ms", Font.PLAIN, 14)}));
		cBoxPolice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cPConfiguration.requestFocusInWindow();
				afficherMessageEtLancerPropertyChange("Vous avez modifié la police d'écriture de "
						+ "l'application pour : " + ((Font) cBoxPolice.getSelectedItem()).getFontName()) ;
			}
		});
		cBoxPolice.setBounds(278, 207, 216, 40);
		cPConfiguration.add(cBoxPolice);

		cBoxModeApp = new JComboBox();
		cBoxModeApp.setModel(new DefaultComboBoxModel(new String[] {"test1212", "Pomme de terre"}));
		cBoxModeApp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				afficherMessageEtLancerPropertyChange("Vous avez modifié le thème de l'application "
						+ "pour : " + cBoxModeApp.getSelectedItem()) ;
			}
		});
		cBoxModeApp.setBounds(278, 116, 217, 40);
		cPConfiguration.add(cBoxModeApp);

		lblAvertissementTouche = new JLabel("N’oubliez pas d’utiliser la touche entrée pour "
				+ "certifier vos choix de touches.");
		lblAvertissementTouche.setBounds(10, 664, 500, 23);
		cPConfiguration.add(lblAvertissementTouche);

		chckBoxMusique = new JCheckBox("Musique ?");
		chckBoxMusique.setSelected(true);
		chckBoxMusique.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cPConfiguration.requestFocusInWindow();
				if(chckBoxMusique.isSelected()) {
					cBoxMusique.setEnabled(true) ;
					afficherMessageEtLancerPropertyChange("Vous avez décidé de mettre de la musique.") ;
				} else {
					cBoxMusique.setEnabled(false);
					afficherMessageEtLancerPropertyChange("Vous avez décidé de ne pas mettre de la "
							+ "musique.") ;
				}
			}
		});
		chckBoxMusique.setFont(new Font("Times New Roman", Font.BOLD, 20));
		chckBoxMusique.setBounds(378, 300, 132, 40);
		cPConfiguration.add(chckBoxMusique);

		spLogsConfig = new JScrollPane();
		spLogsConfig.setBounds(10, 750, 550, 115);
		cPConfiguration.add(spLogsConfig);

		txtaLogsConfig = new JTextArea();
		txtaLogsConfig.setWrapStyleWord(true);
		txtaLogsConfig.setLineWrap(true);
		spLogsConfig.setViewportView(txtaLogsConfig) ;

		chckbxFleche = new JCheckBox("Flèches clavier ?");
		chckbxFleche.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cPConfiguration.requestFocusInWindow();
				if(chckbxFleche.isSelected()) {
					txtFCoteDroit.setEnabled(false);
					txtFCoteGauche.setEnabled(false);
					fleche=true;
				}else {
					txtFCoteDroit.setEnabled(true);
					txtFCoteGauche.setEnabled(true);
					fleche=false;
				}
			}
		});
		chckbxFleche.setFont(new Font("Tahoma", Font.PLAIN, 20));
		chckbxFleche.setBounds(294, 446, 200, 30);
		cPConfiguration.add(chckbxFleche);

		JLabel lblToucheFleche = new JLabel("Pour utiliser les flèches :");
		lblToucheFleche.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblToucheFleche.setBounds(10, 437, 281, 50);
		cPConfiguration.add(lblToucheFleche);

		//ic = new InfoConfiguration(new Object(), (Font) cBoxPolice.getSelectedItem(), 
				//chckBoxMusique.isSelected(), "castor", 'a', 'd') ;
	}

	/**Méthode permettant d'avoir l'indice de choix de l'utilisateur en terme de touche.
	 * @return Si l'utilisateur choisis d'utiliser les flèches ou des touches personnalisé.
	 */
	//Aimé Melançon
	public static boolean isFleche() {
		return fleche;
	}

	/**Méthode permettant de connaitre la valeur d'entré sélectionné du côté droit.
	 * @return La touche du côté droit
	 */
	//Aimé Melançon
	public static char getCoteDroit() {
		return coteDroit;
	}

	/**Méthode permettant de connaitre la valeur d'entré sélectionné du côté gauche.
	 * @return La touche du côté gauche
	 */
	//Aimé Melançon
	public static char getCoteGauche() {
		return coteGauche;
	}

	/**
	 * Méthode servant à ajouter un PropertyChangeListener sur un graphique.
	 * @param listener : L'écouteur de PropertyChange.
	 */
	// Par Elias Kassas
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	/**
	 * Méthode privée permettant d'afficher un message dans les logs de la configuration.
	 * @param message : Le message à afficher.
	 */
	// Par Elias Kassas
	private void afficherMessageEtLancerPropertyChange(String message) {
		txtaLogsConfig.append("+ " + message + ".\n") ;
		ic = new InfoConfiguration(cBoxModeApp.getSelectedItem(), (Font) cBoxPolice.getSelectedItem(), 
				chckBoxMusique.isSelected(), (String) cBoxMusique.getSelectedItem(), 
				txtFCoteDroit.getText().charAt(0), txtFCoteGauche.getText().charAt(0)) ;
		this.pcs.firePropertyChange("configuration", null, ic) ;
	}

	/**
	 * 
	 * @return
	 */
	// Par Elias Kassas
	public InfoConfiguration obtenirConfiguration() {
		return ic ;
	}

	/**
	 * Méthode permettant de tester l'attribution des touches.
	 * @param e La touche du clavier sélectionné
	 */
	//Aimé Melançon
	private void testKey(KeyEvent e) {

		if(!fleche) {
			char code = e.getKeyChar();
			if(code== coteGauche ) {
				System.out.println("Gauche");
			}
			if(code == coteDroit) {
				System.out.println("Droit");
			}
		}else {
			int code = e.getKeyCode();

			switch(code){
			case KeyEvent.VK_LEFT : 
				System.out.println("Gauche");
				break;
			case KeyEvent.VK_RIGHT : 
				System.out.println("Droit");
				break;
			}
		}
	}
}

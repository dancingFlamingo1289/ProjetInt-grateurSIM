package application.appliSatellite  ;

import java.awt.Color;
import java.awt.EventQueue ;
import java.awt.Font ;
import java.awt.event.ActionEvent ;
import java.awt.event.ActionListener ;

import javax.swing.JButton ;
import javax.swing.JColorChooser;
import javax.swing.JFrame ;
import javax.swing.JLabel ;
import javax.swing.JPanel ;
import javax.swing.JScrollPane ;
import javax.swing.JSpinner ;
import javax.swing.JTabbedPane ;
import javax.swing.JTextArea ;
import javax.swing.JTextField ;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerNumberModel ;
import javax.swing.SwingConstants ;
import javax.swing.border.EmptyBorder ;
import javax.swing.border.TitledBorder ;
import javax.swing.event.ChangeEvent ;
import javax.swing.event.ChangeListener ;

import statistiques.graphique.Graphique ;
import javax.swing.JToggleButton;

/**
 * Cette classe sert d'application satellite à notre application en attendant que l'interface
 * graphique soit complétée.
 * @author Elias Kassas
 */
public class AppSatelliteElias extends JFrame {
	/** Serial version UID de l'application. **/
	private static final long serialVersionUID = 1L  ;
	/** Zone de regroupement principale. **/
	private JPanel contentPane  ;
	/** Zone de saisie de texte pour modifier le titre du graphique. **/
	private JTextField txtTitre ;
	/** Objet contenant le graphique. **/
	private Graphique graphique  ;
	private TestPolygoneA testPolygone ;
	private JLabel lblCouleur ;

	/**
	 * Lancer l'application.
	 * @param args : Paramètre de la méthode main
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AppSatelliteElias frame = new AppSatelliteElias() ;
					frame.setVisible(true) ;
				} catch (Exception e) {
					e.printStackTrace() ;
				}
			}
		}) ;
	}

	/**
	 * Constructeur : Crée l'application.
	 */
	// Par Elias Kassas
	public AppSatelliteElias() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE) ;
		setBounds(100, 100, 850, 800) ;
		contentPane = new JPanel() ;
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5)) ;

		setContentPane(contentPane) ;
		contentPane.setLayout(null) ;
		
		JTabbedPane tpTests = new JTabbedPane(JTabbedPane.TOP) ;
		tpTests.setBounds(0, 0, 850, 775) ;
		contentPane.add(tpTests) ;
		
		JPanel panTestGraphique = new JPanel() ;
		tpTests.addTab("Tests pour le graphique", null, panTestGraphique, null) ;
		panTestGraphique.setLayout(null) ;
		
		graphique = new Graphique() ;
		graphique.setBounds(6, 39, 800, 400) ;
		panTestGraphique.add(graphique) ;
		
		JPanel panPoint = new JPanel() ;
		panPoint.setLayout(null) ;
		panPoint.setBounds(47, 462, 293, 89) ;
		panTestGraphique.add(panPoint) ;
		
		JSpinner spnX = new JSpinner() ;
		spnX.setBounds(36, 0, 100, 48) ;
		panPoint.add(spnX) ;
		
		JSpinner spnY = new JSpinner() ;
		spnY.setBounds(168, 0, 100, 48) ;
		panPoint.add(spnY) ;
		
		JButton btnAjoutPoint = new JButton("Ajouter un point") ;
		btnAjoutPoint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graphique.ajouterUnPointSurLaCourbe(((Number) spnX.getValue()).doubleValue(), 
						((Number) spnY.getValue()).doubleValue()) ;
			}
		});
		btnAjoutPoint.setBounds(11, 60, 150, 29) ;
		panPoint.add(btnAjoutPoint) ;
		
		JLabel lblParOuv = new JLabel("(") ;
		lblParOuv.setFont(new Font("Lucida Grande", Font.PLAIN, 22)) ;
		lblParOuv.setBounds(7, 5, 30, 43) ;
		panPoint.add(lblParOuv) ;
		
		JPanel panel_1 = new JPanel() ;
		panel_1.setLayout(null) ;
		panel_1.setBounds(138, 5, 30, 43) ;
		panPoint.add(panel_1) ;
		
		JLabel lblVirgule = new JLabel(" , ") ;
		lblVirgule.setHorizontalAlignment(SwingConstants.CENTER) ;
		lblVirgule.setFont(new Font("Lucida Grande", Font.PLAIN, 22)) ;
		lblVirgule.setBounds(0, 0, 30, 43) ;
		panel_1.add(lblVirgule) ;
		
		JLabel lblParOuv_2 = new JLabel(")") ;
		lblParOuv_2.setFont(new Font("Lucida Grande", Font.PLAIN, 20)) ;
		lblParOuv_2.setBounds(280, 5, 13, 43) ;
		panPoint.add(lblParOuv_2) ;
		
		JButton btnReinitialiser = new JButton("Réinitialiser") ;
		btnReinitialiser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graphique.reinitialiser() ;
			}
		});
		btnReinitialiser.setBounds(160, 60, 116, 29) ;
		panPoint.add(btnReinitialiser) ;
		
		JLabel lblValX = new JLabel("X = ") ;
		lblValX.setBounds(352, 518, 61, 16) ;
		panTestGraphique.add(lblValX) ;
		
		JSpinner spnValVoulue = new JSpinner() ;
		spnValVoulue.setBounds(387, 502, 100, 48) ;
		panTestGraphique.add(spnValVoulue) ;
		
		txtTitre = new JTextField() ;
		txtTitre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				graphique.setTitre(txtTitre.getText()) ;
			}
		});
		txtTitre.setText("Y en fonction de X") ;
		txtTitre.setHorizontalAlignment(SwingConstants.CENTER) ;
		txtTitre.setColumns(10) ;
		txtTitre.setBounds(156, 6, 500, 26) ;
		panTestGraphique.add(txtTitre) ;
		
		JLabel lblTitre = new JLabel("Titre : ") ;
		lblTitre.setBounds(83, 11, 61, 16) ;
		panTestGraphique.add(lblTitre) ;
		
		JPanel panAjoutPtDepart = new JPanel() ;
		panAjoutPtDepart.setLayout(null) ;
		panAjoutPtDepart.setBorder(new TitledBorder(null, "Ajouter un point de d\u00E9part", TitledBorder.LEADING, TitledBorder.TOP, null, null)) ;
		panAjoutPtDepart.setBounds(510, 463, 286, 88) ;
		panTestGraphique.add(panAjoutPtDepart) ;
		
		JLabel lblParOuv_1 = new JLabel("(") ;
		lblParOuv_1.setFont(new Font("Lucida Grande", Font.PLAIN, 22)) ;
		lblParOuv_1.setBounds(0, 25, 30, 43) ;
		panAjoutPtDepart.add(lblParOuv_1) ;
		
		JLabel lblVirgule_1 = new JLabel(" , ") ;
		lblVirgule_1.setHorizontalAlignment(SwingConstants.CENTER) ;
		lblVirgule_1.setFont(new Font("Lucida Grande", Font.PLAIN, 22)) ;
		lblVirgule_1.setBounds(131, 25, 30, 43) ;
		panAjoutPtDepart.add(lblVirgule_1) ;
		
		JPanel panel = new JPanel() ;
		panel.setLayout(null) ;
		panel.setBounds(29, 20, 100, 48) ;
		panAjoutPtDepart.add(panel) ;
		
		JSpinner spnXDepart = new JSpinner() ;
		spnXDepart.setBounds(0, 0, 100, 48) ;
		panel.add(spnXDepart) ;
		
		JSpinner spnYDepart = new JSpinner() ;
		spnYDepart.setBounds(161, 20, 100, 48) ;
		panAjoutPtDepart.add(spnYDepart) ;
		
		JLabel lblParOuv_2_1 = new JLabel(")") ;
		lblParOuv_2_1.setFont(new Font("Lucida Grande", Font.PLAIN, 20)) ;
		lblParOuv_2_1.setBounds(273, 25, 13, 43) ;
		panAjoutPtDepart.add(lblParOuv_2_1) ;
		
		JButton btnNewButton = new JButton("Tester avec une fonction quelconque.") ;
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dessinerGraphique()  ;
			}
		}) ;
		btnNewButton.setBounds(346, 461, 152, 29) ;
		panTestGraphique.add(btnNewButton) ;
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(47, 570, 350, 100);
		panTestGraphique.add(scrollPane);
		
		JTextArea txtaTestToString = new JTextArea();
		txtaTestToString.setWrapStyleWord(true);
		txtaTestToString.setLineWrap(true);
		scrollPane.setViewportView(txtaTestToString);
		
		JButton btnTestToString = new JButton("Tester toString()") ;
		btnTestToString.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtaTestToString.setText(graphique.toString()) ;
			}
		}) ;
		btnTestToString.setBounds(166, 694, 130, 29) ;
		panTestGraphique.add(btnTestToString) ;
		
		JPanel panTestPolygone = new JPanel() ;
		tpTests.addTab("Tests du polygone", null, panTestPolygone, null) ;
		panTestPolygone.setLayout(null) ;
		
		testPolygone = new TestPolygoneA() ;
		testPolygone.setBounds(164, 5, 500, 500) ;
		panTestPolygone.add(testPolygone) ;
		
		JSpinner spnNbCotes = new JSpinner();
		spnNbCotes.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				testPolygone.setNbCotes((Integer) spnNbCotes.getValue()) ;
			}
		});
		spnNbCotes.setModel(new SpinnerNumberModel(5, 2, Integer.MAX_VALUE, 1)) ;
		spnNbCotes.setBounds(164, 517, 100, 26) ;
		panTestPolygone.add(spnNbCotes) ;
		
		JSpinner spnMesureCote = new JSpinner();
		spnMesureCote.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				testPolygone.setLongueurCote((Double) spnMesureCote.getValue()) ;
			}
		});
		spnMesureCote.setModel(new SpinnerNumberModel(0.000d, 0.000, 100.00, 
				0.500d)) ;
		spnMesureCote.setBounds(472, 517, 100, 26) ;
		panTestPolygone.add(spnMesureCote) ;
		
		JLabel lblNewLabel = new JLabel("côtés");
		lblNewLabel.setBounds(275, 522, 61, 16);
		panTestPolygone.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("pixels");
		lblNewLabel_1.setBounds(575, 522, 61, 16);
		panTestPolygone.add(lblNewLabel_1);
		
		lblCouleur = new JLabel("Couleur : ");
		lblCouleur.setBounds(164, 571, 500, 16);
		panTestPolygone.add(lblCouleur);
		
		JButton btnNewButton_1 = new JButton("Changer la couleur du polygone");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changerCouleurPolygone() ;
			}
		});
		btnNewButton_1.setBounds(289, 599, 250, 29);
		panTestPolygone.add(btnNewButton_1);
	}
	
	/**
	 * Méthode permettant de dessiner une courbe quelconque pour tester le graphique.
	 */
	// Par Elias Kassas
	private void dessinerGraphique() {
		for (double x = 1e-10 ; x <= 15.000 ; x += 5e-2) {
			double y = Math.abs(Math.sin(Math.exp(x))*Math.cos(x)) ;
			graphique.ajouterUnPointSurLaCourbe(x, y) ;
		}
	}
	
	private void changerCouleurPolygone() {
		Color nouvCouleur = JColorChooser.showDialog(null, 
				"Sélectionner une couleur pour le polygone.", Color.yellow) ;
		testPolygone.setCouleur(nouvCouleur) ;
		lblCouleur.setText("Couleur : " + nouvCouleur.toString()) ;
	}
}

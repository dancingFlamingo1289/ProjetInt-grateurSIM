package scene.pagesObstacles;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import scene.pagesObstacles.apercu.ApercuPolygone;
import scene.pagesObstacles.apercu.ApercuVentilateur;
import javax.swing.JInternalFrame;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
/**Classe permettant de modifier et de placer l'obstacle de type Ventilateur.
 * PageVentilateur dérive de JPanel
 * 
 * @author Aimé Melançon
 */
public class PageVentilateur extends JPanel {

	/**Identifiant de classe **/
	private static final long serialVersionUID = 1L;
	/**Bouton pour placer l'obstacle. **/
	private JButton btnPlacerObstacle;
	/**Spinner pour modifier la position x de l'obstacle **/
	private JSpinner spnX;
	private JSpinner spnX_1;
	/**Spinner pour modifier la position y de l'obstacle **/
	private JSpinner spnY;
	private JSpinner spnY_1;
	/**Étiquette contenant l'aperçu de la couleur. **/
	private JLabel lblCouleurDeLObstacle;
	/**Bouton permettant de modifier la couleur de l'obstacle **/
	private AbstractButton btnModifierCouleur;
	/**support pour lancer des evenements de type PropertyChange**/
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	/**JSpinner permettant de changer la hauteur de l'obstacle **/
	private JSpinner spnHauteur;
	/**JSpinner permettant de changer la largeur de l'obstacle. **/
	private JSpinner spnLargeur;
	/**JSpinner permettant de changer l'angle de l'obstacle. **/
	private JSpinner spnAngle;
	/**L'apercu du ventilateur. **/
	private ApercuVentilateur apercuVentilateur;
	/**
	 * Création du panneau
	 */
	//Aimé Melançon
	public PageVentilateur() {
		setBounds(100, 100, 450, 579);
		setLayout(null);
		
		
		JPanel panInformations = new JPanel();
		panInformations.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Param\u00E8tres", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panInformations.setBounds(12, 247, 426, 292);
		add(panInformations);
		panInformations.setLayout(null);
		
		
		
		JLabel lblPosition = new JLabel("Position :");
		lblPosition.setBounds(10, 34, 91, 33);
		lblPosition.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(lblPosition);
		
		
		
		spnX = new JSpinner();
		spnX.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnX.setBounds(153, 44, 74, 20);
		panInformations.add(spnX);
		
		spnY = new JSpinner();
		spnY.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnY.setBounds(264, 44, 59, 20);
		panInformations.add(spnY);
		
		JLabel lblVirgule = new JLabel(",");
		lblVirgule.setBounds(224, 51, 41, 13);
		lblVirgule.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblVirgule.setHorizontalAlignment(SwingConstants.CENTER);
		panInformations.add(lblVirgule);
		
		JLabel lblGauche = new JLabel("[");
		lblGauche.setBounds(128, 36, 15, 26);
		lblGauche.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(lblGauche);
		
		JLabel lblDroit = new JLabel("]");
		lblDroit.setBounds(333, 29, 30, 41);
		lblDroit.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(lblDroit);
		
		JLabel lblTitre = new JLabel("Ventilateur");
		lblTitre.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTitre.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitre.setBounds(12, 10, 426, 34);
		add(lblTitre);
		
		btnPlacerObstacle = new JButton("Ajouter le ventilateur");
		btnPlacerObstacle.setBounds(39, 542, 348, 28);
		add(btnPlacerObstacle);
		
		spnX_1 = new JSpinner();
		spnX_1.setBounds(153, 44, 74, 20);
		panInformations.add(spnX_1);
		
		spnY_1 = new JSpinner();
		spnY_1.setBounds(264, 44, 59, 20);
		panInformations.add(spnY_1);
		
		
		
		JLabel lblMPosition = new JLabel("m");
		lblMPosition.setBounds(352, 47, 45, 13);
		lblMPosition.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(lblMPosition);
		
		
		JLabel lblCouleur = new JLabel("Couleur :");
		lblCouleur.setBounds(10, 219, 96, 30);
		lblCouleur.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(lblCouleur);
		
		
		
		
		
		btnModifierCouleur = new JButton("Modifier la couleur");
		btnModifierCouleur.setBounds(108, 252, 200, 30);
		btnModifierCouleur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
		Color c= JColorChooser.showDialog(null, "Choisir la couleur du ventilateur", null) ;
		apercuVentilateur.setCouleur(c);
		lblCouleurDeLObstacle.setBackground(c);
		
			}
		});
		btnModifierCouleur.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(btnModifierCouleur);
		
		lblCouleurDeLObstacle = new JLabel("Couleur");
		lblCouleurDeLObstacle.setBounds(108, 219, 235, 30);
		lblCouleurDeLObstacle.setHorizontalAlignment(SwingConstants.CENTER);
		lblCouleurDeLObstacle.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(lblCouleurDeLObstacle);
		
		JLabel lblAngle = new JLabel("Angle de rotation :");
		lblAngle.setBounds(10, 77, 186, 26);
		lblAngle.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(lblAngle);
		
		spnAngle = new JSpinner();
		spnAngle.setModel(new SpinnerNumberModel(0.0, -180.0, 180.0, 1.0));
		spnAngle.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnAngle.setBounds(183, 82, 85, 26);
		panInformations.add(spnAngle);
		
		JLabel lblUniteDegres = new JLabel("degrés");
		lblUniteDegres.setBounds(274, 82, 85, 26);
		lblUniteDegres.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(lblUniteDegres);
		
		JLabel lblHaut = new JLabel("Hauteur :");
		lblHaut.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblHaut.setBounds(10, 125, 91, 26);
		panInformations.add(lblHaut);
		
		JLabel lblLargeur = new JLabel("Largeur :");
		lblLargeur.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblLargeur.setBounds(10, 163, 91, 26);
		panInformations.add(lblLargeur);
		
		spnHauteur = new JSpinner();
		spnHauteur.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnHauteur.setModel(new SpinnerNumberModel(Integer.valueOf(10), Integer.valueOf(1), null, Integer.valueOf(1)));
		spnHauteur.setBounds(128, 133, 85, 26);
		panInformations.add(spnHauteur);
		
		spnLargeur = new JSpinner();
		spnLargeur.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnLargeur.setModel(new SpinnerNumberModel(Integer.valueOf(10), Integer.valueOf(1), null, Integer.valueOf(1)));
		spnLargeur.setBounds(128, 171, 85, 26);
		panInformations.add(spnLargeur);
		
		JLabel lblMHaut = new JLabel("m");
		lblMHaut.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMHaut.setBounds(224, 136, 45, 13);
		panInformations.add(lblMHaut);
		
		JLabel lblMLargeur = new JLabel("m");
		lblMLargeur.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMLargeur.setBounds(220, 174, 45, 13);
		panInformations.add(lblMLargeur);
		
		apercuVentilateur = new ApercuVentilateur();
		apercuVentilateur.setBounds(22, 46, 416, 191);
		add(apercuVentilateur);
		btnPlacerObstacle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ajoutDObstacleTable();
			}
		});
		
	}
	/**
	 * Méthode permettant d'envoyer un obstacle à la table
	 */
	//Aimé Melançon
	public void ajoutDObstacleTable() {
		pcs.firePropertyChange("obstacle", null, apercuVentilateur.getObstacle());
	}
	
	/**
	 * Méthode permettant de mettre à jour les paramètres de l'obstacle.
	 */
	//Aimé Melançon
	private void majModificationObstacle() {
		apercuVentilateur.setLargeur(Double.parseDouble( spnLargeur.getValue().toString()));
		apercuVentilateur.setHauteur(Double.parseDouble( spnHauteur.getValue().toString()));
		apercuVentilateur.setOrientation(Math.toRadians(Double.parseDouble( spnAngle.getValue().toString())));
		apercuVentilateur.setPosition(Double.parseDouble(spnX.getValue().toString()),Double.parseDouble( spnY.getValue().toString()) );
	}
	
	/**
	 * voici la methode qui permettra de s'ajouter en tant qu'ecouteur
	 */
	//Aimé Melançon
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}
}

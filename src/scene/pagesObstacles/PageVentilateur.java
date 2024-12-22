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
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import scene.pagesObstacles.apercu.ApercuVentilateur;
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
	/**Spinner pour modifier la position y de l'obstacle **/
	private JSpinner spnY;
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
	/**Un  tourniquet qui permet de changer la force du ventilateur à ajouté**/
	private JSpinner spnForceVentilateur;
	
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
		lblPosition.setBounds(6, 22, 91, 33);
		lblPosition.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(lblPosition);



		spnX = new JSpinner();
		spnX.setModel(new SpinnerNumberModel(50, 0, 100, 1));
		spnX.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnX.setBounds(131, 28, 93, 26);
		panInformations.add(spnX);

		spnY = new JSpinner();
		spnY.setModel(new SpinnerNumberModel(Integer.valueOf(100), Integer.valueOf(0), null, Integer.valueOf(1)));
		spnY.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnY.setBounds(242, 28, 79, 26);
		panInformations.add(spnY);

		JLabel lblVirgule = new JLabel(",");
		lblVirgule.setBounds(217, 21, 41, 30);
		lblVirgule.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblVirgule.setHorizontalAlignment(SwingConstants.CENTER);
		panInformations.add(lblVirgule);

		JLabel lblGauche = new JLabel("[");
		lblGauche.setBounds(121, 23, 15, 26);
		lblGauche.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(lblGauche);

		JLabel lblDroit = new JLabel("]");
		lblDroit.setBounds(326, 16, 30, 41);
		lblDroit.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(lblDroit);

		JLabel lblTitre = new JLabel("Ventilateur".toUpperCase());
		lblTitre.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		lblTitre.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitre.setBounds(12, 10, 426, 34);
		add(lblTitre);

		btnPlacerObstacle = new JButton("Ajouter le ventilateur");
		btnPlacerObstacle.setBounds(39, 542, 348, 28);
		add(btnPlacerObstacle);




		JLabel lblMPosition = new JLabel("cm");
		lblMPosition.setBounds(345, 34, 45, 13);
		lblMPosition.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(lblMPosition);


		btnModifierCouleur = new JButton("Modifier la couleur");
		btnModifierCouleur.setBounds(108, 252, 200, 30);
		btnModifierCouleur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Color c= JColorChooser.showDialog(null, "Choisir la couleur du ventilateur", null) ;
				if(c!=null)
				apercuVentilateur.setCouleur(c);
				

			}
		});
		btnModifierCouleur.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(btnModifierCouleur);

		JLabel lblAngle = new JLabel("Angle de rotation :");
		lblAngle.setBounds(6, 65, 186, 26);
		lblAngle.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(lblAngle);

		spnAngle = new JSpinner();
		spnAngle.setModel(new SpinnerNumberModel(0.0, -180.0, 180.0, 1.0));
		spnAngle.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnAngle.setBounds(188, 65, 101, 26);
		panInformations.add(spnAngle);

		JLabel lblUniteDegres = new JLabel("degrés");
		lblUniteDegres.setBounds(301, 65, 85, 26);
		lblUniteDegres.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(lblUniteDegres);

		JLabel lblHaut = new JLabel("Hauteur :");
		lblHaut.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblHaut.setBounds(6, 107, 170, 26);
		panInformations.add(lblHaut);

		JLabel lblLargeur = new JLabel("Largeur :");
		lblLargeur.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblLargeur.setBounds(6, 169, 161, 26);
		panInformations.add(lblLargeur);

		spnHauteur = new JSpinner();
		spnHauteur.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnHauteur.setModel(new SpinnerNumberModel(10, 5, 30, 1));
		spnHauteur.setBounds(188, 107, 101, 26);
		panInformations.add(spnHauteur);

		spnLargeur = new JSpinner();
		spnLargeur.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnLargeur.setModel(new SpinnerNumberModel(10, 5, 30, 1));
		spnLargeur.setBounds(188, 169, 101, 26);
		panInformations.add(spnLargeur);

		JLabel lblMHaut = new JLabel("cm");
		lblMHaut.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMHaut.setBounds(301, 103, 45, 34);
		panInformations.add(lblMHaut);

		JLabel lblMLargeur = new JLabel("cm");
		lblMLargeur.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMLargeur.setBounds(301, 165, 45, 34);
		panInformations.add(lblMLargeur);

		JLabel lblForce = new JLabel("Force de départ :");
		lblForce.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblForce.setBounds(6, 221, 170, 24);
		panInformations.add(lblForce);

		spnForceVentilateur = new JSpinner();
		spnForceVentilateur.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnForceVentilateur.setModel(new SpinnerNumberModel(5, 5, 100, 1));
		spnForceVentilateur.setBounds(188, 220, 101, 26);
		panInformations.add(spnForceVentilateur);

		JLabel lblN = new JLabel("N");
		lblN.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblN.setBounds(301, 220, 59, 26);
		panInformations.add(lblN);

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
		System.out.println("Envoie !");
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
		apercuVentilateur.setForceVentilateur(Double.parseDouble(spnForceVentilateur.getValue().toString()));
		repaint();
		pcs.firePropertyChange("pointeur","Ouistiti",apercuVentilateur.getObstacle());
	}

	/**
	 * voici la methode qui permettra de s'ajouter en tant qu'ecouteur
	 */
	//Aimé Melançon
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}
}

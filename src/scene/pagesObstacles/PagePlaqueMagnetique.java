package scene.pagesObstacles;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import scene.pagesObstacles.apercu.ApercuPlaqueMagnetique;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
/**Classe permettant de modifier et de placer l'obstacle de type PlaqueMagnetique.
 * PagePlaqueMagnetique dérive de JPanel
 * 
 * @author Aimé Melançon
 */
public class PagePlaqueMagnetique extends JPanel {

	/**Identifiant de classe **/
	private static final long serialVersionUID = 1L;
	/**Bouton pour placer l'obstacle. **/
	private JButton btnPlacerObstacle;
	/**Spinner pour modifier la position x de l'obstacle **/
	private JSpinner spnX;
	/**Spinner pour modifier la position y de l'obstacle **/
	private JSpinner spnY;
	/**Étiquette contenant l'aperçu de la couleur. **/
	private JLabel lblCouleurDeLObstacle;
	/**Bouton permettant de modifier la couleur de l'obstacle **/
	private AbstractButton btnModifierCouleur;
	/**Aperçu de l'obstacle. **/
	private ApercuPlaqueMagnetique apercuPlaqueMagnetique;
	/**support pour lancer des evenements de type PropertyChange**/
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	/**Choix de l'utilisateur pour le champ magnétique. **/
	private JSpinner spnChampTesla;
	/**Choix de la longueur de l'utilisateur. **/
	private JSpinner spnLongueur;




	/**
	 * Création d'un panneau
	 */
	//Aimé Melançon
	public PagePlaqueMagnetique() {
		setBounds(100, 100, 450, 579);
		setLayout(null);

		JPanel panInformations = new JPanel();
		panInformations.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Param\u00E8tres", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panInformations.setBounds(12, 247, 426, 292);
		add(panInformations);
		panInformations.setLayout(null);

		apercuPlaqueMagnetique = new ApercuPlaqueMagnetique();
		apercuPlaqueMagnetique.setBounds(10, 55, 428, 188);
		add(apercuPlaqueMagnetique);

		JLabel lblTitre = new JLabel("Plaque magnétique".toUpperCase());
		lblTitre.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTitre.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitre.setBounds(12, 10, 426, 34);
		add(lblTitre);

		JLabel lblPosition = new JLabel("Position :");
		lblPosition.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPosition.setBounds(10, 34, 91, 33);
		panInformations.add(lblPosition);

		btnPlacerObstacle = new JButton("Ajouter la plaque magnétique");
		btnPlacerObstacle.setBounds(39, 542, 348, 28);
		add(btnPlacerObstacle);

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
		lblVirgule.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblVirgule.setHorizontalAlignment(SwingConstants.CENTER);
		lblVirgule.setBounds(224, 51, 41, 13);
		panInformations.add(lblVirgule);

		JLabel lblGauche = new JLabel("[");
		lblGauche.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblGauche.setBounds(128, 36, 15, 26);
		panInformations.add(lblGauche);

		JLabel lblDroit = new JLabel("]");
		lblDroit.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblDroit.setBounds(333, 29, 30, 41);
		panInformations.add(lblDroit);

		JLabel lblMPosition = new JLabel("m");
		lblMPosition.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMPosition.setBounds(352, 47, 45, 13);
		panInformations.add(lblMPosition);

		JLabel lblCouleur = new JLabel("Couleur :");
		lblCouleur.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblCouleur.setBounds(10, 219, 96, 30);
		panInformations.add(lblCouleur);



		lblCouleurDeLObstacle = new JLabel("Couleur");
		lblCouleurDeLObstacle.setHorizontalAlignment(SwingConstants.CENTER);
		lblCouleurDeLObstacle.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblCouleurDeLObstacle.setBounds(108, 219, 235, 30);
		panInformations.add(lblCouleurDeLObstacle);

		btnModifierCouleur = new JButton("Modifier la couleur");
		btnModifierCouleur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Color c= JColorChooser.showDialog(null, "Choisir la couleur de la plaque magnétique", null) ;

				lblCouleurDeLObstacle.setBackground(c);
				apercuPlaqueMagnetique.setCouleur(c);
			}
		});
		btnModifierCouleur.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnModifierCouleur.setBounds(108, 252, 200, 30);
		panInformations.add(btnModifierCouleur);

		JLabel lblChamp = new JLabel("Champ magnétique :");
		lblChamp.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblChamp.setBounds(22, 91, 200, 33);
		panInformations.add(lblChamp);

		spnChampTesla = new JSpinner();
		spnChampTesla.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnChampTesla.setFont(new Font("Tahoma", Font.PLAIN, 20));
		spnChampTesla.setModel(new SpinnerNumberModel(0.0, -4.0, 4.0, 0.1));
		spnChampTesla.setBounds(244, 95, 99, 33);
		panInformations.add(spnChampTesla);

		JLabel lblT = new JLabel("T");
		lblT.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblT.setBounds(352, 91, 45, 41);
		panInformations.add(lblT);

		JLabel lblLongueur = new JLabel("Longueur :");
		lblLongueur.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblLongueur.setBounds(22, 158, 107, 33);
		panInformations.add(lblLongueur);

		spnLongueur = new JSpinner();
		spnLongueur.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnLongueur.setFont(new Font("Tahoma", Font.PLAIN, 20));
		spnLongueur.setModel(new SpinnerNumberModel(Integer.valueOf(10), Integer.valueOf(1), null, Integer.valueOf(1)));
		spnLongueur.setBounds(151, 158, 99, 33);
		panInformations.add(spnLongueur);

		JLabel lblMLongueur = new JLabel("m");
		lblMLongueur.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMLongueur.setBounds(264, 158, 45, 27);
		panInformations.add(lblMLongueur);
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
		pcs.firePropertyChange("obstacle", null, apercuPlaqueMagnetique.getObstacle());
	}
	/**
	 * Méthode permettant de mettre à jour les paramètres de l'obstacle.
	 */
	//Aimé Melançon
	private void majModificationObstacle() {
		apercuPlaqueMagnetique.setLongueur(Double.parseDouble( spnLongueur.getValue().toString()));
		apercuPlaqueMagnetique.setChampMagn(Double.parseDouble( spnChampTesla.getValue().toString()));
		apercuPlaqueMagnetique.setPosition(Double.parseDouble(spnX.getValue().toString()),Double.parseDouble( spnY.getValue().toString()) );
	}
	/**
	 * voici la methode qui permettra de s'ajouter en tant qu'ecouteur
	 */
	//Aimé Melançon
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}
}

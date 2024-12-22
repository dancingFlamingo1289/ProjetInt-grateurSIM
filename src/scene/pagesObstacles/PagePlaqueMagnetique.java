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
		lblTitre.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		lblTitre.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitre.setBounds(12, 10, 426, 34);
		add(lblTitre);

		JLabel lblPosition = new JLabel("Position :");
		lblPosition.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPosition.setBounds(6, 36, 91, 33);
		panInformations.add(lblPosition);

		btnPlacerObstacle = new JButton("Ajouter la plaque magnétique");
		btnPlacerObstacle.setBounds(39, 542, 348, 28);
		add(btnPlacerObstacle);

		spnX = new JSpinner();
		spnX.setModel(new SpinnerNumberModel(50, 0, 100, 1));
		spnX.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnX.setBounds(138, 39, 93, 26);
		panInformations.add(spnX);

		JLabel lblVirg = new JLabel(",");
		lblVirg.setBounds(225, 36, 30, 33);
		panInformations.add(lblVirg);
		lblVirg.setHorizontalAlignment(SwingConstants.CENTER);

		spnY = new JSpinner();
		spnY.setModel(new SpinnerNumberModel(100, 0, 200, 1));
		spnY.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnY.setBounds(249, 39, 79, 26);
		panInformations.add(spnY);

		JLabel lblGauche = new JLabel("[");
		lblGauche.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblGauche.setBounds(128, 39, 15, 26);
		panInformations.add(lblGauche);

		JLabel lblDroit = new JLabel("]");
		lblDroit.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblDroit.setBounds(333, 32, 30, 41);
		panInformations.add(lblDroit);

		JLabel lblMPosition = new JLabel("cm");
		lblMPosition.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMPosition.setBounds(352, 40, 45, 24);
		panInformations.add(lblMPosition);

		btnModifierCouleur = new JButton("Modifier la couleur");
		btnModifierCouleur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Color c= JColorChooser.showDialog(null, "Choisir la couleur de la plaque magnétique", null) ;

				
				if(c!=null)
				apercuPlaqueMagnetique.setCouleur(c);
			}
		});
		btnModifierCouleur.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnModifierCouleur.setBounds(108, 252, 200, 30);
		panInformations.add(btnModifierCouleur);

		JLabel lblChamp = new JLabel("Champ magnétique :");
		lblChamp.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblChamp.setBounds(6, 93, 200, 33);
		panInformations.add(lblChamp);

		spnChampTesla = new JSpinner();
		spnChampTesla.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnChampTesla.setFont(new Font("Tahoma", Font.PLAIN, 20));
		spnChampTesla.setModel(new SpinnerNumberModel(0.0, -4.0, 4.0, 0.1));
		spnChampTesla.setBounds(199, 93, 99, 33);
		panInformations.add(spnChampTesla);

		JLabel lblT = new JLabel("T");
		lblT.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblT.setBounds(320, 89, 45, 41);
		panInformations.add(lblT);

		JLabel lblLongueur = new JLabel("Longueur :");
		lblLongueur.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblLongueur.setBounds(6, 160, 200, 33);
		panInformations.add(lblLongueur);

		spnLongueur = new JSpinner();
		spnLongueur.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnLongueur.setFont(new Font("Tahoma", Font.PLAIN, 20));
		spnLongueur.setModel(new SpinnerNumberModel(10, 1, 30, 1));
		spnLongueur.setBounds(199, 160, 99, 33);
		panInformations.add(spnLongueur);
		
		JLabel lblMLongueur = new JLabel("cm");
		lblMLongueur.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMLongueur.setBounds(320, 163, 45, 27);
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
		System.out.println("Envoie !");
	}
	/**
	 * Méthode permettant de mettre à jour les paramètres de l'obstacle.
	 */
	//Aimé Melançon
	private void majModificationObstacle() {
		apercuPlaqueMagnetique.setLongueur(Double.parseDouble( spnLongueur.getValue().toString()));
		apercuPlaqueMagnetique.setChampMagn(Double.parseDouble( spnChampTesla.getValue().toString()));
		apercuPlaqueMagnetique.setPosition(Double.parseDouble(spnX.getValue().toString()),Double.parseDouble( spnY.getValue().toString()) );
		repaint();
		pcs.firePropertyChange("pointeur","Ouistiti",apercuPlaqueMagnetique.getObstacle());
	}
	/**
	 * voici la methode qui permettra de s'ajouter en tant qu'ecouteur
	 */
	//Aimé Melançon
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}
}

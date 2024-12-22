package scene.pagesObstacles;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import scene.pagesObstacles.apercu.ApercuMurAmovible;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
/**
 * Page de modification pour un mur.
 * @author Elias Kassas
 * @author Aimé Melançon
 */
public class PageMurAmovible extends JPanel {
	/** Coefficient de sérialisation. **/
	private static final long serialVersionUID = 1L;
	/**support pour lancer des evenements de type PropertyChange**/
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	/**JSpinner permettant de changer l'hauteur de l'obstacle **/
	private JSpinner spnHauteur;
	/**JSpinner permettant de changer la largeur de l'obstacle **/
	private JSpinner spnLargeur;
	/**JSpinner permettant de changer l'angle de l'obstacle **/
	private JSpinner spnAngle;
	/**JSpinner permettant de changer la vitesse de l'obstacle **/
	private JSpinner spnVitesse;
	/**L'aperçu du mur amovible **/
	private ApercuMurAmovible apercuMurAmovible;
	/**JSpinner permettant de changer la position x de l'obstacle **/
	private JSpinner spnX;
	/**JSpinner permettant de changer la position y de l'obstacle **/
	private JSpinner spnY;
	/**Bouton permettant de changer la couleur de l'obstacle **/
	private JButton btnChangerCouleur;
	/**Bouton pour ajouter l'obstacle. **/
	private JButton btnAjouter;

	/**
	 * Constructeur de la page de modification d'un mur amovible.
	 */
	// Par Elias Kassas
	public PageMurAmovible() {
		setBounds(100, 100, 450, 587);
		setBorder(new EmptyBorder(5, 5, 5, 5));

		setLayout(null);

		JLabel lblTitre = new JLabel("Mur amovible".toUpperCase());
		lblTitre.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		lblTitre.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitre.setBounds(12, 6, 426, 25);
		add(lblTitre);

		JPanel panInformations = new JPanel();
		panInformations.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Param\u00E8tres", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panInformations.setBounds(6, 247, 438, 292);
		add(panInformations);
		panInformations.setLayout(null);

		JLabel lblPosition = new JLabel("Position :");
		lblPosition.setBounds(6, 23, 61, 16);
		panInformations.add(lblPosition);

		JLabel lblParOuvr = new JLabel("[");
		lblParOuvr.setBounds(140, 18, 30, 16);
		panInformations.add(lblParOuvr);

		spnX = new JSpinner();
		spnX.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnX.setBounds(156, 13, 93, 26);
		panInformations.add(spnX);

		JLabel lblVirg = new JLabel(",");
		lblVirg.setBounds(239, 18, 30, 16);
		panInformations.add(lblVirg);
		lblVirg.setHorizontalAlignment(SwingConstants.CENTER);

		spnY = new JSpinner();
		spnY.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnY.setBounds(255, 13, 79, 26);
		panInformations.add(spnY);

		JLabel lblParFerm = new JLabel("]");
		lblParFerm.setBounds(316, 18, 30, 16);
		panInformations.add(lblParFerm);
		lblParFerm.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblHaut = new JLabel("Hauteur :");
		lblHaut.setBounds(6, 56, 61, 16);
		panInformations.add(lblHaut);

		spnHauteur = new JSpinner();
		spnHauteur.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnHauteur.setModel(new SpinnerNumberModel(Integer.valueOf(10), Integer.valueOf(0), null, Integer.valueOf(1)));
		spnHauteur.setBounds(140, 51, 85, 26);
		panInformations.add(spnHauteur);

		JLabel lblUniteM = new JLabel("m");
		lblUniteM.setBounds(245, 56, 61, 16);
		panInformations.add(lblUniteM);

		JLabel lblLargeur = new JLabel("Largeur :");
		lblLargeur.setBounds(5, 91, 61, 16);
		panInformations.add(lblLargeur);

		spnLargeur = new JSpinner();
		spnLargeur.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnLargeur.setModel(new SpinnerNumberModel(Integer.valueOf(5), Integer.valueOf(0), null, Integer.valueOf(1)));
		spnLargeur.setBounds(139, 87, 85, 26);
		panInformations.add(spnLargeur);

		JLabel lblUniteM_1 = new JLabel("m");
		lblUniteM_1.setBounds(244, 92, 61, 16);
		panInformations.add(lblUniteM_1);

		JLabel lblAngle = new JLabel("Angle de rotation :");
		lblAngle.setBounds(6, 124, 117, 16);
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
		spnAngle.setBounds(140, 119, 85, 26);
		panInformations.add(spnAngle);

		JLabel lblUniteDegres = new JLabel("degrés");
		lblUniteDegres.setBounds(245, 124, 61, 16);
		panInformations.add(lblUniteDegres);

		JLabel lblCouleur = new JLabel("Couleur : ");
		lblCouleur.setBounds(6, 208, 61, 16);
		panInformations.add(lblCouleur);

		JLabel lblInfoCouleur = new JLabel(Color.white.toString()) ;
		lblInfoCouleur.setBounds(79, 208, 341, 16);
		lblInfoCouleur.setBackground(Color.green);
		lblInfoCouleur.setHorizontalAlignment(SwingConstants.CENTER);
		panInformations.add(lblInfoCouleur);

		btnChangerCouleur = new JButton("Modifier la couleur");
		btnChangerCouleur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color c = JColorChooser.showDialog(null, "Choisir la couleur du mur", null) ;
				apercuMurAmovible.setCouleur(c);

			}
		});
		btnChangerCouleur.setBounds(116, 236, 194, 29);
		panInformations.add(btnChangerCouleur);

		JLabel lblUniteM_2 = new JLabel("m");
		lblUniteM_2.setBounds(358, 18, 61, 16);
		panInformations.add(lblUniteM_2);

		JLabel lblVitesse = new JLabel("Vitesse de croisière : ");
		lblVitesse.setBounds(6, 163, 134, 16);
		panInformations.add(lblVitesse);

		spnVitesse = new JSpinner();
		spnVitesse.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnVitesse.setModel(new SpinnerNumberModel(Double.valueOf(1), Double.valueOf(0), null, Double.valueOf(0.1)));
		spnVitesse.setBounds(140, 157, 85, 26);
		panInformations.add(spnVitesse);

		JLabel lblMs = new JLabel("m/s");
		lblMs.setBounds(239, 163, 61, 16);
		panInformations.add(lblMs);

		btnAjouter = new JButton("Ajouter le mur amovible à la table");
		btnAjouter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ajoutDObstacleTable();
			}
		});
		btnAjouter.setBounds(97, 544, 256, 29);
		add(btnAjouter);

		apercuMurAmovible = new ApercuMurAmovible();
		apercuMurAmovible.setBounds(12, 41, 428, 206);
		add(apercuMurAmovible);
	}
	/**
	 * Méthode permettant de mettre à jour les paramètres de l'obstacle.
	 */
	//Aimé Melançon
	private void majModificationObstacle() {
		apercuMurAmovible.setLargeur(Double.parseDouble( spnLargeur.getValue().toString()));
		apercuMurAmovible.setHauteur(Double.parseDouble( spnHauteur.getValue().toString()));
		apercuMurAmovible.setVitesseCroisiere(Double.parseDouble( spnVitesse.getValue().toString()));
		apercuMurAmovible.setAngle(Math.toRadians(Double.parseDouble( spnAngle.getValue().toString())));
		apercuMurAmovible.setPosition(Double.parseDouble(spnX.getValue().toString()),Double.parseDouble( spnY.getValue().toString()) );
	}
	/**
	 * voici la methode qui permettra de s'ajouter en tant qu'ecouteur
	 */
	//Aimé Melançon
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}
	/**
	 * Méthode permettant d'envoyer un obstacle à la table
	 */
	//Aimé Melançon
	public void ajoutDObstacleTable() {
		pcs.firePropertyChange("obstacle", null, apercuMurAmovible.getObstacle());
	}
}

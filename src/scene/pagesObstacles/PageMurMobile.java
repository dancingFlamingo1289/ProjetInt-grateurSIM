package scene.pagesObstacles;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import scene.pagesObstacles.apercu.ApercuMurMobile;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
/**
 * Page de modification pour un mur.
 * @author Elias Kassas
 * @author Aimé Melançon
 */
public class PageMurMobile extends JPanel {
	/** Coefficient de sérialisation. **/
	private static final long serialVersionUID = 1L;
	/**support pour lancer des evenements de type PropertyChange**/
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	/**JSpinner permettant de changer l'hauteur de l'obstacle **/
	private JSpinner spnHauteur;
	/**JSpinner permettant de changer la largeur de l'obstacle **/
	private JSpinner spnLargeur;
	/**JSpinner permettant de changer la vitesse de l'obstacle **/
	private JSpinner spnVitesse;
	/**L'aperçu du mur amovible **/
	private ApercuMurMobile apercuMurMobile;
	/**JSpinner permettant de changer la position x de l'obstacle **/
	private JSpinner spnX;
	/**JSpinner permettant de changer la position y de l'obstacle **/
	private JSpinner spnY;
	/**Bouton permettant de changer la couleur de l'obstacle **/
	private JButton btnChangerCouleur;
	/**Bouton pour ajouter l'obstacle. **/
	private JButton btnAjouter;
	/** Tourniquet pour modifier l'angle de rotation. **/
	private JSpinner spnAngle;

	/**
	 * Constructeur de la page de modification d'un mur amovible.
	 */
	// Par Elias Kassas
	public PageMurMobile() {
		setBounds(100, 100, 450, 587);
		setBorder(new EmptyBorder(5, 5, 5, 5));

		setLayout(null);

		JLabel lblTitre = new JLabel("Mur mobile".toUpperCase());
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
		lblPosition.setBounds(6, 26, 61, 16);
		panInformations.add(lblPosition);

		JLabel lblParOuvr = new JLabel("[");
		lblParOuvr.setBounds(140, 26, 30, 16);
		panInformations.add(lblParOuvr);

		spnX = new JSpinner();
		spnX.setModel(new SpinnerNumberModel(50, 0, 100, 1));
		spnX.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnX.setBounds(156, 21, 93, 26);
		panInformations.add(spnX);

		JLabel lblVirg = new JLabel(",");
		lblVirg.setBounds(239, 26, 30, 16);
		panInformations.add(lblVirg);
		lblVirg.setHorizontalAlignment(SwingConstants.CENTER);

		spnY = new JSpinner();
		spnY.setModel(new SpinnerNumberModel(100, 0, 200, 1));
		spnY.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnY.setBounds(255, 21, 79, 26);
		panInformations.add(spnY);

		JLabel lblParFerm = new JLabel("]");
		lblParFerm.setBounds(316, 26, 30, 16);
		panInformations.add(lblParFerm);
		lblParFerm.setHorizontalAlignment(SwingConstants.TRAILING);

		JLabel lblHaut = new JLabel("Hauteur :");
		lblHaut.setBounds(6, 68, 61, 16);
		panInformations.add(lblHaut);

		spnHauteur = new JSpinner();
		spnHauteur.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnHauteur.setModel(new SpinnerNumberModel(10, 0, 30, 1));
		spnHauteur.setBounds(140, 63, 85, 26);
		panInformations.add(spnHauteur);

		JLabel lblUniteM = new JLabel("cm");
		lblUniteM.setBounds(245, 68, 61, 16);
		panInformations.add(lblUniteM);

		JLabel lblLargeur = new JLabel("Largeur :");
		lblLargeur.setBounds(5, 110, 61, 16);
		panInformations.add(lblLargeur);

		spnLargeur = new JSpinner();
		spnLargeur.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnLargeur.setModel(new SpinnerNumberModel(5, 0, 30, 1));
		spnLargeur.setBounds(139, 105, 85, 26);
		panInformations.add(spnLargeur);

		JLabel lblUniteM_1 = new JLabel("cm");
		lblUniteM_1.setBounds(244, 110, 61, 16);
		panInformations.add(lblUniteM_1);

		JLabel lblAngle = new JLabel("Angle de rotation :");
		lblAngle.setBounds(6, 152, 117, 16);
		panInformations.add(lblAngle);

		JLabel lblUniteDegres = new JLabel("degrés");
		lblUniteDegres.setBounds(245, 152, 61, 16);
		panInformations.add(lblUniteDegres);

		btnChangerCouleur = new JButton("Modifier la couleur");
		btnChangerCouleur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color c = JColorChooser.showDialog(null, "Choisir la couleur du mur", null) ;
				
				if(c!=null)
				apercuMurMobile.setCouleur(c);

			}
		});
		btnChangerCouleur.setBounds(116, 236, 194, 29);
		panInformations.add(btnChangerCouleur);

		JLabel lblUniteM_2 = new JLabel("cm");
		lblUniteM_2.setBounds(358, 26, 61, 16);
		panInformations.add(lblUniteM_2);

		JLabel lblVitesse = new JLabel("Vitesse de croisière : ");
		lblVitesse.setBounds(6, 194, 134, 16);
		panInformations.add(lblVitesse);

		spnVitesse = new JSpinner();
		spnVitesse.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnVitesse.setModel(new SpinnerNumberModel(1.0, 1.0, 100.0, 1.0));
		spnVitesse.setBounds(140, 189, 85, 26);
		panInformations.add(spnVitesse);

		JLabel lblMs = new JLabel("m/s");
		lblMs.setBounds(245, 194, 61, 16);
		panInformations.add(lblMs);
		
		spnAngle = new JSpinner();
		spnAngle.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnAngle.setModel(new SpinnerNumberModel(0, -90, 90, 1));
		spnAngle.setBounds(140, 147, 85, 26);
		panInformations.add(spnAngle);

		btnAjouter = new JButton("Ajouter le mur mobile à la table");
		btnAjouter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ajoutDObstacleTable();
			}
		});
		btnAjouter.setBounds(97, 544, 256, 29);
		add(btnAjouter);

		apercuMurMobile = new ApercuMurMobile();
		apercuMurMobile.setBounds(12, 41, 428, 206);
		add(apercuMurMobile);
	}
	
	/**
	 * Méthode permettant de mettre à jour les paramètres de l'obstacle.
	 */
	//Aimé Melançon
	private void majModificationObstacle() {
		apercuMurMobile.setLargeur(Double.parseDouble( spnLargeur.getValue().toString()));
		apercuMurMobile.setHauteur(Double.parseDouble( spnHauteur.getValue().toString()));
		apercuMurMobile.setVitesseCroisiere(Double.parseDouble( spnVitesse.getValue().toString()));
		apercuMurMobile.setAngle(Math.toRadians(Double.parseDouble( spnAngle.getValue().toString())));
		apercuMurMobile.setPosition(Double.parseDouble(spnX.getValue().toString()),Double.parseDouble( spnY.getValue().toString()) );
		repaint();
		pcs.firePropertyChange("pointeur","Ouistiti",apercuMurMobile.getObstacle());
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
		pcs.firePropertyChange("obstacle", null, apercuMurMobile.getObstacle());
		System.out.println("Envoie !");
	}
}

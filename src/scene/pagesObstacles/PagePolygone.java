package scene.pagesObstacles;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import scene.pagesObstacles.apercu.ApercuPolygone;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.SpinnerNumberModel;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
/**Classe permettant de modifier et de placer l'obstacle de type Polygone.
 * PagePolygone dérive de JPanel
 * 
 * @author Aimé Melançon
 */
public class PagePolygone extends JPanel {
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
	/**Aperçu de l'obstacle. **/
	private ApercuPolygone apercuPolygone;
	/**support pour lancer des evenements de type PropertyChange**/
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	/**JSpinner permettant de choisir le nombre de côté **/
	private JSpinner spnNbCote;
	/**JCheckBox permettant d'activé le mode ressort **/
	private JCheckBox chckBoxFaireRebondir;
	/**JSpinner permettant de choisir la constante de rappel **/
	private JSpinner spnK;
	/**JSPinner permettant de choisir le nombre de côté **/
	private JSpinner spnMesureCote;

	/**
	 * Création d'un panneau
	 */
	//Aimé Melançon
	public PagePolygone() {
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
		
		apercuPolygone = new ApercuPolygone();
		apercuPolygone.setBounds(12, 42, 417, 195);
		add(apercuPolygone);
		
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
		
		JLabel lblTitre = new JLabel("Polygone");
		lblTitre.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblTitre.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitre.setBounds(12, 10, 426, 34);
		add(lblTitre);
		
		btnPlacerObstacle = new JButton("Ajouter le polygone");
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
		
		
		
		lblCouleurDeLObstacle = new JLabel("Couleur");
		lblCouleurDeLObstacle.setBounds(108, 219, 235, 30);
		lblCouleurDeLObstacle.setHorizontalAlignment(SwingConstants.CENTER);
		lblCouleurDeLObstacle.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(lblCouleurDeLObstacle);
		
		btnModifierCouleur = new JButton("Modifier la couleur");
		btnModifierCouleur.setBounds(108, 252, 200, 30);
		btnModifierCouleur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
		Color c= JColorChooser.showDialog(null, "Choisir la couleur du polygone", null) ;
				
		lblCouleurDeLObstacle.setBackground(c);
		apercuPolygone.setCouleur(c);
			}
		});
		btnModifierCouleur.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(btnModifierCouleur);
		
		JLabel lblCote = new JLabel("Nombre de côtés :");
		lblCote.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblCote.setBounds(10, 72, 165, 33);
		panInformations.add(lblCote);
		
		spnNbCote = new JSpinner();
		spnNbCote.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnNbCote.setFont(new Font("Tahoma", Font.PLAIN, 20));
		spnNbCote.setModel(new SpinnerNumberModel(3, 3, 20, 1));
		spnNbCote.setBounds(234, 72, 108, 31);
		panInformations.add(spnNbCote);
		
		chckBoxFaireRebondir = new JCheckBox("Fait rebondir");
		chckBoxFaireRebondir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckBoxFaireRebondir.isEnabled()) {
					spnK.setEnabled(true);
				}else {
					spnK.setEnabled(false);
				}
			}
		});
		chckBoxFaireRebondir.setFont(new Font("Tahoma", Font.PLAIN, 20));
		chckBoxFaireRebondir.setBounds(160, 150, 148, 20);
		panInformations.add(chckBoxFaireRebondir);
		
		JLabel lblConstanteRappel = new JLabel("Constante de rappel :");
		lblConstanteRappel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblConstanteRappel.setBounds(20, 176, 200, 33);
		panInformations.add(lblConstanteRappel);
		
		spnK = new JSpinner();
		spnK.setModel(new SpinnerNumberModel(12, 10, 100, 1));
		spnK.setFont(new Font("Tahoma", Font.PLAIN, 20));
		spnK.setBounds(235, 174, 90, 36);
		panInformations.add(spnK);
		
		JLabel lblNM = new JLabel("N/m");
		lblNM.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNM.setBounds(333, 176, 41, 33);
		panInformations.add(lblNM);
		
		spnMesureCote = new JSpinner();
		spnMesureCote.setModel(new SpinnerNumberModel(Integer.valueOf(5), Integer.valueOf(1), null, Integer.valueOf(1)));
		spnMesureCote.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				 majModificationObstacle();
			}
		});
		spnMesureCote.setFont(new Font("Tahoma", Font.PLAIN, 20));
		spnMesureCote.setBounds(196, 113, 108, 31);
		panInformations.add(spnMesureCote);
		
		JLabel lblMesureCote = new JLabel("Mesure côté :");
		lblMesureCote.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMesureCote.setBounds(71, 111, 129, 33);
		panInformations.add(lblMesureCote);
		
		JLabel lblMMesureCote = new JLabel("m");
		lblMMesureCote.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMMesureCote.setBounds(318, 122, 45, 22);
		panInformations.add(lblMMesureCote);
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
		pcs.firePropertyChange("obstacle", null, apercuPolygone.getObstacle());
	}
	/**
	 * voici la methode qui permettra de s'ajouter en tant qu'ecouteur
	 */
	//Aimé Melançon
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}
	/**
	 * Méthode permettant de mettre à jour les paramètres de l'obstacle.
	 */
	//Aimé Melançon
	private void majModificationObstacle() {
		apercuPolygone.setNbCotes(Integer.parseInt( spnNbCote.getValue().toString()));
		apercuPolygone.setMesureCote(Double.parseDouble( spnMesureCote.getValue().toString()));
		apercuPolygone.setPosition(Double.parseDouble(spnX.getValue().toString()),Double.parseDouble( spnY.getValue().toString()) );
	}
}

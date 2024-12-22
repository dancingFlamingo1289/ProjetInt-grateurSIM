package scene.pagesObstacles;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import scene.pagesObstacles.apercu.ApercuTrouNoir;
import javax.swing.border.BevelBorder;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.SwingConstants;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
/**Classe permettant de modifier et de placer l'obstacle de type TroiNoir.
 * PageTrouNoir dérive de JPanel
 * 
 * @author Aimé Melançon
 */
public class PageTrouNoir extends JPanel {

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
	private ApercuTrouNoir apercuTrouNoir;
	/** JSpinner permettant de choisir le diametre du trou noir**/
	private JSpinner spnDiam;
	/**JSpinner permettant de choisir la masse. **/
	private JSpinner spnMasse;
	/**support pour lancer des evenements de type PropertyChange**/
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	/**
	 * Création de panneau
	 */
	//Aimé Melançon
	public PageTrouNoir() {
		setBounds(100, 100, 450, 579);
		setLayout(null);
		
		apercuTrouNoir = new ApercuTrouNoir();
		apercuTrouNoir.setBounds(10, 36, 430, 151);
		add(apercuTrouNoir);
		
		JPanel panelInfo = new JPanel();
		panelInfo.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Param\u00E8tres", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelInfo.setBounds(10, 197, 430, 337);
		add(panelInfo);
		panelInfo.setLayout(null);
		
		JLabel lblPosition = new JLabel("Position :");
		lblPosition.setBounds(33, 29, 89, 41);
		lblPosition.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblPosition.setEnabled(false);
		panelInfo.add(lblPosition);
		
		spnX = new JSpinner();
		spnX.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle(); 
			}
		});
		spnX.setBounds(153, 44, 74, 20);
		panelInfo.add(spnX);
		
		spnY = new JSpinner();
		spnY.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle(); 
			}
		});
		spnY.setBounds(264, 44, 59, 20);
		panelInfo.add(spnY);
		
		JLabel lblDiamètre = new JLabel("Diamètre  :");
		lblDiamètre.setBounds(10, 103, 121, 30);
		lblDiamètre.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panelInfo.add(lblDiamètre);
		
		JLabel lblVirgule = new JLabel(",");
		lblVirgule.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblVirgule.setHorizontalAlignment(SwingConstants.CENTER);
		lblVirgule.setBounds(224, 51, 41, 13);
		panelInfo.add(lblVirgule);
		
		JLabel lblGauche = new JLabel("[");
		lblGauche.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblGauche.setBounds(128, 36, 15, 26);
		panelInfo.add(lblGauche);
		
		JLabel lblDroit = new JLabel("]");
		lblDroit.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblDroit.setBounds(333, 29, 30, 41);
		panelInfo.add(lblDroit);
		
		spnDiam = new JSpinner();
		spnDiam.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle(); 
			}
		});
		spnDiam.setModel(new SpinnerNumberModel(Integer.valueOf(10), Integer.valueOf(2), null, Integer.valueOf(1)));
		spnDiam.setBounds(115, 108, 112, 30);
		panelInfo.add(spnDiam);
		
		JLabel lblM = new JLabel("m");
		lblM.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblM.setBounds(250, 116, 45, 13);
		panelInfo.add(lblM);
		
		JLabel lblMasse = new JLabel("Masse :");
		lblMasse.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMasse.setBounds(33, 163, 83, 30);
		panelInfo.add(lblMasse);
		
		spnMasse = new JSpinner();
		spnMasse.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle(); 
			}
		});
		spnMasse.setModel(new SpinnerNumberModel(Integer.valueOf(100), Integer.valueOf(100), null, Integer.valueOf(100)));
		spnMasse.setBounds(135, 163, 112, 30);
		panelInfo.add(spnMasse);
		
		JLabel lblKg = new JLabel("Kg");
		lblKg.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblKg.setBounds(264, 159, 41, 30);
		panelInfo.add(lblKg);
		
		JLabel lblCouleur = new JLabel("Couleur :");
		lblCouleur.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblCouleur.setBounds(26, 226, 96, 30);
		panelInfo.add(lblCouleur);
		
		lblCouleurDeLObstacle = new JLabel("Couleur");
		lblCouleurDeLObstacle.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblCouleurDeLObstacle.setBounds(128, 226, 235, 30);
		panelInfo.add(lblCouleurDeLObstacle);
		
		btnModifierCouleur = new JButton("Modifier la couleur");
		btnModifierCouleur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
		Color c= JColorChooser.showDialog(null, "Choisir la couleur du trou noir", null) ;
				
		lblCouleurDeLObstacle.setBackground(c);
		apercuTrouNoir.setCouleur(c);
			}
		});
		btnModifierCouleur.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnModifierCouleur.setBounds(115, 281, 200, 30);
		panelInfo.add(btnModifierCouleur);
		
		JLabel lblMPosition = new JLabel("m");
		lblMPosition.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMPosition.setBounds(352, 47, 45, 13);
		panelInfo.add(lblMPosition);
		
		btnPlacerObstacle= new JButton("Ajouter le trou noir");
		btnPlacerObstacle.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnPlacerObstacle.setBounds(117, 544, 210, 25);
		add(btnPlacerObstacle);
		
		JLabel lblTrouNoir = new JLabel("Trou noir");
		lblTrouNoir.setBounds(97, 10, 224, 19);
		add(lblTrouNoir);
		lblTrouNoir.setHorizontalAlignment(SwingConstants.CENTER);
		lblTrouNoir.setFont(new Font("Tahoma", Font.PLAIN, 20));
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
		pcs.firePropertyChange("obstacle", null, apercuTrouNoir.getObstacle());
	}
	
	/**
	 * Méthode permettant de mettre à jour les paramètres de l'obstacle.
	 */
	//Aimé Melançon
	private void majModificationObstacle() {
		apercuTrouNoir.setDiametre(Double.parseDouble( spnDiam.getValue().toString()));
	
		apercuTrouNoir.setMasse(Double.parseDouble( spnMasse.getValue().toString()));
	
		apercuTrouNoir.setPosition(Double.parseDouble(spnX.getValue().toString()),Double.parseDouble( spnY.getValue().toString()) );
	}
	/**
	 * voici la methode qui permettra de s'ajouter en tant qu'ecouteur
	 */
	//Aimé Melançon
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}
}

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
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import scene.pagesObstacles.apercu.ApercuTrouNoir;
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
		lblPosition.setBounds(22, 29, 89, 41);
		lblPosition.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panelInfo.add(lblPosition);
		
		spnX = new JSpinner();
		spnX.setModel(new SpinnerNumberModel(50, 0, 100, 1));
		spnX.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnX.setBounds(138, 36, 93, 26);
		panelInfo.add(spnX);

		spnY = new JSpinner();
		spnY.setModel(new SpinnerNumberModel(Integer.valueOf(100), Integer.valueOf(0), null, Integer.valueOf(1)));
		spnY.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnY.setBounds(249, 36, 79, 26);
		panelInfo.add(spnY);
		
		JLabel lblDiamètre = new JLabel("Diamètre  :");
		lblDiamètre.setBounds(22, 123, 121, 41);
		lblDiamètre.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panelInfo.add(lblDiamètre);
		
		JLabel lblVirgule = new JLabel(",");
		lblVirgule.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblVirgule.setHorizontalAlignment(SwingConstants.CENTER);
		lblVirgule.setBounds(224, 36, 41, 27);
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
		spnDiam.setModel(new SpinnerNumberModel(10, 10, 30, 1));
		spnDiam.setBounds(148, 128, 112, 30);
		panelInfo.add(spnDiam);
		
		JLabel lblM = new JLabel("cm");
		lblM.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblM.setBounds(283, 123, 45, 41);
		panelInfo.add(lblM);
		
		JLabel lblMasse = new JLabel("Masse :");
		lblMasse.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMasse.setBounds(22, 217, 83, 41);
		panelInfo.add(lblMasse);
		
		spnMasse = new JSpinner();
		spnMasse.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle(); 
			}
		});
		spnMasse.setModel(new SpinnerNumberModel(Integer.valueOf(100), Integer.valueOf(100), null, Integer.valueOf(100)));
		spnMasse.setBounds(117, 222, 112, 30);
		panelInfo.add(spnMasse);
		
		JLabel lblKg = new JLabel("Kg");
		lblKg.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblKg.setBounds(247, 222, 41, 30);
		panelInfo.add(lblKg);
		
		btnModifierCouleur = new JButton("Modifier la couleur");
		btnModifierCouleur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
		Color c= JColorChooser.showDialog(null, "Choisir la couleur du trou noir", null) ;
				
		
		if(c!=null)
		apercuTrouNoir.setCouleur(c);
			}
		});
		btnModifierCouleur.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnModifierCouleur.setBounds(115, 281, 200, 30);
		panelInfo.add(btnModifierCouleur);
		
		JLabel lblMPosition = new JLabel("cm");
		lblMPosition.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMPosition.setBounds(352, 29, 45, 41);
		panelInfo.add(lblMPosition);
		
		btnPlacerObstacle= new JButton("Ajouter le trou noir");
		btnPlacerObstacle.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		btnPlacerObstacle.setBounds(77, 544, 285, 25);
		btnPlacerObstacle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ajoutDObstacleTable();
			}
		});
		add(btnPlacerObstacle);
		
		JLabel lblTrouNoir = new JLabel("Trou noir".toUpperCase());
		lblTrouNoir.setBounds(97, 10, 224, 19);
		add(lblTrouNoir);
		lblTrouNoir.setHorizontalAlignment(SwingConstants.CENTER);
		lblTrouNoir.setFont(new Font("Lucida Grande", Font.BOLD, 20));
	}
	
	/**
	 * Méthode permettant d'envoyer un obstacle à la table
	 */
	//Aimé Melançon
	public void ajoutDObstacleTable() {
		pcs.firePropertyChange("obstacle", null, apercuTrouNoir.getObstacle());
		System.out.println("Envoie !");
	}
	
	/**
	 * Méthode permettant de mettre à jour les paramètres de l'obstacle.
	 */
	//Aimé Melançon
	private void majModificationObstacle() {
		apercuTrouNoir.setDiametre(Double.parseDouble( spnDiam.getValue().toString()));
	
		apercuTrouNoir.setMasse(Double.parseDouble( spnMasse.getValue().toString()));
	
		apercuTrouNoir.setPosition(Double.parseDouble(spnX.getValue().toString()),Double.parseDouble( spnY.getValue().toString()) );
		repaint();
		pcs.firePropertyChange("pointeur","Ouistiti",apercuTrouNoir.getObstacle());
	}
	/**
	 * voici la methode qui permettra de s'ajouter en tant qu'ecouteur
	 */
	//Aimé Melançon
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}
}

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

import scene.pagesObstacles.apercu.ApercuPolygone;
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
	/**Spinner pour modifier la position y de l'obstacle **/
	private JSpinner spnY;
	/**Bouton permettant de modifier la couleur de l'obstacle **/
	private AbstractButton btnModifierCouleur;
	/**Aperçu de l'obstacle. **/
	private ApercuPolygone apercuPolygone;
	/**support pour lancer des evenements de type PropertyChange**/
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	/**JSpinner permettant de choisir le nombre de côté **/
	private JSpinner spnNbCote;
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
		lblPosition.setBounds(6, 34, 93, 33);
		lblPosition.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(lblPosition);
		
		apercuPolygone = new ApercuPolygone();
		apercuPolygone.setBackground(Color.WHITE);
		apercuPolygone.setBounds(12, 42, 426, 195);
		add(apercuPolygone);
		
		spnX = new JSpinner();
		spnX.setModel(new SpinnerNumberModel(50, 0, 100, 1));
		spnX.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnX.setBounds(155, 37, 93, 26);
		panInformations.add(spnX);

		spnY = new JSpinner();
		spnY.setModel(new SpinnerNumberModel(Integer.valueOf(100), Integer.valueOf(0), null, Integer.valueOf(1)));
		spnY.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnY.setBounds(264, 37, 79, 26);
		panInformations.add(spnY);
		
		JLabel lblVirgule = new JLabel(",");
		lblVirgule.setBounds(236, 34, 41, 33);
		lblVirgule.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblVirgule.setHorizontalAlignment(SwingConstants.CENTER);
		panInformations.add(lblVirgule);
		
		JLabel lblGauche = new JLabel("[");
		lblGauche.setBounds(143, 37, 15, 26);
		lblGauche.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(lblGauche);
		
		JLabel lblDroit = new JLabel("]");
		lblDroit.setBounds(355, 30, 30, 41);
		lblDroit.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(lblDroit);
		
		JLabel lblTitre = new JLabel("Polygone".toUpperCase());
		lblTitre.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		lblTitre.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitre.setBounds(12, 10, 426, 34);
		add(lblTitre);
		
		btnPlacerObstacle = new JButton("Ajouter le polygone");
		btnPlacerObstacle.setBounds(39, 542, 348, 28);
		add(btnPlacerObstacle);
		
		
		
		JLabel lblMPosition = new JLabel("cm");
		lblMPosition.setBounds(375, 34, 45, 33);
		lblMPosition.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(lblMPosition);
		
		btnModifierCouleur = new JButton("Modifier la couleur");
		btnModifierCouleur.setBounds(108, 252, 200, 30);
		btnModifierCouleur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
		Color c= JColorChooser.showDialog(null, "Choisir la couleur du polygone", null) ;
				
		
		if(c!=null)
		apercuPolygone.setCouleur(c);
			}
		});
		btnModifierCouleur.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panInformations.add(btnModifierCouleur);
		
		JLabel lblCote = new JLabel("Nombre de côtés :");
		lblCote.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblCote.setBounds(6, 103, 165, 33);
		panInformations.add(lblCote);
		
		spnNbCote = new JSpinner();
		spnNbCote.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnNbCote.setFont(new Font("Tahoma", Font.PLAIN, 20));
		spnNbCote.setModel(new SpinnerNumberModel(3, 3, 20, 1));
		spnNbCote.setBounds(178, 104, 108, 31);
		panInformations.add(spnNbCote);
		
		spnMesureCote = new JSpinner();
		spnMesureCote.setModel(new SpinnerNumberModel(5, 1, 30, 1));
		spnMesureCote.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				 majModificationObstacle();
			}
		});
		spnMesureCote.setFont(new Font("Tahoma", Font.PLAIN, 20));
		spnMesureCote.setBounds(196, 183, 108, 31);
		panInformations.add(spnMesureCote);
		
		JLabel lblMesureCote = new JLabel("Mesure côté :");
		lblMesureCote.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMesureCote.setBounds(6, 182, 129, 33);
		panInformations.add(lblMesureCote);
		
		JLabel lblMMesureCote = new JLabel("cm");
		lblMMesureCote.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMMesureCote.setBounds(318, 187, 45, 22);
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
		System.out.println("Envoie !");
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
		repaint();
		pcs.firePropertyChange("pointeur","Ouistiti",apercuPolygone.getObstacle());
		
	}
}

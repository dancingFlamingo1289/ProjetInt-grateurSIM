package scene.pagesObstacles;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
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
import scene.pagesObstacles.apercu.ApercuCercle;
import javax.swing.event.ChangeListener;

import obstacles.Obstacle;

import javax.swing.event.ChangeEvent;
/**Classe permettant de modifier et de placer l'obstacle de type Cercle.
 * PageCercle dérive de JPanel
 * 
 * @author Aimé Melançon
 */
public class PageCercle extends JPanel {

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
	/** JSpinner permettant de choisir le rayon du trou noir**/
	private JSpinner spnDiam;
	/**support pour lancer des evenements de type PropertyChange**/
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	/**L'aperçu du cercle**/
	private ApercuCercle apercuCercle;
	/**
	 * Création de panneau
	 */
	//Aimé Melançon
	public PageCercle() {
		setBounds(100, 100, 450, 579);
		setLayout(null);

		JPanel panelInfo = new JPanel();
		panelInfo.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "Param\u00E8tres", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelInfo.setBounds(10, 197, 430, 337);
		add(panelInfo);
		panelInfo.setLayout(null);

		JLabel lblPosition = new JLabel("Position :");
		lblPosition.setBounds(33, 29, 89, 41);
		lblPosition.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panelInfo.add(lblPosition);

		spnX = new JSpinner();
		spnX.setModel(new SpinnerNumberModel(50, 0, 100, 1));
		spnX.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnX.setBounds(153, 29, 74, 41);
		panelInfo.add(spnX);

		spnY = new JSpinner();
		spnY.setModel(new SpinnerNumberModel(100, 0, 200, 1));
		spnY.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnY.setBounds(264, 29, 59, 41);
		panelInfo.add(spnY);

		JLabel lblDiam = new JLabel("Diamètre :");
		lblDiam.setBounds(29, 158, 96, 30);
		lblDiam.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panelInfo.add(lblDiam);

		JLabel lblVirgule = new JLabel(",");
		lblVirgule.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblVirgule.setHorizontalAlignment(SwingConstants.CENTER);
		lblVirgule.setBounds(224, 29, 41, 41);
		panelInfo.add(lblVirgule);

		JLabel lblGauche = new JLabel("[");
		lblGauche.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblGauche.setBounds(140, 36, 15, 26);
		panelInfo.add(lblGauche);

		JLabel lblDroit = new JLabel("]");
		lblDroit.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblDroit.setBounds(333, 29, 12, 41);
		panelInfo.add(lblDroit);

		spnDiam = new JSpinner();
		spnDiam.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				majModificationObstacle();
			}
		});
		spnDiam.setModel(new SpinnerNumberModel(10, 5, 30, 1));
		spnDiam.setBounds(177, 158, 112, 30);
		panelInfo.add(spnDiam);

		JLabel lblM = new JLabel("cm");
		lblM.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblM.setBounds(353, 158, 41, 30);
		panelInfo.add(lblM);

		btnModifierCouleur = new JButton("Modifier la couleur");
		btnModifierCouleur.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				Color c= JColorChooser.showDialog(null, "Choisir la couleur du cercle", null) ;
				if(c!=null) {
			
					apercuCercle.setCouleur(c);
				}
			}
		});
		btnModifierCouleur.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnModifierCouleur.setBounds(115, 281, 200, 30);
		panelInfo.add(btnModifierCouleur);

		JLabel lblMPosition = new JLabel("cm");
		lblMPosition.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMPosition.setBounds(351, 36, 45, 30);
		panelInfo.add(lblMPosition);

		JLabel lblObstacle = new JLabel("Cercle".toUpperCase());
		lblObstacle.setHorizontalAlignment(SwingConstants.CENTER);
		lblObstacle.setFont(new Font("Lucida Grande", Font.BOLD, 20));
		lblObstacle.setBounds(110, 10, 224, 19);
		add(lblObstacle);

		btnPlacerObstacle= new JButton("Ajouter le cercle");
		btnPlacerObstacle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ajoutDObstacleTable();
			}
		});
		btnPlacerObstacle.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnPlacerObstacle.setBounds(117, 544, 210, 25);
		add(btnPlacerObstacle);

		apercuCercle = new ApercuCercle();
		apercuCercle.setBounds(10, 39, 430, 157);
		add(apercuCercle);

	}
	/**
	 * Méthode permettant d'envoyer un obstacle à la table
	 */
	//Aimé Melançon
	public void ajoutDObstacleTable() {
		pcs.firePropertyChange("obstacle", null, apercuCercle.getObstacle());
		System.out.println("Envoie !");
	}


	/**
	 * Méthode permettant de mettre à jour les paramètres de l'obstacle.
	 */
	//Aimé Melançon
	private void majModificationObstacle() {
		apercuCercle.setDiametre(Double.parseDouble( spnDiam.getValue().toString()));
		apercuCercle.setPosition(Double.parseDouble(spnX.getValue().toString()),Double.parseDouble( spnY.getValue().toString()) );
		
		pcs.firePropertyChange("pointeur",null,apercuCercle.getObstacle());
	}
	/**
	 * voici la methode qui permettra de s'ajouter en tant qu'ecouteur
	 */
	//Aimé Melançon
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

}

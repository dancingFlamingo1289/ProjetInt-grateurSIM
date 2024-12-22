package scene.pagesObstacles.apercu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeListener;
//import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeSupport;

import javax.swing.JPanel;

import math.vecteurs.Vecteur3D;
import obstacles.Obstacle;
import obstacles.TrouNoir;
/**Classe pour avoir un apperçu des modification du Trou Noir
 * ApercuTrouNoir dérive de JPanel
 * 
 * @author Aimé Melançon
 */
public class ApercuTrouNoir extends JPanel {
	/** Identifiant de classe**/
	private static final long serialVersionUID = 1L;
	/**support pour lancer des evenements de type PropertyChange**/
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	/**Seulement exécuté la première fois.**/
	private boolean premiereFois=true;
	/**La mesure du composant en mètre. **/
	private double pixelsParMetre ;
	/**La largeur du composant en mètre. **/
	private double largeurDuComposant = 100;
	/**La hauteur du composant en mètre. **/
	private double hauteurDuComposant = 50;
	/**Position milieu du panneau **/
	private  Vecteur3D vecMilieu;
	/**Position de l'objet sur la table **/
	private Vecteur3D position= new Vecteur3D(50,100);
	/**La valeur de la couleur de l'obstacle **/
	private Color couleur = Color.gray;
	/** La masse de l'obstacle**/
	private double masse=2E14;
	/**Le trou noir servant d'appeçu **/
	private TrouNoir saintTrouNoir;
	/**Le diamètre du trou noir. **/
	private double diametre=10;


	/**
	 * Création de panneau
	 */
	//Aimé Melançon
	public ApercuTrouNoir() {
		setLayout(null);
		setBackground(Color.WHITE);
	}
	/**Méthode permettant de dessiner l'aperçu de l'obstacle
	 * @param g L'argument graphique
	 */
	//Aimé Melançon
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		if(premiereFois) {
			pixelsParMetre= getWidth()/largeurDuComposant;
			vecMilieu = new Vecteur3D(largeurDuComposant/2, hauteurDuComposant/2-diametre/2);
			premiereFois=false;
		}

		g2d.drawString("Position sur la table "+ position , 0,(int) 10);
		g2d.scale(pixelsParMetre, pixelsParMetre);
		saintTrouNoir= new TrouNoir(vecMilieu, diametre, diametre, couleur);
		saintTrouNoir.dessiner(g2d);
	}

	/**Méthode permettant d'avoir la position.
	 * @return La position
	 */
	//Aimé Melançon
	public Vecteur3D getPosition() {
		return position;
	}
	/**Méthode permettant de changer la position de l'obstacle
	 * @param x le nouveau x
	 * @param y le nouveau y
	 */
	//Aimé Melançon
	public void setPosition(double x, double y) {

		this.position = new Vecteur3D(x,y);
		repaint();
	}
	/**Méthode permettant d'avoir la couleur de l'obstacle.
	 * @return the couleur
	 */
	//Aimé Melançon
	public Color getCouleur() {
		return couleur ;
	}
	/**Méthode permettant de changer la couleur de l'obstacle.
	 * @param couleur la couleur à changer.
	 */
	//Aimé Melançon
	public void setCouleur(Color couleur) {
		this.couleur = couleur;
		repaint();
	}

	/**Méthode permettant d'avoir la masse de l'obstacle
	 * @return La masse
	 */
	//Aimé Melançon
	public double getMasse() {
		return masse;
	}
	/**Méthode permettant de changer la masse de l'obstacle
	 * @param masse La nouvelle masse
	 */
	//Aimé Melançon
	public void setMasse(double masse) {
		this.masse = masse;
		repaint();
	}
	/**Méthode permettant d'avoir le diamètre de l'obstacle
	 * @return le diamètre
	 */
	//Aimé Melançon
	public double getDiametre() {
		return diametre;
	}
	/**Méthode permettant de changer le rayon de l'obstacle.
	 * @param diametre Le nouveau diamètre
	 */
	//Aimé Melançon
	public void setDiametre(double diametre) {
		this.diametre = diametre;
		repaint();
	}
	/**
	 * voici la methode qui permettra de s'ajouter en tant qu'ecouteur
	 */
	//Aimé Melançon
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}
	/**Méthode permettant d'avoir l'obstac
	 * 
	 * @return L'obstacle
	 */
	//Aimé Melançon
	public Obstacle getObstacle() {
		
		return new TrouNoir(position, diametre, masse, couleur);
	}
}

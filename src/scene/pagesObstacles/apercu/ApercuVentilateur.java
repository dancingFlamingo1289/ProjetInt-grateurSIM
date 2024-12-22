package scene.pagesObstacles.apercu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
//import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JPanel;

import math.vecteurs.Vecteur3D;
import obstacles.Obstacle;
import obstacles.Ventilateur;

/**Classe pour avoir un apperçu des modification du Ventilateur
 * ApercuVentilateur dérive de JPanel
 * 
 * @author Aimé Melançon
 */
public class ApercuVentilateur extends JPanel {

	private static final long serialVersionUID = 1L;
	/**support pour lancer des evenements de type PropertyChange**/
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	/**Le ventilateur **/
	private Ventilateur ventilateur;
	/**Position milieu du panneau **/
	private  Vecteur3D vecMilieu;
	/**Position de l'objet sur la table **/
	private Vecteur3D position= new Vecteur3D(50,100);
	/**Seulement exécuté la première fois.**/
	private boolean premiereFois=true;
	/**La couleur du ventilateur **/
	private Color couleur = Color.pink;
	/**L'orientation dans l'espace du ventilateur. **/
	private double orientation=0 ;
	/**La hauteur du ventilateur. **/
	private double hauteur=10;
	/** La largeur du ventilateur**/
	private double largeur=10;
	/**La mesure du composant en mètre. **/
	private double pixelsParMetre ;
	/**La largeur du composant en mètre. **/
	private double largeurDuComposant = 100;
	/**La hauteur du composant en mètre. **/
	private double hauteurDuComposant = 50;
	/**La force du ventilateur qui va être placé. **/
	private double forceVentilateur;
  
	/**
	 * Création de panneau
	 */
	//Aimé Melançon
	public ApercuVentilateur() {

		setBackground(Color.WHITE);
		setLayout(null);

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
			vecMilieu = new Vecteur3D(largeurDuComposant/2, hauteurDuComposant/2);
			premiereFois=false;

		}
		g2d.drawString("Position sur la table "+ position , 0,(int) 10);
		g2d.scale(pixelsParMetre, pixelsParMetre) ;
		ventilateur = new Ventilateur(vecMilieu,hauteur ,largeur ,forceVentilateur,orientation , couleur);
		ventilateur.dessiner(g2d);




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
		return couleur;
	}
	/**Méthode permettant de changer la couleur de l'obstacle.
	 * @param couleur la couleur à changer.
	 */
	//Aimé Melançon
	public void setCouleur(Color couleur) {
		this.couleur = couleur;
		repaint();
	}
	/**Méthode permettant d'avoir l'orientation de l'obstacle
	 * @return l'orientation
	 */
	//Aimé Melançon
	public double getOrientation() {
		return orientation;
	}
	/**Méthode permettant de changer l'orientation de l'obstacle
	 * @param orientation La nouvelle orientation
	 */
	//Aimé Melançon
	public void setOrientation(double orientation) {
		this.orientation = orientation;
		repaint();
	}
	/**Méthode permettant d'avoir la hauteur de l'obstacle.
	 * @return La hauteur
	 */
	//Aimé Melançon
	public double getHauteur() {
		return hauteur;
	}
	/**Méthode permettant de changer la hauteur de l'obstacle.
	 * @param hauteur La nouvelle hauteur
	 */
	//Aimé Melançon
	public void setHauteur(double hauteur) {
		this.hauteur = hauteur;
		repaint();
	}
	/**Méthode permettant d'avoir la largeur.
	 * @return La largeur
	 */
	//Aimé Melançon
	public double getLargeur() {
		return largeur;
	}
	/**Méthode permettant de changer la largeur de l'obstacle.
	 * @param largeur La nouvelle largeur
	 */
	//Aimé Melançon
	public void setLargeur(double largeur) {
		this.largeur = largeur;
		repaint();
	}
	/**Méthode permettant de changer la force du ventilateur.
	 * 
	 * @param forceVentilateur La nouvelle force du ventilateur
	 */
	//Aimé Melançon
	public void setForceVentilateur(double forceVentilateur) {
		this.forceVentilateur=forceVentilateur;
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
		return new Ventilateur(position, hauteur, largeur, forceVentilateur, orientation, couleur);
	}

}

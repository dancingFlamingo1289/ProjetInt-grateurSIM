package scene.pagesObstacles.apercu;

import java.awt.*;
import java.beans.*;
import javax.swing.*;
import math.vecteurs.*;
import obstacles.*;

/**
 * Classe pour avoir un apperçu des modification du mur
 * ApercuMur dérive de JPanel
 * 
 * @author Aimé Melançon
 */
public class ApercuMur extends JPanel {
	/**Identifiant de classe**/
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
	private Color couleur = Color.RED;
	/**La hauteur du mur amovible qui est en apperçu**/
	private double hauteur=10;
	/**La largeur du mur amovible qui est en apperçu **/
	private double largeur=5;
	/**L'angle en radiant du mur amovible **/
	private double angle=0;
	/**L'aperçu du mur **/
	private Mur mur;

	/**
	 * Création de panneau
	 */
	//Aimé Melançon
	public ApercuMur() {
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
			vecMilieu = new Vecteur3D(largeurDuComposant/2, hauteurDuComposant/2-hauteur/2);
			premiereFois=false;
		}
		g2d.drawString("Position sur la table "+ position , 0,(int) 10);
		g2d.scale(pixelsParMetre, pixelsParMetre);
		mur = new Mur(vecMilieu, hauteur, largeur, angle,  couleur);
		mur.dessiner(g2d);
	}
	/**Méthode permettant d'avoir la position.
	 * @return La position
	 */
	//Aimé Melançon
	public Vecteur3D getPosition() {
		return position;
	}
	/**Méthode permettant de changer la position de l'obstacle
	 * @param x La position par rapport au x de la table
	 * @param y La position par rapport au y de la table
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
	/**Méthode permettant d'avoir la hauteur du mur en apperçu
	 * @return la hauteur
	 */
	//Aimé Melançon
	public double getHauteur() {
		return hauteur;
	}
	/**Méthode permettant de changer l'hauteur de la représentation du mur
	 * @param hauteur la nouvelle hauteur
	 */
	//Aimé Melançon
	public void setHauteur(double hauteur) {
		this.hauteur = hauteur;
	}
	/**Méthode permettant d'avoir la largeur de la représentation du mur
	 * @return la largeur
	 */
	//Aimé Melançon
	public double getLargeur() {
		return largeur;
	}
	/**Méthode permettant de changer la largeur de la représentation du mur 
	 * @param largeur la nouvelle largeur
	 */
	//Aimé Melançon
	public void setLargeur(double largeur) {
		this.largeur = largeur;
	}
	/**Méthode permettant d'avoir l'angle de la représentation du mur
	 * @return l'angle
	 */
	//Aimé Melançon
	public double getAngle() {
		return angle;
	}
	/**Méthode permettant de changer l'angle de la représentation du mur a
	 * @param angle Le nouvel angle
	 */
	//Aimé Melançon
	public void setAngle(double angle) {
		this.angle = angle;
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
		
		return new Mur(position, hauteur, largeur, angle, couleur);
	}
	
	
}

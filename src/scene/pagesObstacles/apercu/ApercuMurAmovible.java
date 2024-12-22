package scene.pagesObstacles.apercu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JPanel;

import math.vecteurs.Vecteur3D;
import obstacles.Mur;
import obstacles.MurAmovible;
import obstacles.Obstacle;
/**Classe pour avoir un apperçu des modification du Mur Amovible
 * ApercuMurAmovible dérive de JPanel
 * 
 * @author Aimé Melançon
 */
public class ApercuMurAmovible extends JPanel {

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
	private Vecteur3D position= new Vecteur3D(0,0);;
	/**La valeur de la couleur de l'obstacle **/
	private Color couleur = Color.gray;
	/**La hauteur du mur amovible qui est en apperçu**/
	private double hauteur=10;
	/**La largeur du mur amovible qui est en apperçu **/
	private double largeur=5;
	/**L'angle en radiant du mur amovible **/
	private double angle=0;
	/**La vitesse que le mur va bouger **/
	private double vitesseCroisiere=1;
	/**L'aperçu du mur amovible **/
	private MurAmovible murBougeant;
	/**
	 * Création de panneau
	 */
	//Aimé Melançon
	public ApercuMurAmovible() {
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
		murBougeant = new MurAmovible(vecMilieu, hauteur, largeur, angle, vitesseCroisiere, couleur);
		murBougeant.dessiner(g2d);
	}
	/**Méthode permettant d'avoir la position.
	 * @return La position
	 */
	//Aimé Melançon
	public Vecteur3D getPosition() {
		return position;
	}
	/**Méthode permettant de changer la position de l'obstacle
	 * @param position la nouvelle position
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
	/**Méthode permettant d'avoir la hauteur du mur amovible en apperçu
	 * @return la hauteur
	 */
	//Aimé Melançon
	public double getHauteur() {
		return hauteur;
	}
	/**Méthode permettant de changer l'hauteur de la représentation du mur amovible
	 * @param hauteur la nouvelle hauteur
	 */
	//Aimé Melançon
	public void setHauteur(double hauteur) {
		this.hauteur = hauteur;
	}
	/**Méthode permettant d'avoir la largeur de la représentation du mur amovible
	 * @return la largeur
	 */
	//Aimé Melançon
	public double getLargeur() {
		return largeur;
	}
	/**Méthode permettant de changer la largeur de la représentation du mur amovible
	 * @param largeur la nouvelle largeur
	 */
	//Aimé Melançon
	public void setLargeur(double largeur) {
		this.largeur = largeur;
	}
	/**Méthode permettant d'avoir l'angle de la représentation du mur amovible
	 * @return l'angle
	 */
	//Aimé Melançon
	public double getAngle() {
		return angle;
	}
	/**Méthode permettant de changer l'angle de la représentation du mur amovible
	 * @param angle Le nouvel angle
	 */
	//Aimé Melançon
	public void setAngle(double angle) {
		this.angle = angle;
	}
	/**Méthode permettant d'avoir la vitesse de croisière
	 * @return la vitesseCroisiere
	 */
	//Aimé Melançon
	public double getVitesseCroisiere() {
		return vitesseCroisiere;
	}
	/**Méthode permettant de changer la vitesse de croisière de l'apperçu du mur amovible
	 * @param vitesseCroisiere La nouvelle vitesse de croisière
	 */
	//Aimé Melançon
	public void setVitesseCroisiere(double vitesseCroisiere) {
		this.vitesseCroisiere = vitesseCroisiere;
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
		
		return new MurAmovible(position, hauteur, largeur, angle,vitesseCroisiere, couleur);
	}
}

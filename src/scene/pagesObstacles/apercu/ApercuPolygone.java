package scene.pagesObstacles.apercu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JPanel;

import math.vecteurs.Vecteur3D;
import obstacles.Obstacle;
import obstacles.polygone.Polygone;
/**Classe pour avoir un apperçu des modification du Polygone
 * ApercuPolygone dérive de JPanel
 * Note : Pense à faire une méthode getPolygone(). Tu devrais peut-être permettre de prendre dans le 
 * constructeur un objet Polygone ou alors mettre les différents attributs d'un objet Polygone pour créer 
 * toi-même le polygone que tu veux dessiner.
 * @author Aimé Melançon
 */
public class ApercuPolygone extends JPanel {
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
	private Color couleur = Color.gray;
	/**La mesure d'un côté **/
	private double mesureCote = 5;
	/**Le nombre de côté du polygone**/
	private int nbCotes = 3;
	/**Le polygone d'aperçu **/
	private Polygone poly;
	/**
	 * Création de panneau
	 */
	//Aimé Melançon
	public ApercuPolygone() {

	}
	/**Méthode permettant de dessiner l'aperçu de l'obstacle
	 * @param g L'argument graphique
	 */
	//Aimé Melançon
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON) ; 
		if(premiereFois) {
			pixelsParMetre= getWidth()/largeurDuComposant;
			vecMilieu = new Vecteur3D(largeurDuComposant/2, hauteurDuComposant/2-mesureCote /2);
			premiereFois=false;
		}
		g2d.drawString("Position sur la table "+ position , 0,(int) 10);
		g2d.scale(pixelsParMetre, pixelsParMetre);
		poly = new Polygone(vecMilieu, nbCotes, mesureCote, couleur, Color.black);
		poly.dessiner(g2d);
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
	
	/**Méthode permettant d'avoir le nombre de côté du polygone
	 * @return Le nbCotes
	 */
	//Aimé Melançon
	public int getNbCotes() {
		return nbCotes;
	}
	
	/**Méthode permettant de changer le nombre de côté du polygones
	 * @param nbCotes Le nouveau nombres de côtés
	 */
	//Aimé Melançon
	public void setNbCotes(int nbCotes) {
		this.nbCotes = nbCotes;
		repaint();
	}
	
	/**Méthode permettant d'avoir la mesure d'un côté
	 * @return la mesureCote
	 */
	//Aimé Melançon
	public double getMesureCote() {
		return mesureCote;
	}

	/**Méthode permettant de changer la mesure du côté
	 * @param mesureCote La nouvelle mesure du côté
	 */
	//Aimé Melançon
	public void setMesureCote(double mesureCote) {
		this.mesureCote = mesureCote;
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
		
		return new Polygone(position, nbCotes, mesureCote, couleur, Color.black) ;
	}
}

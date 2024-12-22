package scene.pagesObstacles.apercu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JPanel;

import math.vecteurs.Vecteur3D;
import obstacles.Mur;
import obstacles.Obstacle;
import obstacles.plaqueMagnetique.PlaqueMagnetique;
/**Classe pour avoir un apperçu des modification du Plaque Magnetique
 * ApercuPlaqueMagnetique dérive de JPanel
 * 
 * @author Aimé Melançon
 */
public class ApercuPlaqueMagnetique extends JPanel {
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
	private Vecteur3D position= new Vecteur3D(0,0);;
	/**La valeur de la couleur de l'obstacle **/
	private Color couleur = Color.gray;
	/** Module du champ magnétique d'une zone de champ magnétique. **/
	private double champMagn =0 ;
	/**La longueur de la plaque magnétique. **/
	private double longueur=10;
	/**L'apperçu de la plaque magnétique **/
	private PlaqueMagnetique plaqueSacre;
	/**
	 * Création de panneau
	 */
	//Aimé Melançon
	public ApercuPlaqueMagnetique() {
		setLayout(null);
		setBackground(Color.WHITE);
	}
	/**Méthode permettant de dessiner l'aperçu de l'obstacle
	 *@param g L'argument graphique
	 */
	//Aimé Melançon
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		if(premiereFois) {
			pixelsParMetre= getWidth()/largeurDuComposant;
			vecMilieu = new Vecteur3D(largeurDuComposant/2, hauteurDuComposant/2-longueur/2);
			premiereFois=false;
		}
		g2d.drawString("Position sur la table "+ position , 0,(int) 10);
		g2d.scale(pixelsParMetre, pixelsParMetre);
		plaqueSacre = new PlaqueMagnetique(vecMilieu, longueur, champMagn, couleur);
		plaqueSacre.dessiner(g2d);
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

	/**Méthode permettant d'avoir le module du champ magnétique
	 * @return le champMagn
	 */
	//Aimé Melançon
	public double getChampMagn() {
		return champMagn;
	}
	/**Méthode permettant de changer le champ magnétique
	 * @param champMagn Le nouveau champ magnétique
	 */
	//Aimé Melançon
	public void setChampMagn(double champMagn) {
		this.champMagn = champMagn;
	}
	/**Méthode permettant d'avoir la longueur.
	 * @return la longueur
	 */
	//Aimé Melançon
	public double getLongueur() {
		return longueur;
	}
	/**Méthode permettant de changer la longueur de la plaque.
	 * @param longueur the longueur to set
	 */
	//Aimé Melançon
	public void setLongueur(double longueur) {
		this.longueur = longueur;
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
		
		return new PlaqueMagnetique( position,  longueur, 	 champMagn,  couleur);
	}
}

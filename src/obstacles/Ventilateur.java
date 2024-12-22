package obstacles;

import java.awt.* ;
import java.awt.geom.* ;
import composantDeJeu.* ;
import interfaces.* ;
import math.vecteurs.* ;

/**
 * Cette classe représente un ventilateur donnant une force de poussée à une balle.
 * @author Aimé Melançon
 */
public class Ventilateur extends Obstacle implements Dessinable, Selectionnable {
	/** Coefficient de sérialisation pour les fichiers. **/
	private static final long serialVersionUID = 1L ;
	/**Variable de type double qui a l'orientation du ventilateur. **/
	private double orientation;
	/**Variable de type double qui a la hauteur du ventilateur. **/
	private double hauteur;
	/**Variable de type double qui a la largeur du ventilateur. **/
	private double largeur;
	/**Variable contenant la boite du ventilateur. **/
	private Rectangle2D.Double corpsVentilo;
	/**Variable contenant la grille du ventilateur. **/
	private Rectangle2D.Double grilleVentilo;
	/**Variable permettant de créer le grillage verticalement. **/
	private Line2D.Double elementGrilleVerticale;
	/**Variable permettant de créer le grillage horizontalement. **/
	private Line2D.Double elementGrilleHorizontale;
	/**Variable permettant de créer l'aire du ventilateur. **/
	private transient Area ventilateur;
	/**Matrice permettant de gérer l'orientation de la figure. **/
	private AffineTransform mat = new AffineTransform();

	/**
	 * 
	 * @param position La position où est le ventilateur sur la table.
	 * @param hauteur La hauteur de grandeur du ventilateur.
	 * @param largeur La largeur du ventilateur.
	 * @param orientation L'orientation du ventilateur sur la table.
	 * @param couleur La couleur de la boite ayant le moteur du ventilateur.
	 */
	//Aimé Melançon
	public Ventilateur(Vecteur3D position,double hauteur, double largeur, double orientation, Color couleur) {
		super(position, couleur) ;
		this.hauteur= hauteur;
		this.orientation= orientation;
		this.largeur =largeur;
		creerLaGeometrie();
	}
	
	/**
	 * Méthode public pour créer les formes qui composent Ventilateur
	 * Cette méthode doit être appelée de nouveau chaque fois que sa position ou dimension est modifiée
	 */
	//Aimé Melançon
	protected void creerLaGeometrie() {
		double xBoite = this.position.getX() + largeur/2;
		double yBoite = this.position.getY() + hauteur/2;

		corpsVentilo= new Rectangle2D.Double(xBoite, yBoite,  largeur, hauteur);

		double positionGrilleX = xBoite+largeur/2;

		grilleVentilo = new Rectangle2D.Double(positionGrilleX, yBoite, largeur/2, hauteur);

		mat.rotate(orientation,this.position.getX(),this.position.getY());
		mat.translate(-largeur,-hauteur);
		ventilateur = new Area(mat.createTransformedShape(corpsVentilo));
		Area grille =new Area( mat.createTransformedShape(grilleVentilo));
		ventilateur.add(grille);
		
	}
	
	/**
	 * Permet de dessiner le ventilateur, sur le contexte graphique passe en parametre.
	 * @param g2d contexte graphique.
	 */
	//Aimé Melançon
	@Override
	public void dessiner(Graphics2D g2d) {

		Graphics2D g2dPrivee = (Graphics2D) g2d.create();

		g2dPrivee.setColor(this.couleur);
		
		g2dPrivee.fill(mat.createTransformedShape(corpsVentilo));

		creerGrillage(g2dPrivee);

		g2dPrivee.setColor(Color.BLACK);
		g2dPrivee.draw(mat.createTransformedShape(grilleVentilo));
		g2dPrivee.draw(ventilateur);
		
	}
	
	/**
	 * Permet de dessiner le grillage du ventilateur, sur le contexte graphique passe en parametre.
	 * @param g2d contexte graphique.
	 */
	//Aimé Melançon
	private void creerGrillage(Graphics2D g2dPrivee) {

		double xBoite = this.position.getX() + largeur/2;
		double yBoite = this.position.getY() + hauteur/2;
		double positionGrilleX = xBoite+largeur/2;
		double yMin= yBoite+hauteur;

		for(double i=1.55; i<largeur/2; i+=1.55) {

			elementGrilleVerticale= new Line2D.Double(positionGrilleX+i, yMin, positionGrilleX+i, yBoite);

			g2dPrivee.setColor(Color.GRAY);
			g2dPrivee.draw( mat.createTransformedShape(elementGrilleVerticale));
		}

		for(double i=1.55 ; i<hauteur; i+=1.55) {

			elementGrilleHorizontale = new Line2D.Double(positionGrilleX, yMin-i,positionGrilleX+largeur/2 , yMin-i); 

			g2dPrivee.setColor(Color.GRAY);
			g2dPrivee.draw(mat.createTransformedShape(elementGrilleHorizontale));
		}

	}
	
	/**
	 * Retourne vrai si le point passé en paramètre fait partie du ventilateur dessinable.
	 * sur lequel cette methode sera appelée
	 *
	 *@param x Coordonnée en x du point (exprimé en mètres) 
	 *@param y Coordonnée en y du point (exprimé en mètres)
	 */
	//Aimé Melançon
	@Override
	public boolean contient(double x, double y) {
		
		return ventilateur.contains(x, y);
	}

	@Override
	public void collision(Balle balle) {
	
		

	}
	
	/**
	 * Méthode permettant de déterminer si la balle est en intersection avec le ventilateur.
	 * 
	 * @param balle La position de la balle (du coin gauche)
	 * @return Retourne si la balle est en contact ou non avec le ventilateur.
	 */
	//Aimé Melançon
	@Override
	public boolean intersection(Balle balle) {
		
		Area ventilateurCopie = ventilateur;
		Area balleAireCopie =balle.getAireBalle();
		
		ventilateurCopie.intersect(balleAireCopie);
		
		// Tu peux simplement faire "return !ventilateurCopie.isEmpty()", Aimé
		if(!ventilateurCopie.isEmpty()) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public String toString() {
		return "Le ventilateur suivant est à la position ("+ this.position.getX()+","+ this.position.getY()+")"+ "; Sa hauteur x Largeur :"+ hauteur+"X"+largeur+
				"; Son orientation (radians) :" + this.orientation + "; Sa couleur :"+ this.couleur+";";
	}
	
	@Override
	public void avancerUnPas(java.lang.Double deltaT) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Méthode permettant de faire un déplacement d'un ventilateur sur la table.
	 * @param deplacement Le déplacement de l'obstacle effectué.
	 */
	//Aimé Melançon
	@Override
	public void setDeplacement(Vecteur3D deplacement) {
		this.setPosition(this.position.additionne(deplacement));
		
	}
}

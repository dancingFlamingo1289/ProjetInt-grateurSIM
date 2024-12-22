package obstacles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;
import composantDeJeu.Balle;
import gestionCollision.CollisionDesObjets;
import interfaces.Dessinable;
import interfaces.Selectionnable;
import math.vecteurs.Vecteur3D;

/**
 * Classe pour créer et gérer un objet de type cercle
 * @author Félix Lefrançois
 * @author Aimé Melançon
 */
public class Cercle extends Obstacle implements Selectionnable, Dessinable, Serializable{

	/** Identifiant de classe **/
	private static final long serialVersionUID = 1L;
	/** Le rayon du cercle en m **/
	private double diametre;
	/** Constante de la masse de la balle **/
	private final double MASSE = 300;
	/** Aire du cercle**/
	private transient Area aireCercle;
	/** La forme du cercle **/
	private Ellipse2D.Double cercle;
	
	/**
	 * Constructeur de la classe
	 * @param position Le vecteur position du centre de la balle
	 * @param diametre Le diametre en mètre de la balle
	 * @param couleur La couleur de la balle
	 */
	//Félix Lefrançois
	public Cercle(Vecteur3D position, double diametre,Color couleur) {
		super(position, couleur);
		this.diametre = diametre;
		creerLaGeometrie();
	}
	
	/**
	 * Méthode pour créer les composants de dessin
	 */
	//Félix Lefrançois
	@Override
	protected void creerLaGeometrie() {
		cercle = new Ellipse2D.Double(position.getX()-diametre/2,position.getY()-diametre/2,diametre,diametre);
		aireCercle = new Area() ;
		aireCercle = new Area(cercle);		
	}
	
	/**
	 * Méthode pour dessiner les composants de dessin
	 * @param g2d Contexte graphique
	 */
	//Félix Lefrançois
	@Override
	public void dessiner(Graphics2D g2d) {
		Graphics2D g2dPrive = (Graphics2D) g2d.create();
		
		g2dPrive.setColor(couleur);
		
		g2dPrive.fill(aireCercle);
	}

	/**
	 * Méthode confirmant si un point est dans le cercle ou non
	 * @param x La coordonnée en x du point
	 * @param y La coordonnée en y du point
	 * @return Un booléen si le point est contenu dans le cercle
	 */
	//Félix Lefrançois
	@Override
	public boolean contient(double x, double y) {
		return aireCercle.contains(x,y);
	}

	/**
	 * Méthode pour appliquer les changements de forces et de vitesse à une balle lors d'une collision avec un Cercle
	 * @param balle La balle testée
	 * @exception L'exception quand un vecteur est normalisée
	 */
	//Félix Lefrançois
	@Override
	public void collision(Balle balle) throws Exception {
		Area aireBalleTemp = new Area(balle.getAireBalle());
		Area aireCercleTemp = new Area(aireCercle);
		
		aireCercleTemp.intersect(aireBalleTemp);
		if (!aireCercleTemp.isEmpty()) {
			balle.setVitesse(CollisionDesObjets.vitesseFinaleMouvementCercleImmobile(balle, 
					MASSE, position.getX(), position.getY()));
			Vecteur3D normale = CollisionDesObjets.trouverNormaleCollisionCercle(balle, position.getX(), position.getY());
			balle.setPosition(balle.getPosition().additionne(normale));
		}
		
	}

	/**
	 * Méthode pour vérifier si il y a une intersection avec une balle
	 * @param balle La balle testée
	 * @return Un booléen confirmant qu'il y a une intersection avec la balle
	 */
	//Félix Lefrançois
	@Override
	public boolean intersection(Balle balle) {
		boolean verification = false;
		Area aireTest = new Area(aireCercle);
		
		aireTest.intersect(balle.getAireBalle());
		if (!aireTest.isEmpty()) {
			verification = true;
		}
		return verification;
	}

	/**
	 * Méthode envoyant une chaîne de caractères résumant un Cercle
	 * @return Une chaîne de caractères résumant les caractéristiques d'un Cercle
	 */
	//Félix Lefrançois
	@Override
	public String toString() {
		return "La position du centre du cercle est à la coordonnée ("+position.getX()+","+position.getY()+") et a un rayon de "+diametre+" m.";
	}

	/**
	 * INUTILE
	 * Méthode permettant de calculer les changements physiques du Cercle en fonction du deltaT
	 * @param deltaT La différence de temps simulé
	 */
	//Félix Lefrançois
	@Override
	public void avancerUnPas(Double deltaT) {
	}

	/**
	 * Méthode retournant le diamètre du cercle
	 * @return Le diamètre en mètre du cercle
	 */
	//Félix Lefrançois
	public double getDiametre() {
		return diametre;
	}

	/**
	 * Méthode permettant de changer le diamètre du cercle
	 * @param diametre Le nouveau diamètre du cercle
	 */
	//Félix Lefrançois
	public void setDiametre(double diametre) {
		this.diametre = diametre;
		creerLaGeometrie();
	}

	/**
	 * Méthode retournant la constante de masse des obstacles de type cercle
	 * @return La masse en kg des cercles
	 */
	//Félix Lefrançois
	public double getMASSE() {
		return MASSE;
	}

	/**
	 * Méthode permettant de faire un déplacement d'un cercle sur la table.
	 * @param deplacement Le déplacement de l'obstacle effectué.
	 */
	//Aimé Melançon
	@Override
	public void setDeplacement(Vecteur3D deplacement) {
		this.setPosition(this.position.additionne(deplacement));
		
	}

}

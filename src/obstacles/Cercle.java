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
import outils.GererSon;
import outils.OutilsSon;
import scene.Scene;

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
	private final double MASSE = 30000;
	/** Aire du cercle**/
	private transient Area aireCercle;
	/** La forme du cercle **/
	private Ellipse2D.Double cercle,cercle2,cercleCentre;
	/** Constante du nombre de points ajoutés lors d'une collision avec un cercle**/
	private final int POINTS_AJOUTES = 200;
	/** La différence de rayon entre un cercle intérieur et le cercle juste avant**/
	private final int DIFFERENCE_RAYON =1;
	/** OutilsSon de l'obstacle**/
	private transient OutilsSon sonObst=new OutilsSon();
	/**Nom du fichier du son**/
	private final String NOM_DU_FICHIER="Cercle.wav";
	/** Booléen confirmant si c'est la première fois que l'on crée la géométrie **/
	private boolean premiereFois = true;
	
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
		if (premiereFois) {
			sonObst = new OutilsSon() ;
			sonObst.chargerUnSonOuUneMusique(NOM_DU_FICHIER);
			premiereFois = false;
		}
		cercle = new Ellipse2D.Double(position.getX()-diametre/2,position.getY()-diametre/2,diametre,diametre);
		cercle2 = new Ellipse2D.Double(cercle.x+DIFFERENCE_RAYON,cercle.y+DIFFERENCE_RAYON,cercle.width-2*DIFFERENCE_RAYON, cercle.height-2*DIFFERENCE_RAYON);
		cercleCentre = new Ellipse2D.Double(cercle2.x+DIFFERENCE_RAYON,cercle2.y+DIFFERENCE_RAYON,cercle2.width-2*DIFFERENCE_RAYON, cercle2.height-2*DIFFERENCE_RAYON);

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

		if (aireCercle == null)
			creerLaGeometrie() ;
		g2dPrive.fill(aireCercle);
		g2dPrive.setColor(couleur.darker());
		g2dPrive.fill(cercle2);
		g2dPrive.setColor(couleur.darker().darker());
		g2dPrive.fill(cercleCentre);

		dessinerContour(g2d);
	}
	
	/**
	 * Méthode pour dessiner le contour de l'obstacle.
	 * @param g2d Contexte graphique
	 */
	//Aimé Melançon
	private void dessinerContour(Graphics2D g2d) {
		g2d.draw(aireCercle);
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
	 * Méthode inutile pour le cercle, car celle-ci sert à réinatialiser l'animation.
	 */
	//Aimé Melançon
	@Override
	public void reinitialiser() {
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
		
		if (aireCercle == null)
			creerLaGeometrie() ;
		
		Area aireTest = new Area(aireCercle);
		
		aireTest.intersect(balle.getAireBalle());
		if (!aireTest.isEmpty()) {
			verification = true;
		}
		return verification;
	}
	
	/**
	 * Méthode vérifiant si il y a une intersection entre un obstacle de type Cercle et un autres obsatcle
	 * @param obst L'obstacle testé
	 * @return Un booléen confirmant si il y a une collision
	 */
	//Félix Lefrançois
	@Override
	public boolean intersection(Obstacle obst) {
		Area copieAireCercle = new Area(aireCercle);
		Area copieAireObstacle = new Area(obst.getAire());
		
		copieAireCercle.intersect(copieAireObstacle);
		return !copieAireCercle.isEmpty();
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
			if (sonObst == null) {
				premiereFois = true ;
				creerLaGeometrie() ;
			}
			if(GererSon.isAllumerFermer()) {
			sonObst.jouerUnSon();
			}
			balle.setVitesse(CollisionDesObjets.vitesseFinaleMouvementCercleImmobile(balle, 
					MASSE, position.getX(), position.getY()));
			Vecteur3D normale = CollisionDesObjets.trouverNormaleCollisionCercle(balle, position.getX(), position.getY());
			balle.setPosition(balle.getPosition().additionne(normale));
		}
		
		if (!(Scene.getPoints() == null))Scene.getPoints().ajouterPoints(POINTS_AJOUTES);

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
	 * Méthode envoyant une chaîne de caractères résumant un Cercle
	 * @return Une chaîne de caractères résumant les caractéristiques d'un Cercle
	 */
	//Félix Lefrançois
	@Override
	public String toString() {
		return "La position du centre du cercle est à la coordonnée ("+position.getX()+","+position.getY()+") et a un rayon de "+diametre+" m.";
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

	/**
	 * Méthode retournant l'aire de l'obstacle
	 * @return L'aire du cercle
	 */
	//Félix Lefrançois
	@Override
	public Area getAire() {
		return aireCercle;
	}

}

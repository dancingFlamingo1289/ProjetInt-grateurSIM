package obstacles;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import composantDeJeu.Balle;
import composantDeJeu.Table;
import gestionCollision.CollisionDesObjets;
import interfaces.Dessinable;
import interfaces.Selectionnable;
import math.vecteurs.FlecheVectorielle;
import math.vecteurs.Vecteur3D;
import outils.GererSon;
import outils.OutilsSon;
import physique.MoteurPhysique;
import scene.Scene;

/**
 * Classe héritant de BaseMur qui crée un mur mobile
 * @author Félix Lefrançois
 * @author Aimé Melançon
 */
public class MurMobile extends BaseMur implements Selectionnable, Dessinable{
	/** Coefficient de sérialisation pour les fichiers. **/
	private static final long serialVersionUID = 1L ;
	/** Le vecteur de vitesse de croisiere du mur amovible**/
	private Vecteur3D vitesseCroisiere;
	/** La vitesse de croisière sous forme de norme **/
	private double valeurVitesse;
	/** Les coordonnées initiales du centre du mur lorsqu'il est créé**/
	private double centreXInit, centreYInit;
	/** La masse en kg du mur amovible**/
	private final double MASSE = 1000;
	/** La limite de mouvement d'un mur amovible par rapport à son centre**/
	private final double LIMITE_MOUVEMENT = 20;
	/** L'angle de rotation en radian du mur **/
	private double angle;
	/** LA flèche visuelle de la vitesse du mur **/
	private FlecheVectorielle flecheVitesse;
	/** Le rectangle permettant de former l'aire total du mur amovible incluant sa zone de déplacement**/
	private Rectangle2D.Double rectangleAireTotal;
	/** L'aire total du rectangle (va être utile si l'on veut vérifier qu'il n'y a pas d'autres obstacles dans le chemin**/
	private transient Area aireTotal;
	/** La hauteur et la largeur du mur en cm**/
	private double hauteur, largeur;
	/** Constante du nombre de points ajoutés lors d'une collision avec un mur mobile**/
	private final int POINTS_AJOUTES = 250;
	/** Booléen confirmant si c'est la première fois que l'on crée la géométrie **/
	private boolean premiereFois = true;
	/** OutilsSon de l'obstacle**/
	private transient OutilsSon sonObst = new OutilsSon();
	/**Nom du fichier du son**/
	private final String NOM_DU_FICHIER="LesMurs.wav";
	/**Trait pointillé **/
	private transient Stroke zonePointille ;
	/** Matrice de transformation de la zone de pointillé**/
	AffineTransform mat;

	/**
	 * Constructeur de la classe
	 * @param position Vecteur position du centre du mur mobile
	 * @param hauteur Hauteur en cm du mur
	 * @param largeur Largeur en cm du mur
	 * @param angle Angle de rotation en radian du mur
	 * @param vitesseCroisiere Le module de la vitesse du mur en cm/s
	 * @param couleur Couleur du mur
	 */
	//Félix Lefrançois
	public MurMobile(Vecteur3D position, double hauteur, double largeur, double angle, double vitesseCroisiere, Color couleur) {	
		super(position, hauteur,largeur,angle,couleur);
		this.vitesseCroisiere  = new Vecteur3D(Math.cos(angle)*vitesseCroisiere,Math.sin(angle)*vitesseCroisiere);
		this.valeurVitesse = vitesseCroisiere;
		centreXInit = position.getX();
		centreYInit = position.getY();
		this.hauteur = hauteur;
		this.largeur = largeur;
		this.angle = angle;
		creerLaGeometrie();
	}
	
	/**
	 * Méthode qui crée tous les composanta de dessin du mur mobile
	 */
	//Félix Lefrançois
	protected void creerLaGeometrie() {
		super.creerLaGeometrie();
		
		if (premiereFois) {
			sonObst = new OutilsSon() ;
			sonObst.chargerUnSonOuUneMusique(NOM_DU_FICHIER);
			premiereFois = false;
		}
		
		rectangleAireTotal = new Rectangle2D.Double(0-largeur/2-LIMITE_MOUVEMENT,0-hauteur/2,largeur+LIMITE_MOUVEMENT*2, hauteur);
		mat = new AffineTransform();
		mat.translate(centreXInit, centreYInit);
		mat.rotate(angle);
		aireTotal = new Area(mat.createTransformedShape(rectangleAireTotal));
		flecheVitesse = new FlecheVectorielle(position.getX(),position.getY(),vitesseCroisiere);
		
		zonePointille = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 
				0, new float[] {7.0f}, 0) ;
		
	}
	
	/**
	 * Méthode pour dessiner un mur mobile
	 * @param g2d Contexte graphique
	 */
	//Félix Lefrançois
	public void dessiner(Graphics2D g2d) {
		Graphics2D g2dPrive = (Graphics2D) g2d.create();
		super.dessiner(g2dPrive);
		flecheVitesse.dessiner(g2dPrive);
		g2dPrive.setStroke(zonePointille);
		g2dPrive.draw(aireTotal);
	}
	
	/**
	 * Méthode qui applique les changements physiques à un mur mobile lors de l'animation
	 * @param deltaT La différence de temps simulé
	 */
	//Félix Lefrançois
	@Override
	public void avancerUnPas(Double deltaT) {
		position = MoteurPhysique.calculPosition(deltaT, position, vitesseCroisiere);
		if (position.getX() > centreXInit+Math.cos(angle)*LIMITE_MOUVEMENT || position.getX() < centreXInit- Math.cos(angle)*LIMITE_MOUVEMENT
				|| (position.getY() < centreYInit-Math.sin(angle)*LIMITE_MOUVEMENT && angle > 0) || (position.getY() > centreYInit+Math.sin(angle)*LIMITE_MOUVEMENT && angle > 0)
				||(position.getY() > centreYInit-Math.sin(angle)*LIMITE_MOUVEMENT && angle < 0) || (position.getY() < centreYInit+Math.sin(angle)*LIMITE_MOUVEMENT && angle < 0)) {
			vitesseCroisiere = vitesseCroisiere.multiplie(-1);
		}
		creerLaGeometrie();
	}
	
	/**
	 * Méthode permettant de réinitialiser un mur mobile
	 */
	//Félix Lefrançois
	@Override
	public void reinitialiser() {
		this.vitesseCroisiere  = new Vecteur3D(Math.cos(angle)*valeurVitesse,Math.sin(angle)*valeurVitesse);
		position = new Vecteur3D(centreXInit,centreYInit,0);
		creerLaGeometrie();
	}

	/**
	 * Méthode vérifiant si il y a une collision entre un mur mobile et une balle
	 * @param balle La balle testée
	 * @return Un booléen confirmant si il y a une collision
	 */
	//Félix Lefrançois
	public boolean intersection(Balle balle) {
		return super.intersection(balle);
	}
	
	/**
	 * Méthode vérifiant si il y a une intersection entre un objet de type MurMobile et un obstacle
	 * @param obst L'obstacle testé
	 * @return Un booléen confirmant si il y a une intersection avec l'obstacle
	 */
	//Félix Lefrançois
	@Override
	public boolean intersection(Obstacle obst) {
		Area aireTotalCopie = new Area(aireTotal);
		Area aireObstacleCopie = new Area(obst.getAire());
		
		aireTotalCopie.intersect(aireObstacleCopie);
		return !aireTotalCopie.isEmpty();
	}
	
	/**
	 * Méthode pour appliquer les effets d'une collision avec le mur mobile sur une balle
	 * @param balle La balle testée
	 * @exception Exception lorsqu'un vecteur est normalisé
	 */
	//Félix Lefrançois
	@Override
	public void collision(Balle balle) throws Exception {
		if (sonObst == null) {
			sonObst = new OutilsSon() ;
			creerLaGeometrie() ;
		}
		
        boolean collisionCoin = false;
		
        for (Cercle coin : lesCoins) {
			if(coin.intersection(balle)) {
				Vecteur3D vitesseResultante = CollisionDesObjets.vitesseFinaleMouvementObstacleCercleMobile(balle, coin, vitesseCroisiere);
				balle.setVitesse(vitesseResultante);
				balle.setPosition(balle.getPosition().additionne(new Vecteur3D(balle.obtenirCentreX()-coin.getPosition().getX()
						,balle.obtenirCentreY()-coin.getPosition().getY(),0).normalise()));
				collisionCoin = true;
			}
		}
		
		if (!collisionCoin) {
		    Vecteur3D normale = CollisionDesObjets.trouverNormaleCollisionMur(balle, Table.trouverMurLePlusProche(balle, lesMurs));
		    balle.setVitesse(CollisionDesObjets.vitesseFinaleCollisionAvecMurMobile(balle, normale, vitesseCroisiere,MASSE));
		    balle.setPosition(balle.getPosition().additionne(normale));
		    if (!(Scene.getPoints() == null))Scene.getPoints().ajouterPoints(POINTS_AJOUTES);
		    
		    if (sonObst == null) {
				premiereFois = true ;
				creerLaGeometrie() ;
			}
		    if(GererSon.isAllumerFermer()) {
				sonObst.jouerUnSon();
				}
		}
	}
	
	/**
	 * Méthode pour vérifier si un point est contenu dans l'aire du mur mobile
	 * @param xPix Coordonnée en x du point
	 * @param yPix Coordonnée en y du point
	 * @return Un booléen confirmant si le point est contenu dans l'aire ou non
	 */
	//Félix Lefrançois
	public boolean contient (double xPix, double yPix) {
		return super.contient(xPix, yPix);
	}

	/**
	 * Méthode résumant les caractéristiques d'un mur mobile
	 * @return Une chaîne de caractères résumant les caractéristiques d'un mur mobile
	 */
	//Félix Lefrançois
	public String toString() {
		return super.toString()+" Le mur a aussi une vitesse de croisiere de ("+vitesseCroisiere.getX()+
				","+vitesseCroisiere.getY()+")"+" cm/s.";
	}

	/**
	 * Méthode permettant de faire un déplacement d'un mur mobile sur la table.
	 * @param deplacement Le déplacement de l'obstacle effectué.
	 */
	//Aimé Melançon
	@Override
	public void setDeplacement(Vecteur3D deplacement) {
		this.setPosition(this.position.additionne(deplacement));
		centreXInit = position.getX();
		centreYInit = position.getY();
	}
	
	/**
	 * Méthode permettant de modifier l'ancienne position du mur mobile
	 * @param anciennePosition L'ancienne position du mur mobile
	 */
	//Félix Lefrançois
	public void setAnciennePosition(Vecteur3D anciennePosition) {
		this.anciennePosition = anciennePosition;
		centreXInit = anciennePosition.getX();
		centreYInit = anciennePosition.getY();
		creerLaGeometrie();
	}

	/**
	 * Méthode retournant l'aire total du mur mobile
	 * @return L'aire de l'obstacle
	 */
	//Félix Lefrançois
	@Override
	public Area getAire() {
		return aireTotal;
	}

}


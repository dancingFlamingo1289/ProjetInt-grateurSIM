package obstacles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;

import composantDeJeu.Balle;
import composantDeJeu.Table;
import gestionCollision.CollisionDesObjets;
import interfaces.Dessinable;
import interfaces.Selectionnable;
import math.vecteurs.Vecteur3D;
import outils.GererSon;
import outils.OutilsSon;
import scene.Scene;

/**
 * Classe créant un obstacle de type Mur
 * @author Félix Lefrançois
 * @author Aimé Melançon
 */
public class Mur extends BaseMur implements Selectionnable,Dessinable{
	/** Identifiant de classe **/
	private static final long serialVersionUID = 1L;
	/** Constante du nombre de points ajoutés lors d'une collision avec un mur**/
	private final int POINTS_AJOUTES = 200;
	/** OutilsSon de l'obstacle**/
	private transient OutilsSon sonObst=new OutilsSon();;
	/**Nom du fichier du son**/
	private final String NOM_DU_FICHIER="LesMurs.wav";
	/** Booléen confirmant si c'est la première fois que l'on crée la géométrie **/
	private boolean premiereFois = true;

	/**
	 * Constructeur de la classe
	 * @param position Vecteur position du centre du mur
	 * @param hauteur Hauteur en cm du mur
	 * @param largeur Largeur en cm du mur
	 * @param angle Angle de rotation en radian du mur
	 * @param couleur Couleur du mur
	 */
	//Félix Lefrançois
	public Mur(Vecteur3D position, double hauteur, double largeur, double angle, Color couleur) {
		super(position, hauteur, largeur, angle, couleur);
		creerLaGeometrie();
		
	}
	
	/**
	 * Méthode qui crée tous les composanta de dessin du mur
	 */
	//Félix Lefrançois
	public void creerLaGeometrie() {
		if (premiereFois) {
			sonObst = new OutilsSon() ;
			sonObst.chargerUnSonOuUneMusique(NOM_DU_FICHIER);
			premiereFois = false;
		}
		
		super.creerLaGeometrie();
	}
	
	/**
	 * Méthode pour dessiner un mur
	 * @param g2d Contexte graphique
	 */
	//Félix Lefrançois
	public void dessiner(Graphics2D g2d) {
		Graphics2D g2dPrive = (Graphics2D) g2d.create();
		super.dessiner(g2dPrive);
	}
	
	/**
	 * Méthode qui applique les changements physiques à un mur lors de l'animation
	 * @param deltaT La différence de temps simulé
	 */
	//Félix Lefrançois
	@Override
	public void avancerUnPas(Double deltaT) {}
	
	/**
	 * Méthode inutile pour le mur, car celle-ci sert à réinatialiser l'annimation.
	 */
	//Aimé Melançon
	@Override
	public void reinitialiser() {
	}
	
	/**
	 * Méthode vérifiant si il y a une collision entre un mur et une balle
	 * @param balle La balle testée
	 * @return Un booléen confirmant si il y a une collision
	 */
	//Félix Lefrançois
	public boolean intersection(Balle balle) {
		return super.intersection(balle);
	}
	
	/**
	 * Méthode testant si un obstacle intersecte avec le mur
	 * @param obst L'obstacle testé
	 * @return Un booléen confirmant si il y a une intersection
	 */
	//Félix Lefrançois
	@Override
	public boolean intersection(Obstacle obst) {
		Area copieAireMur = new Area(aireMur);
		Area copieAireObstacle = new Area(obst.getAire());
		
		copieAireMur.intersect(copieAireObstacle);
		return !copieAireMur.isEmpty();
	}

	/**
	 * Méthode pour appliquer les effets d'une collision avec le mur sur une balle
	 * @param balle La balle testée
	 * @exception Exception lorsqu'un vecteur est normalisé
	 */
	//Félix Lefrançois
	@Override
	public void collision(Balle balle) throws Exception {
		if (sonObst == null) {
			premiereFois = true ;
			creerLaGeometrie() ;
		}
		if(GererSon.isAllumerFermer()) {
			sonObst.jouerUnSon();
		}
		
		boolean collisionCoin = false;
		
		for (Cercle coin : lesCoins) {
			if(coin.intersection(balle)) {
				coin.collision(balle);
				Vecteur3D normale = CollisionDesObjets.trouverNormaleCollisionCercle(balle, coin.getPosition().getX(), coin.getPosition().getY());
				balle.setPosition(balle.getPosition().additionne(normale));
				collisionCoin = true;
			}
		}
		
		if(!collisionCoin) {
		    Vecteur3D normale = CollisionDesObjets.trouverNormaleCollisionMur(balle, Table.trouverMurLePlusProche(balle,lesMurs));
		    balle.setVitesse(CollisionDesObjets.vitesseFinaleImmobile(balle.getVitesse(),
				normale));
		    balle.setPosition(balle.getPosition().additionne(normale));
		    Scene.getPoints().ajouterPoints(POINTS_AJOUTES);
		}
	}
	
	/**
	 * Méthode pour vérifier si un point est contenu dans l'aire du mur
	 * @param xPix Coordonnée en x du point
	 * @param yPix Coordonnée en y du point
	 * @return Un booléen confirmant si le point est contenu dans l'aire ou non
	 */
	//Félix Lefrançois
	public boolean contient (double xPix, double yPix) {
		return super.contient(xPix, yPix);
	}

	/**
	 * Méthode résumant les caractéristiques d'un mur
	 * @return Une chaîne de caractères résumant les caractéristiques d'un mur
	 */
	//Félix Lefrançois
	public String toString() {
		return super.toString();
	}
	/**
	 * Méthode permettant de faire un déplacement d'un Mur sur la table.
	 * @param deplacement Le déplacement de l'obstacle effectué.
	 */
	//Aimé Melançon
	@Override
	public void setDeplacement(Vecteur3D deplacement) {
		this.setPosition(this.position.additionne(deplacement));
		
	}
	
	/**
	 * Méthode retournant l'aire du mur
	 * @return L'aire du mur
	 */
	//Félix Lefrançois
	@Override
	public Area getAire() {
		return aireMur;
	}

}

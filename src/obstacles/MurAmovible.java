package obstacles;

import java.awt.Color;
import java.awt.Graphics2D;
import composantDeJeu.Balle;
import composantDeJeu.Table;
import gestionCollision.CollisionDesObjets;
import interfaces.Dessinable;
import interfaces.Selectionnable;
import math.vecteurs.FlecheVectorielle;
import math.vecteurs.Vecteur3D;
import physique.MoteurPhysique;


/**
 * Classe héritant de BaseMur qui crée un mur amovible
 * @author Félix Lefrançois
 * @author Aimé Melançon
 */
public class MurAmovible extends BaseMur implements Selectionnable, Dessinable{
	/** Coefficient de sérialisation pour les fichiers. **/
	private static final long serialVersionUID = 1L ;
	/** Le vecteur de vitesse de croisiere du mur amovible**/
	private Vecteur3D vitesseCroisiere;
	/** Les coordonnées initiales du centre du mur lorsqu'il est créé**/
	private double centreXInit, centreYInit;
	/** La masse en kg du mur amovible**/
	private final double MASSE = 1000;
	/** La limite de mouvement d'un mur amovible par rapport à son centre**/
	private final double LIMITE_MOUVEMENT = 20;
	/** L'angle de rotation en radian du mur **/
	private double angle;

	/**
	 * Constructeur de la classe
	 * @param position Vecteur position du centre du mur amovible
	 * @param hauteur Hauteur en m du mur
	 * @param largeur Largeur en m du mur
	 * @param angle Angle de rotation en radian du mur
	 * @param vitesseCroisire Le module de la vitesse du mur en m/s
	 * @param couleur Couleur du mur
	 */
	//Félix Lefrançois
	public MurAmovible(Vecteur3D position, double hauteur, double largeur, double angle, double vitesseCroisiere, Color couleur) {	
		super(position, hauteur,largeur,angle,couleur);
		this.vitesseCroisiere  = new Vecteur3D(Math.cos(angle)*vitesseCroisiere,Math.sin(angle)*vitesseCroisiere);
		centreXInit = position.getX();
		centreYInit = position.getY();
		creerLaGeometrie();
	}
	
	/**
	 * Méthode qui crée tous les composanta de dessin du mur amovible
	 */
	//Félix Lefrançois
	public void creerLaGeomtrie() {
		super.creerLaGeometrie();
	}
	
	/**
	 * Méthode pour dessiner un mur amovible
	 * @param g2d Contexte graphique
	 */
	//Félix Lefrançois
	public void dessiner(Graphics2D g2d) {
		super.dessiner(g2d);
	}

	/**
	 * Méthode pour appliquer les effets d'une collision avec le mur amovible sur une balle
	 * @param balle La balle testée
	 * @exception Exception lorsqu'un vecteur est normalisé
	 */
	//Félix Lefrançois
	@Override
	public void collision(Balle balle) throws Exception {
		Vecteur3D normale = CollisionDesObjets.trouverNormaleCollisionMur(balle, Table.trouverMurLePlusProche(balle, lesMurs));
		balle.setVitesse(CollisionDesObjets.vitesseFinaleCollisionAvecMurAmovible(balle, normale, vitesseCroisiere,MASSE));
		balle.setPosition(balle.getPosition().additionne(normale));
	}
	
	/**
	 * Méthode pour vérifier si un point est contenu dans l'aire du mur amovible
	 * @param x Coordonnée en x du point
	 * @param y Coordonnée en y du point
	 * @return Un booléen confirmant si le point est contenu dans l'aire ou non
	 */
	//Félix Lefrançois
	public boolean contient (double xPix, double yPix) {
		return super.contient(xPix, yPix);
	}
	
	/**
	 * Méthode vérifiant si il y a une collision entre un mur amovible et une balle
	 * @param balle La balle testée
	 * @return Un booléen confirmant si il y a une collision
	 */
	//Félix Lefrançois
	public boolean intersection(Balle balle) {
		return super.intersection(balle);
		
	}

	/**
	 * Méthode qui applique les changements physiques à un mur amovible lors de l'animation
	 * @param deltaT La différence de temps simulé
	 */
	//Félix Lefrançois
	@Override
	public void avancerUnPas(Double deltaT) {
		position = MoteurPhysique.calculPosition(deltaT, position, vitesseCroisiere);
		if (position.getX() > centreXInit+Math.cos(angle)*LIMITE_MOUVEMENT || position.getX() < centreXInit- Math.cos(angle)*LIMITE_MOUVEMENT
				|| position.getY() < centreYInit-Math.sin(angle)*LIMITE_MOUVEMENT || position.getY() > centreYInit+Math.sin(angle)*LIMITE_MOUVEMENT) {
			vitesseCroisiere = vitesseCroisiere.multiplie(-1);
		}
		creerLaGeometrie();
	}
	
	/**
	 * Méthode résumant les caractéristiques d'un mur amovible
	 * @return Une chaîne de caractères résumant les caractéristiques d'un mur amovible
	 */
	//Félix Lefrançois
	public String toString() {
		return super.toString()+" Le mur a aussi une vitesse de croisiere de ("+vitesseCroisiere.getX()+
				","+vitesseCroisiere.getY()+")"+" m/s.";
	}

	/**
	 * Méthode permettant de faire un déplacement d'un Mur Amovible sur la table.
	 * @param deplacement Le déplacement de l'obstacle effectué.
	 */
	//Aimé Melançon
	@Override
	public void setDeplacement(Vecteur3D deplacement) {
		this.setPosition(this.position.additionne(deplacement));
		
	}

}

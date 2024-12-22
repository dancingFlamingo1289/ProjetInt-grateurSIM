package obstacles;

import java.awt.Color;
import java.awt.Graphics2D;
import composantDeJeu.Balle;
import composantDeJeu.Table;
import gestionCollision.CollisionDesObjets;
import interfaces.Dessinable;
import interfaces.Selectionnable;
import math.vecteurs.Vecteur3D;

/**
 * Classe créant un obstacle de type Mur
 * @author Félix Lefrançois
 * @author Aimé Melançon
 */
public class Mur extends BaseMur implements Selectionnable,Dessinable{
	/** Identifiant de classe **/
	private static final long serialVersionUID = 1L;

	/**
	 * Constructeur de la classe
	 * @param position Vecteur position du centre du mur
	 * @param hauteur Hauteur en m du mur
	 * @param largeur Largeur en m du mur
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
		super.creerLaGeometrie();
	}
	
	/**
	 * Méthode pour dessiner un mur
	 * @param g2d Contexte graphique
	 */
	//Félix Lefrançois
	public void dessiner(Graphics2D g2d) {
		super.dessiner(g2d);
	}

	/**
	 * Méthode pour appliquer les effets d'une collision avec le mur sur une balle
	 * @param balle La balle testée
	 * @exception Exception lorsqu'un vecteur est normalisé
	 */
	//Félix Lefrançois
	@Override
	public void collision(Balle balle) throws Exception {
		Vecteur3D normale = CollisionDesObjets.trouverNormaleCollisionMur(balle, Table.trouverMurLePlusProche(balle,lesMurs));
		balle.setVitesse(CollisionDesObjets.vitesseFinaleImmobile(balle.getVitesse(),
				normale));
		balle.setPosition(balle.getPosition().additionne(normale));
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
	 * Méthode pour vérifier si un point est contenu dans l'aire du mur
	 * @param x Coordonnée en x du point
	 * @param y Coordonnée en y du point
	 * @return Un booléen confirmant si le point est contenu dans l'aire ou non
	 */
	//Félix Lefrançois
	public boolean contient (double xPix, double yPix) {
		return super.contient(xPix, yPix);
	}

	/**
	 * Méthode qui applique les changements physiques à un mur lors de l'animation
	 * @param deltaT La différence de temps simulé
	 */
	//Félix Lefrançois
	@Override
	public void avancerUnPas(Double deltaT) {}
	
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

}

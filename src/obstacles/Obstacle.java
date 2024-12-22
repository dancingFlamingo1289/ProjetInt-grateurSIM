package obstacles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.io.Serializable;

import composantDeJeu.Balle;
import interfaces.Dessinable;
import interfaces.Selectionnable;
import math.vecteurs.Vecteur3D;

/**Classe mère contenant tout les obstacles et les attributs de chaque obstacle.
 * Obstacle implémente Dessinable et Selectionnable.
 * 
 * @author Aimé Melançon
 */
public abstract class Obstacle implements Dessinable, Selectionnable, Serializable {
	/** Coefficient de sérialisation pour les fichiers. **/
	private static final long serialVersionUID = 1L ;
	/**La position d'un obstacle X. **/
	protected  Vecteur3D position;
	/**La couleur de l'obstacle X. **/
	protected Color couleur;
	/** L'ancienne position d'un obstacle X.*/
	protected Vecteur3D anciennePosition;
	
	/**
	 * Constructeur de la classe abstraite Obstacle permettant de créé un obstacle.
	 * @param position La position d'un obstacle sur la table
	 * @param couleur La couleur d'un obstacle sur la table
	 */
	//Aimé Melançon
	public Obstacle(Vecteur3D position, Color couleur) {
		this.position = position;
		this.couleur =couleur;
	}

	/**
	 * Permet de dessiner un Obstacle, sur le contexte graphique passe en paramètre.
	 * @param g2d contexte graphique.
	 */
	//Aimé Melançon
	public abstract void dessiner(Graphics2D g2d) ;

	/**
	 * Retourne vrai si le point passé en paramètre fait partie de l'Obstacle dessinable
	 * sur lequel cette methode sera appelée
	 * 
	 * @param x Coordonnée en x du point (exprimé en mètres) 
	 * @param y Coordonnée en y du point (exprimé en mètres)
	 */
	//Aimé Melançon
	public abstract boolean contient(double x, double y);

	/**
	 * Méthode permettant de connaître la collision entre l'obstacle et la balle.
	 * @param balle la balle courante
	 * @return retourne le vecteur de la collision.
	 * @throws Exception L'exception quand un vecteur est normalisé (Pour le trou noir par exemple)
	 */
	//Aimé Melançon
	public abstract void collision(Balle balle) throws Exception ;

	/**
	 * Méthode permettant de savoir s'il y a une intersection avec l'obstacle et la balle.
	 * 
	 * @param balle La balle courante.  
	 * @return Retourne un tableau d'intersection.
	 */
	//Aimé Melançon
	public abstract boolean intersection(Balle balle) ;

	/**
	 * Méthode permettant de connaître les caractéristiques d'un obstacle.
	 */
	//Aimé Melançon
	public abstract String toString();

	/**
	 * Méthode permettant d'avoir la position d'un obstacle.
	 * @return retourne la position de l'obstacle.
	 */
	//Aimé Melançon
	public  Vecteur3D getPosition() {
		return this.position;	
	}

	/**
	 * Méthode permettant de placer dans un autre endroit l'obstacle.
	 * @param position La nouvelle position de l'obstacle.
	 */
	//Aimé Melançon
	public void setPosition(Vecteur3D position) {
		//setAnciennePosition(this.position);
		this.position = position;
		creerLaGeometrie();
	}
	/**
	 * Méthode permettant d'établir où était un obstacle avant un changement de position.
	 * @param anciennePosition L'ancienne position
	 */
	//Aimé Melançon
	public void setAnciennePosition(Vecteur3D anciennePosition) {
		this.anciennePosition= anciennePosition;
	}
	/**
	 * Méthode permettant d'avoir l'ancienne position d'un obstacle X.
	 * @return Retourne l'ancienne position de l'obstacle X.
	 */
	//Aimé Melançon
	public Vecteur3D getAnciennePosition() {
		return this.anciennePosition;
	}

	/**
	 * Méthode publique pour créer les formes qui composent les obstacles
	 * Cette méthode doit être appelée de nouveau chaque fois que sa position ou dimension est modifiée
	 */
	//Aimé Melançon
	protected abstract void creerLaGeometrie();

	/**Méthode publique permettant d'avoir la couleur de l'obstacle
	 * @return La couleur actuel de l'obstacle.
	 */
	//Aimé Melançon
	public Color getCouleur() {
		return couleur;
	}
	/**Méthode permettant de changer la couleur de l'obstacle;
	 * @param couleur La nouvelle couleur de l'obstacle
	 */
	//Aimé Melançon
	public void setCouleur(Color couleur) {
		this.couleur = couleur;
	}
	/** Méthode permettant d'aller seulement d'une image à la fois dans une animation d'un obstacle.
	 * @param deltaT La différence entre le temps simulé
	 */
	//Aimé Melançon
	public abstract void avancerUnPas(Double deltaT);

	/**
	 * Méthode permettant de faire un déplacement d'un obstacle sur la table.
	 * @param deplacement Le déplacement de l'obstacle effectué.
	 */
	//Aimé Melançon
	public abstract void setDeplacement(Vecteur3D deplacement);
	/*e.i.
	 * obstacle.setPosition(obstacle.getPosition()+deplacement);
	 */
	
	/**
	 * Méthode permettant de réinitialiser les animations des obstacles.
	 */
	//Aimé Melançon
	public abstract void reinitialiser();

	/**
	 * Méthode servant de savoir s'il y a une intersection entre deux obstacles.
	 * @param obst L'obstacle à testé s'il y a ou non une intersection
	 * @return Vrai s'il y a une intersection et faux s'il n'y en a pas.
	 */
	//Aimé Melançon
	public abstract boolean intersection(Obstacle obst);
	
	/**
	 * Méthode permettant d'avoir l'aire d'un obstacle.
	 * @return retourne l'aire de l'obstacle
	 */
	//Aimé Melançon
	public abstract Area getAire();
}

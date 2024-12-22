package gestionCollision;

import java.awt.geom.Line2D;
import composantDeJeu.Balle;
import math.vecteurs.Vecteur3D;

/**
 * Classe qui s'occupe de calculer le résultat des collisions entre les différents objets
 * @author Félix Lefrançois
 */
public class CollisionDesObjets {
	/** Le coefficient de restitution dans une collision**/
	private static final double RESTITUTION = 0.8;
	
	/**
	 * Méthode permettant de calculer l'impulsion lors d'une collision entre deux
	 * objets en mouvement
	 * @return l'impulsion appliquée selon la direction de la force normale (en Ns)
	 */
	//Félix Lefrançois
	public static double impulsionCollisionMouvement(double masseA, double masseB, Vecteur3D vitesseA, Vecteur3D vitesseB, Vecteur3D normaleBA) {
		return (-(1+RESTITUTION))/(1/masseA + 1/masseB)*normaleBA.prodScalaire(vitesseA.soustrait(vitesseB));
	} 
	
	/**
	 * Méthode permettant de calculer la vitesse résulante dans une collision entre une balle et un objet circulaire immobile
	 * @param balleA La balle qui entre en collision avec un objet circulaire immobile
	 * @param masseObjetImmo La masse de l'objet immobile
	 * @param posX La position en x du centre de l'objet immobile circulaire
	 * @param posY La position en y du centre de l'objet immobile circulaire
	 * @return Le vecteur vitesse résultant de la collision
	 */
	//Félix Lefrançois
	public static Vecteur3D vitesseFinaleMouvementCercleImmobile(Balle balleA, double masseObjetImmo, double posX, double posY) {
		double impulsion = impulsionCollisionMouvement(balleA.getMasse(), masseObjetImmo,balleA.getVitesse(),
				new Vecteur3D(0,0,0),trouverNormaleCollisionCercle(balleA,posX,posY));
		Vecteur3D normaleBA = trouverNormaleCollisionCercle(balleA,posX,posY);
		
		return (Vecteur3D.additionne(balleA.getVitesse(), normaleBA.multiplie(impulsion/balleA.getMasse())));
	}
	
	/**
	 * Méthode permettant de calculer le vecteur vitesse résultant d'une collision entre deux balles
	 * @param balleA La balle dont la vitesse est calculée
	 * @param balleB La balle qui entre en collision avec la balle dont la vitesse est calculée
	 * @return Le vecteur vitesse résultant d'une balleA lors d'une collision entre deux balles
	 */
	//Félix Lefrançois
	public static Vecteur3D vitesseFinaleMouvementCercleMobile(Balle balleA, Balle balleB) {
		double impulsion = impulsionCollisionMouvement(balleA.getMasse(), balleB.getMasse(),balleA.getVitesse(),
				balleB.getVitesse(),trouverNormaleCollisionCercle(balleA,balleB.obtenirCentreX(),balleB.obtenirCentreY()));
		Vecteur3D normaleBA = trouverNormaleCollisionCercle(balleB,balleA.obtenirCentreX(),balleA.obtenirCentreY());
		return (Vecteur3D.additionne(balleA.getVitesse(), normaleBA.multiplie(impulsion/balleA.getMasse())));
	}
	
	/**
	 * Méthode permettant de déterminer la vitesse finale lors d'une collision entre une balle et un mur amovible (ou un flipper)
	 * @param balle La balle qui entre en collision
	 * @param normale La force normale perpendiculaire à la surface
	 * @param vitesseMur La vitesse du mur (ou du flipper par rapport au rayon et à la vitesse angulaire)
	 * @param masse La masse du mur (ou du flipper)
	 * @return La vitesse résultante de la balle
	 */
	//Félix Lefrançois
	public static Vecteur3D vitesseFinaleCollisionAvecMurAmovible(Balle balle, Vecteur3D normale, Vecteur3D vitesseMur,double masse) {
		double impulsion = impulsionCollisionMouvement(balle.getMasse(),masse, balle.getVitesse(),vitesseMur,normale);
		
		return (Vecteur3D.additionne(balle.getVitesse(), normale.multiplie(impulsion/balle.getMasse())));
	}
	
	/**
	 * Méthode permettant de calculer la vitesse finale d'un objet lors d'une collision entre un
	 * objet en mouvement et un objet immobile (un mur,etc.)
	 * @param vitesseBalle La vitesse de la balle
	 * @param normaleSurface La normale perpendiculaire à la surface du mur et orientée vers la balle
	 * @return vecteur vitesse finale de l'objet en mouvement
	 */
	//Félix Lefrançois
	public static Vecteur3D vitesseFinaleImmobile(Vecteur3D vitesseBalle, Vecteur3D normaleSurface) {
		return Vecteur3D.multiplie(Vecteur3D.soustrait(vitesseBalle,Vecteur3D.multiplie(Vecteur3D.multiplie
				(normaleSurface,(Vecteur3D.prodScalaire(vitesseBalle, normaleSurface))),2)), RESTITUTION);
	}
	
	/**
	 * Méthode permettant de trouver la normale à la surface d'un mur
	 * @param balleCollision La balle qui entre en collision avec le mur
	 * @param murCollision Le mur de la collision
	 * @return Retourne le Vecteur3D de la normale à la surface du mur
	 * @throws Exception Si le vecteur ne peut être normalisée puisqu'il est de longueur trop petite ou nulle
	 */
	//Félix Lefrançois
	public static Vecteur3D trouverNormaleCollisionMur(Balle balleCollision, Line2D.Double murCollision) throws Exception {
		double deltaXBalle = (balleCollision.obtenirCentreX())  - murCollision.getX1();
		double deltaYBalle  = (balleCollision.obtenirCentreY()) - murCollision.getY1();
		Vecteur3D vecteurCointMurACentreBalle = new Vecteur3D(deltaXBalle, deltaYBalle);
		
		double deltaXMur = murCollision.getX1() - murCollision.getX2();
		double deltaYMur = murCollision.getY1() - murCollision.getY2();
		Vecteur3D vecteurDirecteur = new Vecteur3D(deltaXMur,deltaYMur);
		
		Vecteur3D vecteurprojection = Vecteur3D.projectionOrthogonale(vecteurCointMurACentreBalle, vecteurDirecteur);
		Vecteur3D vecteurNormale = Vecteur3D.soustrait(vecteurCointMurACentreBalle, vecteurprojection);
		if (vecteurNormale.module() == 0) vecteurNormale = vecteurDirecteur;
		vecteurNormale = Vecteur3D.normalise(vecteurNormale);
		return vecteurNormale;
	}
	
	/**
	 * Méthode calculant le vecteur normale dans une collision entre une balle et un objet circulaire
	 * @param balleCollision La balle qui entre en collision avec un objet circulaire (autre balle ou base d'un flipper)
	 * @param positionX La position en x du centre de la surface sur laquelle la balle se frappe
	 * @param positionY La position en y du centre de la surface sur laquelle la balle se frappe
	 * @return La normale perpendiculaire à la surface de la balle qui se dirige vers le centre d'un autre cercle
	 */
	//Félix Lefrançois
	public static Vecteur3D trouverNormaleCollisionCercle(Balle balleCollision, double positionX, double positionY) {
		double deltaX = (balleCollision.obtenirCentreX()) - positionX;
		double deltaY = (balleCollision.obtenirCentreY()) - positionY;
		Vecteur3D normale = new Vecteur3D(deltaX,deltaY,0);
		try {
			normale = normale.normalise();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return normale;
	}
}

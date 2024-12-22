package obstacles.polygone;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

import composantDeJeu.Balle;
import interfaces.Dessinable;
import interfaces.Selectionnable;
import math.vecteurs.Vecteur3D;
import outils.GererSon;
import outils.OutilsSon;

/**
 * Cette classe représente un Trait qui sera Dessinable et Séléctionnable.
 * @author Elias Kassas
 */
public class Trait implements Dessinable, Selectionnable, Serializable {
	/** Coefficient de sérialisation pour les fichiers. **/
	private static final long serialVersionUID = 1L ;
	/** Coordonnées du point P1, point de début du trait. **/
	private double x1, y1 ;
	/** Coordonnées du point P2, point de fin du trait. **/
	private double x2, y2 ;
	/** Coordonnées du point PC,point central du trait. **/
	private double centreX, centreY ;
	/** Longueur d'un trait. **/
	private double longueurTrait ;
	/** Objet représentant le trait **/
	private Line2D.Double trait ;
	/** Couleur du trait. **/
	private Color couleur ;
	/** Vecteur directeur du trait. **/
	private Vecteur3D vecteurDirecteur ;
	/** OutilsSon de l'obstacle**/
	private  transient OutilsSon sonObst = new OutilsSon();
	/**Nom du fichier du son**/
	private final String NOM_DU_FICHIER="LesMurs.wav";
	/**Seulement exécuté la première fois que l'application est lancé **/
	private boolean premiereFois=true;

	/**
	 * Constructeur d'un trait.
	 * @param x1 : Coordonnée en X du point de départ.
	 * @param y1 : Coordonnée en Y du point de départ.
	 * @param x2 : Coordonnée en X du point de fin.
	 * @param y2 : Coordonnée en Y du point de fin.
	 */
	// Par Elias Kassas
	public Trait(double x1, double y1, double x2, double y2, Color couleur) {
		this.x1 = x1 ;
		this.y1 = y1 ;
		this.x2 = x2 ;
		this.y2 = y2 ;
		this.couleur = couleur ;

		centreX = (this.x1 + this.x2)/2 ;
		centreY = (this.y1 + this.y2)/2 ;

		this.longueurTrait = Math.sqrt(Math.pow(this.x1 - this.x2, 2) + Math.pow(this.y1 - this.y2, 2)) ;
		try {
			vecteurDirecteur = new Vecteur3D(this.x2 - this.x1, this.y2 - y1).normalise() ;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		creerLaGeometrie() ;
	}

	/**
	 * Constructeur créant un trait à partir d'une coordonnée (x1, y1) et de la longueur du trait.
	 * 
	 * ATTENTION : Il faut mettre z = 0 pour le vecteur de longueur du trait comme nous travaillons 
	 * en 2D.
	 * @param centreX : Coordonnée en X du point de départ.
	 * @param centreY : Coordonnée en Y du point de départ.
	 * @param longueurTrait : Longueur du trait (vectorielle pour simplifier le traitement, 
	 * en 2D seulement)
	 */
	// Par Elias Kassas
	public Trait(double centreX, double centreY, Vecteur3D longueurTrait) {
		this.longueurTrait = longueurTrait.module() ;
		this.centreX = centreX ;
		this.centreY = centreY ;

		this.x1 = this.centreX - longueurTrait.getX()/2 ;
		this.y1 = this.centreY - longueurTrait.getY()/2 ;
		this.x2 = this.centreX + longueurTrait.getX()/2 ;
		this.y2 = this.centreY + longueurTrait.getY()/2 ;

		try {
			vecteurDirecteur = new Vecteur3D(this.x2 - this.x1, this.y2 - y1).normalise() ;
		} catch (Exception e) {
			e.printStackTrace();
		}

		creerLaGeometrie() ;
	}

	/**
	 * Méthode servant à créer la géométrie du trait.
	 */
	// Par Elias Kassas
	private void creerLaGeometrie() {
		if(premiereFois) {
			sonObst.chargerUnSonOuUneMusique(NOM_DU_FICHIER);
			premiereFois=false;
		}

		trait = new Line2D.Double(x1, y1, x2, y2) ;
	}

	/**
	 * Méthode servant à dessiner le trait.
	 * @param g2d : Le contexte graphique.
	 */
	// Par Elias Kassas
	@Override
	public void dessiner(Graphics2D g2d) {
		Graphics2D g2dPrive = (Graphics2D) g2d.create() ;
		g2dPrive.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON) ;
		g2dPrive.setColor(couleur) ;
		g2dPrive.setStroke(new BasicStroke(2)) ;
		g2dPrive.draw(trait) ;
	}

	/**
	 * Méthode servant à vérifier si le trait contient le clic.
	 */
	// Par Elias Kassas
	@Override
	public boolean contient(double xPix, double yPix) {
		return trait.ptSegDist(new Point2D.Double(xPix, yPix)) > 0 ;
	}

	/**
	 * Méthode servant à obtenir ble segment Line2D du trait.
	 * @return Le segment Line2D du trait.
	 */
	// Par Elias Kassas
	public Line2D.Double getTrait() {
		return trait ;
	}

	/**
	 * Méthode servant à voir l'actuelle valeur de X1.
	 * @return L'actuelle valeur de X1.
	 */
	// Par Elias Kassas
	public double getX1() {
		return x1;
	}

	/**
	 * Méthode servant à modifier la veleur de X1.
	 * @param x1 : La nouvelle valeur de X1.
	 */
	// Par Elias Kassas
	public void setX1(double x1) {
		this.x1 = x1;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode servant à voir l'actuelle valeur de Y1.
	 * @return L'actuelle valeur de Y1.
	 */
	// Par Elias Kassas
	public double getY1() {
		return y1;
	}

	/**
	 * Méthode servant à modifier la veleur de Y1.
	 * @param y1 : La nouvelle valeur de Y1.
	 */
	// Par Elias Kassas
	public void setY1(double y1) {
		this.y1 = y1;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode servant à voir l'actuelle valeur de X2.
	 * @return L'actuelle valeur de X2.
	 */
	// Par Elias Kassas
	public double getX2() {
		return x2;
	}

	/**
	 * Méthode servant à modifier la veleur de X2.
	 * @param x2 : La nouvelle valeur de X2.
	 */
	// Par Elias Kassas
	public void setX2(double x2) {
		this.x2 = x2;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode servant à voir l'actuelle valeur de Y2.
	 * @return L'actuelle valeur de Y2.
	 */
	// Par Elias Kassas
	public double getY2() {
		return y2;
	}

	/**
	 * Méthode servant à modifier la veleur de Y2.
	 * @param y2 : La nouvelle valeur de Y2.
	 */
	// Par Elias Kassas
	public void setY2(double y2) {
		this.y2 = y2;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode servant à voir l'actuelle couleur du trait.
	 * @return L'actuelle couleur du trait.
	 */
	// Par Elias Kassas
	public Color getCouleur() {
		return couleur;
	}

	/**
	 * Méthode servant à modifier la couleur du trait.
	 * @param couleur : La nouvelle couleur du trait.
	 */
	// Par Elias Kassas
	public void setCouleur(Color couleur) {
		this.couleur = couleur;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode permettant la gestion des collisions avec un trait.
	 * @param balle : La balle pour la collisions
	 * @throws Exception Si la normalisation est impossible.
	 */
	// Par Elias Kassas
	public void collision(Balle balle) throws Exception {
		if(premiereFois) {
			
			if(GererSon.isAllumerFermer()) {
				sonObst.jouerUnSon();
				}
			premiereFois=false;
		}
		
		Vecteur3D normale = new Vecteur3D(-vecteurDirecteur.getY(), vecteurDirecteur.getX()).normalise();

		Vecteur3D vitesseReflechie = balle.getVitesse().soustrait(normale.
				multiplie(2*balle.getVitesse().prodScalaire(normale))) ;

		balle.setVitesse(vitesseReflechie) ;
	}

	/**
	 * Méthode servant à vérifier s'il y a une intersection entre une balle et le polygone.
	 * Elle sera complétée à la seconde mêlée.
	 * @param balle : La balle à vérifier.
	 * @return S'il y a une intersection ou non.
	 */
	//Par Elias Kassas
	public boolean intersection(Balle balle) {
		// Vérifier si le centre de la balle est à une distance suffisamment proche du segment de trait
		// Calculer la distance entre le centre de la balle et le segment de trait
		double distanceTraitBalle = trait.ptSegDist(balle.obtenirCentreX(), balle.obtenirCentreY()) ;

		return distanceTraitBalle <= Balle.getDiametre() / 2 ;
	}

	/**
	 * Méthode permettant d'avoir toutes les propriétés d'un trait sous forme de chaine de caractères.
	 */
	// Par Elias Kassas
	@Override
	public String toString() {
		return "Trait [x1=" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2=" + y2 + ", centreX=" + centreX + ", centreY="
				+ centreY + ", longueurTrait=" + longueurTrait + ", couleur=" + couleur + "]";
	}
}

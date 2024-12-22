package obstacles.polygone ;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.io.Serializable;
import java.util.ArrayList;

import composantDeJeu.Balle;
import interfaces.Dessinable;
import interfaces.Selectionnable;
import math.vecteurs.Vecteur3D;
import obstacles.Cercle;
import obstacles.Obstacle;

/**
 * Cette classe représente un Polygone.
 * @author Elias Kassas
 */
public class Polygone extends Obstacle implements Dessinable, Selectionnable, Serializable {
	/** Coefficient de sérialisation pour les fichiers. **/
	private static final long serialVersionUID = 1L ;
	/** Longueur de l'apothème (distance centrePolygone - centreCôté) du polygone. **/
	private double longueurApotheme ;
	/** Le nombre de côtés du polygone. **/
	private int nbCotes ;
	/** La mesure d'un côté du polygone. **/
	private double mesureCote ;
	/** L'objet de type Polygon représentant le polygone (fill). **/
	private Polygon polygone ;
	/** L'angle de rotation successif entre les centre des côtés du polygone (en radians). **/
	private double angleRotation ;
	/** Liste des points du polygone à dessiner. **/
	private ArrayList<Double> pointsADessinerX, pointsADessinerY ;
	/** Liste des points sous forme de tableau du polygone de type Polygon à dessiner. **/
	private int[] ptsADessinerX, ptsADessinerY ;
	/** Liste des traits du polygone à dessiner. **/
	private ArrayList<Trait> lesTraits ;
	/** Liste des cercles qui seront sur les côtés du polygone pour gèrer la collision avec un coin. **/
	private ArrayList<Cercle> lesCercles ;

	/**
	 * Constructeur d'un Polygone.
	 * @param x : Coordonnée en X du centre du polygone.
	 * @param y : Coordonnée en Y du centre du polygone.
	 * @param nbCotes : Nombre de côté du polygone.
	 * @param mesureCote : Mesure d'un côté du polygone.
	 * @param couleur : Couleur de l'intérieur 
	 */
	// Par Elias Kassas
	public Polygone(Vecteur3D position, int nbCotes, double mesureCote, Color couleur) {
		super(position, couleur) ;
		this.nbCotes = nbCotes ;
		this.mesureCote = mesureCote ;
		creerLaGeometrie() ;
		//
	}

	/**
	 * Méthode permettant de créer la géometrie d'un polygone régulier.
	 */
	// Par Elias Kassas
	protected void creerLaGeometrie() {
		lesTraits = new ArrayList<Trait>() ;
		lesCercles = new ArrayList<Cercle>() ;
		
		this.longueurApotheme = mesureCote / (2 * Math.tan(Math.PI / nbCotes));
		angleRotation = 2*Math.PI/nbCotes ;
		pointsADessinerX = new ArrayList<Double>() ;
		pointsADessinerY = new ArrayList<Double>() ;

		double sommeAngles = 0 ;

		for (int i = 0 ; i <= nbCotes ; i++) {
			pointsADessinerX.add(this.position.getX() + longueurApotheme * Math.cos(sommeAngles)) ;
			pointsADessinerY.add(this.position.getY() + longueurApotheme * Math.sin(sommeAngles)) ;
			
			lesCercles.add(new Cercle(new Vecteur3D(pointsADessinerX.get(i), pointsADessinerY.get(i)), 
					5, Color.white)) ;
			
			sommeAngles += angleRotation ;
		}

		for (int n = 0 ; n < pointsADessinerX.size() - 1 && n < pointsADessinerY.size() - 1 ; n++) {
			Trait segment = new Trait(pointsADessinerX.get(n), pointsADessinerY.get(n), 
					pointsADessinerX.get(n + 1), pointsADessinerY.get(n + 1), Color.black) ;
			lesTraits.add(segment) ;
		}

		convertirArraysEnTableaux() ;
		polygone = new Polygon(ptsADessinerX, ptsADessinerY, nbCotes) ;
	}

	/**
	 * Méthode permettant de dessiner le polygone.
	 * @param g2d : Le contexte graphique.
	 */
	// Par Elias Kassas
	@Override
	public void dessiner(Graphics2D g2d) {
		Graphics2D g2dPrive = (Graphics2D) g2d.create() ;
		g2dPrive.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON) ; 
		g2dPrive.setColor(couleur) ;
		
		for (Trait t : lesTraits) {
			t.dessiner(g2dPrive) ;
		}
		
		g2dPrive.setColor(couleur) ;
		g2dPrive.fill(polygone) ;
	}

	/**
	 * Méthode privée permettant de convertir un array en tableau.
	 */
	// Par Elias Kassas
	private void convertirArraysEnTableaux() {
		ptsADessinerX = new int[pointsADessinerX.size()] ;
		ptsADessinerY = new int[pointsADessinerX.size()] ;

		for (int i = 0 ; i < pointsADessinerX.size() && i < pointsADessinerY.size() ; i++) {
			ptsADessinerX[i] = (int) Math.round(pointsADessinerX.get(i)) ;
			ptsADessinerY[i] = (int) Math.round(pointsADessinerY.get(i)) ;
		}
	}

	/**
	 * Méthode permettant de vérifier si le clic de l'utilisateur est fait dans le polygone.
	 * @param xPix : Coordonnée en X du clic.
	 * @param yPix : Coordonnée en Y du clic.
	 */
	// Par Elias Kassas
	@Override
	public boolean contient(double xPix, double yPix) {
		return polygone.contains(xPix, yPix) ;
	}

	/**
	 * Méthode qui retourne le centre actuel en X du polygone.
	 * @return Le centre en X du polygone.
	 */
	// Par Elias Kassas
	public double getXCentre() {
		return this.position.getX() ;
	}

	/**
	 * Méthode servant à modifier la coordonnée en X du centre du polygone.
	 * @param x : La nouvelle coordonnée en X du centre du polygone.
	 */
	// Par Elias Kassas
	public void setX(double x) {
		this.position.setX(x) ;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode qui retourne le centre actuel en Y du polygone.
	 * @return Le centre en Y du polygone.
	 */
	// Par Elias Kassas
	public double getY() {
		return this.position.getY() ;
	}

	/**
	 * Méthode servant à modifier la coordonnée en Y du centre du polygone.
	 * @param y : La nouvelle coordonnée en Y du centre du polygone.
	 */
	// Par Elias Kassas
	public void setY(double y) {
		this.position.setY(y) ;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode servant à modifier les coordonnées en X et en Y du centre du polygone.
	 * @param x : La nouvelle coordonnée en X du centre du polygone.
	 * @param y : La nouvelle coordonnée en Y du centre du polygone.
	 */
	// Par Elias Kassas
	public void setXY(double x, double y) {
		setX(x) ;
		setY(y) ;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode servant à retourner la longueur de l'apothème du polygone.
	 * @return Longueur de l'apothème du polygone.
	 */
	// Par Elias Kassas
	public double getLongueurApotheme() {
		return longueurApotheme ;
	}

	/**
	 * Méthode servant à retourner le nombre de côtés du polygone.
	 * @return Nombre de côtés du polygone.
	 */
	// Par Elias Kassas
	public int getNbCotes() {
		return nbCotes;
	}

	/**
	 * Méthode servant à modifier le nombre de côtés du polygone.
	 * @param nbCotes : Nouveau nombre de côtés du polygone.
	 */
	// Par Elias Kassas
	public void setNbCotes(int nbCotes) {
		this.nbCotes = nbCotes ;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode servant à retourner la longueur d'un côté du polygone.
	 * @return Longueur d'un côté du polygone.
	 */
	// Par Elias Kassas
	public double getMesureCote() {
		return mesureCote ;
	}

	/**
	 * Méthode servant à retourner la longueur d'un côté du polygone.
	 * @param mesureCote : Nouvelle longueur d'un côté du polygone.
	 */
	// Par Elias Kassas
	public void setMesureCote(double mesureCote) {
		this.mesureCote = mesureCote ;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode servant à retourner la couleur actuelle de l'intérieur du polygone.
	 * @return La couleur actuelle de l'intérieur du polygone.
	 */
	// Par Elias Kassas
	public Color getCouleur() {
		return couleur ;
	}

	/**
	 * Méthode servant à modifier la couleur de l'intérieur du polygone.
	 * @param couleur : Nouvelle couleur de l'intérieur du polygone.
	 */
	// Par Elias Kassas
	public void setCouleur(Color couleur) {
		this.couleur = couleur ;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode permettant d'afficher la liste des points du polygone sous forme de chaîne.
	 * @return Une chaîne de caractères contenant la liste des points du polygone.
	 */
	// Par Elias Kassas
	public String toString() {
		return toString(3) ;
	}

	/**
	 * Méthode permettant d'afficher la liste des points du polygone sous forme de chaîne.
	 * @param nbDecimales : Le nombre de décimales de précision voulues.
	 * @return Une chaîne de caractères contenant la liste des points du polygone.
	 */
	// Par Elias Kassas
	public String toString(int nbDecimales) {
		String s = "Polygone : (x,y) {" ;

		for (int i = 0 ; i < pointsADessinerX.size() ; i++) {
			s += "(" + String.format("%." + nbDecimales + "f", pointsADessinerX.get(i)) + ", " +
					String.format("%."+nbDecimales + "f", pointsADessinerY.get(i)) + ")    " ;
		}

		s += "}" ;
		return s ;
	}

	/**
	 * Méthode permettant la gestion des collisions avec un polygone.
	 * @param balle : La balle pour la collisions
	 * @throws Exception Si la normalisation est impossible.
	 */
	// Par Elias Kassas
	@Override
	public void collision(Balle balle) throws Exception {
		for (Trait t : lesTraits) {
			if (t.intersection(balle)) {
				t.collision(balle) ;
			}
		}
		
		for (Cercle c : lesCercles) {
			if (c.intersection(balle)) {
				c.collision(balle) ;
			}
		}
	}

	/**
	 * Méthode servant à vérifier s'il y a une intersection entre une balle et le polygone.
	 * Elle sera complétée à la seconde mêlée.
	 * @param balle : La balle à vérifier.
	 * @return S'il y a une intersection ou non.
	 */
	//Par Elias Kassas
	@Override
	public boolean intersection(Balle balle) {
		for (Trait t : lesTraits) {
			if (t.intersection(balle)) {
				return true ;
			}
		}
		
		for (Cercle c : lesCercles) {
			if (c.intersection(balle)) {
				return true ;
			}
		}
		
		return false ;
	}

	@Override
	public void avancerUnPas(Double deltaT) {
	}
	/**
	 * Méthode permettant de faire un déplacement d'un polygone sur la table.
	 * @param deplacement Le déplacement de l'obstacle effectué.
	 */
	//Aimé Melançon
	@Override
	public void setDeplacement(Vecteur3D deplacement) {
		this.setPosition(this.position.additionne(deplacement));
		
	}
}

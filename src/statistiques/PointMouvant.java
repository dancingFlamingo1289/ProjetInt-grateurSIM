package statistiques;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

import interfaces.Dessinable;

/**
 * Cette classe représente un point mouvant associé à un objet Graphique. Le présent objet sera 
 * dessiné dans la zone de dessin du graphique pour donner un couple précis de valeurs choisi par 
 * l'utilisateur.
 * @author Elias Kassas
 */
public class PointMouvant implements Dessinable {
	/** Coordonées du centre du cercle représentant le point. **/
	private double xCentre, yCentre ;
	private double xProgramme, yProgramme ;
	/** Coordonnées du point supérieur gauche de la boite englobante du point réel. **/
	private double xReel, yReel ;
	/** Rayon du point en pixels. **/
	private final double RAYON = 5 ;
	/** Cercle symbolisant le point. **/
	private Ellipse2D.Double point ;
	/** Ratios d'affichage à appliquer sur le point. **/
	private double ratioAffichageX = 1, ratioAffichageY = 1 ;
	private AffineTransform transfo ;

	/**
	 * Constructeur du point mouvant.
	 * @param x : La coordonnée en X du centre du point mouvant.
	 * @param y : La coordonnée en Y du centre du point mouvant.
	 */
	// Par Elias Kassas
	public PointMouvant(double x, double y) {
		this.xCentre = x ;
		this.yCentre = y ;
		creerLaGeometrie() ;
	}
	
	/**
	 * Constructeur d'un point mouvant avec la possibilité d'ajouter les ratios d'affichages.
	 * @param x : La coordonnée en X du centre du point mouvant.
	 * @param y : La coordonnée en Y du centre du point mouvant.
	 * @param ratioX : ratio d'affichage en X.
	 * @param ratioY : ratio d'affichage en Y.
	 */
	// Par Elias Kassas
	public PointMouvant(double x, double y, double ratioX, double ratioY, AffineTransform transfo) {
		this.xCentre = x ;
		this.yCentre = y ;
		this.ratioAffichageX = ratioX ;
		this.ratioAffichageY = ratioY ;
		this.transfo = transfo ;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode servant à construire la géometrie d'un point mouvant.
	 */
	// Par Elias Kassas
	private void creerLaGeometrie() {
		this.xProgramme = xCentre - RAYON ;
		this.yProgramme = yCentre - RAYON ;
		this.xReel = this.xProgramme * ratioAffichageX ;
		this.yReel = this.yProgramme * ratioAffichageY ;
		point = new Ellipse2D.Double(xReel, yReel, 2*RAYON, 2*RAYON) ;
	}

	/**
	 * Méthode servant à dessiner le point mouvant.
	 * @param g2d : Le contexte graphique.
	 */
	// Par Elias Kassas
	@Override
	public void dessiner(Graphics2D g2d) {
		Graphics2D g2dPrive = (Graphics2D) g2d.create() ;
		
		g2dPrive.setColor(Color.black) ;
		g2dPrive.draw(transfo.createTransformedShape(point)) ;
		g2dPrive.setColor(Color.pink) ;
		g2dPrive.fill(transfo.createTransformedShape(point)) ;
		g2dPrive.setColor(Color.orange) ;
		g2dPrive.drawString(toString(), (float) (xCentre + RAYON), (float) (yCentre + RAYON)) ;
	}

	/**
	 * Méthode permettant de modifier la coordonnée en X du point.
	 * @param x : La nouvelle coordonnée X
	 */
	// Par Elias Kassas
	public void setX(double x) {
		this.xCentre = x ;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode permettant de modifier la coordonnée en Y du point.
	 * @param y : La nouvelle coordonnée Y
	 */
	// Par Elias Kassas
	public void setY(double y) {
		this.yCentre = y ;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode permettant de modifier la coordonnées en X et Y du point.
	 * @param x : La nouvelle coordonnée X
	 * @param y : La nouvelle coordonnée Y
	 */
	// Par Elias Kassas
	public void setXY(double x, double y) {
		setX(x) ;
		setY(y) ;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode permettant d'afficher les coordonnées du point sous la forme "(x, y)".
	 * @return Les coordonnées du point avec <b>nbDecimales = 3</b> décimales de précision
	 */
	// Par Elias Kassas
	public String toString() {
		return toString(3) ;
	}

	/**
	 * Méthode permettant d'afficher les coordonnées du point sous la forme "(x, y)".
	 * @param nbDecimales : Le nombre de décimales de précision voulues
	 * @return Les coordonnées du point avec <b>nbDecimales</b> décimales de précision
	 */
	// Par Elias Kassas
	public String toString(int nbDecimales) {
		return "(" + String.format("%."+nbDecimales + "f", xCentre) + ", " + 
				String.format("%."+nbDecimales + "f", yCentre) + ")" ;
	}
	
	/**
	 * Méthode permettant de modifier les ratios d'affichage appliqués au point mouvant.
	 * @param ratioX : ratio d'affichage en X
	 * @param ratioY : ratio d'affichage en Y
	 */
	// Par Elias Kassas
	public void setRatioAffichage(double ratioX, double ratioY) {
		this.ratioAffichageX = ratioX ;
		this.ratioAffichageY = ratioY ;
		creerLaGeometrie() ;
	}
}

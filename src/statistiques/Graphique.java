package statistiques ;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * Composant permettant de tracer une fonction en segments avec une forme Path2D
 * Il est possible de recadrer la portion visible de la fonction.
 * @author Caroline Houle
 * @author Elias Kassas
 */
public class Graphique extends JPanel {
	/** Identificateur de classe (sérialization). **/
	private static final long serialVersionUID = 1L ;

	/** Valeur minimale par défaut visible sur l'axe des x. **/
	private final double DEFAUT_X_MIN = 0 ;
	/** Valeur maximale par défaut visible sur l'axe des x. **/
	private final double DEFAUT_X_MAX = 40 ;
	/** Valeur minimale par défaut visible sur l'axe des y. **/
	private final double DEFAUT_Y_MIN = 0 ;
	/** Valeur maximale par défaut visible sur l'axe des y. **/
	private final double DEFAUT_Y_MAX = 5 ;

	/** Valeur minimale visible sur l'axe des x. **/
	private double xMin = DEFAUT_X_MIN ;
	/** Valeur maximale visible sur l'axe des x. **/
	private double xMax = DEFAUT_X_MAX ;
	/** Valeur minimale visible sur l'axe des y. **/
	private double yMin = DEFAUT_Y_MIN ;
	/** Valeur maximale visible sur l'axe des y. **/
	private double yMax = DEFAUT_Y_MAX ;
	/** Ratio d'affichage des taquets et des grillages. 
	 * Le traitement 1 : ratioAffichage est géré par le composant. **/
	private double ratioAffichageX = 1, ratioAffichageY = 0.5 ;
	/** Point de départ sur le graphique. **/
	private double ptDepartX, ptDepartY ;

	/** Variables représentant les axes ainsi que la fonction. **/
	private Path2D.Double axes, courbe = null ;
	/** Collections contenant les coordonées des points à ajouter. **/
	private ArrayList<Double> pointsADessinerX, pointsADessinerY ;
	/** Nombre de pixels par unité. **/
	private double pixelsParUniteX, pixelsParUniteY ;

	// Entrées fournies au composant.
	/** Titre du graphique. **/
	private String titreGraphique = "Y en fonction de X" ;
	/** Matrices de transformations du graphique et du point mouvant. **/
	private AffineTransform matTransfo = new AffineTransform() ;
	/** Variable booléenne vérifiant si le dessin est dessiné pour la première fois. **/
	private boolean premiereFois = true ;
	/** Nombre de graduations en X et en Y. **/
	private final int NB_GRADUATIONS_X = 10, NB_GRADUATIONS_Y = 10 ;

	/**
	 * Constructeur: crée le composant et fixe la couleur de fond.
	 * ATTENTION : Il faut poser un point de départ (x, y) au graphique pour qu'il puisse être 
	 * généré par le panel. Autrement, la génération de celui-ci commencera au point (0, 0) 
	 * par défaut.
	 */
	// Par Elias Kassas
	public Graphique() {
		setBackground(Color.white) ;
		setPreferredSize(new Dimension(342, 304)) ;

		pointsADessinerX = new ArrayList<Double>() ;
		pointsADessinerY = new ArrayList<Double>() ;

		poserUnPointDeDepart(xMin, yMin) ;
	} //fin du constructeur

	/**
	 * Méthode servant à ajouter un point de départ au graphique dessiné.
	 * @param x : La coordonnée en X du point de départ.
	 * @param y : La coordonnée en Y du point de départ.
	 */
	// Par Elias Kassas
	public void poserUnPointDeDepart(double x, double y) {
		ptDepartX = x ;
		ptDepartY = y ;

		xMin = DEFAUT_X_MIN ;
		xMax = DEFAUT_X_MAX ;
		yMin = DEFAUT_Y_MIN ;
		yMax = DEFAUT_Y_MAX ;

		pointsADessinerX.add(0, ptDepartX) ;
		pointsADessinerY.add(0, ptDepartY) ;

		repaint() ;
	}

	/**
	 * Dessiner la fonction sur le composant
	 * @param g : Le contexte graphique
	 */
	// Par Elias Kassas
	@Override
	public void paintComponent(Graphics g) {		
		super.paintComponent(g) ;
		Graphics2D g2d = (Graphics2D) g ;	

		pixelsParUniteX = getWidth()/(xMax-xMin) ;
		pixelsParUniteY = getHeight()/(yMax-yMin) ;

		creerAxes() ;

		if (premiereFois) {
			matTransfo.scale(1, -1) ;

			//matTransfo.scale(ratioAffichageX, ratioAffichageY) ;

			matTransfo.scale(pixelsParUniteX, pixelsParUniteY) ;

			matTransfo.translate(-xMin, -yMax) ;
			//matTransfoPtMouvant.translate(-xMin, yMax - getHeight()) ;

			premiereFois = false ;
		}

		//matTransfo.scale(ratioAffichageX, ratioAffichageY) ;

		g2d.setFont(new Font("K2D", Font.PLAIN, 15)) ;

		//on dessine les axes
		g2d.setColor(Color.blue) ;
		g2d.draw(matTransfo.createTransformedShape(axes)) ;

		//on dessine les taquets sur l'axe des x
		dessinerTaquetsGrillageX(g2d) ;

		//on dessine les taquets sur l'axe des y
		dessinerTaquetsGrillageY(g2d) ;

		//on dessine la courbe 
		g2d.setColor(Color.red) ;
		courbe = creerLaCourbe() ;
		g2d.setStroke(new BasicStroke(2)) ;
		g2d.draw(matTransfo.createTransformedShape(courbe)) ;
		g2d.setStroke(new BasicStroke(1)) ;

		// On écrit le titre centré par rapport au composant au dessus de la courbe.
		int longueurTitre = g2d.getFontMetrics().stringWidth(titreGraphique) ;
		float positionXTitre = (getWidth() - longueurTitre)/2 ;
		g2d.drawString(titreGraphique, positionXTitre, g2d.getFont().getSize2D()) ;

		recadrer(xMin, xMax, yMin, yMax) ;
	}//fin paintComponent

	/**
	 * Création du Path2D formant les axes
	 */
	private void creerAxes() {
		axes = new Path2D.Double() ;
		axes.moveTo(xMin, 0) ;
		axes.lineTo(xMax,  0) ;
		axes.moveTo(0,  yMin) ;
		axes.lineTo(0,  yMax) ;
	}

	/**
	 * Création de la courbe sous la forme d'un Path2D.
	 * NOTE : Plus il y aura de points, plus la "courbe" ressemblera à une courbe.
	 */
	// Par Elias Kassas
	private Path2D.Double creerLaCourbe() {
		Path2D.Double courbe = new Path2D.Double() ;

		// On vérifie qu'il y a bien des éléments présent dans les listes de coordonnées outre 
		// le point de départ.
		// Si ce n'est pas le cas, on retourne une courbe "vierge".
		if (pointsADessinerX.size() - 1 != 0 && pointsADessinerY.size() != 0 && courbe != null) {
			courbe.moveTo(pointsADessinerX.get(0), pointsADessinerY.get(0)) ;

			for (int i = 1 ; i < pointsADessinerX.size() && i < pointsADessinerY.size() ; i++) {
				double x = pointsADessinerX.get(i), // ratioAffichageX, 
						y = pointsADessinerY.get(i) ; // ratioAffichageY ;

				//System.out.println(this.toString()) ;
				courbe.lineTo(x, y) ;

			}
		}

		return courbe ;
	}

	/**
	 * Création de la courbe sous la forme d'un Path2D.
	 * NOTE : Plus il y aura de points, plus la "courbe" ressemblera à une courbe.
	 */
	// Par Elias Kassas
	/*private void creerLaCourbe() {
		Path2D.Double courbe = new Path2D.Double() ;
		courbe.moveTo(ptDepartX / ratioAffichageX, ptDepartY / ratioAffichageY) ;
		courbe.lineTo(ptDepartX / ratioAffichageX, ptDepartY) ;
		this.courbe = courbe ;
	}*/

	/**
	 * Méthode servant à actualiser le nombre de graduations de chaque axe.
	 * @param x : La valeur en x à comparer
	 * @param y : La valeur en y à comparer
	 */
	// Par Elias Kassas
	private void actualiserLesGraduations(double x, double y) {
		recadrer(xMin, Math.max(x, xMax), yMin, Math.max(y, yMax)) ;
		// Possible système sophistiqué d'adaptation des taquets...
		repaint() ;
	}

	/**
	 * Méthode permettant d'ajouter une graduation au graphique.
	 */
	// Par Elias Kassas
	private void ajouterUneGraduation() {
		recadrer(xMin, xMax + ratioAffichageX, yMin, yMax + ratioAffichageY) ;
	}

	/**
	 * Méthode servant à réinitialiser le graphique et effacer tous les points présents sur 
	 * celui-ci.
	 */
	// Par Elias Kassas
	public void reinitialiser() {
		pointsADessinerX.clear() ;
		pointsADessinerY.clear() ;
		recadrer(DEFAUT_X_MIN/ratioAffichageX, DEFAUT_X_MAX/ratioAffichageX, 
				DEFAUT_Y_MIN/ratioAffichageY, DEFAUT_Y_MAX/ratioAffichageY) ;
		poserUnPointDeDepart(ptDepartX, ptDepartY) ;
		repaint() ;
	}

	/**
	 * Méthode servant à dessiner les taquets accompagnés de leur valeur ainsi que de 
	 * leur grillage en x.
	 * @param g2d : Le contexte graphique.
	 */
	// Par Elias Kassas
	private void dessinerTaquetsGrillageX(Graphics2D g2d) {
		double posY1 = (getHeight() - 5) ;
		double posY2 = (getHeight() + 5) ;

		// Dessiner des taquets sur l'axe des x
		//for (double i = xMin ; i <= xMax ; i += 1) {
		for (double i = 0 ; i <= NB_GRADUATIONS_X ; i += 1) { 
			double espacementX = (xMax - xMin) / NB_GRADUATIONS_X ;
			double posX = xMin + i * espacementX ;
			posX *= pixelsParUniteX ;

			if (i != 0) { // Pour éviter de cacher l'axes des x.
				// On dessine le grillage en x.
				Line2D.Double grillageX = new Line2D.Double(posX, 0, posX, getHeight()) ;
				g2d.setColor(Color.lightGray) ;
				g2d.draw(grillageX) ;

				// On dessine les taquets en x.
				Line2D.Double taquetX = new Line2D.Double(posX, posY1, posX, posY2) ;
				g2d.setColor(Color.red) ;
				g2d.draw(taquetX) ;

				// On place une valeur à chaque taquet.
				g2d.setColor(Color.orange) ;
				g2d.drawString(String.format("%.1f", ratioAffichageX*i), (float) taquetX.getX2(), 
						(float) taquetX.getY1()) ;
			}
		}
	}

	/**
	 * Méthode servant à dessiner les taquets accompagnés de leur valeur ainsi que de 
	 * leur grillage en y.
	 * @param g2d : Le contexte graphique.
	 */
	// Par Elias Kassas
	private void dessinerTaquetsGrillageY(Graphics2D g2d) {
		double posX1 = xMin - 5 ;
		double posX2 = xMin + 5 ;

		// Dessiner des taquets sur l'axe des y.
		for (double j = 0 ; j <= NB_GRADUATIONS_Y ; j += 1) {
			//double posY = (j - yMin) * pixelsParUniteY ;

			double espacementY = (yMax - yMin) / NB_GRADUATIONS_Y ;
			double posY = yMax - j * espacementY ;
			posY *= pixelsParUniteY ;

			if (j != 0) { // Pour éviter de cacher l'axes des y.
				// On dessine le grillage en y.
				Line2D.Double grillageY = new Line2D.Double(0, posY, getWidth(), posY) ;
				g2d.setColor(Color.gray) ;
				g2d.draw(grillageY) ;

				// On dessine les taquets en y.
				Line2D.Double taquetY = new Line2D.Double(posX1, posY, posX2, posY) ;
				g2d.setColor(Color.red) ;
				g2d.draw(taquetY) ;

				//AffineTransform matInit = g2d.getTransform() ;
				//g2d.scale(1, -1) ; 
				// On place une valeur à chaque taquet.
				g2d.setColor(Color.orange) ;
				g2d.drawString(String.format("%.1f", (yMin + j)*ratioAffichageY), 
						(float) taquetY.getX2(), (float) taquetY.getY2()) ;
				//g2d.setTransform(matInit) ;
			}
		}
	}

	/**
	 * Méthode servant à ajouter un point.
	 * @param x : La coordonnée en X du point à ajouter.
	 * @param y : La coordonnée en Y du point à ajouter.
	 */
	// Par Elias Kassas
	public void ajouterUnPointSurLaCourbe(double x, double y) { 
		boolean capaciteMaxAtteinte = (int) Math.ceil(x) >= (int) Math.ceil(xMax) ;

		if (courbe != null) {
			pointsADessinerX.add(x) ;
			pointsADessinerY.add(y) ;

			if (capaciteMaxAtteinte) 
				ajouterUneGraduation() ;

			// Mettre à jour la courbe avec les nouveaux points
			courbe = creerLaCourbe() ;
		}

		repaint() ;
	}

	/**
	 * Permet de modifier les limites entre lesquelles la fonction sera tracée
	 * 
	 * @param xMin : Abcisse minimale visible
	 * @param xMax : Abcisse maximale visible
	 * @param yMin : Ordonnée minimale visible
	 * @param yMax : Ordonnée maximale visible
	 */
	public void recadrer(double xMin, double xMax, double yMin, double yMax) {
		ratioAffichageX = xMax/NB_GRADUATIONS_X ;
		ratioAffichageY = yMax/NB_GRADUATIONS_Y ;
		//pixelsParUniteX = xMax/NB_GRADUATIONS_X ;
		//pixelsParUniteY = yMax/NB_GRADUATIONS_Y ;

		this.xMin = xMin ;
		this.xMax = xMax ;
		this.yMin = yMin ;
		this.yMax = yMax ;
		repaint() ;
	}

	/**
	 * Méthode permettant d'obtenir le titre d'un graphique.
	 * @return
	 */
	// Par Elias Kassas
	public String getTitre() {
		return titreGraphique ;
	}

	/**
	 * Méthode servant à changer le titre du graphique.
	 * @param nouveauTitre : Nouveau titre du graphique.
	 */
	// Par Elias Kassas
	public void setTitre(String nouveauTitre) {
		titreGraphique = nouveauTitre ;
		repaint() ;
	}

	/**
	 * Méthode permettant d'afficher la liste des points de la courbe sous forme de chaîne.
	 * @param nbDecimales : Le nombre de décimales de précision voulues.
	 * @return Une chaîne de caractères contenant la liste des points de la courbe.
	 */
	// Par Elias Kassas
	@Override
	public String toString() {
		String s = "[" ;

		for (int n = 0 ; n < pointsADessinerX.size() ; n++)
			s += "(" + pointsADessinerX.get(n) + ", " + pointsADessinerY.get(n) + ")\t" ;

		s+= "]" ;

		return "Graphique " + titreGraphique + "\n\t" + s ;
	}

	/**
	 * Méthode permettant d'obtenir le ratio d'affichage sur l'axe des X.
	 * @return Le ratio d'affichage sur l'axe des X.
	 */
	// Par Elias Kassas
	public double getRatioAffichageX() {
		return ratioAffichageX ;
	}

	/**
	 * Méthode permettant de modifier le ratio d'affichage sur l'axe des X.
	 * @param ratioAffichageX : Le ratio d'affichage sur l'axe des X.
	 */
	// Par Elias Kassas
	public void setRatioAffichageX(double ratioAffichageX) {
		this.ratioAffichageX = ratioAffichageX ;
		repaint() ;
	}

	/**
	 * Méthode permettant d'obtenir le ratio d'affichage sur l'axe des Y.
	 * @return Le ratio d'affichage sur l'axe des Y.
	 */
	// Par Elias Kassas
	public double getRatioAffichageY() {
		return ratioAffichageY ;
	}

	/**
	 * Méthode permettant de modifier le ratio d'affichage sur l'axe des Y.
	 * @param ratioAffichageY : Le ratio d'affichage sur l'axe des Y.
	 */
	// Par Elias Kassas
	public void setRatioAffichageY(double ratioAffichageY) {
		this.ratioAffichageY = ratioAffichageY ;
		repaint() ;
	}
} // fin classe
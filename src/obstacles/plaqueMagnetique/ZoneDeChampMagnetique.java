package obstacles.plaqueMagnetique;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.io.Serializable;

import composantDeJeu.Balle;
import interfaces.Dessinable;
import interfaces.Selectionnable;
import math.vecteurs.Vecteur3D;
import obstacles.Obstacle;
import outils.GererSon;
import outils.OutilsSon;
import physique.MoteurPhysique;

/**
 * Cette classe représente une zone de champ magnétique.
 * Notez qu'elle dérive de la classe-mère Obstacle pour le moment en attendant que la plaque 
 * magnétique soit implémentée.
 * @author Elias Kassas
 */
public class ZoneDeChampMagnetique extends Obstacle implements Dessinable, Selectionnable, Serializable {
	/** Coefficient de sérialisation pour les fichiers. **/
	private static final long serialVersionUID = 1L ;
	/** Rayon dessiné de la zone de champ magnétique. **/
	private double rayonDessine ;
	/** Rayons d'attraction minimal et maximal du champ magnétique par défaut. **/
	private final double RAYON_ATTRACTION_MIN_DEFAUT = 0, RAYON_ATTRACTION_MAX_DEFAUT = 0 ;
	/** Rayons d'attraction minimal et maximal du champ magnétique. **/
	private double rayonAttractionMin = RAYON_ATTRACTION_MIN_DEFAUT, 
			rayonAttractionMax = RAYON_ATTRACTION_MAX_DEFAUT ;
	/** Vecteur en 3D du champ magnétique, B. B = [0, 0, k] où k est un réel. **/
	private Vecteur3D champMagnetique ;
	/** Vecteur en 3D de la force magnétique résultante. **/
	private Vecteur3D forceMagnetique ;

	/** Objet dessinable matérialisant la zone de champ magnétique. **/
	private Ellipse2D.Double zone ;
	/** Point signifiant que le vecteur champ magnétique est sortant. (si z > 0) **/
	private Ellipse2D.Double point ;
	/** Cercle représentant la bordure de la zone de champ magnétique. **/
	private Ellipse2D.Double cercle ;
	/** Rayon physique de la zone de champ magnétique. **/
	private double rayonActuel ;
	/** OutilsSon de l'obstacle **/
	private transient OutilsSon sonObst = new OutilsSon();
	/**Nom du fichier du son**/
	private final String NOM_DU_FICHIER="ChampMagnetique.wav";
	/** Seulement exécuté la première fois que l'application est lancée **/
	private boolean premiereFois = true;

	/**
	 * Constructeur d'une ZoneDeChampMagnetique.
	 * @param x : La coordonnée en X du centre de la zone.
	 * @param y : La coordonnée en X du centre de la zone.
	 * @param rayonPhysAimant : La longueur du rayon physique dessiné de la zone.
	 * @param moduleChampMagn : Le module du champ magnétique généré par la zone.
	 * @param rayonAttractionMin : Rayon d'attraction minimal de la zone.
	 * @param rayonAttractionMax : Rayon d'attraction maximal de la zone.
	 * @param couleur : Couleur d'une plaque magnétique.
	 */
	// Par Elias Kassas
	public ZoneDeChampMagnetique(double x, double y, double rayonPhysAimant, double moduleChampMagn, 
			double rayonAttractionMin, double rayonAttractionMax, Color couleur) {
		super(new Vecteur3D(x, y), couleur) ;
		this.rayonDessine = rayonPhysAimant ;
		this.champMagnetique = new Vecteur3D(0, 0, moduleChampMagn) ;

		this.rayonAttractionMin = rayonAttractionMin ;
		this.rayonAttractionMax = rayonAttractionMax ;

		creerLaGeometrie() ;
	}

	/**
	 * Méthode servant à créer la géometrie d'une zone de champ magnétique.
	 */
	// Par Elias Kassas
	protected void creerLaGeometrie() {
		if(premiereFois) {
			sonObst.chargerUnSonOuUneMusique(NOM_DU_FICHIER);
			premiereFois=false;
		}

		double xCoin = this.position.getX() - rayonDessine, yCoin = this.position.getY() - rayonDessine ;
		double diametreDessine = 2 * rayonDessine ;

		zone = new Ellipse2D.Double(xCoin, yCoin, diametreDessine, diametreDessine) ;
		//point = new Ellipse2D.Double(xCoin, yCoin, rayonDessine/2, rayonDessine/2) ;

		double xCercle = this.position.getX() - rayonAttractionMax,
				yCercle = this.position.getY() - rayonAttractionMax ;
		cercle = new Ellipse2D.Double(xCercle, yCercle, 2*rayonAttractionMax, 2*rayonAttractionMax) ;
	}

	/**
	 * Retourne vrai si le point passé en paramètre fait partie de la zone de champ magnétique.
	 * @param xPix : Coordonnée en x du point (exprimée en pixels) 
	 * @param yPix : Coordonnée en y du point (exprimée en pixels)
	 */
	// Par Elias Kassas
	@Override
	public boolean contient(double xPix, double yPix) {
		return zone.contains(xPix, yPix) ;
	}

	/**
	 * Méthode permettant de dessiner la zone de champ magnétique.
	 * @param g2d : Le contexte graphique.
	 */
	// Par Elias Kassas
	@Override
	public void dessiner(Graphics2D g2d) {
		Graphics2D g2dPrive = (Graphics2D) g2d.create() ;
		g2dPrive.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON) ;
		g2dPrive.draw(zone) ;

		if (champMagnetique.getZ() < 0) {
			// Dessiner la croix
			double demiLongueur = rayonDessine ;

			double x1 = position.getX() - demiLongueur ;
			double y1 = position.getY() ;
			double x2 = position.getX() + demiLongueur ;
			double y2 = position.getY() ;
			Line2D.Double ligne1 = new Line2D.Double(x1, y1, x2, y2) ;

			double x3 = position.getX() ;
			double y3 = position.getY() - demiLongueur ;
			double x4 = position.getX() ;
			double y4 = position.getY() + demiLongueur ;
			Line2D.Double ligne2 = new Line2D.Double(x3, y3, x4, y4) ;

			g2dPrive.rotate(Math.PI/4, position.getX(), position.getY()) ;
			g2dPrive.setColor(couleur) ;
			g2dPrive.draw(ligne1) ;
			g2dPrive.draw(ligne2) ;
			g2dPrive.rotate(-Math.PI/4, position.getX(), position.getY()) ;
		} /*else if (champMagnetique.getZ() > 0) {
			g2dPrive.setColor(couleur) ;
			g2d.fill(point) ;
		}*/

		g2d.draw(cercle) ;
	}

	/**
	 * Méthode servant à retrouver la coordonnée en X du centre de l'aimant.
	 * @return La coordonnée en X du centre de l'aimant.
	 */
	// Par Elias Kassas
	public double getX() {
		return position.getX() ;
	}

	/**
	 * Méthode servant à retrouver la coordonnée en X du centre de l'aimant.
	 * @param x : La nouvelle coordonnée en X du centre de l'aimant.
	 */
	// Par Elias Kassas
	public void setX(double x) {
		super.setPosition(new Vecteur3D(x, super.getPosition().getY())) ;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode servant à retrouver la coordonnée en Y du centre de l'aimant.
	 * @return La coordonnée en Y du centre de l'aimant.
	 */
	// Par Elias Kassas
	public double getY() {
		return position.getY() ;
	}

	/**
	 * Méthode servant à retrouver la coordonnée en Y du centre de l'aimant.
	 * @param y : La nouvelle coordonnée en Y du centre de l'aimant.
	 */
	// Par Elias Kassas
	public void setY(double y) {
		super.setPosition(new Vecteur3D(super.getPosition().getX(), y)) ;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode servant à retrouver les coordonnées en X et en Y du centre de l'aimant.
	 * @param position : La position du centre de l'aimant.
	 */
	// Par Elias Kassas
	public void setPosition(Vecteur3D position) {
		super.setPosition(position) ;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode servant à retourner le rayon d'attraction minimal.
	 * @return Le rayon d'attraction minimal.
	 */
	// Par Elias Kassas
	public double getRayonAttractionMin() {
		return rayonAttractionMin ;
	}

	/**
	 * Méthode servant à modifier le rayon d'attraction minimal.
	 * @param rayonAttractionMin : Le rayon d'attraction minimal.
	 */
	// Par Elias Kassas
	public void setRayonAttractionMin(double rayonAttractionMin) {
		this.rayonAttractionMin = rayonAttractionMin ;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode servant à retourner le rayon d'attraction maximal.
	 * @return Le rayon d'attraction maximal.
	 */
	// Par Elias Kassas
	public double getRayonAttractionMax() {
		return rayonAttractionMax ;
	}

	/**
	 * Méthode servant à modifier le rayon d'attraction minimal.
	 * @param rayonAttractionMax : Le rayon d'attraction minimal.
	 */
	// Par Elias Kassas
	public void setRayonAttractionMax(double rayonAttractionMax) {
		this.rayonAttractionMax = rayonAttractionMax ;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode permettant de retourner le vecteur champ magnétique de la zone, B.
	 * @return Le vecteur champ magnétique de la zone.
	 */
	// Par Elias Kassas
	public Vecteur3D getChampMagnetique() {
		return champMagnetique ;
	}

	/**
	 * Méthode permettant de modifier le vecteur champ magnétique de la zone, B.
	 * @param champMagnetique : Le nouveau vecteur champ magnétique de la zone.
	 */
	// Par Elias Kassas
	public void setChampMagnetique(double champMagnetique) {
		this.champMagnetique.setZ(champMagnetique) ;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode permettant d'évaluer la force magnétique générée par la zone de champ magnétique.
	 * @param vitesseBalle : La vitesse de la balle en m/s.
	 * @param chargeBalle : La charge de la balle en Coulombs.
	 * @return La force magnétique sur la balle
	 * @throws Exception Si le vecteur ne peut être 
	 */
	// Par Elias Kassas
	public Vecteur3D evaluerForceMagnetique(Vecteur3D vitesseBalle, double chargeBalle) throws Exception {
		forceMagnetique = MoteurPhysique.forceMagnetique(champMagnetique, vitesseBalle, chargeBalle) ;

		return forceMagnetique ;
	}

	/**
	 * Méthode permettant de vérifier si une balle est attirée par la zone de champ magnétique.
	 * @param balle : La balle que dont on veut vérifier l'attraction à la zone de champ magnétique.
	 * @return L'attraction ou non à la zone de champ magnétique 
	 * (true = attire, false = n'attire pas).
	 */
	// Par Elias Kassas
	public boolean attire(Balle balle) {
		double distanceBalleZone = Math.sqrt(Math.pow(balle.getPosition().getX() - 
				this.getPosition().getX(), 2) + Math.pow(balle.getPosition().getY() - 
						this.getPosition().getY(), 2)) ;

		return distanceBalleZone >= rayonAttractionMin && distanceBalleZone <= rayonAttractionMax ;
	}

	/**
	 * Méthode permettant d'évaluer la collision entre une zone de champ magnétique et une balle.
	 * @param balle : La balle dont on doit évaluer la collision.
	 * @throws Exception Si le vecteur normalisé calculé ne peut pas l'être.
	 */
	// Par Elias Kassas
	@Override
	public void collision(Balle balle) throws Exception {
		if (sonObst == null) {
			premiereFois = true ;
			sonObst = new OutilsSon() ;
			creerLaGeometrie() ;
		}

		if(GererSon.isAllumerFermer()) {
			sonObst.jouerUnSon();
		}

		Vecteur3D posBalle = new Vecteur3D(balle.obtenirCentreX(), balle.obtenirCentreY()),
				dist = posBalle.soustrait(this.position) ;
		Vecteur3D forces = evaluerForceMagnetique(balle.getVitesse(), balle.getCharge()) ;
		balle.ajouterAForceMagnetique(forces) ;
	}

	/**
	 * Méthode permettant de vérifier si la balle intersecte la zone de champ magnétique.
	 * @param balle : La balle dont on doit vérifier l'intersection.
	 * @return Si la balle intersecte la zone de champ magnétique
	 */
	// Par Elias Kassas
	@Override
	public boolean intersection(Balle balle) {
		Vecteur3D posBalle = new Vecteur3D(balle.obtenirCentreX(), balle.obtenirCentreY()),
				distance = posBalle.soustrait(position) ;

		return distance.module() < rayonAttractionMax || distance.module() < rayonAttractionMax + 0.1 || 
				distance.module() < rayonAttractionMax - 0.1 ;
	}

	/**
	 * Méthode toString permettant d'afficher les propriétés du la ZoneDeChampMagnetique.
	 */
	// Par Elias Kassas
	@Override
	public String toString() {
		return "ZoneDeChampMagnetique [rayonDessine = " + rayonDessine + " unités, "
				+ "rayonAttractionMin = " + rayonAttractionMin + ", rayonAttractionMax = " + 
				rayonAttractionMax + ", " + "champMagnetique = " + champMagnetique + ", "
				+ "forceMagnetique = " + forceMagnetique + ", rayonActuel = " + rayonActuel + "]" ;
	}

	/**
	 * 
	 */
	@Override
	public void avancerUnPas(Double deltaT) {
		// TODO Auto-generated method stub
	}

	/**
	 * 
	 */
	@Override
	public void setDeplacement(Vecteur3D deplacement) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 */
	@Override
	public void reinitialiser() {
		// TODO Auto-generated method stub	
	}

	/**
	 * Méthode servant à déterminer s'il y a une intersection entre un obstacle et une zone de 
	 * champ magnétique.
	 */
	// Par Elias Kassas
	@Override
	public boolean intersection(Obstacle obst) {
		Area aireInter = new Area(cercle) ;
		aireInter.intersect(obst.getAire()) ; 

		return !aireInter.isEmpty() ;
	}

	/**
	 * Méthode servant à obtenir l'aire d'une zone de champ magnétique.
	 */
	@Override
	public Area getAire() {
		return new Area(cercle) ;
	}
}

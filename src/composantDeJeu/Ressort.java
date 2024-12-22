package composantDeJeu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.io.Serializable;

import interfaces.Dessinable;
import math.vecteurs.Vecteur3D;
import obstacles.Mur;
import physique.MoteurPhysique;

/**
 * Classe représentant un ressort pouvant être dessiné selon un angle précis.
 * Note : Pour simplifier la programmation et la physique, un ressort ne peut être que compressé. 
 * De plus, la physique du ressort a un peu été approximée.
 * @author Elias Kassas
 * @author Félix Lefrançois
 */
public class Ressort implements Dessinable, Serializable {
	/** Coefficient de sérialisation pour les fichiers. **/
	private static final long serialVersionUID = 1L;
	/** Position vectorielle du coin inférieur droit du ressort. **/
	private Vecteur3D positionAncre ;
	/** Longueur et largeur de la boîte englobante du ressort. **/
	private double longueurNaturelle, longueur, largeur ;
	/** Nombre de boucles d'un ressort. **/
	private int nbBoucles ;
	/** Diamètre d'une boucle **/
	private double diametreBoucle ;
	/** Angle de rotation d'un ressort en radians. **/
	private double angleRot ;
	/** Path2D du ressort avant d'appliquer la rotation. **/
	private Path2D.Double ressort ;
	/** Path2D du ressort après d'appliquer la rotation. **/
	private Shape ressortTransfo ;
	/** Constante de rappel du ressort. **/
	private double constanteRappel ;
	/** Étirement du ressort pour l'animation de la collision. **/
	private double etirement ;
	/** Objet mur représentant le platteau de support du ressort. **/
	private Mur plateauSupport ;
	/** Position du plateau de support du ressort. **/
	private double plateauX, plateauY ;
	/** Hauteur du plateau de support du ressort. **/
	private double hauteurPlateau = 10.0 ;

	/**
	 * Constructeur d'un ressort.
	 * @param x : Coordonnée en X de la position du coin inférieur droit du ressort.
	 * @param y : Coordonnée en Y de la position du coin inférieur droit du ressort.
	 * @param longueur : Longueur de la boîte englobante du ressort.
	 * @param largeur : Largeur de la boîte englobante du ressort.
	 * @param nbBoucles : Nombre de boucles d'un ressort.
	 * @param diametreBoucle : Diamètre d'une boucle.
	 * @param angleRot : Angle de rotation d'un ressort en radians.
	 * @param constanteRappel : Constante de rappel du ressort.
	 */
	// Par Elias Kassas
	public Ressort(double x, double y, double longueur, double largeur, int nbBoucles, double diametreBoucle,
			double angleRot, double constanteRappel) {
		this.positionAncre = new Vecteur3D(x, y) ;
		this.longueurNaturelle = longueur ;
		this.largeur = largeur ;
		this.nbBoucles = 2*nbBoucles ; // Pour faire un aller-retour sur le dessin.
		this.diametreBoucle = diametreBoucle ;
		this.angleRot = angleRot ;
		this.constanteRappel = constanteRappel ;
		this.etirement = 0 ;

		this.longueur = longueurNaturelle ;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode privée permettant de créer la géométrie d'un ressort.
	 */
	// Par Elias Kassas
	private void creerLaGeometrie() {
		ressort = new Path2D.Double() ;

		double xPrime = positionAncre.getX(), yPrime = positionAncre.getY() ;
		yPrime = (longueur+etirement/nbBoucles)/nbBoucles ;
		double deplacementY = positionAncre.getY();

		ressort.moveTo(positionAncre.getX(), positionAncre.getY()) ;
		for (int i = 0 ; i <= nbBoucles ; i++) {
			if (i%2 == 0) {
				xPrime = positionAncre.getX() - largeur ;
			} else {
				xPrime = positionAncre.getX() ;
			}

			ressort.lineTo(xPrime, deplacementY) ;
			deplacementY -= yPrime + etirement/nbBoucles;
		}

		creerLaFinDuRessort() ;

		plateauX = ressort.getCurrentPoint().getX() - largeur / 2d ;
		plateauY = ressort.getCurrentPoint().getY() - longueur + 3*hauteurPlateau/2d ;
		plateauSupport = new Mur(new Vecteur3D(plateauX, plateauY), hauteurPlateau, largeur, 0.0, 
				Color.darkGray.brighter()) ;

		// Appliquer la rotation du ressort en son point initial s'il y a lieu.
		AffineTransform matTransfo = new AffineTransform() ;
		matTransfo.rotate(angleRot, positionAncre.getX(), positionAncre.getY()) ;
		ressortTransfo = matTransfo.createTransformedShape(ressort) ;
	}

	/**
	 * Méthode pour ajouter la fin du ressort au path2D
	 */
	//J'ai fait plus que ça comme modifications dans creerLaGeometrie(), sauf que ce sont plusieurs petites
	//modifications qui ne méritent pas de méthodes
	//Félix Lefrançois
	private void creerLaFinDuRessort() {
		if (!(nbBoucles%2 == 0)) {
			ressort.lineTo(positionAncre.getX()-largeur, positionAncre.getY()-longueur-etirement);
		} else if (nbBoucles%2 == 0) {
			ressort.lineTo(positionAncre.getX(), positionAncre.getY()-longueur-etirement);
		}
	}

	/**
	 * Méthode permettant de dessiner un ressort.
	 * @param g2d : Le contexte graphique.
	 */
	// Par Elias Kassas
	@Override
	public void dessiner(Graphics2D g2d) {
		Graphics2D g2dPrive = (Graphics2D) g2d.create() ;
		g2dPrive.setColor(Color.GRAY) ;
		g2dPrive.draw(ressortTransfo) ;

		plateauSupport.dessiner(g2dPrive) ;
	}

	/**
	 * Méthode permettant d'évaluer s'il y a une intersection avec un ressort.
	 * @param balle : La balle à tester.
	 * @return S'il y a ou non une intersection avec un ressort.
	 */
	// Par Elias Kassas
	public boolean intersection(Balle balle) {
		return plateauSupport.intersection(balle) ;
	}

	/**
	 * Méthode permettant de gérer la collision avec un ressort.
	 * @param balle : La balle à laquelle on veut appliquer une force de rappel.
	 */
	// Par Elias Kassas
	public void collision(Balle balle) {
		Vecteur3D forceRap ;

		Area inter = new Area(plateauSupport.getAire()) ;
		inter.intersect(balle.getAireBalle()) ;
		if (!inter.isEmpty()) {
			forceRap = MoteurPhysique.forceDuRessort(constanteRappel, new Vecteur3D(0, -etirement)) ;
			longueur = longueurNaturelle - etirement ;
			balle.setPosition(balle.getPosition().additionne(new Vecteur3D(0, -balle.getDiametre()/2))) ;
			Vecteur3D nouvelleVitesse = Vecteur3D.additionne(balle.getVitesse(), trouverVitesseFinale(balle)) ;
			balle.setVitesse(nouvelleVitesse.multiplie(-1)) ;
		}

		this.etirement = 0.0 ;
		creerLaGeometrie() ;
		//this.longueur = longueurNaturelle ;
	}

	/**
	 * Méthode permettant de propulser une balle à partir d'un ressort.
	 * @param balle : La balle à propulser.
	 * @param etirement : Le vecteur étirement du ressort.
	 * @throws Exception Si le module du vecteur étirement est plus grande que l'amplitude (longueur) du 
	 * ressort.
	 */
	// Par Elias Kassas
	public void charger(Balle balle, Vecteur3D etirement) throws Exception {
		Vecteur3D forceRap ;
		balle.setVitesse(balle.getVitesse().multiplie(-1)) ;

		if (!(balle.obtenirCentreY() < longueurNaturelle)) {
			// Le ressort est comprimé.
			this.etirement = longueurNaturelle - balle.obtenirCentreY() ;
			/*Vecteur3D etirement = Vecteur3D.multiplie(new Vecteur3D(0, longueur), 
					1 - longueurNaturelle/longueur) ;*/
			forceRap = MoteurPhysique.forceDuRessort(constanteRappel, etirement) ;
			//longueur -= etirement.module() ;

			balle.setPosition(new Vecteur3D(balle.getPosition().getX(), longueur)) ;
			creerLaGeometrie() ;
		} else {
			forceRap = MoteurPhysique.forceDuRessort(constanteRappel, new Vecteur3D()) ;
		}

		balle.setVitesse(trouverVitesseFinale(balle).multiplie(-1)) ;
		balle.ajouterAForceRessort(forceRap) ;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode privée permettant de trouver la vitesse finale après une collision avec le ressort
	 * par conservation d'énergie.
	 * @return Le vecteur vitesse finale à appliquer à la balle.
	 */
	// Par Elias Kassas
	private Vecteur3D trouverVitesseFinale(Balle balle) {
		if (etirement != 0.0000000000000000000000000000000) {
			double energieCinInitiale = energieCin(balle.getMasse(), balle.getVitesse()), energieCinFinale ;
			//System.out.println("\nenergieCinInitiale = " + energieCinInitiale) ;
			double energiePotInitiale = energiePotRessort(constanteRappel, new Vecteur3D(0, -etirement)), 
					energiePotFinale = energiePotRessort(constanteRappel, new Vecteur3D(0, 0)) ;
			double travailRessort = travailRessort(MoteurPhysique.forceDuRessort(constanteRappel, 
					new Vecteur3D(0, -etirement)), new Vecteur3D(0, -etirement)) ;
			System.out.println("travailRessort = " + travailRessort) ;
			// On divise par quatre pour un meilleur effet visuel.
			double U1 = energieCinInitiale + energiePotInitiale + travailRessort, U2 = U1/4.0 ;

			energieCinFinale = energieCinInitiale + energiePotInitiale - energiePotFinale + U1 - U2 + 
					travailRessort ;
			
			double vF = Math.sqrt(constanteRappel * (2.0/balle.getMasse()) * energieCinFinale) ;
			return new Vecteur3D(0, vF) ;
		}
		return new Vecteur3D() ;
	}

	/**
	 * Méthode permettant d'avoir toutes les propriétés d'un ressort en chaîne de caractères.
	 * @return Les propriétés d'un ressort dans une chaîne de caractères.
	 */
	// Par Elias Kassas
	@Override
	public String toString() {
		return "Ressort [position=" + positionAncre + ", longueur=" + longueurNaturelle + ", largeur=" + largeur + ", nbBoucles="
				+ nbBoucles + ", diametreBoucle=" + diametreBoucle + ", angleRot=" + angleRot + ", ressort=" + ressort
				+ ", ressortTransfo=" + ressortTransfo + ", constanteRappel=" + constanteRappel + "]";
	}

	/**
	 * Méthode modifiant l'étirement du ressort
	 * @param nouvelEtirement Le nouvel étirement du ressort en cm
	 */
	//Félix Lefrançois
	public void setEtirement(double nouvelEtirement) {
		this.etirement = nouvelEtirement;
		creerLaGeometrie();
	}

	/**
	 * Méthode qui renvoie l'étirement du ressort
	 * @return L'étirement du ressort
	 */
	//Félix Lefrançois
	public double getEtirement(){
		return etirement;
	}

	/**
	 * Méthode permettant de calculer l'énergie potentielle du ressort.
	 * @param constRap : La constante de rappel.
	 * @param etirement : Le vecteur étirement du ressort.
	 * @return L'énergie potentielle du ressort.
	 */
	// Par Elias Kassas
	public static double energiePotRessort(double constRap, Vecteur3D etirement) {
		return constRap * etirement.module() * etirement.module()/2.0 ;
	}

	/**
	 * Méthode permettant de calculer l'énergie potentielle du ressort.
	 * @param masse
	 * @param vitesse
	 * @return
	 */
	// Par Elias Kassas
	public static double energieCin(double masse, Vecteur3D vitesse) {
		return masse * vitesse.module() * vitesse.module()/2.0 ;
	}

	/**
	 * 
	 * @param force
	 * @param etirement
	 * @return
	 */
	// Par Elias Kassas
	public static double travailRessort(Vecteur3D force, Vecteur3D etirement) {
		return Vecteur3D.prodScalaire(force, etirement) ;
	}

	/**
	 * Méthode retournant la constante de rappel du ressort
	 * @return La constante de rappel k du ressort
	 */
	//Félix Lefrançois
	public double getConstanteRappel() {
		return constanteRappel;
	}

	/**
	 * Méthode permettant de modifier la constante de rappel k du ressort
	 * @param constanteRappel La nouvelle constante de rappel du ressort
	 */
	//Félix Lefrançois
	public void setConstanteRappel(double constanteRappel) {
		this.constanteRappel = constanteRappel;
	}
}

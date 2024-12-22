package composantDeJeu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Path2D;
import java.io.Serializable;

import interfaces.Dessinable;
import math.vecteurs.Vecteur3D;
import physique.MoteurPhysique;

/**
 * Classe représentant un ressort pouvant être dessiné selon un angle précis.
 * @author Elias Kassas
 */
public class Ressort implements Dessinable, Serializable {
	/** Coefficient de sérialisation pour les fichiers. **/
	private static final long serialVersionUID = 1L;
	/** Position du coin inférieur droit du ressort. **/
	private double x, y ;
	private Vecteur3D position ;
	/** Longueur et largeur de la boîte englobante du ressort. **/
	private double longueur, largeur ;
	/** Nombre de boucles d'un ressort. EXPLICATION DE CE QU'EST UNE BOUCLE POUR MOI. **/
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

	/**
	 * Constructeur d'un ressort.
	 * @param x : Coordonnée en X de la position du coin inférieur droit du ressort.
	 * @param y : Coordonnée en Y de la position du coin inférieur droit du ressort.
	 * @param longueur : 
	 * @param largeur : 
	 * @param nbBoucles : 
	 * @param diametreBoucle : 
	 * @param angleRot : 
	 * @param constanteRappel : 
	 */
	// Par Elias Kassas
	public Ressort(double x, double y, double longueur, double largeur, int nbBoucles, double diametreBoucle,
			double angleRot, double constanteRappel) {
		this.x = x ;
		this.y = y ;
		this.position = new Vecteur3D(this.x, this.y) ;
		this.longueur = longueur ;
		this.largeur = largeur ;
		this.nbBoucles = nbBoucles ;
		this.diametreBoucle = diametreBoucle ;
		this.angleRot = angleRot ;
		this.constanteRappel = constanteRappel ;

		creerLaGeometrie() ;
	}

	/**
	 * Méthode privée permettant de créer la géométrie d'un ressort.
	 */
	// Par Elias Kassas
	private void creerLaGeometrie() {
		ressort = new Path2D.Double() ;
		ressort.moveTo(position.getX(), position.getY()) ;

		double xPrime = position.getX(), yPrime = position.getY() ;
		for (int i = 0 ; i < nbBoucles ; i++) {
			// Il y a un problème avec P_i' dans la boucle.
			yPrime += i*longueur/nbBoucles ;

			if (i%2 == 0) {
				xPrime = largeur ;
			} else {
				xPrime = position.getX() ;
			}
			System.out.println("P" + i + "' (" + xPrime + ", " + yPrime + ")") ;

			ressort.lineTo(xPrime, yPrime) ;
		}
		ressort.lineTo(largeur, yPrime) ;

		// Appliquer la rotation du ressort en son point initial s'il y a lieu.
		AffineTransform matTransfo = new AffineTransform() ;
		matTransfo.rotate(angleRot, position.getX(), position.getY()) ;
		ressortTransfo = matTransfo.createTransformedShape(ressort) ;
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
	}

	/**
	 * Méthode permettant d'évaluer s'il y a une intersection avec un ressort.
	 * @param balle : La balle à tester.
	 * @return S'il y a ou non une intersection avec un ressort.
	 */
	// Par Elias Kassas
	public boolean intersection(Balle balle) {
		return ressort.contains(balle.obtenirCentreX(), balle.obtenirCentreY()) ;
	}

	/**
	 * Méthode permettant de gérer la collision avec un ressort.
	 * @param balle : La balle à laquelle on veut appliquer une force de rappel.
	 */
	public void collision(Balle balle) {
		Vecteur3D etirement =  this.position.soustrait(
				new Vecteur3D(balle.obtenirCentreX(), balle.obtenirCentreY())) ;
		Vecteur3D forceRap = MoteurPhysique.forceDuRessort(constanteRappel, etirement) ;

		if (etirement.getY() > 0) {
			// Le ressort est étiré.
			System.out.println("Le ressort est étiré.") ;
			longueur += etirement.module() ;
		} else if (etirement.getY() < 0) {
			// Le ressort est comprimé.
			System.out.println("Le ressort est comprimé.") ;
			longueur -= etirement.module() ;
		}
		creerLaGeometrie() ;

		balle.ajouterASommeDesForces(forceRap.multiplie(-1)) ;
	}

	/**
	 * Méthode permettant de propulser une balle à partir d'un ressort.
	 * @param balle : La balle à propulser.
	 * @param etirement : Le vecteur étirement du ressort.
	 * @throws Exception Si le module du vecteur étirement est plus grande que l'amplitude (longueur) du ressort.
	 */
	public void charger(Balle balle, Vecteur3D etirement) throws Exception {
		Vecteur3D forceRap = MoteurPhysique.forceDuRessort(constanteRappel, etirement) ;

		if (etirement.module() < longueur) {
			if (etirement.getY() > 0) {
				// Le ressort est étiré.
				System.out.println("Le ressort est étiré.") ;
				longueur += etirement.module() ;
			} else if (etirement.getY() < 0) {
				// Le ressort est comprimé.
				System.out.println("Le ressort est comprimé.") ;
				longueur -= etirement.module() ;
			}
			creerLaGeometrie() ;
		} else {
			throw new Exception("L'étirement que vous tentez d'appliquer plus grand que l'amplitude du ressort.") ;
		}

		balle.ajouterASommeDesForces(forceRap.multiplie(-1)) ;
	}

	@Override
	public String toString() {
		return "Ressort [position=" + position + ", longueur=" + longueur + ", largeur=" + largeur + ", nbBoucles="
				+ nbBoucles + ", diametreBoucle=" + diametreBoucle + ", angleRot=" + angleRot + ", ressort=" + ressort
				+ ", ressortTransfo=" + ressortTransfo + ", constanteRappel=" + constanteRappel + "]";
	}
	
	
}

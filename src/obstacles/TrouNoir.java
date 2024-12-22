package obstacles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

import composantDeJeu.Balle;
import composantDeJeu.Table;
import interfaces.Dessinable;
import interfaces.Selectionnable;
import math.vecteurs.Vecteur3D;
import physique.MoteurPhysique;
/**
 * Classe permettant de simuler un trou noir approximatif comme un obstacle.
 * TrouNoir implémente sélectionnable et dessinable.
 * 
 * @author Aimé Melançon
 */
public class TrouNoir extends Obstacle implements Selectionnable, Dessinable, Serializable {
	/** Coefficient de sérialisation pour les fichiers. **/
	private static final long serialVersionUID = 1L ;
	/** Le rayon du trou noir**/
	private double diamDuGrandCercle ;
	/** Le rayon de la bouche du trou noir **/
	private double diamDuPetitCercle= diamDuGrandCercle/2;
	/** La masse initiale du trou noir **/
	private double masseDuTrouNoir;
	/** La variable qui permet de dessiner la bouche du Trou noir. **/
	private Ellipse2D boucheTrouNoir;
	/** La variable qui permet de dessiner le contour du Trou noir. **/
	private Ellipse2D contourTrouNoir;
	/** Permet de créer la forme  pour produire le trou Noir. **/
	private transient Shape trouNoir;
	/** Partie du trou noir dont on a soustrait le trou. **/
	private transient Area contour ;
	/** Permet de créer une aire pour produire la bouche du trou noir. **/
	private transient Area bouche;
	/**Matrice permettant de gérer le position milieu de la figure. **/
	private AffineTransform mat = new AffineTransform();

	/**
	 * Constructeur permettant de créer un trou noir.
	 * 
	 * @param positionDuTrouNoir La postion du trou noir dans la table
	 * @param diamDuTrouNoir Le diamètre du trou noir (m)
	 * @param masseDuTrouNoir La masse du trou noir (kg)
	 * @param couleur La couleur du contour du trou noir;
	 */
	//Aimé Melançon
	public TrouNoir(Vecteur3D positionDuTrouNoir, double diamDuTrouNoir, double masseDuTrouNoir, Color couleur ) {
		super(positionDuTrouNoir, couleur);
		this.diamDuGrandCercle= diamDuTrouNoir;
		this.masseDuTrouNoir = masseDuTrouNoir;
		creerLaGeometrie();
	}

	/**
	 * Retourne vrai si le point passé en paramètre fait partie du trou noir dessinable
	 * sur lequel cette methode sera appelée
	 *
	 *@param x Coordonnée en x du point (exprimé en mètres) 
	 *@param y Coordonnée en y du point (exprimé en mètres)
	 */
	//Aimé Melançon
	@Override
	public boolean contient(double x, double y) {
		return trouNoir.contains(x, y);
	
	}

	/**
	 * Méthode public pour créer les formes qui composent TrouNoir
	 * Cette méthode doit être appelée de nouveau chaque fois que sa position ou dimension est modifiée
	 */
	//Aimé Melançon
	public void creerLaGeometrie() {
		diamDuPetitCercle= diamDuGrandCercle/3;
		double x = this.position.getX()+diamDuGrandCercle/2;
		double y = this.position.getY()+ diamDuGrandCercle/2;

		contourTrouNoir = new Ellipse2D.Double(x, y, diamDuGrandCercle, diamDuGrandCercle);

		mat = new AffineTransform() ;
		mat.translate(-diamDuGrandCercle, -diamDuGrandCercle);

		boucheTrouNoir= new Ellipse2D.Double(x+diamDuPetitCercle, y+diamDuPetitCercle, diamDuPetitCercle, diamDuPetitCercle);
		
		bouche = new Area(boucheTrouNoir);

		contour = new Area() ;
		contour = new Area(contourTrouNoir);
		trouNoir = mat.createTransformedShape(contour);
		contour.subtract(bouche);
	}

	/**
	 * Permet de dessiner le trou noir, sur le contexte graphique passe en parametre.
	 * @param g2d contexte graphique.
	 */
	//Aimé Melançon
	@Override
	public void dessiner(Graphics2D g2d) {

		Graphics2D g2dPrive = (Graphics2D) g2d.create();
		
		if (mat != null) {
			g2dPrive.setColor(Color.BLACK);
			g2dPrive.fill(mat.createTransformedShape(bouche));
			g2dPrive.setColor(this.couleur);
			g2dPrive.fill(mat.createTransformedShape(contour));
		}


		//g2dPrivee.drawImage(...);
	}

	/**Methode permettant de connaitre la grandeur du diamètre du trou noir.
	 * 
	 * @return Retourne le diamètre du trou noir.
	 */
	//Aimé Melançon
	public double getDiamDuTrouNoir() {
		return diamDuGrandCercle;
	}

	/**Méthode permettant de choisir la grandeur du rayon du trou noir.
	 * 
	 * @param rayon La grandeur du nouveau diamètre du trou noir.
	 */
	//Aimé Melançon
	public void setDiamDuTrouNoir(double rayon) {
		this.diamDuGrandCercle = rayon;
		creerLaGeometrie();
	}
	/**Méthode pour déterminer la masse du trou noir.
	 * 
	 * @return Retourne la masse actuel du trou noir.
	 */
	public double getMasseDuTrouNoir() {
		return masseDuTrouNoir;
	}
	/**Méthode permettant de changer la masse du trou noir.
	 * 
	 * @param masseDuTrouNoir La masse du trou noir souhaité.
	 */
	//Aimé Melançon
	public void setMasseDuTrouNoir(double masseDuTrouNoir) {
		this.masseDuTrouNoir = masseDuTrouNoir;
	}

	/**Méthode permettant de déterminer si la balle est dans le trou noir.
	 * 
	 * @param balle La balle reçu.
	 * @return Retourne si la balle est oui ou non dans le trou noir.
	 **/
	//Aimé Melançon
	@Override
	public boolean intersection(Balle balle) {

		//Vecteur3D vecPositionBalle = new Vecteur3D(balle.obtenirCentreX(),balle.obtenirCentreY());



		//Vecteur3D distance = super.getPosition().soustrait(vecPositionBalle);
		//double normeDistance = distance.module();

		Area trouNoirCopie = new Area(trouNoir);
		Area balleCopie = new Area(balle.getAireBalle());
		trouNoirCopie.intersect(balleCopie);


		if(!trouNoirCopie.isEmpty()) {
			return true;
		}else {
			return false;
		}
	}


	/**Méthode permettant de savoir où dans le trou noir est la balle.
	 * 
	 * @param Balle la balle reçu.
	 * @return Retourne l'effet en question du trou noir qui est soit une force ou soit faire disparaitre la balle
	 * @throws Exception L'exception de la normalisation d'un vecteur.
	 */
	//Aimé Melançon
	public  boolean[] gererEffetDuTrouNoir(Balle balle) throws Exception {

		Vecteur3D vecPosition = balle.getPosition();

		boolean[] interieurDuTrouNoir = {false,false};
		Vecteur3D distance = super.getPosition().soustrait(vecPosition);
		double normeDistance = distance.module();
		if(normeDistance < diamDuGrandCercle/2) {
			interieurDuTrouNoir[0]=true;
		}else if(normeDistance< diamDuPetitCercle/2){
			interieurDuTrouNoir[1]=true;
		}
		return interieurDuTrouNoir;

	}
	/**Méthode permettant de déterminer la collision de la balle et le trou noir.
	 * 
	 * @param Balle La balle qui entre en collision avec le trou noir
	 * @return La force gravitationnelle général appliqué sur la balle ou téléporter la balle à sa position originelle.
	 * @throws Exception L'exception de la normalisation d'un vecteur.
	 */
	//Aimé Melançon
	@Override
	public void collision(Balle balle) throws Exception {

		Vecteur3D vecPositionBalle = new Vecteur3D(balle.obtenirCentreX(),balle.obtenirCentreY());
		Vecteur3D distanceVec = vecPositionBalle.soustrait(new Vecteur3D(this.position.getX()+diamDuGrandCercle/2, this.position.getY()+ diamDuGrandCercle/2));
		Vecteur3D distanceVecUnitaire =distanceVec.normalise();
		double normedistance = distanceVec.module();

		/*boolean intersecte[]= GererEffetDuTrouNoir(balle);

		if(intersecte[0]==true && intersecte[1]==false) {
			return  MoteurPhysique.forceGravitationnelleGenerale( masseDuTrouNoir,  balle.getMasse(), distance, distanceVecUnitaire);
		}else if(intersecte[0]==true && intersecte[1]==true) {
			balle.setPosition(Table.getPOS_INIT_BALLE());
			return VEC_ZERO;
		}else {
			return VEC_ZERO;
		}*/
		Area boucheTrouNoirCopie = new Area(mat.createTransformedShape(bouche));
		Area balleCopie = new Area(balle.getAireBalle());
		boucheTrouNoirCopie.intersect(balleCopie);


		if(!boucheTrouNoirCopie.isEmpty()) {
			System.out.println("Hop une téléportation");
			balle.setPosition(Table.getPOS_INIT_BALLE());
			balle.setVitesse(balle.getVitesse().multiplie(-1)) ;
			//return VEC_ZERO;
		}else {
			System.out.println("Effet de force gravitationnelle générale");

			balle.ajouterASommeDesForces( MoteurPhysique.forceGravitationnelleGenerale( masseDuTrouNoir,  balle.getMasse(), normedistance, distanceVecUnitaire));
			//return  MoteurPhysique.forceGravitationnelleGenerale( masseDuTrouNoir,  balle.getMasse(), normedistance, distanceVecUnitaire);
		}
	}
	/**
	 * Méthode permettant de connaître les caractéristique d'un obstacle de type trou noir.
	 */
	//Aimé Melançon
	@Override
	public String toString() {
		return "{Un Trou noir} [Rayon : "+this.diamDuGrandCercle +" m ; Position : ("+ super.getPosition().getX() + ","+ super.getPosition().getY()+")"+
				" ; Masse : "+ masseDuTrouNoir+" kg.]";

	}
	/**Méthode non utilisé, car le trou noir n'a pas d'animation.
	 * @param deltaT le temps
	 */
	//Aimé Melançon
	@Override
	public void avancerUnPas(Double deltaT) {
		// TODO Auto-generated method stub

	}
	/**
	 * Méthode permettant de faire un déplacement d'un trou noir sur la table.
	 * @param deplacement Le déplacement de l'obstacle effectué.
	 */
	//Aimé Melançon
	@Override
	public void setDeplacement(Vecteur3D deplacement) {
		this.setPosition(this.position.additionne(deplacement));
		
	}



}
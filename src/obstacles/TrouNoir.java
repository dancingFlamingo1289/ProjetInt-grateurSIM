package obstacles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;

import composantDeJeu.Balle;
import interfaces.Dessinable;
import interfaces.Selectionnable;
import math.vecteurs.Vecteur3D;
import outils.GererSon;
import outils.OutilsSon;
import physique.MoteurPhysique;
import scene.Scene;
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
	/**Matrice permettant de gérer la position milieu de la figure. **/
	private AffineTransform mat = new AffineTransform();
	/** La position où la balle va être téléporter sur la table si celle-ci rentre dans la bouche du trou noir.**/
	private final Vecteur3D POSITION_TELEPORTEE_BALLE= new Vecteur3D(60,20);
	/** Constante du nombre de points ajoutés lors d'une collision avec un trou noir**/
	private final int POINTS_AJOUTES = 7;
	/** OutilsSon de l'obstacle**/
	private transient OutilsSon sonObst= new OutilsSon();
	/**OutilsSon pour le trou du trou noir. **/
	private transient OutilsSon sonObstrou= new OutilsSon();
	/**Nom du fichier du son**/
	private final String NOM_DU_FICHIER="TrouNoir.wav";
	/**Nom du fichier du son**/
	private final String NOM_DU_FICHIER_TROU="Teleportation2.wav";
	/**Seulement exécuté la première fois que l'application est lancée **/
	private boolean premiereFois=true;
	
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
	 * Méthode pour créer les formes qui composent TrouNoir
	 * Cette méthode doit être appelée de nouveau chaque fois que sa position ou dimension est modifiée
	 */
	//Aimé Melançon
	protected void creerLaGeometrie() {
		if (premiereFois) {
			sonObst = new OutilsSon() ;
			sonObst.chargerUnSonOuUneMusique(NOM_DU_FICHIER);
			sonObstrou = new OutilsSon() ;
			sonObstrou.chargerUnSonOuUneMusique(NOM_DU_FICHIER_TROU);
			premiereFois=false;
		}
		
		diamDuPetitCercle= diamDuGrandCercle/3;
		double x = this.position.getX()+diamDuGrandCercle/2;
		double y = this.position.getY()+ diamDuGrandCercle/2;

		contourTrouNoir = new Ellipse2D.Double(x, y, diamDuGrandCercle, diamDuGrandCercle);

		mat = new AffineTransform() ;
		mat.translate(-diamDuGrandCercle, -diamDuGrandCercle);

		boucheTrouNoir= new Ellipse2D.Double(x+diamDuPetitCercle, y+diamDuPetitCercle, diamDuPetitCercle, diamDuPetitCercle);

		//if (bouche == null)
		bouche = new Area(boucheTrouNoir);

		//if (contour == null)
		contour = new Area(contourTrouNoir);
		trouNoir = mat.createTransformedShape(contour);
		contour.subtract(bouche);
	}

	/**
	 * Permet de dessiner le trou noir, sur le contexte graphique passé en paramètre.
	 * @param g2d contexte graphique.
	 */
	//Aimé Melançon
	@Override
	public void dessiner(Graphics2D g2d) {

		Graphics2D g2dPrive = (Graphics2D) g2d.create();

		if (mat == null || bouche == null || contour == null)
			creerLaGeometrie() ;

		if (mat != null) {
			g2dPrive.setColor(Color.BLACK);
			g2dPrive.fill(mat.createTransformedShape(bouche));
			g2dPrive.setColor(this.couleur);
			g2dPrive.fill(mat.createTransformedShape(contour));
			g2d.draw(mat.createTransformedShape(contour));

		}
	}

	/**
	 * Méthode permettant de connaître la grandeur du diamètre du trou noir.
	 * 
	 * @return Retourne le diamètre du trou noir.
	 */
	//Aimé Melançon
	public double getDiamDuTrouNoir() {
		return diamDuGrandCercle;
	}

	/**
	 * Méthode permettant de choisir la grandeur du rayon du trou noir.
	 * @param rayon La grandeur du nouveau diamètre du trou noir.
	 */
	//Aimé Melançon
	public void setDiamDuTrouNoir(double rayon) {
		this.diamDuGrandCercle = rayon;
		creerLaGeometrie();
	}
	/**
	 * Méthode pour déterminer la masse du trou noir.
	 * @return Retourne la masse actuelle du trou noir.
	 */
	public double getMasseDuTrouNoir() {
		return masseDuTrouNoir;
	}
	
	/**
	 * Méthode permettant de changer la masse du trou noir.
	 * @param masseDuTrouNoir La masse souhaitée du trou noir.
	 */
	//Aimé Melançon
	public void setMasseDuTrouNoir(double masseDuTrouNoir) {
		this.masseDuTrouNoir = masseDuTrouNoir;
		creerLaGeometrie();
	}

	/**
	 * Méthode permettant de déterminer si la balle est dans le trou noir.
	 * @param balle La balle reçue.
	 * @return Retourne si la balle est dans le trou noir ou non.
	 **/
	//Aimé Melançon
	@Override
	public boolean intersection(Balle balle) {
		Area trouNoirCopie = new Area(trouNoir);
		Area balleCopie = new Area(balle.getAireBalle());
		trouNoirCopie.intersect(balleCopie);
		return !trouNoirCopie.isEmpty();
	}

	/**
	 * Méthode permettant de déterminer la collision de la balle et du trou noir.
	 * 
	 * @param balle La balle qui entre en collision avec le trou noir
	 * @throws Exception L'exception de la normalisation d'un vecteur.
	 */
	//Aimé Melançon
	@Override
	public void collision(Balle balle) throws Exception {

		Vecteur3D vecPositionBalle = new Vecteur3D(balle.obtenirCentreX(),balle.obtenirCentreY());

		if (position == null)
			creerLaGeometrie() ;

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
			if (sonObstrou == null) {
				premiereFois = true ;
				sonObstrou = new OutilsSon() ;
				creerLaGeometrie() ;
			}
			if(GererSon.isAllumerFermer()) {
			sonObstrou.jouerUnSon();
			}
			System.out.println("Hop une téléportation");
			balle.setPosition(POSITION_TELEPORTEE_BALLE);
			balle.setVitesse(balle.getVitesse().multiplie(-1)) ;
			//return VEC_ZERO;
		}else {
			if (sonObst == null) {
				premiereFois = true ;
				sonObst = new OutilsSon() ;
				creerLaGeometrie() ;
			}

			if(GererSon.isAllumerFermer()) {
				sonObst.jouerUnSon();
				}

			System.out.println("Effet de force gravitationnelle générale");

			balle.setForceGravGenerale(MoteurPhysique.forceGravitationnelleGenerale(masseDuTrouNoir, balle.getMasse(), normedistance, distanceVecUnitaire));

			//return  MoteurPhysique.forceGravitationnelleGenerale( masseDuTrouNoir,  balle.getMasse(), normedistance, distanceVecUnitaire);

			 if (!(Scene.getPoints() == null))Scene.getPoints().ajouterPoints(POINTS_AJOUTES);
		}
	}
	
	/**
	 * Méthode permettant de connaître les caractéristiques d'un obstacle de type trou noir.
	 */
	//Aimé Melançon
	@Override
	public String toString() {
		return "{Un Trou noir} [Rayon : "+this.diamDuGrandCercle +" m ; Position : ("+ super.getPosition().getX() + ","+ super.getPosition().getY()+")"+
				" ; Masse : "+ masseDuTrouNoir+" kg.]";

	}
	
	/**
	 * Méthode non utilisée, car le trou noir n'a pas d'animation.
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
	
	/**
	 * Méthode non utilisée, car le trou noir ne possède aucune animation à réinitialiser.
	 */
	//Aimé Melançon
	@Override
	public void reinitialiser() {
		// TODO Auto-generated method stub

	}
	/**
	 * Méthode permettant de savoir s'il y a une intersection avec le trou noir
	 * @param obst L'obstacle qui est possiblement en intersection
	 */
	//Aimé Melançon
	@Override
	public boolean intersection(Obstacle obst) {
		
		Area aireTrouNoirCopie = new Area(getAire());
		Area aireObstacleCopie = new Area(obst.getAire());
		aireTrouNoirCopie.intersect(aireObstacleCopie);

		return !aireTrouNoirCopie.isEmpty() ;
	}
	
	/**
	 * Méthode permettant d'avoir l'aire d'un trou noir
	 * @return Retourne l'aire du trou noir
	 */
	//Aimé Melançon
	@Override
	public Area getAire() {
		// TODO Auto-generated method stub
		Area aireTrouNoir = new Area(trouNoir);
		return aireTrouNoir;
	}
}
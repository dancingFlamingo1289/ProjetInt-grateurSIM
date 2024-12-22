package composantDeJeu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;
import gestionCollision.CollisionDesObjets;
import interfaces.Dessinable;
import math.vecteurs.Vecteur3D;
import physique.MoteurPhysique;

/**
 * Classe qui crée le flipper permettant de frapper la balle en tapant sur une touche du clavier
 * @author Félix Lefrançois
 */
public class Flipper implements Dessinable, Serializable  {
	/** Coefficient de sérialisation pour les fichiers. **/
    private static final long serialVersionUID = 1L;
	/** String déterminant si le flipper frappe par la droite ou la gauche**/
	private String toucheDuFlipper;
	/** Constantes de la longueur et la hauteur du flipper **/
	private final double LONGUEUR_BARRE = 30, HAUTEUR_BARRE = 5;
	/** Ellispe de la base du flipper **/
	private Ellipse2D.Double baseFlipper;
	/** La barre composant le flipper**/
	private Rectangle2D.Double barreFlipper;
	/** La constante du diamètre de la base du flipper**/
	private final double DIAM_BASE = 10;
	/** Les coordonnées du coin centre de la base du flipper**/
	private double positionX, positionY;
	/** L'angle de repos du flipper **/
	private double angleRepos;
	/** Constante de l'angle de repos gauche de la barre de flipper (sera additionnée à l'angle de rotation)**/
	private final double ANGLE_REPOS_GAUCHE = Math.PI/6;
	/** Constante de l'angle de repos droit de la barre de flipper (sera additionnée à l'angle de rotation)**/
	private final double ANGLE_REPOS_DROIT = 5*Math.PI/6;
	/** Constante de l'angle final droit de la rotation lorsque cliqué**/
	private final double ANGLE_FINAL_DROIT = 5*Math.PI/4;
	/** Constante de l'angle final gauche de la rotation lorsqu cliqué **/
	private final double ANGLE_FINAL_GAUCHE = -Math.PI/4;
	/** L'angle de rotation du flipper à partir du centre**/
	private double rotation;
	/** La couleur du flipper **/
	private Color couleurFlipper = Color.GRAY;
	/** Les aires des différents composants du flipper **/
	private transient Area aireBarre, aireBase;
	/** l'aire total du flipper**/
	private transient Area aireTotal;
	/** Booléen confirmant si le flipper est en animation **/
	private boolean bouge = false;
	/** La masse d'un flipper **/
	private final int MASSE = 450;
	/** Les murs qui composent la barre du flipper **/
	private Line2D.Double murHaut, murCote,murBas;
	/** Un array list contenant les murs **/
	private CopyOnWriteArrayList<Line2D.Double> lesMurs;
	/** La rotation du flipper en radian à un moment précis **/
	private double rotationDuMoment;
	/** Le rectangel résultatnt de la transformation de la barre du flipper **/
	private Shape rectangleTransfo;
	/** L'accélération angulaire du flipper **/
	private double accelAngu = -20;
	/** La vitesse angulaire du flipper **/
	private double vitesseAngulaire = 0;
	/** Un booléen confirmant à quelle phase de l'animation se trouve la rotation du flipper **/
	private boolean changementFait = false;
	
	/**
	 * Constructeur de la classe Flipper
	 * @param toucheDuFlipper Détermine si l'animation se fait avec la touche de droite ou la touche de gauche
	 * @param posX La coordonnée en x du centre de la base
	 * @param posY La coordonnée en y du centre de la base
	 * @param rotation la rotation en radian du flipper
	 */
	//Félix Lefrançois
	public Flipper(String toucheDuFlipper, double posX, double posY, double rotation) {
		this.toucheDuFlipper = toucheDuFlipper;
		this.positionX = posX;
		this.positionY = posY;
		this.rotation = rotation;
		lesMurs = new CopyOnWriteArrayList<Line2D.Double>();
		determinerCote();
		creerLaGeometrie();
	}
	
	/**
	 * Méthode qui crée la géométrie du flipper
	 */
	//https://stackoverflow.com/questions/41898990/find-corners-of-a-rotated-rectangle-given-its-center-point-and-rotation
	//Le code a été légèrement modifiée puisqu'il s'agit d'un calcul des position à partir du centre d'un rectangle ce qui
	//n'est pas comment le flipper est créé
	//elle n'affecte en rien la charge de travail planifiée
	//Félix Lefrançois
	public void creerLaGeometrie() {
		lesMurs.clear();
		
		baseFlipper = new Ellipse2D.Double(positionX-DIAM_BASE/2,positionY-DIAM_BASE/2,DIAM_BASE,DIAM_BASE);
		aireBase = new Area(baseFlipper);
		
		barreFlipper = new Rectangle2D.Double(0,0-HAUTEUR_BARRE/2,LONGUEUR_BARRE,HAUTEUR_BARRE);
		
		AffineTransform mat = new AffineTransform();
		mat.translate(positionX, positionY);
		mat.rotate(rotationDuMoment);
		rectangleTransfo = mat.createTransformedShape(barreFlipper);
		aireBarre = new Area(rectangleTransfo);
		
		aireTotal = new Area();
		aireTotal.add(aireBase);
		aireTotal.add(aireBarre);
		
		//Voir entête de méthode
		murHaut = new Line2D.Double((positionX)-(HAUTEUR_BARRE/2 *Math.sin(rotationDuMoment)),
				(positionY)+(HAUTEUR_BARRE/2 *Math.cos(rotationDuMoment)),
				(positionX)+(LONGUEUR_BARRE*Math.cos(rotationDuMoment))-(HAUTEUR_BARRE/2 *Math.sin(rotationDuMoment)),
				(positionY)+(LONGUEUR_BARRE*Math.sin(rotationDuMoment))+(HAUTEUR_BARRE/2 *Math.cos(rotationDuMoment)));
		lesMurs.add(murHaut);
		murBas = new Line2D.Double((positionX)+(HAUTEUR_BARRE/2 *Math.sin(rotationDuMoment)),
				(positionY)-(HAUTEUR_BARRE/2 *Math.cos(rotationDuMoment)),
				(positionX)+(LONGUEUR_BARRE*Math.cos(rotationDuMoment))+(HAUTEUR_BARRE/2 *Math.sin(rotationDuMoment)),
				(positionY)+(LONGUEUR_BARRE*Math.sin(rotationDuMoment))-(HAUTEUR_BARRE/2 *Math.cos(rotationDuMoment)));
		lesMurs.add(murBas);
		murCote = new Line2D.Double((positionX)+(LONGUEUR_BARRE*Math.cos(rotationDuMoment))-(HAUTEUR_BARRE/2 *Math.sin(rotationDuMoment)),
				positionY+(LONGUEUR_BARRE*Math.sin(rotationDuMoment))+(HAUTEUR_BARRE/2 *Math.cos(rotationDuMoment)),
				(positionX)+(LONGUEUR_BARRE*Math.cos(rotationDuMoment))+(HAUTEUR_BARRE/2 *Math.sin(rotationDuMoment)),
				positionY+(LONGUEUR_BARRE*Math.sin(rotationDuMoment))-(HAUTEUR_BARRE/2 *Math.cos(rotationDuMoment)));
		lesMurs.add(murCote);
	}
	
	/**
	 * Méthode permettant de dessiner un flipper
	 * @param g2d Contexte graphique
	 */
	//Félix Lefrançois
	public void dessiner(Graphics2D g2d) {
		Graphics2D g2dPrive = (Graphics2D) g2d.create();
		
		g2dPrive.setColor(couleurFlipper);
		g2dPrive.fill(aireTotal);
	}
	
	/**
	 * Méthode pour déterminer et appliquer les changements selon le côté du flipper
	 */
	//Félix Lefrançois
	public void determinerCote() {
		if (toucheDuFlipper.equalsIgnoreCase("Gauche")) {
			angleRepos = ANGLE_REPOS_GAUCHE;
			rotationDuMoment = ANGLE_REPOS_GAUCHE + rotation;
		} else if (toucheDuFlipper.equalsIgnoreCase("Droit")) {
			angleRepos = ANGLE_REPOS_DROIT;
			rotationDuMoment = ANGLE_REPOS_DROIT - rotation;
			accelAngu = 20;
		}
	}
	
	/**
	 * Néthode permettant de vérifier si une balle entre en collision avec un flipper
	 * @param balle La balle dont on vérifie la collision avec le flipper
	 * @return Un booléen confirmant si il y a collision
	 */
	//Félix Lefrançois
	public boolean verifierCollisionFlipper(Balle balle) {
		boolean verification = false;
		Area aireFlipper = new Area(aireTotal);
		Area aireBalle = new Area(balle.getAireBalle());
		
		aireFlipper.intersect(aireBalle);
		if (!aireFlipper.isEmpty()) {
			verification = true;
		}
		
		return verification;
	}
	
	/**
	 * Méthode permettant d'appliquer les changements à une balle lors d'une collision avec le flipper
	 * @param balle La balle qui entre en collision avec le flipper
	 * @throws Exception Si le vecteur ne peut être normalisée si il est de longueur trop petite ou nulle
	 */
	//Félix Lefrançois
	public void appliquerCollisionFlipper(Balle balle) throws Exception {
		Area aireBarreTemp = new Area(aireBarre);
		Area aireBaseTemp = new Area(aireBase);
		Area aireBalleTemp = new Area(balle.getAireBalle());

		aireBarreTemp.intersect(aireBalleTemp);
		aireBaseTemp.intersect(aireBalleTemp);
		if (!bouge) {
			if(!aireBaseTemp.isEmpty()) {
				balle.setVitesse(CollisionDesObjets.vitesseFinaleMouvementCercleImmobile(balle, 
					MASSE, positionX+DIAM_BASE/2, positionY+DIAM_BASE/2));
				Vecteur3D normale = CollisionDesObjets.trouverNormaleCollisionCercle(balle, positionX+DIAM_BASE/2, positionY+DIAM_BASE/2);
				balle.setPosition(balle.getPosition().additionne(normale));
			} else if(Table.verifierCollisionAvecMurs(balle, lesMurs)) {
				Line2D.Double murVerif = Table.trouverMurLePlusProche(balle,lesMurs);
				Vecteur3D normale = CollisionDesObjets.trouverNormaleCollisionMur(balle, murVerif);
				balle.setVitesse(CollisionDesObjets.vitesseFinaleImmobile(balle.getVitesse(), normale));
				balle.setPosition(balle.getPosition().additionne(normale));
			} 
		}else if (bouge) {
			double deltaX = balle.obtenirCentreX()-positionX;
			double deltaY = balle.obtenirCentreY()-positionY;
			Vecteur3D vecBaseBalle = new Vecteur3D(deltaX,deltaY,0);
				
		    Line2D.Double murVerif = Table.trouverMurLePlusProche(balle, lesMurs);
		    Vecteur3D directeur = new Vecteur3D(murVerif.x2-murVerif.x1,murVerif.y2-murVerif.y1,0);
		        
		    Vecteur3D projection = Vecteur3D.projectionOrthogonale(vecBaseBalle, directeur);
		    double rayon= projection.module();
		        
		    double vitesse = vitesseAngulaire*rayon;
		    Vecteur3D vecteurVitesse = new Vecteur3D(-vitesse*Math.sin(rotationDuMoment),vitesse*Math.cos(rotationDuMoment),0);
		    System.out.println(vecteurVitesse);
		        
		    Vecteur3D normale = CollisionDesObjets.trouverNormaleCollisionMur(balle, murVerif);
		        
		    balle.setVitesse(CollisionDesObjets.vitesseFinaleCollisionAvecMurAmovible(balle, normale, vecteurVitesse, MASSE));
		    balle.setPosition(balle.getPosition().additionne(normale));
		}
	}
	/**
	 * Méthode permettant d'effectuer le déplacement d'un flipper en fonction du temps simulé
	 * @param deltaT La différence de temps simulé
	 */
	//Félix Lefrançois
	public void avancerUnPas(double deltaT) {
		if (bouge) {
		    vitesseAngulaire = MoteurPhysique.vitesseAngulaire(vitesseAngulaire, accelAngu, deltaT);
		    rotationDuMoment = MoteurPhysique.positionAngu(rotationDuMoment, vitesseAngulaire, accelAngu, deltaT);
		    gererAcceleration();
		    creerLaGeometrie();
		}    
	}
	
	/**
	 * Méthode permettant de changer l'accélération en fonction de l'angle de rotation du flipper
	 */
	//Félix Lefrançois
	public void gererAcceleration() {
		if (((rotationDuMoment < ANGLE_FINAL_GAUCHE && toucheDuFlipper.equalsIgnoreCase("Gauche")) 
				|| (rotationDuMoment > ANGLE_FINAL_DROIT && toucheDuFlipper.equalsIgnoreCase("Droit"))) && !changementFait) {
			vitesseAngulaire = 0;
			accelAngu = accelAngu*-1;
			changementFait = true;
		} else if (((rotationDuMoment > ANGLE_REPOS_GAUCHE && toucheDuFlipper.equalsIgnoreCase("Gauche")) 
				|| (rotationDuMoment < ANGLE_REPOS_DROIT && toucheDuFlipper.equalsIgnoreCase("Droit"))) && changementFait) {
			rotationDuMoment = angleRepos + rotation;
			vitesseAngulaire = 0;
			accelAngu = accelAngu*-1;
			bouge = false;
			changementFait = false;
		}
	}

	/**
	 * Méthode qui réinitialise un flipper à son état initial
	 */
	//Félix Lefrançois
	public void reinitialiser() {
		rotationDuMoment = angleRepos + rotation;
		vitesseAngulaire = 0;
		determinerCote();
		bouge = false;
		changementFait = false;
		creerLaGeometrie();
	}
	
	/**
	 * Méthode qui déclenche l'animation d'un flipper
	 */
	public void commencerAnimation() {
		bouge = true;
	}
	
	/**
	 * Méthode retournant la touche à laquelle le flipper est associé
	 * @return La côté du flipper à partir duquel le joueur frappe
	 */
	//Félix Lefrançois
	public String getToucheDuFlipper() {
		return toucheDuFlipper;
	}

	/**
	 * Méthode changeant le côté à partir duquel le flipper frappe
	 * @param toucheDuFlipper Le nouveau côté du flipper pour la frappe
	 */
	//Félix Lefrançois
	public void setToucheDuFlipper(String toucheDuFlipper) {
		this.toucheDuFlipper = toucheDuFlipper;
	}

	/**
	 * Méthode retournant le coin superieur gauche en x du flipper
	 * @return Le coin superieur gauche en x du flipper
	 */
	//Félix Lefrançois
	public double getPositionX() {
		return positionX;
	}

	/**
	 * Méthode changeant le centre en x du flipper
	 * @param positionX Le nouveau coin superieur gauche en x du flipper
	 */
	//Félix Lefrançois
	public void setPositionX(double positionX) {
		this.positionX = positionX;
	}

	/**
	 * Méthodee retournant le coin superieur gauche en y du flipper
	 * @return Le coin superieur gauche en y du flipper
	 */
	//Félix Lefrançois
	public double getPositionY() {
		return positionY;
	}

	/**
	 * Méthode changeant la position en y du flipper
	 * @param positionY Le nouveau coin superieur gauche en y du flipper
	 */
	//Félix Lefrançois
	public void setPositionY(double positionY) {
		this.positionY = positionY;
	}

	/**
	 * Méthode retournant l'angle de rotation (à partir du centre de la base) du flipper
	 * @return L'angle de rotation du flipper
	 */
	//Félix Lefrançois
	public double getRotation() {
		return rotation;
	}

	/**
	 * Méthode changeant l'angle de rotation (à partir du centre de la base) du flipper
	 * @param rotation Le nouvel angle de rotation du flipper
	 */
	//Félix Lefrançois
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
}

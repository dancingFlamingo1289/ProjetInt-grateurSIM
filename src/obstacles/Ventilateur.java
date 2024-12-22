package obstacles;

import java.awt.* ;
import java.awt.geom.* ;
import composantDeJeu.* ;
import interfaces.* ;
import math.vecteurs.* ;
import outils.GererSon;
import outils.OutilsSon;
import scene.Scene;

/**
 * Cette classe représente un ventilateur donnant une force de poussée à une balle.
 * @author Aimé Melançon
 * @author Félix Lefrançois
 */
public class Ventilateur extends Obstacle implements Dessinable, Selectionnable {
	/** Coefficient de sérialisation pour les fichiers. **/
	private static final long serialVersionUID = 1L ;
	/**Variable de type double qui a l'orientation du ventilateur. **/
	private double orientation;
	/**Variable de type double qui a la hauteur du ventilateur. **/
	private double hauteur;
	/**Variable de type double qui a la largeur du ventilateur. **/
	private double largeur;
	/**Variable contenant la boite du ventilateur. **/
	private Rectangle2D.Double corpsVentilo;
	/**Variable contenant la grille du ventilateur. **/
	private Rectangle2D.Double grilleVentilo;
	/**Variable permettant de créer le grillage verticalement. **/
	private Line2D.Double elementGrilleVerticale;
	/**Variable permettant de créer le grillage horizontalement. **/
	private Line2D.Double elementGrilleHorizontale;
	/**Variable permettant de créer l'aire du ventilateur. **/
	private transient Area ventilateur;
	/**Matrice permettant de gérer l'orientation de la figure. **/
	private AffineTransform mat = new AffineTransform();
	/** Le path2D utilisé pour dessiner la zone d'effet du ventilateur **/
	private Path2D.Double chemin;
	/** L'aire de la zone d'effet (vent) du ventilateur**/
	private transient Area aireZone;
	/** Les limites de la zone de vent**/
	private final double LIMITE_VENT_X = 20, LIMITE_VENT_Y = 10;
	/** La force de poussée du ventilateur**/
	private double forceVentilateur;
	/** Le mur servant à la collision du ventilateur**/
	private Mur mur;
	/** L'aire totale (incluant le corps et la zone de vent) du ventilateur**/
	private transient Area aireTotal;
	/** Constante du nombre de points ajoutés lors d'une collision avec la zone de poussée du ventilateur**/
	private final int POINTS_AJOUTES = 8;
	/** OutilsSon de l'obstacle**/
	private transient OutilsSon sonObst= new OutilsSon();
	/**Nom du fichier du son**/
	private final String NOM_DU_FICHIER="Ventilateur.wav";
	/**L'itération entre chaque grille **/
	private final double ITERATION = 1.55;
	/**Seulement exécuté la première fois que l'application est lancé **/
	private boolean premiereFois=true;
	/**Trait pointillé **/
	private transient Stroke zonePointille ;
	/**Pour trouver l'angle max **/
	private double theta,alpha,beta,delta,rho;
	/**Le temps maximum que les baguettes peuvent bouger avant de changer de direction. **/
	private  double ticTacMax;
	/**Angle actuelle des baguettes **/
	private double  angleDroit=0,angleGauche=0;
	/**Un compteur pour savoir quand changer la rotation **/
	private int tictac=0;
	/** L'itération de rotation des baguettes**/
	private double omega=1;
	/**Les baguettes qui va fonctionner comme des oscillateurs **/
	private Line2D.Double baguetteGauche,baguetteDroite;
	/**La doublure de la matrice de transformation pour la baguette gauche et droite. **/
	private AffineTransform matCopieG,matCopieD;
	/**Constructeur permettant de créé un obstacle de type ventilateur.
	 * 
	 * @param position La position où est le ventilateur sur la table.
	 * @param hauteur La hauteur de grandeur du ventilateur.
	 * @param largeur La largeur du ventilateur.
	 * @param forceVentilateur la force de poussée du ventilateur (module en N)
	 * @param orientation L'orientation du ventilateur sur la table.
	 * @param couleur La couleur de la boite ayant le moteur du ventilateur.
	 */
	//Aimé Melançon
	public Ventilateur(Vecteur3D position,double hauteur, double largeur, double forceVentilateur, double orientation, Color couleur) {
		super(position, couleur) ;
		this.hauteur= hauteur;
		this.orientation= orientation;
		this.largeur =largeur;
		this.forceVentilateur = forceVentilateur;

		creerLaGeometrie();
	}

	/**
	 * Méthode public pour créer les formes qui composent Ventilateur
	 * Cette méthode doit être appelée de nouveau chaque fois que sa position ou dimension est modifiée
	 */
	//Aimé Melançon
	protected void creerLaGeometrie() {
		if (premiereFois) {
			sonObst = new OutilsSon() ;
			sonObst.chargerUnSonOuUneMusique(NOM_DU_FICHIER);
			premiereFois = false;


		}

		double xBoite = this.position.getX() + largeur/2;
		double yBoite = this.position.getY() + hauteur/2;

		corpsVentilo= new Rectangle2D.Double(xBoite, yBoite,  largeur, hauteur);

		double positionGrilleX = xBoite+largeur/2;
		
		grilleVentilo = new Rectangle2D.Double(positionGrilleX, yBoite, largeur/2, hauteur);
		
		mat = new AffineTransform() ;
		mat.rotate(orientation,this.position.getX(),this.position.getY());
		mat.translate(-largeur,-hauteur);
		ventilateur = new Area(mat.createTransformedShape(corpsVentilo));
		Area grille =new Area( mat.createTransformedShape(grilleVentilo));
		ventilateur.add(grille);
		matCopieG = new AffineTransform(mat);
		matCopieD =new AffineTransform(mat);
		mur = new Mur(position,hauteur, largeur,orientation,couleur);

		zoneEffetVentilateur();
		aireTotal = new Area(ventilateur);
		aireTotal.add(aireZone);

		zonePointille = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 
				0, new float[] {7.0f}, 0) ;

		double a = Math.sqrt(Math.pow(grilleVentilo.getMaxX()+LIMITE_VENT_X, 2)+Math.pow(grilleVentilo.getMinY()-LIMITE_VENT_Y, 2))-Math.sqrt( Math.pow(xBoite+largeur, 2)+Math.pow(yBoite+hauteur/2, 2));
		double b = Math.sqrt(Math.pow(grilleVentilo.getMaxX()+LIMITE_VENT_X, 2) )-Math.sqrt(Math.pow(xBoite+largeur, 2));
		
		theta= Math.asin(a/b);
		alpha = 90-theta;
		beta=alpha+90;
		delta=alpha/2;
		rho=180-delta-beta;
		
		ticTacMax=45*((90- rho)/90);
	
		baguetteDroite= new Line2D.Double(xBoite+largeur,yBoite+hauteur/2, grilleVentilo.getMaxX()+LIMITE_VENT_X, grilleVentilo.getMinY()-LIMITE_VENT_Y);
		baguetteGauche = new Line2D.Double(xBoite+largeur,yBoite+hauteur/2, grilleVentilo.getMaxX()+LIMITE_VENT_X,  grilleVentilo.getMaxY()+LIMITE_VENT_Y);
		matCopieG.rotate(Math.toRadians(angleDroit),xBoite+largeur,yBoite+hauteur/2);
		
		matCopieD.rotate(Math.toRadians(angleGauche),xBoite+largeur,yBoite+hauteur/2);
		
	}
	/**
	 * Méthode permettant de créer la zone où il y aura une poussée
	 */
	//Félix Lefrançois
	private void zoneEffetVentilateur() {
		chemin = new Path2D.Double();
		chemin.moveTo(grilleVentilo.getMaxX(), grilleVentilo.getMaxY());
		chemin.lineTo(grilleVentilo.getMaxX(), grilleVentilo.getMinY());
		chemin.lineTo(grilleVentilo.getMaxX()+LIMITE_VENT_X, grilleVentilo.getMinY()-LIMITE_VENT_Y);
		chemin.lineTo(grilleVentilo.getMaxX()+LIMITE_VENT_X, grilleVentilo.getMaxY()+LIMITE_VENT_Y);
		chemin.lineTo(grilleVentilo.getMaxX(), grilleVentilo.getMaxY());
		chemin.closePath();
		aireZone = new Area(mat.createTransformedShape(chemin));
	}

	/**
	 * Permet de dessiner le ventilateur, sur le contexte graphique passe en parametre.
	 * @param g2d contexte graphique.
	 */
	//Aimé Melançon
	@Override
	public void dessiner(Graphics2D g2d) {
		if (ventilateur == null)
			creerLaGeometrie() ;

		Graphics2D g2dPrivee = (Graphics2D) g2d.create();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON) ;
		g2dPrivee.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON) ;
		g2dPrivee.setColor(this.couleur);

		g2dPrivee.fill(mat.createTransformedShape(corpsVentilo));

		creerGrillage(g2dPrivee);
		g2dPrivee.setColor(Color.LIGHT_GRAY);
		g2dPrivee.setStroke(zonePointille);
		g2dPrivee.draw(aireZone);
		g2dPrivee.setColor(Color.BLACK);
		g2d.draw(mat.createTransformedShape(grilleVentilo));
		gestionBaguette(g2d);
		g2d.draw(ventilateur);


	}

	/**
	 * Permet de dessiner les deux baguettes.
	 * @param g2d contexte graphique
	 */
	//Aimé Melançon
	private void gestionBaguette(Graphics2D g2d) {
		Graphics2D g2dPrivee = (Graphics2D) g2d.create();
		
		g2dPrivee.draw(matCopieG.createTransformedShape(baguetteGauche));
		g2dPrivee.draw(matCopieD.createTransformedShape(baguetteDroite));
		
		
	}
	/**
	 * Permet de dessiner le grillage du ventilateur, sur le contexte graphique passe en parametre.
	 * @param g2d contexte graphique.
	 */
	//Aimé Melançon
	private void creerGrillage(Graphics2D g2dPrivee) {

		double xBoite = this.position.getX() + largeur/2;
		double yBoite = this.position.getY() + hauteur/2;
		double positionGrilleX = xBoite+largeur/2;
		double yMin= yBoite+hauteur;

		for(double i=ITERATION; i<largeur/2; i+=ITERATION) {

			elementGrilleVerticale= new Line2D.Double(positionGrilleX+i, yMin, positionGrilleX+i, yBoite);

			g2dPrivee.setColor(Color.GRAY);
			g2dPrivee.draw( mat.createTransformedShape(elementGrilleVerticale));
		}

		for(double i=ITERATION ; i<hauteur; i+=ITERATION) {

			elementGrilleHorizontale = new Line2D.Double(positionGrilleX, yMin-i,positionGrilleX+largeur/2 , yMin-i); 

			g2dPrivee.setColor(Color.GRAY);
			g2dPrivee.draw(mat.createTransformedShape(elementGrilleHorizontale));
		}

	}

	/**
	 * Retourne vrai si le point passé en paramètre fait partie du ventilateur dessinable.
	 * sur lequel cette methode sera appelée
	 *
	 *@param x Coordonnée en x du point (exprimé en mètres) 
	 *@param y Coordonnée en y du point (exprimé en mètres)
	 */
	//Aimé Melançon
	@Override
	public boolean contient(double x, double y) {
		return ventilateur.contains(x, y);
	}

	/**
	 * Méthode permettant d'appliquer les effets et la collision du ventilateur
	 * @param balle La balle qui entre en collision avec le ventilateur
	 */
	//https://danceswithcode.net/engineeringnotes/rotations_in_2d/rotations_in_2d.html
	//(J'utilise les équations 3 et 4 pour être précis)
	//J'ai utilisé les équations données dans la section "Rotating Points around an Arbitrary Center" du document
	//afin de déterminer les coordonnées du point centre du côté droit du ventilateur afin de créer mon vecteur directeur
	//et mon vecteur reliant ce même point à la balle.
	//J'utilise les équations sans rien modifier à part les variables utilisées et les parties avec les coordonnées en y
	//puisque ce sont les mêmes, donc cela donne toujours 0
	//Seule les équations sont utilisées, donc le code est copié à 0%
	//Cela n'affecte en rien ma charge de travail puisque cette tâche était une tâche que j'ai reçu d'Aimé
	//pour qu'il ne soit pas surchargé, cela n'a donc jamais été pris en compte dans ma charge de travail.
	@Override
	//Félix Lefrançois
	public void collision(Balle balle) {
		Area aireZoneTemp = new Area(aireZone);
		Area aireBalleTemp = new Area(balle.getAireBalle());

		aireZoneTemp.intersect(aireBalleTemp);

		if (mur.intersection(balle)) {
			try {
				mur.collision(balle);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (!aireZoneTemp.isEmpty()) {
			//Voir l'entête de méthode
			if (sonObst == null) {
				sonObst = new OutilsSon() ;
				premiereFois = true ;
				creerLaGeometrie() ;
			}

			if(GererSon.isAllumerFermer()) {
				sonObst.jouerUnSon();
			}

			Vecteur3D directeur = new Vecteur3D((position.getX()+Math.cos(orientation)*(grilleVentilo.getMaxX()-largeur+LIMITE_VENT_X-position.getX()))-
					(position.getX()+Math.cos(orientation)*(grilleVentilo.getMaxX()-largeur-position.getX())),
					(position.getY()+Math.sin(orientation)*(grilleVentilo.getMaxX()-largeur+LIMITE_VENT_X-position.getX()))
					-(position.getY()+Math.sin(orientation)*(grilleVentilo.getMaxX()-largeur-position.getX())),0);

			//Voir entête de méthode
			double deltaX = balle.obtenirCentreX()-(position.getX()+Math.cos(orientation)*(grilleVentilo.getMaxX()-largeur-position.getX()));
			double deltaY = balle.obtenirCentreY()-(position.getY()+Math.sin(orientation)*(grilleVentilo.getMaxX()-largeur-position.getX()));;
			Vecteur3D vecZoneBalle = new Vecteur3D(deltaX,deltaY,0);

			Vecteur3D projection = Vecteur3D.projectionOrthogonale(vecZoneBalle, directeur);
			double rayon = projection.module();

			try {
				projection = projection.normalise();
			} catch (Exception e) {
				e.printStackTrace();
			}
			balle.ajouterAForceVentilateur(projection.multiplie(forceVentilateur/rayon));
		}

		Scene.getPoints().ajouterPoints(POINTS_AJOUTES);

	}

	/**
	 * Méthode permettant de déterminer si la balle est en intersection avec le ventilateur.
	 * @param balle La position de la balle (du coin gauche)
	 * @return Retourne si la balle est en contact ou non avec le ventilateur.
	 */
	//Aimé Melançon
	@Override
	public boolean intersection(Balle balle) {		
		Area balleAireCopie = new Area(balle.getAireBalle());
		Area aireZoneTemp = new Area(aireZone);

		aireZoneTemp.intersect(balleAireCopie);

		return !aireZoneTemp.isEmpty() || mur.intersection(balle);
	}
	/**
	 * Méthode permettant de connaître les caractéristiques d'un ventilateur.
	 */
	//Aimé Melançon
	@Override
	public String toString() {
		return "Le ventilateur suivant est à la position ("+ this.position.getX()+","+ this.position.getY()+")"+ "; Sa hauteur x Largeur :"+ hauteur+"X"+largeur+
				"; Son orientation (radians) :" + this.orientation + "; Sa couleur :"+ this.couleur+";"+"Sa force de départ :"+ forceVentilateur +";";
	}
	/** Méthode permettant d'aller seulement d'une image à la fois dans une animation d'un ventilateur s'il y a lieu.
	 * @param deltaT La différence entre le temps simulé
	 */
	//Aimé Melançon
	@Override
	public void avancerUnPas(java.lang.Double deltaT) {

		if(tictac>ticTacMax) {

			omega=-omega;
			tictac=0;
		}else {
			tictac=tictac +1;
		}
		angleGauche+=omega;
		angleDroit-=omega;
		creerLaGeometrie();

	}

	/**
	 * Méthode permettant de faire un déplacement d'un ventilateur sur la table.
	 * @param deplacement Le déplacement de l'obstacle effectué.
	 */
	//Aimé Melançon
	@Override
	public void setDeplacement(Vecteur3D deplacement) {
		this.setPosition(this.position.additionne(deplacement));

	}

	/**Méthode permettant d'avoir la force de départ du ventilateur.
	 * @return la forceVentilateur
	 */
	//Aimé Melançon
	public double getForceVentilateur() {
		return forceVentilateur;
	}

	/**Méthode permettant de changer la force de départ du ventilateur.
	 * @param forceVentilateur la nouvelle force du ventilateur
	 */
	//Aimé Melançon
	public void setForceVentilateur(double forceVentilateur) {
		this.forceVentilateur = forceVentilateur;
	}
	/**
	 * Méthode permettant de réinitialiser l'animation du ventilateur s'il y a lieu.
	 */
	//Aimé Melançon
	@Override
	public void reinitialiser() {
		angleGauche=0;
		angleDroit=0;
		tictac=0;
		omega=1;
		creerLaGeometrie();
	}
	/**
	 * Méthode permettant de faire l'instersection entre deux obstacle.
	 * @param obst l'obstacle possiblement en intersection avec le ventilateur.
	 */
	//Félix Lefrançois
	@Override
	public boolean intersection(Obstacle obst) {
		Area copieAireVentilateur = new Area(aireTotal);
		Area copieAireObstacle = new Area(obst.getAire());

		copieAireVentilateur.intersect(copieAireObstacle);
		return !copieAireVentilateur.isEmpty();
	}

	/**
	 *
	 * Méthode servant d'avoir l'aire du ventilateur avec son effet.
	 * @return l'aire du ventilateur simple
	 */
	//Aimé Melançon
	@Override
	public Area getAire() {
		return aireTotal;
	}
}

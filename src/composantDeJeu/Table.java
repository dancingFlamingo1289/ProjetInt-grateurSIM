package composantDeJeu;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;
import gestionCollision.CollisionDesObjets;
import interfaces.Dessinable;
import math.vecteurs.Vecteur3D;
import obstacles.BaseMur;
import obstacles.Cercle;
import obstacles.Mur;
import obstacles.Obstacle;
import obstacles.TrouNoir;
import obstacles.plaqueMagnetique.PlaqueMagnetique;
import obstacles.plaqueMagnetique.ZoneDeChampMagnetique;
import obstacles.polygone.Polygone;

/**
 * Composant de dessin où tous les obstacles seront placés et qui permet de jouer
 * @author Félix Lefrançois
 */
public class Table implements Dessinable, Serializable {
	/** Coefficient de sérialisation pour les fichiers. **/
	private static final long serialVersionUID = 1L;
	/** Un Array contenant tous les objets de type Balle se trouvant sur la table **/
	private CopyOnWriteArrayList<Balle> lesBalles;
	/** Le coefficient de frottement de la table **/
	private double coefDeFrottement = 0.20;
	/** L'angle d'inclinaison de la table **/
	private double inclinaison = Math.PI/4;
	/** nombre de pixels pour un metre **/
	private double pixelsParMetre = 1;
	/** La hauteur en mètres de la table**/
	private double hauteurTable;
	/** La largeur en mètres de la table**/
	private double largeurTable;
	/** L'aire de la table**/
	private transient Area surfaceTable = new Area(), surfaceCourbe = new Area(), surfaceRectangle = new Area();
	/** Les différents murs composant la table**/
	private Line2D.Double murGauche, murDroit, murPropulsion, murFlipperDroit, murFlipperGauche, murBasDroit,murBasGauche;
	/** Le rectangle permettant de dessiner l'aire de la table**/
	private Rectangle2D.Double rectangleSurface;
	/** La couleur de la surface de la table**/
	private Color couleurSurface = Color.CYAN;
	/** L'endroit où la courbe débute par rapport à la hauteur de la table**/
	private final double DEBUT_COURBE = 0.1;
	/** Booléen confirmant le jeu a commencé**/
	private boolean debutJeu = true;
	/** Couleur des murs de la table**/
	private Color couleurMur = Color.BLACK;
	/** La position initiale d'une balle**/
	private static final Vecteur3D POS_INIT_BALLE = new Vecteur3D(30, 90, 0);
	/** La balle créée au début du jeu**/
	private Balle balleDebut;
	/** Un Array contenant tous les objets de type Obstacle se trouvant sur la table **/
	private CopyOnWriteArrayList<Obstacle> lesObstacles;
	/** Un array contenant tous les murs composant la table**/
	private CopyOnWriteArrayList<Line2D.Double> lesMurs;
	/** La courbe du haut de la table**/
	private Path2D.Double murCourbe;
	/** L'incertitude de la distance entre le centre de la balle et un mur**/
	private static final double INCERTITUDE = 0.5;
	/** Le compteur pour la réinitialisation automatique de la scène**/
	private int compteurReinitialisation = 0;
	/** L'incertitude reliée à la réinitialisation automatique de la scene */
	private final int INCERTITUDE_REINI = 4;
	/** Le coefficient de multiplication du début des murs des flippers par rapport à la hauteur de la table**/
	private final double DEBUT_MUR_FLIPPER = 0.8;
	/** La largeur du trou de la table**/
	private final int LARGEUR_TROU = 40;
	/** La hauteur des flippers par rapport au mur du bas **/
	private final int HAUTEUR_FLIPPER = 15;
	/** Les deux flippers de la table **/
	private Flipper flipperDroit, flipperGauche;
	/** Un array contenant tous les flippers **/
	private CopyOnWriteArrayList<Flipper> lesFlippers;
	/** Le nombre d'iterations passées dans le calcul des collisions pour facilité l'animation **/
	private final int N_ITERATIONS = 2;
	/** La masse d'une balle**/
	private double masseBalle = 5;
	/** Des obstacles temporaires pour démontrer leurs effets sur la balle **/
	private Obstacle trouNoir, polygone, plaqueMagn, plaqueMagn2, cercle;
	private BaseMur mur;
	private Ressort ressortDebut ;

	/**
	 * Constructeur de la classe Table
	 * @param hauteurDuComposantEnMetre La hauteur du composant (y) en mètres
	 * @param largeurDuComposantEnMetre La largeur du composant (x) en mètres
	 * @param pixelsParMetre Le nombre de pixels par metre du dessin
	 * @param coefFrottement Le coefficient de frottement de la surface de la table
	 */
	//Félix Lefrançois
	public Table(double hauteurDuComposantEnMetre, double largeurDuComposantEnMetre, 
			double pixelsParMetre, double coefFrottement,double masseBalle) {

		this.pixelsParMetre = pixelsParMetre;
		this.hauteurTable = hauteurDuComposantEnMetre;
		this.largeurTable = largeurDuComposantEnMetre;
		this.masseBalle = masseBalle;
		lesBalles = new CopyOnWriteArrayList<Balle>();
		lesObstacles = new CopyOnWriteArrayList<Obstacle>();
		lesMurs = new CopyOnWriteArrayList<Line2D.Double>();
		lesFlippers = new CopyOnWriteArrayList<Flipper>(); 
		this.coefDeFrottement = coefFrottement;

		creerLaGeometrie();
	}

	/**
	 * Création de la géométrie de la table
	 */
	//Félix Lefrançois
	public void creerLaGeometrie() {
		if (debutJeu) {
			rectangleSurface = new Rectangle2D.Double(0, DEBUT_COURBE*hauteurTable, largeurTable, hauteurTable-DEBUT_COURBE*hauteurTable);
			surfaceRectangle = new Area(rectangleSurface);

			murGauche = new Line2D.Double(0, DEBUT_COURBE*hauteurTable, 0, hauteurTable);
			lesMurs.add(murGauche);
			murDroit = new Line2D.Double(largeurTable, hauteurTable*DEBUT_COURBE, largeurTable, hauteurTable);
			lesMurs.add(murDroit);
			murPropulsion = new Line2D.Double(largeurTable- Balle.getDiametre()-2, 
					hauteurTable*DEBUT_COURBE*2, largeurTable - Balle.getDiametre()-2, hauteurTable);
			lesMurs.add(murPropulsion);
			double distancePropulsionEtMurGauche = murPropulsion.getX1()- murGauche.getX1();
			murFlipperDroit = new Line2D.Double(murPropulsion.getX1(),hauteurTable*DEBUT_MUR_FLIPPER,distancePropulsionEtMurGauche/2+LARGEUR_TROU/2,hauteurTable);
			lesMurs.add(murFlipperDroit);
			
			murFlipperGauche = new Line2D.Double(0,hauteurTable*DEBUT_MUR_FLIPPER,distancePropulsionEtMurGauche/2-LARGEUR_TROU/2,hauteurTable);
			lesMurs.add(murFlipperGauche);
			
			murBasDroit = new Line2D.Double(murDroit.getX1(),hauteurTable,distancePropulsionEtMurGauche/2+LARGEUR_TROU/2,hauteurTable);
			lesMurs.add(murBasDroit);
			
			murBasGauche = new Line2D.Double(0,hauteurTable,distancePropulsionEtMurGauche/2-LARGEUR_TROU/2,hauteurTable);
			lesMurs.add(murBasGauche);
			
			murCourbe = new Path2D.Double();
			murCourbe.moveTo(0, hauteurTable*DEBUT_COURBE);
			creerCourbeBaseeSurSin(murCourbe);
			
			flipperDroit = new Flipper("Droit",distancePropulsionEtMurGauche-15,hauteurTable-15,0);
			lesFlippers.add(flipperDroit);
			
			flipperGauche = new Flipper("Gauche",0+15,hauteurTable-15,0);
			lesFlippers.add(flipperGauche);

			surfaceCourbe = new Area(murCourbe);

			surfaceTable = new Area() ;
			surfaceTable.add(surfaceRectangle);
			surfaceTable.add(surfaceCourbe);

			balleDebut = new Balle(POS_INIT_BALLE, masseBalle);
			lesBalles.add(balleDebut);
			
			//cercle = new Cercle(new Vecteur3D(10,70),10, Color.BLUE);
			//lesObstacles.add(cercle);
	
			//mur = new Mur(new Vecteur3D(0,0,0), 10,10,0, Color.BLUE);

			//ressortDebut = new Ressort(largeurTable, hauteurTable - 20.0, 20.0, 1.0, 5, 1.0, 0.0, 0.15d) ;
			
			//trouNoir = new TrouNoir(new Vecteur3D(30,30), 40, 2e14, Color.LIGHT_GRAY) ;
			//lesObstacles.add(trouNoir);

			//plaqueMagn = new PlaqueMagnetique(new Vecteur3D(30, 150), 40, -4.5, Color.blue) ;
			//lesObstacles.add(plaqueMagn);

			//plaqueMagn2 = new PlaqueMagnetique(new Vecteur3D(60, 30), 40, +4.5, Color.green) ;
			//lesObstacles.add(plaqueMagn2);

			//polygone = new Polygone(new Vecteur3D(60, 80), 4, 20d, Color.YELLOW) ;
			//lesObstacles.add(polygone) ;

			debutJeu = false;
		}
	}

	/**
	 * Méthode permettant de dessiner un objet de type table
	 * @param Contexte graphique
	 */
	@Override
	//Félix Lefrançois
	public void dessiner(Graphics2D g2d) {
		Graphics2D g2dPrive = (Graphics2D) g2d.create();

		g2dPrive.setColor(couleurSurface);

		g2dPrive.fill(surfaceTable);

		g2dPrive.setColor(couleurMur);

		g2dPrive.setStroke(new BasicStroke(1));

		g2dPrive.draw(murCourbe);

		dessinerMurs(g2dPrive);

		dessinerObstacles(g2dPrive);

		dessinerFlippers(g2dPrive);

		dessinerBalles(g2dPrive);

		//ressortDebut.dessiner(g2dPrive) ;
	}

	/**
	 * Méthode permettant de dessiner les balles durant l'animation
	 * @param g2d Contexte graphique
	 */
	//Félix Lefrançois
	public void dessinerBalles(Graphics2D g2d) {
		for (Balle balle  : lesBalles) {
			balle.dessiner(g2d);
		}
	}

	/**
	 * Méthode pour dessiner les différents murs composant la table
	 * @param g2d contexte graphique
	 */
	//Félix Lefrançois
	public void dessinerMurs(Graphics2D g2d) {
		for (Line2D mur : lesMurs) {
			g2d.draw(mur);
		}
	}

	/**
	 * Méthode pour dessiner les obstacles se trouvant sur la table
	 * @param g2d Contexte graphique
	 */
	//Félix Lefrançois
	public void dessinerObstacles(Graphics2D g2d) {
		for (Obstacle obstacle  : lesObstacles) {
			obstacle.dessiner(g2d);
		}
	}

	/**
	 * Méthode dessinant tous les flippers
	 * @param g2d Contexte graphique
	 */
	//Félix Lefrançois
	public void dessinerFlippers(Graphics2D g2d) {
		for (Flipper flipper : lesFlippers) {
			flipper.dessiner(g2d);
		}
	}

	/**
	 * Méthode créant une courbe basée sur la fonction sin
	 * @param courbe La courbe de la table
	 */
	//Félix Lefrançois
	public void creerCourbeBaseeSurSin(Path2D.Double courbe){
		courbe.moveTo(0, hauteurTable*DEBUT_COURBE);
		for (int i = 1; i < largeurTable/2+2;i++) {
			courbe.lineTo(i*2,hauteurTable*DEBUT_COURBE-(20* Math.sin((Math.PI/largeurTable)*i*2)));
			lesMurs.add(new Line2D.Double(2*i-1, (hauteurTable*DEBUT_COURBE-(20* Math.sin((Math.PI/largeurTable)*(2*i-1)))), 2*i, hauteurTable*DEBUT_COURBE-(20* Math.sin((Math.PI/largeurTable)*2*i))));
		}
	}

	/**
	 * Méthode pour ajouter un obstacle sur la table
	 * @param obstacle L'obstacle ajouté sur la table
	 */
	//Félix Lefrançois
	public void ajouterObstacle(Obstacle obstacle) {
		lesObstacles.add(obstacle);
	}

	/**
	 * Méthode permettant d'ajouter une balle sur la table
	 */
	//Félix Lefrançois
	public void ajouterBalle() {
		Balle nouvelleBalle = new Balle(POS_INIT_BALLE,masseBalle);
		lesBalles.add(nouvelleBalle);
	}

	/**
	 * Méthode permettant de réintialiser les propriétés de la table lorsque demandé
	 */
	//Félix Lefrançois
	public void reinitialiserTable() {
		lesBalles.clear();
		balleDebut = new Balle(POS_INIT_BALLE,masseBalle);
		lesBalles.add(balleDebut);
		reinitialiserFlippers();
		compteurReinitialisation = 0;
	}
	
    /**
     * Méthode permettant de réinitialiser tous les flippers de la table
     */
	//Félix Lefrançois
	public void reinitialiserFlippers() {
		for (Flipper flipper : lesFlippers) {
			flipper.reinitialiser();
		}
	}
	
	/**
	 * Méthode qui change la masse de toutes les balles lors du jeu
	 * @param masse La nouvelle masse
	 */
	//Félix Lefrançois
	public void changerMasseDesBalles(double masse) {
		masseBalle = masse;
		for (Balle balle : lesBalles) {
			balle.setMasse(masseBalle);
		}
	}

	/**
	 * Méthode s'occupant de calculer les cahngements physiques des balles et des obstacles ainsi que de les
	 * appliquer
	 * @param deltaT La différence de temps simulé
	 * @throws Exception Erreur si la masse est pratiquement nulle
	 */
	//Félix Lefrançois
	public void avancerUnPas(Double deltaT) throws Exception {
		for (Balle balle  : lesBalles) {
			balle.calculerSommeDesForcesConstantes(inclinaison, coefDeFrottement);
			calculerForceObstacles(balle);
			balle.calculerAcceleration();
			balle.avancerUnPas(deltaT*N_ITERATIONS);
			verifierCollision(balle);
		}
		avancerUnPasFlippers(deltaT*N_ITERATIONS);
		avancerUnPasObstacles(deltaT*N_ITERATIONS);
		creerLaGeometrie();
	}

	/**
	 * La méthode pour calculer et appliquer les changement visuels et physiques des obstacles
	 * @param deltaT La différence de temps simulé
	 */
	//Félix Lefrançois
	public void avancerUnPasObstacles(double deltaT) {
		for (Obstacle obstacle : lesObstacles) {
			obstacle.avancerUnPas(deltaT);
		}
	}
	
	/**
	 * La méthode pour calculer et appliquer les changements visuels et physiques des flippers
	 * @param deltaT La différence de temps simulé
	 */
	//Félix Lefrançois
	public void avancerUnPasFlippers(double deltaT) {
		for (Flipper flipper : lesFlippers) {
			flipper.avancerUnPas(deltaT);
		}
	}

	/**
	 * Méthode permettant de vérifier si toutes les balles sont arrêtées afin de réinitialiser l'application
	 * automatiquement
	 * @return Retourne le compteur de réinitialisation qui compte le nombre de fois où la balle ne bouge presque pas près
	 * du bas de la table
	 */
	//Félix Lefrançois
	public int arreterAnimationTableAutomatiquement() {
		for (Balle balle : lesBalles) {
			if (balle.getVitesse().module() < INCERTITUDE_REINI) {
				compteurReinitialisation++;
			} else {
				compteurReinitialisation = 0;	
			}
		}
		return compteurReinitialisation;
	}


	/**
	 * Méthode pour vérifier si il y a une collision avec les murs et effectuer la collision sur la balle
	 * @param balle La balle testée
	 * @throws Exception Si le vecteur ne peut être normalisée puisqu'il est de longueur trop petite ou nulle
	 */
	//Félix Lefrançois
	public void verifierCollision(Balle balle) throws Exception {
		if (!debutJeu) {
				if (verifierCollisionAvecMurs(balle,lesMurs)) {
					Vecteur3D normale = CollisionDesObjets.trouverNormaleCollisionMur(balle, trouverMurLePlusProche(balle,lesMurs));
					balle.setVitesse(CollisionDesObjets.vitesseFinaleImmobile(balle.getVitesse(),
							normale));
					System.out.println("Il y a eu une collision avec un mur");
					balle.setPosition(balle.getPosition().additionne(normale));
				} else if (verifierCollisionAvecFlippers(balle,lesFlippers)) {
					appliquerCollisionAvecFlippers(balle,lesFlippers);
					System.out.println("Il y a eu une collision avec un flipper");
				}
		}
	}

	public void gererEtirementRessortDepart(double nouveauEtirement) {
		//ressortDebut.setEtirement(nouveauEtirement);
	}
	
	/**
	 * Méthode vérifiant si la balle est en collision avec un mur
	 * @param balleTest La balle qui est testée
	 * @param lesMursTestes
	 * @return Un booléen qui dit si la balle est en contact avec un mur
	 */
	//Félix Lefrançois
	public static boolean verifierCollisionAvecMurs(Balle balleTest, CopyOnWriteArrayList<Line2D.Double> lesMursTestes) {
		boolean verifierCollision = false;
		for (Line2D.Double mur : lesMursTestes) {
			if (mur.ptSegDist(balleTest.getPosition().getX()+Balle.getDiametre()/2, balleTest.getPosition().getY()
					+Balle.getDiametre()/2) - INCERTITUDE < Balle.getDiametre()/2 && !verifierCollision) {
				verifierCollision = true;
				return verifierCollision;
			}
		}
		return verifierCollision;
	}

	/**
	 * Méthode permettant de vérifier si il y a une collision avec des flippers
	 * @param balleTest La balle testée
	 * @param lesFlippersTestes Un array contenant des flippers
	 * @return un booléen confirmant si il y a une collision
	 */
	//Félix Lefrançois
	public boolean verifierCollisionAvecFlippers(Balle balleTest, CopyOnWriteArrayList<Flipper> lesFlippersTestes) {
		boolean verifierCollision = false;
		for (Flipper flipper : lesFlippers) {
			if (flipper.verifierCollisionFlipper(balleTest) && !verifierCollision) {
				verifierCollision = true;
				return verifierCollision;
			}
		}
		return verifierCollision;
	}

	/**
	 * Méthode permettant de trouver avec lequel la balle entre en collision
	 * @param balleTest La balle qui entre en collision avec un mur
	 * @param lesMursTestes Un array contenant tous les murs testés
	 * @return Le mur le plus proche de la balle
	 */
	//Félix Lefrançois
	public static Line2D.Double trouverMurLePlusProche(Balle balleTest, CopyOnWriteArrayList<Line2D.Double> lesMursTestes) {
		Line2D.Double murVerif = new Line2D.Double();
		for (Line2D.Double mur : lesMursTestes) {
			if (mur.ptSegDist(balleTest.getPosition().getX()+Balle.getDiametre()/2, balleTest.getPosition().getY()
					+Balle.getDiametre()/2) - INCERTITUDE < Balle.getDiametre()/2) {

				if(murVerif == null || mur.ptSegDist(balleTest.getPosition().getX()+Balle.getDiametre()/2, balleTest.getPosition().getY()
						+Balle.getDiametre()/2) < murVerif.ptSegDist(balleTest.getPosition().getX()+Balle.getDiametre()/2, balleTest.getPosition().getY()
								+Balle.getDiametre()/2)) {
					murVerif = mur;
				}  		
			}
		}

		return murVerif;
	}

	/**
	 * Méthode permettant d'appliquer le changement de vitesse à une balle lors d'une collision avec un flipper
	 * @param balleTest La balle testée
	 * @param lesFlippersTestes Un array contenant tous les flippers testés
	 */
	//Félix Lefrançois
	public void appliquerCollisionAvecFlippers(Balle balleTest, CopyOnWriteArrayList<Flipper> lesFlippersTestes) {
		for(Flipper flipper: lesFlippers) {
			if (flipper.verifierCollisionFlipper(balleTest)) {
				try {
					flipper.appliquerCollisionFlipper(balleTest);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Méthode appliquant l'effet d'un obstacle sur la balle lorsqu'elle se trouve près de celui-ci
	 * @param balleTestee La balle qui est testée
	 * @throws Exception Exception lorsqu'un vecteur est normalisée
	 */
	//Félix Lefrançois
	public void calculerForceObstacles(Balle balleTestee) throws Exception {
		for (Obstacle obstacle: lesObstacles) {
			if (obstacle.intersection(balleTestee)) {
				try {
					System.out.println("On entre en collision avec un obstacle.") ;
					obstacle.collision(balleTestee);
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Méthode qui permet de déclencher l'animation du flipper droit
	 */
	//Félix Lefrançois
	public void flipperDroitMonte() {
		flipperDroit.commencerAnimation();
	}
	
	/**
	 * Méthode qui permet de déclencher l'animation du flipper gauche
	 */
	//Félix Lefrançois
	public void flipperGaucheMonte() {
		flipperGauche.commencerAnimation();
	}
	
	/**
	 * Méthode permettant d'obtenir le coefficient de frottement de la table
	 * @return Le coefficient de frottement de la table
	 */
	//Félix Lefrançois
	public double getCoefDeFrottement() {
		return coefDeFrottement;
	}

	/**
	 * Méthode permettant de changer le coefficient de frottement de la table
	 * @param coefDeFrottement Le nouveau coefficient de frottement de la table
	 */
	//Félix Lefrançois
	public void setCoefDeFrottement(double coefDeFrottement) {
		this.coefDeFrottement = coefDeFrottement;
	}

	/**
	 * Méthode permettant d'obtenir l'inclinaison de la table
	 * @return L'inclinaison de la table
	 */
	//Félix Lefrançois
	public double getInclinaison() {
		return inclinaison;
	}

	/**
	 * Méthode permettant de changer l'inclinaison de la table
	 * @param inclinaison La nouvelle inclinaison de la table
	 */
	//Félix Lefrançois
	public void setInclinaison(double inclinaison) {
		this.inclinaison = inclinaison;
	}

	/**
	 * Méthode permettant d'obtenir les pixels par metre
	 * @return Les pixels par metre de la table
	 */
	//Félix Lefrançois
	public double getPixelsParMetre() {
		return pixelsParMetre;
	}

	/**
	 * Méthode retournant la constante de la position initiale d'une balle
	 * @return La position initiale d'une balle
	 */
	//Félix Lefrançois
	public static Vecteur3D getPOS_INIT_BALLE() {
		return POS_INIT_BALLE;
	}

	/**
	 * Méthode permettant de changer les pixelsParMetre de la table
	 * @param pixelsParMetre Le nombre de pixels par metre
	 */
	//Félix Lefrançois
	public void setPixelsParMetre(double pixelsParMetre) {
		this.pixelsParMetre = pixelsParMetre;
		creerLaGeometrie();
	}

	/**
	 * Méthode permettant d'obtenir la couleur de la surface de la table
	 * @return La couleur de la surface de la table
	 */
	//Félix Lefrançois
	public Color getCouleurSurface() {
		return couleurSurface;
	}

	/**
	 * Méthode permettant de modifier la couleur de la surface de la table
	 * @param couleurSurface La nouvelle couleur du sol
	 */
	//Félix Lefrançois
	public void setCouleurSurface(Color couleurSurface) {
		this.couleurSurface = couleurSurface;
	}

	/**
	 * Méthode permettant d'obtenir la couleur des murs
	 * @return La couleur des murs
	 */
	//Félix Lefrançois
	public Color getCouleurMur() {
		return couleurMur;
	}

	/**
	 * Méthode permettant de changer la couleur des murs
	 * @param couleurMur La nouvelle couleur des murs
	 */
	//Félix Lefrançois
	public void setCouleurMur(Color couleurMur) {
		this.couleurMur = couleurMur;
	}

	/**
	 * Méthode retournant la balle du début
	 * @return La balle du début
	 */
	//Félix Lefrançois
	public Balle getBalleDebut() {
		return balleDebut;
	}

	/**
	 * Méthode permettant de modifier la hauteur en mètres de la table
	 * @param hauteurTable La nouvelle hauteur en mètres de la table
	 */
	//Félix Lefrançois
	public void setHauteurTable(double hauteurTable) {
		this.hauteurTable = hauteurTable;
		creerLaGeometrie();
	}

	/**
	 * Méthode permettant d'obtenir la hauteur en mètres de la table
	 * @return La hauteur en mètres de la table
	 */
	//Félix Lefrançois
	public double getHauteurTable() {
		return hauteurTable;
	}

	/**
	 * Méthode permettant de modifier la largeur en m¸etres de la table
	 * @param hauteurTable La nouvelle largeur en mètres de la table
	 */
	//Félix Lefrançois
	public void setLargeurTable(double largeurTable) {
		this.largeurTable = largeurTable;
		creerLaGeometrie();
	}

	/**
	 * Méthode permettant d'obtenir la largeur en mètres de la table
	 * @return La largeur en mètres de la table
	 */
	//Félix Lefrançois
	public double getLargeurTable() {
		return largeurTable;
	}

	/**
	 * Méthode permettant de modifier l'état de la partie.
	 * @param debutJeu : État de la table.
	 */
	// Par Elias Kassas
	public void setDebutJeu(boolean debutJeu) {
		this.debutJeu = debutJeu ;
		creerLaGeometrie() ;
	}

	/**
	 * Méthode permettant de créer une nouvelle table.
	 */
	// Par Elias Kassas
	public void nouvelleTable() {
		lesObstacles.clear() ;
		lesFlippers.clear() ;
		lesMurs.clear() ;
		lesBalles.clear() ;
		this.debutJeu = true ;
		creerLaGeometrie() ;
	}
}
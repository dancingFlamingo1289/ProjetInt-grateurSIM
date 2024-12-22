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
import obstacles.Obstacle;
import scene.Scene;
import outils.GererSon;
import outils.OutilsSon;

/**
 * Composant de dessin où tous les obstacles seront placés et qui permet de jouer
 * @author Félix Lefrançois
 * @author Aimé Melançon
 * @author Elias Kassas
 */
public class Table implements Dessinable, Serializable {
	/** Coefficient de sérialisation pour les fichiers. **/
	private static final long serialVersionUID = 1L;
	/** Un Array contenant tous les objets de type Balle se trouvant sur la table **/
	private CopyOnWriteArrayList<Balle> lesBalles;
	/** Le coefficient de frottement de la table **/
	private double coefDeFrottement = 0.5;
	/** L'angle d'inclinaison de la table **/
	private double inclinaison;
	/** nombre de pixels pour un metre **/
	private double pixelsParCentimetre = 1;
	/** La hauteur en mètres de la table**/
	private double hauteurTable;
	/** La largeur en mètres de la table**/
	private double largeurTable;
	/** L'aire de la table**/
	private transient Area surfaceTable = new Area(), surfaceCourbe = new Area(), surfaceRectangle = new Area();
	/** Les différents murs composant la table**/
	private Line2D.Double murGauche, murDroit, murPropulsion, murFlipperDroit, murFlipperGauche, murBasDroit,murBasGauche, murPorte;
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
	/** La couleur des balles créées **/
	private final Color COULEUR_BALLE = Color.GRAY;
	/** La constante de la couleur de la balle de début de jeu**/
	private final Color COULEUR_BALLE_DEBUT = Color.RED;
	/** La position initiale d'une balle**/
	private final Vecteur3D POS_INIT_BALLE = new Vecteur3D(122, 160, 0);
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
	/** Le coefficient de multiplication du début des murs des flippers par rapport à la hauteur de la table**/
	private final double DEBUT_MUR_FLIPPER = 0.8;
	/** La largeur du trou de la table**/
	private final int LARGEUR_TROU = 40;
	/** La hauteur des flippers par rapport au mur du bas **/
	private final int HAUTEUR_FLIPPER = 20;
	/** La distance du flipper par rapport à un mur**/
	private final int DISTANCE_FLIPPER = 10;
	/** Les deux flippers de la table **/
	private Flipper flipperDroit, flipperGauche;
	/** Un array contenant tous les flippers **/
	private CopyOnWriteArrayList<Flipper> lesFlippers;
	/** Le nombre d'iterations passées dans le calcul des collisions pour facilité l'animation **/
	private final int N_ITERATIONS = 2;
	/** Le nombre de saut de pas avant une réinitialisation automatique du composant **/
	private final int NB_DELTAT_AV_REINI_AUTO = 300;
	/** La masse d'une balle**/
	private double masseBalle;
	/** Les chaines de caractères représentant les lettres des touches affichées sur leur base **/
	private String lettreGauche, lettreDroit;
	/** Le ressort situé dans la zone de propulsion de la table. **/
	private transient Ressort ressortDebut ;
	/** Le nombre de points ajoutés au système de points de Scene lors d'une collision avec une autre balle **/
	private final int POINTS_AJOUTES = 275;
	/** OutilsSon pour les murs**/
	private transient OutilsSon sonMur =new OutilsSon();
	/** OutilsSon pour jouer un son lors d'une collision entre deux balles**/
	private transient OutilsSon sonBalle = new OutilsSon();
	/**Nom du fichier du son pour la collision des murs **/
	private final String NOM_DU_FICHIER="LesMurs.wav";
	/** Nom du fichier de son pour la collision entre deux balles**/
	private final String NOM_DU_FICHIER_COLLISION_BALLE = "Bille.wav";
	/** Constante du nombre de points ajoutés lors d'une collision avec un mur fait avec un Line2D.Double **/
	private final int POINTS_AJOUTES_LINE2D = 200;
	/** Le nombre de balles qui ont été perdu au cours d'une vie **/
	private int nbDePerte = 3;
	/** Le nombre maximum sur la table à chaque vie **/
	private final int MAXIMUM_BALLE = 3;
	/** Booléen confirmant si c'est la première fois que l'on crée la géométrie **/
	private boolean premiereFois = true;
	/** La zone d'aire où il y a une propulsion de la balle **/
	private transient Area airePropulsion;
	/** Le rectangle permettant de former airePropulsion**/
	private Rectangle2D.Double rectanglePropulsion;
	/** Le compteur du nombre de deltaT depuis que la balle a quittée la zone de propulsion **/
	private int compteurPorte;
	/** La constante de la valeur du compteur lorsqu'une balle est proche de la porte**/
	private final int VALEUR_MAX_COMPTEUR_PORTE = 80;
	/** L'aire où il est possible de placer des obstacles **/
	private transient Area aireOuPossibleDePlacer;
	/** Constante ajoutant une valeur à la boucle créant la courbe afin que l'aire soit complête**/
	private final int AJOUT_BOUCLE = 1;
	/** Le multiplicateur pour que la courbe se termine au bon endroit **/
	private final int MULTIPLICATEUR_COURBE = 20;
	/** Constante d'incrémentation pour correctement placer les flippers **/
	private final double INCREMENTATION_FLIPPER = 2.5;
	/** Le coefficient de l'incertitude pour ouvrir la porte**/
	private final int MULTIPLICATEUR_INCERTITUDE_PORTE = 8;
	/** Longueur et largeur du ressort de la table **/
	private final double LONGUEUR_RESSORT = 20.0, LARGEUR_RESSORT = 9.0 ;
	/** Nombre de boucles présentes dans un ressort. **/
	private final int NB_BOUCLES = 10 ;
	/** Constante de rappel initiale. **/
	private double constanteRappelInit = 30.0 ;
	/** Constante d'incertitude de la largeur de la zxone de propulsion**/
	private final int INCERTITUDE_ESPACE_PROPULSION = 2;

	/**
	 * Constructeur de la classe Table
	 * @param hauteurDuComposantEnCentimetre La hauteur du composant (y) en centimètres
	 * @param largeurDuComposantEnCentimetre La largeur du composant (x) en centimètres
	 * @param pixelsParCentimetre Le nombre de pixels par metre du dessin
	 * @param coefFrottement Le coefficient de frottement de la surface de la table
	 * @param inclinaison L'angle d'inclinaison en radian de la table
	 * @param masseBalle La masse de la balle en kg
	 * @param lettreGauche Chaîne de caractères représentant le flipper gauche
	 * @param lettreDroit Chaîne de caractères représentant le flipper droit
	 */
	//Félix Lefrançois
	public Table(double hauteurDuComposantEnCentimetre, double largeurDuComposantEnCentimetre, 
			double pixelsParCentimetre, double coefFrottement,double inclinaison,double masseBalle, 
			String lettreGauche, String lettreDroit, Color couleurSurface) {
		this.pixelsParCentimetre = pixelsParCentimetre;
		this.hauteurTable = hauteurDuComposantEnCentimetre;
		this.largeurTable = largeurDuComposantEnCentimetre;
		this.masseBalle = masseBalle;
		this.inclinaison = inclinaison;
		this.lettreGauche = lettreGauche;
		this.lettreDroit = lettreDroit;
		lesBalles = new CopyOnWriteArrayList<Balle>();
		lesObstacles = new CopyOnWriteArrayList<Obstacle>();
		lesMurs = new CopyOnWriteArrayList<Line2D.Double>();
		lesFlippers = new CopyOnWriteArrayList<Flipper>(); 
		this.coefDeFrottement = coefFrottement;
		this.couleurSurface = couleurSurface;

		creerLaGeometrie();
	}

	/**
	 * Création de la géométrie de la table
	 */
	//Félix Lefrançois
	private void creerLaGeometrie() {
		if (premiereFois) {
			sonMur = new OutilsSon() ;
			sonMur.chargerUnSonOuUneMusique(NOM_DU_FICHIER);

			sonBalle = new OutilsSon() ;
			sonBalle.chargerUnSonOuUneMusique(NOM_DU_FICHIER_COLLISION_BALLE);

			premiereFois = false ;
		}

		if (debutJeu) {
			lesMurs.clear();
			lesFlippers.clear();
			rectangleSurface = new Rectangle2D.Double(0, DEBUT_COURBE*hauteurTable, largeurTable, hauteurTable-DEBUT_COURBE*hauteurTable);
			surfaceRectangle = new Area(rectangleSurface);

			murGauche = new Line2D.Double(0, DEBUT_COURBE*hauteurTable, 0, hauteurTable);
			lesMurs.add(murGauche);
			murDroit = new Line2D.Double(largeurTable, hauteurTable*DEBUT_COURBE, largeurTable, hauteurTable);
			lesMurs.add(murDroit);
			murPropulsion = new Line2D.Double(largeurTable- Balle.getDiametre()-INCERTITUDE_ESPACE_PROPULSION, 
					hauteurTable*DEBUT_COURBE*2, largeurTable - Balle.getDiametre()-INCERTITUDE_ESPACE_PROPULSION, hauteurTable);
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
			murPorte = new Line2D.Double(murPropulsion.getX1(),murPropulsion.getY1(),largeurTable,murPropulsion.getY1());
			lesMurs.add(murPorte);

			murCourbe = new Path2D.Double();
			murCourbe.moveTo(0, hauteurTable*DEBUT_COURBE);
			creerCourbeBaseeSurSin(murCourbe);

			rectanglePropulsion = new Rectangle2D.Double(murPropulsion.x1,murPropulsion.y1,murDroit.x1-murPropulsion.x1,hauteurTable-murPropulsion.y1);

			flipperDroit = new Flipper("Droit",distancePropulsionEtMurGauche-DISTANCE_FLIPPER - INCREMENTATION_FLIPPER,
					hauteurTable-HAUTEUR_FLIPPER-INCREMENTATION_FLIPPER,0,lettreDroit);

			lesFlippers.add(flipperDroit);

			flipperGauche = new Flipper("Gauche",DISTANCE_FLIPPER+INCREMENTATION_FLIPPER,
					hauteurTable-HAUTEUR_FLIPPER - INCREMENTATION_FLIPPER,0,lettreGauche);

			lesFlippers.add(flipperGauche);

			surfaceCourbe = new Area(murCourbe);

			surfaceTable = new Area() ;
			surfaceTable.add(surfaceRectangle);
			surfaceTable.add(surfaceCourbe);

			if(lesBalles.isEmpty()) {
				balleDebut = new Balle(POS_INIT_BALLE, masseBalle,COULEUR_BALLE_DEBUT);
				lesBalles.add(balleDebut);
			}

			double diametreBoucle = 1.0 ;
			double angleRessort = 0.0 ;
			ressortDebut = new Ressort(largeurTable, hauteurTable, LONGUEUR_RESSORT, LARGEUR_RESSORT, 
					NB_BOUCLES, diametreBoucle, angleRessort, constanteRappelInit) ;

			airePropulsion = new Area(rectanglePropulsion);

			aireOuPossibleDePlacer = new Area(surfaceTable);
			aireOuPossibleDePlacer.subtract(airePropulsion);
			aireOuPossibleDePlacer.subtract(flipperGauche.getAireIncluantAnimation());
			aireOuPossibleDePlacer.subtract(flipperDroit.getAireIncluantAnimation());

			debutJeu = false;

		}
	}


	/**
	 * Méthode qui détermine s'il y a une balle proche de la porte et qui effectue les changements nécessaires pour que la porte s'adapte
	 */
	//Félix Lefrançois
	private void ouverturePorte() {
		if (compteurPorte == 0) {
			boolean verification = false;
			if (airePropulsion == null) {
				debutJeu =true;
				creerLaGeometrie();
			}

			for (Balle balle : lesBalles) {
				Area copieAirePropulsion = new Area(airePropulsion);
				Area  copieAireBalle = new Area (balle.getAireBalle());
				copieAirePropulsion.intersect(copieAireBalle);
				if (!(copieAirePropulsion.isEmpty()) && (murPorte.ptSegDist(balle.obtenirCentreX(), balle.obtenirCentreY()) < Balle.getDiametre()/2+INCERTITUDE*MULTIPLICATEUR_INCERTITUDE_PORTE)) {
					if (murPorte.getY1() < balle.obtenirCentreY()){
						lesMurs.remove(murPorte);
						murPorte = new Line2D.Double(murPropulsion.getX1(),murPropulsion.getY1(),murPropulsion.getX1(),murPropulsion.getY1());
						lesMurs.add(murPorte);
						verification = true;
						compteurPorte = VALEUR_MAX_COMPTEUR_PORTE;
					}
				}
			}

			if (verification == false) {
				lesMurs.remove(murPorte);
				murPorte = new Line2D.Double(murPropulsion.getX1(),murPropulsion.getY1(),largeurTable,murPropulsion.getY1());
				lesMurs.add(murPorte);
			}
		} else if( compteurPorte > 0) {
			compteurPorte -= 1;
		}
	}

	/**
	 * Méthode créant une courbe basée sur la fonction sinus
	 * @param courbe La courbe de la table
	 */
	//Félix Lefrançois
	private void creerCourbeBaseeSurSin(Path2D.Double courbe){
		courbe.moveTo(0, hauteurTable*DEBUT_COURBE);
		for (int i = 1; i < largeurTable/2+AJOUT_BOUCLE;i++) {
			courbe.lineTo(i*2,hauteurTable*DEBUT_COURBE-(MULTIPLICATEUR_COURBE* Math.sin((Math.PI/largeurTable)*i*2)));
			lesMurs.add(new Line2D.Double(2*i-1, (hauteurTable*DEBUT_COURBE-(MULTIPLICATEUR_COURBE* Math.sin((Math.PI/largeurTable)*(2*i-1))))
					, 2*i, hauteurTable*DEBUT_COURBE-(MULTIPLICATEUR_COURBE* Math.sin((Math.PI/largeurTable)*2*i))));
		}
	}

	/**
	 * Méthode permettant de dessiner un objet de type Table
	 * @param g2d Contexte graphique
	 */
	@Override
	//Félix Lefrançois
	public void dessiner(Graphics2D g2d) {
		Graphics2D g2dPrive = (Graphics2D) g2d.create();

		if (surfaceTable == null || surfaceRectangle == null || surfaceCourbe == null || airePropulsion == null || aireOuPossibleDePlacer == null) {
			debutJeu = true ;
			creerLaGeometrie() ;
		} 
		g2dPrive.setColor(couleurSurface);
		g2dPrive.fill(surfaceTable);

		g2dPrive.setColor(couleurMur);

		g2dPrive.setStroke(new BasicStroke(1));

		g2dPrive.draw(murCourbe);

		dessinerMurs(g2dPrive);

		dessinerObstacles(g2dPrive);

		dessinerFlippers(g2dPrive);

		dessinerBalles(g2dPrive);

		ressortDebut.dessiner(g2dPrive) ;

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
	private void dessinerMurs(Graphics2D g2d) {
		for (Line2D mur : lesMurs) {
			g2d.draw(mur);
		}
	}

	/**
	 * Méthode pour dessiner les obstacles se trouvant sur la table
	 * @param g2d Contexte graphique
	 */
	//Félix Lefrançois
	private void dessinerObstacles(Graphics2D g2d) {
		for (Obstacle obstacle  : lesObstacles) {
			obstacle.dessiner(g2d);
		}
	}

	/**
	 * Méthode dessinant tous les flippers
	 * @param g2d Contexte graphique
	 */
	//Félix Lefrançois
	private void dessinerFlippers(Graphics2D g2d) {
		if (lesFlippers == null) {
			creerLaGeometrie() ;
		}

		for (Flipper flipper : lesFlippers) {
			flipper.dessiner(g2d);
		}
	}

	/**
	 * Méthode pour ajouter un obstacle sur la table
	 * @param obstacle L'obstacle ajouté sur la table
	 */
	//Félix Lefrançois
	public void ajouterObstacle(Obstacle obstacle) {
		lesObstacles.add(obstacle);
		repeindre();
	}

	/**
	 * Méthode permettant d'ajouter une balle sur la table
	 */
	//Félix Lefrançois
	public void ajouterBalle() {
		if (!verifierPresenceBalleZonePropulsion()) {
			Balle nouvelleBalle = new Balle(POS_INIT_BALLE,masseBalle,COULEUR_BALLE);
			lesBalles.add(nouvelleBalle);
		}
	}
	
	/**
	 * Méthode vérifiant si une balle se trouve dans la zone de propulsion
	 * @return Un booléen confirmant si une balle se trouve dans la zone de propulsion
	 */
	//Félix Lefrançois
	public boolean verifierPresenceBalleZonePropulsion() {
		boolean intersection = false;
		
		for(Balle balle : lesBalles) {
			Area copieAirePropulsion = new Area(airePropulsion);
			Area copieAireBalle = new Area(balle.getAireBalle());
			
			copieAirePropulsion.intersect(copieAireBalle);
			if (!copieAirePropulsion.isEmpty() && intersection == false) {
				intersection = true;
			}
		}
		
		return intersection;
	}

	/**
	 * Méthode permettant de réintialiser les propriétés de la table lorsque demandé
	 */
	//Félix Lefrançois
	public void reinitialiserTable() {
		lesBalles.clear();
		balleDebut = new Balle(POS_INIT_BALLE,masseBalle,balleDebut.getCouleurBalle());
		lesBalles.add(balleDebut);
		reinitialiserFlippers();
		reinitialiserObstacles();
		nbDePerte = MAXIMUM_BALLE;
		compteurPorte = 0;
		creerLaGeometrie();
	}

	/**
	 * Méthode permettant de réinitialiser tous les flippers de la table
	 */
	//Félix Lefrançois
	private void reinitialiserFlippers() {
		if (lesFlippers == null)
			creerLaGeometrie() ;

		for (Flipper flipper : lesFlippers) {
			flipper.reinitialiser();
		}
	}

	/**
	 * Méthode permettant de réinitialiser les obstacles
	 */
	//Félix Lefrançois
	private void reinitialiserObstacles() {
		if (lesFlippers == null)
			creerLaGeometrie() ;

		for (Obstacle obstacle : lesObstacles) {
			obstacle.reinitialiser();
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
			calculerForceObstacles(balle);
			balle.calculerSommeDesForces(inclinaison, coefDeFrottement);
			balle.calculerAcceleration();
			balle.avancerUnPas(deltaT*N_ITERATIONS);
			verifierCollisions(balle);
		}
		avancerUnPasFlippers(deltaT*N_ITERATIONS);
		avancerUnPasObstacles(deltaT*N_ITERATIONS);
		ouverturePorte();
		creerLaGeometrie();
	}

	/**
	 * La méthode pour calculer et appliquer les changement visuels et physiques des obstacles
	 * @param deltaT La différence de temps simulé
	 */
	//Félix Lefrançois
	private void avancerUnPasObstacles(double deltaT) {
		for (Obstacle obstacle : lesObstacles) {
			obstacle.avancerUnPas(deltaT);
		}
	}

	/**
	 * La méthode pour calculer et appliquer les changements visuels et physiques des flippers
	 * @param deltaT La différence de temps simulé
	 */
	//Félix Lefrançois
	private void avancerUnPasFlippers(double deltaT) {
		for (Flipper flipper : lesFlippers) {
			flipper.avancerUnPas(deltaT);
		}
	}

	/**
	 * Méthode permettant de vérifier si toutes les balles sont arrêtées afin de réinitialiser l'application
	 * automatiquement
	 * @return Un booléen confirmant si il n'y a plus de balles sur la table ou non
	 */
	//Félix Lefrançois
	public boolean arreterAnimationTableAutomatiquement() {
		for (Balle balle : lesBalles) {
			Area copieAireBalle = new Area(balle.getAireBalle());
			Area copieAirePropulsion = new Area(airePropulsion);
			copieAirePropulsion.intersect(copieAireBalle);
			if ((NB_DELTAT_AV_REINI_AUTO == balle.reinitialiserAutomatiquement() || balle.getPosition().getY() >= hauteurTable) && copieAirePropulsion.isEmpty()) {
				lesBalles.remove(balle);
				nbDePerte -= 1;
			}
		}
		return lesBalles.isEmpty();
	}

	/**
	 * Méthode faisant toutes les modifications reliés aux points
	 */
	//Félix Lefrançois
	public void changementPoints() {
		if (Scene.getPoints().verifierAjoutBalle() && !(lesBalles.size() >= nbDePerte)) {
			Scene.getPoints().appliquerAjoutBalle();
			ajouterBalle();
		}
		Scene.getPoints().passerAuProchainMultiplicateur();
	}


	/**
	 * Méthode pour vérifier si il y a une collision avec les murs et effectuer la collision sur la balle
	 * @param balle La balle testée
	 * @throws Exception Si le vecteur ne peut être normalisée puisqu'il est de longueur trop petite ou nulle
	 */
	//Félix Lefrançois
	private void verifierCollisions(Balle balle) throws Exception {
		if (!debutJeu) {
			Line2D.Double murVerif = trouverMurLePlusProche(balle,lesMurs);
			appliquerCollisionAvecBalles(balle,lesBalles);
			appliquerCollisionAvecFlippers(balle,lesFlippers);
			if (murVerif != null) {
				if (sonMur == null) {
					premiereFois = true ;
					sonMur = new OutilsSon() ;
					creerLaGeometrie() ;
				} 
				if(GererSon.isAllumerFermer()) {
					sonMur.jouerUnSon();
					System.out.println("e");
				}
				Vecteur3D normale = CollisionDesObjets.trouverNormaleCollisionMur(balle, murVerif);
				balle.setVitesse(CollisionDesObjets.vitesseFinaleImmobile(balle.getVitesse(),
						normale));

				balle.setPosition(balle.getPosition().additionne(normale));
				Scene.getPoints().ajouterPoints(POINTS_AJOUTES_LINE2D);
			} 
		}
	}

	/**
	 * Méthode permettant de vérifier si une balle entre en collision avec une autre balle
	 * @param balleTestee La balle qui est testée pour une collision avec une balle de la table
	 * @return Un booléen confirmant si il y a une collision avec l'une des balles
	 */
	//Félix Lefrançois
	public boolean verifierCollisionAvecBalles(Balle balleTestee) {
		boolean verification = false;

		for (Balle balle : lesBalles) {
			if (balle.verifierCollisionBalle(balleTestee) && !balle.equals(balleTestee)) {
				verification = true;
				return verification;
			}
		}
		return verification;
	}

	/**
	 * Méthode permettant de vérifier si il y a une collision avec des flippers
	 * @param balleTest La balle testée
	 * @return Un booléen confirmant si il y a une collision avec un des flippers
	 */
	//Félix Lefrançois
	public boolean verifierCollisionAvecFlippers(Balle balleTest) {
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
	 * @return Le mur (sous forme de Line2D.Double) le plus proche de la balle
	 */
	//Félix Lefrançois
	public static Line2D.Double trouverMurLePlusProche(Balle balleTest, CopyOnWriteArrayList<Line2D.Double> lesMursTestes) {
		Line2D.Double murVerif = null;

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
	 * Méthode vérifiant si la balle est en collision avec un mur
	 * @param balleTest La balle qui est testée
	 * @param lesMursTestes Les murs
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
	 * Méthode permettant d'appliquer les changements de vitesse reliés à la collision entre des balles
	 * @param balle La balle qui entre en collision avec une autre balle
	 * @param lesBallesTestees La balle testée provenant d'un array list
	 */
	//Félix Lefrançois
	public void appliquerCollisionAvecBalles(Balle balle, CopyOnWriteArrayList<Balle> lesBallesTestees) {
		for(Balle balleTest : lesBalles) {
			if(balle.verifierCollisionBalle(balleTest) && !balle.equals(balleTest)) {
				Balle copieBalle = new Balle(balle);
				balle.setVitesse(CollisionDesObjets.vitesseFinaleMouvementCercleMobile(balle, balleTest));
				Vecteur3D normale = CollisionDesObjets.trouverNormaleCollisionCercle(balle, balleTest.obtenirCentreX(), balleTest.obtenirCentreY());
				balle.setPosition(balle.getPosition().additionne(normale));
				balleTest.setVitesse(CollisionDesObjets.vitesseFinaleMouvementCercleMobile(balleTest, copieBalle));
				Vecteur3D normaleInverse = normale.multiplie(-1);
				balleTest.setPosition(balleTest.getPosition().additionne(normaleInverse));

				if (sonBalle == null) {
					premiereFois = true ;
					creerLaGeometrie() ;
				}
				if(GererSon.isAllumerFermer()) {
					sonBalle.jouerUnSon();
				}
				Scene.getPoints().ajouterPoints(POINTS_AJOUTES);
			}
		}
	}

	/**
	 * Méthode permettant d'appliquer le changement de vitesse à une balle lors d'une collision avec un flipper
	 * @param balleTest La balle testée
	 * @param lesFlippersTestes Un array contenant tous les flippers testés
	 */
	//Félix Lefrançois
	public void appliquerCollisionAvecFlippers(Balle balleTest, CopyOnWriteArrayList<Flipper> lesFlippersTestes) {
		for(Flipper flipper: lesFlippers) {
			try {
				flipper.appliquerCollisionFlipper(balleTest);
			} catch (Exception e) {
				e.printStackTrace();
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
					obstacle.collision(balleTestee);
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		}
		if (ressortDebut.intersection(balleTestee)) {
			ressortDebut.collision(balleTestee);
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

	/**¨
	 * Changement de la lettre du flipper gauche
	 * @param nouvelleLettre La nouvelle lettre
	 */
	//Félix Lefrançois
	public void modifierAffichageLettreGauche(String nouvelleLettre){
		flipperGauche.setLettreTouche(nouvelleLettre);
	}

	/**
	 * Changement de la lettre du flipper droit
	 * @param nouvelleLettre La nouvelle lettre
	 */
	//Félix Lefrançois
	public void modifierAffichageLettreDroit(String nouvelleLettre){
		flipperDroit.setLettreTouche(nouvelleLettre);
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
		return pixelsParCentimetre;
	}


	/**
	 * Méthode permettant de changer les pixelsParMetre de la table
	 * @param pixelsParMetre Le nombre de pixels par metre
	 */
	//Félix Lefrançois
	public void setPixelsParMetre(double pixelsParMetre) {
		this.pixelsParCentimetre = pixelsParMetre;
		creerLaGeometrie();
	}

	/**
	 * Méthode retournant la constante de la position initiale d'une balle
	 * @return La position initiale d'une balle
	 */
	//Félix Lefrançois
	public Vecteur3D getPOS_INIT_BALLE() {
		return POS_INIT_BALLE ;
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
	 * Méthode permettant d'obtenir l'aire complet du flipper gauche
	 * @return L'aire total du flipper gauche en incluant son animation
	 */
	//Félix Lefrançois
	public Area obtenirAireTotalFlipperGauche() {
		return flipperGauche.getAireIncluantAnimation();
	}

	/**
	 * Méthode permettant d'obtenir l'aire complet du flipper droit
	 * @return L'aire total du flipper droit en incluant son animation
	 */
	//Félix Lefrançois
	public Area obtenirAireTotalFlipperDroit() {
		return flipperDroit.getAireIncluantAnimation();
	}

	/**
	 * Méthode retournant la liste des balles de la table
	 * @return La liste des balles de la table
	 */
	//Félix Lefrançois
	public CopyOnWriteArrayList<Balle> getLesBalles(){
		return lesBalles;
	}

	/**
	 * Méthode permettant de modifier la constante de rappel du ressort de début
	 */
	//Félix Lefrançois
	public void changerConstanteDeRappel(double constanteDeRappel) {
		ressortDebut.setConstanteRappel(constanteDeRappel);
	}

	/**
	 * Méthode retournant l'aire de la zone de propulsion
	 * @return L'aire de la zone de propulsion
	 */
	//Félix Lefrançois
	public Area getAirePropulsion() {
		return airePropulsion;
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
	 * Méthode permettant de changer l'étirement du ressort du début
	 * @param nouveauEtirement Le nouvel étirement du ressort du début
	 */
	//Félix Lefrançois
	public void gererEtirementRessortDepart(double nouveauEtirement) {
		ressortDebut.setEtirement(nouveauEtirement);
	}

	/**Méthode permettant d'avoir le CopyOnWriteArrayList lesObstacles actuel.
	 * @return le CopyOnWriteArrayList lesObstacles
	 */
	//Aimé Melançon
	public CopyOnWriteArrayList<Obstacle> getLesObstacles() {
		return lesObstacles;
	}

	/**Méthode permettant de redéfinir le CopyOnWriteArrayList lesObstacles.
	 * @param lesObstacles Le nouveau CopyOnWriteArrayList lesObstacles.
	 */
	//Aimé Melançon
	public void setLesObstacles(CopyOnWriteArrayList<Obstacle> lesObstacles) {
		this.lesObstacles = lesObstacles;
		repeindre();
	}

	/**
	 * Méthode qui change la masse de toutes les balles lors du jeu
	 * @param masse La nouvelle masse
	 */
	//Félix Lefrançois
	public void changerMasseDesBalles(double masse) {
		masseBalle = masse;

		if (lesBalles == null)
			creerLaGeometrie() ;

		for (Balle balle : lesBalles) {
			balle.setMasse(masseBalle);
		}
	}

	/**
	 * Méthode retournant l'aire où il est possible de placer des obstacles
	 * @return L'aire où il est possible de placer des obstacles
	 */
	//Félix Lefrançois
	public Area getAireOuPossibleDePlacer() {
		return this.aireOuPossibleDePlacer;
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

	/**
	 * Permet de mettre à jour le dessin d'une table sur la scène
	 */
	//Aimé Melançon
	public void repeindre() {
		//System.out.println("CreerLaGeomtetrie de la table");
		creerLaGeometrie();
	}

	/**
	 * Méthode permettant de propulser une balle à partir du ressort.
	 * @param balle : La balle à propulser.
	 * @param etirement : L'étirement voulu du ressort.
	 */
	// Par Elias Kassas
	public void propulser(Balle balle, Vecteur3D etirement) {
		ressortDebut.collision(balle) ;
	}

	/**
	 * Méthode permettant d'avoir l'aire de la table.
	 * @return L'aire de la table
	 */
	//Aimé Melançon
	public Area getAire() {
		return surfaceTable;
	}
}
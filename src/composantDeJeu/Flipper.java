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
import obstacles.Cercle;
import outils.GererSon;
import outils.OutilsSon;
import physique.MoteurPhysique;
import scene.Scene;

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
	private final double LONGUEUR_BARRE = 40, HAUTEUR_BARRE = 5;
	/** Ellispe de la base du flipper **/
	private Ellipse2D.Double baseFlipper;
	/** La barre composant le flipper**/
	private Rectangle2D.Double barreFlipper;
	/** La constante du diamètre de la base du flipper**/
	private final double DIAM_BASE = 10;
	/** Les coordonnées du centre de la base du flipper**/
	private double positionX, positionY;
	/** L'angle de repos du flipper **/
	private double angleRepos;
	/** Constante de l'angle de repos gauche de la barre de flipper (sera additionnée à l'angle de rotation)**/
	private final double ANGLE_REPOS_GAUCHE = Math.PI/6;
	/** Constante de l'angle de repos droit de la barre de flipper (sera additionnée à l'angle de rotation)**/
	private final double ANGLE_REPOS_DROIT = 5*Math.PI/6;
	/** Constante de l'angle final droit de la rotation lorsque cliqué**/
	private final double ANGLE_FINAL_DROIT = 5*Math.PI/4;
	/** Constante de l'angle final gauche de la rotation lorsque cliqué **/
	private final double ANGLE_FINAL_GAUCHE = -Math.PI/4;
	/** L'angle de rotation du flipper à partir du centre**/
	private double rotation;
	/** La couleur du flipper **/
	private Color couleurFlipper = Color.GRAY;
	/** Les aires des différents composants du flipper **/
	private transient Area aireBarre, aireBase;
	/** Objet aire total du flipper**/
	private transient Area aireTotal;
	/** Booléen confirmant si le flipper est en animation **/
	private boolean bouge = false;
	/** La masse d'un flipper **/
	private final int MASSE = 45000;
	/** Les murs qui composent la barre du flipper **/
	private Line2D.Double murHaut, murCote,murBas;
	/** Un array list contenant les murs **/
	private CopyOnWriteArrayList<Line2D.Double> lesMurs;
	/** La rotation du flipper en radian à un moment précis **/
	private double rotationDuMoment;
	/** Le rectangel résultatnt de la transformation de la barre du flipper **/
	private Shape rectangleTransfo;
	/** L'accélération angulaire du flipper **/
	private double accelAngu;
	/** La vitesse angulaire du flipper **/
	private double vitesseAngulaire = 0;
	/** Un booléen confirmant à quelle phase de l'animation se trouve la rotation du flipper **/
	private boolean changementFait = false;
	/** Une chaîne représentant la touche associée au flipper**/
	private String lettreTouche;
	/** La taille de la police de l'affichage de la touche**/
	private final float TAILLE_FONT = 8f;
	/** La distance entre le coin inférieur gauche de la base et le string affichant la touche**/
	private final int DISTANCE_AFF_TOUCHE = 2;
	/** La couleur du string utilisé pour dessiner la lettre représentant la touche **/
	private final Color COULEUR_TOUCHE = Color.BLUE;
	/** La constante d'accélération angulaire**/
	private final int ACCEL_ANGULAIRE = 20;
	/** Constante du nombre de points ajoutés lors d'une collision avec un flipper**/
	private final int POINTS_AJOUTES = 300;
	/** La constante d'incertitude de rotation du flipper**/
	private final double INCERTITUDE_FLIPPER = 0.1;
	/** L'incertitude de rotation du flipper dépendamment du côté choisi**/
	private double incertitudeFlipper;
	/** Booléen confirmant si c'est la première fois que la méthode creerLaGeometrie() est utilisée **/
	private boolean premiereFois = false;
	/** L'aire total du flipper en incluant l'espace qu'il prend durant son animation**/
	private transient Area aireIncluantAnimation;
	/** Nombre de fois où il est nécessaire de recréer l'aire **/
	private final int NB_COPIE = 38;
	/** Le deltaT utilisé pour créer La,ire incluant l'animation du flipper**/
	private final double FAUX_DELTA_T = 0.01;
	/** Les coins du flipper sous forme de cercle **/
	private Cercle cercleSuperieur, cercleInferieur;
	/** L'ensemble des coins du flipper **/
	private CopyOnWriteArrayList<Cercle> lesCoins;
	/** La constante du diamètres des coins**/
	private final int DIAMETRE_DES_COINS = 1;
	/** OutilsSon du flipper**/
	private transient OutilsSon sonFlipper = new OutilsSon();
	/**Nom du fichier du son**/
	private final String NOM_DU_FICHIER_FLIPPER="Flipper.wav";

	/**
	 * Constructeur de la classe Flipper
	 * @param toucheDuFlipper Détermine si l'animation se fait avec la touche de droite ou la touche de gauche
	 * @param posX La coordonnée en x du centre de la base
	 * @param posY La coordonnée en y du centre de la base
	 * @param rotation la rotation en radian du flipper
	 * @param Une chaîne représentant la touche associée au flipper affichée sur la base
	 */
	//Félix Lefrançois
	public Flipper(String toucheDuFlipper, double posX, double posY, double rotation, String lettreTouche) {
		this.toucheDuFlipper = toucheDuFlipper;
		this.positionX = posX;
		this.positionY = posY;
		this.rotation = rotation;
		this.lettreTouche = lettreTouche;
		lesMurs = new CopyOnWriteArrayList<Line2D.Double>();
		lesCoins = new CopyOnWriteArrayList<Cercle>();
		determinerCote();
		creerLaGeometrie();
	}

	/**
	 * Méthode qui crée la géométrie du flipper
	 */
	//https://stackoverflow.com/questions/41898990/find-corners-of-a-rotated-rectangle-given-its-center-point-and-rotation Par l'utilisateur Tonia Sanzo
	//Le code a été légèrement modifiée puisqu'il s'agit d'un calcul des positions à partir du centre d'un rectangle ce qui
	//n'est pas comment la barre est créée puisque c'est son côté gauche qui tourne autour du centre.
	//elle n'affecte en rien la charge de travail planifiée
	//Le code est modifié, donc c'est à 60% le code original
	//Félix Lefrançois
	private void creerLaGeometrie() {
		lesMurs.clear();
		//lesCoins.clear();

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

		cercleSuperieur = new Cercle(new Vecteur3D(murCote.x1,murCote.y1,0),DIAMETRE_DES_COINS,Color.BLACK);
		lesCoins.add(cercleSuperieur);
		cercleInferieur = new Cercle(new Vecteur3D(murCote.x2,murCote.y2,0),DIAMETRE_DES_COINS,Color.BLACK);
		lesCoins.add(cercleInferieur);

		if (!premiereFois) creerAireTotalIncluantAnimation();

		sonFlipper = new OutilsSon() ;
		sonFlipper.chargerUnSonOuUneMusique(NOM_DU_FICHIER_FLIPPER) ;
	}

	/**
	 * Méthode permettant de créer l'aire total du flipper en incluant les positions possibles durant son animation
	 */
	//Félix Lefrançois
	private void creerAireTotalIncluantAnimation() {
		aireIncluantAnimation = new Area(baseFlipper);
		double copieVitesseAngulaire = vitesseAngulaire;
		double copieRotationDuMoment = rotationDuMoment;
		for(int recurrence =0;recurrence < NB_COPIE;recurrence++) {
			AffineTransform mat2 = new AffineTransform();
			mat2.translate(positionX, positionY);
			mat2.rotate(copieRotationDuMoment);
			aireIncluantAnimation.add(new Area (mat2.createTransformedShape(barreFlipper)));
			copieVitesseAngulaire = MoteurPhysique.vitesseAngulaire(copieVitesseAngulaire, accelAngu, FAUX_DELTA_T);
			copieRotationDuMoment = MoteurPhysique.positionAngu(copieRotationDuMoment, copieVitesseAngulaire, accelAngu, FAUX_DELTA_T);
			gererAcceleration();
		}

		sonFlipper = new OutilsSon() ;
		sonFlipper.chargerUnSonOuUneMusique(NOM_DU_FICHIER_FLIPPER);

		premiereFois = true;
	}

	/**
	 * Méthode permettant de dessiner un flipper
	 * @param g2d Contexte graphique
	 */
	//Félix Lefrançois
	public void dessiner(Graphics2D g2d) {
		Graphics2D g2dPrive = (Graphics2D) g2d.create();

		if (aireTotal == null) {
			creerLaGeometrie() ;
		}

		if (lesCoins == null) {
			lesCoins = new CopyOnWriteArrayList<Cercle>() ;
			creerLaGeometrie() ;
		}

		g2dPrive.setColor(couleurFlipper);
		g2dPrive.fill(aireTotal);
		g2dPrive.setColor(COULEUR_TOUCHE);
		g2dPrive.setFont(g2dPrive.getFont().deriveFont(TAILLE_FONT));
		g2dPrive.drawString(lettreTouche + "",(int)(positionX-DIAM_BASE/2+DISTANCE_AFF_TOUCHE), (int)(positionY+DIAM_BASE/2-DISTANCE_AFF_TOUCHE));
		g2dPrive.setColor(Color.BLACK);
	}

	/**
	 * Méthode pour déterminer et appliquer les changements selon le côté du flipper
	 */
	//Félix Lefrançois
	private void determinerCote() {
		if (toucheDuFlipper.equalsIgnoreCase("Gauche")) {
			angleRepos = ANGLE_REPOS_GAUCHE;
			rotationDuMoment = ANGLE_REPOS_GAUCHE + rotation;
			accelAngu = -ACCEL_ANGULAIRE;
			incertitudeFlipper = INCERTITUDE_FLIPPER;
		} else if (toucheDuFlipper.equalsIgnoreCase("Droit")) {
			angleRepos = ANGLE_REPOS_DROIT;
			rotationDuMoment = ANGLE_REPOS_DROIT - rotation;
			accelAngu = ACCEL_ANGULAIRE;
			incertitudeFlipper = -INCERTITUDE_FLIPPER;
		}
	}

	/**
	 * Méthode permettant de changer l'accélération en fonction de l'angle de rotation du flipper
	 */
	//Félix Lefrançois
	private void gererAcceleration() {
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
	//Félix Lefrançois
	public void commencerAnimation() {
		bouge = true;
	}

	/**
	 * Néthode permettant de vérifier si une balle entre en collision avec un flipper
	 * @param balle La balle dont on vérifie la collision avec le flipper
	 * @return Un booléen confirmant s'il y a collision
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
	 * @throws Exception Si le vecteur ne peut être normalisé si il est de longueur trop petite ou nulle
	 */
	//Félix Lefrançois
	public void appliquerCollisionFlipper(Balle balle) throws Exception {
		Area aireBarreTemp = new Area(aireBarre);
		Area aireBaseTemp = new Area(aireBase);
		Area aireBalleTemp = new Area(balle.getAireBalle());

		Line2D.Double murVerif = Table.trouverMurLePlusProche(balle, lesMurs);

		aireBarreTemp.intersect(aireBalleTemp);
		aireBaseTemp.intersect(aireBalleTemp);
		boolean collisionCoin = false;

		if (!aireBaseTemp.isEmpty()) {
			balle.setVitesse(CollisionDesObjets.vitesseFinaleMouvementCercleImmobile(balle, 
					MASSE, positionX+DIAM_BASE/2, positionY+DIAM_BASE/2));
			Vecteur3D normale = CollisionDesObjets.trouverNormaleCollisionCercle(balle, positionX+DIAM_BASE/2, positionY+DIAM_BASE/2);
			balle.setPosition(balle.getPosition().additionne(normale));
			Scene.getPoints().ajouterPoints(POINTS_AJOUTES);

			if (sonFlipper == null)
				creerLaGeometrie() ;
			if(GererSon.isAllumerFermer()) {
				sonFlipper.jouerUnSon();
			}
		} else if (murVerif != null) {
			if (!bouge) {

				for (Cercle coin : lesCoins) {
					if(coin.intersection(balle)) {
						coin.collision(balle);
						Vecteur3D normale = CollisionDesObjets.trouverNormaleCollisionCercle(balle, coin.getPosition().getX(), coin.getPosition().getY());
						balle.setPosition(balle.getPosition().additionne(normale));
						collisionCoin = true;
					}
				}

				if (!collisionCoin) {
					Vecteur3D normale = CollisionDesObjets.trouverNormaleCollisionMur(balle, murVerif);
					balle.setVitesse(CollisionDesObjets.vitesseFinaleImmobile(balle.getVitesse(),
							normale));
					balle.setPosition(balle.getPosition().additionne(normale));
					Scene.getPoints().ajouterPoints(POINTS_AJOUTES);
					if(GererSon.isAllumerFermer()) {
						sonFlipper.jouerUnSon();
					}
				}
			} else if (bouge) {

				for (Cercle coin : lesCoins) {
					if(coin.intersection(balle)) {
						double vitesse = vitesseAngulaire*LONGUEUR_BARRE;
						Vecteur3D vecteurVitesse = new Vecteur3D(-vitesse*Math.sin(rotationDuMoment),vitesse*Math.cos(rotationDuMoment),0);
						Vecteur3D vitesseResultante = CollisionDesObjets.vitesseFinaleMouvementObstacleCercleMobile(balle, coin, vecteurVitesse);
						balle.setVitesse(vitesseResultante);
						balle.setPosition(balle.getPosition().additionne(new Vecteur3D(balle.obtenirCentreX()-coin.getPosition().getX()
								,balle.obtenirCentreY()-coin.getPosition().getY(),0).normalise()));
						collisionCoin = true;
					}
				}

				if (!collisionCoin) {
					double deltaX = balle.obtenirCentreX()-positionX;
					double deltaY = balle.obtenirCentreY()-positionY;
					Vecteur3D vecBaseBalle = new Vecteur3D(deltaX,deltaY,0);

					Vecteur3D directeur = new Vecteur3D(murVerif.x2-murVerif.x1,murVerif.y2-murVerif.y1,0);

					Vecteur3D projection = Vecteur3D.projectionOrthogonale(vecBaseBalle, directeur);
					double rayon= projection.module();

					double vitesse = vitesseAngulaire*rayon;
					Vecteur3D vecteurVitesse = new Vecteur3D(-vitesse*Math.sin(rotationDuMoment),vitesse*Math.cos(rotationDuMoment),0);

					Vecteur3D normale = CollisionDesObjets.trouverNormaleCollisionMur(balle, murVerif);

					balle.setVitesse(CollisionDesObjets.vitesseFinaleCollisionAvecMurMobile(balle, normale, vecteurVitesse, MASSE));
					balle.setPosition(balle.getPosition().additionne(normale));

					rotationDuMoment += incertitudeFlipper;

					Scene.getPoints().ajouterPoints(POINTS_AJOUTES);
					if(GererSon.isAllumerFermer()) {
						sonFlipper.jouerUnSon();
					}
				}
			}
		}
	}

	/**
	 * Méthode retournant la touche à laquelle le flipper est associé
	 * @return Le côté du flipper à partir duquel le joueur frappe
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
		determinerCote();
		creerLaGeometrie();
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
		creerLaGeometrie();
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
		creerLaGeometrie();
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
		creerLaGeometrie();
	}

	/**
	 * Méthode permettant d'obtenir la chaîne de caractère représentant la touche associée au flipper
	 * @return La lettre représentant la touche associée au flipper
	 */
	//Félix Lefrançois
	public String getLettreTouche() {
		return lettreTouche;
	}

	/**
	 * Méthode permettant de changer la chaîne de caractère représentant la lettre affichée sur le flipper
	 * @param lettreTouche La nouvelle lettre
	 */
	//Félix Lefrançois
	public void setLettreTouche(String lettreTouche) {
		this.lettreTouche = lettreTouche;
	}

	/**
	 * Méthode retournant l'aire total d'un flipper en incluant son animation
	 * @return L'aire du flipper incluant son animation
	 */
	//Félix Lefrançois
	public Area getAireIncluantAnimation() {
		return aireIncluantAnimation;
	}
}

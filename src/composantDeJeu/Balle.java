package composantDeJeu;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

import interfaces.Dessinable;
import math.vecteurs.FlecheVectorielle;
import math.vecteurs.Vecteur3D;
import physique.MoteurPhysique;

/**
 * Classe permettant la création des objets de type Balle
 * @author Félix Lefrançois
 * @author Elias Kassas
 */
public class Balle implements Dessinable, Serializable {
	/** Coefficient de sérialisation pour les fichiers. **/
	private static final long serialVersionUID = 1L;
	/** La constante de diametre d'une balle (ne varie pas)**/
	private final static double DIAMETRE = 8.0;
	/** La masse initiale de la balle**/
	private double masse = 5;
	/** Vecteur position de la balle**/
	private Vecteur3D position;
	/** Vecteur vitesse de la vitesse**/
	private Vecteur3D vitesse = new Vecteur3D(0,0);
	/** Vecteur accélération de la balle**/
	private Vecteur3D acceleration = new Vecteur3D(0,0);
	/** Cercle qui sert de corps à la balle**/
	private Ellipse2D.Double cercle;
	/** Aire de la balle**/
	private transient Area aireBalle;
	/** Couleur de la balle**/
	private Color couleurBalle = Color.GRAY;
	/** La flèche visuelle servant à représenter la vitesse de la balle **/
	private FlecheVectorielle flecheVitBalle;
	/** Le coefficient de redimensionnement de la flèche visuelle de la vitesse de la balle **/
	private final double COEF_REDIM = 0.1;
	/** Le vecteur visuel représentant la vitesse de la balle **/
	private final Color COULEUR_FLECHE = Color.ORANGE;
	/** La somme de toutes les forces appliquées sur la balle **/
	private Vecteur3D sommeDesForcesSurBalle;
	/** Les différentes forces appliquées sur la balle sous forme de Vecteur3D**/
	private Vecteur3D forceGrav, forceFrott, forceMagn, forceGravGenerale;
	/** Le vecteur contenant l'énergie totale de la balle**/
	private double sommeDesEnergiesSurBalle;
	/** Les différents vecteurs d'énergie de la balle**/
	private double energieGrav, energieCinetique;
	/** La charge de la balle en Coulombs (C). Cette propriété est temporaire le temps que nous 
	 * codions la classe Materiau. **/
	private double charge = -10 ;
	/** Liste des forces appliquées sur la balle.
	 * Ordre des forces dans la liste : {force gravitationelle, force du trou noir, force de rappel, 
	 * force magnétique, force de frottement, force normale} **/
	private CopyOnWriteArrayList<Vecteur3D> lesForces ;

	/**
	 * Constructeur de la classe Balle
	 * @param position Vecteur position de la balle lorsqu'elle est créée
	 * @param masse La masse en kg de la balle (TEMPORAIREMENT DÉSACTIVÉ)
	 */
	//Félix Lefrançois
	public Balle(Vecteur3D position,double masse) {
		this.position = position;
		this.masse = masse;
		this.vitesse = new Vecteur3D(5, -100, 0) ;
		creerLaGeometrie();
	}

	/**
	 * Création de la géométrie de la balle
	 */
	//Félix Lefrançois
	public void creerLaGeometrie() {
		cercle = new Ellipse2D.Double(position.getX(), position.getY(), DIAMETRE, DIAMETRE);
		aireBalle = new Area(cercle);
		flecheVitBalle = new FlecheVectorielle(position.getX()+DIAMETRE/2, position.getY()+DIAMETRE/2,vitesse);
		flecheVitBalle.redimensionneCorps(COEF_REDIM);
	}


	@Override
	/**
	 * Methode de dessin d'un objet de type Balle
	 * @param Contexte graphique
	 */
	//Félix Lefrançois
	public void dessiner(Graphics2D g2d) {
		Graphics2D g2dPrive = (Graphics2D) g2d.create();

		g2dPrive.setColor(couleurBalle);
		g2dPrive.fill(aireBalle);

		g2dPrive.setColor(COULEUR_FLECHE);
		flecheVitBalle.dessiner(g2dPrive);
	}

	/**
	 * Méthode permettant de faire un saut de pas à chaque deltaT durant une animation
	 * @param deltaT La différence de temps simulé
	 * @throws Exception Erreur si la masse est pratiquement nulle
	 */
	//Félix Lefrançois
	public void avancerUnPas(double deltaT) throws Exception {
		vitesse = MoteurPhysique.calculVitesse(deltaT, vitesse, acceleration);
		position = MoteurPhysique.calculPosition(deltaT, position, vitesse);
		creerLaGeometrie();
	}

	/**
	 * Méthode modifiant l'accélération de la balle grâce à la somme des forces (Équation F=ma)
	 */
	//Félix Lefrançois
	public void calculerAcceleration() {
		try {
			acceleration = MoteurPhysique.calculAcceleration(sommeDesForcesSurBalle, masse);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Méthode permettant de calculer les forces appliquées sur la balle en tout temps
	 * @param angle L'angle d'inclinaison de la table
	 * @param coefFrot Le coefficient de frottement de la surface de la table
	 * @throws Exception Si le vecteur ne peut être normalisée puisqu'il est de longueur trop petite ou nulle
	 */
	//Félix Lefrançois
	public Vecteur3D calculerSommeDesForcesConstantes(double angle, double coefFrot) throws Exception {
		forceGrav = MoteurPhysique.calculForceGrav(masse, angle);
		sommeDesForcesSurBalle = forceGrav;
		if (vitesse.module() > 0) {
			forceFrott = MoteurPhysique.calculForceFrottement(masse, coefFrot, this.vitesse);
			sommeDesForcesSurBalle = sommeDesForcesSurBalle.additionne(forceFrott);
		}
		forceMagn = new Vecteur3D(0,0,0);
		forceGravGenerale = new Vecteur3D(0,0,0);

		return sommeDesForcesSurBalle;
	}

	/**
	 * Méthode permettant d'appliquer des forces extérieures sur la balle
	 * @param forceAppliquee La force appliquee sur la balle
	 */
	//Félix Lefrançois
	public void ajouterASommeDesForces(Vecteur3D forceAppliquee) {
		sommeDesForcesSurBalle = sommeDesForcesSurBalle.additionne(forceAppliquee);
	}

	/**
	 * Méthode permettant de calculer la somme totale des énergies
	 * @param angle L'angle d'inclinaison de la table
	 * @param coefFrot Le coefficient de frottement de la surface de la table
	 * @return La somme totale des énergies
	 */
	//Félix Lefrançois
	public double calculSommeDesEnergies(double angle, double coefFrot) {
		energieCinetique = MoteurPhysique.energieCinetique(vitesse, masse);
		energieGrav = MoteurPhysique.energieGravitationnelle(masse, new Vecteur3D(obtenirCentreX(),obtenirCentreY()));
		sommeDesEnergiesSurBalle = energieCinetique + energieGrav;
		return sommeDesEnergiesSurBalle;
	}

	/**
	 * Méthode pour obtenir la masse en kg de la balle
	 * @return La masse en kg de la balle
	 */
	//Félix Lefrançois
	public double getMasse() {
		return masse;
	}

	/**
	 * Méthode pour changer la masse en kg de la balle
	 * @param masse La nouvelle masse en kg de la balle
	 */
	//Félix Lefrançois
	public void setMasse(double masse) {
		this.masse = masse;
	}

	/**
	 * Méthode pour obtenir le Vecteur3D représentant la position vectorielle de la balle
	 * @return La position vectorielle de la balle
	 */
	//Félix Lefrançois
	public Vecteur3D getPosition() {
		return position;
	}

	/**
	 * Méthode pour modifier la position vectorielle de la balle
	 * @param position La nouvelle position vectorielle de la balle
	 */
	//Félix Lefrançois
	public void setPosition(Vecteur3D position) {
		this.position = position;
		creerLaGeometrie();
	}

	/**
	 * Méthode changeant l'aire de la balle
	 * @param nouvelAire Le nouveal aire de la balle
	 */
	//Félix Lefrançois
	public void setAireBalle(Area nouvelAire) {
		aireBalle = nouvelAire;
		creerLaGeometrie();
	}

	/**
	 * Méthode retournant l'aire de la balle
	 * @return L'aire de la balle
	 */
	//Félix Lefrançois
	public Area getAireBalle() {
		return aireBalle;
	}

	/**
	 * Méthode pour obtenir le Vecteur3D représentant la vitesse vectorielle de la balle
	 * @return La vitesse vectorielle de la balle
	 */
	//Félix Lefrançois
	public Vecteur3D getVitesse() {
		return vitesse;
	}

	/**
	 * Méthode pour changer la vitesse vectorielle de la balle
	 * @param vitesse La nouvelle vitesse vectorielle de la balle
	 */
	//Félix Lefrançois
	public void setVitesse(Vecteur3D vitesse) {
		this.vitesse = vitesse;
	}

	/**
	 * Méthode pour obtenir le Vecteur3D représentant l'accélération de la balle
	 * @return L'accélération vectorielle de la balle
	 */
	//Félix Lefrançois
	public Vecteur3D getAcceleration() {
		return acceleration;
	}

	/**
	 * Méthode pour changer l'accélération d'une balle
	 * @param acceleration La nouvelle accélération de la balle
	 */
	//Félix Lefrançois
	public void setAcceleration(Vecteur3D acceleration) {
		this.acceleration = acceleration;
	}

	/**
	 * Méthode pour obtenir la nouvelle couleur de la balle
	 * @return La couleur de la balle
	 */
	//Félix Lefrançois
	public Color getCouleurBalle() {
		return couleurBalle;
	}

	/**
	 * Méthode pour changer la nouvelle couleur de la balle si désiré
	 * @param couleurBalle Nouvelle couleur de la balle
	 */
	//Félix Lefrançois
	public void setCouleurBalle(Color couleurBalle) {
		this.couleurBalle = couleurBalle;
		creerLaGeometrie();
	}

	/**
	 * Méthode retournant le diamètre de la balle
	 * @return Le diamètre de la balle
	 */
	//Félix Lefrançois
	public static double getDiametre() {
		return DIAMETRE;
	}
	
	/**
	 * Méthode retournant la force gravitationnelle appliquée sur la balle
	 * @return Retourne la force gravitationnelle appliquée sur la balle
	 */
	//Félix Lefrançois
	public Vecteur3D getForceGrav() {
		return forceGrav;
	}
	
	/**
	 * Méthode permettant de changer la force gravitationnelle appliquée sur la balle
	 * @param nouvelleForce La nouvelle force gravitationnelle
	 */
	//Félix Lefrançois
	public void setForceGrav(Vecteur3D nouvelleForce) {
		this.forceGrav = nouvelleForce;
	}
	
	/**
	 * Méthode retournant la force de frottement appliquée sur la balle
	 * @return Retourne la force de frottement appliquée sur la balle
	 */
	//Félix Lefrançois
	public Vecteur3D getForceFrott() {
		return forceFrott;
	}
	
	/**
	 * Méthode permettant de changer la force de frottement appliquée sur la balle
	 * @param nouvelleForce La nouvelle force de frottement
	 */
	//Félix Lefrançois
	public void setForceFrott(Vecteur3D nouvelleForce) {
		forceFrott = nouvelleForce;
	}
	
	/**
	 * Méthode retournant la force magnétique appliquée sur la balle
	 * @return Retourne la force magnétique appliquée sur la balle
	 */
	//Félix Lefrançois
	public Vecteur3D getForceMagn() {
		return forceMagn;
	}
	
	/**
	 * Méthode permettant de changer la force magnétique appliquée sur la balle
	 * @param nouvelleForce La nouvelle force magnétique
	 */
	//Félix Lefrançois
	public void setForceMagn(Vecteur3D nouvelleForce) {
		forceMagn = nouvelleForce;
	}
	
	/**
	 * Méthode retournant la force gravitationnelle générale appliquée sur la balle
	 * @return Retourne la force gravitationnelle générale appliquée sur la balle
	 */
	//Félix Lefrançois
	public Vecteur3D getForceGravGenerale() {
		return forceGravGenerale;
	}
	
	/**
	 * Méthode permettant de changer la force gravitationnelle générale appliquée sur la balle
	 * @param nouvelleForce La nouvelle force gravitationnelle générale
	 */
	//Félix Lefrançois
	public void setForceGravGenerale(Vecteur3D nouvelleForce) {
		forceGravGenerale = nouvelleForce;
	}

	/**
	 * Méthode retournant la coordonnée en x du centre de la balle
	 * @return La coordonnée en x du centre de la balle
	 */
	//Félix Lefrançois
	public double obtenirCentreX() {
		return (position.getX()+DIAMETRE/2);
	}

	/**
	 * Méthode retournant la coordonnée en y du centre de la balle
	 * @return La coordonnée en y du centre de la balle
	 */
	//Félix Lefrançois
	public double obtenirCentreY() {
		return (position.getY()+DIAMETRE/2);
	}

	/**
	 * Méthode qui retourne un série de caractères expliquant la balle
	 */
	//Félix Lefrançois
	public String toString() {
		return "La balle se trouve à la position : "+position.toString()+", va à une vitesse : "
				+vitesse.toString() + " et a une accélération : " + acceleration.toString() + ". "
						+ "La balle pèse aussi " + masse + " kg.";
	}

	/**
	 * Méthode permettant d'obtenir la charge de la balle.
	 * NOTE : Cette méthode ainsi que la propriété de classe est temporaire le temps que nous 
	 * codions la classe de Materiau pour la balle.
	 * @return La charge de la balle.
	 */
	// Par Elias Kassas
	public double getCharge() {
		return charge ;
	}
	
	/**
	 * Méthode permettant d'obtenir les forces appliquées sur la balle.
	 * Ordre des forces dans la liste : {force gravitationelle, force du trou noir, force de rappel, 
	 * force magnétique, force de frottement}
	 * @return La liste des forces appliquées sur la balle.
	 */
	// Par Elias Kassas
	public CopyOnWriteArrayList<Vecteur3D> getForces() {
		Vecteur3D[] forces = {forceGrav, forceGravGenerale, /*vecteur fR*/ new Vecteur3D(9, 9), 
				forceMagn, forceFrott} ;
		lesForces = new CopyOnWriteArrayList<Vecteur3D>(forces) ;
		
		return this.lesForces ;
	}
	
	/**
	 * 
	 * @return
	 */
	// Par Elias Kassas
	public Vecteur3D getSommeDesForces() {
		return this.sommeDesForcesSurBalle ;
	}
} 


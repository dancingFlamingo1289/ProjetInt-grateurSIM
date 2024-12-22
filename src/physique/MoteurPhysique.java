package physique;

import math.vecteurs.Vecteur3D;

/** Cette classe regroupera les calculs physiques n�cessaires au mouvement des objets
 * des divers objets dans la sc�ne.
 * Utilise la m�thode d'int�gration num�rique d'Euler semi-implicite. 
 *  
 * @author Caroline Houle
 * @author Aimé Melançon
 * @author Elias Kassas
 */
public class MoteurPhysique {
	//Constantes//
	/**La force gravitationnelle de la Terre**/
	private static final double ACCEL_G = 9.80665;
	/**La force gravitationnelle de la Lune**/
	private static final double LUNE_G = 1.62;
	/**La force gravitationnelle de Mercure**/
	private static final double MERCURE_G = 3.70;
	/**La force gravitationnelle de Vénus**/
	private static final double VENUS_G =8.87;
	/**La force gravitationnelle de Mars**/
	private static final double MARS_G = 3.71;
	/**La force gravitationnelle de Jupiter**/
	private static final double JUPITER_G = 24.79;
	/**La force gravitationnelle de Saturne**/
	private static final double SATURNE_G = 10.44;
	/**La force gravitationnelle d'Uranus**/
	private static final double URANUS_G = 8.69;
	/**La force gravitationnelle de Neptune**/
	private static final double NEPTUNE_G = 11.15;
	/**La force gravitationnelle de Pluton**/
	private static final double PLUTON_G = 0.62;
	/** Tolérance utilisée dans les comparaisons réelles avec zéro**/
	private static final double EPSILON = 1e-10;
	/**Constante de la gravitation universelle. **/
	private static final double GRAND_G = 6.67e-11;

	//Variables//
	/** La constante gravitationnelle variable **/
	private  static double g = ACCEL_G;
	/** L'angle de la force gravitationnelle conservé pour la force normale. **/
	private static double angleForceGrav;
	/** La normale de l'objet en mouvement **/
	private static double normale;

	/**
	 * Méthode permettant de faire la conversion d'unités en centimètres vers des unités en mètres.
	 * @param vecteurEnCm : Le vecteur contenant des composantes en centimètres au numérateur.
	 * @return Le même vecteur avec des unités en mètres au numérateur.
	 */
	// Par Elias Kassas
	public static Vecteur3D convertirCentimetresEnMetres(Vecteur3D vecteurEnCm) {
		return Vecteur3D.multiplie(vecteurEnCm, 1.0/100.0) ;
	}

	/**
	 * Méthode permettant de faire la conversion d'unités en centimètres vers des unités en mètres.
	 * @param valEnCm : La contenant des coordonnées en centimètres au numérateur.
	 * @return La même valeur avec des unités en mètres au dénominateur.
	 */
	// Par Elias Kassas
	public static double convertirCentimetresEnMetres(double valEnCm) {
		return valEnCm/100.0 ;
	}

	/**
	 * Méthode permettant de faire la conversion d'unités en mètres vers des unités en centimètres.
	 * @param vecteurEnM : Le vecteur contenant des coordonnées en mètres au numérateur.
	 * @return Le même vecteur avec des unités en centimètres au dénominateur.
	 */
	// Par Elias Kassas
	public static Vecteur3D convertirMetresEnCentimetres(Vecteur3D vecteurEnM) {
		return Vecteur3D.multiplie(vecteurEnM, 100.0) ;
	}

	/**
	 * Méthode permettant de faire la conversion d'unités en mètres vers des unités en centimètres.
	 * @param valEnM : La contenant des coordonnées en centimètres au numérateur.
	 * @return La même valeur avec des unités en centimètres au dénominateur.
	 */
	// Par Elias Kassas
	public static double convertirMetresEnCentimetres(double valEnM) {
		return valEnM*100.0 ;
	}

	/**
	 * Calcule et retourne l'accélération en utilisant F=ma
	 * @param sommeDesForces Somme des forces appliquées
	 * @param masse Masse de l'objet
	 * @return l'acceletation F/m
	 * @throws Exception Erreur si la masse est pratiquement nulle
	 */
	//Caroline Houle
	public static Vecteur3D calculAcceleration(Vecteur3D sommeDesForces, double masse) throws Exception { 
		if(masse < EPSILON) 
			throw new Exception("Erreur MoteurPhysique: La masse étant nulle ou presque l'accéleration ne peut pas etre calcul�e.");
		else
			return new Vecteur3D( sommeDesForces.getX()/masse , sommeDesForces.getY()/masse, sommeDesForces.getZ()/masse );	
	}

	/**
	 * Calcule et retourne la nouvelle vitesse, deltaT secondes plus tard, en utilisant l'algorithme
	 * d'Euler semi-implicite.
	 * @param deltaT L'intervalle de temps (petit!) en secondes
	 * @param vitesse La vitesse initiale au debut de l'intervalle de temps, en m/s
	 * @param accel L'acceleration initiale au debut de l'intervalle de temps, en m/s2
	 * @return La nouvelle vitesse (a la fin de l'intervalle)
	 */
	//Caroline Houle
	public static Vecteur3D calculVitesse(double deltaT, Vecteur3D vitesse, Vecteur3D accel) {
		Vecteur3D deltaVitesse = Vecteur3D.multiplie(accel, deltaT);
		Vecteur3D resultVit = vitesse.additionne( deltaVitesse );
		return new Vecteur3D(resultVit.getX(), resultVit.getY());

	}

	/**
	 * Calcule et retourne la nouvelle position, deltaT secondes plus tard, en utilisant l'algorithme
	 * d'Euler semi-implicite.
	 * @param deltaT L'intervalle de temps (petit!) en secondes
	 * @param position La position initiale au debut de l'intervalle de temps, en m
	 * @param vitesse La vitesse initiale au debut de l'intervalle de temps, en m/s
	 * @return La nouvelle position (a la fin de l'intervalle)
	 */
	//Caroline Houle
	public static Vecteur3D calculPosition(double deltaT, Vecteur3D position, Vecteur3D vitesse) {
		Vecteur3D deltaPosition = Vecteur3D.multiplie(vitesse, deltaT);
		Vecteur3D resultPos = position.additionne(deltaPosition); 
		return new Vecteur3D(resultPos.getX(), resultPos.getY());

	}

	/**
	 * Forme et retourne un vecteur exprimant la force gravitationnelle sur la Terre s'appliquant sur un objet dont la masse est passée en parametre.
	 * @param masse Masse de l'objet
	 * @param angle L'angle de la force
	 * @return Un vecteur représentant la force gravitationnelle exercée
	 */
	//Aimé Melançon
	public static Vecteur3D calculForceGrav(double masse, double angle) {
		return new Vecteur3D(0, g*masse*Math.sin(angle));

	}

	/**
	 * Méthode permettant de récupérer la constante de gravitation sélectionnée.
	 * 
	 * @return g La variable de gravitation
	 */
	//Aimé Melançon
	public static double getG() {
		return g;
	}

	/**
	 * Méthode qui met à jour la constante de gravitation.
	 * 
	 * @param gPlanete Le nom de la planète
	 * @throws Exception Envoie l'exception en cas d'erreur d'écriture du nom de l'astre
	 */
	//Aimé Melançon
	public static void setG(String gPlanete) throws Exception {
		switch(gPlanete) {
		case("Terre"):
			g=ACCEL_G;
		break;
		case("Lune"):
			g=LUNE_G;
		break;
		case("Mercure"):
			g=MERCURE_G;
		break;
		case("Vénus"):
			g=VENUS_G;
		break;
		case("Mars"):
			g=MARS_G;
		break;
		case("Jupiter"):
			g=JUPITER_G;
		break;
		case("Saturne"):
			g= SATURNE_G;
		break;
		case("Uranus"):
			g=URANUS_G;
		break;
		case("Neptune"):
			g=NEPTUNE_G;
		break;
		case("Pluton"):
			g=PLUTON_G;
		break;
		default:
			throw new Exception("Erreur de constante de gravitation");
		}
	}
	/**
	 * Forme et retourne un vecteur de type Vecteur3D illustrant la force de frottement.
	 * 
	 * @param masse masse de l'objet en mouvement
	 * @param coefficientDeFrottement Le coefficient de frottement (μ) 
	 * @param VecVitesse Le vecteur vitesse de l'objet en mouvement
	 * @return Un vecteur représentant la force de frottement exercée
	 * @throws Exception Exception pour la normalisation d'un vecteur nul
	 */
	//Aimé Melançon
	public static Vecteur3D calculForceFrottement(double masse,double coefficientDeFrottement, 
			Vecteur3D vecVitesse) throws Exception {
		normale= masse*g*Math.cos(angleForceGrav);

		Vecteur3D forceFrottement = convertirCentimetresEnMetres(vecVitesse).normalise().multiplie(
				-coefficientDeFrottement*normale);
		return forceFrottement;
	}

	/**
	 * Méthode permettant d'effectuer la somme des forces.
	 * @param forces L'ArrayList contenant les forces
	 * @return Retourne la somme des forces en nouveau Vecteur3D
	 */
	//Aimé Melançon
	/*public static Vecteur3D sommesDesForces( ArrayList<Vecteur3D> forces) {
		Vecteur3D somme = new Vecteur3D();
		for(Vecteur3D i: forces) {
			somme= somme.additionne(i);
		}
		sommeDesForces = forces;
		return somme;
	}*/

	/**
	 * Méthode qui forme un vecteur de type Vecteur3D illustrant la force magnétique.
	 * @param champMagnetique Le vecteur du champ magnétique
	 * @param vecVit Le vecteur vitesse
	 * @param charge La charge 
	 * @return La force magnétique
	 */
	//Aimé Melançon
	public static Vecteur3D forceMagnetique(Vecteur3D champMagnetique, Vecteur3D vecVit, 
			double charge) {
		Vecteur3D force = convertirCentimetresEnMetres(vecVit).prodVectoriel(champMagnetique);

		return force.multiplie(charge) ;
	}

	/**
	 * Méthode permettant de calculer la force du ressort.
	 * 
	 * @param constanteDeRappel Constante de rappel du ressort/pare-chocs en N/m.
	 * @param VecEtirOuCompress Vecteur étirement ou compression du pare-chocs en cm
	 * @return Force de rappel
	 */
	//Aimé Melançon
	public static Vecteur3D forceDuRessort(double constanteDeRappel, Vecteur3D VecEtirOuCompress) {		
		return convertirCentimetresEnMetres(VecEtirOuCompress).multiplie(-constanteDeRappel);
	}

	/**
	 * Méthode permettant de calculer l'énergie cinétique.
	 * @param vecVit Le vecteur vitesse (m/s)
	 * @param masse La masse de l'objet (kg)
	 * @return l'énergie cinétique (J)
	 */
	//Aimé Melançon
	public static double energieCinetique(Vecteur3D vecVit, double masse) {
		return Math.pow(vecVit.module(), 2)*masse/2 ;
	}

	/**
	 * Méthode permettant de calculer l'énergie potentielle gravitationnelle.
	 * @param masse La masse de l'objet
	 * @param position La position de l'objet
	 * @return L'énergie potentielle gravitationnelle
	 */
	//Aimé Melançon
	public static double energieGravitationnelle(double masse, Vecteur3D position) {
		return masse*g*convertirCentimetresEnMetres(position).getY();
	}

	/**
	 * Méthode permettant de calculer l'énergie potentielle du ressort
	 * 
	 * @param k constante des ressorts
	 * @param e déformation des ressorts en cm
	 * @return L'énergie potentielle des ressorts
	 */
	//Aimé Melançon
	public static double energieRessort(double k, Vecteur3D e) {
		return 1/2*k*Math.pow(convertirCentimetresEnMetres(e).module(), 2);
	}

	/**
	 * Méthode permettant de calculer la force gravitationnelle générale.
	 * 
	 * @param masseA Masse qui subit l'influence du champ gravitationnel (kg)
	 * @param masseB Masse qui produit le champ gravitationnel (kg)
	 * @param distance Ditance entre les deux objets
	 * @param rVec Vecteur unitaire de M (source) à m (cible)
	 * @return Force gravitationnelle subit par m (N)
	 */
	//Aimé Melançon
	public static Vecteur3D forceGravitationnelleGenerale(double masseA, double masseB, double distance, Vecteur3D rVec) {
		return rVec.multiplie(-(GRAND_G*(masseA*masseB)/Math.pow(convertirCentimetresEnMetres(distance),2)));

	}

	/**
	 * Méthode permettant de calculer le champ gravitationnel.
	 * 
	 * @param masse Masse qui produit le champ gravitationnel (kg)
	 * @param rdistance Distance entre deux objets en cm
	 * @param rVec Vecteur unitaire de M (source) à m (cible)
	 * @return Champ gravitationnel produit par M (N/kg)
	 */
	//Aimé Melançon
	public static Vecteur3D champGravitationnel(double masse, double distance, Vecteur3D rVec) {
		return rVec.multiplie(-GRAND_G*(masse/Math.pow(convertirCentimetresEnMetres(distance), 2)));
	}

	/**
	 * Méthode permettant de calculer le déplacement du flipper. 
	 * @param angleDepart L'angle de repos du flipper
	 * @param vitesseDeRotationMax La vitesse maximum de rotation du flipper
	 * @param temps Le delta t qui permet d'évoluer dans le temps 
	 * @param accelRotation L'accélération de rotation
	 * @return Retourne la nouvelle position dans l'angle du flipper
	 * @throws Exception L'exception de la méthode pour fonctionner.
	 */
	//Aimé Melançon
	public static double mouvementCirculaireDuFlipper(double angleDepart, double vitesseDeRotationMax, double temps, double accelRotation) throws Exception {
		if(accelRotation>0) {
			return angleDepart+vitesseDeRotationMax*temps-accelRotation/2*Math.pow(temps, 2);
		}else {
			throw new Exception("L'accélération de rotation n'est pas conforme.");
		}
	}

	/**
	 * Méthode permettant de calculer la nouvelle vitesse angulaire
	 * @param vitesseAngu La vitesse angulaire initiale en rad/s
	 * @param accelAngu L'accélération angulaire en rad/s^2
	 * @param deltaT La différence de temps
	 * @return La valeur de la vitesse angulaire résultante
	 */
	//Félix Lefrançois
	public static double vitesseAngulaire(double vitesseAngu, double accelAngu, double deltaT) {
		return vitesseAngu + accelAngu*deltaT;
	}

	/**
	 * Méthode permettant de calculer une nouvelle position angulaire
	 * @param positionAngu La position angulaire initiale en rad
	 * @param vitesseAngu La vitesse angulaire initiale en rad/s
	 * @param accelAngu L'accélération angulaire en rad/s*s
	 * @param deltaT La différence de temps
	 * @return La nouvelle position angulaire
	 */
	//Félix Lefrançois
	public static double positionAngu(double positionAngu, double vitesseAngu, double accelAngu, double deltaT) {
		return positionAngu + vitesseAngu*deltaT + 1/2*accelAngu*deltaT*deltaT;
	}
}

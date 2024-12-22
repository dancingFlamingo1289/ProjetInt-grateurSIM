package composantDeJeu;

//import java.text.DecimalFormat;

/**
 * Classe s'occupant de gérer les points de l'application
 * @author Félix Lefrançois
 */
public class SystemeDePoint {
	/** Les points totaux **/
	private int pointsTotauxAffiches = 0;
	/** Les points en incluant les "transactions" (nouvelle balle, etc.)**/
	private int pointsApresTransaction = 0;
	/** Le nombre de points nécessaire pour ajouter une balle **/
	private final int NB_POINTS_AJOUT_BALLE = 10000;
	/** La valeur initiale du pointage au début de la partie **/
	private int valeurInitialePointage;
	/** Le multiplicateur de points obtenus **/
	private int multiplicateur = 1;
	/** La valeur a atteindre pour passer au prochain niveau de multiplicateur**/
	private int limiteMultiplicateur = 10000;
	/** La constante du multiplication pour calculer le prochain nécessaire pour passer au prochain 
	 * multiplicateur **/
	private final int MULTI_PROCHAIN_GRADE_MULTIPLICATEUR = 5;
	/** La limite totale de points dans un système **/
	private final int LIMITE_POINTS = 1000000000;
	/** La limite de l'affichage de points avant de passer à la notation exponentielle **/
	private final int LIMITE_AFFICHAGE = 1000000;
	/** Format décimal après avoir dépassé une certaine quantité de points pour le toString**/
	//private final DecimalFormat formatDecimal = new DecimalFormat("0.##E0");
	
	/**
	 * Constructeur de classe
	 * @param pointsDeDepart Les points de départ du système de point
	 */
	//Félix Lefrançois
	public SystemeDePoint(int pointsDeDepart) {
		pointsTotauxAffiches = pointsDeDepart;
		pointsApresTransaction = pointsDeDepart;
		this.valeurInitialePointage = pointsDeDepart;
	}
	
	/**
	 * Méthode faisant les changements au système si il y a assez de points pour ajouter une balle
	 * @return Un booléen confirmant s'il y a l'ajout d'une balle à la table
	 */
	//Félix Lefrançois
	public void appliquerAjoutBalle() {
		if (pointsApresTransaction >= NB_POINTS_AJOUT_BALLE) {
			pointsApresTransaction -= NB_POINTS_AJOUT_BALLE;
		}
	}
	
	/**
	 * Méthode qui vérifier si il y a assez de points pour ajouter une balle
	 * @return Un booléen confirmant si il y a assez de points pour ajouter une balle
	 */
	//Félix Lefrançois
	public boolean verifierAjoutBalle() {
		return pointsApresTransaction >= NB_POINTS_AJOUT_BALLE;
	}
	
	/**
	 * Méthode permettant de faire les changements nécessaires au multiplicateur lorsque le système atteint 
	 * une certaine valeur de points
	 */
	//Félix Lefrançois
	public void passerAuProchainMultiplicateur() {
		if (pointsTotauxAffiches >= limiteMultiplicateur) {
			limiteMultiplicateur = limiteMultiplicateur*MULTI_PROCHAIN_GRADE_MULTIPLICATEUR;
			multiplicateur += 1;
		}
	}
	
	/**
	 * Méthode permettant d'ajouter des points au système de point
	 * @param lesPointsAjoutes Les points ajoutés au système de points
	 */
	//Félix Lefrançois
	public void ajouterPoints(double lesPointsAjoutes) {
		if (pointsTotauxAffiches <= LIMITE_POINTS) {
		    pointsTotauxAffiches += lesPointsAjoutes*multiplicateur;
		    pointsApresTransaction += lesPointsAjoutes*multiplicateur;
		    if (pointsTotauxAffiches > LIMITE_POINTS) {
		    	pointsTotauxAffiches = LIMITE_POINTS;
		    }
		}
	}
	
	/**
	 * Méthode de réinitialisation d'un système de point
	 */
	//Félix Lefrançois
	public void reinitialiser() {
		pointsTotauxAffiches = valeurInitialePointage;
		pointsApresTransaction = valeurInitialePointage;
		multiplicateur = 1;
	}
	
	/**
	 * Méthode qui résume un Système de points en donnant le nombre de points accumulés
	 * @return Une chaîne de caractère résumant le système de points
	 */
	//Félix Lefrançois
	public String toString() {
		String chaine;
		if (pointsTotauxAffiches <= LIMITE_AFFICHAGE) {
			chaine = pointsTotauxAffiches+" points.";
		} else {
			chaine = /*formatDecimal.format(pointsTotauxAffiches) +*/ " points.";
		}
		return chaine;
	}

	/**
	 * Méthode retournant le multiplicateur de points
	 * @return Le multiplicateur de points
	 */
	//Félix Lefrançois
	public int getMultiplicateur() {
		return multiplicateur;
	}

	/**
	 * Méthode changeant le multiplicateur de points
	 * @param multiplicateur Le nouveau multiplicateur de points
	 */
	//Félix Lefrançois
	public void setMultiplicateur(int multiplicateur) {
		this.multiplicateur = multiplicateur;
	}

	/**
	 * Méthode retournant le total des points accumulés au cours de la partie
	 * @return Le total des points accumulés au cours de la partie
	 */
	//Félix Lefrançois
	public int getPointsTotauxAffiches() {
		return pointsTotauxAffiches;
	}

	/**
	 * Méthode permettant de changer le total de points affichés
	 * @param pointsTotauxAffiches Le nouveau total de points affichés
	 */
	//Félix Lefrançois
	public void setPointsTotauxAffiches(int pointsTotauxAffiches) {
		this.pointsTotauxAffiches = pointsTotauxAffiches;
	}

	/**
	 * Méthode permettant d'obtenir la somme des points avec transaction
	 * @return Les points en incluant les transaction faites
	 */
	//Félix Lefrançois
	public int getPointsApresTransaction() {
		return pointsApresTransaction;
	}

	/**
	 * Méthode permettant de changer la somme des points incluant transaction
	 * @param pointsApresTransaction La nouvelle somme des points en incluants les transactions
	 */
	//Félix Lefrançois
	public void setPointsApresTransaction(int pointsApresTransaction) {
		this.pointsApresTransaction = pointsApresTransaction;
	}

	/**
	 * Méthode qui retourne la valeur initiale de pointage
	 * @return La valeur initiale de pointage
	 */
	//Félix Lefrançois
	public int getValeurInitialePointage() {
		return valeurInitialePointage;
	}

	/**
	 * Méthode qui permet de modifier la valeur initiale de pointage
	 * @param valeurInitialePointage La nouvelle valeur initiale de pointage
	 */
	//Félix Lefrançois
	public void setValeurInitialePointage(int valeurInitialePointage) {
		this.valeurInitialePointage = valeurInitialePointage;
	}
}

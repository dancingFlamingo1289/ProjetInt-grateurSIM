package application.gestionScore;

import java.util.Date;

public class Score implements Comparable<Score> {
	/** Valeur du score. **/
	private int valeurScore ;
	/** Objet Date contenant la date et l'heure à laquelle le score a été obtenu. **/
	private Date dateEtHeure ;

	/**
	 * Constructeur d'un score vierge.
	 */
	// Par Elias Kassas
	public Score() {
		this.valeurScore = 0 ;
		this.dateEtHeure = new Date() ;
	}

	/**
	 * Constructeur d'un score à partir de la valeur du score obtenu.
	 * @param scoreObtenu
	 */
	// Par Elias Kassas
	public Score(int scoreObtenu) {
		this.valeurScore = scoreObtenu ;
		this.dateEtHeure = new Date() ;
	}
	
	/**
	 * Constructeur d'un score à partir de la valeur du score obtenu.
	 * @param scoreObtenu : 
	 * @param dateEtHeure : 
	 */
	// Par Elias Kassas
	@SuppressWarnings("deprecation")
	public Score(int scoreObtenu, String dateEtHeure) {
		this.valeurScore = scoreObtenu ;
		this.dateEtHeure = new Date(dateEtHeure) ;
	}
	
	/**
	 * @return the valeurScore
	 */
	// Par Elias Kassas
	public int getValeurScore() {
		return valeurScore;
	}

	/**
	 * @param valeurScore the valeurScore to set
	 */
	// Par Elias Kassas
	public void setValeurScore(int valeurScore) {
		this.valeurScore = valeurScore;
	}

	/**
	 * @return the dateEtHeure
	 */
	// Par Elias Kassas
	public Date getDateEtHeure() {
		return dateEtHeure;
	}

	/**
	 * Méthode permettant de comparer deux objets de type Score à l'aide de la valeur du score obtenue.
	 * @param autreScore : L'objet Score à comparer.
	 * @return Si le présent objet est plus grand, plus petit ou égal à l'objet à comparer.
	 */
	// Par Elias Kassas
	@Override
	public int compareTo(Score autreScore) {
		if (valeurScore > autreScore.getValeurScore())
			return -1 ;
		else if (valeurScore == autreScore.getValeurScore())
			return 0 ;
		else
			return 1 ;
	}
	
	/**
	 * Méthode toString d'un objet score.
	 * @return La chaîne de caractères contenant toutes les informations afférant à un score.
	 */
	// Par Elias Kassas
	@Override
	public String toString() {
		return "\nScore : "
				+ "\tvaleurScore = " + valeurScore + 
				",\tdateEtHeure = " + dateEtHeure ;
	}

	/**
	 * Méthode permettant de décoder une chaîne de caractère du toString d'un objet Score
	 * pour en créer un. 
	 * @param scoreStr : Le toString de l'objet à créer.
	 * @return L'objet Score associé au toString.
	 */
	// Par Elias Kassas
	public static Score decoder(String scoreStr) {
		// On sépare la chaîne pour travailler plus facilement.
		String[] chaineEnParties = scoreStr.split(",") ;
		chaineEnParties[0] = chaineEnParties[0].split(" : ")[1] ;
		
		String valScoreStr = chaineEnParties[0].trim().split("=")[1].trim();
		int valScore = Integer.parseInt(valScoreStr) ;
		
		String valDateStr = chaineEnParties[1].trim().split("=")[1].trim() ;
		
		return new Score(valScore, valDateStr) ;
	}
} // fin classe

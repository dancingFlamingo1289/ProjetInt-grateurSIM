package application.configuration;

import java.awt.Color;

/**
 * Cette classe représente un thème.
 * Un thème possède un nom, une couleur primaire (couleur de fond), une couleur secondaire 
 * (couleur de fond des composants), une couleur tertiaire (couleur des bordures des composants) ainsi qu'une
 * couleur de police pour le texte affiché.
 * @author Elias Kassas
 */
public class Theme {
	/** Nom du thème. **/
	private String nom ;
	/** Couleurs primaire, secondaire et tertiaire du thème. **/
	private Color couleurPrimaire, couleurSecondaire, couleurTertiaire ;
	/** Couleur de la police de caractère du thème. **/
	private Color couleurPolice ;

	/**
	 * Constructeur d'un thème prenant en paramètre les couleurs primaire et secondaire.
	 * @param couleurPrimaire : La couleur primaire du thème.
	 * @param couleurSecondaire : La couleur secondaire du thème.
	 * @param couleurPolice : La couleur de la police de caractère.
	 */
	// Par Elias Kassas
	public Theme(String nom, Color couleurPrimaire, Color couleurSecondaire, Color couleurPolice) {
		this.nom = nom ;
		this.couleurPrimaire = couleurPrimaire ;
		this.couleurSecondaire = couleurSecondaire ;
		this.couleurPolice = couleurPolice ;
	}

	/**
	 * Constructeur d'un thème prenant en paramètre les couleurs primaire, secondaire et tertiaire.
	 * @param couleurPrimaire : La couleur primaire du thème.
	 * @param couleurSecondaire : La couleur secondaire du thème.
	 * @param couleurTertiaire : La couleur tertiaire du thème. 
	 * @param couleurPolice : La couleur de la police de caractère.
	 */
	// Par Elias Kassas
	public Theme(String nom, Color couleurPrimaire, Color couleurSecondaire, Color couleurTertiaire, 
			Color couleurPolice) {
		this.nom = nom ;
		this.couleurPrimaire = couleurPrimaire ;
		this.couleurSecondaire = couleurSecondaire ;
		this.couleurTertiaire = couleurTertiaire ;
		this.couleurPolice = couleurPolice ;
	}

	/**
	 * Méthode permettant d'obtenir la couleur primaire d'un thème.
	 * @return La couleur primaire.
	 */
	// Par Elias Kassas
	public Color getCouleurPrimaire() {
		return couleurPrimaire ;
	}

	/**
	 * Méthode permettant de modifier la couleur primaire d'un thème.
	 * @param couleurPrimaire : La couleur primaire.
	 */
	// Par Elias Kassas
	public void setCouleurPrimaire(Color couleurPrimaire) {
		this.couleurPrimaire = couleurPrimaire ;
	}

	/**
	 * Méthode permettant d'obtenir la couleur secondaire d'un thème.
	 * @return La couleur secondaire.
	 */
	// Par Elias Kassas
	public Color getCouleurSecondaire() {
		return couleurSecondaire ;
	}

	/**
	 * Méthode permettant d'obtenir la couleur secondaire d'un thème.
	 * @param couleurSecondaire : La couleur secondaire.
	 */
	// Par Elias Kassas
	public void setCouleurSecondaire(Color couleurSecondaire) {
		this.couleurSecondaire = couleurSecondaire ;
	}

	/**
	 * Méthode permettant d'obtenir la couleur tertiaire d'un thème.
	 * @return La couleur tertiaire.
	 */
	// Par Elias Kassas
	public Color getCouleurTertiaire() {
		return couleurTertiaire ;
	}

	/**
	 * Méthode permettant d'obtenir la couleur tertiaire d'un thème.
	 * @param couleurTertiaire : La couleur tertiaire.
	 */
	// Par Elias Kassas
	public void setCouleurTertiaire(Color couleurTertiaire) {
		this.couleurTertiaire = couleurTertiaire ;
	}

	/**
	 * Méthode permettant d'obtenir la couleur de la police d'un thème.
	 * @return La couleur de la police.
	 */
	// Par Elias Kassas
	public Color getCouleurPolice() {
		return couleurPolice ;
	}

	/**
	 * Méthode permettant de modifier la couleur de la police d'un thème.
	 * @param couleurPolice : La couleur de la police.
	 */
	// Par Elias Kassas
	public void setCouleurPolice(Color couleurPolice) {
		this.couleurPolice = couleurPolice ;
	}

	/**
	 * Méthode permettant d'obtenir le nom d'un thème.
	 * @return Le nom du thème.
	 */
	// Par Elias Kassas
	public String getNom() {
		return nom ;
	}

	/**
	 * Méthode permettant de modifier le nom d'un thème.
	 * @param nouvNom : Le nouveau nom du thème.
	 */
	// Par Elias Kassas
	public void setNom(String nouvNom) {
		nom = nouvNom ;
	}

	/**
	 * Méthode toString d'un objet Theme.
	 * @return La chaîne de caractère contenant les informations d'un thème.
	 */
	// Par Elias Kassas
	@Override
	public String toString() {
		if (couleurPrimaire == null)
			couleurPrimaire = new Color(0, 0, 0) ;
		if (couleurSecondaire == null)
			couleurSecondaire = new Color(0, 0, 0) ;
		if (couleurTertiaire == null)
			couleurTertiaire = new Color(0, 0, 0) ;
		if (couleurPolice == null)
			couleurPolice = new Color(0, 0, 0) ;

		return "\nTheme :\n"
		+ "\tnom = " + nom + "\n"
		+ "\tcouleurPrimaire = " + couleurPrimaire + "\n"
		+ "\tcouleurSecondaire = " + couleurSecondaire + "\n"
		+ "\tcouleurTertiaire = " + couleurTertiaire + "\n"
		+ "\tcouleurPolice=" + couleurPolice + "\n" ;
	}
}

package outils;

/**
 * Classe permettant de gérer le son de tout objet qui émet un son.
 * @author Aimé Melançon
 */
public class GererSon {
	/**Variable de type boolean qui permet d'activer et de désactivé tout les sons. **/
	private static boolean allumerFermer=true;

	/**
	 * Méthode permettant de connaitre l'état si le son est activé ou désactivé.
	 * @return L'état si les sons pour les obstacles sont activé ou désactivé
	 */
	//Aimé Melançon
	public static boolean isAllumerFermer() {
		return allumerFermer;
	}

	/**
	 * Méthode permettant de changer l'état et de choisir d'activé ou de désactivé le son.
	 * @param allumerFermer Le boolean qui permet d'activer ou de désactivé le son
	 */
	//Aimé Melançon
	public static void setAllumerFermer(boolean allumerFermer) {
		GererSon.allumerFermer = allumerFermer;
	}
}

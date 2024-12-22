package interfaces;

/**
 * Interface qui définit la méthode qu'un objet doit implémenter pour pouvoir
 * être sélectionné 
 *  
 * @author Caroline Houle
 *
 */
public interface Selectionnable {
	
	/**
	 * Retourne vrai si le point pass� en param�tre fait partie de l'objet dessinable
	 * sur lequel cette methode sera appelée
	 * 
	 * 
	 * @param xPix Coordonnée en x du point (exprimé en pixels) 
	 * @param yPix Coordonnée en y du point (exprimé en pixels)
	 * @return Retourne si l'objet contient ou non le point
	 */
	public boolean contient(double xPix, double yPix);
	
}

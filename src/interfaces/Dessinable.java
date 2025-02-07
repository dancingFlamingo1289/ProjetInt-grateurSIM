package interfaces;
import java.awt.Graphics2D;

/**
 * Interface qui definit la methode (ou possiblement les methodes) qu'un objet dessinable
 * doit implementer.
 *  
 * @author Caroline Houle
 *
 */

public interface Dessinable {
	
	/**
	 * Dessine les formes constituant l'objet.
	 * Doit s'assurer de ne pas modifier le contexte graphique
	 * 
	 * @param g2d Contexte graphique du composant sur lequel dessiner
	 */
	public void dessiner(Graphics2D g2d);
}

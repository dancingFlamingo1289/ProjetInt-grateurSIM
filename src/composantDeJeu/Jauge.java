package composantDeJeu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import javax.swing.JPanel;

/**
 * Jauge servant à déterminer la force initiale de lancer de la balle.
 * @author Félix Lefrançois
 */
public class Jauge extends JPanel implements Runnable{
	/** Identifiant de classe**/
	private static final long serialVersionUID = 1L;
	/** Booléen confirmant si l'animation doit commencer **/
	private boolean animationDemarree = false;
	/** Le temps du sleep **/
	private int tempsDuSleep = 30;
	/** Le rectangle dessiné **/
	private Rectangle2D.Double rectangle;
	/** La hauteur du rectangle**/
	private double hauteurRectangle = 0;
	/** Le pourcentage d'augmentation **/
	private double pixelsSurCent;
	/** Booléen confirmant si c'est le premier dessinc**/
	private boolean premiereFois = true;
	/** La couleur du rectangle de l jauge**/
	private Color couleurRectangle = Color.GREEN;
	/** L'incrémentation de la hauteur en mètre de la classe en fonction du deltaT**/
	private double incrementationHauteur;
    /** Support pour lancer des évènements de type propretyChange**/
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	/** L'étirement maximum du ressort initial **/
	private final double ETIREMENT_MAX = 5;
	/** Une valeur impossible d'étirement pour le PropertyChange **/
	private final int VALEUR_IMPOSSIBLE = 9999;
	/** Hauteur du composant **/
	private double hauteurMax;
	
	/**
	 * Permet la communication avec l'application
	 * @param listener Le listener de l'application
	 */
	//Félix Lefrançois
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}
	
	/**
	 * Constrcuteur
	 */
	//Félix Lefrançois
	public Jauge() {
        setLayout(null);
	}

	/**
	 * Dessin de la jauge
	 * @param g Contexte graphique
	 */
	//Félix Lefrançois
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		if(premiereFois) {
			pixelsSurCent = (double)getHeight()/100;
			premiereFois = false ;
			hauteurMax = getHeight();
		}
		
		rectangle = new Rectangle2D.Double(0,getHeight()-hauteurRectangle,getWidth(),hauteurRectangle);
		g2d.setColor(couleurRectangle);
		g2d.fill(rectangle);
		
	}

	/**
	 * Méthode gérant les changements/l'animation de la classe
	 */
	//Félix Lefrançois
	@Override
	public void run() {
		while (animationDemarree) {
			if (hauteurRectangle >= 100*pixelsSurCent) {
				hauteurRectangle = getHeight();
				incrementationHauteur = -pixelsSurCent;
			} else if (hauteurRectangle <= 0) {
				hauteurRectangle = 0;
				incrementationHauteur = pixelsSurCent;
			}
			
			if (hauteurRectangle < pixelsSurCent*33) {
				couleurRectangle = Color.GREEN;
			} else if (hauteurRectangle >= pixelsSurCent*33 && hauteurRectangle <= pixelsSurCent*67) {
				couleurRectangle = Color.YELLOW;
			} else if (hauteurRectangle > pixelsSurCent*67) {
				couleurRectangle = Color.RED;
			}
			
			hauteurRectangle += incrementationHauteur;
			pcs.firePropertyChange("Etirement", VALEUR_IMPOSSIBLE, (hauteurRectangle/hauteurMax)*ETIREMENT_MAX);
			
			repaint();
			try {
				Thread.sleep(tempsDuSleep);
			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Méthode qui démarre l'animation
	 */
	//Félix Lefrançois
	public void demarrer() {
		if (!animationDemarree) {
			Thread threadAnim = new Thread(this);
			threadAnim.start();
			animationDemarree = true;
		}
	}
	
	/**
	 * Méthode qui arrête l'animation
	 */
	//Félix Lefrançois
	public void arreter() {
		animationDemarree = false ; 
	}
	
	/**
	 * Méthode qui réinitialise la jauge lorsque la balle est lancée
	 */
	//Félix Lefrançois
	public void terminer() {
		animationDemarree = false;
		hauteurRectangle = 0;
	    couleurRectangle = Color.GREEN;
	    repaint();
	}
	
	/**
	 * Méthode retournant la hauteur en pixels du remplissage de la jauge
	 * @return La hauteur en pixels du remplissage de la jauge
	 */
	//Félix Lefrançois
	public double getHauteurRectangle() {
		return hauteurRectangle;
	}

	/**
	 * Méthode permettant de changer la hauteur en pixels du remplissage de la jauge
	 * @param hauteurRectangle La nouvelle hauteur en pixels du remplissage de la jauge
	 */
	//Félix Lefrançois
	public void setHauteurRectangle(double hauteurRectangle) {
		this.hauteurRectangle = hauteurRectangle;
	}
}

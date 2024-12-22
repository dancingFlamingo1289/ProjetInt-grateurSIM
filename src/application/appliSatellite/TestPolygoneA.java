package application.appliSatellite;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import math.vecteurs.Vecteur3D;
import obstacles.polygone.Polygone;

/**
 * Panel de tests du polygone.
 * @author Elias Kassas
 */
public class TestPolygoneA extends JPanel {
	/** Coefficient de sérialisation. **/
	private static final long serialVersionUID = 1L ;
	/** Le polygone à dessiner. **/
	private Polygone p ;
	/** Longueur d'un côté du polygone dessiné. **/
	private double longueurCote = 40 ;
	/** Nombre de côtés du polygone dessiné. **/
	private int nbCotes = 5 ;
	/** Couleur du polygone dessiné. **/
	private Color couleurPolygone = Color.yellow ;
	
	/**
	 * Constructeur du panel.
	 */
	// Par Elias Kassas
	public TestPolygoneA() {
		setPreferredSize(new Dimension(200, 200)) ;
		setBackground(Color.white) ;
	}

	/**
	 * Méthode permettant de dessiner les composants.
	 * @param g : Le contexte graphique.
	 */
	// Par Elias Kassas
	@Override
	public void paintComponent(Graphics g) {		
		super.paintComponent(g) ;
		Graphics2D g2d = (Graphics2D) g ;
		
		p = new Polygone(new Vecteur3D(getWidth()/2, getHeight()/2), nbCotes, longueurCote, couleurPolygone, null) ;
		p.dessiner(g2d) ;
	}
	
	/**
	 * Méthode permettant de modifier le nombre de côtés.
	 * @param nbCotes : Le nouveau nombre de côtés.
	 */
	// Par Elias Kassas
	public void setNbCotes(int nbCotes) {
		this.nbCotes = nbCotes ; 
		repaint() ;
	}
	
	/**
	 * Méthode permettant de modifier la longueur d'un côté.
	 * @param longueurCote : La nouvelle longueur.
	 */
	// Par Elias Kassas
	public void setLongueurCote(double longueurCote) {
		this.longueurCote = longueurCote ;
		repaint() ;
	}
	
	/**
	 * Méthode permettant de modifier la couleur.
	 * @param couleur : La nouvelle couleur.
	 */
	// Par Elias Kassas
	public void setCouleur(Color couleur) {
		this.couleurPolygone = couleur ;
		repaint() ;
	}
	
	/**
	 * Méthode permettant d'obtenir le polygone dessiné.
	 * @return Le polygone dessiné.
	 */
	// Par Elias Kassas
	public Polygone getPolygone() {
		return this.p ;
	}
}

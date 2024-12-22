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
	private static final long serialVersionUID = 1L ;
	private Polygone p ;
	private double longueurCote = 40 ;
	private int nbCotes = 5 ;
	private Color couleurPolygone = Color.yellow ;
	
	// Par Elias Kassas
	public TestPolygoneA() {
		setPreferredSize(new Dimension(200, 200)) ;
		setBackground(Color.white) ;
	}

	// Par Elias Kassas
	@Override
	public void paintComponent(Graphics g) {		
		super.paintComponent(g) ;
		Graphics2D g2d = (Graphics2D) g ;
		
		p = new Polygone(new Vecteur3D(getWidth()/2, getHeight()/2), nbCotes, longueurCote, couleurPolygone) ;
		p.dessiner(g2d) ;
	}
	
	// Par Elias Kassas
	public void setNbCotes(int nbCotes) {
		this.nbCotes = nbCotes ; 
		repaint() ;
	}
	
	// Par Elias Kassas
	public void setLongueurCote(double longueurCote) {
		this.longueurCote = longueurCote ;
		repaint() ;
	}
	
	// Par Elias Kassas
	public void setCouleur(Color couleur) {
		this.couleurPolygone = couleur ;
		repaint() ;
	}
	
	// Par Elias Kassas
	public Polygone getPolygone() {
		return this.p ;
	}
}

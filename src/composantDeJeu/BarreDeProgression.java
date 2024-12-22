package composantDeJeu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.Random;

import javax.swing.JColorChooser;
import javax.swing.JPanel;

/**
 * Cette classe représente une barre de progression.
 * @author Elias Kassas
 */
public class BarreDeProgression extends JPanel {
	/** Coefficient de sérialisation. **/
	private static final long serialVersionUID = 1L;
	/** Facteur d'agrandissement de la barre de progression. **/
	private double pixelsParPourcent ;
	/** On vérifie si c'est la première fois que la barre de progression est dessinée. **/
	private boolean premiereFois = true ;
	/** Progression par rapport à une valeur donnée sur 100. **/
	private double progression = 0.00 ; // en %
	/** Valeur maximale pour évaluer le pourcentage de progression. **/
	private double valeurMax ;
	/** Rectangle symbolisant la barre de progression. Elle progresse au fur et à mesure que l'utilisateur
	 * augmente sa progression. **/
	private Rectangle2D.Double barreProg ;
	/** Couleur du texte. **/
	private Color couleurTexte = Color.black ;
	/** Couleur de la barre de progression. **/
	private Color couleurBarre = Color.pink ;

	/**
	 * Créer la barre de progression.
	 */
	// Par Elias Kassas
	public BarreDeProgression() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					Color couleur = JColorChooser.showDialog(null, "Changer la couleur de la jauge.", null) ;

					if(couleur != null) {
						couleurBarre = couleur ;
					}

					repaint();
				}
			}
		});

		setBackground(Color.white) ;
	}

	/**
	 * Méthode permettant de poser une valeur maximale pour la barre de progression.
	 * @param valeurMax : La valeur maximale.
	 */
	// Par Elias Kassas
	public void poserUneValeurMaximale(double valeurMax) {
		if (valeurMax > 0)
			this.valeurMax = valeurMax ;
		else 
			this.valeurMax = 1e20 ;
		repaint() ;
	}

	/**
	 * Méthode permettant de dessiner une barre de progression.
	 * @param g : Le contexte graphique.
	 */
	// Par Elias Kassas
	public void paintComponent(Graphics g) {
		super.paintComponent(g) ;
		Graphics2D g2d = (Graphics2D) g ;

		if (premiereFois) {
			pixelsParPourcent = getWidth()/100d ;
			premiereFois = false ;
		}

		AffineTransform mat = new AffineTransform() ;
		mat.scale(pixelsParPourcent, 1) ;

		barreProg = new Rectangle2D.Double(0, 0, progression, getHeight()) ;
		g2d.setColor(couleurBarre) ;
		Shape barreTransfo = mat.createTransformedShape(barreProg) ;
		g2d.fill(barreTransfo) ;

		g2d.setColor(couleurTexte) ;
		g2d.drawString(String.format("%.1f", progression) + "%", getWidth()/2, getHeight()/2) ;
	}

	/**
	 * Méthode permettant d'ajouter une valeur à la progression de la barre. L'ajout en pourcent fait à la 
	 * progression sera calculé à partir de la formule de pourcentage d'écart.
	 * @param nouvValeur : La nouvelle valeur pour la barre de progression.
	 */
	// Par Elias Kassas
	public void remplacerLActuelleValeur(double nouvValeur) {
		progression = nouvValeur/valeurMax*100.0 ;

		if (progression >= 100.0) {
			couleurBarre = Color.red ;
		}

		repaint() ;
	}

	/**
	 * Méthode permettant de reinitialiser la barre de progression.
	 */
	// Par Elias Kassas
	public void reinitialiser() {
		progression = 0.0 ;
		couleurBarre = Color.pink ;
		repaint() ;
	}
	
	public double getValeurMaximale() {
		return valeurMax ;
	}
}

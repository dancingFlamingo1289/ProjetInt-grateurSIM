package composantDeJeu;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Classe permettant de créer un triangle de la taille composant dont les tailles sont basées sur la taille du composant
 * @author Félix Lefrançois
 */
public class TriangleInclinaison extends JPanel{
	/** Identificateur de classe **/
	private static final long serialVersionUID = 1L;
	/** L'angle d'inclinaison de la table qui affecte le triangle **/
	private double angle;
	/** Le chemin permettant de dessiner le triangle **/
	private Path2D.Double dessinTriangle;
	/** L'aire du triangle**/
	private Area aireTriangle;
	/** L'espace entre le centre en x de la composante et un côté (peut être positif ou négatif dépendamment de celui-ci)**/
	private double incrementation;
	/** La couleur du triangle **/
	private Color couleurTriangle = Color.CYAN;
	/** La constante d'incrémentationd e de l'angle après avoir atteint 45 degrés**/
	private final double INCREMENTATION_ANGLE_APRES_45_DEGRE = Math.PI/4;
	/** La valeur d'incrémentation de l'angle après avoir atteint 45 degrés dépendamment du signe de l'angle**/
	private double incrementationApres45Degre;

	/**
	 * Constructeur de la classe
	 * @param angle L'angle d'inclinaison en degré de la table transmis au triangle
	 */
	//Félix Lefrançois
	public TriangleInclinaison(double angle) {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				couleurTriangle = new Color((float) Math.random(), (float) Math.random(), (float) Math.random());
				repaint();
			}
		});
		setLayout(null);
		determinerTransitionAngle(angle);
	}
	
	/**
	 * Méthode permettant de créer et dessiner le triangle
	 * @param g Contexte graphique
	 */
	//Félix Lefrançois
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		determinerQuelCoteCommencer();
		dessinTriangle = new Path2D.Double();

		dessinTriangle.moveTo(getWidth()/2, getHeight());
		
	    if(angle <= Math.PI/4 && angle >= -Math.PI/4) {
			dessinTriangle.lineTo(getWidth()/2+incrementation, getHeight());
			dessinTriangle.lineTo(getWidth()/2+incrementation, Math.cos(angle*2)*getHeight());
			dessinTriangle.lineTo(getWidth()/2, getHeight());
	    } else {
			dessinTriangle.lineTo(getWidth()/2+incrementation, getHeight());
			dessinTriangle.lineTo(getWidth()/2+incrementation,0);
			dessinTriangle.lineTo(getWidth()/2+incrementation*Math.cos((angle-incrementationApres45Degre)*2), 0);
	    	dessinTriangle.lineTo(getWidth()/2, getHeight());
	    }
	    
	    dessinTriangle.closePath();
	    
	    aireTriangle = new Area(dessinTriangle);
	    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	    g2d.setColor(couleurTriangle);
	    g2d.fill(aireTriangle);

	 }
	
	/**
	 * Méthode permettant de faire les modifications nécessaires pour que le dessin du triangle s'adapte à l'angle
	 * @angle L'angle d'inclinaison de la table transmis au triangle
	 */
	//Félix Lefrançois
	private void determinerTransitionAngle(double angle) {
		this.angle = Math.toRadians(angle);
	}
    
	/**
	 * Méthode permettant de déterminer l'incrémentation du début du triangle
	 */
	//Félix Lefrançois
	private void determinerQuelCoteCommencer() {
		if(angle == 0) {
			incrementation = 0;
			incrementationApres45Degre = 0;
		} else if (angle > 0) {
			incrementation = -getWidth()/2;
			incrementationApres45Degre = INCREMENTATION_ANGLE_APRES_45_DEGRE;
		} else if (angle < 0) {
			incrementation = +getWidth()/2;
			incrementationApres45Degre = -INCREMENTATION_ANGLE_APRES_45_DEGRE;
		}
	}
	
	/**
	 * Méthode permettant de changer l'angle d'inclinaison de la table transmis au triangle
	 * @param angle Le nouvel angle d'inclinaison en degré de la table transmis au triangle
	 */
	//Félix Lefrançois
	public void setAngle(double angle) {
		determinerTransitionAngle(angle);
		repaint();
	}
	
	/**
	 * Méthode retournant l'angle d'inclianaison de la table transmis au triangle
	 * @return L'angle d'inclinaison de la table transmis au triangle
	 */
	//Félix Lefrançois
	public double getAngle() {
		return this.angle;
	}
	
	/**
	 * Méthode permettant de changer la couleur du triangle
	 * @param couleurTriangle La nouvelle couleur du triangle
	 */
	//Félix Lefrançois
	public void setCouleurTriangle(Color couleurTriangle) {
		this.couleurTriangle = couleurTriangle;
		repaint();
	}
	
	/**
	 * Méthode retournant la couleur du triangle
	 * @return La couleur du triangle
	 */
	//Félix Lefrançois
	public Color getCouleurTriangle() {
		return couleurTriangle;
	}
	
}

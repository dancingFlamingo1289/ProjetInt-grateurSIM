package application.logo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.JPanel;
import composantDeJeu.Balle;
import math.vecteurs.Vecteur3D;
import obstacles.Cercle;
import obstacles.MurMobile;
import obstacles.Obstacle;
import obstacles.TrouNoir;
import obstacles.polygone.Trait;

/**
 * Composant de dessin servant à dessiner le logo du jeu et à gérer son animation.
 * @author Elias Kassas
 */
public class Logo extends JPanel implements Runnable {
	/** Coefficient de sérialisation. **/
	private static final long serialVersionUID = 1L ;
	/** Texte à écrire sur le composant. **/
	private final String TEXTE = "Fr   ss   nB   um" ;
	/** Coordonnées de la position du nom de l'application. **/
	private float posX = 0, posY = 0 ;
	/** Liste contenant les balles affichées sur le composant. **/
	private CopyOnWriteArrayList<Balle> lesBalles ;
	/** Liste contenant les balles affichées sur le composant. **/
	private CopyOnWriteArrayList<Obstacle> lesObstacles ;
	/** Liste contenant **/
	private CopyOnWriteArrayList<Trait> lesTraitsDesMurs ;
	/** La différence de temps **/
	private double deltaT = 0.006 ;
	/** On vérifie si c'est la première fois que l'on passe dans le paintComponent. **/
	private boolean premiereFois = true ;
	/** On vérifie si l'animation est en cours. **/
	private boolean enCours = false ;

	/**
	 * On crée la zone de dessin.
	 */
	// Par Elias Kassas
	public Logo() {
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				//System.out.println("Ahhh...") ;
				demarrer() ;
			}

			@Override
			public void mouseExited(MouseEvent e) {
				//System.out.println("Behh...") ;
				reinitialiser() ;
			}
		});
		setFont(new Font("Wawati SC", Font.BOLD, 55)) ;
		setBackground(Color.white) ;
	}

	/**
	 * Méthode permettant de dessiner le logo de l'application sur le panneau de dessin.
	 */
	// Par Elias Kassas
	public void paintComponent(Graphics g) {
		super.paintComponent(g) ;
		Graphics2D g2d = (Graphics2D) g ;

		if (premiereFois) {
			int longueurTitre = g2d.getFontMetrics().stringWidth(TEXTE) ;
			int hauteurTitre = g2d.getFontMetrics().getAscent() - g2d.getFontMetrics().getDescent() ;

			posX = (getWidth() - longueurTitre)/2 ;
			posY = (getHeight() - hauteurTitre)/2 + g2d.getFontMetrics().getAscent() ;

			lesObstacles = new CopyOnWriteArrayList<Obstacle>() ;

			MurMobile murDuI = new MurMobile(new Vecteur3D(g2d.getFontMetrics().stringWidth("Fr  ") + posX, 
					getHeight()/2), 1.5*g2d.getFontMetrics().stringWidth("I"), 
					1.5*hauteurTitre, Math.PI/2, -50, new Color((float) Math.random(), (float) Math.random(), 
							(float) Math.random())) ;
			lesObstacles.add(murDuI) ;

			Cercle cercleDuPremierO = new Cercle(new Vecteur3D(g2d.getFontMetrics().stringWidth("Fr   ss  ") + 
					posX - 0.25*g2d.getFontMetrics().stringWidth("O"), 
					getHeight()/2 + 0.25*g2d.getFontMetrics().stringWidth("O")), 
					1.2*g2d.getFontMetrics().stringWidth("O"), new Color((float) Math.random(),
							(float) Math.random(), 
							(float) Math.random())) ;
			lesObstacles.add(cercleDuPremierO) ;

			TrouNoir trouNoirDuSecondO = new TrouNoir(new Vecteur3D(g2d.getFontMetrics().
					stringWidth("Fr   ss   nB  ") + posX, getHeight()/2, g2d.getFontMetrics().stringWidth("O")), 
					1.2*g2d.getFontMetrics().stringWidth("O"), 2e14, new Color((float) Math.random(), 
							(float) Math.random(), (float) Math.random())) ;
			lesObstacles.add(trouNoirDuSecondO) ;

			lesBalles = new CopyOnWriteArrayList<Balle>() ;
			Balle b1 = new Balle(new Vecteur3D(getWidth()/6, getHeight()/6), 8.0,Color.blue) ;
					//b2 = new Balle(new Vecteur3D(5.5*getWidth()/6, 5.5*getHeight()/6), 8.0, Color.GRAY) ;
			lesBalles.add(b1) ;
			//lesBalles.add(b2) ;

			lesTraitsDesMurs = new CopyOnWriteArrayList<Trait>() ;
			lesTraitsDesMurs.add(new Trait(0, 0, getWidth(), 0, Color.black)) ;
			lesTraitsDesMurs.add(new Trait(0, 0, 0, getHeight(), Color.black)) ;
			lesTraitsDesMurs.add(new Trait(0, getHeight(), getWidth(), getHeight(), Color.black)) ;
			lesTraitsDesMurs.add(new Trait(getWidth(), 0, getWidth(), getHeight(), Color.black)) ;

			premiereFois = false ;
		}

		g2d.drawString(TEXTE, posX, posY) ;
		for (Obstacle o : lesObstacles) {
			o.dessiner(g2d) ;
		}

		for (Balle b : lesBalles) {
			b.dessiner(g2d) ;
		}
	}

	/**
	 * Méthode pour l'animation du logo.
	 * Dans l’animation du logo, la couleur des “o” qui seront des objets Cercle et celle du “i” qui sera un 
	 * mur mobile sera modifiée aléatoirement à chaque deltaT. De plus, il y aura deux balles (hors-champ au 
	 * début) qui se déplaceront dans le composant du logo.
	 */
	// Par Elias Kassas
	@Override
	public void run() {
		while(enCours) {
			calculerUneIterationPhysique(deltaT);

			repaint();
			try {
				Thread.sleep(30);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Méthode servant à démarrer l'animation de l'application.
	 */
	// Par Elias Kassas
	public void demarrer() {
		if (!enCours) {
			Thread proc = new Thread(this) ;
			proc.start() ;
			enCours = true ;

			for (Balle b : lesBalles) {
				b.setVitesse(new Vecteur3D(100, 100)) ;
			}
		}
	}

	/**
	 * Méthode servant à arrêter l'animation de l'application.
	 */
	// Par Elias Kassas
	public void arreter() {
		enCours = false ;
	}

	/**
	 * Méthode servant à réinitialiser l'animation de l'application.
	 */
	// Par Elias Kassas
	public void reinitialiser() {
		arreter() ;
		for (Obstacle o : lesObstacles) {
			o.reinitialiser() ;
		}

		premiereFois = true ;
		repaint() ;
	}

	/**
	 * Méthode servant à calculer une itération physique.
	 * @param dt : Différence de temps.
	 */
	// Par Elias Kassas
	private void calculerUneIterationPhysique(double dt) {
		for (Obstacle o : lesObstacles) {
			o.avancerUnPas(dt) ;
		}

		for (Balle b : lesBalles) {
			try {
				b.avancerUnPas(dt) ;
				b.calculerSommeDesForces(20, 0.50d) ;
				b.calculerAcceleration();
				b.avancerUnPas(deltaT) ;
			} catch (Exception e) {
				e.printStackTrace();
			}

			for (Trait t : lesTraitsDesMurs) {
				if (t.intersection(b)) {
					try {
						t.collision(b) ;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			for (Obstacle o : lesObstacles) {
				if (o.intersection(b)) {
					try {
						o.collision(b) ;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
}

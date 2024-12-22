package statistiques.schemaDesForces;

import java.awt.* ;
import java.awt.geom.* ;
import java.beans.* ;
import java.util.concurrent.* ;
import javax.swing.* ;
import composantDeJeu.* ;
import math.vecteurs.* ;

/**
 * Composant qui affiche un schéma des forces appliquées sur une balle.
 * Il utilise une liste de vecteurs pour les dessiner.
 * @author Elias Kassas
 */
public class SchemaDesForces extends JPanel {
	/** Coefficient de sérialisation. **/
	private static final long serialVersionUID = 1L;
	/** Support pour les PropertyChange. **/
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

	/** Balle dont on veut extraire les forces pour les dessiner. **/
	private Balle balle ;
	/** Cercle représentant la toute première balle de la table.  **/
	private Ellipse2D.Double balleDessinee ;
	private double xBalle, yBalle ;
	private double rayon ;
	private Color couleurBalle = Color.orange ;
	/** Liste des forces appliquées sur la balle.
	 * {force gravitationelle, force du trou noir, force de rappel, force magnétique, force de frottement} **/
	private CopyOnWriteArrayList<Vecteur3D> lesForces ;
	/** Liste des forces à dessiner qui sont appliquées sur la balle. Elle reprend la liste des 
	 * forces vectorielles. **/
	private CopyOnWriteArrayList<FlecheVectorielle> forcesSurLaBalle ;
	/** Couleurs pour dessiner les flèches vectorielles. **/
	private Color[] couleurs = {Color.blue, Color.red, Color.magenta, Color.orange, Color.pink, 
			Color.yellow} ;
	/****/
	private boolean premiereFois = true ;
	private boolean forcesChargees = false ;

	/**
	 * Constructeur d'un schéma des forces.
	 */
	public SchemaDesForces() {
		setBackground(Color.white) ;
	}

	/**
	 * Méthode permettant de poser une balle et de charger dans la liste de flèches vectorielles ses forces.
	 * @param balle : La balle à charger.
	 */
	// Par Elias Kassas
	public void poserUneBalle(Balle balle) {
		this.balle = balle ;
		lesForces = new CopyOnWriteArrayList<Vecteur3D>(balle.getForces()) ;
		chargerForces() ;
	}

	/**
	 * Dessiner la fonction sur le composant
	 * @param g : Le contexte graphique
	 */
	// Par Elias Kassas
	@Override
	public void paintComponent(Graphics g) {		
		super.paintComponent(g) ;
		Graphics2D g2d = (Graphics2D) g ;

		if (premiereFois) {
			xBalle = getWidth()/2 ;
			yBalle = getHeight()/2 ;
			rayon = getWidth()/6 ;
			balleDessinee = new Ellipse2D.Double(xBalle - rayon, yBalle - rayon, 
					2*rayon, 2*rayon) ;

			g2d.setColor(couleurBalle) ;
			g2d.fill(balleDessinee) ;
			g2d.setColor(Color.black) ;
			g2d.setStroke(new BasicStroke(2)) ;
			g2d.draw(balleDessinee) ;

			premiereFois = false ;
		}

		int longueurTitre = g2d.getFontMetrics().stringWidth("Schéma des forces".toUpperCase()) ;
		float positionXTitre = (getWidth() - longueurTitre)/2 ;
		g2d.drawString("Schéma des forces".toUpperCase(), positionXTitre, g2d.getFont().getSize2D()) ;

		/*if (forcesChargees) {
			dessinerBalleEtVecteursForces(g2d) ;
			ecrireValeursForces(g2d) ;
		}*/
	}

	/**
	 * Méthode privée permettant de charger les forces appliquées sur la balle dans la liste des forces.
	 */
	// Par Elias Kassas
	private void chargerForces() {
		for (int i = 0 ; i < lesForces.size() ; i++) {
			Vecteur3D force = lesForces.get(i) ;
			forcesSurLaBalle.add(new FlecheVectorielle(xBalle, yBalle, force)) ;
		}
	}

	/**
	 * Méthode permettant de dessiner la balle ainsi que les vecteurs des forces appliquées sur celle-ci.
	 */
	// Par Elias Kassas
	private void dessinerBalleEtVecteursForces(Graphics2D g2d) {
		Graphics2D g2dPrive = (Graphics2D) g2d.create() ;
		g2dPrive.setColor(couleurBalle) ;
		g2dPrive.fill(balleDessinee) ;
		g2dPrive.setColor(Color.black) ;
		g2d.draw(balleDessinee) ;

		for (int i = 0 ; i < forcesSurLaBalle.size() ; i++) {
			FlecheVectorielle fleche = forcesSurLaBalle.get(i) ;
			g2dPrive.setColor(couleurs[i]) ;
			fleche.dessiner(g2dPrive) ;
		}
	}

	/**
	 * Méthode permettant d'écrire les valeurs des forces appliquées sur la balle.
	 * Ordre : {force gravitationelle, force du trou noir, force de rappel, force magnétique, 
	 * force de frottement, force normale}
	 * @param g2d : Le contexte graphique.
	 */
	// Par Elias Kassas
	private void ecrireValeursForces(Graphics2D g2d) {
		Graphics2D g2dPrive = (Graphics2D) g2d.create() ;
		String[] nomForces = {"Force gravitationelle", "force du trou noir", "Force de rappel", 
				"Force magnétique", "Force de frottement"} ;

		// Juste pour éviter de lever des exceptions.
		if (lesForces.size() == forcesSurLaBalle.size() && lesForces.size() == nomForces.length &&
				forcesSurLaBalle.size() == nomForces.length) {
			double posY = 2*rayon + 10 ;
			
			g2dPrive.setColor(Color.black) ;
			int longueurTitre = g2d.getFontMetrics().stringWidth("Forces appliquées sur la balle".
					toUpperCase()) ;
			float positionXTitre = (getWidth() - longueurTitre)/2 ;
			g2dPrive.drawString("Forces appliquées sur la balle", positionXTitre, (float) posY - 10) ;

			for (int i = 0 ; i < forcesSurLaBalle.size() ; i++) {
				g2dPrive.setColor(this.couleurs[i]) ;
				g2dPrive.drawString(nomForces[i] + " : " /*+ lesForces.get(i).module()*/ + " N", 
						10f, (float) posY) ;
				posY += 10 ;
			}
		} else {
			System.err.println("Erreur : Les listes des forces ne font pas la même taille.") ;
		}
	}

	/**
	 * Méthode permettant de charger de nouvelles forces appliquées sur la balle.
	 */
	// Par Elias Kassas
	public void chargerDeNouvellesForces(Balle balle) {
		lesForces = new CopyOnWriteArrayList<Vecteur3D>(balle.getForces()) ;
		repaint() ;
	}

	/**
	 * Méthode servant à ajouter un PropertyChangeListener sur un schéma des forces.
	 * @param listener : L'écouteur de PropertyChange.
	 */
	// Par Elias Kassas
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}
}

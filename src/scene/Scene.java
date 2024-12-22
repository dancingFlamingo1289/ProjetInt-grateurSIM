package scene;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JPanel;

import application.AppPrincipale36;
import application.Configuration;
import composantDeJeu.Table;
import math.vecteurs.Vecteur3D;
import obstacles.Cercle;
import obstacles.Mur;
import obstacles.MurAmovible;
import obstacles.Obstacle;
import obstacles.TrouNoir;
import obstacles.Ventilateur;
import obstacles.plaqueMagnetique.PlaqueMagnetique;
import obstacles.polygone.Polygone;
import java.awt.event.KeyAdapter;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Classe contenant tous les composants de jeu de l'application et gérant leur création
 * et toute l'animation.
 * @author Félix Lefrançois
 * @author Elias Kassas
 * @author Aimé Melançon
 */
public class Scene extends JPanel implements Runnable {
	/** Identifiant de classe **/
	private static final long serialVersionUID = 1L;
	/** La composante Table de la classe Scene où la balle et les obstacles se trouveront **/
	private Table tableDeJeu;
	/** Booléen pour confirmer que l'animation est en cours **/
	private boolean animationDemarree = false;
	/** Les pixels par mètre de la composante **/
	private double pixelsParCentimetre;
	/** La hauteur en metre du composant scene **/
	private double hauteurDeLaTableEnMetre = 200;
	/** La largeur en metre du composant scene **/
	private double largeurDeLaTableEnMetre;
	/** Booléen confirmant si c'est le début de la partie **/
	private boolean debutPartie = false;
	/** Le coefficient de frottement de la surface de la table **/
	private double coefFrott = 0.20;
	/** Le temps du sleep **/
	private int tempsDuSleep = 30;
	/** Le temps simulé total de la simulation **/
	private double tempsTotal = 0.000;
	/** Support pour lancer des évènements de type PropertyChange. **/
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this) ;
	/** La différence de temps **/
	private static final double DELTA_T = 0.010; 
	/** Le nombre de saut de pas avant une réinitialisation automatique du composant **/
	private final int NB_DELTAT_AV_REINI_AUTO = 60;
	/** Largeur de la table voulue. **/
	private final double LARGEUR_TABLE_VOULUE = 29 ;
	/** La masse des balles de la table **/
	private double masseBalle = 5;
	/**La liste d'obstacle dessiné **/
	private CopyOnWriteArrayList<Obstacle> obstaclesADessiner ;
	/**Les pages d'obstacles **/
	private JPanel pageObstacle ;
	/**Pour savoir si un obstacle est sélectionné ou non. **/
	private boolean obstacleSelectionne=false;
	/**Permet de savoir quand c'est le mode création pour faire bouger les obstacles ou non **/
	private boolean modeCreation=false;
	/**L'ancien x de la position de l'obstacle sélectionné. **/
	private double xPrecedent;
	/**L'ancien y de la position dde l'obstacle sélectionné. **/
	private double yPrecedent;
	/**L'obstacle pris par la souris **/
	private Obstacle obstaclePris;
	/**Permet de savoir si un utilisateur à pris au moins un obstacle **/
	private boolean premierPris=false;
	/**
	 * Constructeur de la classe Scene
	 */
	//Félix Lefrançois
	public Scene() {



		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent e) {
				bougeObstacle(e);
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					cliquerDeLobstacle(e);
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				deposeObstacle(e);
			}
		});
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				flipperKey( e);
			}
		});
		setLayout(null);
	}
	/**
	 * Méthode permettant de déposer un obstacle à un endroit choisis par l'utilisateur.
	 * @param e La position de la souris
	 */
	//Aimé Melançon
	private void deposeObstacle(MouseEvent e) {
		if(modeCreation) {

			tableDeJeu.ajouterObstacle(obstaclePris);

			obstacleSelectionne=false;
			System.out.println("Fiou");
			repaint();
		}
	}
	/**Méthode permettant de bouger l'obstacle sélectionné sur la table.
	 * 
	 * @param e La position de déplacement de la souris
	 */
	//Aimé Melançon
	private void bougeObstacle(MouseEvent e) {
		if(obstacleSelectionne&&modeCreation) {

			premierPris=true;
			obstaclePris.setDeplacement(new Vecteur3D(e.getX()/pixelsParCentimetre - xPrecedent,e.getY()/pixelsParCentimetre - yPrecedent ));
			xPrecedent = e.getX()/pixelsParCentimetre ;
			yPrecedent = e.getY()/pixelsParCentimetre ;
			repaint();		
		}
	}
	/**
	 * Méthode permettant de prendre avec la souris l'obstacle.
	 * @param e L'endroit où la souris à cliquer sur un obstacle
	 * @throws ClassNotFoundException L'exception de si une page n'est pas trouvé
	 */
	//Aimé Melançon
	private void cliquerDeLobstacle(MouseEvent e) throws ClassNotFoundException {
		if(modeCreation) {
			obstacleSelectionne=true;
			for(Obstacle obst :obstaclesADessiner) {

				if(obst.contient(e.getX()/pixelsParCentimetre, e.getY()/pixelsParCentimetre)) {
					System.out.println(	obst.toString());
					obstaclePris=obst;
					System.out.println("Clic");

					xPrecedent = e.getX()/pixelsParCentimetre;
					yPrecedent = e.getY()/pixelsParCentimetre;
					
					//afficherPageObstacle(obst);
				}
			}
		}
	}
	/**
	 * La méthode servant à dessiner les objets de la classe Scene
	 * @param g contexte graphique
	 */
	//Félix Lefrançois
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		if (!debutPartie) {
			System.out.println("Début de la partie!");
			pixelsParCentimetre = getHeight()/hauteurDeLaTableEnMetre;
			largeurDeLaTableEnMetre = LARGEUR_TABLE_VOULUE * pixelsParCentimetre ;
			debutPartie = true;

			tableDeJeu = new Table(hauteurDeLaTableEnMetre, largeurDeLaTableEnMetre, 
					pixelsParCentimetre, coefFrott,masseBalle);
		}

		g2d.scale(pixelsParCentimetre, pixelsParCentimetre) ;

		tableDeJeu.dessiner(g2d);

		dessinerModeEdition(g2d) ;
		dessinerObstacle(g2d);
	}

	private void dessinerObstacle(Graphics2D g2d) {
		if(premierPris) {
			obstaclePris.dessiner(g2d);	
			tableDeJeu.dessinerObstacles(g2d);	

		}
	}

	/**
	 * Méthode privée permettant de dessiner les obstacles à choisir ainsi que le mode édition.
	 * @param g2d : Le contexte graphique.
	 */
	// Par Elias Kassas
	private void dessinerModeEdition(Graphics2D g2d) {
		Graphics2D g2dPrive = (Graphics2D) g2d.create() ;

		// On dessine la ligne de démarquation entre la table et les obstacles à dessiner.
		g2dPrive.setStroke(new BasicStroke(2)) ;
		double posXLigneTableObst = tableDeJeu.getLargeurTable() + 5 ;
		Line2D.Double ligneTableObst = new Line2D.Double(posXLigneTableObst, 0, posXLigneTableObst, 
				getHeight()) ;
		g2dPrive.draw(ligneTableObst) ;

		// On dessine la ligne de démarquation entre les obstacles à dessiner et la page d'obstacle.
		double posXObstaclesMod = posXLigneTableObst + 100 ;
		Line2D.Double ligneObstaclesMod = new Line2D.Double(posXObstaclesMod, 0, posXObstaclesMod, 
				getHeight()) ;
		g2dPrive.draw(ligneObstaclesMod) ;

		double posX = (posXLigneTableObst + posXObstaclesMod)/2 ;
		System.out.println(posX);
		double posY ;
		Color coul = Color.pink ;
		double hautObstacles = hauteurDeLaTableEnMetre/9 ;

		// On ajoute les obstacles à dessiner dans une liste pour faciliter le cliquer-glisser.
		obstaclesADessiner = new CopyOnWriteArrayList<Obstacle>() ;
		posY = (obstaclesADessiner.size() + 1.0/2.0)*hauteurDeLaTableEnMetre/7 + 2.5 ;
		// Ajout du mur
		obstaclesADessiner.add(new Mur(new Vecteur3D(posX, posY), hautObstacles, 60, 0, coul)) ;
		posY = (obstaclesADessiner.size() + 1.0/2.0)*hauteurDeLaTableEnMetre/7 + 2.5 ;
		// Ajout du mur amovible
		obstaclesADessiner.add(new MurAmovible(new Vecteur3D(posX, posY), hautObstacles, 60, 0.0, 0, coul)) ;
		posY = (obstaclesADessiner.size() + 1.0/2.0)*hauteurDeLaTableEnMetre/7 + 5 ;
		// Ajout de la plaque magnétique
		obstaclesADessiner.add(new PlaqueMagnetique(new Vecteur3D(posX, posY), 0.7*hautObstacles, -1.0, 
				coul)) ;
		posY = (obstaclesADessiner.size() + 1.0/2.0)*hauteurDeLaTableEnMetre/7 + 7.5 ;
		// Ajout du ventilateur
		obstaclesADessiner.add(new Ventilateur(new Vecteur3D(posX , posY ), hautObstacles, 
				40, 0, coul)) ;
		posY = (obstaclesADessiner.size() + 1.0/2.0)*hauteurDeLaTableEnMetre/7 + 4.0 ;
		// Ajout du trou noir
		obstaclesADessiner.add(new TrouNoir(new Vecteur3D(posX, posY), 
				hautObstacles, 1000000000, coul)) ;
		posY = (obstaclesADessiner.size() + 1.0/2.0)*hauteurDeLaTableEnMetre/7 + 2.5 ;
		// Ajout du polygone
		obstaclesADessiner.add(new Polygone(new Vecteur3D(posX, posY), 4, hautObstacles, coul)) ;
		posY = (obstaclesADessiner.size() + 1.0/2.0)*hauteurDeLaTableEnMetre/7 + 2.5 ;
		// Ajout du cercle
		obstaclesADessiner.add(new Cercle(new Vecteur3D(posX, posY), hautObstacles, coul)) ;

		// Instructions pour dessiner les différents obstacles du mode édition.
		g2dPrive.setStroke(new BasicStroke(1)) ;
		for (int j = 0 ; j < obstaclesADessiner.size() ; j++) {
			Obstacle obst = obstaclesADessiner.get(j) ;
			obst.dessiner(g2dPrive) ;
		}
	}

	/**
	 * 
	 * @throws ClassNotFoundException 
	 */
	// Par Elias Kassas
	private void afficherPageObstacle(Obstacle obstacle) throws ClassNotFoundException {
		if (obstacle instanceof Mur) {

		} else if (obstacle instanceof MurAmovible) {

		} else if (obstacle instanceof PlaqueMagnetique) {

		} else if (obstacle instanceof Ventilateur) {

		} else if (obstacle instanceof TrouNoir) {

		} else if (obstacle instanceof Polygone) {

		} else {
			throw new ClassNotFoundException("La classe de l'obstacle " + obstacle.toString() + " n'a pu "
					+ "être lue.") ;
		}
	}

	/**
	 * La méthode permettant l'animation des balles et de tous les obstacles se trouvant sur la table
	 */
	@Override
	// Félix Lefrançois
	public void run() {
		while(animationDemarree) {
			calculerIterationPhysique(DELTA_T);

			envoyerTableAppli() ;

			arretAutomatique();

			repaint();
			try {
				Thread.sleep(tempsDuSleep);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Fin de l'animation");
	}

	/**
	 * Méthode permettant d'appliquer les différents changements physiques et vectoriels à la table, aux
	 * obstacles et aux balles se trouvant sur la table
	 * @param deltaT La différence de temps simulé
	 */
	//Félix Lefrançois
	public void calculerIterationPhysique(double deltaT) {
		tempsTotal += deltaT;

		try {
			tableDeJeu.avancerUnPas(deltaT);
			envoyerTempsAppli() ;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Méthode pour démarrer l'animation de la table, des balles et des obstacles
	 */
	//Félix Lefrançois
	public void demarrerAnimation() {
		if (!animationDemarree) {
			Thread threadAnim = new Thread(this);
			threadAnim.start();
			animationDemarree = true;
		}
	} 

	/**
	 * Méthode pour arrêter l'animation de la table, des balles et des obstacles
	 */
	//Félix Lefrançois
	public void arreterAnimation() {
		animationDemarree = false;
	}

	/**
	 * Méthode permettant de réinitialiser la scène de jeu
	 */
	//Félix Lefrançois
	public void reinitialiser() {
		arreterAnimation();
		tableDeJeu.reinitialiserTable();
		tempsTotal = 0;

		repaint();
	}

	/**
	 * Méthode pour avancer l'animation de l'application de DELTA_T quand elle n'est pas partie
	 * @throws Exception Si le vecteur ne peut être normalisée puisqu'il est de longueur trop petite ou nulle
	 */
	//Félix Lefrançois
	public void sautDePas() throws Exception{
		calculerIterationPhysique(DELTA_T);
		envoyerTableAppli() ;
		repaint();
	}

	/**
	 * Méthode permettant de réinitialiser automatiquement la scène
	 */
	//Félix Lefrançois
	public void arretAutomatique () {
		if (animationDemarree) {
			if (tableDeJeu.arreterAnimationTableAutomatiquement() >= NB_DELTAT_AV_REINI_AUTO) {
				envoyerEtatAnimationAppli() ;
				reinitialiser();
				AppPrincipale36.setBouton(animationDemarree);
				AppPrincipale36.setAvancerUnPasBtn(true);
			}
		}		
	}

	public void changerEtirementRessortDepart(double etirement) {
		tableDeJeu.gererEtirementRessortDepart(etirement);
	}

	/**
	 * Méthode permettant de changer la masse de toutes les balle
	 * @param nouvelleMasse La nouvelle masse des balles en kg
	 */
	//Félix Lefrançois
	public void changerMasseDesBalles(double nouvelleMasse) {
		masseBalle = nouvelleMasse;
		tableDeJeu.changerMasseDesBalles(masseBalle);
	}

	/**
	 * Méthode permettant de modifier le coefficient de frottement de table à partir de scene
	 * @param coefFrott Le nouveau coefficient de frottement de la surface de la table
	 */
	//Félix Lefrançois
	public void changerCoefficientDeFrottementTable(double coefFrott) {
		tableDeJeu.setCoefDeFrottement(coefFrott);
	}

	/**
	 * Méthode permettant de changer l'inclinaison de la table
	 * @param nouvelleInclinaison La nouvelle inclinaison en degré de la table
	 */
	//Félix Lefrançois
	public void changerInclinaisonTable(double nouvelleInclinaison) {
		tableDeJeu.setInclinaison(nouvelleInclinaison);
	}

	/**
	 * Méthode permettant de modifier le coefficient de frottement de la surface de la table
	 * @param nouveauCoefFrott Le nouveau coefficient de frottement de la surface de la table
	 */
	//Félix Lefrançois
	public void changerCoefFrott(double nouveauCoefFrott) {
		this.coefFrott = nouveauCoefFrott;
		tableDeJeu.setCoefDeFrottement(nouveauCoefFrott);
	}

	/**
	 * Méthode qui retourne une série de caractère résumant un objet de type Scene
	 */
	//Félix Lefrançois
	public String toString() {
		return tableDeJeu.toString();
	}

	/**
	 * Méthode servant à ajouter un PropertyChangeListener sur la scène.
	 * @param listener : L'écouteur de PropertyChange.
	 */
	// Par Elias Kassas
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.pcs.addPropertyChangeListener(listener);
	}

	/**
	 * Méthode servant à envoyer le temps total de la simulation à la table.
	 */
	// Par Elias Kassas
	private void envoyerTableAppli() {

		this.pcs.firePropertyChange("table", null, tableDeJeu) ;
	}

	/**
	 * Méthode servant à envoyer le temps total de la simulation à l'application.
	 */
	// Par Elias Kassas
	private void envoyerTempsAppli() {
		pcs.firePropertyChange("temps", -tempsTotal, tempsTotal) ;
	}

	/**
	 * Méthode servant à envoyer l'état de l'animation à l'application.
	 */
	// Par Elias Kassas
	private void envoyerEtatAnimationAppli() {
		pcs.firePropertyChange("etatAnimation", !animationDemarree, animationDemarree) ;
	}

	/**
	 * Méthode servant à obtenir le deltaT de la scène.
	 * @return Le deltaT de la scène.
	 */
	// Par Elias Kassas
	public static double getDeltaT() {
		return DELTA_T ;
	}

	/**
	 * Méthode permettant de changer la table affichée.
	 * @param nouvTable : La nouvelle table à charger.
	 */
	// Par Elias Kassas
	public void changerTable(Table nouvTable) {
		this.tableDeJeu = nouvTable ;
		repaint() ;
	}

	/**
	 * Méthode permettant de changer de table.
	 * @param nouvTable : La table à charger
	 */
	// Par Elias Kassas
	public void nouvelleTable() {
		this.tableDeJeu = new Table(hauteurDeLaTableEnMetre, largeurDeLaTableEnMetre, 
				pixelsParCentimetre, coefFrott,masseBalle) ;
		repaint() ;
	}

	public Table getTableActuelle() {
		return tableDeJeu ;
	}

	/**Méthode permettant de gérer quel flipper est actionné
	 * 
	 * @param e La touche du clavier sélectionné
	 */
	//Aimé Melançon
	private void flipperKey(KeyEvent e) {

		if(!Configuration.isFleche()) {
			char code = e.getKeyChar();
			if(code==Configuration. getCoteGauche() ) {
				System.out.println("Gauche");
				if (animationDemarree) {
					tableDeJeu.flipperGaucheMonte();
				}     
			}
			if(code == Configuration.getCoteDroit()) {
				System.out.println("Droit");
				if (animationDemarree) {
					tableDeJeu.flipperDroitMonte();
				}    
			}
		}else {
			int code = e.getKeyCode();

			switch(code){

			case KeyEvent.VK_LEFT : 
				System.out.println("Gauche");
				tableDeJeu.flipperGaucheMonte();
				break;
			case KeyEvent.VK_RIGHT : 
				System.out.println("Droit");
				tableDeJeu.flipperDroitMonte();
				break;
			}
		}
	}

	/**Méthode permettant de savoir si l'application est en mode création ou scientifique.
	 * @return Si l'application est en mode création ou non.
	 */
	//Aimé Melançon
	public boolean isModeCreation() {
		return modeCreation;
	}

	/**Méthode pour activé les fonctionnalités du mode création
	 * @param modeCreation L'argument si oui ou non le mode création est activé.
	 */
	//Aimé Melançon
	public void setModeCreation(boolean modeCreation) {
		this.modeCreation = modeCreation;
	}
}

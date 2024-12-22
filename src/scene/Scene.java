package scene;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import application.Application;
import application.configuration.Configuration;
import composantDeJeu.Balle;
import composantDeJeu.SystemeDePoint;
import composantDeJeu.Table;
import math.vecteurs.Vecteur3D;
import obstacles.Cercle;
import obstacles.Mur;
import obstacles.MurMobile;
import obstacles.Obstacle;
import obstacles.TrouNoir;
import obstacles.Ventilateur;
import obstacles.plaqueMagnetique.PlaqueMagnetique;
import obstacles.polygone.Polygone;
import scene.pagesObstacles.PageCercle;
import scene.pagesObstacles.PageMur;
import scene.pagesObstacles.PageMurMobile;
import scene.pagesObstacles.PagePlaqueMagnetique;
import scene.pagesObstacles.PagePolygone;
import scene.pagesObstacles.PageTrouNoir;
import scene.pagesObstacles.PageVentilateur;
import javax.swing.JTextField;
import javax.swing.JTextPane;

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
	private double hauteurDeLaTableEnCentimetre = 200;
	/** La largeur en metre du composant scene **/
	private double largeurDeLaTableEnCentimetre;
	/** Booléen confirmant si c'est le début de la partie **/
	private boolean debutPartie = false;
	/** Le coefficient de frottement de la surface de la table **/
	private double coefFrott = 0.2;
	/** L'inclinaison de la tableDeJeu**/
	private double inclinaison = 2*Math.PI/18;
	/** Le temps du sleep **/
	private int tempsDuSleep = 10;
	/** Le temps simulé total de la simulation **/
	private double tempsTotal = 0.000;
	/** Support pour lancer des évènements de type PropertyChange. **/
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this) ;
	/** La différence de temps **/
	private static final double DELTA_T = 0.004; 
	/** Largeur de la table voulue. **/
	private final double LARGEUR_TABLE_VOULUE = 29 ;
	/** La masse des balles de la table **/
	private double masseBalle = 1;
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
	private Obstacle obstaclePris=null;
	/**Permet de savoir si un utilisateur à pris au moins un obstacle **/
	private boolean premierPris=false;
	/**La couleur qui apparait quand un obstacle est sélectionné. **/
	private Color couleurSelectionne =new Color (153,51,255);
	/** Les chaines de caractère représentant les lettres des touches affichées sur leur base **/
	private String lettreGauche = "A", lettreDroit = "D";
	/** Le système de points de la scène**/
	private static SystemeDePoint points;
	/** La coordonnée en x de l'affichage des points**/
	private final float X_AFF_POINTS = 105f;
	/** La coordonnée en y de l'affichage des points **/
	private final float Y_AFF_POINTS = 8.5f;
	/**La touche du côté gauche **/
	private char coteGauche='a';
	/**La touche du côté droit **/
	private char coteDroit='d';
	/**Étiquette permettant de connaitre comme supprimer un obstacle sélectionné **/
	private JLabel lblCmtSupp;
	/**Le saut de ligne entre pour la suite du texte dans lblCmtSuppSuite **/
	private JLabel lblCmtSuppSuite1;
	/**Le saut de ligne entre pour la suite du texte dans lblCmtSupp **/
	private JLabel lblCmtSuppSuite;
	/**Le saut de ligne entre pour la suite du texte dans lblCmtSuppSuite1 **/
	private JLabel lblCmtChangerCouleurObst;
	/** La couleur de la surface de la table **/
	private Color couleurTable = Color.RED.darker().darker();
	/**Fonctionne seulement la première fois qu'une scene est créé. **/
	private Boolean premiereFois=true;
	/**La liste qui conserve seulement les obstacles qui sont dans la barre d'outil. **/
	private CopyOnWriteArrayList<Obstacle>obstBarreOutils;
	/**Étiquette qui guide l'utilisateur de comment utiliser le cliquer glisser durant l'utilisation du mode édition. **/
	private JTextPane txtGuidePourUtiliserCliquerGlisser;
	/**Balle fantôme qui permet d'empêcher de mettre un obstacle sur la position (50,100) **/
	private Balle balleFantome;
	/**Le pointeur qui va montrer la position où l'obstacle va être posé. **/
	private Balle pointeur;
	/**Permet de savoir si une page est ouverte ou non et d'afficher le pointeur en conséquence.**/
	private boolean premierePage=false;

	/**
	 * Constructeur de la classe Scene
	 */
	//Aimé Melançon
	public Scene() {
		setLayout(null);

		lblCmtSupp = new JLabel("Pour supprimer un obstacle sélectionné, il faut appuyer ");
		lblCmtSupp.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblCmtSupp.setBounds(1085, 600, 691, 45);
		add(lblCmtSupp);

		lblCmtSuppSuite = new JLabel("sur la touche supprimer un caractère (backspace). De");
		lblCmtSuppSuite.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblCmtSuppSuite.setBounds(1085, 620, 691, 45);
		add(lblCmtSuppSuite);

		lblCmtSuppSuite1 = new JLabel("plus, vous pouvez modifier la couleur de l'obstacle");
		lblCmtSuppSuite1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblCmtSuppSuite1.setBounds(1085, 640, 691, 45);
		add(lblCmtSuppSuite1);

		lblCmtChangerCouleurObst = new JLabel("en effectuant un clic droit avec la souris.");
		lblCmtChangerCouleurObst.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblCmtChangerCouleurObst.setBounds(1085, 660, 691, 45);
		add(lblCmtChangerCouleurObst);

		txtGuidePourUtiliserCliquerGlisser = new JTextPane();
		txtGuidePourUtiliserCliquerGlisser.setEditable(false);
		txtGuidePourUtiliserCliquerGlisser.setFont(new Font("Tahoma", Font.PLAIN, 20));
		txtGuidePourUtiliserCliquerGlisser.setText("Pour déplacer un obstacle, veuillez utiliser le clic gauche de la souris, puis le déplacer sur la table. "
				+ "Si l’obstacle est déjà sur la table, vous pouvez le sélectionner avec le clic gauche et le déplacer ensuite sur la table. Un obstacle ne peut"
				+ " pas être placé en dehors de la table, dans la zone de propulsion de la balle, dans la zone des flippers, sur un autre obstacle ou à l’extérieur "
				+ "de la table. D’ailleurs, il y a un pointeur sur la table qui montre où l’obstacle à ajouter sera lorsqu’il y a une page d’obstacle.");
		txtGuidePourUtiliserCliquerGlisser.setBounds(1085, 695, 600, 205);
		add(txtGuidePourUtiliserCliquerGlisser);

		pointeur= new Balle(new Vecteur3D(50,100), 5, new Color(0, 255, 255, 50));

		points = new SystemeDePoint(0);
		requestFocusInWindow();

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

					e1.printStackTrace();
				}
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				gestionEntreObstacle(e);
			}
			@Override
			public void mouseClicked(MouseEvent e) {

				changerCouleurBalle(e);

				changerCouleurObstaclePris(e);
			}
		}); 
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				toucheUtile( e);
			}
			@Override
			public void keyReleased(KeyEvent e) {
			}
		});
	}
	/**
	 * Méthode permettant de modifier la couleur d'un obstacle avec un clic droit.
	 * @param e La position de la souris
	 */
	//Aimé Melançon
	private void changerCouleurObstaclePris(MouseEvent e) {
		if(e.getButton()== MouseEvent.BUTTON3&&modeCreation&&obstaclePris!=null) {
			if(obstaclePris.contient(e.getX()/pixelsParCentimetre, e.getY()/pixelsParCentimetre)) {

				Color couleur = JColorChooser.showDialog(null, "Choisir la couleur de l'obstacle pris", null) ;
				if(couleur!=null) {
					obstaclePris.setCouleur(couleur);
				}
				repaint();
				tableDeJeu.repeindre();
			}
		}

	}

	/**
	 * Méthode permettant de changer la couleur de la balle grâce à un clic de la souris sur la balle
	 * CETTE MÉTHODE EST INSPIRÉ DE CE QUE AIMÉ A FAIT
	 * @param e La souris
	 */
	//Félix Lefrançois
	private void changerCouleurBalle(MouseEvent e) {
		if(!animationDemarree) {
			for(Balle balle : tableDeJeu.getLesBalles()) {
				if (balle.contient(e.getX()/pixelsParCentimetre, e.getY()/pixelsParCentimetre)) {
					Color couleur = JColorChooser.showDialog(null, "Choisir la couleur de la balle prise", null);
					if (couleur != null) {
						balle.setCouleurBalle(couleur);
					}
				}
			}
			repaint();
		}
	}

	/**
	 * Méthode permettant de faire la gestion entre les obstacle
	 * @param e La position de la souris
	 */
	//Aimé Melançon
	private void gestionEntreObstacle(MouseEvent e) {
		if(obstaclePris!=null&&modeCreation) {
			Area obstaclePrisCopieAire= new Area(obstaclePris.getAire());

			Area aireTableCopie = new Area(tableDeJeu.getAireOuPossibleDePlacer());
			Area aireFlipperD = new Area(tableDeJeu.obtenirAireTotalFlipperDroit());
			Area aireFlipperG = new Area(tableDeJeu.obtenirAireTotalFlipperGauche());
			Area airePropulsion = new Area(tableDeJeu.getAirePropulsion());
			Area aireBalleFantomeCopie = new Area (balleFantome.getAireBalle());
			boolean jePeuxDeposer=true;
			boolean obstacleDistinct= true;
			Obstacle obstacleCopie=null;
			int i=0;

			aireTableCopie.subtract(aireBalleFantomeCopie);
			obstaclePrisCopieAire.subtract(aireTableCopie);


			if(obstaclePrisCopieAire.isEmpty()) {

				if(tableDeJeu.getLesObstacles().contains(obstaclePris)){
					tableDeJeu.getLesObstacles().remove(obstaclePris);
					tableDeJeu.getLesObstacles().add(obstaclePris);
				}

				while(jePeuxDeposer&&i<tableDeJeu.getLesObstacles().size()) {

					if(tableDeJeu.getLesObstacles().get(i).intersection(obstaclePris)) {
						jePeuxDeposer=false;
						obstacleCopie=tableDeJeu.getLesObstacles().get(i);
					}
					i++;
				}

				if(obstacleCopie!=null) {
					if(tableDeJeu.getLesObstacles().contains(obstaclePris)&&obstaclePris!=obstacleCopie) {

						obstacleDistinct=false;


					}
				}

				if(jePeuxDeposer&&obstacleDistinct) {
					deposeObstacle(e);
					obstaclePris.setAnciennePosition(obstaclePris.getPosition());
				} else {
					if(!obstacleDistinct) {



						obstaclePris.setPosition(obstaclePris.getAnciennePosition());	
						obstacleSelectionne=false;

					}
					obstaclePris.setAnciennePosition(obstaclePris.getPosition());
					premierPris=false;
					repaint();
					tableDeJeu.repeindre();
				}
			}else {


				Area aireCopieFlipper = new Area(aireFlipperG);
				aireCopieFlipper.add(new Area(aireFlipperD));
				obstaclePrisCopieAire.subtract(aireBalleFantomeCopie);
				obstaclePrisCopieAire.subtract(aireCopieFlipper);
				obstaclePrisCopieAire.subtract(airePropulsion);
				if(tableDeJeu.getLesObstacles().contains(obstaclePris)&&obstaclePrisCopieAire.isEmpty()) {
					obstaclePris.setPosition(obstaclePris.getAnciennePosition());	
					obstacleSelectionne=false;

					obstaclePris.setAnciennePosition(obstaclePris.getPosition());
					premierPris=false;
					repaint();
					tableDeJeu.repeindre();

				}else if(tableDeJeu.getLesObstacles().contains(obstaclePris)) {
					supprimerObstacle();
					premierPris=false;
					repaint();
					tableDeJeu.repeindre();
				}else {
					premierPris=false;
					repaint();
					tableDeJeu.repeindre();
				}
			}
		}
	}


	/**
	 * Méthode permettant de mettre à null obstaclePris.
	 */
	//Aimé Melançon
	public void setNullObstaclePris() {
		obstaclePris=null;
		premierPris=false;
		premierePage=false;
	}

	/**
	 * Méthode permettant de supprimer un obstacle qui vient d'être sélectionné
	 */
	//Aimé Melançon
	private void supprimerObstacle() {


		if(modeCreation&&obstaclePris!=null) {
			this.tableDeJeu.getLesObstacles().remove(this.obstaclePris);
			this.obstaclesADessiner.remove(this.obstaclePris);


			for(Obstacle obst : this.tableDeJeu.getLesObstacles()) {
				//System.out.println(obst.toString());
				this.obstaclesADessiner.add(obst);
				if(obstaclePris.equals(obst)) {
					this.tableDeJeu.getLesObstacles().remove(obst);
					this.obstaclesADessiner.remove(obst);
				}
			}

			obstacleSelectionne=false;
			premierPris=false;
			obstaclePris=null;
			//tableDeJeu.setLesObstacles(obstaclesADessiner);
			//System.out.println("J'ai effacé un obstacle");
			tableDeJeu.repeindre();
			repaint();
		}
	}

	/**
	 * Méthode permettant de déposer un obstacle à un endroit choisis par l'utilisateur.
	 * @param e La position de la souris
	 */
	//Aimé Melançon
	private void deposeObstacle(MouseEvent e) {
		if(modeCreation) {

			if(premierPris) {

				tableDeJeu.ajouterObstacle(obstaclePris);
				//obstaclesADessiner.add(obstaclePris);

				requestFocusInWindow();

			}
			obstacleSelectionne=false;
			tableDeJeu.repeindre();
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
			requestFocusInWindow();
			if(premierPris) {
				obstaclePris.setDeplacement(new Vecteur3D(e.getX()/pixelsParCentimetre - xPrecedent,e.getY()/pixelsParCentimetre - yPrecedent ));

			}
			xPrecedent = e.getX()/pixelsParCentimetre;
			yPrecedent = e.getY()/pixelsParCentimetre;
			repaint();
			tableDeJeu.repeindre();
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
			requestFocusInWindow();
			obstacleSelectionne=true;

			obstaclesADessiner.addAll(tableDeJeu.getLesObstacles());

			for(Obstacle obst :obstaclesADessiner) {

				if(obst.contient(e.getX()/pixelsParCentimetre, e.getY()/pixelsParCentimetre)) {

					//System.out.println(	obst.toString());
					obstaclePris=obst;
					xPrecedent = e.getX()/pixelsParCentimetre;
					yPrecedent = e.getY()/pixelsParCentimetre;
					premierPris=true;


				}
			}


			for(Obstacle obst : obstBarreOutils) {

				if(obst.contient(e.getX()/pixelsParCentimetre, e.getY()/pixelsParCentimetre)) {
					premierePage=true;
					afficherPageObstacle(obstaclePris);
				}

			}
		}
	}

	/**
	 * La méthode servant à dessiner les objets de la classe Scene
	 * @param g Contexte graphique
	 */
	//Félix Lefrançois
	public void paintComponent(Graphics g) {

		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		AffineTransform transfoInit = g2d.getTransform() ;
		if (!debutPartie) {
			System.out.println("Début de la partie!");
			pixelsParCentimetre = getHeight()/hauteurDeLaTableEnCentimetre;
			largeurDeLaTableEnCentimetre = LARGEUR_TABLE_VOULUE * pixelsParCentimetre ;
			debutPartie = true;

			tableDeJeu = new Table(hauteurDeLaTableEnCentimetre, largeurDeLaTableEnCentimetre, 
					pixelsParCentimetre, coefFrott,inclinaison,masseBalle, lettreGauche,lettreDroit,couleurTable);
		}

		g2d.scale(pixelsParCentimetre, pixelsParCentimetre) ;

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


		tableDeJeu.dessiner(g2d);
		dessinerBalleFantome(g2d);
		dessinerModeEdition(g2d);
		dessinerObstacleSelectione(g2d);
		//dessinerObstacle(g2d);
		tableDeJeu.dessinerBalles(g2d);
		dessinerEchelle(g2d);
		dessinerPoints(g2d);

		g2d.setTransform(transfoInit) ;

		afficherEtiquette();
		dessinerPointeur(g2d);
		dessinerContourFlippers(g2d);

	}

	private void dessinerPointeur(Graphics2D g2d) {
		Graphics2D	g2dPrive = (Graphics2D) g2d.create();
		g2dPrive.scale(pixelsParCentimetre, pixelsParCentimetre);



		if(!animationDemarree && modeCreation&&premierePage) {
			pointeur.dessiner(g2dPrive);
		}

	}
	/**Méthode permettant de dessiner une balle fantôme pour indiquer où la balle se téléporte avec 
	 * le trou noir et empêcher qu’un utilisateur place un obstacle à cet endroit.
	 *
	 * @param g2d Contexte graphique
	 */
	//Aimé Melançon
	private void dessinerBalleFantome(Graphics2D g2d) {
		balleFantome= new Balle(new Vecteur3D(60,20), 5, new Color(255, 255, 255, 50));
		if(!animationDemarree && modeCreation) {
			balleFantome.dessiner(g2d);
		}
	}
	/**
	 * Méthode dessinant des contours autour des flippers pour dire où il n'est pas possible de placer des obstacles dans le mode création
	 * @param g2d Contexte graphique
	 */
	//Félix Lefrançois
	private void dessinerContourFlippers(Graphics2D g2d) {
		Graphics2D	g2dPrive = (Graphics2D) g2d.create();
		g2dPrive.scale(pixelsParCentimetre, pixelsParCentimetre);

		if (!animationDemarree && modeCreation) {
			g2dPrive.draw(tableDeJeu.obtenirAireTotalFlipperDroit());
			g2dPrive.draw(tableDeJeu.obtenirAireTotalFlipperGauche());
		}
	}

	/**
	 * Permet d'afficher l'étiquette seulement quand un obstacle est sélectionné.
	 */
	//Aimé Melançon
	private void afficherEtiquette() {

		if(obstaclePris!=null) {
			lblCmtSupp.setVisible(true);
			lblCmtSuppSuite.setVisible(true);
			lblCmtSuppSuite1.setVisible(true);
			lblCmtChangerCouleurObst.setVisible(true);
		}else {
			lblCmtSupp.setVisible(false);
			lblCmtSuppSuite.setVisible(false);
			lblCmtSuppSuite1.setVisible(false);
			lblCmtChangerCouleurObst.setVisible(false);
		}

	}
	/**Méthode permettant de dessiner et appliqué la couleur magenta à l'obstacle sélectionné.
	 * 
	 * @param g2d Le contexte graphique
	 */
	//Aimé Melançon
	private void dessinerObstacleSelectione(Graphics g2d) {
		Graphics2D	g2dPrive = (Graphics2D) g2d.create();
		if(premierPris) {

			g2dPrive.setColor(couleurSelectionne);
			obstaclePris.dessiner(g2dPrive);

		}

	}

	/**
	 * Méthode servant à dessiner les points du joueur
	 * @param g2d Contexte graphique
	 */
	//Félix Lefrançois
	private void dessinerPoints(Graphics2D g2d) {
		Graphics2D g2dPrive = (Graphics2D) g2d.create() ;

		Font policeActuelle = g2d.getFont() ;
		g2dPrive.setFont(new Font(policeActuelle.getName(), policeActuelle.getStyle(), policeActuelle.getSize()/3)) ;
		g2dPrive.drawString(points.toString(), X_AFF_POINTS, Y_AFF_POINTS);
	}


	/**
	 * Méthode privée servant à dessiner l'échelle sur la scène.
	 * @param g2d : Le contexte graphique.
	 */
	// Par Elias Kassas
	private void dessinerEchelle(Graphics2D g2d) {
		Graphics2D g2dPrive = (Graphics2D) g2d.create() ;
		//g2dPrive.scale(pixelsParCentimetre, pixelsParCentimetre) ;

		float posX = 0.5f, posY = 0.5f ;
		Line2D.Double ligneHor = new Line2D.Double(posX + 1, posY, posX + 2, posY), 
				ligneVert = new Line2D.Double(posX, posY + 1, posX, posY + 2) ;

		g2dPrive.setColor(Color.black) ;

		AffineTransform mat = new AffineTransform() ;
		mat.scale(pixelsParCentimetre, pixelsParCentimetre) ;
		g2dPrive.draw(mat.createTransformedShape(ligneVert)) ;
		g2dPrive.draw(mat.createTransformedShape(ligneHor)) ;

		// Le texte ne respecte pas la mise à l'échelle (pixelsParCentimetre, pixelsParCentimetre).
		Font policeActuelle = g2d.getFont() ;
		g2dPrive.setFont(new Font(policeActuelle.getName(), policeActuelle.getStyle(), policeActuelle.getSize()/3)) ;
		g2dPrive.drawString("1 cm", (posX + 5), (posY + 8)) ;
	}

	/**
	 * Méthode privée permettant de dessiner les obstacles à choisir ainsi que le mode édition.
	 * @param g2d : Le contexte graphique.
	 */
	// Par Elias Kassas
	private void dessinerModeEdition(Graphics2D g2d) {
		Graphics2D g2dPrive = (Graphics2D) g2d.create() ;

		g2dPrive.setFont(new Font(getFont().getName(), getFont().getStyle(), 5)) ;

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

		double posY, posYTexte = 6.0 ;
		Color coul = Color.pink ;
		double hautObstacles = hauteurDeLaTableEnCentimetre/9 ;

		// On ajoute les obstacles à dessiner dans une liste pour faciliter le cliquer-glisser.
		obstaclesADessiner = new CopyOnWriteArrayList<Obstacle>() ;
		posY = (obstaclesADessiner.size() + 1.0/2.0)*hauteurDeLaTableEnCentimetre/7 + 2.5 ;

		// Ajout du mur
		obstaclesADessiner.add(new Mur(new Vecteur3D(posX, posY), hautObstacles, 60, 0, coul)) ;
		g2dPrive.drawString("Mur".toUpperCase(), (int) (posXLigneTableObst + 5), (int) posYTexte) ;
		posY = (obstaclesADessiner.size() + 1.0/2.0)*hauteurDeLaTableEnCentimetre/7 + 2.5 ;

		// Ajout du mur mobile
		obstaclesADessiner.add(new MurMobile(new Vecteur3D(posX, posY), hautObstacles, 60, 0, 30, coul)) ;
		posYTexte += hautObstacles ;
		g2dPrive.drawString("Mur mobile".toUpperCase(), (int) (posXLigneTableObst + 5), (int) posYTexte + 5) ;
		posY = (obstaclesADessiner.size() + 1.0/2.0)*hauteurDeLaTableEnCentimetre/7 + 5 ;

		// Ajout de la plaque magnétique
		obstaclesADessiner.add(new PlaqueMagnetique(new Vecteur3D(posX, posY), 0.7*hautObstacles, -10.0, 
				coul)) ;
		posYTexte += 1.55*hautObstacles ;
		g2dPrive.drawString("Plaque magnétique".toUpperCase(), (int) (posXLigneTableObst + 5), (int) posYTexte) ;
		posY = (obstaclesADessiner.size() + 1.0/2.0)*hauteurDeLaTableEnCentimetre/7 + 7.5 ;

		// Ajout du ventilateur
		obstaclesADessiner.add(new Ventilateur(new Vecteur3D(posX , posY ), hautObstacles, 40, 100,0, coul)) ;
		posYTexte += 1.5*hautObstacles ;
		g2dPrive.drawString("Ventilateur".toUpperCase(), (int) (posXLigneTableObst + 5), (int) posYTexte) ;
		posY = (obstaclesADessiner.size() + 1.0/2.0)*hauteurDeLaTableEnCentimetre/7 + 4.0 ;

		// Ajout du trou noir
		obstaclesADessiner.add(new TrouNoir(new Vecteur3D(posX, posY), 
				hautObstacles, 1000000000, coul)) ;
		posYTexte += 1.5*hautObstacles ;
		g2dPrive.drawString("Trou noir".toUpperCase(), (int) (posXLigneTableObst + 5), (int) posYTexte) ;
		posY = (obstaclesADessiner.size() + 1.0/2.0)*hauteurDeLaTableEnCentimetre/7 + 2.5 ;

		// Ajout du polygone
		obstaclesADessiner.add(new Polygone(new Vecteur3D(posX, posY), 4, hautObstacles, coul, Color.black)) ;
		posYTexte += hautObstacles ;
		g2dPrive.drawString("Polygone".toUpperCase(), (int) (posXLigneTableObst + 5), (int) posYTexte) ;
		posY = (obstaclesADessiner.size() + 1.0/2.0)*hauteurDeLaTableEnCentimetre/7 + 2.5 ;

		// Ajout du cercle
		obstaclesADessiner.add(new Cercle(new Vecteur3D(posX, posY), hautObstacles, coul)) ;
		posYTexte += 1.5*hautObstacles ;
		g2dPrive.drawString("Cercle".toUpperCase(), (int) (posXLigneTableObst + 5), (int) posYTexte) ;

		// Instructions pour dessiner les différents obstacles du mode édition.
		g2dPrive.setStroke(new BasicStroke(1)) ;
		for (int j = 0 ; j < obstaclesADessiner.size() ; j++) {
			//dessinerBouton(g2dPrive, posY, hautObstacles);
			Obstacle obst = obstaclesADessiner.get(j) ;
			obst.dessiner(g2dPrive) ;
			//
			//valPosY += hautObstacles ;
			//g2dPrive.drawString(nomsobstacles.get(j), (int) (posXLigneTableObst + 5), (int) valPosY) ;
		}

		if(premiereFois) {
			obstBarreOutils=obstaclesADessiner;
			premiereFois=false;
		}
	}

	/**
	 * Méthode privée servant à afficher la page de modification d'obstacle associée à un obstacle.
	 * @param obstacle : L'obstacle dont on veut générer la page de modification.
	 * @throws ClassNotFoundException : Si la classe n'est pas dans la liste des obstacles.
	 */
	// Par Elias Kassas
	private void afficherPageObstacle(Obstacle obstacle) throws ClassNotFoundException {
		if (pageObstacle != null) {
			remove(pageObstacle) ;
			pointeur.setPosition(new Vecteur3D(50,100));
		}

		if (obstacle instanceof Mur) {
			pageObstacle = new PageMur() ;
		} else if (obstacle instanceof MurMobile) {
			pageObstacle = new PageMurMobile() ;
		} else if (obstacle instanceof PlaqueMagnetique) {
			pageObstacle = new PagePlaqueMagnetique() ;
		} else if (obstacle instanceof Ventilateur) {
			pageObstacle = new PageVentilateur() ;
		} else if (obstacle instanceof TrouNoir) {
			pageObstacle = new PageTrouNoir() ;
		} else if (obstacle instanceof Polygone) {
			pageObstacle = new PagePolygone() ;
		} else if (obstacle instanceof Cercle) {
			pageObstacle = new PageCercle() ;
		} else {
			throw new ClassNotFoundException("La classe de l'obstacle " +  
					obstacle.getClass() + " n'a pu être lue.") ;
		}
		ajoutObstacle();
		pageObstacle.setBounds(1100, 6, 450, 579) ;
		add(pageObstacle);
		validate();
		repaint() ;
	}
	/**
	 * Méthode permettant d'effectuer le property change des pages.
	 */
	//Aimé Melançon
	private void ajoutObstacle() {
		pageObstacle.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				//System.out.println("J'ai reçu un obstacle");

				changerPositionPointeur(evt);
				recevoirObstacle(evt);

			}
		});

	}



	private void changerPositionPointeur(PropertyChangeEvent evt) {

		if(evt.getPropertyName().equals("pointeur")) {
			//System.out.println("Wow");
			Obstacle obst = (Obstacle) evt.getNewValue();
			//System.out.println(obst.getPosition());
			pointeur.setPosition(obst.getPosition());
			repaint();
		}

	}
	/**
	 * Méthode permettant d'avoir des obstacles modifiés
	 * @param evt L'écouteur du changement d'évent.
	 */
	//Aimé Melançon
	private void recevoirObstacle(PropertyChangeEvent evt) {
		if(evt.getPropertyName().equals("obstacle")) {
			Obstacle obst=(Obstacle)evt.getNewValue();

			boolean jePeuxDeposer=true;
			int i=0;
			Area obstaclePrisCopieAire= new Area(obst.getAire());

			Area aireTableCopie = new Area(tableDeJeu.getAireOuPossibleDePlacer());

			obstaclePrisCopieAire.subtract(aireTableCopie);


			if(obstaclePrisCopieAire.isEmpty()) {
				while(jePeuxDeposer&&i<tableDeJeu.getLesObstacles().size()) {

					if(tableDeJeu.getLesObstacles().get(i).intersection(obst)) {
						jePeuxDeposer=false;

					}
					i++;
				}


				if(jePeuxDeposer) {

					obst.setAnciennePosition(obst.getPosition());
					tableDeJeu.ajouterObstacle(obst);

					System.out.println("Nous avons reçu " +obst.toString());
					repaint();
					tableDeJeu.repeindre();

				}else {
					JOptionPane.showMessageDialog(null,"Vous avez essayé de placer un obstacle sur un autre qui était déjà présent sur la table. Veuillez donc déplacer celui que vous souhaitez ajouter.","Ajout Obstacle", JOptionPane.WARNING_MESSAGE);
				}
			}else {
				JOptionPane.showMessageDialog(null,"Vous avez essayé de placer un obstacle à l'extérieur de la table ou vous avez essayer de mettre votre obstacles dans une zone interdite.\nVeuillez donc déplacer celui que vous souhaitez ajouter.","Ajout Obstacle", JOptionPane.WARNING_MESSAGE);
			}


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

			tableDeJeu.changementPoints();

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
		points.reinitialiser();

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
			if (tableDeJeu.arreterAnimationTableAutomatiquement()) {
				envoyerEtatAnimationAppli() ;
				reinitialiser();
				Application.setBouton(animationDemarree);
				Application.setAvancerUnPasBtn(true);
			}
		}		
	}

	/**
	 * Méthode permettant de modifier l'étirement du ressort initial
	 * @param etirement Le nouvel étirement du ressort
	 */
	//Félix Lefrançois
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
	 * Méthode permettant de changer l'inclinaison de la table
	 * @param nouvelleInclinaison La nouvelle inclinaison en degré de la table
	 */
	//Félix Lefrançois
	public void changerInclinaisonTable(double nouvelleInclinaison) {
		tableDeJeu.setInclinaison(Math.toRadians(nouvelleInclinaison));
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
	 * Méthode changeant la valeur du temps du sleep (en ms)
	 * @param nouveauTempsDuSleep Le nouveau temps du sleep en ms
	 */
	//Félix Lefrançois
	public void setTempsDuSleep(int nouveauTempsDuSleep) {
		this.tempsDuSleep = nouveauTempsDuSleep;
	}

	/**
	 * Méthode permettant de changer la couleur de la surface de la table
	 * @param couleurSurface La nouvelle couleur de la surface de la table
	 */
	//Félix Lefrançois
	public void changerCouleurSurface(Color couleurSurface) {
		this.couleurTable = couleurSurface;
		tableDeJeu.setCouleurSurface(couleurSurface);
		repaint();
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
	public double getDeltaT() {
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
		this.tableDeJeu = new Table(hauteurDeLaTableEnCentimetre, largeurDeLaTableEnCentimetre, 
				pixelsParCentimetre, coefFrott,inclinaison,masseBalle,lettreGauche,lettreDroit,couleurTable) ;
		repaint() ;
	}
	/**
	 * Méthode permettant d'avoir la table actuel.
	 * @return La table de jeu actuelle
	 */
	//Aimé Melançon
	public Table getTableActuelle() {
		return tableDeJeu ;
	}

	/**Méthode permettant de gérer quel flipper est actionné
	 * @param e La touche du clavier sélectionné
	 */
	//Aimé Melançon
	private void toucheUtile(KeyEvent e) {
		if(!modeCreation&&animationDemarree) {
			if(!Configuration.isFleche()) {
				char code = e.getKeyChar();
				if(code==this.coteGauche ) {
					tableDeJeu.flipperGaucheMonte(); 
				}
				if(code == this.coteDroit) {
					tableDeJeu.flipperDroitMonte();
				}
			}else {
				int code = e.getKeyCode();

				switch(code){

				case KeyEvent.VK_LEFT : 
					tableDeJeu.flipperGaucheMonte();
					break;
				case KeyEvent.VK_RIGHT : 
					tableDeJeu.flipperDroitMonte();
					break;
				}
			}
		}
		int code = e.getKeyCode();

		if(code== KeyEvent.VK_BACK_SPACE) {

			supprimerObstacle();
			repaint();
		}

	}

	/**
	 * Méthode pour changer le texte de la touche du flipper gauche
	 * @param nouveauTexte Le nouveau texte de la touche
	 */
	//Félix Lefrançois
	public void changerAffichageToucheGauche(String nouveauTexte) {
		tableDeJeu.modifierAffichageLettreGauche(nouveauTexte);
		lettreGauche = nouveauTexte;
		repaint();
	}

	/**
	 * Méthode pour changer le texte de la touche du flipper droit
	 * @param nouveauTexte Le nouveau texte
	 */
	//Félix Lefrançois
	public void changerAffichageToucheDroite(String nouveauTexte) {
		tableDeJeu.modifierAffichageLettreDroit(nouveauTexte);
		lettreDroit = nouveauTexte;
		repaint();
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

	/**
	 * Méthode permettant de propulser une balle à partir du ressort.
	 * @param balle : La balle à propulser.
	 * @param etirement : L'étirement voulu du ressort.
	 */
	// Par Elias Kassas
	public void propulser(Balle balle, Vecteur3D etirement) throws Exception {
		tableDeJeu.propulser(balle, etirement) ;
	}

	/**
	 * Méthode permettant d'obtenir le facteur d'agrandissement de l'application.
	 * @return Le facteur d'agrandissement de l'application.
	 */
	// Par Elias Kassas
	public double getPixelsParCentimetre() {
		return pixelsParCentimetre ;
	}

	/**
	 * Méthode retournant le système de points de la scène
	 * @return Le système de points de la scene
	 */
	//Félix Lefrançois
	public static SystemeDePoint getPoints() {
		return points;
	}

	/**
	 * Méthode permettant de retirer la constante de rappel du ressort de début de la table
	 * @param constanteDeRappel La nouvelle constante de rappel du ressort de début
	 */
	//Félix Lefrançois
	public void modifierConstanteDeRappel(double constanteDeRappel) {
		tableDeJeu.changerConstanteDeRappel(constanteDeRappel);
	}

	/**Méthode permettant d'avoir la touche gauche
	 * @return le coteGauche
	 */
	//Aimé Melançon
	public char getCoteGauche() {
		return coteGauche;
	}
	
	/**Méthode permettant de changer la touche gauche
	 * @param coteGauche le nouveau côté gauche
	 */
	//Aimé Melançon
	public void setCoteGauche(char coteGauche) {
		this.coteGauche = coteGauche;
		this.lettreGauche= (coteGauche+"").toUpperCase();
	}
	
	/**Méthode permettant d'avoir le cote droit
	 * @return le coteDroit
	 */
	//Aimé Melançon
	public char getCoteDroit() {
		return coteDroit;

	}
	
	/**Méthode permettant de changer le côté droit
	 * @param coteDroit le nouveau coteDroit 
	 */
	//Aimé Melançon
	public void setCoteDroit(char coteDroit) {
		this.coteDroit = coteDroit;
		this.lettreDroit= (coteDroit+"").toUpperCase();
	}
}

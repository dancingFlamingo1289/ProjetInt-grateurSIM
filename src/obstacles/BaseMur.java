package obstacles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.CopyOnWriteArrayList;
import composantDeJeu.Balle;
import composantDeJeu.Table;
import interfaces.Dessinable;
import interfaces.Selectionnable;
import math.vecteurs.Vecteur3D;

/**
 * Classe modèle qui permet de créer des murs et des murs amovibles
 * @author Félix Lefrançois
 */
public abstract class BaseMur extends Obstacle implements Dessinable, Selectionnable {
	/** Identifiant de classe **/
	private static final long serialVersionUID = 1L;
	/** La largeur et la hauteur du mur en créé**/
	private double largeur, hauteur;
	/** Un array list des Line2D.Double composant l'objet de type mur (pour les collisions) **/
	protected CopyOnWriteArrayList<Line2D.Double> lesMurs;
	/** Les différents Line2D.Double composant le mur **/
	private Line2D.Double murBas, murHaut, murDroit, murGauche;
	/** L'aire du mur **/
	protected transient Area aireMur;
	/** Le rectangle permettant de créer l'aire du mur **/
	private Rectangle2D.Double rectangleMur;
	/** La forme transformée résultante des transformations effectuées sur rectangleMur et les différents rectangles du mur **/
	private Shape rectangleTransfo, rectangleTransfo2, rectangleTransfoCentre;
	/** Angle de rotation en radian du mur**/
	private double angle;
	/** La différence de taille des rectangles intérieurs par rapport au rectaugle de base**/
	private final int DIFFERENCE = 2;
	/** La liste des coins du mur **/
	protected CopyOnWriteArrayList<Cercle> lesCoins;
	/** Les cercles représentant les coins du mur **/
	private Cercle cercleGaucheInf,cercleDroitInf,cercleGaucheSup,cercleDroitSup;
	/** Constante du diamètre des coins**/
	private final int DIAM_COIN =  1;

	/**
	 * Constructeur de la classe
	 * @param position Vecteur position du centre du mur
	 * @param hauteur Hauteur en m du mur
	 * @param largeur Largeur en m du mur
	 * @param angle Angle de rotation en radian du mur
	 * @param couleur Couleur du mur
	 */
	//Félix Lefrançois
	public BaseMur(Vecteur3D position, double hauteur, double largeur, double angle, Color couleur) {
		super(position,couleur);
		this.hauteur = hauteur;
		this.largeur = largeur;
		this.angle = angle;
		lesMurs = new CopyOnWriteArrayList<Line2D.Double>();
		lesCoins = new CopyOnWriteArrayList<Cercle>();
	}

	/**
	 * Méthode qui crée tous les composants de dessin du mur
	 */
	//https://stackoverflow.com/questions/41898990/find-corners-of-a-rotated-rectangle-given-its-center-point-and-rotation Par l'utilisateur Tonia Sanzo
	//Le code permet de créer des murs autour d'un centre de rectangle quelconque en prenant en compte l'angle de rotation
	//C'est utiliser pour créer des murs invisibles permettant d'appliquer un effet de collision à la balle
	//Le code a été utilisé tel quel, mais la difficulté de cette partie du code n'avait pas été pensée, donc
	//elle n'affecte en rien la charge de travail planifiée
	//Le code est à 100% utilisé sans modification sauf les noms des variables
	//Félix Lefrançois
	@Override
	protected void creerLaGeometrie() {
		lesMurs.clear();
		lesCoins.clear();
		rectangleMur = new Rectangle2D.Double(0-largeur/2,0-hauteur/2, largeur, hauteur);
		AffineTransform mat = new AffineTransform();
		mat.translate(position.getX(), position.getY());
		mat.rotate(angle);
		rectangleTransfo = mat.createTransformedShape(rectangleMur);
		aireMur = new Area(rectangleTransfo);
		rectangleTransfo2 = mat.createTransformedShape(new Rectangle2D.Double(rectangleMur.getMinX()+DIFFERENCE,rectangleMur.getMinY()+DIFFERENCE,rectangleMur.width-DIFFERENCE*2,
				rectangleMur.height-DIFFERENCE*2));
		rectangleTransfoCentre = mat.createTransformedShape(new Rectangle2D.Double(rectangleMur.getMinX()+2*DIFFERENCE,rectangleMur.getMinY()+2*DIFFERENCE,rectangleMur.width-DIFFERENCE*4,
				rectangleMur.height-DIFFERENCE*4));

		//Voir entête de méthode
		murBas = new Line2D.Double(position.getX()-(largeur/2*Math.cos(angle))-(hauteur/2 *Math.sin(angle)),
				position.getY()-(largeur/2*Math.sin(angle))+(hauteur/2 *Math.cos(angle)),
				position.getX()+(largeur/2*Math.cos(angle))-(hauteur/2 *Math.sin(angle)),
				position.getY()+(largeur/2*Math.sin(angle))+(hauteur/2 *Math.cos(angle)));
		lesMurs.add(murBas);
		murHaut = new Line2D.Double(position.getX()-(largeur/2*Math.cos(angle))+(hauteur/2 *Math.sin(angle)),
				position.getY()-(largeur/2*Math.sin(angle))-(hauteur/2 *Math.cos(angle)),
				position.getX()+(largeur/2*Math.cos(angle))+(hauteur/2 *Math.sin(angle)),
				position.getY()+(largeur/2*Math.sin(angle))-(hauteur/2 *Math.cos(angle)));
		lesMurs.add(murHaut);
		murDroit = new Line2D.Double(position.getX()+(largeur/2*Math.cos(angle))-(hauteur/2 *Math.sin(angle)),
				position.getY()+(largeur/2*Math.sin(angle))+(hauteur/2 *Math.cos(angle)),
				position.getX()+(largeur/2*Math.cos(angle))+(hauteur/2 *Math.sin(angle)),
				position.getY()+(largeur/2*Math.sin(angle))-(hauteur/2 *Math.cos(angle)));
		lesMurs.add(murDroit);
		murGauche = new Line2D.Double(position.getX()-(largeur/2*Math.cos(angle))-(hauteur/2 *Math.sin(angle)),
				position.getY()-(largeur/2*Math.sin(angle))+(hauteur/2 *Math.cos(angle)),
				position.getX()-(largeur/2*Math.cos(angle))+(hauteur/2 *Math.sin(angle)),
				position.getY()-(largeur/2*Math.sin(angle))-(hauteur/2 *Math.cos(angle)));
		lesMurs.add(murGauche);	
		
		 cercleGaucheInf = new Cercle (new Vecteur3D(murBas.x1,murBas.y1,0),DIAM_COIN,Color.BLACK); 
		 lesCoins.add(cercleGaucheInf);
		 cercleDroitInf = new Cercle(new Vecteur3D(murBas.x2,murBas.y2,0),DIAM_COIN,Color.BLACK); 
		 lesCoins.add(cercleDroitInf); 
		 cercleGaucheSup = new Cercle(new Vecteur3D(murHaut.x1,murHaut.y1,0),DIAM_COIN,Color.BLACK); 
		 lesCoins.add(cercleGaucheSup); 
		 cercleDroitSup = new Cercle(new Vecteur3D(murHaut.x2,murHaut.y2,0),DIAM_COIN,Color.BLACK); 
		 lesCoins.add(cercleDroitSup);
	}

	/**
	 * Méthode pour dessiner un mur
	 * @param g2d Contexte graphique
	 */
	//Félix Lefrançois
	@Override
	public void dessiner(Graphics2D g2d) {
		Graphics2D g2dPrive = (Graphics2D) g2d.create();

		g2dPrive.draw(murBas);
		g2dPrive.draw(murDroit);;
		g2dPrive.draw(murGauche);
		g2dPrive.draw(murHaut);

		g2dPrive.setColor(couleur);

		if (aireMur == null)
			creerLaGeometrie() ;
		g2dPrive.fill(aireMur);
		g2dPrive.setColor(couleur.darker());
		g2dPrive.fill(rectangleTransfo2);
		g2dPrive.setColor(couleur.darker().darker());
		g2dPrive.fill(rectangleTransfoCentre);
	}

	/**
	 * Méthode pour vérifier si un point est contenu dans l'aire du mur
	 * @param x Coordonnée en x du point
	 * @param y Coordonnée en y du point
	 * @return Un booléen confirmant si le point est contenu dans l'aire ou non
	 */
	//Félix Lefrançois
	@Override
	public boolean contient(double x, double y) {
		return aireMur.contains(x, y);
	}

	/**
	 * Méthode pour appliquer les effets d'une collision avec le mur sur une balle
	 * @param balle La balle testée
	 * @exception Exception lorsqu'un vecteur est normalisé
	 */
	//Félix Lefrançois
	@Override
	public abstract void collision(Balle balle) throws Exception;

	/**
	 * Méthode vérifiant si il y a une collision entre un mur et une balle
	 * @param balle La balle testée
	 * @return Un booléen confirmant si il y a une collision
	 */
	//Félix Lefrançois
	@Override
	public boolean intersection(Balle balle) {
		return Table.verifierCollisionAvecMurs(balle, lesMurs);
	}

	/**
	 * Méthode résumant les caractéristiques d'un mur
	 * @return Une chaîne de caractères résumant les caractéristiques d'un mur
	 */
	//Félix Lefrançois
	@Override
	public String toString() {
		return "Le centre du mur se trouve aux coordonnées ("+position.getX()+","+position.getY()+") et mesure "+hauteur
				+"m de hauteur et "+largeur+" m de largeur.";
	}

	/**
	 * Méthode qui applique les changements physiques à un mur lors de l'animation
	 * @param deltaT La différence de temps simulé
	 */
	//Félix Lefrançois
	@Override
	public abstract void avancerUnPas(Double deltaT);

	/**
	 * Méthode permettant d'obtenir la largeur en mètre du mur
	 * @return La largeur en mètre du mur
	 */
	//Félix Lefrançois
	public double getLargeur() {
		return largeur;
	}

	/**
	 * Méthode permettant de changer la hauteur en mètre du mur
	 * @param largeur La nouvelle largeur en mètre du mur
	 */
	//Félix Lefrançois
	public void setLargeur(double largeur) {
		this.largeur = largeur;
		creerLaGeometrie();
	}

	/**
	 * Méthode retournant la hauteur en mètre du mur
	 * @return La hauteur en mètre du mur
	 */
	//Félix Lefrançois
	public double getHauteur() {
		return hauteur;
	}

	/**
	 * Méthode permettant de changer la hauteur en mètre du mur
	 * @param hauteur La nouvelle hauteur en mètre du mur
	 */
	//Félix Lefrançois
	public void setHauteur(double hauteur) {
		this.hauteur = hauteur;
		creerLaGeometrie();
	}

	/**
	 * Méthode permettant d'obtenir l'angle de rotation du mur
	 * @return L'angle de rotation en radian du mur
	 */
	//Félix Lefrançois
	public double getAngle() {
		return angle;
	}

	/**
	 * Méthode permettant de changer l'angle de rotation du mur en radian
	 * @param angle Le nouvel angle en radian
	 */
	//Félix Lefrançois
	public void setAngle(double angle) {
		this.angle = angle;
	}

}

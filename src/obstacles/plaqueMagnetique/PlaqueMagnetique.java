package obstacles.plaqueMagnetique;

import java.awt.Color;
import java.awt.Graphics2D ;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import composantDeJeu.Balle;
import interfaces.Dessinable ;
import interfaces.Selectionnable ;
import math.vecteurs.Vecteur3D;
import obstacles.Obstacle ;
import outils.OutilsSon;
import scene.Scene;

/**
 * Cette classe représente une plaque magnétique.
 * La plaque magnétique est un obstacle dessinable, sélectionnable et sérialisable.
 * @author Elias Kassas
 * @author Aimé Melançon
 */
public class PlaqueMagnetique extends Obstacle implements Dessinable, Selectionnable, Serializable {
	/** Coefficient de sérialisation pour les fichiers. **/
	private static final long serialVersionUID = 1L ;
	/** Longueur d'un coté de la plaque magnétique. **/
	private double longueurCote ;
	/** Cadre de la plaque magnétique. **/
	private Rectangle2D.Double cadre ;
	// Paramètres concernant la zone de champ magnétique.
	/** Liste des zones de champ magnétique. **/
	private ZoneDeChampMagnetique[] lesZones ;
	/** Rayon physique d'une zone de champ magnétique. **/
	private double rayonPhys ;
	/** Module du champ magnétique d'une zone de champ magnétique. **/
	private double champMagn ;
	/** Rayon d'attraction minimal et maximal d'une plaque magnétique. **/
	private double rayonAttrMin, rayonAttrMax ;
	/** Constante du nombre de points ajoutés lors d'une collision avec une plaque magnétique**/
	private final int POINTS_AJOUTES = 5;
	/** OutilsSon de l'obstacle**/
	private transient OutilsSon sonObst = new OutilsSon();
	/**Nom du fichier du son**/
	private final String NOM_DU_FICHIER="ChampMagnetique.wav";
	/**Seulement exécuté la première fois que l'application est lancé **/
	private boolean premiereFois=true;

	/**
	 * Constructeur d'une plaque magnétique.
	 * @param position : Le vecteur position du centre de la plaque magnétique. 
	 * @param rayonPhys : 
	 * @param champMagnetique :
	 * @param rayonAttractionMin :  
	 * @param rayonAttractionMax :
	 * @param couleur : La couleur de l'intérieur du cadre de la plaque.
	 */
	// Par Elias Kassas
	public PlaqueMagnetique(Vecteur3D position, double longueurCote, 
			double champMagnetique, Color couleur) {
		super(position, couleur) ;

		this.champMagn = champMagnetique ;
		this.longueurCote = longueurCote ;
		this.rayonPhys = this.longueurCote/2 ;
		this.rayonAttrMin = this.longueurCote/7 ;
		this.rayonAttrMax = this.longueurCote/2 ;
		lesZones = new ZoneDeChampMagnetique[4] ;

		creerLaGeometrie() ;
	}

	/**
	 * Méthode vérifiant si le pixel passé en paramètre est dans la plaque magnétique.
	 * @param xPix : Position en x du pixel.
	 * @param yPix : Position en y du pixel.
	 */
	// Par Elias Kassas
	@Override
	public boolean contient(double xPix, double yPix) {
		return cadre.contains(xPix, yPix) ;
	}

	/**
	 * Méthode privée permettant de créer la géométrie d'une plaque magnétique.
	 */
	// Par Elias Kassas
	@Override
	public void creerLaGeometrie() {
		if(premiereFois) {
			sonObst.chargerUnSonOuUneMusique(NOM_DU_FICHIER);
			premiereFois=false;
		}

		cadre = new Rectangle2D.Double(position.getX() - longueurCote/2, position.getY() - longueurCote/2, 
				longueurCote, longueurCote) ;

		Color coulZdcm = new Color(couleur.getBlue(), couleur.getRed(), couleur.getGreen()) ;

		// Ajoute les nouvelles zones à la liste
		lesZones[0] = new ZoneDeChampMagnetique(position.getX(), position.getY() - longueurCote/2, rayonPhys/4, 
				+champMagn, rayonAttrMin, rayonAttrMax, coulZdcm) ;
		lesZones[1] = new ZoneDeChampMagnetique(position.getX() + longueurCote/2, position.getY(), rayonPhys/4, 
				-champMagn, rayonAttrMin, rayonAttrMax, coulZdcm) ;

		lesZones[2] = new ZoneDeChampMagnetique(position.getX() - longueurCote/2, position.getY(), rayonPhys/4, 
				-champMagn, rayonAttrMin, rayonAttrMax, coulZdcm) ;
		lesZones[3] = new ZoneDeChampMagnetique(position.getX(), position.getY() + longueurCote/2, rayonPhys/4, 
				+champMagn, rayonAttrMin, rayonAttrMax, coulZdcm) ;
	}

	/**
	 * Méthode qui dessine une plaque magnétique.
	 * @param g2d : Le contexte graphique.
	 */
	// Par Elias Kassas
	@Override
	public void dessiner(Graphics2D g2d) {
		Graphics2D g2dPrive = (Graphics2D) g2d.create() ;
		g2dPrive.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON) ;
		g2dPrive.setColor(couleur) ;
		g2dPrive.fill(cadre) ;
		g2dPrive.setColor(Color.black) ;
		g2dPrive.draw(cadre) ;

		// Dessiner chaque zone avec une couleur différente
		g2dPrive.setColor(Color.black) ;
		for (ZoneDeChampMagnetique zone : lesZones) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON) ;
			zone.dessiner(g2d) ;
		}
	}

	/**
	 * Méthode vérifiant l'intersection avec la plaque magnétique.
	 */
	// Par Elias Kassas
	@Override
	public boolean intersection(Balle balle) {
		for (ZoneDeChampMagnetique zone : lesZones) {
			// On vérifie s'il y a une intersection avec l'une des zones.
			if (zone.intersection(balle)) {
				return true ;
			}
		}

		return false ;
	}

	/**
	 * Méthode établissant la collision avec une plaque magnétique.
	 * @param balle : La balle à tester.
	 */
	// Par Elias Kassas
	@Override
	public void collision(Balle balle) throws Exception {
		for (ZoneDeChampMagnetique zone : lesZones) {
			// On vérifie s'il y a une intersection avec l'une des zones pour faire la collision.
			if (zone.intersection(balle)) {
				zone.collision(balle) ;

				if (sonObst == null) {
					sonObst = new OutilsSon() ;
					creerLaGeometrie() ;
				}
			}
		}

		Scene.getPoints().ajouterPoints(POINTS_AJOUTES);
	}

	/**
	 * Méthode permettant d'avoir sous force de chaîne de caractères les différentes propriétés d'une 
	 * plaque magnétique.
	 */
	// Par Elias Kassas
	@Override
	public String toString() {
		return "PlaqueMagnetique [longueurCote = " + longueurCote + ", cadre = " + cadre + ", lesZones = " + 
				lesZones + ", rayonPhys = " + rayonPhys + ", champMagn = " + champMagn + ", rayonAttrMin = " + 
				rayonAttrMin + ", rayonAttrMax = " + rayonAttrMax + ", position = " + position + 
				", couleur = " + couleur + "]" ;
	}

	/**
	 * Méthode permettant d'animer une plaque magnétique.
	 * Notez qu'elle ne fait rien étant donné que la plaque magnétique n'a pas besoin d'être animée.
	 */
	@Override
	public void avancerUnPas(Double deltaT) {
		// TODO Auto-generated method stub
	}

	/**
	 * Méthode permettant de faire un déplacement d'une plaque magnétique sur la table.
	 * @param deplacement Le déplacement de l'obstacle effectué.
	 */
	//Aimé Melançon
	@Override
	public void setDeplacement(Vecteur3D deplacement) {
		this.setPosition(this.position.additionne(deplacement));

	}

	/**
	 * Méthode servant à réinitialiser la plaque magnétique. INUTILE
	 */
	// Par Elias Kassas
	@Override
	public void reinitialiser() {
		// TODO Auto-generated method stub

	}

	/**
	 * Méthode servant à établir l'intersection avec un autre obstacle pour le cliquer-glisser.
	 * @return Si l'obstacle passé en paramètre intersecte la plaque.
	 */
	// Par Elias Kassas
	@Override
	public boolean intersection(Obstacle obst) {
		// TODO Auto-generated method stub
		Area cadreAire = new Area(cadre);
		cadreAire.intersect(new Area(obst.getAire())) ;

		boolean intersection = false ;
		for (ZoneDeChampMagnetique zone : lesZones) {
			intersection = zone.intersection(obst) ;
			
			if (intersection == true)
				break ;
		}
		
		return !cadreAire.isEmpty() || intersection ;
	}

	/**
	 * Méthode permettant d'obtenir l'objet Area du rectangle de la boîte englobante de la plaque 
	 * magnétique.
	 * @return L'area du rectangle de la boîte englobante de la plaque magnétique.
	 */
	// Par Elias Kassas
	@Override
	public Area getAire() {
		Rectangle2D.Double carrePlaque = new Rectangle2D.Double(position.getX() - longueurCote, 
				position.getY() - longueurCote, longueurCote + rayonPhys/2, longueurCote + rayonPhys/2) ;
		Area rh = new Area(carrePlaque) ;
		
		for (ZoneDeChampMagnetique z : lesZones) {
			rh.add(z.getAire()) ;
		} 
		
		return rh ;
	}
}

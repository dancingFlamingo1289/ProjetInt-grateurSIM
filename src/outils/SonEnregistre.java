package outils;

import java.net.URL;


/**
 * Classe permettant de créer un objet de type contenu qui contient le contenu jouer et l'audio sauvegarder
 * @author Aimé Melançon
 */
public class SonEnregistre {
	/**Le nom du son ou de la musique en terme fichier **/
	private  String nomDuFichier ; 
	/**le fichier URL jouer **/
	private URL urlFichier=null ;

	/**
	 * Constructeur de l'objet SonEnregistre qui crée une "sauvegarde" d'un son d'obstacle qui a été chargé 
	 * au moins une fois.
	 * 
	 * @param nomDuFichier Le nom du fichier du son.
	 * @param urlFichier L'url généré pour produire du son.
	 */
	//Aimé Melançon
	public SonEnregistre(String nomDuFichier,URL urlFichier ) {
		this.nomDuFichier=nomDuFichier;
		this.urlFichier= urlFichier;
	}
	
	/**
	 * Méthode permettant d'avoir le nom du fichier
	 * @return Retourne le nom du fichier 
	 */
	//Aimé Melançon
	public String getNomDuFichier() {
		return nomDuFichier;
	}
	
	/**
	 * Méthode permettant de changer le nom d'un fichier d'un SonEnregistre.
	 * @param nomDuFichier Le nouveau nom du fichier
	 */
	//Aimé Melançon
	public void setNomDuFichier(String nomDuFichier) {
		this.nomDuFichier = nomDuFichier;
	}
	
	/**
	 * Méthode permettant de savoir si un nom du fichier est déjà existant dans un objet.
	 * @param nomDuFichier Le nom du fichier qu'on veut savoir s'il est dans l'objet.
	 * @return Retourne si le nom du fichier est dans l'objet ou non.
	 */
	//Aimé Melançon
	public boolean contientNomDuFichier(String nomDuFichier) {
		if(this.nomDuFichier==nomDuFichier) {
			return true;
		}else {
			return false;
		}
	}
	
	/**
	 * Méthode permettant d'avoir un fichier audio jouable.
	 * @return Retourne objetFichier
	 */
	//Aimé Melançon
	public URL getUrlFichier() {
		return urlFichier;
	}
	
	/**
	 * Méthode permettant de changer un fichier audio.
	 * @param objetFichier Le nouveau fichier audio jouable
	 */
	//Aimé Melançon
	public void setUrlFichier(URL urlFichier) {
		this.urlFichier = urlFichier;
	}
}

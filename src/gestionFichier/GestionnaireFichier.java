package gestionFichier;

import java.io.* ;
import javax.swing.* ;
import composantDeJeu.* ;

/**
 * Classe servant à gérer le système de sauvegarde de l'application
 * @author Caroline Houle
 * @author Elias Kassas
 */
public class GestionnaireFichier {
	/** Sous dossier de travail dans lequel seront sauvegardées les tables crées par l'utilisateur. **/
	private String sousDossierTravail = "Tables sauvegardées - FrissonBoum" ;
	/** Dossier de travail de l'utilisateur. **/
	private static File dossierUtilisateur ;

	/**
	 * Le constructeur permet d'initialiser le dossier de travail
	 */
	// Par Caroline Houle
	public GestionnaireFichier() {
		// chemin d'acces à un emplacement où l'utilisateur a des droits en écriture (espace personnel, 
		// par exemple "...\OneDrive\Documents".
		dossierUtilisateur = new File(javax.swing.filechooser.FileSystemView.getFileSystemView().
				getDefaultDirectory().getPath() + "//" + sousDossierTravail) ;
	}//fin constructeur

	/**
	 * Méthode permettant de sauvegarder un objet de type Object
	 * @return Une table précédemment sauvegardée
	 */
	//Par Elias Kassas
	public Object lire(String nomFichier) {
		// chemin d'acces au fichier de travail
		File fichierDeTravail = new File(dossierUtilisateur + "//" + nomFichier) ;

		Table tableLue = null ;

		ObjectInputStream ois = null ;
		try {
			ois = new ObjectInputStream(new FileInputStream(fichierDeTravail)) ;
			tableLue = (Table) ois.readObject() ;
			JOptionPane.showMessageDialog(null, "Table lue avec succès !") ;
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Erreur : Le fichier " + nomFichier + " est introuvable.") ;
			e.printStackTrace() ;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erreur : I/O Exception " + nomFichier) ;
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			// on exécutera toujours ceci, erreur ou pas
			try {
				ois.close();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null,"Erreur rencontrée lors de la fermeture!");
				e.printStackTrace();
			}
		}

		return tableLue ;
	}

	/**
	 * Méthode permettant de sauvegarder un objet de type Object dans un fichier .bin
	 * @param tableASauvegarder : Table à sauvegarder.
	 * @param nomFichier : Nom du fichier voulu. Il ne faut pas inclure le ".bin" !
	 */
	// Par Elias Kassas
	public void sauvegarder(Table tableASauvegarder, String nomFichier) {
		nomFichier += ".bin" ;

		// On crée le dossier s'il n'existe pas.
		if (dossierUtilisateur.mkdir()) {
			JOptionPane.showMessageDialog(null, "Le dossier " + dossierUtilisateur.toString() + 
					" a été créé car il n'existait pas.") ;
		}

		// chemin d'acces au fichier de travail
		File fichierDeTravail = new File(dossierUtilisateur + "//" + nomFichier);

		ObjectOutputStream objOutputStr = null ;

		try {
			objOutputStr = new ObjectOutputStream(new FileOutputStream(fichierDeTravail, 
					fichierDeTravail.exists()));

			//on écrit chacun des objets
			objOutputStr.writeObject(tableASauvegarder) ;
			JOptionPane.showMessageDialog(null, "Fichier de table sauvegardé avec succès !\n"
					+ "Il est trouvable dans le dossier '" + dossierUtilisateur.getAbsolutePath() + "'.") ;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erreur à l'écriture : " + e.toString()) ;
			e.printStackTrace() ;
		} finally {
			// on exécutera toujours ceci, erreur ou pas
			try {
				objOutputStr.close() ;
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Erreur rencontrée lors de la fermeture : " + 
						e.toString()) ;
				e.printStackTrace() ;
			}
		} // fin finally
	}

	/**
	 * Méthode permettant d'écrire un fichier de Table vierge
	 * @param nomFichier
	 */
	// Par Elias Kassas
	/*public static void ecrireFichierTableVierge(String nomFichier) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomFichier)) ;
			JOptionPane.showMessageDialog(null, "Fichier de table créé avec succès !") ;
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Table sauvegardée avec succcès dans le fichier " + nomFichier 
					+ ".") ;
			e.printStackTrace() ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace() ;
		}
	}*/

	/**
	 * Méthode permettant d'écrire le fichier d'une table.
	 * @param nomFichier
	 * @param table
	 */
	// Par Elias Kassas
	public static void ecrireFichierTable(String nomFichier, Table table) {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomFichier)) ;
			JOptionPane.showMessageDialog(null, "Fichier de table créé avec succès !") ;

			oos.writeObject(table) ;

			oos.close() ;
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Table non sauvegardée dans le fichier " 
					+ nomFichier + ".") ;
			e.printStackTrace() ;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace() ;
		}
	}

	public Table lireTableInit(String nomFichier) {
		ObjectInputStream ois=null;
		InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(nomFichier);

		if (is == null) {
			JOptionPane.showMessageDialog(null, "Incapable de trouver ce fichier dans le BuildPath (ou dans "
					+ "le jar exécutable) : " + nomFichier) ;
		}

		Table tableLue = null ;
		
		// ce fichier a été conçu d'avance et placé dans un dossier qui fait partie du Build Path
		try {
			ois = new ObjectInputStream(is) ;
			// on lit d'un coup un objet stocké dans le fichier
			tableLue = (Table) ois.readObject() ;
			JOptionPane.showMessageDialog(null, "Le fichier " + nomFichier + " a été lu avec succès.") ;
		} catch (InvalidClassException e) {
			JOptionPane.showMessageDialog(null, "Les classes utilisées pour l'écriture et la lecture "
					+ "différent !\n" + e.toString()) ;
			e.printStackTrace() ;
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "Fichier " + nomFichier + "  introuvable.\n" + e.toString()) ;
			System.exit(0);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Erreur rencontree lors de la lecture " + nomFichier + ".\n"
					+ e.toString()) ;
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null,"L'objet lu est d'une classe inconnue.\n" + e.toString()) ;
			e.printStackTrace() ;
		} finally {
			// on exécutera toujours ceci, erreur ou pas.
			try { 
				ois.close();
			} catch (IOException e) { 
				JOptionPane.showMessageDialog(null, "Erreur rencontrée lors de la fermeture !") ; 
				e.printStackTrace();
			}
		} // fin finally
		
		return tableLue ;
	}
}

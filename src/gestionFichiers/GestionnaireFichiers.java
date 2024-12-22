package gestionFichiers;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.JOptionPane;

import application.configuration.Theme;
import application.gestionScore.Score;
import composantDeJeu.Table;

/**
 * Classe servant à gérer le système de sauvegarde de l'application
 * @author Caroline Houle
 * @author Elias Kassas
 */
public class GestionnaireFichiers {
	/** Sous dossier de travail dans lequel seront sauvegardées les tables crées par l'utilisateur. **/
	private String sousDossierTravail = "Tables sauvegardées - FrissonBoum" ;
	/** Dossier de travail de l'utilisateur. **/
	private static File dossierUtilisateur, dossierCache ;
	/** Fichier imaginaire pour créer le fichier de configuration ainsi que vérifier s'il existe dans le 
	 * dossier du projet. **/
	private File nomFichierConfig ;
	/** Incrément pour les noms de table. **/
	private int nbFichier = 0 ;
	/** Ce qu'il faut écrire. **/
	private String indicDernierSauv = "Dernier sauvegardé : " ;
	/** Thème par défaut à charger au sein du fichier de configuration. **/
	private Theme theme = new Theme("Halloween", Color.orange, new Color(175, 122, 197), Color.black,
			Color.black) ;
	/** Liste des cinq meilleurs scores de l'utilisateur mémorisés dans le fichier de configuration. **/
	private ArrayList<Score> lesScores ;
	// Ceci est un commentaire.

	/**
	 * Le constructeur permet d'initialiser le dossier de travail.
	 */
	// Par Caroline Houle
	public GestionnaireFichiers() {
		// chemin d'acces à un emplacement où l'utilisateur a des droits en écriture (espace personnel, 
		// par exemple "...\OneDrive\Documents".
		dossierUtilisateur = new File(javax.swing.filechooser.FileSystemView.getFileSystemView().
				getDefaultDirectory().getPath() + "//" + sousDossierTravail) ;

		creerDossierCacheEtLancerConfiguration() ;
	}//fin constructeur

	/**
	 * Méthode privée ayant pour but de créer le dossier caché et de lancer la méthode configuration.
	 */
	// Par Elias Kassas
	private void creerDossierCacheEtLancerConfiguration() {
		dossierCache = new File(javax.swing.filechooser.FileSystemView.getFileSystemView().
				getDefaultDirectory().getPath() + "//" + ".frissonBoumTopSecret") ;

		configuration() ;
	}

	/**
	 * Méthode privée servant à créer le fichier de configuration s'il n'existe pas et recueillir l'incrément
	 * de fichier si le fichier existe.
	 */
	// Par Elias Kassas
	private void configuration() {
		// On crée le dossier caché.
		dossierCache.mkdir() ;

		// On y passe le fichier de configuration.
		nomFichierConfig = new File(dossierCache + "//" + "configuration.txt") ;

		if (nomFichierConfig.exists()) {
			lesScores = new ArrayList<Score>() ;

			// On extrait les informations du fichier de configuration.
			FileReader fichier;
			try {
				fichier = new FileReader(nomFichierConfig) ;
				BufferedReader fluxEntree = new BufferedReader(fichier) ;
				fluxEntree.readLine() ;
				String ligne = fluxEntree.readLine() ;
				nbFichier = Integer.parseInt(ligne) ;
				fluxEntree.readLine() ;

				// Pour le thème.
				fluxEntree.readLine() ;
				String ligneNom = fluxEntree.readLine() ;
				String nom = ligneNom.substring(ligneNom.indexOf('=') + 1) ;
				Color coulPrim, coulSeco, coulTert, coulPol ;
				coulPrim = traiterCouleur(fluxEntree.readLine()) ;
				coulSeco = traiterCouleur(fluxEntree.readLine()) ;
				coulTert = traiterCouleur(fluxEntree.readLine()) ;
				coulPol = traiterCouleur(fluxEntree.readLine()) ;

				theme = new Theme(nom, coulPrim, coulSeco, coulTert, coulPol) ;

				// Pour les scores
				fluxEntree.readLine() ;
				for (int i = 0 ; i < 5 ; i++)
					lesScores.add(Score.decoder(fluxEntree.readLine())) ;
				Collections.sort(lesScores) ;

				fluxEntree.close() ;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace() ;
			}
		} else {
			// On crée le fichier et on y ajoute les informations.
			try {
				JOptionPane.showMessageDialog(null, "Le fichier de configuration est inexistant.\n"
						+ "Nous le recréons avec des valeurs par défaut !") ;

				nbFichier = 0 ;
				FileWriter fichier = new FileWriter(nomFichierConfig, nomFichierConfig.exists()) ;
				fichier.write("Dernier sauvegardé\n" + nbFichier + "\n") ;

				fichier.write(theme.toString()) ;

				lesScores = new ArrayList<Score>() ;
				for (int i = 0 ; i < 5 ; i++)
					lesScores.add(new Score()) ;

				for (Score s : lesScores) {
					fichier.write(s.toString()) ;
				}

				fichier.close() ;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace() ;
			}
		}
	}

	/**
	 * Méthode servant à traiter un objet Color à partir de son toString.
	 * @param ligneCoul : Le toString de l'objet Color.
	 * @return L'objet Color associé au toString.
	 */
	// Par Elias Kassas
	private Color traiterCouleur(String coulStr) {
		String vecCoul = coulStr.substring(coulStr.indexOf("[") + 1, coulStr.indexOf("]")) ;
		String[] coul = vecCoul.split(",") ;

		int rouge = Integer.parseInt(coul[0].substring(coul[0].indexOf("r") + 2)); 
		int vert = Integer.parseInt(coul[1].substring(coul[1].indexOf("g") + 2)); 
		int bleu = Integer.parseInt(coul[2].substring(coul[2].indexOf("b") + 2)); 
		return new Color(rouge, vert, bleu) ;
	}

	/**
	 * Méthode permettant de sauvegarder un objet de type Object
	 * @return Une table précédemment sauvegardée
	 */
	// Par Elias Kassas
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
	public void sauvegarder(Table tableASauvegarder) {
		String nomFichier = "table" + nbFichier + ".bin" ;

		// On crée le dossier s'il n'existe pas.
		if (dossierUtilisateur.mkdir()) {
			JOptionPane.showMessageDialog(null, "Le dossier " + dossierUtilisateur.toString() + 
					" a été créé car il n'existait pas.") ;
		}

		BufferedReader fluxEntree ;
		try {
			fluxEntree = new BufferedReader(new FileReader(nomFichierConfig)) ;
			//fluxEntree.readLine() ;

			if (nomFichierConfig.exists()) { 
				// Le dernier chiffre mémorisé dans le fichier de configuration.) {
				nbFichier++ ;

				ecrireFichierConfiguration() ;

				ecrireFichierTable(nomFichier, tableASauvegarder) ;
			} else {
				configuration() ;
				// On réécrit le fichier.
				ecrireFichierTable(nomFichier, tableASauvegarder) ;
				JOptionPane.showMessageDialog(null, "Le fichier " + nomFichier + 
						" a été réécrit avec succès.") ;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	 * @param nomFichier : Le nom du fichier.
	 * @param La table à écrire.
	 */
	// Par Elias Kassas
	public static void ecrireFichierTable(String nomFichier, Table table) {
		File fichierDeTravail = new File(dossierUtilisateur + "//" + nomFichier) ;

		ObjectOutputStream objOutputStr = null ;

		try {
			objOutputStr = new ObjectOutputStream(new FileOutputStream(fichierDeTravail, 
					fichierDeTravail.exists()));

			//on écrit chacun des objets
			objOutputStr.writeObject(table) ;
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
	 * Méthode servant à lire un fichier de table présent dans le BuildPath.
	 * @param nomFichier : Le nom du fichier.
	 * @return La table lue.
	 */
	// Par Elias Kassas
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

	/**
	 * Méthode permettant d'obtenir le thème enregistré dans le fichier de configuration.
	 * @return Le thème enregistré dans le fichier de configuration.
	 */
	// Par Elias Kassas
	public Theme obtenirThemeActuel() {
		return this.theme ;
	}

	/**
	 * Méthode permettant de modifier le thème enregistré dans le fichier de configuration.
	 * @param nouvTheme : Le nouveau thème à enregistrer dans le fichier de configuration.
	 */
	// Par Elias Kassas
	public void modifierThemeActuel(Theme nouvTheme) {
		this.theme = nouvTheme ;

		ecrireFichierConfiguration() ;
	}

	/**
	 * Méthode permettant de modifier les scores enregistrés dans le fichier de configuration.
	 * @param lesNouveauxScores : Les nouveaux scores à enregistrer dans le fichier de configuration.
	 */
	// Par Elias Kassas
	public void modifierLesScores(ArrayList<Score> lesNouveauxScores) {
		Collections.sort(lesNouveauxScores) ;
		this.lesScores = lesNouveauxScores ;

		ecrireFichierConfiguration() ;
	}

	public ArrayList<Score> obtenirLesScores() {
		Collections.sort(lesScores) ;
		return lesScores ;
	}

	/**
	 * Méthode permettant d'écrire le fichier de configuration.
	 */
	// Par Elias Kassas
	private void ecrireFichierConfiguration() {
		BufferedReader fluxEntree ;
		try {
			fluxEntree = new BufferedReader(new FileReader(nomFichierConfig)) ;
			fluxEntree.readLine() ;
			if (nomFichierConfig.exists()) { 
				//nbFichier++ ;

				FileWriter fluxSortie ;
				try {
					fluxSortie = new FileWriter(nomFichierConfig) ;
					fluxSortie.write(indicDernierSauv + "\n" + nbFichier + "\n") ;

					fluxSortie.write(theme.toString()) ;

					for (Score s : lesScores) {
						fluxSortie.write(s.toString()) ;
					}

					fluxSortie.close() ;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				// On réécrit le fichier.
				configuration() ;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public File getDossierUtilisateur() {
		return dossierUtilisateur ;
	}
}

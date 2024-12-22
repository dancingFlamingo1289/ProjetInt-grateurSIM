package outils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JOptionPane;

/**Outil permettant de gérer les musiques et les sons en général de l'application.
 * 
 * @author Aimé Melançon
 * @author Caroline Houle
 */
public class OutilsSon  {
	
	/**Le clip joué **/
	private   Clip  leClip=null;
	/**Le nom du son ou de la musique en terme fichier **/
	private  String nomDuFichier ; 
	/**L'audio entendu **/
	private  AudioInputStream audioStr;
	/**La trace du fichier développer **/
	private URL urlFichier = null; 
	/**C'est le registre des sons déjà chargé une fois. **/
	private static ArrayList <SonEnregistre> registreDesSons= new ArrayList<SonEnregistre>();


	/**
	 * Méthode permettant de charger et jouer une musique
	 * @param nomDuFichier Le nom du fichier de musique sélectionné
	 */
	//Aimé Melançon
	public void chargerUneMusiqueEtJouer(String nomDuFichier) {
		this.nomDuFichier=nomDuFichier;

		jouerUneMusique();
	}
	/**
	 * Méthode permettant de charger et jouer un son
	 * @param nomDuFichier Le nom du fichier du son sélectionné
	 */
	//Aimé Melançon
	public void chargerUnSonDObstacleEtJouer(String nomDuFichier) {
		this.nomDuFichier=nomDuFichier;

		jouerUnSon();
	}

	/**
	 * Méthode permettant de seulement charger une musique ou un son
	 * @param nomDuFichier Le nom du fichier du son ou de la musique sélectionné
	 */
	//Aimé Melançon
	public void chargerUnSonOuUneMusique(String nomDuFichier) {
		this.nomDuFichier=nomDuFichier;

	}
	/**
	 * Méthode permettant de faire jouer une musique en boucle.
	 */
	//Aimé Melançon
	public  void jouerUneMusique() {

		if(leClip!=null) {
			leClip.close();
		}

		chargerLeSon();
		leClip.loop(Clip.LOOP_CONTINUOUSLY);
		leClip.flush();
	}
	/**
	 * Méthode pour faire jouer un son.
	 */
	//Aimé Melançon
	/*public void jouerUnSon() {
		boolean sonDejaExistant=false;
		int i=0;
		
		if(leClip!=null) {
			if(!registreDesSons.isEmpty()) {
				while(sonDejaExistant==false&&i<registreDesSons.size()) {

					if(registreDesSons.get(i).contientNomDuFichier(nomDuFichier)) {
						sonDejaExistant=true;
					}else {
						i++;
					}

				}
			}
		}
		
		if(sonDejaExistant==false) {
			
			System.out.println("Je charge un son");
			
			chargerLeSon();
			leClip.start();
			
		}else {
			try {
				
				leClip = AudioSystem.getClip();
				leClip.open( AudioSystem.getAudioInputStream(registreDesSons.get(i).getUrlFichier()));
			} catch (UnsupportedAudioFileException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (audioStr != null) { 

				try {
					audioStr.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 

				leClip.close(); 

				} 
			leClip.start();
			
		}
	}*/
	/**
	 * Méthode pour faire jouer un son.
	 */
	//Aimé Melançon
	public void jouerUnSon() {
		if(leClip!=null) {
			leClip.close();
		}

		chargerLeSon();
		leClip.start();
		leClip.flush();
		
	}
	/**
	 * Méthoder permettant d'arrêter un son ou une musique
	 */
	//Aimé Melançon
	public void arreterUneMusique() {
		if(leClip!=null) {
			leClip.stop();
		}
	}

	/**
	 * Methode privee pour lire le son et en faire un clip
	 * 
	 */
	//Caroline Houle
	void chargerLeSon() {

		try {
			//si ce n'est pas la premiere fois, on evite de reacceder au fichier sur disque (consomme du temps)
			if (urlFichier==null) { 

				urlFichier = getClass().getClassLoader().getResource(nomDuFichier); 
				registreDesSons.add(new SonEnregistre(nomDuFichier, urlFichier));
				 

				} 
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Incapable d'ouvrir le fichier de son ");
			e.printStackTrace();
			return;
		}

		try {
			if (audioStr != null) { 

				audioStr.close(); 

				leClip.close(); 

				} 

				audioStr = AudioSystem.getAudioInputStream( urlFichier ); 

				leClip = AudioSystem.getClip(); 

				leClip.open(audioStr); 
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Probl�me � la cr�ation du clip (son)! " + nomDuFichier);
			e.printStackTrace();
			return;
		}

	}// fin methode
}

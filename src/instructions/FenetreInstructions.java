package instructions;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import application.configuration.Theme;
import gestionFichiers.GestionnaireFichiers;

/**
 * Exemple de fenetre qui cree un JPanel dans lequel sera affichees (et bien ajustees) une suite d'images 
 * representant du texte continu. Les boutons places sur cette fenetre permettent de passer a l'image 
 * precedente/suivante.
 * 
 * @author Caroline Houle
 * @author Elias Kassas
 */
public class FenetreInstructions extends JFrame {
	/** Numéro d'identifiant de classe **/
	private static final long serialVersionUID = 1L;
	/**Le panneau qui contient les instructions. **/
	private JPanel contentPane;
	/**Le bouton servant pour aller voir l'instruction précédente. **/
	private JButton btnPagePrecedente;
	/**Le bouton servant pour aller voir l'instruction suivante. **/
	private JButton btnPageSuivante;
	/** C'est ici que l'on declare un tableau ou on enumere toutes les pages d'aide desirees. **/
	private String tableauImages[] = {"Instructions-01.png","Instructions-02.png","Instructions-03.png",
			"Instructions-04.png","Instructions-05.png","Instructions-06.png",
			"Instructions-07.png","Instructions-08.png","Instructions-09.png", 
			"Instructions-10.png","Instructions-11.png","Instructions-12.png",
			"Instructions-13.png","Instructions-14.png"} ;
	/** Gestionnaire de fichier pour accéder au thème de l'application. **/
	private GestionnaireFichiers gest ;
	/** Liste contenant tous les composants de la fenêtre pour l'application d'un thème. **/
	private ArrayList<Component> lesComposants ;

	/**
	 * Constructeur: crée une fenêtre qui inclut une instance d'image avec défilement
	 */
	//Caroline Houle
	public FenetreInstructions() {
		gest = new GestionnaireFichiers() ;
		lesComposants = new ArrayList<Component>() ;
		
		setTitle("Instructions");
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, 886, 808);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// creation du composant qui contiendra les pages d'aide
		PanelImagesAvecDefilement panAide = new PanelImagesAvecDefilement();
		//Pour modifier la largeur et la couleur du cadre autour des pages 
		panAide.setLargeurCadre(10);
		panAide.setFichiersImages(tableauImages); // on precise quelles images seront utilisees
		panAide.setBounds(49, 57, 772, 637);
		contentPane.add(panAide);
		enregistrerUnComposant(panAide) ;
		
		btnPagePrecedente = new JButton("Page pr\u00E9c\u00E9dente");
		btnPagePrecedente.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnPagePrecedente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPagePrecedente.setEnabled( panAide.precedente() );
				btnPageSuivante.setEnabled(true);
			}
		});
		btnPagePrecedente.setBounds(49, 705, 165, 45);
		contentPane.add(btnPagePrecedente);
		enregistrerUnComposant(btnPagePrecedente) ;
		
		btnPageSuivante = new JButton("Page suivante");
		btnPageSuivante.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnPageSuivante.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnPageSuivante.setEnabled( panAide.suivante() );
				btnPagePrecedente.setEnabled(true);
			}
		
		});
		if (tableauImages.length==1 ) {
			btnPagePrecedente.setEnabled(false);
			btnPageSuivante.setEnabled(false);
		}
		btnPageSuivante.setBounds(656, 705, 165, 45);
		contentPane.add(btnPageSuivante);
		enregistrerUnComposant(btnPageSuivante) ;
		
		JLabel lblAideInstructions = new JLabel("Aide : instructions d'utilisation");
		lblAideInstructions.setHorizontalAlignment(SwingConstants.CENTER);
		lblAideInstructions.setForeground(Color.WHITE);
		lblAideInstructions.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblAideInstructions.setBounds(264, 11, 342, 34);
		contentPane.add(lblAideInstructions);
		enregistrerUnComposant(lblAideInstructions) ;
		
		/*lblAttention = new JLabel("Instructions pour le sprint 2");
		lblAttention.setHorizontalAlignment(SwingConstants.CENTER);
		lblAttention.setForeground(Color.RED);
		lblAttention.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblAttention.setBounds(49, 43, 772, 34);
		contentPane.add(lblAttention);*/
		
		appliquerTheme(gest.obtenirThemeActuel()) ;
	}//fin constructeur
	
	/**
	 * Méthode servant à ajouter un composant dans la liste des composants pour la modification.
	 * @param composant : Le composant à ajouter dans la liste.
	 */
	// Par Elias Kassas
	private void enregistrerUnComposant(Component composant) {
		lesComposants.add(composant) ;
	}
	
	/**
	 * Méthode privée servant à appliquer un thème à la fenêtre d'instructions.
	 * @param theme : L'objet theme dont on veut extraire les informations.
	 */
	// Par Elias Kassas
	private void appliquerTheme(Theme theme) {
		for (Component composant : lesComposants) {
			Font policeAct = composant.getFont() ;
			if (policeAct == null)
				revalidate() ;
			else
				composant.setFont(new Font("Tahoma", policeAct.getStyle(), policeAct.getSize())) ;
			
			if (!(composant instanceof JMenuItem) && !(composant instanceof JMenu) && 
					!(composant instanceof JMenuBar)) {
				composant.setForeground(theme.getCouleurPolice()) ;

				if (!(composant instanceof JComboBox))
					composant.setBackground(theme.getCouleurSecondaire()) ;
			}

			if (composant instanceof JPanel) {
				composant.setBackground(theme.getCouleurSecondaire()) ;
				((JComponent) composant).setBorder(new LineBorder(theme.getCouleurTertiaire(), 
						1, true)) ;
			}
		}

		contentPane.setBackground(theme.getCouleurPrimaire()) ;
		revalidate() ;
		repaint() ;
	}
}//fin classe

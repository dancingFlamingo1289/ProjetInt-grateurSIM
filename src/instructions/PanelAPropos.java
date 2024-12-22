package instructions;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

/**
 * Panel qui affiche dans des onglets de l'information sur les auteurs, sur l'application et sur les 
 * sources.
 * @author Caroline Houle
 * @author Aimé Melançon
 * @author Elias Kassas
 */
public class PanelAPropos extends JPanel {
	/**Numéro d'identifiant de classe **/
	private static final long serialVersionUID = -3110011146750233775L;
	private JTabbedPane tabOnglets ;

	/**
	 * Création de panneau 
	 */
	//Aimé Melançon
	public PanelAPropos() {
		//noter: aucun layout précisé: le conteneur à onglets prendra la largeur de la plus longue ligne de texte
		tabOnglets = new JTabbedPane(JTabbedPane.TOP);
		tabOnglets.setBounds(0, 0, 500, 250);
		add(tabOnglets);

		JPanel pnlAuteurs = new JPanel();
		tabOnglets.addTab("Auteurs", null, pnlAuteurs, null);

		JLabel lblAuteurs = new JLabel( "<html>" +
				"Équipe 36 " + 
				"<br>FrissonBoum : Le frisson de la victoire !" +
				"<br>"+
				"<br>Félix Lefrançois" + 
				"<br>Aimé Melançon" +
				"<br>Élias Kassas" + 
				"<br><br>Cours 420-SCD" +
				"<br>Intégration des apprentissages en SIM" +
				"<br>Hiver 2024</html>");
		lblAuteurs.setVerticalAlignment(SwingConstants.TOP);
		pnlAuteurs.add(lblAuteurs);

		ajouterPanelInfo() ;
		
		JPanel pnlSources = new JPanel();
		tabOnglets.addTab("Sources", null, pnlSources, null);

		JLabel lblSources = new JLabel( "<html>" + 
				"<br>Aucune Image pour le moment" +
				"<br>" + 
				"<br>" +
				"<br>" + 
				"<br></html>");
		lblSources.setVerticalAlignment(SwingConstants.TOP);
		pnlSources.add(lblSources);
	}

	/**
	 * Méthode privée servant à rajouter dans la zone à onglets l'onglet contenant un court texte
	 * expliquant et vantant le but l'application.
	 */
	// Par Elias Kassas
	private void ajouterPanelInfo() {
		JPanel panInfo = new JPanel() ;
		tabOnglets.addTab("Information", null, panInfo, null) ;
		
		JTextArea txtaInfo = new JTextArea("Bienvenue dans l'univers magnétisant de FrissonBoum !\n"
				+ "Plongez dans une aventure passionnante où le jeu de flipper classique rencontre "
				+ "la puissance de la physique moderne.\nAvec FrissonBoum, préparez-vous à une "
				+ "expérience qui fera vibrer vos sens et stimulera votre esprit !\n"
				+ "Domptez la gravité et les obstacles, défiez les lois de la physique et lancez-vous"
				+ " dans une course frénétique pour atteindre des scores épiques !\n"
				+ "Plongez dans des environnements graphiques spectaculaires et vivez des sensations "
				+ "fortes grâce à des animations fluides et réalistes.\n"
				+ "Explorez les concepts de la physique d'une manière totalement nouvelle ! "
				+ "Jouez avec la gravité, l'énergie cinétique et bien plus encore, tout\n"
				+ "en vous amusant comme jamais auparavant.\n" 
				+ "Modifiez les paramètres du jeu selon vos préférences et découvrez des combinaisons"
				+ " infinies pour une expérience de jeu unique à chaque partie.\n"
				+ "Relevez le défi, atteignez des sommets et éveillez votre esprit à la beauté de la "
				+ "physique avec FrissonBoum dès aujourd'hui !") ;
		txtaInfo.setEditable(false) ;
		panInfo.add(txtaInfo);
	}
}

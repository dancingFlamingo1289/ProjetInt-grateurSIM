package instructions;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;

/**
 * Exemple de fenetre qui cree un JPanel dans lequel sera affichees (et bien ajustees) une suite d'images representant du texte
 * continu. Les boutons places sur cette fenetre permettent de passer a l'image precedente/suicante.
 * 
 * @author Caroline Houle
 *
 */
public class FenetreInstructions extends JFrame {
	/**Numéro d'identifiant de classe **/
	private static final long serialVersionUID = 1L;
	/**Le panneau qui contient les instructions. **/
	private JPanel contentPane;
	/**Le bouton servant pour aller voir l'instruction précédante. **/
	private JButton btnPagePrecedente;
	/**Le bouton servant pour aller voir l'instruction suivante. **/
	private JButton btnPageSuivante;
	
	/**c'est ici que l'on declare un tableau ou on enumere toutes les pages d'aide desirees**/
	private String tableauImages[] = {"aideAlpha.png"};
	/**Étiquette montrant la phase d'instruction. **/
	private JLabel lblAttention;

	/**
	 * Constructeur: cr�e une fen�tre qui inclut une instance d'image avec d�filement
	 */
	//Caroline Houle
	public FenetreInstructions() {
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
		panAide.setBounds(49, 88, 772, 606);
		contentPane.add(panAide);
		
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
		
		JLabel lblAideInstructions = new JLabel("Aide : instructions d'utilisation");
		lblAideInstructions.setHorizontalAlignment(SwingConstants.CENTER);
		lblAideInstructions.setForeground(Color.WHITE);
		lblAideInstructions.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblAideInstructions.setBounds(264, 11, 342, 34);
		contentPane.add(lblAideInstructions);
		
		lblAttention = new JLabel("---> Instructions pour le sprint 1 (remise Alpha) <---");
		lblAttention.setHorizontalAlignment(SwingConstants.CENTER);
		lblAttention.setForeground(Color.MAGENTA);
		lblAttention.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblAttention.setBounds(49, 43, 772, 34);
		contentPane.add(lblAttention);
		
	
	}//fin constructeur
}//fin classe

package scene.pagesObstacles;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class TestPages extends JFrame {
	/** Identifiant de classe**/
	private static final long serialVersionUID = 1L;
	/** Le panneau contenant les pages**/
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestPages frame = new TestPages();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TestPages() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 981, 619);
		getContentPane().setLayout(null);
		
		PageMur pageMur = new PageMur();
		pageMur.setBounds(6, 6, 438, 579);
		getContentPane().add(pageMur);
		
		PagePlaqueMagnetique pageCercle = new PagePlaqueMagnetique();
		pageCercle.setBounds(469, 10, 446, 562);
		getContentPane().add(pageCercle);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	}
}

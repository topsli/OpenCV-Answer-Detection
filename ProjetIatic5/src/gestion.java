import javax.swing.JFrame;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;


public class gestion extends JFrame implements ActionListener{



	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the frame.
	 */
	public gestion() {
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 720, 351);
		getContentPane().setLayout(null);
		getContentPane().setLayout(null);
		
		JLabel lblAnneScolaire = new JLabel("Ann\u00E9e Scolaire");
		lblAnneScolaire.setBounds(75, 32, 115, 21);
		getContentPane().add(lblAnneScolaire);
	
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}

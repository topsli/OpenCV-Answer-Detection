import javax.swing.JFrame;

public class FrameWindow{

	JFrame frame;
	JFrame frameGestionAnneeScolaire;
	

	public FrameWindow() {
		initialize();
	}
	
	private void initialize() {
		frame = new mainWindow();
		frame.setTitle("");
		
		frameGestionAnneeScolaire = new gestion();
		frameGestionAnneeScolaire.setTitle("PsyLife School");
	}

}

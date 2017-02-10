import java.awt.EventQueue;

import org.opencv.core.Core;



public class main {

	public static void main(String[] args) {
	    
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); // Load the library openCv3.0
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrameWindow window = new FrameWindow();
					window.frame.setVisible(true);
					window.frameGestionAnneeScolaire.setVisible(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


}

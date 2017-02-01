import java.awt.EventQueue;

import org.opencv.core.Core;



public class main {

	public static void main(String[] args) {
	    // Load the library
		//System.out.println(System.getProperty("java.library.path")+":"+Core.NATIVE_LIBRARY_NAME);
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME); //openCv3.0
		//new traitement();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrameWindow window = new FrameWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


}

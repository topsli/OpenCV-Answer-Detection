import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Core;

public class FrameWindow{

	JFrame frame;
	
	public FrameWindow() {
		initialize();
	}

	
	private void initialize() {
		frame = new mainWindow();
		frame.setTitle("OpenCV Project");
	}

}

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.opencv.core.Core;

import models.Exam;
import models.SchoolYear;
import models.Subject;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JTextArea;

public class mainWindow extends JFrame implements ActionListener{

	JPanel contentPane;
	dbUtils db = new dbUtils();
	List<SchoolYear> schoolYears = db.getSchoolYears();
	List<Subject> subjects = new ArrayList<>();
	List<Integer> answers = new ArrayList<>();
	JComboBox subjectComboBox = new JComboBox();
	JComboBox examComboBox = new JComboBox();
	public String imageUrl;
	public List<String> imageUrls = new ArrayList<String>();
	public List<DetectResult> detectResults = new ArrayList<DetectResult>();
	JComboBox imageSelectComboBox = new JComboBox();
	public List<Exam> exams= new ArrayList<Exam>();

	/**
	 * Launch the application.
	 */
	

	/**
	 * Create the frame.
	 */
	public mainWindow() {
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 720, 720);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSchoolYear = new JLabel("School year");
		lblSchoolYear.setBounds(13, 12, 81, 14);
		contentPane.add(lblSchoolYear);
		
		JComboBox yearComboBox = new JComboBox();
		yearComboBox.setBounds(104, 8, 186, 22);
		//System.out.println(schoolYears.size());
		for(int i=0;i<schoolYears.size();i++){
			yearComboBox.addItem(schoolYears.get(i));
		}
		subjects = db.getSubjectFromSchoolYear((SchoolYear)yearComboBox.getSelectedObjects()[0]);
		//System.out.println(subjects);
		subjectComboBox.removeAllItems();
		for(int i=0;i<subjects.size();i++){
			subjectComboBox.addItem(subjects.get(i));
		}
		yearComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				subjects = db.getSubjectFromSchoolYear((SchoolYear)yearComboBox.getSelectedObjects()[0]);
				//System.out.println(subjects);
				subjectComboBox.removeAllItems();
				for(int i=0;i<subjects.size();i++){
					subjectComboBox.addItem(subjects.get(i));
				}
				
			}
		}
		);
		contentPane.add(yearComboBox);
		
		subjectComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				examComboBox.removeAllItems();
				if(subjectComboBox.getSelectedObjects().length >0){
					exams = db.getExamFromSubject((Subject)subjectComboBox.getSelectedObjects()[0]);
					
					for(int i=0;i<exams.size();i++){
						examComboBox.addItem(exams.get(i));
					}
				}
			}
		});
		
//		JButton btnOk = new JButton("Ok");
//		btnOk.setBounds(234, 9, 59, 19);
//		btnOk.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				subjects = db.getSubjectFromSchoolYear((SchoolYear)yearComboBox.getSelectedObjects()[0]);
//				//System.out.println(subjects);
//				subjectComboBox.removeAllItems();
//				for(int i=0;i<subjects.size();i++){
//					subjectComboBox.addItem(subjects.get(i));
//				}
//			}
//		});
//		contentPane.add(btnOk);
		
		examComboBox.setBounds(377, 42, 122, 21);
		contentPane.add(examComboBox);
		
		subjectComboBox.setBounds(104, 41, 186, 23);
		contentPane.add(subjectComboBox);
		
		JLabel lblSubject = new JLabel("Subject");
		lblSubject.setBounds(13, 45, 46, 14);
		contentPane.add(lblSubject);
		
		JLabel labelImage = new JLabel("");
		labelImage.setBounds(10, 74, 410, 595);
		contentPane.add(labelImage);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(430, 108, 264, 448);
		contentPane.add(textArea);
		
		imageSelectComboBox.setBounds(430, 75, 264, 22);
		contentPane.add(imageSelectComboBox);
		imageSelectComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(imageSelectComboBox.getSelectedObjects().length > 0){
					DetectResult detect = ((DetectResult)imageSelectComboBox.getSelectedItem());
					String path = detect.getImageUrl();
					ImageIcon myImage = new ImageIcon(path);
	        		Image img = myImage.getImage();
	        		Image newImage = img.getScaledInstance(labelImage.getWidth(), labelImage.getHeight(), Image.SCALE_SMOOTH);
	        		ImageIcon finalImg = new ImageIcon(newImage);
	        		labelImage.setIcon(finalImg);
	        		
	        		String textToShow = "";
					textToShow+="Student Id is:"+detect.getStudentId()+"\n";
					answers = detect.getAnswers();
					for(int i=0;i<answers.size();i++){
						textToShow+="Question :"+(i+1)+" Answer is :"+answers.get(i)+"\n";
						
					}
					if(detect.getIsChecked() == 1){
						textToShow+="Checked : checked\n";
						textToShow+="Score :"+detect.getScore();
					}
					else{
						textToShow+="Checked : not checked";
					}
				
					textArea.setText(textToShow);
				}
			}
		}
		);
		
		JButton btnChooseFile = new JButton("Choose File");
		btnChooseFile.setBounds(555, 8, 120, 22);
		btnChooseFile.addActionListener(new ActionListener( ) {
            public void actionPerformed(ActionEvent e) {
            	JFileChooser fc = new JFileChooser();
            	fc.setMultiSelectionEnabled(true);
            	fc.setCurrentDirectory(new File("C://"));
            	FileNameExtensionFilter filter = new FileNameExtensionFilter("Image", "jpg", "png", "gif");
            	fc.addChoosableFileFilter(filter);
            	int result = fc.showSaveDialog(null);
            	if(result==JFileChooser.APPROVE_OPTION)
            	{
            		File[] selectedFiles = fc.getSelectedFiles();
            		imageSelectComboBox.removeAllItems();
            		detectResults = new ArrayList<DetectResult>();
            		for(int i=0;i<selectedFiles.length;i++){
            			String path = selectedFiles[i].getAbsolutePath();
            			System.out.println(path);
            			imageUrls.add(path);
            			DetectResult detect = new DetectResult();
            			detect.setImageUrl(path);
            			detect.Processing();
            			detectResults.add(detect);
            			imageSelectComboBox.addItem(detect);
//                		imageUrl = path;
//                		ImageIcon myImage = new ImageIcon(path);
//                		Image img = myImage.getImage();
//                		Image newImage = img.getScaledInstance(labelImage.getWidth(), labelImage.getHeight(), Image.SCALE_SMOOTH);
//                		ImageIcon finalImg = new ImageIcon(newImage);
//                		labelImage.setIcon(finalImg);
            		}
            		   		
            	}
            	else
            	{
            		
            	}
            	
            }
        });
		contentPane.add(btnChooseFile);
		
		
		
		JLabel lblNewLabel = new JLabel("exam");
		lblNewLabel.setBounds(310, 45, 46, 14);
		contentPane.add(lblNewLabel);
		
		JButton btnSaveScore = new JButton("Process and Save");
		btnSaveScore.setBounds(466, 567, 197, 31);
		contentPane.add(btnSaveScore);
		btnSaveScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DetectResult resultToSave = detectResults.get(imageSelectComboBox.getSelectedIndex());//(DetectResult)imageSelectComboBox.getSelectedItem();
				
				if(resultToSave.getIsChecked() == 0){//resultToSave.getIsChecked() == 0
					if(examComboBox.getSelectedIndex()== -1){
						JOptionPane.showMessageDialog(null, "Please Select Exam");
						return;
					}
					db.saveScore(resultToSave, ((Exam)examComboBox.getSelectedItem()).getExamId(), resultToSave.getScore());
					resultToSave.writeScore();
					String path = resultToSave.getImageUrl();
					ImageIcon myImage = new ImageIcon(path);
	        		Image img = myImage.getImage();
	        		Image newImage = img.getScaledInstance(labelImage.getWidth(), labelImage.getHeight(), Image.SCALE_SMOOTH);
	        		ImageIcon finalImg = new ImageIcon(newImage);
	        		labelImage.setIcon(finalImg);
	        		
	        		String textToShow = "";
					textToShow+="Student Id is:"+resultToSave.getStudentId()+"\n";
					answers = resultToSave.getAnswers();
					for(int i=0;i<answers.size();i++){
						textToShow+="Question :"+(i+1)+" Answer is :"+answers.get(i)+"\n";
						
					}
					if(resultToSave.getIsChecked() == 1){
						textToShow+="Checked : checked\n";
						textToShow+="Score :"+resultToSave.getScore();
					}
					else{
						textToShow+="Checked : not checked";
					}
				
					textArea.setText(textToShow);
					
//					imageSelectComboBox.removeAllItems();
//            		for(int i=0;i<detectResults.size();i++){
//            			imageSelectComboBox.addItem(detectResults.get(i));
//            		}
            		imageSelectComboBox.setSelectedItem(resultToSave);
            		
            		
					
				}
				else{
					JOptionPane.showMessageDialog(null, "It's Already checked");
				}
			}
		});
		
		
		JButton btnProcessAll = new JButton("Process and Save All");
		btnProcessAll.setBounds(466, 610, 197, 31);
		contentPane.add(btnProcessAll);
		btnProcessAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for(int d=0; d< detectResults.size(); d++){
				DetectResult resultToSave = detectResults.get(d);//(DetectResult)imageSelectComboBox.getSelectedItem();
				
				if(resultToSave.getIsChecked() == 0){//resultToSave.getIsChecked() == 0
					if(examComboBox.getSelectedIndex()== -1){
						JOptionPane.showMessageDialog(null, "Please Select Exam");
						return;
					}
					db.saveScore(resultToSave, ((Exam)examComboBox.getSelectedItem()).getExamId(), resultToSave.getScore());
					resultToSave.writeScore();
					String path = resultToSave.getImageUrl();
					ImageIcon myImage = new ImageIcon(path);
	        		Image img = myImage.getImage();
	        		Image newImage = img.getScaledInstance(labelImage.getWidth(), labelImage.getHeight(), Image.SCALE_SMOOTH);
	        		ImageIcon finalImg = new ImageIcon(newImage);
	        		labelImage.setIcon(finalImg);
	        		
	        		String textToShow = "";
					textToShow+="Student Id is:"+resultToSave.getStudentId()+"\n";
					answers = resultToSave.getAnswers();
					for(int i=0;i<answers.size();i++){
						textToShow+="Question :"+(i+1)+" Answer is :"+answers.get(i)+"\n";
						
					}
					if(resultToSave.getIsChecked() == 1){
						textToShow+="Checked : checked\n";
						textToShow+="Score :"+resultToSave.getScore();
					}
					else{
						textToShow+="Checked : not checked";
					}
				
					textArea.setText(textToShow);
					
//					imageSelectComboBox.removeAllItems();
//            		for(int i=0;i<detectResults.size();i++){
//            			imageSelectComboBox.addItem(detectResults.get(i));
//            		}
            		imageSelectComboBox.setSelectedItem(resultToSave);
            		
            		
					
				}
				else{
					
				}
			}
			}
		});
		
//		JButton btnProcess = new JButton("Process");
//		btnProcess.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent arg0) {
//				DetectResult detect = new DetectResult(imageUrl);
//				detect.Processing();
//				String textToShow = "";
//				textToShow+="Student Id is:"+detect.getStudentId()+"\n";
//				answers = detect.getAnswers();
//				for(int i=0;i<answers.size();i++){
//					textToShow+="Question :"+(i+1)+" Answer is :"+answers.get(i)+"\n";
//					
//				}
//				textArea.setText(textToShow);
//			}
//		});
//		btnProcess.setBounds(574, 36, 89, 23);
//		contentPane.add(btnProcess);
		
		
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}

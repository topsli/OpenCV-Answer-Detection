import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;


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
	 * Create the frame.
	 */
	public mainWindow() {
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 720, 689);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSchoolYear = new JLabel("Ann\u00E9e scolaire");
		lblSchoolYear.setBounds(10, 15, 92, 14);
		contentPane.add(lblSchoolYear);
		
		JComboBox yearComboBox = new JComboBox();
		yearComboBox.setBounds(107, 11, 70, 22);
		for(int i=0;i<schoolYears.size();i++){
			yearComboBox.addItem(schoolYears.get(i));
		}
		subjects = db.getSubjectFromSchoolYear((SchoolYear)yearComboBox.getSelectedObjects()[0]);
		subjectComboBox.removeAllItems();
		for(int i=0;i<subjects.size();i++){
			subjectComboBox.addItem(subjects.get(i));
		}
		yearComboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				subjects = db.getSubjectFromSchoolYear((SchoolYear)yearComboBox.getSelectedObjects()[0]);
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
		
		examComboBox.setBounds(466, 12, 92, 21);
		contentPane.add(examComboBox);
		
		subjectComboBox.setBounds(269, 11, 103, 23);
		contentPane.add(subjectComboBox);
		
		JLabel lblSubject = new JLabel("Mati\u00E8re");
		lblSubject.setBounds(213, 15, 46, 14);
		contentPane.add(lblSubject);
		
		JLabel labelImage = new JLabel("");
		labelImage.setBounds(10, 57, 410, 612);
		contentPane.add(labelImage);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(430, 90, 264, 405);
		contentPane.add(textArea);
		
		imageSelectComboBox.setBounds(430, 57, 264, 22);
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
					textToShow+="Numéro d'étudiant :"+detect.getStudentId()+"\n"; //here it's me i modify but he works before that
					answers = detect.getAnswers();
					for(int i=0;i<answers.size();i++){
						String reponse;
						if (answers.get(i)==1) {
							reponse = "Non aquis"; //Not good marks
						}
						else if (answers.get(i)==2) {
							reponse = "Aquis"; // good marks
						}
						else{
							reponse = "Bien aquis"; // very good marks
						}
						
						textToShow+="Question"+(i+1)+" ==> "+reponse+"\n";
						
					}
					if(detect.getIsChecked() == 1){
						textToShow+="Feuille : remplie !\n";
						textToShow+="Note :"+detect.getScore();
					}
					else{
						textToShow+="Feuille : non remplie !";
					}
				
					textArea.setText(textToShow);
				}
			}
		}
		);
		
		JButton btnChooseFile = new JButton("Parcourir");
		btnChooseFile.setBounds(602, 11, 92, 22);
		btnChooseFile.addActionListener(new ActionListener( ) {
            public void actionPerformed(ActionEvent e) {
            	JFileChooser fc = new JFileChooser();
            	fc.setMultiSelectionEnabled(true);
            	fc.setCurrentDirectory(new File("C:/Users/Benreghai/Desktop/Inputs"));
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
            			imageUrls.add(path);
            			DetectResult detect = new DetectResult();
            			detect.setImageUrl(path);
            			detect.Processing();
            			detectResults.add(detect);
            			imageSelectComboBox.addItem(detect);
            		}   		
            	}
            	else
            	{
            		
            	}
            	
            }
        });
		contentPane.add(btnChooseFile);
		
		
		
		JLabel lblNewLabel = new JLabel("Examen");
		lblNewLabel.setBounds(406, 15, 46, 14);
		contentPane.add(lblNewLabel);
		
		JButton btnSaveScore = new JButton("Traiter et sauvegarder");
		btnSaveScore.setBounds(460, 567, 218, 31);
		contentPane.add(btnSaveScore);
		btnSaveScore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DetectResult resultToSave = detectResults.get(imageSelectComboBox.getSelectedIndex());//(DetectResult)imageSelectComboBox.getSelectedItem();
				
				if(resultToSave.getIsChecked() == 0){//resultToSave.getIsChecked() == 0
					if(examComboBox.getSelectedIndex()== -1){
						JOptionPane.showMessageDialog(null, "Faut d'abord selectionner un examen !");
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
            		imageSelectComboBox.setSelectedItem(resultToSave);	
				}
				else{
					JOptionPane.showMessageDialog(null, "Feuille dèjà traitée !");
				}
			}
		});
		
		
		JButton btnProcessAll = new JButton("Tous traiter et sauvegarder");
		btnProcessAll.setBounds(460, 609, 218, 31);
		contentPane.add(btnProcessAll);
		
		
		JButton btnGestionDesAnnes = new JButton("Gestion des ann\u00E9es scolaires");
		btnGestionDesAnnes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FrameWindow fw = new FrameWindow();
				fw.frame.setVisible(false);
				fw.frameGestionAnneeScolaire.setVisible(true);
			}
		});
		btnGestionDesAnnes.setBounds(460, 525, 218, 31);
		contentPane.add(btnGestionDesAnnes);
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

            		imageSelectComboBox.setSelectedItem(resultToSave);
            		
            		
					
				}
				else{
					
				}
			}
			}
		});
			
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}

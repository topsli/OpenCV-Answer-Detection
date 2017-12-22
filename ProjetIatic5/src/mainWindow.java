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
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;

public class mainWindow extends JFrame implements ActionListener{

	JPanel contentPane;
	dbUtils db = new dbUtils();
	List<SchoolYear> schoolYears = db.getSchoolYears();
	List<Subject> subjects = new ArrayList<>();
	List<String> answers = new ArrayList<>();
	JComboBox yearComboBox = new JComboBox();
	JComboBox subjectComboBox = new JComboBox();
	JComboBox examComboBox = new JComboBox();
	public String imageUrl;
	public List<String> imageUrls = new ArrayList<String>();
	public List<DetectResult> detectResults = new ArrayList<DetectResult>();
	JComboBox imageSelectComboBox = new JComboBox();
	public List<Exam> exams= new ArrayList<Exam>();
	List<Integer> questionFirstNumber = new ArrayList<Integer>();

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

//		JLabel lblSchoolYear = new JLabel("Year");
//		lblSchoolYear.setBounds(10, 15, 92, 14);
//		contentPane.add(lblSchoolYear);
//
//		yearComboBox.setBounds(107, 11, 70, 22);
//		for(int i=0;i<schoolYears.size();i++){
//			yearComboBox.addItem(schoolYears.get(i));
//		}
//		subjects = db.getSubjectFromSchoolYear((SchoolYear)yearComboBox.getSelectedObjects()[0]);
//		subjectComboBox.removeAllItems();
//		for(int i=0;i<subjects.size();i++){
//			subjectComboBox.addItem(subjects.get(i));
//		}
//		yearComboBox.addActionListener(new ActionListener() {
//										   public void actionPerformed(ActionEvent e) {
//											   subjects = db.getSubjectFromSchoolYear((SchoolYear)yearComboBox.getSelectedObjects()[0]);
//											   subjectComboBox.removeAllItems();
//											   for(int i=0;i<subjects.size();i++){
//												   subjectComboBox.addItem(subjects.get(i));
//											   }
//
//										   }
//									   }
//		);
//		contentPane.add(yearComboBox);
//
//		subjectComboBox.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				examComboBox.removeAllItems();
//				if(subjectComboBox.getSelectedObjects().length >0){
//					exams = db.getExamFromSubject((Subject)subjectComboBox.getSelectedObjects()[0]);
//					for(int i=0;i<exams.size();i++){
//						examComboBox.addItem(exams.get(i));
//					}
//				}
//			}
//		});
//
//		examComboBox.setBounds(466, 12, 92, 21);
//		contentPane.add(examComboBox);
//
//		subjectComboBox.setBounds(269, 11, 103, 23);
//		contentPane.add(subjectComboBox);
//
//		JLabel lblSubject = new JLabel("Subject");
//		lblSubject.setBounds(213, 15, 46, 14);
//		contentPane.add(lblSubject);

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
														  textToShow+="StudentId :"+detect.getStudentId()+"\n"; //here it's me i modify but he works before that
														  answers = detect.getAnswers();
														  for(int i=0;i<answers.size();i++){

															  //textToShow+="Question"+(i+1)+" ==> "+reponse+"\n";
															  textToShow+="Question"+(questionFirstNumber.get(imageSelectComboBox.getSelectedIndex())+i)+" ==> "+answers.get(i)+"\n";


														  }
														  textToShow+="Note :"+detect.getScore();
//					if(detect.getIsChecked() == 1){
//						textToShow+="Feuille : remplie !\n";
//						textToShow+="Note :"+detect.getScore();
//					}
//					else{
//						textToShow+="Feuille : non remplie !";
//					}

														  textArea.setText(textToShow);
													  }
												  }
											  }
		);

		JButton btnChooseFile = new JButton("Choose");
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
					int questionNumber = 1;
					for(int i=0;i<selectedFiles.length;i++){
						String path = selectedFiles[i].getAbsolutePath();
						imageUrls.add(path);
						DetectResult detect = new DetectResult();
						detect.setImageUrl(path);
						detect.Processing();
						detectResults.add(detect);
						questionFirstNumber.add(questionNumber);
						imageSelectComboBox.addItem(detect);
						questionNumber += detect.getAnswers().size();
					}
				}
				else
				{

				}

			}
		});
		contentPane.add(btnChooseFile);



//		JLabel lblNewLabel = new JLabel("Review");
//		lblNewLabel.setBounds(406, 15, 46, 14);
//		contentPane.add(lblNewLabel);
//
//		JButton btnSaveScore = new JButton("Save and Process it");
//		btnSaveScore.setBounds(460, 567, 218, 31);
//		contentPane.add(btnSaveScore);
//		btnSaveScore.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				DetectResult resultToSave = detectResults.get(imageSelectComboBox.getSelectedIndex());//(DetectResult)imageSelectComboBox.getSelectedItem();
//
//				if(resultToSave.getIsChecked() == 0){//resultToSave.getIsChecked() == 0
//					if(examComboBox.getSelectedIndex()== -1){
//						JOptionPane.showMessageDialog(null, "You need choose the exam first!");
//						return;
//					}
//					try {
//						db.saveScore(resultToSave.getStudentId(), ((Exam)examComboBox.getSelectedItem()).getExamId(), resultToSave.getScore());
//					} catch (SQLException e1) {
//						// TODO Auto-generated catch block
//						JOptionPane.showMessageDialog(null, "Student Id"+resultToSave.getStudentId()+" is already add:");
//
//						e1.printStackTrace();
//					}
//					resultToSave.writeScore();
//					String path = resultToSave.getImageUrl();
//					ImageIcon myImage = new ImageIcon(path);
//					Image img = myImage.getImage();
//					Image newImage = img.getScaledInstance(labelImage.getWidth(), labelImage.getHeight(), Image.SCALE_SMOOTH);
//					ImageIcon finalImg = new ImageIcon(newImage);
//					labelImage.setIcon(finalImg);
//
//					String textToShow = "";
//					textToShow+="Student Id is:"+resultToSave.getStudentId()+"\n";
////					if(resultToSave.getStudentId().equals("Student Id Not Found")){
////						JOptionPane.showMessageDialog(null, "Student Id Not Found");
////					}
//					answers = resultToSave.getAnswers();
//					for(int i=0;i<answers.size();i++){
//						textToShow+="Question :"+(i+1)+" Answer is :"+answers.get(i)+"\n";
//
//					}
//					if(resultToSave.getIsChecked() == 1){
////						textToShow+="Checked : checked\n";
//						textToShow+="Score :"+resultToSave.getScore();
//					}
//					else{
////						textToShow+="Checked : not checked";
//					}
//
//					textArea.setText(textToShow);
//					imageSelectComboBox.setSelectedItem(resultToSave);
//				}
//				else{
//					JOptionPane.showMessageDialog(null, "Failed !");
//				}
//			}
//		});


//		JButton btnProcessAll = new JButton("Save data");
//		btnProcessAll.setBounds(460, 609, 218, 31);
//		contentPane.add(btnProcessAll);
//
//
//		JButton btnGestionDesAnnes = new JButton("PsyLife School Manage");
//		btnGestionDesAnnes.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				FrameWindow fw = new FrameWindow();
//				fw.frame.setVisible(false);
//				fw.frameGestionAnneeScolaire.setVisible(true);
//			}
//		});
//		btnGestionDesAnnes.setBounds(460, 525, 218, 31);
//		contentPane.add(btnGestionDesAnnes);
//		btnProcessAll.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				String textToShow = "";
//				int scoreToSave = 0;
//				String studentIdToSave = "";
//				for(int d=0; d< detectResults.size(); d++){
//					DetectResult resultToSave = detectResults.get(d);//(DetectResult)imageSelectComboBox.getSelectedItem();
//
//					if(resultToSave.getIsChecked() == 0){//resultToSave.getIsChecked() == 0
//						if(examComboBox.getSelectedIndex()== -1){
//							JOptionPane.showMessageDialog(null, "Please Select Exam");
//							return;
//						}
////						db.saveScore(resultToSave, ((Exam)examComboBox.getSelectedItem()).getExamId(), resultToSave.getScore());
//						scoreToSave+=resultToSave.getScore();
//						resultToSave.writeScore();
//						//textToShow+=resultToSave.imageUrl+" was save to "+studentIdToSave+" with score"+resultToSave.getScore();
//						if(resultToSave.getStudentId().equals("Student Id Not Found") == false){
//							studentIdToSave = resultToSave.getStudentId();
//						}
////						String path = resultToSave.getImageUrl();
////						ImageIcon myImage = new ImageIcon(path);
////		        		Image img = myImage.getImage();
////		        		Image newImage = img.getScaledInstance(labelImage.getWidth(), labelImage.getHeight(), Image.SCALE_SMOOTH);
////		        		ImageIcon finalImg = new ImageIcon(newImage);
////		        		labelImage.setIcon(finalImg);
//
//
//						//					textToShow+="Student Id is:"+resultToSave.getStudentId()+"\n";
//						//					answers = resultToSave.getAnswers();
//						//					for(int i=0;i<answers.size();i++){
//						//						if(answers.get(i) == 0){
//						//							textToShow+="Question :"+(i+1)+" Doesn't Checked\n";
//						//							JOptionPane.showMessageDialog(null, "Question :"+(i+1)+" Doesn't Checked");
//						//						}
//						//						textToShow+="Question :"+(i+1)+" Answer is :"+answers.get(i)+"\n";
//						//
//						//					}
//						//					if(resultToSave.getIsChecked() == 1){
//						//						textToShow+="Checked : checked\n";
//						//						textToShow+="Score :"+resultToSave.getScore();
//						//					}
//						//					else{
//						//						textToShow+="Checked : not checked";
//						//					}
//
//
//
////	            		imageSelectComboBox.setSelectedItem(resultToSave);
//
//
//
//					}
//					else{
//
//					}
//				}
//				if(studentIdToSave.equals("")){
//					JOptionPane.showMessageDialog(null, "Student Id NotFound");
//				}
//				else{
//					textToShow+="score :"+scoreToSave+"\n";
//					textToShow+="student id:"+studentIdToSave+"\n";
//					textToShow+="Year:"+((SchoolYear)yearComboBox.getSelectedItem()).getSchoolYear()+"\n";
//					textToShow+="Subject:"+((Subject)subjectComboBox.getSelectedItem()).getSubject()+"\n";
//
//					textToShow+="Exam:"+((Exam)examComboBox.getSelectedItem()).getExamName()+"\n";
//
//					try {
//						db.saveScore(studentIdToSave, ((Exam)examComboBox.getSelectedItem()).getExamId(), scoreToSave);
//					} catch (SQLException e1) {
//						// TODO Auto-generated catch block
//						JOptionPane.showMessageDialog(null, "Student Id"+studentIdToSave+" is already add:");
//						e1.printStackTrace();
//
//					}
//
//					textArea.setText(textToShow);
//				}
//
//
//			}
//		});

	}

	@Override
	public void actionPerformed(ActionEvent e) {

	}
}

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import models.Exam;
import models.SchoolYear;
import models.Subject;

public class dbUtils {
	private Connection connection;
	private Statement statement;

	dbUtils(){
		String url = "jdbc:mysql://localhost:3306/opencvdb";
		String username = "root";
		String password = "root";

		//System.out.println("Connection to the database ...");

		try  {
			Class.forName("com.mysql.jdbc.Driver");
			connection = (Connection) DriverManager.getConnection(url, username, password);
			//System.out.println("Connection the database successfully !");
		} catch (SQLException e) {
			throw new IllegalStateException("Connexion exception !", e);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	List<SchoolYear> getSchoolYears(){
		try {
			statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from schoolyear");
			List<SchoolYear> schoolYears = new ArrayList<SchoolYear>();
			while (resultSet.next()) {
				SchoolYear schoolYear = new SchoolYear();
				schoolYear.setIdSchoolYear(resultSet.getInt(1));
				schoolYear.setSchoolYear(Integer.toString(resultSet.getInt(2)));
				schoolYears.add(schoolYear);
			}
			return schoolYears;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	List<Subject> getSubjectFromSchoolYear(SchoolYear schoolyear){
		try {
			statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from subject where IdSchoolYear ='"+schoolyear.getIdSchoolYear()+"'");
			List<Subject> subjects = new ArrayList<Subject>();
			while (resultSet.next()) {
				Subject subject = new Subject();
				subject.setIdSubject(resultSet.getInt(1));
				subject.setSubject(resultSet.getString(2));
				subject.setSchoolYear(schoolyear);
				subjects.add(subject);
			}
			return subjects;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	List<Exam> getExamFromSubject(Subject subject){
		try {
			statement = (Statement) connection.createStatement();
			ResultSet resultSet = statement.executeQuery("select * from exam where subjectid ='"+subject.getIdSubject()+"'");
			List<Exam> exams = new ArrayList<Exam>();
			while (resultSet.next()) {
				Exam exam = new Exam();
				exam.setExamId(resultSet.getInt(1));
				exam.setExamName(resultSet.getString(2));
				exam.setSubject(subject);
				exams.add(exam);
			}
			return exams;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void saveScore(String studentId,int examId,int score) throws SQLException{
		try {
			statement = (Statement) connection.createStatement();
			statement.executeUpdate("INSERT INTO studentscore(studentid,score,examid) VALUES ('"+studentId+"',"+score+","+examId+");");//ON DUPLICATE KEY UPDATE score = score + "+score+";");
		}catch (SQLException e) {
			throw e;
			//e.printStackTrace();
		}
	}

	void CloseDb(){
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}

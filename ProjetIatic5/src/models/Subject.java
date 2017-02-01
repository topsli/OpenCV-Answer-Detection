package models;

public class Subject {
	int IdSubject;
	String subject;
	SchoolYear schoolYear;
	public Subject() {
		
	}
	
	public Subject(int idSubject, String subject, SchoolYear schoolYear) {
		super();
		IdSubject = idSubject;
		this.subject = subject;
		this.schoolYear = schoolYear;
	}
	public int getIdSubject() {
		return IdSubject;
	}
	public void setIdSubject(int idSubject) {
		IdSubject = idSubject;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public SchoolYear getSchoolYear() {
		return schoolYear;
	}
	public void setSchoolYear(SchoolYear schoolYear) {
		this.schoolYear = schoolYear;
	}
	
	public String toString(){
		return subject;
	}
}

package models;

public class SchoolYear {
	int IdSchoolYear;
	String SchoolYear;
	
	public SchoolYear() {
		super();
	}
	
	public SchoolYear(int idSchoolYear, String schoolYear) {
		super();
		IdSchoolYear = idSchoolYear;
		SchoolYear = schoolYear;
	}

	public int getIdSchoolYear() {
		return IdSchoolYear;
	}

	public void setIdSchoolYear(int idSchoolYear) {
		IdSchoolYear = idSchoolYear;
	}

	public String getSchoolYear() {
		return SchoolYear;
	}

	public void setSchoolYear(String schoolYear) {
		SchoolYear = schoolYear;
	}
	
	public String toString(){
		return SchoolYear;
	}
	
}

//esof 3050
// grasley / fiorot / grandell

public class Registration {
	private String courseID;
	private String studentID;
	private int grade;

	public Registration(String courseID, String studentID, int grade) {
		this.courseID = courseID;
		this.studentID = studentID;
		this.grade = grade;
	}
	
	public String getCourseID() {
		return courseID;
	}
	public String getStudentID() {
		return studentID;
	}
	public int getGrade() {
		return grade;
	}
	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}
	public void setGrade(int grade) {
		this.grade = grade;
	}
}
//esof 3050
// grasley / fiorot / grandell

public final class Course {
	private String title;
	private String subject;
	private int courseNum;
	private int sectionNum;
	private String course_id;
	private int maximumEnrollment;
	private int currentEnrollment;
	private int yearLevel;
	private String description;
	private String calendarEntry;
	private boolean enrolled;
	
	//override constructor
	public Course(String identifier, String title, int currentEnrollment, 
			int maximumEnrollment,int yearLevel, String description, 
			String calendarEntry) {
		this.course_id = identifier;
		this.title = title;
		this.currentEnrollment = currentEnrollment;
		this.maximumEnrollment = maximumEnrollment;
		this.yearLevel = yearLevel;
		this.description = description;
		this.calendarEntry = calendarEntry;
	}
	
	//override constructor
	public Course(String identifier, String title, int currentEnrollment, 
			int maximumEnrollment,int yearLevel, String description, 
			String calendarEntry, boolean enrollment) {
		this.course_id = identifier;
		this.title = title;
		this.currentEnrollment = currentEnrollment;
		this.maximumEnrollment = maximumEnrollment;
		this.yearLevel = yearLevel;
		this.description = description;
		this.calendarEntry = calendarEntry;
		this.enrolled = enrollment;
	}
	
	//regular constructor
	public Course(String title, String subject, int courseNum, int sectionNum, String identifier, int maximumEnrollment,
			int currentEnrollment, int yearLevel, String description, String calendarEntry) {
		this.title = title;
		this.subject = subject;
		this.courseNum = courseNum;
		this.sectionNum = sectionNum;
		this.course_id = identifier;
		this.maximumEnrollment = maximumEnrollment;
		this.currentEnrollment = currentEnrollment;
		this.yearLevel = yearLevel;
		this.description = description;
		this.calendarEntry = calendarEntry;
	}

	public String getTitle() {
		return title;
	}

	public String getSubject() {
		return subject;
	}

	public int getCourseNum() {
		return courseNum;
	}

	public int getSectionNum() {
		return sectionNum;
	}

	public String getCourse_id() {
		return course_id;
	}

	public int getMaximumEnrollment() {
		return maximumEnrollment;
	}

	public int getCurrentEnrollment() {
		return currentEnrollment;
	}

	public int getYearLevel() {
		return yearLevel;
	}

	public String getDescription() {
		return description;
	}

	public String getCalendarEntry() {
		return calendarEntry;
	}

	public boolean isEnrolled() {
		return enrolled;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public void setCourseNum(int courseNum) {
		this.courseNum = courseNum;
	}

	public void setSectionNum(int sectionNum) {
		this.sectionNum = sectionNum;
	}

	public void setCourse_id(String course_id) {
		this.course_id = course_id;
	}

	public void setMaximumEnrollment(int maximumEnrollment) {
		this.maximumEnrollment = maximumEnrollment;
	}

	public void setCurrentEnrollment(int currentEnrollment) {
		this.currentEnrollment = currentEnrollment;
	}

	public void setYearLevel(int yearLevel) {
		this.yearLevel = yearLevel;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setCalendarEntry(String calendarEntry) {
		this.calendarEntry = calendarEntry;
	}

	public void setEnrolled(boolean enrolled) {
		this.enrolled = enrolled;
	}
}

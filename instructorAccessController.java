//esof 3050
// grasley / fiorot / grandell

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
@SuppressWarnings("unused")

public class instructorAccessController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    
    @FXML
    private Label facultyIDLabel;

    @FXML
    private Button switchButton;
    
    @FXML
    private Button logoutButton;

    @FXML
    private ComboBox<String> searchComboBox;

    @FXML
    private TextField keywordField;

    @FXML
    private Label avgLabel;

    @FXML
    private TextField avgTextField;

    @FXML
    private TableView<Course> courseDisplayTable;

    @FXML
    private TableColumn<Course, String> courseIdColumn;

    @FXML
    private TableColumn<Course, String> titleColumn;

    @FXML
    private TableColumn<Course, String> currentSizeColumn;

    @FXML
    private TableColumn<Course, String> maxSizeColumn;

    @FXML
    private TableColumn<Course, String> yearColumn;

    @FXML
    private TableColumn<Course, String> descColumn;

    @FXML
    private TableColumn<Course, String> calendarColumn;

    @FXML
    private TableView<Registration> marksDisplayTable;

    @FXML
    private TableColumn<Registration, String> marksIDColumn;

    @FXML
    private TableColumn<Registration, String> marksStudentColumn;

    @FXML
    private TableColumn<Registration, String> marksGradeColumn;

    @FXML
    private Button manageCourseButton;

    @FXML
    private TextField courseidField;

    @FXML
    private TextField descField;

    @FXML
    private TextField calendarField;

    @FXML
    private Button submitButton;

    @FXML
    private Button searchCourseButton;

    @FXML
    private Button manageMarkButton;

    @FXML
    void logout(MouseEvent event) {
    	//send user to main page
    	Main.setPage(0);
    	Main.pages.remove(1);
    }

    @FXML
    void manageCourse(MouseEvent event) {
    	submitButton.setVisible(true);
    	
    	courseidField.setVisible(true);
    	courseidField.setPromptText("Course ID");
    	descField.setVisible(true);
    	descField.setPromptText("Description");
    	calendarField.setVisible(true);
    	calendarField.setPromptText("Schedule");
    }
    
    @FXML
    void manageMark(MouseEvent event) {
    	submitButton.setVisible(true);
    	
    	courseidField.setVisible(true);
    	courseidField.setPromptText("Course ID");
    	descField.setVisible(true);
    	descField.setPromptText("Student ID");
    	calendarField.setVisible(true);
    	calendarField.setPromptText("New Grade");
    }

    @FXML
    void searchCourse(MouseEvent event) throws SQLException {
    	clearTable();
    	Connection newConnection = ServerConnection.openConnection();
    	
    	if (courseDisplayTable.isVisible()) {
        	//check to see if there is a keyword, otherwise run the selection box
    		if (!keywordField.getText().equals("")) {
    			//temp variable
    			String keyword = keywordField.getText();
    			
    			//create a query for looking for that keyword
    			String query = ("SELECT course_id,title,current_enrollment,maximum_enrollment,year_level,course_description,calendar_entry "
    					+ "FROM course "
    					+ "WHERE course.course_id LIKE '%" + keyword + "%' "
    					+ "OR course.title LIKE '%" + keyword + "%' "
    					+ "OR course.course_description LIKE '%" + keyword + "%';");
    			
    			//check database for keyword
    			ResultSet courses = ServerConnection.sendToServer(newConnection, query);
    			while (courses.next()) {
    				courseDisplayTable.getItems().add(new Course(courses.getString("course_id"), courses.getString("title"), 
    						courses.getInt("current_enrollment"), courses.getInt("maximum_enrollment"), 
    						courses.getInt("year_level"), courses.getString("course_description"),
    						courses.getString("calendar_entry")));
    			}
    			
    			//finish with clearing the field
    			keywordField.clear();
    		} else if (searchComboBox.getSelectionModel().getSelectedItem() != null) {
    			String course = searchComboBox.getSelectionModel().getSelectedItem();
    			String searchQuery = ("SELECT course_id,title,current_enrollment,maximum_enrollment,year_level,course_description,calendar_entry "
    					+ "FROM course "
    					+ "WHERE course_id = '" + course + "';");
    			ResultSet courses = ServerConnection.sendToServer(newConnection, searchQuery);
    			if (courses.next()) 
    				courseDisplayTable.getItems().add(new Course(courses.getString("course_id"), courses.getString("title"), 
    						courses.getInt("current_enrollment"), courses.getInt("maximum_enrollment"), 
    						courses.getInt("year_level"), courses.getString("course_description"),
    						courses.getString("calendar_entry")));
    		}
    		else {	/*do nothing*/	}
    	} else {
    		//if in marks view
    		if (searchComboBox.getSelectionModel().getSelectedItem() != null) {
    			String course = searchComboBox.getSelectionModel().getSelectedItem();
    			String searchQuery = ("SELECT * FROM registration WHERE course_id = '" + course + "';");
    			ResultSet courses = ServerConnection.sendToServer(newConnection, searchQuery);
    			
				while(courses.next())
					marksDisplayTable.getItems().add(new Registration(courses.getString("course_id"), courses.getString("student_id"), courses.getInt("grade")));
				
				courses = ServerConnection.sendToServer(newConnection, "SELECT AVG(grade) FROM registration WHERE course_id = '" + course + "';");
				while(courses.next()) {
					int mark = courses.getInt("AVG(grade)");
	    			avgTextField.setText(String.valueOf(Math.round(mark)) + "%");
				}
    		}
    	}
    	//close connection to server
		ServerConnection.closeConnection(newConnection);
    }

    void clearTable() {
    	courseDisplayTable.getItems().clear();
    	marksDisplayTable.getItems().clear();
    }
    
    @FXML
    void submitData(MouseEvent event) throws SQLException {
    	//start connection
    	Connection newConnection = ServerConnection.openConnection();
    	
    	//get ready to submit data
    	//check fields
    	switch (descField.getPromptText()) {
    	case ("Student ID"):
    		//check if empty
    		if (courseidField.getText() == "" || descField.getText() == "" || calendarField.getText() == "") {
    			JOptionPane.showMessageDialog(null, "Warning! Cannot have empty field.", "Input Error", 2);
    		} else {
    			//check to see if values are valid
    			ResultSet checkID = ServerConnection.sendToServer(newConnection, "SELECT * FROM registration WHERE course_id = '" + courseidField.getText().toUpperCase() + "' AND student_id = '" + descField.getText() + "';");
    			if (checkID.next()) {
    				//this means the line exists, send signal
    				if (ServerConnection.updateToServer(newConnection, "UPDATE registration SET grade = '" + Integer.parseInt(calendarField.getText()) + "' WHERE course_id = '" + courseidField.getText() + "' AND student_id = '" + descField.getText() + "';") != 0) {
    					JOptionPane.showMessageDialog(null, "Update Successful. Press search for updates.");
    				} else {
    					JOptionPane.showMessageDialog(null, "Update Failed: Contact IT.", "Error", 0);
    				}
    			}
    		}
    		break;
    	case ("Description"):
    		if (courseidField.getText() == "" || descField.getText() == "" || calendarField.getText() == "") {
    			JOptionPane.showMessageDialog(null, "Warning! Cannot have empty field.", "Input Error", 2);
    		} else {
    			//check to see if values are valid
    			ResultSet checkID = ServerConnection.sendToServer(newConnection, "SELECT * FROM course WHERE course_id IN (SELECT course_id FROM teaches WHERE faculty_id = '" + Main.userID +"');");
    			if (checkID.next()) {
    				//this means the line exists, send signal
    				if (ServerConnection.updateToServer(newConnection, "UPDATE course SET course_description = '" + descField.getText() + "', calendar_entry = '" + calendarField.getText() + "' WHERE course_id = '" + courseidField.getText() + "';") != 0) {
    					JOptionPane.showMessageDialog(null, "Update Successful. Press search for updates.");
    				} else {
    					JOptionPane.showMessageDialog(null, "Update Failed: Contact IT.", "Error", 0);
    				}
    			} else {
    				JOptionPane.showMessageDialog(null, "You are not registered for this course.", "Error", 0);
    			}
    		}
    		break;
    	default:
    		JOptionPane.showMessageDialog(null, "Error: Please contact IT.", "Error", 2);
    		break;
    	}
    	
    	//make button disapear with boxes
    	submitButton.setVisible(false);
 
    	courseidField.setVisible(false);
    	courseidField.clear();
    	descField.setVisible(false);
    	descField.clear();
    	calendarField.setVisible(false);
    	calendarField.clear();
    	
    	//close connection
    	ServerConnection.closeConnection(newConnection);
    }
    
    @FXML
    void switchOption(MouseEvent event) throws SQLException {
    	//start a connection
    	Connection newConnection = ServerConnection.openConnection();
    	
    	//switch the tables
    		//start with switching from default
    	if (!marksDisplayTable.isVisible()) {
    		courseDisplayTable.setVisible(false);
    		marksDisplayTable.setVisible(true);
    		avgLabel.setVisible(true);
    		avgTextField.setVisible(true);
    		keywordField.setEditable(false);
    		keywordField.setPromptText("Disabled during View.");
    		switchButton.setText("Switch to Courses");
    		
    		//update the search course combo box
    		searchComboBox.getItems().clear();
    	
    		ResultSet options = ServerConnection.sendToServer(newConnection, "SELECT * FROM teaches WHERE faculty_id = "+ Main.userID + ";");
    		while (options.next())
    			searchComboBox.getItems().add(options.getString("course_id"));
    		//view complete
    	} else {
    		searchComboBox.getItems().clear();
    		
    		courseDisplayTable.setVisible(true);
    		marksDisplayTable.setVisible(false);
    		avgLabel.setVisible(false);
    		avgTextField.setVisible(false);
    		keywordField.setEditable(true);
    		keywordField.setPromptText("keyword");
    		switchButton.setText("Switch to Marks");
    		
    		comboBoxDefault();
    	}
    	
    	clearTable();
    	avgTextField.clear();
    	searchComboBox.setPromptText("Select Course");
    	//close connection
    	ServerConnection.closeConnection(newConnection);
    }
    
    void comboBoxDefault() {
    	try {
    		//leave connection
    		Connection newConnection = ServerConnection.openConnection();
    		
    		//add all the available courses to the searchBox
        	ResultSet searchComboOptions = ServerConnection.sendToServer(newConnection, "SELECT course_id FROM COURSE;");
        	
        	while(searchComboOptions.next())
        		searchComboBox.getItems().add(searchComboOptions.getString("course_id"));
        	
        	//end connection
        	ServerConnection.closeConnection(newConnection);
		} catch (SQLException e) {e.printStackTrace();}
    }

    @FXML
    void initialize() {
        //set user id
    	facultyIDLabel.setText(Main.userID);
    	
    	//initialize course table data
    	courseDisplayTable.setPlaceholder(new Label("No course information..."));
		courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("course_id"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
		currentSizeColumn.setCellValueFactory(new PropertyValueFactory<>("currentEnrollment"));
		maxSizeColumn.setCellValueFactory(new PropertyValueFactory<>("maximumEnrollment"));
		yearColumn.setCellValueFactory(new PropertyValueFactory<>("yearLevel"));
		descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		calendarColumn.setCellValueFactory(new PropertyValueFactory<>("calendarEntry"));
		
		marksDisplayTable.setPlaceholder(new Label("No marks information..."));
		marksIDColumn.setCellValueFactory(new PropertyValueFactory<>("courseID"));
		marksStudentColumn.setCellValueFactory(new PropertyValueFactory<>("studentID"));
		marksGradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
    	
    	//initialize all course
		comboBoxDefault();
    }
}

//esof 3050
// grasley // fiorot // grandell

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.util.EventObject;
import java.awt.EventQueue; 
@SuppressWarnings("unused")

public class studentAccessController {
	
    @FXML
    private Label studentIdLabel;
    
    @FXML
    private Button logoutButton;

    @FXML
    private Button searchCourseButton;

    @FXML
    private ComboBox<String> searchComboBox;

    @FXML
    private TextField keywordField;

    @FXML
    private Button viewMarksButton;

    @FXML
    private ComboBox<String> marksComboBox;
    
    @FXML
    private TextField marksTextField;
    
    @FXML
    private Button hideMarksButton;
    
    @FXML
    private Button enrollButton;
    
    @FXML
    private Button dropButton;

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
    private TableColumn<Course, String> enrolledColumn;
    
    //hashmap used for storing marks without constant database connection
    private Map<String, String> marksTable = new HashMap<String, String>();
        
    @FXML
    void logout(MouseEvent event) {
    	//send user to main page
    	Main.setPage(0);
    	Main.pages.remove(1);
    }
    
    @FXML
    void enrollInCourse(MouseEvent event) throws SQLException {
    	//this will prompt the user for the course_id
    	String input = JOptionPane.showInputDialog("Please enter a Course ID to enroll:");
    	input = input.toUpperCase();
    	
    	String enrollQuery = ("INSERT INTO registration VALUES('"+ input +"', '" + Main.userID + "', 0);");
    	
    	//now we check the database for the course
    	Connection newConnection = ServerConnection.openConnection();
    	ResultSet check = ServerConnection.sendToServer(newConnection, "SELECT * FROM course WHERE course_id = '" + input + "';");
    	
    	if (check.next()) { // this means the course exists
    		if (check.getInt("current_enrollment") < check.getInt("maximum_enrollment")) {
    			//this means the course has room
    			//now obtain the year of the course and the students year of enrollment
    			int courseYear = check.getInt("year_level");
    			ResultSet result = ServerConnection.sendToServer(newConnection, "SELECT student.year_level FROM student WHERE student.student_id = " + Main.userID + ";");
    			if (result.next()) {
    				int studentYear = result.getInt("year_level");
    				if (studentYear >= courseYear) {    					
    					//is the user already in the course?
    					if (ServerConnection.sendToServer(newConnection, "SELECT * FROM registration WHERE course_id = '" + input + "' AND student_id = " + Main.userID + ";").next()) {
    						//this means they are already registered, throw
    						JOptionPane.showMessageDialog(null, "Cannot Enroll: Already registered.", "Enrollment Error", 0);
    					} else {
    						//this means that they are not registered and fully qualify to register.
    						if (ServerConnection.updateToServer(newConnection, enrollQuery) != 0) {
    	        				//submit to database if this passes
            					JOptionPane.showMessageDialog(null, "Enrollment Confirmed. Please re-login to see changes.", "Enrollment Success", 1);
            				} else {
            					JOptionPane.showMessageDialog(null, "Cannot Enroll: Please contact an administrator.", "Enrollment Error", 0);
            				}
    					}
        			} else {
        				JOptionPane.showMessageDialog(null, "Cannot Enroll: You do not qualify for this year level.", "Enrollment Error", 0);
        			}
    			} else {
    				JOptionPane.showMessageDialog(null, "Error: Contact Administration", "Enrollment Error", 0);
    			}
    		} else {
    			JOptionPane.showMessageDialog(null, "Cannot Enroll: No available spots.", "Enrollment Error", 0);
    		}
    	} else {
    		JOptionPane.showMessageDialog(null, "Cannot Enroll: Course ID not found.", "Enrollment Error", 0);
    	}
    	ServerConnection.closeConnection(newConnection);
    }
    
    @FXML
    void dropCourse(MouseEvent event) throws SQLException {
    	//this will prompt the user for the course_id
    	String input = JOptionPane.showInputDialog("Please enter a Course ID to drop:");
    	
    	String dropQuery = ("DELETE FROM registration WHERE student_id = " + Main.userID + " AND course_id = '" + input.toUpperCase() + "' LIMIT 1;");

    	//check to see if the course exists
    	Connection newConnection = ServerConnection.openConnection();
    	ResultSet check = ServerConnection.sendToServer(newConnection, "SELECT * FROM course WHERE course_id = '" + input.toUpperCase() + "';");
    	
    	if(check.next()) {
    		if (ServerConnection.updateToServer(newConnection, dropQuery) != 0) {
				JOptionPane.showMessageDialog(null, "Drop successful. Please re-login to see changes.", "Drop Success", 1);
			} else {
				JOptionPane.showMessageDialog(null, "Cannot Drop: Please contact an administrator.", "Drop Error", 0);
			}
    	} else {
    		JOptionPane.showMessageDialog(null, "Cannot Drop: Course ID not enrolled.", "Drop Error", 0);
    	}
    	ServerConnection.closeConnection(newConnection);
    }
    
    @FXML
    void updateMark(MouseEvent event) {
    	if (marksComboBox.getSelectionModel().getSelectedItem() != null) {
    		marksTextField.setText(marksTable.get(marksComboBox.getSelectionModel().getSelectedItem()) + "%");
    	}
    }
    
    @FXML
    void hideMark(MouseEvent event) {
    	marksTextField.clear();
    }
    
    @FXML
    void searchCourse(MouseEvent event) throws SQLException {
		//start with clearing the table data
    	clearTable();
    	
    	//start the database connection
    	Connection newConnection = ServerConnection.openConnection();
		
    	//check to see if the table is setup
    	if (!courseDisplayTable.isVisible()) {
    		courseDisplayTable.setVisible(true);
    	}
    		
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
						courses.getString("calendar_entry"), checkEnrollment(courses.getString("course_id"))));
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
						courses.getString("calendar_entry"), checkEnrollment(courses.getString("course_id"))));
		}
		else {	/*do nothing*/	}
		
    	//close connection to server
		ServerConnection.closeConnection(newConnection);
    }
    
    boolean checkEnrollment(String course_id) throws SQLException {
    	boolean result = false;
    	
    	Connection newConnection = ServerConnection.openConnection();
    	
    	String query = ("SELECT * FROM registration WHERE course_id LIKE '" + course_id + "' AND student_id = '" + Main.userID + "';");
    	
    	if(ServerConnection.sendToServer(newConnection, query).next())
    		result = true;
    	
    	ServerConnection.closeConnection(newConnection);
    	//return false;
		return result;
    }
    
    //function for clearing the table data
    void clearTable() {
    	courseDisplayTable.getItems().clear();
    }
    
    @FXML
    void initialize() {
    	
    	//set user id
    	studentIdLabel.setText(Main.userID);
    	
    	//initialize course table data
    	courseDisplayTable.setPlaceholder(new Label("No course information..."));
		courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("course_id"));
		titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
		currentSizeColumn.setCellValueFactory(new PropertyValueFactory<>("currentEnrollment"));
		maxSizeColumn.setCellValueFactory(new PropertyValueFactory<>("maximumEnrollment"));
		yearColumn.setCellValueFactory(new PropertyValueFactory<>("yearLevel"));
		descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
		calendarColumn.setCellValueFactory(new PropertyValueFactory<>("calendarEntry"));
		enrolledColumn.setCellValueFactory(new PropertyValueFactory<>("enrolled"));
    	
		//initialize all course
    	try {
    		//leave connection
    		Connection newConnection = ServerConnection.openConnection();
    		
    		//add all the available courses to the searchBox
        	ResultSet searchComboOptions = ServerConnection.sendToServer(newConnection, "SELECT course_id FROM COURSE;");
        	
        	while(searchComboOptions.next())
        		searchComboBox.getItems().add(searchComboOptions.getString("course_id"));
        	
        	//add all the enrolled courses to the marksBox
        	ResultSet marksComboOptions = ServerConnection.sendToServer(newConnection, "SELECT course_id,grade FROM registration WHERE student_id = " + Main.userID + ";");
        	
        	while (marksComboOptions.next()) {
        		marksComboBox.getItems().add(marksComboOptions.getString("course_id"));
        		marksTable.put(marksComboOptions.getString("course_id"), marksComboOptions.getString("grade"));
        	}
        	
        	//end connection
        	ServerConnection.closeConnection(newConnection);
        	
    	} catch (SQLException e) {e.printStackTrace();}
    }
}
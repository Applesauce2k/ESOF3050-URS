import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javax.swing.JOptionPane;

public class loginController {
    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private Label universityLabel;
    @FXML
    private Label loginLabel;
    @FXML
    private Label userTypeLabel;
    @FXML
    private ChoiceBox<String> typeChoiceBox;
    @FXML
    private Label userIDLabel;
    @FXML
    private TextField userIdField;
    @FXML
    private Label pLabel;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton;
    
    @FXML
    void login(MouseEvent event) throws IOException {
    	//create a variable for the options on the login page
    	String typeOption = typeChoiceBox.getSelectionModel().getSelectedItem();
    	String userIdData = userIdField.getText();
    	String passData = passwordField.getText();
    	
    	
    	if (typeOption == "Student") {
    		//check database for code
    			//create student query
    		String studentQuery = queryCreator(typeOption, "student_id", userIdData, passData);
    		try {
				if (ServerConnection.checkLogin(studentQuery, userIdData, passData)) {
					Main.setUserID(userIdData);
					Main.pages.add(FXMLLoader.load(getClass().getResource("StudentAccess.fxml")));	//student is page 1
					pageUpdate(1);
				}
				else {errorCall();}
			} catch (SQLException e) {e.printStackTrace();}	
    	}
    	else if (typeOption == "Instructor") {
    		//check database for code
				//create student query
			String instructorQuery = queryCreator(typeOption, "faculty_id", userIdData, passData);		
			try {
				if (ServerConnection.checkLogin(instructorQuery, userIdData, passData)) {
					Main.setUserID(userIdData);
					Main.pages.add(FXMLLoader.load(getClass().getResource("InstructorAccess.fxml")));	//instructor is page 2
					pageUpdate(1);
				}
				else {errorCall();}
			} catch (SQLException e) {e.printStackTrace();}
    	}
    	else if (typeOption == "Staff") {
    		//check database for code
				//create student query
			String staffQuery = queryCreator(typeOption, "staff_id", userIdData, passData);
			try {
				if (ServerConnection.checkLogin(staffQuery, userIdData, passData)) {
					Main.setUserID(userIdData);
					Main.pages.add(FXMLLoader.load(getClass().getResource("StaffAccess.fxml")));	//instructor is page 2
					pageUpdate(1);
				}
				else {errorCall();}
			} catch (SQLException e) {e.printStackTrace();}
    	}
    	else {
    		JOptionPane.showMessageDialog(null, "Please select a user type.", "Error", 2);
    	}
    }
    
    @FXML
    void initialize() {
        //add the possible options for the choice box field
    	typeChoiceBox.getItems().add("Student");
    	typeChoiceBox.getItems().add("Instructor");
    	typeChoiceBox.getItems().add("Staff");
    }
    
    //function to call when the password credentials are incorrect.
    void errorCall() {
    	JOptionPane.showMessageDialog(null, "Incorrect Login Credentials.", "Error", 0);
		passwordField.clear();
    }
    
    void pageUpdate(int pageIndex) {
    	Main.setPage(pageIndex);
   		passwordField.clear();
    }
    
    //function to call when creating the queries for database login
    String queryCreator(String type, String idName, String ID, String pass) {
    	//creates a query
    		//ex: SELECT student_id FROM student WHERE password = password
    	return ("SELECT " + type + "." + idName + "," + type + ".pass_word " +
    			"FROM " + type + " WHERE binary " + type + "." + idName + " = \'" + ID 
    			+ "\' AND binary " + type + ".pass_word = \'" + pass + "\';");
    }
}

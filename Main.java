//esof 3050
// grasley / fiorot / grandell

import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
@SuppressWarnings("unused")

public class Main extends Application {
	//create initial placeholder pane
	static AnchorPane root;
	//create a list to contain all fxml panes
	static ArrayList<VBox> pages = new ArrayList<VBox>();
	
	public static int currentPageNum = 0;
	static int pagewidth = 900;
	static int pageheight = 600;
	
	public static String userID;

	public static void setUserID(String userID) {
		Main.userID = userID;
	}
	
	@Override
	public void start(Stage startStage) throws Exception {		
		//the root has to be declared to an anchor placeholder to properly overwrite the current scene
		root =
				FXMLLoader.load(getClass().getResource("anchor.fxml"));
		
		//add all the fxml files to the pages list
		pages.add(FXMLLoader.load(getClass().getResource("Login.fxml")));	//login is page 0		
		
		//add the login page to the root, so it gets added to the scene
		root.getChildren().add(pages.get(currentPageNum));
		
		//initialize the scene
		Scene scene = new Scene(root,pagewidth,pageheight);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		startStage.setTitle("University System");
		startStage.setScene(scene);
		startStage.show();
		
	}
	
	//create get and set methods for changing pages
	public static VBox getPage(int pageNum) {
		return pages.get(pageNum);
	}
	
	public static void setPage(int pageNum) {
		//start with removing the old page from the root
		root.getChildren().remove(pages.get(currentPageNum));
		//now with empty root, insert the new child page
		root.getChildren().add(pages.get(pageNum));
		//update the page num counter to keep track of where we are
		currentPageNum = pageNum;
	}
	
	public static void main(String[] args) {
		
		//load the database connection driver
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
            e.printStackTrace();
        }
		
		//run the user interface
		launch(args);
	}

	
}
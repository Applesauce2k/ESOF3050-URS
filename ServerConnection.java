//esof 3050

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
@SuppressWarnings("unused")

public class ServerConnection {
	// since the driver is already loaded in args, it is not required here

//start of function list
	// open connection for the server
	public static Connection openConnection() throws SQLException {
		// this function creates a connection to the server
		Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/urs?user=Jarrod&password=root");
		return c;
	}

	// close connection for the server
	public static void closeConnection(Connection c) throws SQLException {
		c.close();
	}

	public static boolean checkLogin(String query, String userID, String userPass) throws SQLException {
		// create ending variable
		boolean loginAuth = false;

		// initialize object
		Connection connect = openConnection();
		// create a statement object
		Statement s = connect.createStatement();
		// create a result object
		ResultSet r = s.executeQuery(query);		
		
		while (r.next()) {	
			loginAuth = true;
			 // this is the case for allowing the user in loginAuth = true; } 
		}
		// end with stopping the connection
		closeConnection(connect);

		return loginAuth;
	}

	public static ResultSet sendToServer(Connection connect, String query) throws SQLException {
		// create a statement object
		Statement statement = connect.createStatement();
		// create a result object
		ResultSet result = statement.executeQuery(query);
		return result;
	}
	
	public static int updateToServer(Connection connect, String query) throws SQLException {
		// create a statement object
		Statement statement = connect.createStatement();
		//send to server, return result
		return (statement.executeUpdate(query));
	}
	 

//end of function list
}
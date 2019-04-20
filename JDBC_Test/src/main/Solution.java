/**
 * 
 */
package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author Paul-Reftu
 *
 */
public class Solution {

	/**
	 * 
	 */
	public Solution() {
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			/*
			 * instead of "localhost:1521:xe" you should use your own database connection details
			 * instead of "STUDENT", "STUDENT" - you ought to use the username/password pair for your connection
			 */
			Connection con = 
					DriverManager.getConnection(
							"jdbc:oracle:thin:@localhost:1521:xe", 
							"STUDENT", "STUDENT");
			
			Statement statement = con.createStatement();
			
			ResultSet rs = statement.executeQuery("SELECT * FROM nodes");
			
			while (rs.next()) {
				// output nodes.id, nodes.floor, nodes.name and nodes.type
				System.out.println(rs.getInt(1) + " " + rs.getInt(2) + " " +
						rs.getString(3) + " " + rs.getString(4));
			}
			
			con.close();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}

	}

}

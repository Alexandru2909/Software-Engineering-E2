package main;
/**
 * 
 */

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Paul-Reftu
 *
 */
public class DatabaseEmissary {
	private String driver;
	private String connPath;
	private String username;
	private String password;
	private Connection conn;

	/**
	 * @return the conn
	 */
	public Connection getConn() {
		return conn;
	}

	/**
	 * @param driver the JDBC driver to be used (e.g "oracle.jdbc.driver.OracleDriver")
	 * @param connPath the connection path for your DB (e.g "localhost:1521:xe")
	 * @param username the username of your DB account (e.g "STUDENT")
	 * @param password the password of your DB account (e.g "STUDENT0")
	 */
	public DatabaseEmissary(String driver, String connPath, String username, String password) {
		this.driver = driver;
		this.connPath = connPath;
		this.username = username;
		this.password = password;	
	}
	
	/**
	 * @throws ClassNotFoundException when the driver class is unknown
	 * @throws SQLException when a DB access error occurs
	 */
	public void establishConn() throws ClassNotFoundException, SQLException {
		Class.forName(driver);
        conn = DriverManager.getConnection(connPath, username, password);
	}
	
	/**
	 * @return the list of results w.r.t the query (i.e, all classrooms)
	 * @throws SQLException when the creation of the execution statement or the execution of the query itself fails
	 */
	public List<String> selectAllClassroomNames() throws SQLException {
		List<String> queryResults = new ArrayList<String>();
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery("SELECT name FROM nodes WHERE type='classroom'");
			
		while (rs.next()) {
			queryResults.add(rs.getString(1));
		}
		
		return queryResults;
	}
	
	/**
	 * @param classroomName the name of the classroom whose schedule should be returned
	 * @return the list of results w.r.t the query (i.e, a set of tuples of the form (day, starting_time, ending_time, course_name))
	 * @throws SQLException on database access error
	 */
	public List<String> selectClassroomSchedule (String classroomName) throws SQLException {
		List<String> queryResults = new ArrayList<String>();
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT s.day, s.starting_time, s.ending_time, c.name AS course_name " + 
				"FROM nodes n JOIN schedule s ON n.id=s.node_id " + 
				"JOIN courses c ON s.course_id=c.id " + 
				"WHERE n.type='classroom' AND n.name ='" + classroomName + "' " + 
				"ORDER BY CASE " + 
					"WHEN s.day = 'LUNI' THEN 1 " + 
					"WHEN s.day = 'MARTI' THEN 2 " + 
					"WHEN s.day = 'MIERCURI' THEN 3 " + 
					"WHEN s.day = 'JOI' THEN 4 " + 
					"WHEN s.day = 'VINERI' THEN 5 " + 
					"WHEN s.day = 'SAMBATA' THEN 6 " + 
					"WHEN s.day = 'DUMINICA' THEN 7 " +
				"END ASC"
				);
		
		while (rs.next()) {
			queryResults.add(rs.getString(1) + ' ' + rs.getString(2) + ' ' +
					rs.getString(3) + ' ' + rs.getString(4));
		}
		
		return queryResults;
	}
	
	/**
	 * @param tableNameList the list of the names of the tables to check whether they exist in our DB or not
	 * @return true if given tables exist, false otherwise
	 * @throws SQLException upon DB access failure
	 */
	public boolean doDbTablesExist(List<String> tableNameList) throws SQLException {
		boolean currTableExists = false;
		
		for (String tableName : tableNameList) {
			DatabaseMetaData dbMetadata = conn.getMetaData();
			ResultSet rs = dbMetadata.getTables(null, null, tableName, null);
			
			while (rs.next()) {
				String currTableName = rs.getString("TABLE_NAME");
				
				if (currTableName != null && currTableName.equals(tableName)) {
					currTableExists = true;
					break;
				}
				else
					continue;
			}
			
			if (!currTableExists)
				return false;
			
			currTableExists = false;
		}
			
		return true;
	}
	
	/**
	 * @param tableNameList the list of the names of the tables to check whether they are filled w/ information
	 * @return true if the tables are filled, false otherwise
	 * @throws SQLException upon DB access failure
	 */
	public boolean areDbTablesFilled(List<String> tableNameList) throws SQLException {
		for (String tableName : tableNameList) {
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM " + tableName);
			
			rs.next();
			
			if (rs.getInt(1) == 0)
				return false;			
		}
		
		return true;
	}

}

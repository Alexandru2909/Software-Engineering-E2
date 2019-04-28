/**
 * 
 */

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.List;s

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
	 * establish connection to the database using connection credentials (driver, connPath, username, password)
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
	 */
	public List<String> selectClassroomSchedule (String classroomName) {
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
}

package main;
/**
 * 
 */

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * the main class whose instance will hold the current connection to the database and be able to perform certain tasks related to it
 * @author Paul-Reftu
 *
 */
public class DatabaseEmissary {
	private String dbPath;
	private String dbDriver;
	private Connection conn;

	/**
	 * get the current connection to the database
	 * @return the conn
	 */
	public Connection getConn() {
		return conn;
	}

	/**
	 * construct the instance w.r.t this class, given the path to the database and the driver
	 * @param dbDriver the driver of our database
	 * @param connPath the connection path for our DB (e.g "jdbc:sqlite:../database/faculty.db")
	 */
	public DatabaseEmissary(String dbDriver, String connPath) {
		this.dbPath = dbDriver;
		this.dbDriver = connPath;
	}
	
	/**
	 * establish a connection to the database given by the currently declared type
	 * @throws SQLException when a DB access error occurs
	 */
	public void establishConn() throws SQLException {
        conn = DriverManager.getConnection(dbDriver);
	}
	
	/**
	 * select all classroom names in our database
	 * @return the list of results w.r.t the query (i.e, all classrooms)
	 * @throws SQLException when the creation of the execution statement or the execution of the query itself fails
	 */
	public List<String> selectAllClassroomNames() throws SQLException {
		List<String> queryResults = new ArrayList<String>();
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery("SELECT name FROM nodes WHERE type IN ('Classroom', 'Amphitheatre')");
			
		while (rs.next()) {
			queryResults.add(rs.getString(1));
		}
		
		return queryResults;
	}
	
	/**
	 * select all schedule entries related to the given classroom name (could return null in case that specific classroom does not exist in our DB)
	 * @param classroomName the name of the classroom whose schedule should be returned
	 * @return the list of results w.r.t the query (i.e, a set of tuples of the form (day, starting_time, ending_time, course_name), which are each stored in their own list)
	 * @throws SQLException on database access error
	 */
	public List<List<String>> selectClassroomSchedule (String classroomName) throws SQLException {
		List<List<String>> queryResults = new ArrayList<List<String>>();
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(
				"SELECT s.day, s.starting_time, s.ending_time, c.name AS course_name " + 
				"FROM nodes n JOIN schedule s ON n.id=s.node_id " + 
				"JOIN courses c ON s.course_id=c.id " + 
				"WHERE n.type IN ('Classroom', 'Amphitheatre') AND n.name ='" + classroomName + "' " + 
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
			List<String> resultEntry = new ArrayList<String>(Arrays.asList(rs.getString(1), 
					rs.getString(2), rs.getString(3), rs.getString(4)));
			
			queryResults.add(resultEntry);
		}
		
		return queryResults;
	}
	
	/**
	 * checks whether the database at the currently-declared path exists
	 * @return true if the database specified by the current path exists; false otherwise
	 */
	public boolean doesDbExist() {
		File f = new File(dbPath);
		
		if (f.exists() && !f.isDirectory())
			return true;
		
		return false;
	}
	
	/**
	 * checks whether the given tables exist in our database
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
	 * checks whether the given tables are empty
	 * @param tableNameList the list of the names of the tables to check whether they are filled w/ information
	 * @return true if the tables are filled, false otherwise
	 * @throws SQLException upon DB access failure
	 */
	public boolean areDbTablesFilled(List<String> tableNameList) throws SQLException {
		for (String tableName : tableNameList) {
			/*
			 * for the moment, we have no images stored
			 */
			if (tableName.equals("images"))
				continue;
			
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery("SELECT COUNT(*) FROM " + tableName);
			
			rs.next();
			
			if (rs.getInt(1) == 0)
				return false;			
		}
		
		return true;
	}

	/**
	 * creates the tables w.r.t the database
	 * @throws SQLException upon database access error or operation attempt through a closed connection
	 */
	public void createTables() throws SQLException {
		List<String> queries = new ArrayList<>(Arrays.asList(
				"DROP TABLE IF EXISTS schedule\r\n", 
				"DROP TABLE IF EXISTS edges\r\n", 
				"DROP TABLE IF EXISTS images\r\n", 
				"DROP TABLE IF EXISTS nodes\r\n", 
				"DROP TABLE IF EXISTS courses\r\n", 
				
				"CREATE TABLE nodes (\r\n" + 
				"  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\r\n" + 
				"  floor INTEGER,\r\n" + 
				"  name VARCHAR(50),\r\n" + 
				"  type VARCHAR(15)\r\n" + 
				")\r\n", 
				
				"CREATE TABLE edges (\r\n" + 
				"  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\r\n" + 
				"  id1 INTEGER NOT NULL,\r\n" + 
				"  id2 INTEGER NOT NULL,\r\n" + 
				"  cost DOUBLE PRECISION,\r\n" + 
				"  CONSTRAINT fk_edges_nodes1 FOREIGN KEY (id1) REFERENCES nodes(id),\r\n" + 
				"  CONSTRAINT fk_edges_nodes2 FOREIGN KEY (id2) REFERENCES nodes(id)\r\n" + 
				")\r\n",
				
				"CREATE TABLE images (\r\n" + 
				"  node_id INTEGER NOT NULL,\r\n" + 
				"  image VARCHAR(100),\r\n" + 
				"   CONSTRAINT fk_edges_nodes FOREIGN KEY (node_id) REFERENCES nodes(id)\r\n" + 
				")\r\n",
				
				"CREATE TABLE courses (\r\n" + 
				"  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\r\n" + 
				"  name VARCHAR2(60) NOT NULL,\r\n" + 
				"  studyGroup VARCHAR2(100)\r\n" + 
				")\r\n",
				
				"CREATE TABLE schedule (\r\n" + 
				"  node_id INTEGER not null,\r\n" + 
				"  course_id INTEGER not null,\r\n" + 
				"  starting_time VARCHAR2(20),\r\n" + 
				"  ending_time VARCHAR2(20),\r\n" + 
				"  day VARCHAR(10),\r\n" + 
				"  CONSTRAINT fk_schedule_nodes FOREIGN KEY (node_id) REFERENCES nodes(id),\r\n" + 
				"  CONSTRAINT fk_schedule_courses FOREIGN KEY (course_id) REFERENCES courses(id)\r\n" + 
				")"));
		
		Statement statement = conn.createStatement();
		
		for (String query : queries)
			statement.execute(query);
		
		return;
	}
}

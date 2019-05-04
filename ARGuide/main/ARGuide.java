/**
 * 
 */
package main;

import java.io.File;

/**
 * Apache Ant Library required: http://www.java2s.com/Code/Jar/a/Downloadapacheant182jar.htm
 */

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;

/**
 * the object type that makes the connection to the Back-End functionalities of the ARG application
 * @author Paul-Reftu
 *
 */
public class ARGuide {
	private DatabaseEmissary dbEmissary;
	private ARGProcessor argProcessor;
	
	/*
	 * path to our database
	 */
	private String dbPath = "../../database/faculty.db";
	
	/*
	 * the path to the database creation script
	 */
	private String dbCreationScriptPath = "../../database/dbCreation.sql";
	
	/*
	 * the path to the JSON resource representing our working schedule and our building plan
	 */
	private String schedulePath = "../schedules/facultySchedule.json";
	private String planPath;
	
	/*
	 * an executer class that is able to run SQL scripts for us
	 */
	final class SqlExecuter extends SQLExec {
		public SqlExecuter() {
			Project project = new Project();
			project.init();
			setProject(project);
			setTaskType("sql");
			setTaskName("sql");
		}
	}

	/**
	 * establish connection to the database and insert information w.r.t the Building Plan and Working Schedule if necessary
	 * @param connPath the connection path for your DB (e.g "jdbc:sqlite:../../database/faculty.db")
	 * @throws ClassNotFoundException when the driver class is unknown
	 * @throws SQLException when a DB access error occurs
	 * @throws JSONResourceException upon unknown request or WSProcessor operation failure
	 */
	public ARGuide(String connPath) throws ClassNotFoundException, SQLException, JSONResourceException {
		List<String> tableNameList = new ArrayList<String>();
		tableNameList.addAll(Arrays.asList("nodes", "edges", "images", "schedule", "courses"));
		
		/*
		 * make a new DatabaseEmissary object and establish connection to our database
		 */
		this.dbEmissary = new DatabaseEmissary(dbPath, connPath);
		
		/*
		 * don't need to check if database already exists or not;
		 * the establishment of the connection creates it automatically IN CASE it doesn't exist
		 */
		dbEmissary.establishConn();
		
		/*
		 * make a new processor for our application
		 */
		this.argProcessor = new ARGProcessor(this.dbEmissary.getConn(), schedulePath, planPath);
		
		/*
		 * if the database tables that we need for our application do not already exist, create them
		 */
		if (!dbEmissary.doDbTablesExist(tableNameList)) {
			SqlExecuter executer = new SqlExecuter();
			
			executer.setSrc(new File(dbCreationScriptPath));
			executer.setUrl(connPath);
			executer.execute();
		}
		
		/*
		 * if our database tables that we need for our application are not already filled with the required information, then fill them accordingly
		 */
		if (!dbEmissary.areDbTablesFilled(tableNameList)) {
			/*
			 * parse the JSON resource for our building plan and working schedule and save them in our DB if they are valid
			 */
			/*
			 * Building Plan not yet deployed.
			 * 
				this.argProcessor.processRequest("parseBP");
				this.argProcessor.processRequest("saveBP");
				
				this.argProcessor.processRequest("parseWS");
				this.argProcessor.processRequest("saveWS");
			*/
		}
	}

	/**
	 * select all classroom names of our database
	 * @return the list of results w.r.t the query (i.e, all classrooms)
	 * @throws SQLException when the creation of the execution statement or the execution of the query itself fails
	 */
	public List<String> selectAllClassroomNames() throws SQLException {
		return dbEmissary.selectAllClassroomNames();
	}
	
	/**
	 * select all schedule entries related to the given classroom name (could return NULL if the given classroom does NOT exist)
	 * @param classroomName the name of the classroom whose schedule should be returned
	 * @return the list of results w.r.t the query (i.e, a set of tuples of the form (day, starting_time, ending_time, course_name))
	 * @throws SQLException on database access error
	 */
	public List<String> selectClassroomSchedule(String classroomName) throws SQLException {
		return dbEmissary.selectClassroomSchedule(classroomName);
	}
}

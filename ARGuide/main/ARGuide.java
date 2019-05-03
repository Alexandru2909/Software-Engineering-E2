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
import java.util.List;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.SQLExec;

/**
 * @author Paul-Reftu
 *
 */
public class ARGuide {
	private DatabaseEmissary dbEmissary;
	private ARGProcessor argProcessor;
	
	/*
	 * the names of our PL/SQL database packages that handle our working schedule and our building plan respectively
	 */
	private String schedulePackageName = "ScheduleMaster";
	private String planPackageName = "BuildingPlanMaster";
	
	/*
	 * the extension of the method names for our PL/SQL packages
	 */
	private String schedulePackageMethodsExtension = "Schedule";
	private String planPackageMethodsExtension = "BuildingPlan";
	
	/*
	 * the path to our PL/SQL scripts that we require
	 */
	private String dbCreationScriptPath = "../../database/databaseCreation.sql";
	private String scheduleMasterScriptPath = "../../database/scheduleMaster.sql";
	private String buildingPlanMasterScriptPath = "../../database/buildingPlanMaster.sql";
	
	/*
	 * the path to the JSON resource representing our working schedule and our building plan
	 */
	private String schedulePath = "../../scheduleParser/schedules/facultySchedule.json";
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
	 * @param driver the JDBC driver to be used (e.g "oracle.jdbc.driver.OracleDriver")
	 * @param connPath the connection path for your DB (e.g "localhost:1521:xe")
	 * @param username the username of your DB account (e.g "STUDENT")
	 * @param password the password of your DB account (e.g "STUDENT0")
	 * @throws ClassNotFoundException when the driver class is unknown
	 * @throws SQLException when a DB access error occurs
	 * @throws JSONResourceException upon unknown request or WSProcessor operation failure
	 */
	public ARGuide(String driver, String connPath, String username, String password) throws ClassNotFoundException, SQLException, JSONResourceException {
		List<String> tableNameList = new ArrayList<String>();
		tableNameList.add("nodes");
		tableNameList.add("edges");
		tableNameList.add("images");
		tableNameList.add("schedule");
		tableNameList.add("courses");
		
		/*
		 * make a new DatabaseEmissary object and establish connection to our database
		 */
		this.dbEmissary = new DatabaseEmissary(driver, connPath, username, password);
		dbEmissary.establishConn();
		
		/*
		 * make a new processor for our application
		 */
		this.argProcessor = new ARGProcessor(this.dbEmissary.getConn(), schedulePath, schedulePackageName, schedulePackageMethodsExtension, planPath, planPackageName, planPackageMethodsExtension);
		
		/*
		 * if the database tables that we need for our application do not already exist, create them
		 */
		if (!dbEmissary.doDbTablesExist(tableNameList)) {
			SqlExecuter executer = new SqlExecuter();
			
			executer.setSrc(new File(dbCreationScriptPath));
			executer.setDriver(driver);
			executer.setUrl(connPath);
			executer.setUserid(username);
			executer.setPassword(password);
			
			executer.execute();
		}
		
		/*
		 * if our database tables that we need for our application are not already filled with the required information, then fill them accordingly
		 */
		if (!dbEmissary.areDbTablesFilled(tableNameList)) {
			SqlExecuter executer = new SqlExecuter();
			executer.setSrc(new File(scheduleMasterScriptPath));
			executer.setDriver(driver);
			executer.setUrl(connPath);
			executer.setUserid(username);
			executer.setPassword(password);
			executer.execute();
			
			/*
			 * Building Plan resource is not yet deployed. The script thus does not yet have the required information to use.
			 * 
				executer = new SqlExecuter();
				executer.setSrc(new File(buildingPlanMasterScriptPath));
				executer.setDriver(driver);
				executer.setUrl(connPath);
				executer.setUserid(username);
				executer.setPassword(password);
				executer.execute();
			*/
			
			/*
			 * parse the JSON resource for our working schedule and save it in our DB if it is valid
			 */
			this.argProcessor.processRequest("parseWS");
			this.argProcessor.processRequest("saveWS");
			
			/*
			 * parse the JSON resource for our building plan and save it in our DB if it is valid
			 */
			
			/*
			 * Building Plan not yet deployed.
			 * 
				this.argProcessor.processRequest("parseBP");
				this.argProcessor.processRequest("saveBP");
			*/
		}
	}

	/**
	 * @return the list of results w.r.t the query (i.e, all classrooms)
	 * @throws SQLException when the creation of the execution statement or the execution of the query itself fails
	 */
	public List<String> selectAllClassroomNames() throws SQLException {
		return dbEmissary.selectAllClassroomNames();
	}
	
	/**
	 * @param classroomName the name of the classroom whose schedule should be returned
	 * @return the list of results w.r.t the query (i.e, a set of tuples of the form (day, starting_time, ending_time, course_name))
	 * @throws SQLException on database access error
	 */
	public List<String> selectClassroomSchedule(String classroomName) throws SQLException {
		return dbEmissary.selectClassroomSchedule(classroomName);
	}
	
	public void ARGuideController() {
		
	}
}

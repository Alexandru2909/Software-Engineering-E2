/**
 * 
 */
package main;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import webParserV3.AutoUpdateClass;
import webParserV3.WebParser;

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
	 * default: "ARGuide/database/faculty.db"
	 */
	private String dbPath = "ARGuide/database/faculty.db";
	
	private String dbDriver = "jdbc:sqlite:" + dbPath;
	
	/*
	 * the path to the JSON resource representing our working schedule and our building plan
	 * default for WS: "ARGuide/schedules/facultySchedule.json"
	 * default for BP: "ARGuide/buildingPlan/buildingPlan.json"
	 */
	private String schedulePath = "ARGuide/schedules/facultySchedule.json";
	private String planPath = "ARGuide/buildingPlan/buildingPlan.json";

	/**
	 * establish connection to the database and insert information w.r.t the Building Plan and Working Schedule if necessary
	 * @param dbPath the path to the database
	 * @param schedulePath the path to the JSON resource representing the Working Schedule
	 * @param planPath the path to the JSON resource representing the Building Plan
	 * @throws ClassNotFoundException when the driver class is unknown
	 * @throws SQLException when a DB access error occurs
	 * @throws JSONResourceException upon unknown request or WSProcessor operation failure
	 */
	public ARGuide(String dbPath, String schedulePath, String planPath) throws ClassNotFoundException, SQLException, JSONResourceException {
		this.dbPath = dbPath;
		this.dbDriver = "jdbc:sqlite:" + dbPath;
		this.schedulePath = schedulePath;
		this.planPath = planPath;
		
		/****************** WEBPARSER CALL ****************************/
		/*
		Old code
		AutoUpdateClass autoUpdateClass=new AutoUpdateClass("https://profs.info.uaic.ro/~orar/orar_resurse.html","lastUpdateFile");
		if(autoUpdateClass.runDataCollector()==true){
			try {
				WebParser parser = new WebParser("https://profs.info.uaic.ro/~orar/", "orar_resurse.html", "resultFile.json","sectionsNames.txt");
				parser.runParset();
			}catch (Exception e){
				System.out.println("problema la crearea fisielor" +e.getMessage());
			}
		}*/
		try {
		    AutoUpdateClass autoUpdateClass=new AutoUpdateClass("https://profs.info.uaic.ro/~orar/orar_resurse.html","ARGuide/schedules/lastUpdateTime.txt");
		    if(autoUpdateClass.runDataCollector()==false){
			System.out.println("parser-ul a rulat");
			WebParser parser = new WebParser("https://profs.info.uaic.ro/~orar/", "orar_resurse.html", "ARGuide/schedules/facultySchedule.json","ARGuide/schedules/sectionsNames.txt");
			parser.runParset();
			autoUpdateClass.setNewDate();
		    }else{
			System.out.println("parserul nu a rulat, niciun update necesar");
		    }

        }catch (Exception e){
            System.out.println("problema la crearea fisielor" +e.getMessage());
        }
		/****************** WEBPARSER CALL ****************************/


		List<String> tableNameList = new ArrayList<String>();
		tableNameList.addAll(Arrays.asList("nodes", "edges", "images", "schedule", "courses"));
		
		/*
		 * make a new DatabaseEmissary object and establish connection to our database
		 */
		this.dbEmissary = new DatabaseEmissary(dbPath, dbDriver);
		
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
		if (!dbEmissary.doDbTablesExist(tableNameList)) 
			dbEmissary.createTables();
		
		/*
		 * if our database tables that we need for our application are not already filled with the required information, then fill them accordingly
		 */
		if (!dbEmissary.areDbTablesFilled(tableNameList)) {
			/*
			 * parse the JSON resource for our building plan and working schedule and save them in our DB if they are valid
			 */
				this.argProcessor.processRequest("parseBP");
				this.argProcessor.processRequest("saveBP");
				
				this.argProcessor.processRequest("parseWS");
				this.argProcessor.processRequest("saveWS");
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
	 * @return the list of results w.r.t the query (i.e, a set of tuples of the form (day, starting_time, ending_time, course_name), which are each stored in their own list)
	 * @throws SQLException on database access error
	 */
	public List<List<String>> selectClassroomSchedule(String classroomName) throws SQLException {
		return dbEmissary.selectClassroomSchedule(classroomName);
	}
}

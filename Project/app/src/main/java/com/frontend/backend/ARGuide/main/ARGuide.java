package com.frontend.backend.ARGuide.main;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.frontend.backend.ARGuide.webParserV3.AutoUpdateClass;
import com.frontend.backend.ARGuide.webParserV3.RunningThread;
import com.frontend.backend.ARGuide.webParserV3.SignalType;
import com.frontend.backend.ARGuide.webParserV3.WebParser;

/**
 * the object type that makes the connection to the Back-End functionalities of the ARG application
 * @author Paul-Reftu
 */
public class ARGuide extends AppCompatActivity {
	private DatabaseEmissary dbEmissary;
	private ARGProcessor argProcessor;
	
	/*
	 * path to our database
	 * default: "app/src/main/java/com/frontend/backend/ARGuide/database/faculty.db"
	 */
	private String dbPath = "app/src/main/java/com/frontend/backend/ARGuide/database/faculty.db";

	/*
    * The name of the current building
    */
    private String buildingName;
	
	/*
	 * the path to the JSON resource representing our working schedule and our building plan
	 * default for WS: "ARGuide/schedules/facultySchedule.json"
	 * default for BP: "ARGuide/buildingPlan/buildingPlan.json"
	 */
	private String schedulePath = "app/src/main/java/com/frontend/backend/ARGuide/schedules/facultySchedule.json";
	private String planPath = "app/src/main/java/com/frontend/backend/ARGuide/buildingPlan/buildingPlan.json";

	/**
     * Establish connection to the database and insert information w.r.t the given building
	 * @param buildingName the name of the building to consider
	 * @param dbPath the path to the database
	 * @param schedulePath the path to the JSON resource representing the Working Schedule
	 * @param planPath the path to the JSON resource representing the Building Plan
	 * @throws SQLException when a DB access error occurs
	 * @throws JSONResourceException upon unknown request or WSProcessor operation failure
	 */
	public ARGuide(String buildingName, String dbPath, String schedulePath, String planPath) throws SQLException, JSONResourceException {

		this.buildingName = buildingName;
		this.dbPath = dbPath;
		this.schedulePath = schedulePath;
		this.planPath = planPath;
		switch (buildingName) {
			/*
			 * the construction of the main back-end application functionalities for the Faculty of Computer Science
			 * within "Alexandru Ioan Cuza" University of Iasi building
			 */
			case "faculty_uaic_cs": {
				/* ***************** WEB PARSER CALL START ***************** */
				try {
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								AutoUpdateClass autoUpdateClass = new AutoUpdateClass("https://profs.info.uaic.ro/~orar/orar_resurse.html",
										"/data/user/0/com.frontend.frontend/files/lastUpdateTime.txt");

								if (!autoUpdateClass.runDataCollector()) {
									WebParser parser = new WebParser("https://profs.info.uaic.ro/~orar/",
											"orar_resurse.html",
											"/data/user/0/com.frontend.frontend/files/facultySchedule.json",
											"/data/user/0/com.frontend.frontend/files/sectionsNames.txt");
									parser.runParset();
									autoUpdateClass.setNewDate();
								}

								SignalType signal = new SignalType();//acest obiect trebuie utilizat de front-end pentru semnalul de update
								RunningThread runningThread = new RunningThread(autoUpdateClass, signal);
								Thread updateThread = new Thread(runningThread);
								updateThread.setDaemon(true);
								updateThread.start();
							} catch (Exception e) {
								System.out.println("problema la crearea fisirelor3\n" + e);
							}
						}
					});
					thread.start();
					thread.join();

				} catch (Exception e) {
					System.out.println("problema la crearea fisirelor1\n" + e);
				}
				/* ***************** WEB PARSER CALL END ***************** */


				List<String> tableNameList = new ArrayList<>(Arrays.asList("nodes", "edges", "images",
						"schedule", "courses"));

				/*
				 * make a new DatabaseEmissary object and establish connection to our database
				 * don't need to check if database already exists or not;
				 * the DatabaseEmissary object will automatically create the database if it doesn't exist
				 */

				File folder = new File("/data/user/0/com.frontend.frontend/files");
				File[] listOfFiles = folder.listFiles();
				System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!\n");
				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						System.out.println("File " + listOfFiles[i].getName());
					} else if (listOfFiles[i].isDirectory()) {
						System.out.println("Directory " + listOfFiles[i].getName());
					}
				}
				System.out.print("!!!!!!!!!!!!!!!!!!!!!!!!!!\n");


				this.dbEmissary = new DatabaseEmissary(this, dbPath, "faculty_uaic_cs");

				/*
				 * make a new processor for our application
				 */
				this.argProcessor = new ARGProcessor(dbEmissary, schedulePath, planPath);

				/*
				 * if the database tables that we need for our application do not already exist, create them
				 */
				if (!dbEmissary.doDbTablesExist(tableNameList))
					dbEmissary.onCreate(dbEmissary.getWritableDatabase());

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

				break;
			} // END of "faculty_uaic_cs" CASE
			/*
			 * construction of the main application's back-end functionalities for a generic building
			 */
			default: {
				this.dbPath = dbPath;
				this.planPath = planPath;

				List<String> tableNameList = new ArrayList<>(Arrays.asList("nodes", "edges", "images"));

				this.dbEmissary = new DatabaseEmissary(this.getApplicationContext(), dbPath, "standard");

				this.argProcessor = new ARGProcessor(dbEmissary, planPath);

				if (!dbEmissary.doDbTablesExist(tableNameList))
					dbEmissary.onCreate(dbEmissary.getWritableDatabase());

				if (!dbEmissary.areDbTablesFilled(tableNameList)) {
					this.argProcessor.processRequest("parseBP");
					this.argProcessor.processRequest("saveBP");
				}
			} // END of default CASE
		}
	}

	/**
	 * select all classroom names of our database
	 * @return the list of results w.r.t the query (i.e, all classrooms)
	 */
	public List<String> selectAllClassroomNames() {
		return dbEmissary.selectAllClassroomNames();
	}
	
	/**
	 * select all schedule entries related to the given classroom name (could return NULL if the given classroom does NOT exist)
	 * @param classroomName the name of the classroom whose schedule should be returned
	 * @return the list of results w.r.t the query (i.e, a set of tuples of the form (day, starting_time, ending_time, course_name), which are each stored in their own list)
	 */
	public List<List<String>> selectClassroomSchedule(String classroomName) {
		return dbEmissary.selectClassroomSchedule(classroomName);
	}

	/**
	 * selects all building nodes found in the database
	 * @return the list of all building nodes in our building graph
	 */
    public List<String> selectAllNodes() {
        return dbEmissary.selectAllNodes();
    }
}

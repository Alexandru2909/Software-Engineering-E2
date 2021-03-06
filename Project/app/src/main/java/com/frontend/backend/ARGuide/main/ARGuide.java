package com.frontend.backend.ARGuide.main;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;

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
	private String dbPath;

	/*
    * The name of the current building
    */
    private String buildingName;
	
	/*
	 * the path to the JSON resource representing our working schedule and our building plan
	 * default for WS: "app/src/main/java/com/frontend/backend/ARGuide/schedules/facultySchedule.json"
	 * default for BP: "app/src/main/java/com/frontend/backend/ARGuide/buildingPlan/buildingPlan.json"
	 */
	private String schedulePath;
	private String planPath;

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
										MyApplication.path+"/lastUpdateTime.txt");

								WebParser parser = new WebParser("https://profs.info.uaic.ro/~orar/",
										"orar_resurse.html",
										MyApplication.path+"/facultySchedule.json",
										MyApplication.path+"/sectionsNames.txt");
//								parser.runParset();
								if (!autoUpdateClass.runDataCollector()) {
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
//				try {
//					File dataBase = new File("/data/user/0/com.frontend.frontend/files/faculty.db");
//					if (dataBase.createNewFile())
//						System.out.print("'faculty.db' file was created at  " + "data/user/0/com.frontend.frontend/files/");
//				}
//				catch (Exception e)
//				{
//					System.out.println("+++++++" + e);
//				}

				File folder = new File(MyApplication.path);
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


				this.dbEmissary = new DatabaseEmissary(MyApplication.getAppContext(), dbPath, "faculty_uaic_cs");

				/*
				 * make a new processor for our application
				 */
				this.argProcessor = new ARGProcessor(dbEmissary, schedulePath, planPath);

				/*
				 * if the database tables that we need for our application do not already exist, create them
				 */
				if (!dbEmissary.doDbTablesExist(tableNameList))
					this.argProcessor.processRequest("removeBP");
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

				this.dbEmissary = new DatabaseEmissary(MyApplication.getAppContext(), dbPath, "standard");

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
     * select a classroom's id by its name
     * @param classroomName the name of the classroom
     * @return the unique id of the classroom
     */
	public Integer selectClassroomIdByName(String classroomName) {
	    return dbEmissary.selectClassroomIdByName(classroomName);
    }

    /**
     * select a classroom's name by its id
     * @param id the unique id of the classroom
     * @return the name of the classroom
     */
    public String selectClassroomNameById(Integer id) {
	    return dbEmissary.selectClassroomNameById(id);
    }

	/**
	 * selects all building nodes found in the database
	 * @return the list of all building nodes in our building graph
	 */
    public List<String> selectAllNodes() {
        return dbEmissary.selectAllNodes();
    }

    /**
     * computes the shortest path b/w two nodes, identified by the given source and destination indexes
     * @param source the id of the source vertex
     * @param destination the id of the destination vertex
     * @return the list of (nodeId, bearing) tuples ; if the direction is upwards then the bearing will be -1 and if it is downwards then it will be -2,
     *  otherwise it will be a real number \in [0, 360) (the bearing is b/w a particular node in the list and the consecutive one ; if this particular node
     *  is the last one, then its associated bearing will be null)
     * @throws DbEmissaryException upon database operation errors
     */
    public List<Pair<Integer, Double>> computeSP(Integer source, Integer destination) throws DbEmissaryException {
        return argProcessor.computeSP(source, destination);
    }
}

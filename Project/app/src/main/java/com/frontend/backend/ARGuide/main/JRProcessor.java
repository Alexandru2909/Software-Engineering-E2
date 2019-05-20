package com.frontend.backend.ARGuide.main;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.frontend.backend.ARGuide.webParserV3.DataRecord;
import com.frontend.backend.ARGuide.webParserV3.DayRecord;
import com.frontend.backend.ARGuide.webParserV3.Eveniment;

/**
 * the class whose instance is the processor of a JSONResource object
 * @author Paul-Reftu
 */
public class JRProcessor {
    private DatabaseEmissary dbEmissary;
    private String targetType;
    
    /**
     * get the reference to the already-established database helper and also the type of the JSON resource being processed (either "WS" or "BP")
     * @param dbEmissary the database helper class that allows operations on and with the database
     * @param targetType the type of the JSON resource begin processed (either WS or BP)
     */
    public JRProcessor(DatabaseEmissary dbEmissary, String targetType) {
        this.dbEmissary = dbEmissary;
        this.targetType = targetType;
    }
    
    /**
     * parse the current JSON resource and check if it is valid (w.r.t the JSON format)
     * @param jrDecoder the decoder holding information about the schedule or the building plan to be parsed
     * @throws JSONResourceException upon invalid JSON resource or decoding failure
     */
    public void parseJR (JRDecoder jrDecoder) throws JSONResourceException{
         if (jrDecoder.getJrContent() != null) {
             Gson json = new Gson();
             
             /*
              * this is not actually going to check if a JSON file is valid - an alternative to GSON should be sought
              * e.g the JSON API: https://www.json.org/
              * try parsing it through that 
              */
             try {
            	 switch (targetType) {
            	 	case "WS":
            	 		Type scheduleType = new TypeToken<LinkedList<DataRecord>>() {}.getType();
            	 		LinkedList<DataRecord> schedule = json.fromJson(jrDecoder.getJrContent(), scheduleType);
            	 		break;
            	 	case "BP":
            	 		BuildingPlan buildingPlan = json.fromJson(jrDecoder.getJrContent(), BuildingPlan.class);
            	 		break;
            	 	default:
            	 		throw new JSONResourceException("Targetted object for processing is of unknown type.");
            	 }
            	 
                 
             } catch (Exception e) {
                 throw new JSONResourceException("The data file cannot be deserialized into a '" + targetType + "' object.");
             }
         }
         else {
             throw new JSONResourceException("The data file is not valid.");
         }
    }
    
    /**
     * save the information w.r.t the current JSON resource in our database
     * @param jrDecoder the decoder holding information about the schedule or the building plan to be saved
     * @throws JSONResourceException upon failure of save operation
     * @throws SQLException upon failed database DML operations
     */
    public void saveJR (JRDecoder jrDecoder) throws JSONResourceException, SQLException {
        Gson json = new Gson();
        
        switch (targetType) {
        	/*
        	 * case when JSON resource is the Building Plan
        	 */
	        case "BP": {
	        	SQLiteDatabase db = dbEmissary.getWritableDatabase();
		 		BuildingPlan buildingPlan = json.fromJson(jrDecoder.getJrContent(), BuildingPlan.class);
		 		
		 		/*
		 		 * insert nodes
		 		 */
		 		for (BuildingPlan.Node node : buildingPlan.getNodes()) {
					db.execSQL("INSERT INTO nodes(id, floor, name, type) VALUES(" + node.getId() +
							", " + node.getFloor() + ", '" + node.getName() + "', '" + node.getType() + "')");
				}
		 		
		 		/*
		 		 * insert edges
		 		 */
		 		for (BuildingPlan.Edge edge : buildingPlan.getEdges()) {
					/*
					 * edges.id is automatically incremented starting from 1
					 * if the cost of the edges is <= 0, then set it to 1 instead
					 * otherwise, set the cost to the given value in the building plan
					 */
		 			db.execSQL("INSERT INTO edges(id1, id2, cost) VALUES(" + edge.getId_node1() + ", " +
							edge.getId_node2() + ", " + (edge.getCost() <= 0 ?  1.0 : edge.getCost()) + ")");
		 		}
		 		
		 		break;
	        } // end of 'case "BP"'
	        
		 		
		 	/*
		 	 * case when the JSON resource is the Working Schedule
		 	 */
        	case "WS": {
        		SQLiteDatabase db = dbEmissary.getWritableDatabase();
        		Type scheduleType = new TypeToken<LinkedList<DataRecord>>() {}.getType();
        		LinkedList<DataRecord> schedule = json.fromJson(jrDecoder.getJrContent(), scheduleType);
        		List<String> days = new ArrayList<>(Arrays.asList("LUNI", "MARTI", "MIERCURI",
						"JOI", "VINERI", "SAMBATA", "DUMINICA"));
        		
        		for (DataRecord data : schedule) {
					for (String day : days) {
            			if (data.getRoomRecord().containsKey(day)) {
            				DayRecord dayRecord = data.getRoomRecord().get(day);
            				
            				for (Eveniment event : dayRecord.getListaEvenimente()) {
            					String studyGroups = "";
								for (int i = 0; i < event.getListaGrupe().size(); i++) {
									if (i == 0)
										studyGroups = event.getListaGrupe().get(i);
									else
										studyGroups += ", " + event.getListaGrupe().get(i);
								}

            					db.execSQL("INSERT INTO courses(name, studyGroup) VALUES('" +
										event.getNumeEveniment() + "', '" + studyGroups + "')");

								Cursor rs = db.rawQuery("SELECT id FROM nodes WHERE name='" + data.getRoomCode() + "'", null);
								rs.moveToFirst();

            					int targetNode;
            					if (rs.getCount() < 1)
            						targetNode = rs.getInt(1);
            					else {
            						System.out.println("Node '" + data.getRoomCode() + "' does not exist in the database. "
            								+ "Its schedule has therefore NOT been introduced.");
            						continue;
            					}

            					rs.close();

            					rs = db.rawQuery("SELECT id FROM courses WHERE name='" + event.getNumeEveniment() + "'", null);
            					rs.moveToFirst();

            					int targetCourse = rs.getInt(1);
            					if (rs.getCount() < 1)
            						targetCourse = rs.getInt(1);
            					else {
            						System.out.println("Course '" + data.getRoomCode() + "' does not exist in the database. "
            								+ "Its schedule has therefore NOT been introduced.");
            						continue;
            					}

            					rs.close();

            					db.execSQL("INSERT INTO schedule(node_id, course_id, starting_time, ending_time, day) " +
										"VALUES(" + targetNode + ", " + targetCourse + ", " +  event.getOraStart().toString()
										+ ", " + event.getOraFinal().toString() + ", '" + day + "')");
            				}
            			}
            		}
        		}
        		
        		break;
        	} // end of 'case "WS"'
        		
        	/*
        	 * error case - JSON resource of unexpected type
        	 */
    	 	default:
    	 		throw new JSONResourceException("Targeted object for processing is of unknown type.");
        }
    }
    
    /**
     * update the information w.r.t the current JSON resource in our database
     * @param jrDecoder the decoder holding information about the schedule or the building plan that is to replace the old one (if it existed)
     * @throws JSONResourceException upon failure of update operation
     * @throws SQLException upon failed database DML operations
     */
    public void updateJR (JRDecoder jrDecoder) throws JSONResourceException, SQLException {
        switch (targetType) {
	        case "BP": {
	        	SQLiteDatabase db = dbEmissary.getWritableDatabase();
	        	List<String> tableNames = new ArrayList<>(Arrays.asList("schedule", "edges", "images",
						"nodes", "courses"));
	        	
	        	for (String tableName : tableNames)
					db.execSQL("DELETE FROM " + tableName);
	        	
	        	saveJR(jrDecoder);
	        	
	        	break;
	        }
	        
	        case "WS": {
	        	SQLiteDatabase db = dbEmissary.getWritableDatabase();
	        	List<String> tableNames = new ArrayList<>(Arrays.asList("schedule", "courses"));
	        	
	        	for (String tableName : tableNames)
	        		db.execSQL("DELETE FROM " + tableName);
	        	
	        	saveJR(jrDecoder);
	        	
	        	break;
	        }
	        
	        default:
	        	throw new JSONResourceException("Targetted object for processing is of unknown type.");
        }
    }
    
    /**
     * remove the information w.r.t the current JSON resource from our database
     * @throws JSONResourceException upon failure of remove operation
     * @throws SQLException upon failed database DELETE operation on table(s)
     */
    public void removeJR () throws JSONResourceException, SQLException {
        switch (targetType) {
	        case "BP": {
	        	SQLiteDatabase db = dbEmissary.getWritableDatabase();
	        	List<String> tableNames = new ArrayList<>(Arrays.asList("schedule", "edges", "images",
						"nodes", "courses"));
	        	
	        	for (String tableName : tableNames)
	        		db.execSQL("DELETE FROM " + tableName);
	        	
	        	break;
	        }
	        
	        case "WS": {
				SQLiteDatabase db = dbEmissary.getWritableDatabase();
	        	List<String> tableNames = new ArrayList<>(Arrays.asList("schedule", "courses"));

				for (String tableName : tableNames)
					db.execSQL("DELETE FROM " + tableName);
	        	
	        	break;
	        }
	        
	        default:
	        	throw new JSONResourceException("Targetted object for processing is of unknown type.");
        }
    }

}

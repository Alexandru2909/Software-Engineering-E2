package com.frontend.backend.ARGuide.main;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.lang.reflect.Type;
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
     */
    public void saveJR (JRDecoder jrDecoder) throws JSONResourceException {
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
		 		    SQLiteStatement statement = db.compileStatement(
                            "INSERT INTO nodes(id, floor, name, type) VALUES(?, ?, ?, ?)"
                    );

		 		    statement.bindLong(1, node.getId());
		 		    statement.bindLong(2, node.getFloor());
		 		    statement.bindString(3, node.getName());
		 		    statement.bindString(4, node.getType());

		 		    statement.executeInsert();
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
					SQLiteStatement statement = db.compileStatement(
                            "INSERT INTO edges(id1, id2, cost) VALUES(?, ?, ?)"
                    );

					statement.bindLong(1, edge.getId_node1());
					statement.bindLong(2, edge.getId_node2());
					statement.bindDouble(3, (edge.getCost() <= 0 ? 1.0 : edge.getCost()));

					statement.executeInsert();
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

            				if (dayRecord == null)
                                continue;

            				for (Eveniment event : dayRecord.getListaEvenimente()) {
            					String studyGroups = "";
								for (int i = 0; i < event.getListaGrupe().size(); i++) {
									if (i == 0)
										studyGroups = event.getListaGrupe().get(i);
									else
										studyGroups += ", " + event.getListaGrupe().get(i);
								}

								SQLiteStatement statement = db.compileStatement(
                                        "INSERT INTO courses(name, studyGroup) VALUES(?, ?)"
                                );
								statement.bindString(1, event.getNumeEveniment());
								statement.bindString(2, studyGroups);
								statement.executeInsert();

								Cursor rs = db.rawQuery("SELECT id FROM nodes WHERE name=?", new String[] {data.getRoomCode()});
								rs.moveToFirst();

            					int targetNode;
            					if (rs.getCount() >= 1)
            						targetNode = rs.getInt(0);
            					else {
            						System.out.println("Node '" + data.getRoomCode() + "' does not exist in the database. "
            								+ "Its schedule has therefore NOT been introduced.");
            						continue;
            					}

            					rs.close();

            					rs = db.rawQuery("SELECT id FROM courses WHERE name=?", new String[] {event.getNumeEveniment()});
            					rs.moveToFirst();

            					int targetCourse;
            					if (rs.getCount() >= 1)
            						targetCourse = rs.getInt(0);
            					else {
            						System.out.println("Course '" + event.getNumeEveniment() + "' does not exist in the database. "
            								+ "Its schedule has therefore NOT been introduced.");
            						continue;
            					}

            					rs.close();


            					statement = db.compileStatement(
                                        "INSERT INTO schedule(node_id, course_id, starting_time, ending_time, day) VALUES(?, ?, ?, ?, ?)"
                                );
            					statement.bindLong(1, targetNode);
            					statement.bindLong(2, targetCourse);
            					statement.bindString(3, event.getOraStart());
            					statement.bindString(4, event.getOraFinal());
            					statement.bindString(5, day);

                                statement.executeInsert();
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
     */
    public void updateJR (JRDecoder jrDecoder) throws JSONResourceException {
        switch (targetType) {
	        case "BP": {
	        	SQLiteDatabase db = dbEmissary.getWritableDatabase();
	        	List<String> tableNames = new ArrayList<>(Arrays.asList("schedule", "edges", "images",
						"nodes", "courses"));
	        	
	        	for (String tableName : tableNames)
	        	    db.delete(tableName, null, null);
	        	
	        	saveJR(jrDecoder);
	        	
	        	break;
	        }
	        
	        case "WS": {
	        	SQLiteDatabase db = dbEmissary.getWritableDatabase();
	        	List<String> tableNames = new ArrayList<>(Arrays.asList("schedule", "courses"));
	        	
	        	for (String tableName : tableNames)
	        	    db.delete(tableName, null, null);
	        	
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
     */
    public void removeJR () throws JSONResourceException {
        switch (targetType) {
	        case "BP": {
	        	SQLiteDatabase db = dbEmissary.getWritableDatabase();
	        	List<String> tableNames = new ArrayList<>(Arrays.asList("schedule", "edges", "images",
						"nodes", "courses"));
	        	
	        	for (String tableName : tableNames)
	        	    db.delete(tableName, null, null);
	        	
	        	break;
	        }
	        
	        case "WS": {
				SQLiteDatabase db = dbEmissary.getWritableDatabase();
	        	List<String> tableNames = new ArrayList<>(Arrays.asList("schedule", "courses"));

				for (String tableName : tableNames)
                    db.delete(tableName, null, null);
	        	
	        	break;
	        }
	        
	        default:
	        	throw new JSONResourceException("Targetted object for processing is of unknown type.");
        }
    }

}

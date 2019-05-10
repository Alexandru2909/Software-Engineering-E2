package main;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

import main.BuildingPlan.Edge;
import main.BuildingPlan.Node;
import webParserV3.Schedule;
import webParserV3.DataRecord;
import webParserV3.DayRecord;
import webParserV3.Eveniment;

/**
 * the class whose instance is the processor of a JSONResource object
 * @author Paul-Reftu
 *
 */
public class JRProcessor {
    private Connection connection;
    private String targetType;
    
    /**
     * get the reference to the already-established connection to our database and also the type of the JSON resource being processed (either "WS" or "BP") 
     * @param conn the Connection object holding information w.r.t our current database connection
     * @param targetType the type of the JSON resource begin processed (either WS or BP)
     */
    public JRProcessor(Connection conn, String targetType) {
        this.connection = conn;
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
            	 		Schedule schedule = json.fromJson(jrDecoder.getJrContent(), Schedule.class);
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
		 		BuildingPlan buildingPlan = json.fromJson(jrDecoder.getJrContent(), BuildingPlan.class);
		 		String query;
		 		PreparedStatement pStatement;
		 		
		 		/*
		 		 * insert nodes
		 		 */
		 		for (Node node : buildingPlan.getNodes()) {
	    	 		query = "INSERT INTO nodes(id, floor, name, type) VALUES(?, ?, ?, ?)";
	    	 		pStatement = connection.prepareStatement(query);
	    	 		pStatement.setInt(1, node.getId());
	    	 		pStatement.setInt(2, node.getFloor());
	    	 		pStatement.setString(3, node.getName());
	    	 		pStatement.setString(4, node.getType());
	    	 		pStatement.executeUpdate();
		 		}
		 		
		 		/*
		 		 * insert edges
		 		 */
		 		for (Edge edge : buildingPlan.getEdges()) {
		 			query = "INSERT INTO edges(id, id1, id2, cost) VALUES(?, ?, ?, ?)";
		 			pStatement = connection.prepareStatement(query);
		 			pStatement.setNull(1, Types.INTEGER); //edges.id is automatically incremented starting from 1
		 			pStatement.setInt(2, edge.getId_node1());
		 			pStatement.setInt(3, edge.getId_node2());
		 			pStatement.setDouble(4, edge.getCost());
		 			pStatement.executeUpdate();
		 		}
		 		
		 		break;
	        } // end of 'case "BP"'
	        
		 		
		 	/*
		 	 * case when the JSON resource is the Working Schedule
		 	 */
        	case "WS": {
        		Schedule schedule = json.fromJson(jrDecoder.getJrContent(), Schedule.class);
        		List<String> days = new ArrayList<String>();
        		days.addAll(Arrays.asList("LUNI", "MARTI", "MIERCURI", "JOI", "VINERI", "SAMBATA", "DUMINICA"));
        		
        		for (DataRecord data : schedule.getRoomSchedules()) {
        			String query;
        			PreparedStatement pStatement;
        			ResultSet rs;
            		
            		for (String day : days) {
            			if (data.getRoomRecord().containsKey(day)) {
            				DayRecord dayRecord = data.getRoomRecord().get(day);
            				
            				for (Eveniment event : dayRecord.getListaEvenimente()) {
            					String studyGroups = "";
                				query = "INSERT INTO courses(id, name, studyGroup) VALUES(?, ?, ?)";
                				pStatement = connection.prepareStatement(query);
                				pStatement.setNull(1, Types.INTEGER); //courses.id is automatically incremented starting from 1
                				pStatement.setString(2, event.getNumeEveniment());
                					
                				for (int i = 0; i < event.getListaGrupe().size(); i++) { 
                					if (i == 0)
                						studyGroups = event.getListaGrupe().get(i);
                					else
                						studyGroups += ", " + event.getListaGrupe().get(i);
                				}
                					
                				pStatement.setString(3, studyGroups);
                				pStatement.executeUpdate();
            					
            					query = "SELECT id FROM nodes WHERE name=" + data.getRoomCode();
            					Statement statement = connection.createStatement();
            					rs = statement.executeQuery(query);
            					rs.next();
            					int targetNode = rs.getInt(1);
            					
            					query = "SELECT id FROM courses WHERE name=" + event.getNumeEveniment();
            					statement = connection.createStatement();
            					rs = statement.executeQuery(query);
            					rs.next();
            					int targetCourse = rs.getInt(1);
            					
            					query = "INSERT INTO schedule(node_id, course_id, starting_time, ending_time, day) " +
            							"VALUES(?, ?, ?, ?, ?)";
            					pStatement = connection.prepareStatement(query);
            					pStatement.setInt(1, targetNode);
            					pStatement.setInt(2, targetCourse);
            					pStatement.setString(3, event.getOraStart().toString());
            					pStatement.setString(4, event.getOraFinal().toString());
            					pStatement.setString(5, day);
            					pStatement.executeUpdate();
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
    	 		throw new JSONResourceException("Targetted object for processing is of unknown type.");	
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
	        	List<String> tableNames = new ArrayList<String>();
	        	tableNames.addAll(Arrays.asList("schedule", "edges", "images", "nodes", "courses"));
	        	
	        	for (String tableName : tableNames) {
	        		String query = "DELETE FROM " + tableName;
		        	PreparedStatement pStatement = connection.prepareStatement(query);
		        	pStatement.executeUpdate();
	        	}
	        	
	        	saveJR(jrDecoder);
	        	
	        	break;
	        }
	        
	        case "WS": {
	        	List<String> tableNames = new ArrayList<String>();
	        	tableNames.addAll(Arrays.asList("schedule", "courses"));
	        	
	        	for (String tableName : tableNames) {
	        		String query = "DELETE FROM " + tableName;
		        	PreparedStatement pStatement = connection.prepareStatement(query);
		        	pStatement.executeUpdate();
	        	}
	        	
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
	        	List<String> tableNames = new ArrayList<String>();
	        	tableNames.addAll(Arrays.asList("schedule", "edges", "images", "nodes", "courses"));
	        	
	        	for (String tableName : tableNames) {
	        		String query = "DELETE FROM " + tableName;
		        	PreparedStatement pStatement = connection.prepareStatement(query);
		        	pStatement.executeUpdate();
	        	}
	        	
	        	break;
	        }
	        
	        case "WS": {
	        	List<String> tableNames = new ArrayList<String>();
	        	tableNames.addAll(Arrays.asList("schedule", "courses"));
	        	
	        	for (String tableName : tableNames) {
	        		String query = "DELETE FROM " + tableName;
		        	PreparedStatement pStatement = connection.prepareStatement(query);
		        	pStatement.executeUpdate();
	        	}
	        	
	        	break;
	        }
	        
	        default:
	        	throw new JSONResourceException("Targetted object for processing is of unknown type.");
        }
    }

}

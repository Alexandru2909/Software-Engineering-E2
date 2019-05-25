/**
 * 
 */
package com.frontend.backend.ARGuide.main;

import android.util.Pair;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * the main processor of the ARG application's Back-End module
 * @author Paul-Reftu
 *
 */
public class ARGProcessor {
	private DatabaseEmissary dbEmissary;
	private JSONResource bpResource;
	private JSONResource wsResource;
	private PathGenerator pathGenerator;

	/**
	 * construct the JSON resource objects w.r.t the Building Plan and the Working Schedule
	 * @param dbEmissary the database helper class that allows operations on and with the database
	 * @param planPath the path to the JSON resource representing our building plan
	 * @param schedulePath the path to the JSON resource representing our schedule
	 */
	public ARGProcessor(final DatabaseEmissary dbEmissary, final String schedulePath, final String planPath) {
		this.dbEmissary = dbEmissary;
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try
				{
					bpResource = new JSONResource(dbEmissary, planPath, "BP");
					wsResource = new JSONResource(dbEmissary, schedulePath, "WS");
					pathGenerator = new PathGenerator(dbEmissary);
				}catch (JSONResourceException e)
				{
					e.printStackTrace();
				}

			}
		});
		thread.start();
		try{
			thread.join();
		}
		catch (Exception e)
		{
			System.out.println(e);
		}

	}

	/**
	 * construct the JSON resource object w.r.t the Building Plan
	 * @param dbEmissary the database helper class that allows operations on and with the database
	 * @param planPath the path to the JSON resource representing our building plan
	 * @throws JSONResourceException when the JRDecoder object fails the decoding process of the schedule
	 */
	public ARGProcessor(DatabaseEmissary dbEmissary, String planPath) throws JSONResourceException {
		this.dbEmissary = dbEmissary;
		bpResource = new JSONResource(dbEmissary, planPath, "BP");
		pathGenerator = new PathGenerator(dbEmissary);
	}
	
	/**
	 * process the given request (which is related to the Building Plan, the Working Schedule and/or certain operations on them)
	 * @param request the request to be processed
	 * @throws JSONResourceException upon unknown request w.r.t the working schedule or the building plan resource, upon invalid JSON resource or decoding failure
	 * @throws SQLException upon failed database DML operations
	 */
	public void processRequest(String request) throws JSONResourceException, SQLException {
		switch (request) {
			case "parseWS":
				wsResource.sendRequest("parse");
				break;
			case "saveWS":
				wsResource.sendRequest("save");
				break;
			case "updateWS":
				wsResource.sendRequest("update");
				break;
			case "removeWS":
				wsResource.sendRequest("remove");
				break;
			
			case "parseBP":
				bpResource.sendRequest("parse");
				break;
			case "saveBP":
				bpResource.sendRequest("save");
				break;
			case "updateBP":
				bpResource.sendRequest("update");
				break;
			case "removeBP":
				bpResource.sendRequest("remove");
				break;
				
			default:
				throw new JSONResourceException("Unknown request!");
		
		}
	}

    /**
     * compute the shortest path b/w two given nodes, each point in the path being associated with the bearing
     *  b/w that node and the consecutive one in the path
     * @param startVertex the index of the node representing the starting point
     * @param endVertex the index of the node representing the destination
     * @return the list of pairs <nodeId, bearing> of the shortest path
     * @throws DbEmissaryException upon database operation errors
     */
	public List<Pair<Integer, Double>> computeSP(Integer startVertex, Integer endVertex) throws DbEmissaryException {
	    List<Integer> sp = pathGenerator.dijkstra(startVertex, endVertex);

	    if (sp.size() == 1)
	        new ArrayList<>(Arrays.asList(new Pair<Integer, Double>(sp.get(0), null)));

	    List<Pair<Integer, Double>> ret = new ArrayList<>();
	    Double upDirectionCode = -1.0;
	    Double downDirectionCode = -2.0;

	    for (Integer i = 0; i < sp.size(); i++) {
	        if (i + 1 >= sp.size()) {
                ret.add(new Pair<Integer, Double>(sp.get(i), null));
                break;
            }

	        Integer v1 = sp.get(i);
	        Integer v2 = sp.get(i + 1);

            Pair<Double, Double> v1Coord = dbEmissary.selectLatLonById(v1);
            Pair<Double, Double> v2Coord = dbEmissary.selectLatLonById(v2);
            String v2Type = dbEmissary.selectNodeTypeById(v2);
            Integer v1Floor = dbEmissary.selectNodeFloorById(v1);
            Integer v2Floor = dbEmissary.selectNodeFloorById(v2);

            if (v2Type.equalsIgnoreCase("Stairs") || v2Type.equalsIgnoreCase("Elevator")) {
                if (v1Floor < v2Floor) {
                    ret.add(new Pair<>(v1, upDirectionCode));
                    continue;
                }
                else if (v1Floor > v2Floor) {
                    ret.add(new Pair<>(v1, downDirectionCode));
                    continue;
                }
            }

            ret.add(
                    new Pair<>(
                            v1, computeBearing(v1Coord, v2Coord)
                    )
            );

        }

	    return ret;
    }

    /**
     * compute the bearing b/w a starting and an ending geolocation
     * @param coordStart the (lat, lon) tuple of the start geolocation
     * @param coordEnd the (lat, lon) tuple of the end geolocation
     * @return the bearing b/w given two geographic coordinates
     */
    private double computeBearing(Pair<Double, Double> coordStart, Pair<Double, Double> coordEnd) {
        double thetaEins = Math.toRadians(coordStart.first);
        double thetaZwei = Math.toRadians(coordEnd.first);
        double deltaZwei = Math.toRadians(coordEnd.second - coordStart.second);

        double Y = Math.sin(deltaZwei) * Math.cos(thetaZwei);
        double X = Math.cos(thetaEins) * Math.sin(thetaZwei) -
                Math.sin(thetaEins) * Math.cos(thetaZwei) *
                        Math.cos(deltaZwei);

        return ((int)Math.toDegrees(Math.atan2(Y, X)) + 360) % 360;
    }


    /*
     * Paul-Reftu: I do not yet trust the deployment of the tweaked BFS algorithm
     * for the SP computation. More tests will be done before that will happen.
     */
//	/**
//	 * computes the shortest path b/w two given nodes in the building graph
//	 * @param startVertex the starting vertex
//	 * @param endVertex the destination vertex
//	 * @return the shortest path from a source to a destination vertex
//	 * @throws JSONResourceException upon null vertex parameters
//	 */
//	public List<Integer> computeSP(Integer startVertex, Integer endVertex) throws JSONResourceException {
//		if (startVertex == null || endVertex == null)
//			throw new JSONResourceException("Invalid start/end vertices for shortest path computation!");
//
//		BuildingPlan bp = new BuildingPlan();
//		boolean equalCost = true;
//
//		for(BuildingPlan.Edge e : bp.getEdges())
//		{
//			if( (e.getId_node1().getCost()) != (e.getId_node2()).getCost())
//				equalCost = false;
//		}
//
//		if(equalCost == false)
//			return pathGenerator.dijkstra(startVertex, endVertex);
//		else
//		        return path.generator.bfsShortestPath(startVertex, endVertex);
//	}

}

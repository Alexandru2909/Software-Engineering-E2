/**
 * 
 */
package com.frontend.backend.ARGuide.main;

import java.sql.SQLException;

/**
 * the main processor of the ARG application's Back-End module
 * @author Paul-Reftu
 *
 */
public class ARGProcessor {
	private DatabaseEmissary dbEmissary;
	private JSONResource bpResource;
	private JSONResource wsResource;

	/**
	 * construct the JSON resource objects w.r.t the Building Plan and the Working Schedule
	 * @param dbEmissary the database helper class that allows operations on and with the database
	 * @param planPath the path to the JSON resource representing our building plan
	 * @param schedulePath the path to the JSON resource representing our schedule
	 * @throws JSONResourceException when the JRDecoder object fails the decoding process of the schedule
	 */
	public ARGProcessor(DatabaseEmissary dbEmissary, String schedulePath, String planPath) throws JSONResourceException {
		this.dbEmissary = dbEmissary;
		bpResource = new JSONResource(dbEmissary, planPath, "BP");
		wsResource = new JSONResource(dbEmissary, schedulePath, "WS");
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
}

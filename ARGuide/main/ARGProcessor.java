/**
 * 
 */
package main;

import java.sql.Connection;

/**
 * @author Paul-Reftu
 *
 */
public class ARGProcessor {
	private JSONResource wsResource;
	/*
	 * Building Plan resource not yet deployed.
	 * 
		private JSONResource bpResource;
	*/

	/**
	 * @param conn the Connection object holding information w.r.t our current database connection
	 * @param schedulePath the path to the JSON resource representing our schedule
	 * @param dbWSPackageName the name of the PL/SQL package in our DB that handles the working schedule operations
	 * @param dbWSPackageMethodsExtension the extension of the name of the operations in the working schedule PL/SQL package (e.g storeSchedule(), saveSchedule(), ..., where 'Schedule' is our extension)
	 * @param planPath the path to the JSON resource representing our building plan
	 * @param dbBPPackageName the name of the PL/SQL package in our DB that handles the building plan operations
	 * @param dbBPPackageMethodsExtension the extension of the name of the operations in the building plan PL/SQL package (e.g storeBuildingPlan(), saveBuildingPlan(), ..., where 'BuildingPlan' is our extension)
	 * @throws JSONResourceException when the JRDecoder object fails the decoding process o the schedule
	 */
	public ARGProcessor(Connection conn, String schedulePath, String dbWSPackageName, String dbWSPackageMethodsExtension,
			String planPath, String dbBPPackageName, String dbBPPackageMethodsExtension) throws JSONResourceException {
		wsResource = new JSONResource(conn, schedulePath, dbWSPackageName, dbWSPackageMethodsExtension);
		/*
		 * Building Plan resource not yet deployed.
		 * 
			bpResource = new JSONResource(conn, planPath, dbBPPackageName, dbBPPackageMethodsExtension);
		*/
	}
	
	/**
	 * @param request the request to be processed
	 * @throws JSONResourceException upon unknown request w.r.t the working schedule or the building plan resource, upon invalid JSON resource or decoding failure
	 */
	public void processRequest(String request) throws JSONResourceException {
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
			
			/*
			 * Building Plan resource not yet deployed.
			 * 
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
			*/
				
			default:
				throw new JSONResourceException("Unknown request!");
		
		}
	}
}

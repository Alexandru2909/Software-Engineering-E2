package IpClasses;

public class WorkingSchedule {
	private WSDecoder wsDecoder;
    protected ScheduleStatus scheduleStatus;
    private String dbConnPath;
    private String dbUsername;
    private String dbPassword;
    
    /**
     * @param schedulePath the path to the JSON resource representing the schedule
     * @param dbConnPath the path of the Oracle database connection
     * @param dbUsername the username for the database log-in
     * @param dbPassword the password of the user
     */
    public WorkingSchedule(String schedulePath, String dbConnPath, String dbUsername, String dbPassword) {
    	this.wsDecoder = new WSDecoder(schedulePath);
    	this.dbConnPath = dbConnPath;
    	this.dbUsername = dbUsername;
    	this.dbPassword = dbPassword;
    }
    
    /**
     * @return the status of the last operation of the WSProcessor
     */
    public ScheduleStatus getStatus() {
    	return scheduleStatus;
    }
    
    /**
     * @param request the request to be transmitted to the WSProcessor
     * @throws WorkingScheduleException upon unknown request or WSProcessor operation failure
     */
    public void sendRequest (String request) throws WorkingScheduleException {    	
    	WSProcessor wsProcessor = new WSProcessor(dbConnPath, dbUsername, dbPassword);
    	
	    switch (request) {
			case "parse":
		    	wsProcessor.parseWS(wsDecoder);
		    	break;
		    case "save":
		    	wsProcessor.saveWS(wsDecoder);
		    	break;
		    case "update":
		    	wsProcessor.updateWS(wsDecoder);
		    	break;
		    case "remove":
		    	wsProcessor.removeWS();
		    	break;
		    default: 
		    	throw new WorkingScheduleException("Unknown request!");
		    	break;
	    }
    	
	    return;
    }
}

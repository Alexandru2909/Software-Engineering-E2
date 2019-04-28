package main;

import java.sql.Connection;

public class JSONResource {
	private JRDecoder jrDecoder;
    private JRProcessor jrProcessor;
    
    /**
     * 
     * @param conn the Connection object holding information w.r.t our current database connection
     * @param resourcePath the path to the JSON resource representing our working schedule or our building plan
     * @param dbPackageName the name of the PL/SQL package that handles what our JSON resource represents
     * @param dbPackageMethodsExtension the method name extension in our PL/SQL database package that allows us to know which methods to use (e.g for extension 'Schedule', we could use storeSchedule(), saveSchedule(), etc)
     * @throws JSONResourceException when the JRDecoder object fails the decoding process
     */
    public JSONResource(Connection conn, String resourcePath, String dbPackageName, String dbPackageMethodsExtension) throws JSONResourceException {
    	this.jrDecoder = new JRDecoder(resourcePath);
    	this.jrProcessor = new JRProcessor(conn, dbPackageName, dbPackageMethodsExtension);
    }
    
    /**
     * @return the status of the last operation of the JRProcessor
     */
    public JRProcessorStatus getStatus() {
    	return jrProcessor.getStatus();
    }
    
    /**
     * @param request the request to be transmitted to the JRProcessor
     * @throws JSONResourceException upon unknown request or JRProcessor operation failure
     */
    public void sendRequest (String request) throws JSONResourceException {    	
	    switch (request) {
			case "parse":
		    	jrProcessor.parseJR(jrDecoder);
		    	break;
		    case "save":
		    	jrProcessor.saveJR(jrDecoder);
		    	break;
		    case "update":
		    	jrProcessor.updateJR(jrDecoder);
		    	break;
		    case "remove":
		    	jrProcessor.removeJR();
		    	break;
		    default: 
		    	throw new JSONResourceException("Unknown request!");
	    }
    	
	    return;
    }
}

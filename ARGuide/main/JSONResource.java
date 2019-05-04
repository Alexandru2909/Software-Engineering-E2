package main;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * class whose instance holds methods that handle the Working Schedule and Building Plan resources in JSON format
 * @author Paul-Reftu
 *
 */
public class JSONResource {
	private JRDecoder jrDecoder;
    private JRProcessor jrProcessor;
    private String type;
    
    /**
     * instantiate the JRDecoder and JRProcessor
     * @param conn the Connection object holding information w.r.t our current database connection
     * @param resourcePath the path to the JSON resource representing our working schedule or our building plan
     * @param type the type of the JSON resource (either WS or BP)
     * @throws JSONResourceException when the JRDecoder object fails the decoding process
     */
    public JSONResource(Connection conn, String resourcePath, String type) throws JSONResourceException {
    	this.jrDecoder = new JRDecoder(resourcePath);
    	this.jrProcessor = new JRProcessor(conn, type);
    	this.type = type;
    }
    
    /**
     * get the type of the JSON resource (either "WS" or "BP")
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * set the type of the JSON resource
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
    
    /**
     * send a specific request to the JRProcessor
     * @param request the request to be transmitted to the JRProcessor
     * @throws JSONResourceException upon unknown request or JRProcessor operation failure
     * @throws SQLException upon failed database DML operations
     */
    public void sendRequest (String request) throws JSONResourceException, SQLException {    	
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

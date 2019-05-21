package com.frontend.backend.ARGuide.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
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
     * @param dbEmissary the database helper class that allows operations on and with the database
     * @param resourcePath the path to the JSON resource representing our working schedule or our building plan
     * @param type the type of the JSON resource (either WS or BP)
     * @throws JSONResourceException when the JRDecoder object fails the decoding process or when we fail to create the BP resource in case it doesn't already exist
     * at the specified 'resourcePath' parameter
     */
    public JSONResource(DatabaseEmissary dbEmissary, String resourcePath, String type) throws JSONResourceException {
        /*
         * check if the JSON resource for the Building Plan exists at the specified path
         */
        if (type.equalsIgnoreCase("BP")) {
            File bp = new File(resourcePath);

            /*
             * if it does not - then take it from an URL that has this JSON resource
             */
            if (!bp.exists()) {
                String bpUrl = "https://raw.githubusercontent.com/Alexandru2909/Software-Engineering-E2/master/Project/app/src/main/java/com/frontend/backend/ARGuide/buildingPlan/jsonFormat/buildingPlan.json";

                try {
                    /*
                     * read the content of that URL - which should contain a pure JSON resource
                     */
                    InputStream in = new URL(bpUrl).openStream();
                    BufferedReader in2 = new BufferedReader(new InputStreamReader(in, Charset.forName("UTF-8")));

                    StringBuilder sb = new StringBuilder();
                    int readChar;

                    while ((readChar = in2.read()) != -1)
                        sb.append((char) readChar);

                    in2.close();
                    in.close();

                    String bpJsonString = sb.toString();

                    if (!bp.createNewFile())
                        throw new JSONResourceException("Failure on attempting to create JSON resource for the Building Plan at " +
                                resourcePath + "!");

                    /*
                     * finally, write the content to the newly created file representing the BP JSON resource
                     */
                    FileWriter out = new FileWriter(bp);
                    out.write(bpJsonString);
                    out.close();
                } catch (MalformedURLException e) {
                    throw new JSONResourceException("Malformed URL w.r.t the building plan! (" + bpUrl + ")");
                } catch (IOException e) {
                    throw new JSONResourceException("IO exception on opening stream from URL for the Building Plan resource: " + bpUrl);
                }

            }
        }
    	this.jrDecoder = new JRDecoder(resourcePath);
    	this.jrProcessor = new JRProcessor(dbEmissary, type);
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

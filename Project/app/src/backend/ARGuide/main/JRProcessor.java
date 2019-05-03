package main;
import com.google.gson.Gson;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Types;

import webParserV2.DataRecord;

public class JRProcessor {
    private Connection connection;
    private JRProcessorStatus status;
    private String dbPackageName;
    private String dbPackageMethodsExtension;
    
    /**
     * @param conn the Connection object holding information w.r.t our current database connection
     * @param dbPackageName the name of the PL/SQL package that handles what our JSON resource represents
     * @param dbPackageMethodsExtension the method name extension in our PL/SQL database package that allows us to know which methods to use (e.g for extension 'Schedule', we could use storeSchedule(), saveSchedule(), etc)
     */
    public JRProcessor(Connection conn, String dbPackageName, String dbPackageMethodsExtension) {
        this.connection = conn;
        this.dbPackageName = dbPackageName;
        this.dbPackageMethodsExtension = dbPackageMethodsExtension;
    }
    
    /**
     * @param jrDecoder the decoder holding information about the schedule or the building plan to be parsed
     * @throws JSONResourceException upon invalid JSON resource or decoding failure
     */
    public void parseJR (JRDecoder jrDecoder) throws JSONResourceException{
         if (jrDecoder.getJrContent() != null) {
             Gson json = new Gson();
             
             try {
                 DataRecord dataRecord = json.fromJson(jrDecoder.getJrContent(), DataRecord.class);
             } catch (Exception e) {
            	 status = JRProcessorStatus.WS_PROCESSOR_FAILURE;
                 throw new JSONResourceException("Schedule-ul nu poate fi convertit la DataRecord");
             }
         }
         else {
             throw new JSONResourceException("Datele trimise sunt invalide");
         }
    }
    
    /**
     * @param jrDecoder the decoder holding information about the schedule or the building plan to be saved
     * @throws JSONResourceException upon failure of save operation
     */
    public void saveJR (JRDecoder jrDecoder) throws JSONResourceException {
        status = JRProcessorStatus.WS_PROCESSOR_SUCCESS;
        try {
            CallableStatement statement = connection.prepareCall("{call " + dbPackageName + ".store" + dbPackageMethodsExtension + "(?,?)}");
            statement.registerOutParameter(1, Types.VARCHAR);
            statement.registerOutParameter(2, Types.VARCHAR);
            statement.setString(1,jrDecoder.getJrFilePath());
            statement.setString(2,jrDecoder.getJrFileName());
            Boolean result = statement.execute();
            
            if (result == false) {
                this.status=JRProcessorStatus.WS_PROCESSOR_SUCCESS;
            }
        } catch (Exception e) {
        	status = JRProcessorStatus.WS_PROCESSOR_FAILURE;
            throw new JSONResourceException("Problema la apelarea functiei saveWorkingSchedule");
        }
    }
    
    /**
     * @param jrDecoder the decoder holding information about the schedule or the building plan that is to replace the old one (if it existed)
     * @throws JSONResourceException upon failure of update operation
     */
    public void updateJR (JRDecoder jrDecoder) throws JSONResourceException {
        status = JRProcessorStatus.WS_PROCESSOR_SUCCESS;
        
        try {
            CallableStatement statement = connection.prepareCall("{call " + dbPackageName + ".update" + dbPackageMethodsExtension + "(?,?)}");
            statement.registerOutParameter(1, Types.VARCHAR);
            statement.registerOutParameter(2,Types.VARCHAR);
            statement.setString(1,jrDecoder.getJrFilePath());
            statement.setString(2,jrDecoder.getJrFileName());
            Boolean result = statement.execute();
            
            if (result == true){
                this.status = JRProcessorStatus.WS_PROCESSOR_SUCCESS;
            }
        } catch (Exception e) {
        	status = JRProcessorStatus.WS_PROCESSOR_FAILURE;
            throw new JSONResourceException("Problema la apelarea functiei saveWorkingSchedule");
        }
    }
    
    /**
     * @throws JSONResourceException upon failure of remove operation
     */
    public void removeJR () throws JSONResourceException {
        status = JRProcessorStatus.WS_PROCESSOR_SUCCESS;
        try {
            CallableStatement statement = connection.prepareCall("{call " + dbPackageName + ".remove" + dbPackageMethodsExtension + "()}");
            Boolean result = statement.execute();
            
            if (result == true){
                this.status=JRProcessorStatus.WS_PROCESSOR_SUCCESS;
            }
        } catch (Exception e) {
        	status = JRProcessorStatus.WS_PROCESSOR_FAILURE;
            throw new JSONResourceException("Problema la apelarea functiei saveWorkingSchedule");
        }
    }

	/**
	 * @return the status
	 */
	public JRProcessorStatus getStatus() {
		return status;
	}

	/**
	 * @param status the scheduleStatus to set
	 */
	public void setStatus(JRProcessorStatus status) {
		this.status = status;
	}
}

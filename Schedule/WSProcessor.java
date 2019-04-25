package IpClasses;
import WebParserV2.*;
import com.google.gson.Gson;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Types;

public class WSProcessor extends WorkingSchedule{
    private Connection connection;
    
    /**
     * @param connectionPath the path of the connection to the Oracle database
     * @param username the username used to log into the database
     * @param password the password of the user
     * @throws WorkingScheduleException upon failed connection to the DB
     */
    public WSProcessor(String connectionPath, String username, String password) throws WorkingScheduleException {
        try {
        	Class.forName("oracle.jdbc.driver.OracleDriver");
        	
            connection = DriverManager.getConnection(connectionPath, username, password);
        } catch (Exception e) {
            throw new WorkingScheduleException("Problema la realizarea conexiunii cu baza de date");
        }
    }
    
    /**
     * @param wsDecoder the decoder holding information about the schedule to be parsed
     * @throws WorkingScheduleException upon invalid JSON resource or decoding failure
     */
    public void parseWS (WSDecoder wsDecoder) throws WorkingScheduleException{
         if (wsDecoder.getScheduleContent() != null) {
             Gson json = new Gson();
             
             try {
                 DataRecord dataRecord = json.fromJson(wsDecoder.getScheduleContent(), DataRecord.class);
             } catch (Exception e) {
            	 scheduleStatus = ScheduleStatus.WS_PROCESSOR_FAILURE;
                 throw new WorkingScheduleException("Schedule-ul nu poate fi convertit la DataRecord");
             }
         }
         else {
             throw new WorkingScheduleException("Datele trimise sunt invalide");
         }
    }
    
    /**
     * @param wsDecoder the decoder holding information about the schedule to be saved
     * @throws WorkingScheduleException upon failure of save operation
     */
    public void saveWS (WSDecoder wsDecoder) throws WorkingScheduleException {
        scheduleStatus = ScheduleStatus.WS_PROCESSOR_SUCCESS;
        try {
            CallableStatement statement = connection.prepareCall("{call ScheduleMaster.storeSchedule(?,?)}");
            statement.registerOutParameter(1, Types.VARCHAR);
            statement.registerOutParameter(2, Types.VARCHAR);
            statement.setString(1,wsDecoder.getFilePath());
            statement.setString(2,wsDecoder.getFileName());
            Boolean result = statement.execute();
            
            if (result == false) {
                this.scheduleStatus=ScheduleStatus.WS_PROCESSOR_SUCCESS;
            }
        } catch (Exception e) {
        	scheduleStatus = ScheduleStatus.WS_PROCESSOR_FAILURE;
            throw new WorkingScheduleException("Problema la apelarea functiei saveWorkingSchedule");
        }
    }
    
    /**
     * @param wsDecoder the decoder holding information about the schedule that is to replace the old one (if it existed)
     * @throws WorkingScheduleException upon failure of update operation
     */
    public void updateWS (WSDecoder wsDecoder) throws WorkingScheduleException {
        scheduleStatus = ScheduleStatus.WS_PROCESSOR_SUCCESS;
        
        try {
            CallableStatement statement = connection.prepareCall("{call ScheduleMaster.updateSchedule(?,?)}");
            statement.registerOutParameter(1, Types.VARCHAR);
            statement.registerOutParameter(2,Types.VARCHAR);
            statement.setString(1,wsDecoder.getFilePath());
            statement.setString(2,wsDecoder.getFileName());
            Boolean result = statement.execute();
            
            if (result == true){
                this.scheduleStatus = ScheduleStatus.WS_PROCESSOR_SUCCESS;
            }
        } catch (Exception e) {
        	scheduleStatus = ScheduleStatus.WS_PROCESSOR_FAILURE;
            throw new WorkingScheduleException("Problema la apelarea functiei saveWorkingSchedule");
        }
    }
    
    /**
     * @throws WorkingScheduleException upon failure of remove operation
     */
    public void removeWS () throws WorkingScheduleException {
        scheduleStatus = ScheduleStatus.WS_PROCESSOR_SUCCESS;
        try {
            CallableStatement statement = connection.prepareCall("{call ScheduleMaster.removeSchedule()}");
            Boolean result = statement.execute();
            
            if (result == true){
                this.scheduleStatus=ScheduleStatus.WS_PROCESSOR_SUCCESS;
            }
        } catch (Exception e) {
        	scheduleStatus = ScheduleStatus.WS_PROCESSOR_FAILURE;
            throw new WorkingScheduleException("Problema la apelarea functiei saveWorkingSchedule");
        }
    }
}

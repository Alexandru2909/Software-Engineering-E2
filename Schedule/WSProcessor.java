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
    public WSProcessor(String connectionpPath)throws WorkingScheduleException{
        try{
            connection= DriverManager.getConnection(connectionpPath);
        }catch(Exception e){
            throw new WorkingScheduleException("Problema la realizarea conexiunii cu baza de date");
        }
    }
    public void parseWorkingSchedule(Schedule schedule) throws WorkingScheduleException{
         if(schedule.getScheduleContent()!=null){
             Gson json=new Gson();
             try{
                 DataRecord dataRecor=json.fromJson(schedule.getScheduleContent(),DataRecord.class);
             }catch(Exception e){
                 throw new WorkingScheduleException("Schedule-ul nu poate fi convertit la DataRecord");
             }
         }else{
             throw new WorkingScheduleException("Datele trimise sunt invalide");
         }
    }
    public void saveWorkingSchedule(Schedule schedule) throws WorkingScheduleException{
        scheduleStatus=ScheduleStatus.WS_PROCESSOR_SUCCESS;
        try{
            CallableStatement statement=connection.prepareCall("{call ScheduleMaster.storeSchedule(?,?)}");
            statement.registerOutParameter(1, Types.VARCHAR);
            statement.registerOutParameter(2, Types.VARCHAR);
            statement.setString(1,schedule.getFilePath());
            statement.setString(2,schedule.getFileName());
            Boolean result=statement.execute();
            if(result==false){
                this.scheduleStatus=ScheduleStatus.WS_PROCESSOR_SUCCESS;
            }
        }catch(Exception e){
            throw new WorkingScheduleException("Problema la apelarea functiei saveWorkingSchedule");
        }
    }
    public void updateWorkingSchedule(Schedule schedule) throws WorkingScheduleException{
        scheduleStatus=ScheduleStatus.WS_PROCESSOR_SUCCESS;
        try{
            CallableStatement statement=connection.prepareCall("{call ScheduleMaster.updateSchedule(?,?)}");
            statement.registerOutParameter(1, Types.VARCHAR);
            statement.registerOutParameter(2,Types.VARCHAR);
            statement.setString(1,schedule.getFilePath());
            statement.setString(2,schedule.getFileName());
            Boolean result=statement.execute();
            if(result==true){
                this.scheduleStatus=ScheduleStatus.WS_PROCESSOR_SUCCESS;
            }
        }catch(Exception e){
            throw new WorkingScheduleException("Problema la apelarea functiei saveWorkingSchedule");
        }

    }
    public void removeWorkingSchedule(Schedule schedule)throws WorkingScheduleException{
        scheduleStatus=ScheduleStatus.WS_PROCESSOR_SUCCESS;
        try{
            CallableStatement statement=connection.prepareCall("{call ScheduleMaster.updateSchedule(?,?)}");
            statement.registerOutParameter(1, Types.VARCHAR);
            statement.registerOutParameter(2,Types.VARCHAR);
            statement.setString(1,schedule.getFilePath());
            statement.setString(2,schedule.getFileName());
            Boolean result=statement.execute();
            if(result==true){
                this.scheduleStatus=ScheduleStatus.WS_PROCESSOR_SUCCESS;
            }
        }catch(Exception e){
            throw new WorkingScheduleException("Problema la apelarea functiei saveWorkingSchedule");
        }


    }
}

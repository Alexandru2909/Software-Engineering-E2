package IpClasses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class WSDecoder {
    private String scheduleContent;
    private File scheduleFile;
    
    /**
     * @param schedulePath the path to the JSON resource representing the schedule
     * @throws WorkingScheduleException when the specified resource does not exist or when we cannot access it
     */
    public WSDecoder(String schedulePath)throws WorkingScheduleException{
        scheduleFile=new File(schedulePath);
        if(scheduleFile.exists()==false){
            throw new WorkingScheduleException("Fisierul nu exista");
        }
        try{
            BufferedReader input=new BufferedReader(new FileReader(schedulePath));
            String localBuffer;
            scheduleContent="";
            while((localBuffer=input.readLine())!=null){
                scheduleContent=scheduleContent+localBuffer;
            }
            input.close();
        }catch(Exception e){
            throw new WorkingScheduleException("Problema la accesarea fisierului");
        }
    }
    
    /**
     * @return the raw content of the JSON resource representing the schedule
     */
    public String getScheduleContent(){
        return scheduleContent;
    }
    
    /**
     * @return the file path to the JSON resource representing the schedule (w/o the file name)
     */
    public String getFilePath(){
        return scheduleFile.getParentFile().getAbsolutePath();
    }
    
    /**
     * @return the file name of the JSON resource representing the schedule
     */
    public String getFileName(){
        return scheduleFile.getName();
    }

}

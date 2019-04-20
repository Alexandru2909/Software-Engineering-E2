package IpClasses;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Schedule {
    private String scheduleContent;
    private File scheduleFile;
    public Schedule(String schedulePath)throws WorkingScheduleException{
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
    public String getScheduleContent(){
        return scheduleContent;
    }
    public String getFilePath(){
        return scheduleFile.getParentFile().getAbsolutePath();
    }
    public String getFileName(){
        return scheduleFile.getName();
    }

}

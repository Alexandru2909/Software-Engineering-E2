package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class JRDecoder {
    private String jrContent;
    private File jrFile;
    
    /**
     * @param jrPath the path to the JSON resource representing the schedule
     * @throws JSONResourceException when the specified resource does not exist or when we cannot access it
     */
    public JRDecoder(String jrPath)throws JSONResourceException{
        jrFile=new File(jrPath);
        if(jrFile.exists()==false){
            throw new JSONResourceException("Fisierul nu exista");
        }
        try{
            BufferedReader input=new BufferedReader(new FileReader(jrPath));
            String localBuffer;
            jrContent="";
            while((localBuffer=input.readLine())!=null){
                jrContent=jrContent+localBuffer;
            }
            input.close();
        }catch(Exception e){
            throw new JSONResourceException("Problema la accesarea fisierului");
        }
    }
    
    /**
     * @return the raw content of the JSON resource representing the schedule
     */
    public String getJrContent(){
        return jrContent;
    }
    
    /**
     * @return the file path to the JSON resource representing the schedule or the building plan (w/o the file name)
     */
    public String getJrFilePath(){
        return jrFile.getParentFile().getAbsolutePath();
    }
    
    /**
     * @return the file name of the JSON resource representing the schedule or the building plan
     */
    public String getJrFileName(){
        return jrFile.getName();
    }

}

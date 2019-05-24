package com.frontend.backend.ARGuide.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * class whose instance decodes the given JSON resource
 * @author Paul-Reftu
 *
 */
public class JRDecoder {
    private String jrContent;
    private File jrFile;
    
    /**
     * access and temporarily store the JSON string w.r.t the given JSON resource
     * @param jrPath the path to the JSON resource representing the schedule
     * @throws JSONResourceException when the specified resource does not exist or when we cannot access it
     */
    public JRDecoder(String jrPath)throws JSONResourceException{
        jrFile=new File(jrPath);
        if(!jrFile.exists()){
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
     * get the JSON string w.r.t the current JSON resource being stored
     * @return the raw content of the JSON resource representing the schedule
     */
    public String getJrContent(){
        return jrContent;
    }
    
    /**
     * get the file path to the current JSON resource (without the file name)
     * @return the file path to the JSON resource representing the schedule or the building plan (w/o the file name)
     */
    public String getJrFilePath(){
        return jrFile.getParentFile().getAbsolutePath();
    }
    
    /**
     * get the file name of the current JSON resource
     * @return the file name of the JSON resource representing the schedule or the building plan
     */
    public String getJrFileName(){
        return jrFile.getName();
    }

}

package com.frontend.backend.ARGuide.webParserV3;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AutoUpdateClass{
    private String siteAddress;
    private String lastUpdateDate;
    private String data;
    private Document document;
    private String lastUpdateFilePath;



    public AutoUpdateClass(String siteAddress,String lastUpdateFilePath){
        this.siteAddress=siteAddress;
        this.lastUpdateFilePath=lastUpdateFilePath;
        try{
            this.lastUpdateDate="";
            BufferedReader inputData=new BufferedReader(new FileReader(lastUpdateFilePath));
            this.lastUpdateDate=inputData.readLine();
        }catch (FileNotFoundException e){
            /*
             * if 'lastUpdateTime.txt' does not exist - attempt creating it
             * and setting 'this.lastUpdateDate' to null in order to let the 'runDataCollector()' method know
             * that an update is necessary
             */
            try {
                File lastUpdateTime = new File(lastUpdateFilePath);

                if (!lastUpdateTime.createNewFile())
                    throw new IOException("'lastUpdateTime.txt' could not be created at " + lastUpdateFilePath);

                this.lastUpdateDate = null;
            } catch (IOException e2) {
                e2.printStackTrace();
            }

        }catch(IOException e){
            System.out.println(e.getMessage());
            //nu facem nimic ,consideram ca este o prima utilizare a aplicateiei asa ca vom reincarca baza de date
        }
    }
    public void setNewDate(){
        try {
            document = Jsoup.connect(siteAddress).get();
            Element element = document.getElementsByTag("b").get(0);
            data = element.text();
            BufferedWriter output=new BufferedWriter(new FileWriter(lastUpdateFilePath));
            output.write(data);
            output.close();
        }catch(IOException e){
            System.out.println("problema la conectare");
        }
    }

    /**
     * functia verifica daca s-au efectuat modificari in orar
     * @return
     */
    public boolean runDataCollector(){
        if(lastUpdateDate==null){
            return false;
        }else if(lastUpdateDate.compareTo("")==0){
            return false;
        }
        try {
            document = Jsoup.connect(siteAddress).get();
            Element element = document.getElementsByTag("b").get(0);
            data = element.text();
            if (data.compareTo(lastUpdateDate) == 0) {
                return true;
            }else{
                return false;
            }
        }catch(IOException e){
            System.out.println("problema la conectare");
            return false;
        }
    }

}


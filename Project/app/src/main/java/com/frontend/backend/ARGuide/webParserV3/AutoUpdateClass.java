package com.frontend.backend.ARGuide.webParserV3;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;
import java.util.concurrent.ExecutionException;


public class AutoUpdateClass extends AppCompatActivity {
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

    /**
     * asynchronous task that utilizes JSoup to connect to the website where we have the information
     * we need w.r.t the Working Schedule
     */
    private class JSoupAssignment extends AsyncTask<Void, Void, Document> {
        /**
         * connects to the website where the WS is present and returns that respective document for further processing
         * @param params no parameters
         * @return the document that represents the web page at the given link (namely - 'this.siteAddress')
         */
        @Override
        public Document doInBackground(Void... params) {
            try {
                return Jsoup.connect(siteAddress).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public void setNewDate(){
        try {
            /*
             * execute the JSoupAssignment asynchronous task and wait for its return value
             * yes - this approach is anything BUT asynchronous
             * an improvement must be done to use the class as it is meant to be used
             */
            document = new JSoupAssignment().execute().get();
            Element element = document.getElementsByTag("b").get(0);
            data = element.text();
            BufferedWriter output=new BufferedWriter(new FileWriter(lastUpdateFilePath));
            output.write(data);
            output.close();
        }catch(IOException e){
            System.out.println("problema la conectare");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
            /*
             * execute the JSoupAssignment asynchronous task and wait for its return value
             * yes - this approach is anything BUT asynchronous
             * an improvement must be done to use the class as it is meant to be used
             */
            document = new JSoupAssignment().execute().get();
            Element element = document.getElementsByTag("b").get(0);
            data = element.text();
            if (data.compareTo(lastUpdateDate) == 0) {
                return true;
            }else{
                return false;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }
}


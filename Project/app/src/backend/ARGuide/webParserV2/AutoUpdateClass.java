package WebParserV2;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.*;






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
            System.out.println(e.getMessage());
        }catch(IOException e){
            //nu facem nimic ,consideram ca este o prima utilizare a aplicateiei asa ca vom reincarca baza de date
        }
    }
    public boolean runDataCollector(){
        if(lastUpdateDate.compareTo("")==0){
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


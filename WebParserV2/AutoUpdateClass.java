package WebParserV2;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class AutoUpdateClass extends  Thread{
    private String siteAddress="https://profs.info.uaic.ro/~orar/orar_resurse.html";
    private String lastUpdateDate;
    private Document document;
    public AutoUpdateClass(String siteAddress,String lastUpdateDate){
        this.siteAddress=siteAddress;
        this.lastUpdateDate=lastUpdateDate;
    }

    public void run(){
        try {
            if(lastUpdateDate.compareTo("")==0){
                //pornim parser-ul pentru ca nu avem nicio data curent
            }
            while(true){
                document = Jsoup.connect(siteAddress).get();
                Element element=document.getElementsByTag("b").get(0);
                String data=element.text();
                if(data.compareTo(lastUpdateDate)==0){
                    //pornim parser-ul pentru ca nu avem nicio data curent
                }
                Thread.sleep(10000);
            }

        }catch(Exception e){
            System.out.println("problema in procesul de actualizare");
            return;
        }
    }

    public static void main(String...args){
        AutoUpdateClass auto=new AutoUpdateClass("https://profs.info.uaic.ro/~orar/orar_resurse.html","");
        auto.run();
    }
}

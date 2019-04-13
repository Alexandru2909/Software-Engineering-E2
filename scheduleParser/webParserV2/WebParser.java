/*
* Inainte de utilizare este necesara instalarea librariei Jsoup
* Paul Reftu: The GSON library is also required
*
* */


package webParserV2;
import com.google.gson.Gson;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.time.LocalTime;
import java.util.*;


public class WebParser {
    /**
     * adresa paginii orarului
     * pagina contine toate linkurile catre paginile cu tabele
     */
    public static String siteAddress="https://profs.info.uaic.ro/~orar/orar_resurse.html";
    /**
     * documentul paginii principale
     * */
    public static Document mainDocument;
    /**
     * lista documentelor paginilor ce contin tabele
     */
    public static LinkedList<Document> documentsList=new LinkedList<Document>();
    /**
     * lista inregistrarilor
     */
    public static LinkedList<DataRecord> recordsList=new LinkedList<DataRecord>();

    /**
     * functia care extrage datele dintr-un document specificat intr-un obiect de tip DataRecord
     * @param doc documentrul ce contine codul paginii web
     * @param dataRecord obiectul in care vor fi salvate datele
     */

    public static void getDataFromPage(Document doc,DataRecord dataRecord){
        if(dataRecord==null||doc==null){
            return;
        }
        Element tableTag=doc.getElementsByTag("table").get(0);
        Elements trTags=tableTag.getElementsByTag("tr");
        DayRecord dayRecord=null;
        for(int i=1;i<trTags.size();i++){
            //String trTagContent=trTags.get(i).toString(); /* Paul Reftu:  "trTagContent" does not appear to be necessary */
            Elements tdTags=trTags.get(i).getElementsByTag("td");
            if(tdTags.size()==1){
                String day=tdTags.get(0).text();//extragem ziua din tabel
                Scanner dayScanner=new Scanner(day);
                dayScanner.useDelimiter(" ");
                day=dayScanner.next();
                dayScanner.close();
                dayRecord=new DayRecord();
                dataRecord.setValue(day,dayRecord);
            }else{
                Eveniment eveniment=new Eveniment();
                eveniment.oraStart=LocalTime.parse(tdTags.get(0).text());//extragem ora de inceput
                eveniment.oraFinal=LocalTime.parse(tdTags.get(1).text());//extragem ora de final
                eveniment.numeEveniment=new String(tdTags.get(2).text());//extragem numele disciplinei
                eveniment.tipEveniment=new String(tdTags.get(3).text());//extragem tipul activitatii
                LinkedList<String> auxList=new LinkedList<>(); //extragem lista de profesori
                Scanner scanner=new Scanner(tdTags.get(4).text());
                scanner.useDelimiter("\n");
                while(scanner.hasNext()==true){
                    auxList.add(scanner.next());
                }
                eveniment.listaProfesori=auxList;
                auxList=new LinkedList<>();
                scanner.close();
                scanner=new Scanner(tdTags.get(5).text());
                scanner.useDelimiter("\n, ");
                while(scanner.hasNext()==true){
                    auxList.add(scanner.next());
                }
                eveniment.listaGrupe=auxList;
                auxList=null;
                if(dayRecord!=null){
                    dayRecord.listaEvenimente.add(eveniment);
                }
                scanner.close();
            }

        }

    }

    public static void main(String[] args){
        try{
            mainDocument=Jsoup.connect(siteAddress).get();
        }catch(Exception e){
            System.out.println("problema la gasirea paginii principale");
            System.exit(0);
        }
        Elements links=mainDocument.getElementsByTag("a");
        for(int i=0;i< links.size()-2;i++){//eliminam ultimele doua linkuri
            Document auxDocument;
            String link=links.get(i).attr("href");
            try{
                auxDocument=Jsoup.connect("https://profs.info.uaic.ro/~orar/"+"/"+link).get();
                DataRecord dataRecord=new DataRecord();
                dataRecord.setRoomCode(links.get(i).text());
                getDataFromPage(auxDocument,dataRecord);
                recordsList.add(dataRecord);
            }catch(Exception e){
                continue;
            }
            documentsList.add(auxDocument);
        }

        String mainPath="./scheduleParser/schedules/faculty_";
        File file;
        String filePath;
        for(DataRecord data:recordsList){
            filePath=mainPath+data.roomCode+".json";
            file=new File(filePath);
            try {
                if (file.createNewFile() == true) {
                    System.out.println("fisierul a fost creat");
                }else{
                    System.out.println("problema la crearea fisierului");
                }
                Gson json=new Gson();
                String response=json.toJson(data);
                FileWriter output=new FileWriter(filePath);
                output.write(response);
                output.close();
            }catch(Exception e){
                System.out.println("problema la crearea fisierului");
            }

        }

    }
}

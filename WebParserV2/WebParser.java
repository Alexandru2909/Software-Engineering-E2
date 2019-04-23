/*
* Inainte de utilizare este necesara instalarea librariei Jsoup
*
*
* */


package WebParserV2;
import com.google.gson.Gson;
import com.sun.org.apache.bcel.internal.generic.LoadClass;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

/**
 * Clasa Eveniment retine o linie din tabelul parsat
 *
 */
class Eveniment{
    /**
     * ora de inceput a evenimentului
     */
    public LocalTime oraStart;
    /**
     * ora de final a evenimentului
     */
    public LocalTime oraFinal;
    /**
     *numele evenimentului
     */
    public String numeEveniment;
    /**
     * tipul evenimentului curs sau  laborator
     */
    public String tipEveniment;
    /**
     * lista profesorilor
     */
    public LinkedList<String> listaProfesori=new LinkedList<>();
    /**
     *Lista grupelor de studenti
     */

    public LinkedList<String> listaGrupe=new LinkedList<>();

    /**************************************************************/
    /**
     * functia care returneaza ora de start
     */
    public LocalTime getOraStart(){
        return this.oraStart;
    }
    /**
     * functia care returneaza ora de final
     */

    public LocalTime getOraFinal(){
        return this.oraFinal;
    }
    /**
     * functia care returneza numele evenimentului
     */

    public String getNumeEveniment(){
        return this.numeEveniment;
    }
    /**
     * functia care returneaza tipul evenimentului
     */
    public String getTipEveniment(){
        return this.tipEveniment;
    }
    /**
     * functia care returneaza lista profesorilor
     */
    public LinkedList<String> getListaProfesori(){
        return this.listaProfesori;
    }
    /**
     * functia care returneaza lista grupelor de studenti
     */
    public LinkedList<String> getListaGrupe(){
        return this.listaGrupe;
    }


    /************************************************************/
    public Eveniment(LocalTime oraStart,LocalTime oraFinal,String numeEveniment,String tipEveniment,LinkedList<String> listaProfesori,LinkedList<String> listaGrupe){
        this.oraStart=oraStart;
        this.oraFinal=oraFinal;
        this.numeEveniment=numeEveniment;
        this.tipEveniment=tipEveniment;
        this.listaProfesori=listaProfesori;
        this.listaGrupe=listaGrupe;
    }
    public Eveniment(){}
    public String toString(){
        String returnString=new String("Ora de inceput :"+this.oraStart.toString()+"\n"+"Ora de terminare :"+this.oraFinal.toString()+"\n"+"Numele evenimentului :"+this.numeEveniment+"\n"+"Tipul evenimentului :"+this.tipEveniment+"\n");
        String profesori=new String("");
        String grupe=new String("");
        for(String name:listaProfesori){profesori=profesori+name;}
        for(String grupa:listaGrupe){grupe=grupe+grupa;}
        returnString=returnString+"lista profesori:"+profesori+"\nlista grupelor:"+grupe+"\n\n";
        return returnString;
    }
}

class DayRecord{
    /**
     * lista evenimentelor din ziua desemnata de obiectul curent
     */

    public LinkedList<Eveniment> listaEvenimente=new LinkedList<Eveniment>();
    public String toString(){
        String returnValue=new String("");
        for(Eveniment e:listaEvenimente){
            returnValue=returnValue+e;
        }
        return returnValue;
    }
}

public class WebParser {
    /**
     * adresa paginii orarului
     * pagina contine toate linkurile catre paginile cu tabele
     */
    public String siteAddress;//"https://profs.info.uaic.ro/~orar/";
    public String sitePageName;//"orar_resurse.html";
    /**
     * locatia unde dorim sa fie depuse fisierele
     */
    public String resultFilesAddress;
    /**
     * documentul paginii principale
     * */
    public Document mainDocument;
    /**
     * lista documentelor paginilor ce contin tabele
     */
    public LinkedList<Document> documentsList=new LinkedList<Document>();
    /**
     * lista inregistrarilor
     */
    public LinkedList<DataRecord> recordsList=new LinkedList<DataRecord>();
    /**
     *
     * @param siteAddress adresa paginii ce contine lista orarului
     * @param resultFilesLocation locatia unde vor fi salvate fisierele
     * @throws IOException
     */
    public WebParser(String siteAddress,String sitePageName,String resultFilesLocation) throws IOException {
            this.siteAddress=siteAddress;
            this.sitePageName=sitePageName;
            this.resultFilesAddress=resultFilesLocation;
            File myFile=new File(resultFilesLocation);
            if(!myFile.exists()){
                if(myFile.createNewFile()==false){
                    throw new IOException("nu s-au putut ierarhia de foldere");
                }
            }
    }

    /**
     * functia care extrage datele dintr-un document specificat intr-un obiect de tip DataRecord
     * @param doc documentrul ce contine codul paginii web
     * @param dataRecord obiectul in care vor fi salvate datele
     */

    public void getDataFromPage(Document doc,DataRecord dataRecord){
        if(dataRecord==null||doc==null){
            return;
        }
        Element tableTag=doc.getElementsByTag("table").get(0);
        Elements trTags=tableTag.getElementsByTag("tr");
        DayRecord dayRecord=null;
        for(int i=1;i<trTags.size();i++){
            String trTagContent=trTags.get(i).toString();
            Elements tdTags=trTags.get(i).getElementsByTag("td");
            if(tdTags.size()==1){
                String day=tdTags.get(0).text();//extragem ziua din tabel
                Scanner dayScanner=new Scanner(day);
                dayScanner.useDelimiter(" ");
                day=dayScanner.next();
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
            }

        }

    }

    public void runParset(){
        try{
            mainDocument=Jsoup.connect(siteAddress+sitePageName).get();
        }catch(Exception e){
            System.out.println("problema la gasirea paginii principale");
            System.exit(0);
        }
        Elements links=mainDocument.getElementsByTag("a");
        for(int i=0;i< links.size()-2;i++){//eliminam ultimele doua linkuri
            Document auxDocument;
            String link=links.get(i).attr("href");
            try{
                auxDocument=Jsoup.connect(siteAddress+"/"+link).get();
                DataRecord dataRecord=new DataRecord();
                dataRecord.setRoomCode(links.get(i).text());
                getDataFromPage(auxDocument,dataRecord);
                recordsList.add(dataRecord);
            }catch(Exception e){
                continue;
            }
            documentsList.add(auxDocument);
        }

        String mainPath=resultFilesAddress;
        File file;
        String filePath;
        for(DataRecord data:recordsList){
            filePath=mainPath+data.roomCode+".txt";
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

    public static void main(String[] args){
        /*
        secventa asta trebuie integrata in programul principal
         */
        try {
            WebParser parser = new WebParser("https://profs.info.uaic.ro/~orar/", "orar_resurse.html", "C:\\Users\\Bogdan\\Desktop\\resultFiles\\");
            parser.runParset();
        }catch (Exception e){
            System.out.println("problema la crearea fisielor");
        }
    }
}

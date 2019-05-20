/*
* Inainte de utilizare este necesara instalarea librariei Jsoup
*
*
* */


package com.frontend.backend.ARGuide.webParserV3;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.time.LocalTime;
import java.util.*;

public class WebParser {
    /**
     * adresa paginii orarului
     * pagina contine toate linkurile catre paginile cu tabele
     */
    public String siteAddress;//"https://profs.info.uaic.ro/~orar/";
    public String sitePageName;//"orar_resurse.html";
    public String sectionsNamesFile;//adresa fisierului in care se gasesc numele sectiunilor care trebuiesc procesate
    private ArrayList<String> titlesList=new ArrayList<>();
    /**
     * locatia unde dorim sa fie depuse fisierele
     */
    public String resultFileAddress;
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
     * @param resultFileLocation locatia unde vor fi salvate fisierele
     * @throws IOException
     */
    public WebParser(String siteAddress,String sitePageName,String resultFileLocation,String sectionsNamesFile) throws IOException {
            this.siteAddress=siteAddress;
            this.sitePageName=sitePageName;
            this.sectionsNamesFile=sectionsNamesFile;
            this.resultFileAddress=resultFileLocation;
            File myFile=new File(resultFileLocation);
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
    public void deleteOldFiles(){
        File oldFilesFolder=new File(resultFileAddress);
        oldFilesFolder.delete();
    }

    public void runParset(){
        deleteOldFiles();
        try{
            mainDocument=Jsoup.connect(siteAddress+sitePageName).get();
        }catch(Exception e){
            System.out.println("problema la gasirea paginii principale");
            System.exit(0);
        }

        try{
            BufferedReader inputData=new BufferedReader(new FileReader(sectionsNamesFile));
            String sectionName;
            while((sectionName=inputData.readLine())!=null){ ;
                titlesList.add(sectionName);
            }
        }catch(FileNotFoundException e){
            /*
             * if 'sectionNames.txt' is not found, then create it
             * and insert the standard section names for our faculty
             */
            try {
                File sectionsNames = new File(sectionsNamesFile);

                if (!sectionsNames.createNewFile())
                    throw new IOException("'sectionNames.txt' file could not be created at " + sectionsNamesFile);

                String sectionsNamesContents = "ALTE SALI\n" +
                            "CABINET\n" +
                            "LABORATOARE\n" +
                            "SALI DE CURS\n" +
                            "SALI DE SEMINAR";
                FileWriter out = new FileWriter(sectionsNames);
                out.write(sectionsNamesContents);
                out.close();

                /*
                 * lazy approach - should in fact use a regular expression to get the titles from 'sectionNamesContents'
                 */
                BufferedReader in = new BufferedReader(new FileReader(sectionsNames));
                String sectionName;

                while ((sectionName = in.readLine()) != null)
                    titlesList.add(sectionName);
                in.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }catch(IOException e){
            System.out.print("probleme la citirea din fisier");
        }

        Element firstListLayer=mainDocument.getElementsByTag("ul").first();
        String sectionName;
        Elements childrenList=firstListLayer.children();
        for(int i=0;i<childrenList.size();i++){
            sectionName=childrenList.get(i).ownText();
            sectionName=sectionName.toUpperCase();
            if(titlesList.contains(sectionName)==true){
                Elements links=childrenList.get(i).getElementsByTag("a");
                Document auxDocument;
                for(Element element:links){
                    try {
                        String hrefAttribute = element.attr("href");
                        auxDocument = Jsoup.connect(siteAddress + "/" + hrefAttribute).get();
                        DataRecord dataRecord = new DataRecord();
                        dataRecord.setRoomCode(element.text());
                        getDataFromPage(auxDocument, dataRecord);
                        recordsList.add(dataRecord);
                        documentsList.add(auxDocument);
                    }catch (IOException e){
                        System.out.println(" probglema pe site "+e.getMessage()+" "+element.text());
                        continue;
                    }

                }
            }
        }




        File destinationFile=new File(resultFileAddress);
        if(destinationFile.exists()==false){
            try{
                destinationFile.createNewFile();
            }catch(IOException e){
                System.out.println("problema la crearea fisierului rezultat");
            }
        }
        String response="";
        
        /*
         * create a new 'Schedule' object and store all individual classroom schedules inside it
         */
        Schedule schedule = new Schedule();
        
        for (DataRecord dataRecord : recordsList)
        	schedule.add(dataRecord);
        
        Gson json = new GsonBuilder().setPrettyPrinting().create();
        response = json.toJson(schedule.getRoomSchedules());
        
        /*
         * write the JSON file to the corresponding destination
         */
        FileWriter output;
		try {
			output = new FileWriter(resultFileAddress);
			output.write(response);
	        output.close();
		} catch (IOException e) {
			System.out.println("Failure on writing the JSON schedule file.");
			e.printStackTrace();
		}
        
    }

    public static void main(String[] args){
        /*
        secventa asta trebuie integrata in programul principal
         */
        try {
            WebParser parser = new WebParser("https://profs.info.uaic.ro/~orar/", "orar_resurse.html", "C:\\Users\\Bogdan\\Desktop\\resultFiles\\","C:\\Users\\Bogdan\\Desktop\\sectionsNames.txt");
            parser.runParset();
        }catch (Exception e){
            System.out.println("problema la crearea fisielor" +e.getMessage());
        }
    }
}



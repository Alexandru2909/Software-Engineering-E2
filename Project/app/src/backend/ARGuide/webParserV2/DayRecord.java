package webParserV2;

import java.util.LinkedList;

public class DayRecord{
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

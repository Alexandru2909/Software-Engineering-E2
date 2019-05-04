package webParserV2;

import java.util.LinkedList;

public class DayRecord{
    /**
     * lista evenimentelor din ziua desemnata de obiectul curent
     */

    public LinkedList<Eveniment> listaEvenimente=new LinkedList<Eveniment>();
    /**
	 * @return the listaEvenimente
	 */
	public LinkedList<Eveniment> getListaEvenimente() {
		return listaEvenimente;
	}
	/**
	 * @param listaEvenimente the listaEvenimente to set
	 */
	public void setListaEvenimente(LinkedList<Eveniment> listaEvenimente) {
		this.listaEvenimente = listaEvenimente;
	}
	public String toString(){
        String returnValue=new String("");
        for(Eveniment e:listaEvenimente){
            returnValue=returnValue+e;
        }
        return returnValue;
    }
}

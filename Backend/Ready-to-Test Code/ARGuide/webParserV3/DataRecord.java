package webParserV3;

import java.util.HashMap;
import java.util.Set;

public class DataRecord{
    /**
     * codul camerei
     */

    public String roomCode="roomCode";
    /**
     * lista evenimentelor din camera grupate in functie de ziua
     */

    public HashMap<String,DayRecord> roomRecord=new HashMap<String,DayRecord>();
    /**
     * functia care seteaza o lista de evenimente pentru o anumita zi
     * @param day este ziua
     * @param dayRecord este lista de evenimente din ziua specificata
     */
    public void setValue(String day,DayRecord dayRecord){
        day=day.toUpperCase();
        roomRecord.put(day,dayRecord);
    }
    public String getRoomCode(){
        return this.roomCode;
    }
    public String toString(){
        Set<String> keysSet = roomRecord.keySet();
        String returnValue=new String(this.roomCode+"\n");
        for(String day:keysSet){
            DayRecord auxDayRecord=roomRecord.get(day);
            returnValue=returnValue+day+"\n"+auxDayRecord;
        }
        return returnValue;
    }


    public DataRecord(){}

    /**
	 * @return the roomRecord
	 */
	public HashMap<String, DayRecord> getRoomRecord() {
		return roomRecord;
	}
	/**
	 * @param roomRecord the roomRecord to set
	 */
	public void setRoomRecord(HashMap<String, DayRecord> roomRecord) {
		this.roomRecord = roomRecord;
	}
	/**
     * functia seteaza o noua valoare pentru atributul roomCode
     * @param roomCode este noua valoare a atributului roomCode
     */
    public void setRoomCode(String roomCode){
        this.roomCode=roomCode;
    }
}

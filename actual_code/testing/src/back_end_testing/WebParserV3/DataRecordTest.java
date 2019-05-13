package com.frontend.backend.ARGuide.webParserV3;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class DataRecordTest {
    DataRecord test1,test2;
    LinkedList<Eveniment> evTest1 = new LinkedList <>();
    DayRecord dayRec;
    LinkedList<Eveniment> evTest2 = new LinkedList <>();
    LinkedList<String> lista= new LinkedList();
    LinkedList<String> lista2= new LinkedList();
    HashMap<String,DayRecord> hashMap1=new HashMap<String,DayRecord>();
    HashMap<String,DayRecord> hashMap2=new HashMap<String,DayRecord>();
    String day;
    @Before
    public void setUp() throws Exception {
        test1 = new DataRecord();
        test2 = new DataRecord();
        lista.add("prof");
        lista2.add("grup");
        evTest1.add(new Eveniment());
        evTest2.add(new Eveniment(LocalTime.of(8,0,0),LocalTime.of(10,0,0),"da","nu",lista,lista2));
        dayRec = new DayRecord();
        dayRec.setListaEvenimente(evTest2);
        day="saturday";
        setRoomCode();
        setValue();
    }

    @After
    public void tearDown() throws Exception {
        test1 = null;
        test2=null;
    }

    @Test
    public void setValue() {
        test2.setValue(day,dayRec);
        hashMap2.put(day.toUpperCase(),dayRec);
    }

    @Test
    public void getRoomCode() {
        assertEquals("roomCode",test1.getRoomCode());
        assertEquals("301",test2.getRoomCode());
    }

    @Test
    public void toStringTest() {
        assertEquals("roomCode\n",test1.toString());
        assertEquals(test2.getRoomCode() + "\n"+day.toUpperCase()+"\n"+dayRec.toString(),test2.toString());
    }

    @Test
    public void getRoomRecord() {
        assertEquals(hashMap1,test1.getRoomRecord());
        assertEquals(hashMap2,test2.getRoomRecord());
    }

    @Test
    public void setRoomRecord() {
        //no need for this function
    }

    @Test
    public void setRoomCode() {
        test2.setRoomCode("301");
    }
}
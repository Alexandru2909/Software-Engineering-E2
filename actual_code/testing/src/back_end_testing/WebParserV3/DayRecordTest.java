package com.frontend.backend.ARGuide.webParserV3;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class DayRecordTest {
    DayRecord test1,test2;
    LinkedList<Eveniment> evTest1 = new LinkedList <>();
    LinkedList<Eveniment> evTest2 = new LinkedList <>();
    LinkedList<String> listaEv= new LinkedList();
    LinkedList<String> lista= new LinkedList();
    LinkedList<String> lista2= new LinkedList();
    @Before
    public void setUp() throws Exception {
        lista.add("prof");
        lista2.add("grup");
        test1 = new DayRecord();
        evTest1.add(new Eveniment());
        evTest2.add(new Eveniment(LocalTime.of(8,0,0),LocalTime.of(10,0,0),"da","nu",lista,lista2));
        test2  = new DayRecord();
    }

    @After
    public void tearDown() throws Exception {
        test1 = null;
        test2 = null;
    }

    @Test
    public void toStringTest() {
        assertEquals("",test1.toString());
        setListaEvenimente();
        assertEquals(new Eveniment(LocalTime.of(8,0,0),LocalTime.of(10,0,0),"da","nu",lista,lista2).toString(),test2.toString());
    }

    @Test
    public void setListaEvenimente() {
        test1.setListaEvenimente(evTest1);
        test2.setListaEvenimente(evTest2);
    }

    @Test
    public void getListaEvenimente() {
        setListaEvenimente();
        assertEquals(evTest1,test1.getListaEvenimente());
        assertEquals(evTest2,test2.getListaEvenimente());
    }


}
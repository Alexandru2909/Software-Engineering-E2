package com.frontend.backend.ARGuide.webParserV3;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalTime;
import java.util.LinkedList;

import static org.junit.Assert.*;

public class EvenimentTest {
    Eveniment test1,test2;
    LinkedList<String> lista= new LinkedList();
    LinkedList<String> lista2= new LinkedList();
    @Before
    public void setUp() throws Exception {
        lista.add("prof");
        lista2.add("grup");
        test1 = new Eveniment(LocalTime.of(8,0,0),LocalTime.of(10,0,0),"da","nu",lista,lista2);
        test2 = new Eveniment();
    }

    @After
    public void tearDown() throws Exception {
        test1 = null;
        test2 = null;
    }

    @Test
    public void getOraStart() {
        assertEquals(LocalTime.of(8,0,0),test1.getOraStart());
        assertNull(test2.getOraStart());
    }

    @Test
    public void getOraFinal() {
         assertEquals(LocalTime.of(10,0,0),test1.getOraFinal());
        assertNull(test2.getOraFinal());
    }

    @Test
    public void getNumeEveniment() {
        assertEquals("da",test1.getNumeEveniment());
        assertNull(test2.getNumeEveniment());
    }

    @Test
    public void getTipEveniment() {
        assertEquals("nu",test1.getTipEveniment());
        assertNull(test2.getTipEveniment());
    }

    @Test
    public void getListaProfesori() {
        assertEquals(lista,test1.listaProfesori);
        assertEquals(new LinkedList <String>(),test2.getListaProfesori());
    }

    @Test
    public void getListaGrupe() {
        assertEquals(lista2,test1.listaGrupe);
        assertEquals(new LinkedList <String>(),test2.getListaGrupe());
    }

    @Test
    public void toStringTest() {
        String expected = "Ora de inceput :"+test1.oraStart.toString()+"\n"+"Ora de terminare :"+test1.oraFinal.toString()+"\n"+"Numele evenimentului :"+test1.numeEveniment+"\n"+"Tipul evenimentului :"+test1.tipEveniment+"\n";
        expected = expected + "lista profesori:" + "prof" + "\nlista grupelor:"+"grup"+"\n\n";
        assertEquals(expected,test1.toString());
    }
}
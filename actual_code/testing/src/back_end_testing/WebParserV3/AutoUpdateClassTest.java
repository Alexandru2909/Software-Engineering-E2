package com.frontend.backend.ARGuide.webParserV3;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class AutoUpdateClassTest {
    AutoUpdateClass test1,test2;
    @Before
    public void setUp() throws Exception {
        test1 = new AutoUpdateClass("","");
        test2 = new AutoUpdateClass("https://profs.info.uaic.ro/~orar/","C:\\Users\\paulc\\Desktop\\lastUpdateTime.txt");
        setNewDate();
    }

    @After
    public void tearDown() throws Exception {
        test1= null;
        test2= null;
    }

    @Test
    public void setNewDate() {
        try {
            test1.setNewDate();
        }
        catch (Exception e)
        {
            if(e instanceof IllegalArgumentException)
            {
                assertEquals(true,true);
            }
            else
                assertEquals(true,false);
        }

        test2.setNewDate();

    }

    @Test
    public void runDataCollector() {
        try {
            test1.runDataCollector();
        }
        catch (Exception e)
        {
            if(e instanceof IllegalArgumentException)
            {
                assertEquals(true,true);
            }
            else
                assertEquals(true,false);
        }

        assertTrue(test2.runDataCollector());

    }
}
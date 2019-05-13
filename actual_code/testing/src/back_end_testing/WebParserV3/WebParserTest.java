package com.frontend.backend.ARGuide.webParserV3;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class WebParserTest {
    WebParser test1,test2;
    Document auxDocument;
    @Before
    public void setUp() throws Exception {
        try
        {
            test1 = new WebParser("","","","");
        }
        catch (Exception e)
        {
            if (e instanceof IOException)
            {
                assertTrue(true);
            }
            else
                assertTrue(false);
        }

        test2 = new WebParser("https://profs.info.uaic.ro/~orar/", "orar_resurse.html", "C:\\Users\\paulc\\Desktop\\resultFiles\\","C:\\Users\\paulc\\Desktop\\schedules\\sectionsNames.txt");
    }

    @After
    public void tearDown() throws Exception {
        test1 = null;
        test2 = null;
    }

    @Test
    public void getDataFromPage() {
        // to be continued

    }

    @Test
    public void deleteOldFiles() {
        test2.deleteOldFiles();
        File oldFilesFolder=new File(test2.resultFileAddress);
        assertFalse(oldFilesFolder.exists());
    }

    @Test
    public void runParset() {
        try {
            test1.runParset();
        }
        catch (Exception e)
        {
            if (e instanceof NullPointerException)
            {
                assertTrue(true);
            }
            else
                assertEquals(true,false);
        }
        test2.runParset();
    }

    @Test
    public void main() {
    }
}
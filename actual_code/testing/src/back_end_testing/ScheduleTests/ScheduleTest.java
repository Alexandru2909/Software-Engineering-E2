package IpClasses;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScheduleTest {
    Schedule test;
    @Before
    public void setUp() throws Exception {
        try
        {
            test = new Schedule("C:\\Users\\paulc\\AndroidStudioProjects\\MyApplication3\\app\\src\\main\\java\\IpClasses\\da.txt");
        }
        catch (Exception e)
        {
            System.out.println(e);
        }
    }

    @After
    public void tearDown() throws Exception {
        test = null;
    }

    @Test
    public void getScheduleContent() {
        try
        {
            assertNotNull(test.getScheduleContent());
            System.out.println(test.getScheduleContent());
        }
        catch (Exception e)
        {
            System.out.println(e + "getScheduleContent");
        }

    }

    @Test
    public void getFileName() {
        try
        {
            assertNotNull(test.getFileName());
            System.out.println(test.getFileName());
        }
        catch (Exception e)
        {
            System.out.println(e + "getFileName");
        }
    }
}
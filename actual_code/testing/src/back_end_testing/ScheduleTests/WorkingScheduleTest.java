package IpClasses;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WorkingScheduleTest {
    WorkingSchedule test;
    @Before
    public void setUp() throws Exception {
        test = new WorkingSchedule();
    }

    @After
    public void tearDown() throws Exception {
        test = null;
    }

    @Test
    public void getStatus() {
        //ScheduleStatus does nothing so will always be null
    }

    @Test
    public void sendRequest() {
        //no code to be tested
    }
}
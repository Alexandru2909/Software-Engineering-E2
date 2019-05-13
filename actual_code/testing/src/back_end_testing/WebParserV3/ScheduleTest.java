package com.frontend.backend.ARGuide.webParserV3;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;

public class ScheduleTest {
    Schedule test1,test2,test3;
    @Before
    public void setUp() throws Exception {
        test1 =  new Schedule();
        test2 = new Schedule();
        test3 = new Schedule();
    }

    @After
    public void tearDown() throws Exception {
        test1 = null;
        test2 = null;
        test3 = null;
    }

    @Test
    public void getRoomSchedules() {
        assertEquals(new LinkedList<DataRecord> (),test1.getRoomSchedules());

    }

    @Test
    public void setRoomSchedules() {
        test1.setRoomSchedules(new LinkedList <DataRecord>());
        test2.add(new DataRecord());
    }

    @Test
    public void add() {
    }
}
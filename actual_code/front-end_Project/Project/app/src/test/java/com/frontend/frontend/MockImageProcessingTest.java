package com.frontend.frontend;

import android.content.Intent;

import com.frontend.frontend.Timetable.TimetableScreen;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MockImageProcessingTest {

    private MockImageProcessing x;

    @Before
    public void setUp() throws Exception {
        this.x=new MockImageProcessing();
    }

    @Test
    public void openMenu() {
        assertEquals(new Intent(this.x, TimetableScreen.class),this.x.openMenu());
        assertNotEquals(new Intent(new MockImageProcessing(),TimetableScreen.class),this.x.openMenu());
    }

    @Test
    public void onRequestPermissionsResult() {
        assertEquals("SuperCalled",this.x.onRequestPermissionsResult(3,[1,2]);
        assertEquals("RecognizerStarted",this.x.onRequestPermissionsResult(2,[1,2]));
    }
}
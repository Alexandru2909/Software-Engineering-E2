package com.example.test2sprinttest;

import android.os.Bundle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainActivityTest {
    MainActivity test;
    @Before
    public void setUp() throws Exception {
        test = new MainActivity();
    }

    @After
    public void tearDown() throws Exception {
        test = null;
    }

    @Test
    public void onCreate() {
       // ???
    }
}
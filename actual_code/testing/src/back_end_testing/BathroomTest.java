package com.example.test2sprinttest;

import org.junit.After;
import org.junit.Before;

import static org.junit.Assert.*;

public class BathroomTest {
    Bathroom test;
    @Before
    public void setUp() throws Exception {
        test = new Bathroom();
    }

    @After
    public void tearDown() throws Exception {
        test = null;
    }
}
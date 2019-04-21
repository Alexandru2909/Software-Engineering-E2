package com.frontend.frontend.BuildingLayout;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestBuildings {

    @Test
    public void getId() {
        int[] testAvailableFloors = {1,2,3,4,6,7};
        Buildings testBuilding = new Buildings("01","TestBuilding",7,6, testAvailableFloors);
        assertEquals("01", testBuilding.getId());
    }

    @Test
    public void setId() {
        int[] testAvailableFloors = {1,2,3,4,6,7};
        Buildings testBuilding = new Buildings("01","TestBuilding",7,6, testAvailableFloors);
        testBuilding.setId("02");
        assertEquals("02", testBuilding.getId());

    }

    @Test
    public void getName() {
        int[] testAvailableFloors = {1,2,3,4,6,7};
        Buildings testBuilding = new Buildings("01","TestBuilding",7,6, testAvailableFloors);
        assertEquals("TestBuilding", testBuilding.getName());

    }

    @Test
    public void setName() {
        int[] testAvailableFloors = {1,2,3,4,6,7};
        Buildings testBuilding = new Buildings("01","TestBuilding",7,6, testAvailableFloors);
        testBuilding.setName("TestBuilding2");
        assertEquals("TestBuilding2", testBuilding.getName());

    }

    @Test
    public void getNumberOfFloors() {
        int[] testAvailableFloors = {1,2,3,4,6,7};
        Buildings testBuilding = new Buildings("01","TestBuilding",7,6, testAvailableFloors);
        testBuilding.setName("TestBuilding2");
        assertEquals(7, testBuilding.getNumberOfFloors());

    }

    @Test
    public void setNumberOfFloors() {
        int[] testAvailableFloors = {1,2,3,4,6,7};
        Buildings testBuilding = new Buildings("01","TestBuilding",7,6, testAvailableFloors);
        testBuilding.setNumberOfFloors(5);
        assertEquals(5, testBuilding.getNumberOfFloors());

    }

    @Test
    public void getNumberOfAvailableFloors() {
        int[] testAvailableFloors = {1,2,3,4,6,7};
        Buildings testBuilding = new Buildings("01","TestBuilding",7,6, testAvailableFloors);
        assertEquals(6, testBuilding.getNumberOfAvailableFloors());

    }

    @Test
    public void setNumberOfAvailableFloors() {
        int[] testAvailableFloors = {1,2,3,4,6,7};
        Buildings testBuilding = new Buildings("01","TestBuilding",7,6, testAvailableFloors);
        testBuilding.setNumberOfAvailableFloors(8);
        assertEquals(8, testBuilding.getNumberOfAvailableFloors());

    }

    @Test
    public void setAvailableFloors() {
        int[] testAvailableFloors = {1,2,3,4,5,6};
        List<Floors> availableFloors = new ArrayList<>();
        Buildings testBuilding = new Buildings("01","TestBuilding",7,6, testAvailableFloors);
        for (int i = 1;i<=7;i++)
        {
            Floors testFloor = new Floors(i,5);
            availableFloors.add(testFloor);
        }
        testBuilding.setAvailableFloors(availableFloors);
        assertEquals(availableFloors,testBuilding.getAvailableFloors());
    }
}

/*
* O problema este ca nu sunt impuse nici un fel de restrictii intre numberOfFloors, numberOfAvailableFloots si lista de obiecte floors care se genereaza,
* in momentul de fata numberOfAvailableFloors poate fi mai mare decat numberOfFloors si poate fi diferit de cate elemente sunt in arraiul dat ca parametru
* constructorului Buildings().
 */
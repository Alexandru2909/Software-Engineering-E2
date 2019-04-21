package com.frontend.frontend.BuildingLayout;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TestFloor {

    @Test
    public void getFloorNumberTest() {
        Floors testFloor = new Floors(1 ,10);
        assertEquals(1,testFloor.getFloorNumber());
    }

    @Test
    public void getNumberOfRoomsTest() {
        Floors testFloor = new Floors(1,10);
        assertEquals(10 ,testFloor.getNumberOfRooms());
    }

    @Test
    public void setFloorNumberTest() {
        Floors testFloor = new Floors(1,10);
        testFloor.setFloorNumber(4);
        assertEquals(4,testFloor.getFloorNumber());
    }

    @Test
    public void setNumberOfRoomsTest() {
        Floors testFloor = new Floors(1,10);
        testFloor.setNumberOfRooms(20);
        assertEquals(20,testFloor.getNumberOfRooms());

    }

    @Test
    public void setRoomsTest() {
        Floors testFloor = new Floors(1,10);
        List<Rooms> testRooms = new ArrayList<>();
        for (Integer i=1;i<=10;i++)
        {
            Rooms testRoom=new Rooms(i.toString(),i);
            testRooms.add(testRoom);
        }
        testFloor.setRooms(testRooms);
        assertEquals(true,testRooms.equals(testFloor.getRooms()));
    }

    @Test
    public void addRoomTest() {
        Floors testFloor = new Floors(1,10);
        List<Rooms> testRooms = new ArrayList<>();
        for (Integer i=1;i<=10;i++)
        {
            Rooms testRoom=new Rooms(i.toString(),i);
            testRooms.add(testRoom);
        }
        testFloor.setNumberOfRooms(10);
        testFloor.setRooms(testRooms);
        Rooms testRoom = new Rooms("11",11);
        testFloor.addRoom(testRoom);
        System.out.println(testFloor.getRooms().size());
        assertEquals(true,testFloor.getRooms().get(10).equals(testRoom));

    }
}
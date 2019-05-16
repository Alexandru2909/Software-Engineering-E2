package com.frontend.frontend.BuildingLayout;

import java.util.ArrayList;
import java.util.List;

public class Floors {
    int floorNumber = -100;
    int numberOfRooms = -1;
    List<Rooms> rooms = new ArrayList<>();

    public Floors(int floorNumber, int numberOfRooms){
        this.floorNumber = floorNumber;
        this.numberOfRooms = numberOfRooms;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public int getNumberOfRooms() {
        return numberOfRooms;
    }

    public List<Rooms> getRooms() {
        return rooms;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public void setNumberOfRooms(int numberOfRooms) {
        this.numberOfRooms = numberOfRooms;
    }

    public void setRooms(List<Rooms> rooms) {
        this.rooms = rooms;
    }

    public void addRoom (Rooms room){
        this.rooms.add(room);
    }
}

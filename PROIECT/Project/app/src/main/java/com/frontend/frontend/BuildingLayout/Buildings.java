package com.frontend.frontend.BuildingLayout;

import java.util.ArrayList;
import java.util.List;

public class Buildings {
    String id = new String();
    String name = new String();
    int numberOfFloors = -1;
    int numberOfAvailableFloors = -1;
    List<Floors> availableFloors = new ArrayList<>();

    public Buildings(String id, String name, int numberOfFloors, int numberOfAvailableFloors, int[] availableFloors) {
        this.id = id;
        this.name = name;
        this.numberOfFloors = numberOfFloors;
        this.numberOfAvailableFloors = numberOfAvailableFloors;
        setAvailableFloors(availableFloors);
    }

    private void setAvailableFloors(int... floorNumber){
        int numberOfRooms = 0; //place holder
        for(int number : floorNumber){
            availableFloors.add(new Floors(number, numberOfRooms));
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    public void setNumberOfFloors(int numberOfFloors) {
        this.numberOfFloors = numberOfFloors;
    }

    public int getNumberOfAvailableFloors() {
        return numberOfAvailableFloors;
    }

    public void setNumberOfAvailableFloors(int numberOfAvailableFloors) {
        this.numberOfAvailableFloors = numberOfAvailableFloors;
    }

    public List<Floors> getAvailableFloors() {
        return availableFloors;
    }

    public void setAvailableFloors(List<Floors> availableFloors) {
        this.availableFloors = availableFloors;
    }
}

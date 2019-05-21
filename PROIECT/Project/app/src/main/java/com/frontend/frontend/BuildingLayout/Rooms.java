package com.frontend.frontend.BuildingLayout;

public class Rooms {
    String id = new String();
    int floorNumber = -100;
    String info = new String(); // place holder

    public Rooms(String id, int floorNumber) {
        this.id = id;
        this.floorNumber = floorNumber;
    }

    public String getId() {
        return id;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public String getInfo() {
        return info;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

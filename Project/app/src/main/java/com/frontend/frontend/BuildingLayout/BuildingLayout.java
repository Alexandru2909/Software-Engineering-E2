package com.frontend.frontend.BuildingLayout;

import java.util.ArrayList;
import java.util.List;


public class BuildingLayout {
    List<Buildings> buildingsList = new ArrayList<>();

    public List<Buildings> getBuildingsList() {
        return buildingsList;
    }

    public void setBuildingsList(List<Buildings> buildingsList) {
        this.buildingsList = buildingsList;
    }

    /**
     * Identifies the selected building from the MainActivity
     * @return: The selected building.
     */
    public Buildings getSelectedBuilding(){
        String placeHolder = "1"; // place holder for the selected building's id
        for (Buildings building : buildingsList)
            if(placeHolder.equals(building.getId()))
                return building;

        return null; //error
    }

    /**
     * Searches the floor in the floor list of the building and returns the information about all its rooms.
     * @param floorNumber int: Number of the targeted floor
     * @return: Information about all the rooms of the selected floor
     */

    public String getFloorDetails(int floorNumber){
        Buildings building = getSelectedBuilding();
        List<Floors> availableFloors = building.getAvailableFloors();

        for(Floors floor : availableFloors){
            if(floorNumber == floor.getFloorNumber())
                for(Rooms room : floor.getRooms())
                    return room.getInfo();
        }

        return null;
    }
}

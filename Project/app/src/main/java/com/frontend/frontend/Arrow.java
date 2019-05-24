package com.frontend.frontend;

public class Arrow {
    private String orientation="N";

    public Arrow (){
        this.orientation = "N";
    }

    public int update(){
        switch (this.orientation){
            case "N":{
                return 0;
            }
            case "S":{
                return 180;
            }
            case "W":{
                return 270;
            }
            case "E":{
                return 90;
            }
        }
        return 0;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getOrientation() {
        return orientation;
    }
}

package com.frontend.frontend;

import android.Manifest;
import android.support.v4.app.ActivityCompat;

public class ActivityCompatUtil {
    boolean admitted;

    public ActivityCompatUtil(){
        this.admitted=false;
    }

    public boolean shouldShowRequestPermissionRationale(MockImageProcessing x ,
    Manifest.permission y){
        return this.admitted;
    }

    public void requestPermissions(MockImageProcessing x,String[] y , int CAMERA_PERM){
        if (y == Manifest.permission.CAMERA && CAMERA_PERM==2){
            this.admitted=true;
        }
    }

}

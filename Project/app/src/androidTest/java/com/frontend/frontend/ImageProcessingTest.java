package com.frontend.frontend;

import android.media.Image;
import android.os.Bundle;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class ImageProcessingTest {

    @Test
    public void onCreate() {
        Bundle bundleMock = Mockito.mock(Bundle.class);
        getRoomBtn = findViewById(R.id.getRoomBtn);
        getRoomBtn.setVisibility(View.INVISIBLE);
        getRoomTimetable = findViewById(R.id.getRoomTimeTable);
        getRoomTimetable.setVisibility(View.INVISIBLE);
        returnButton = findViewById(R.id.returnButton);
        cameraView = findViewById(R.id.cameraViewSurface);
        ocrTextView = findViewById(R.id.ocrTextView);
        Mockito.doReturn(11).when(bundleMock).getString(getRoomBtn);
        Mockito.doReturn(12).when(bundleMock).getString(getRoomTimeTable);
        Mockito.doReturn(13).when(bundleMock).getString(cameraViewSurface);
        Mockito.doReturn(14).when(bundleMock).getString(returnButton);
        Mockito.doReturn(15).when(bundleMock).getString(ocrTextView);
        ImageProcessing x=new ImageProcessing();
        onCreate(bundleMock);
        openMenu(bundleMock);
        onDestroy(bundleMock);
    }

    @Test
    public void openMenu(ImageProcessing x) {
        x.openMenu();
    }

    @Test
    public void onDestroy(ImageProcessing x) {
        x.onDestroy();
    }
}
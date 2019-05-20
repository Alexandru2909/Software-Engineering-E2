package com.frontend.frontend;

import android.support.v7.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

import com.frontend.backend.ARGuide.main.JSONResourceException;
import com.frontend.frontend.Main.MainActivity;
import com.frontend.frontend.Timetable.TimetableScreen;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.List;

import com.frontend.backend.ARGuide.main.ARGuide;

public class MockImageProcessing extends AppCompatActivity {

    private Button getRoomBtn;
    private Button returnButton;
    private Button getRoomTimetable;
    private CameraSource cameraSource;
    private TextRecognizer textRecognizer;
    private SurfaceView cameraView;
    private TextView ocrTextView;

    private String finalText;

    private static int CAMERA_PERM = 2;

    private List<String> roomsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_room);

        getRoomBtn = findViewById(R.id.getRoomBtn);
        getRoomBtn.setVisibility(View.INVISIBLE);
        getRoomTimetable = findViewById(R.id.getRoomTimeTable);
        getRoomTimetable.setVisibility(View.INVISIBLE);
        returnButton = findViewById(R.id.returnButton);
        cameraView = findViewById(R.id.cameraViewSurface);
        ocrTextView = findViewById(R.id.ocrTextView);

        final Toast warning = Toast.makeText(getApplicationContext(), "Room not found.", Toast.LENGTH_SHORT);
        warning.setGravity(Gravity.CENTER, 0, 0);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            startTextRecognizer();
        } else {
            askCameraPermission();
        }

        getRoomTimetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (roomsList.contains(finalText.trim())) {
                    Intent returnIntent = openTimetable();
                    returnIntent.putExtra("room", finalText.trim());
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                    startActivity(returnIntent);
                } else {
                    warning.show();
                }
            }
        });

        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMenu();
            }
        });
    }

    public Intent openMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        return intent;
    }

    private Intent openTimetable() {
        Intent intent = new Intent(this, TimetableScreen.class);
        return intent;
    }

    @Override
    public String onRequestPermissionsResult(int requestCode,
                                           @NonNull int[] grantResults) {
        if (requestCode != CAMERA_PERM) {
            return "SuperCalled";
        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            return "RecognizerStarted";
        }

    }

    private boolean askCameraPermission() {
        final String[] permissions = new String[]{Manifest.permission.CAMERA};
        ActivityCompatUtil act = new ActivityCompatUtil();
        if (!act.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            act.requestPermissions(this, permissions, CAMERA_PERM);
        }
    }

}

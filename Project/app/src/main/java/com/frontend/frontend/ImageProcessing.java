package com.frontend.frontend;

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
import com.frontend.backend.ARGuide.main.MyApplication;
import com.frontend.frontend.Main.MainActivity;
import com.frontend.frontend.Timetable.TimetableScreen;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;
import java.util.List;

import com.frontend.backend.ARGuide.main.ARGuide;

import static java.lang.System.exit;

/**
 * Tools for the pattern recognition and the camera functionality.
 */
public class ImageProcessing extends AppCompatActivity {

    private Button getRoomBtn;
    private Button returnButton;
    private Button getRoomTimetable;
    private CameraSource cameraSource;
    private TextRecognizer textRecognizer;
    private SurfaceView cameraView;
    private TextView ocrTextView;

    private String finalText;


    private List<String> roomsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.path = this.getFilesDir().getAbsolutePath();
        setContentView(R.layout.scan_room);

        getRoomBtn = findViewById(R.id.getRoomBtn);
        getRoomBtn.setVisibility(View.INVISIBLE);
        getRoomTimetable = findViewById(R.id.getRoomTimeTable);
        getRoomTimetable.setVisibility(View.INVISIBLE);
        returnButton = findViewById(R.id.returnButton);
        cameraView = findViewById(R.id.cameraViewSurface);
        ocrTextView = findViewById(R.id.ocrTextView);

        try {
            ARGuide databaseConn = new ARGuide("faculty_uaic_cs",
                    MyApplication.path+"/faculty.db",
                    MyApplication.path+"/facultySchedule.json",
                    MyApplication.path+"/buildingPlan.json");

            roomsList = databaseConn.selectAllClassroomNames();

            final Toast warning = Toast.makeText(getApplicationContext(), "Room not found.", Toast.LENGTH_SHORT);
            warning.setGravity(Gravity.CENTER, 0, 0);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                startTextRecognizer();
            }
        getRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (roomsList.contains(finalText.trim()) ){
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("room", finalText.trim());
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                } else {
                    warning.show();
                }
            }
        });

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
                    finish();
                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONResourceException e) {
            e.printStackTrace();
        }
    }

    public void openMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private Intent openTimetable() {
        Intent intent = new Intent(this, TimetableScreen.class);
        return intent;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraSource.release();
    }

    /**
     * Initializes text recognition
     */
    private void startTextRecognizer() {
        textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational()) {
            Toast.makeText(getApplicationContext(), "Oops ! Not able to start the text recognizer ...", Toast.LENGTH_LONG).show(); exit(0);
        }
            cameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
                    .setFacing(CameraSource.CAMERA_FACING_BACK)
                    .setRequestedPreviewSize(1280, 1024)
                    .setRequestedFps(15.0f)
                    .setAutoFocusEnabled(true)
                    .build();

            cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
                @Override
                public void surfaceCreated(SurfaceHolder holder) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        try {
                            cameraSource.start(cameraView.getHolder());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });

            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<TextBlock> detections) {
                    SparseArray<TextBlock> items = detections.getDetectedItems();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < items.size(); ++i) {
                        TextBlock item = items.valueAt(i);
                        if (item != null && item.getValue() != null) {
                            stringBuilder.append(item.getValue() + " ");
                        }
                    }

                    final String fullText = stringBuilder.toString();
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        public void run() {
                            ocrTextView.setText(fullText);
                            if (roomsList.contains(fullText.trim())) {
                                finalText = fullText;
                                getRoomTimetable.setText(finalText + " - See info");
                                getRoomBtn.setVisibility(View.VISIBLE);
                                getRoomTimetable.setVisibility(View.VISIBLE);
                                //startTextRecognizer();
                            }
                        }
                    });
                }
            });

    }
}
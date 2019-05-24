package com.frontend.frontend;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.Image;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.time.Clock;
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
public class SelectDestination extends AppCompatActivity implements SensorEventListener {

    private Button getRoomBtn;
    private Button returnButton;
    private CameraSource cameraSource;
    private TextRecognizer textRecognizer;
    private SurfaceView cameraView;
    private TextView ocrTextView;
    private ImageView arrowImg;
    private Arrow arrow;

    private String room = null;
    private String finalText;
    private String destRoom = null;

    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float azimuth = 0f;
    private float currectAzimuth = 0f;
    private SensorManager mSensorManager;

    private List<String> roomsList = new ArrayList<>();

    private boolean firstRun = true;
    private float firstAzimuth = 0f;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.path = this.getFilesDir().getAbsolutePath();
        setContentView(R.layout.destination_layout);

        getRoomBtn = findViewById(R.id.getRoomBtn);
        getRoomBtn.setVisibility(View.INVISIBLE);
        returnButton = findViewById(R.id.returnButton);
        cameraView = findViewById(R.id.cameraViewSurface);
        ocrTextView = findViewById(R.id.ocrTextView);

        arrowImg = findViewById(R.id.imageViewCompass);
        arrowImg.setVisibility(View.VISIBLE);
        arrow = new Arrow();
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);


        try {

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                room = extras.getString("current_room");
            }
            ARGuide databaseConn = new ARGuide("faculty_uaic_cs",
                    MyApplication.path+"/faculty.db",
                    MyApplication.path+"/facultySchedule.json",
                    MyApplication.path+"/buildingPlan.json");

            roomsList = databaseConn.selectAllClassroomNames();

            roomsList.add("C309");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("What room");
            final EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    destRoom = input.getText().toString();
                    arrowImg.setVisibility(View.VISIBLE);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            });
            builder.show();

            final Toast warning = Toast.makeText(getApplicationContext(), "Room not found.", Toast.LENGTH_SHORT);
            warning.setGravity(Gravity.CENTER, 0, 0);

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                startTextRecognizer();
            }

            returnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openMenu();
                }
            });
            getRoomBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (roomsList.contains(finalText.trim()) ){
                        getRoomBtn.setVisibility(View.INVISIBLE);
                        room = finalText;
                    } else {
                        warning.show();
                    }
                }
            });

        }catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONResourceException e) {
            e.printStackTrace();
        }
    }

    public void openMenu() {
        finish();
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
                            getRoomBtn.setVisibility(View.VISIBLE);
                            //startTextRecognizer();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener( this,mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener( this,mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        final float alpha=  0.97f;
        synchronized (this){
            if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            {
                mGravity[0] = alpha*mGravity[0]+(1-alpha)*sensorEvent.values[0];
                mGravity[1] = alpha*mGravity[1]+(1-alpha)*sensorEvent.values[1];
                mGravity[2] = alpha*mGravity[2]+(1-alpha)*sensorEvent.values[2];
            }
            if(sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
            {
                mGeomagnetic[0] = alpha*mGeomagnetic[0]+(1-alpha)*sensorEvent.values[0];
                mGeomagnetic[1] = alpha*mGeomagnetic[1]+(1-alpha)*sensorEvent.values[1];
                mGeomagnetic[2] = alpha*mGeomagnetic[2]+(1-alpha)*sensorEvent.values[2];
            }
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R,I,mGravity,mGeomagnetic);
            if(success)
            {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R,orientation);
                azimuth = (float)Math.toDegrees(orientation[0]);

                if(firstRun == true){
                    firstAzimuth = azimuth;
                    firstRun = false;
                }
                azimuth = (azimuth+360 - firstAzimuth)%360;

                System.out.println(azimuth);


                if  ((azimuth >315 && azimuth <= 360)  || (azimuth >= 0 && azimuth <= 45) ) {
                    arrow.setOrientation("N");
                }
                if  (azimuth >45 && azimuth <= 135 ) {
                    arrow.setOrientation("W");
                }
                if  (azimuth >135 && azimuth <=225) {
                    arrow.setOrientation("S");
                }
                if  (azimuth >225 && azimuth <=315) {
                    arrow.setOrientation("E");
                }



                //System.out.println(arrow.getOrientation());
                arrowImg.setRotation(arrow.update());

                currectAzimuth = azimuth;
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int i){ }
}
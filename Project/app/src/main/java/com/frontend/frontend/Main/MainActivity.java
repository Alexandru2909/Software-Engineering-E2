package com.frontend.frontend.Main;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.frontend.frontend.ImageProcessing;
import com.frontend.frontend.R;
import com.frontend.frontend.SelectDestination;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;

public class MainActivity extends AppCompatActivity {

    Button startOcrBtn;
    Button destinationBtn;
    String currentRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.CAMERA

                },
                100);


        startOcrBtn = findViewById(R.id.startOcrBtn);
        destinationBtn = findViewById(R.id.selectDestination);
        startOcrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOCR();
            }
        });
        destinationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDestination();
            }
        });
    }

    private void startOCR() {
        Intent ocrActivity = new Intent(getApplicationContext(), ImageProcessing.class);
        startActivityForResult(ocrActivity, 1);

    }

    private void startDestination() {
        Intent destActivity = new Intent(getApplicationContext(), SelectDestination.class);
        destActivity.putExtra("current_room", currentRoom);
        startActivity(destActivity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String ocrRoom = data.getStringExtra("room");
                // Do something with ocrRoom
                currentRoom = ocrRoom;

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Do something if no result got
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 100){
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED
                    && grantResults[3] == PackageManager.PERMISSION_GRANTED) {
            } else {
                Toast.makeText(this, "Permission is denied.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}

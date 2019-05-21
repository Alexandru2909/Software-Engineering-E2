package com.frontend.frontend.Main;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.frontend.frontend.ImageProcessing;
import com.frontend.frontend.R;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Paths;

public class MainActivity extends AppCompatActivity {

    Button startOcrBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        startOcrBtn = findViewById(R.id.startOcrBtn);
        startOcrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startOCR();
            }
        });
    }

    private void startOCR() {
        Intent ocrActivity = new Intent(getApplicationContext(), ImageProcessing.class);
        startActivityForResult(ocrActivity, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String ocrRoom = data.getStringExtra("room");
                // Do something with ocrRoom
//                startOcrBtn.setText(ocrRoom);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Do something if no result got
            }
        }
    }
}

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
        /* afisare director curent + creare un director test */
        //File f =Environment.getExternalStorageDirectory();;
        String path2 = this.getFilesDir().getAbsolutePath();
        String path = this.getFilesDir().getAbsolutePath() ;
        String path3 = this.getFilesDir().getAbsolutePath() + "/lastUpdateTime.txt";
        String path4 = this.getFilesDir().getAbsolutePath() + "/sectionNames.txt";
        System.out.println("\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.print(path);
        System.out.println("\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

        File folder = Paths.get(path).toFile();
        folder.mkdir();
        folder= new File (path3);
        folder= new File (path4);

        try
        {
            FileWriter fw=new FileWriter();
            folder.createNewFile();
        }
        catch (Exception e)
        {
            System.out.print(e);
        }

        folder = Paths.get(path2).toFile();
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }



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

package com.frontend.frontend.Timetable;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frontend.frontend.R;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;

public class TimetableScreen extends AppCompatActivity {
    String roomNumber = new String();
    TextView text;

    CarouselPicker dayPicker;
    List<CarouselPicker.PickerItem> daysList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable);

        TextView text = findViewById(R.id.testing);


        roomNumber = getIntent().getStringExtra("room");

        dayPicker = (CarouselPicker) findViewById(R.id.dayPicker);
        addDays("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
        CarouselPicker.CarouselViewAdapter dayAdapter = new CarouselPicker.CarouselViewAdapter(this, daysList, 0);
        dayPicker.setAdapter(dayAdapter);

        dayPicker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override public void onPageScrolled(int i, float v, int i1) {

            }

            @Override public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        TextView t = findViewById(R.id.subject1);
                        t.setText("POO");
                        break;
                    case 1:
                        TextView t1 = findViewById(R.id.subject1);
                        t1.setText("IP");
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                }
            }
            @Override public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void addDays(String... days){
        for(String day : days)
            this.daysList.add(new CarouselPicker.TextItem(day, 12));
    }
}
package com.frontend.frontend.Timetable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.frontend.frontend.R;

import java.util.ArrayList;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;

public class TimetableScreen extends AppCompatActivity {

    CarouselPicker dayPicker;
    List<CarouselPicker.PickerItem> daysList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable);

        dayPicker = (CarouselPicker) findViewById(R.id.dayPicker);
        addDays("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
        CarouselPicker.CarouselViewAdapter dayAdapter = new CarouselPicker.CarouselViewAdapter(this, daysList, 0);
        dayPicker.setAdapter(dayAdapter);
    }

    private void addDays(String... days){
        for(String day : days)
            this.daysList.add(new CarouselPicker.TextItem(day, 12));
    }
}

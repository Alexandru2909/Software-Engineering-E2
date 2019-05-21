package com.frontend.frontend.Timetable;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.frontend.backend.ARGuide.main.JSONResourceException;
import com.frontend.frontend.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;
import com.frontend.backend.ARGuide.main.ARGuide;

public class TimetableScreen extends AppCompatActivity {
    String roomNumber = new String();
    TextView text;

    String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    CarouselPicker dayPicker;
    List<CarouselPicker.PickerItem> daysList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable);

        roomNumber = getIntent().getStringExtra("room");

        dayPicker = (CarouselPicker) findViewById(R.id.dayPicker);
        addDays("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
        CarouselPicker.CarouselViewAdapter dayAdapter = new CarouselPicker.CarouselViewAdapter(this, daysList, 0);
        dayPicker.setAdapter(dayAdapter);

        try {
            ARGuide databaseConn = new ARGuide("faculty_uaic_cs",
                    "/data/user/0/com.frontend.frontend/files/faculty.db",
                    "/data/user/0/com.frontend.frontend/files/facultySchedule.json",
                    "/data/user/0/com.frontend.frontend/files/buildingPlan.json");

            final List<List<String>> schedule = databaseConn.selectClassroomSchedule(roomNumber);


            dayPicker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    writeInTable(days[i - 1], schedule);
                }

                @Override
                public void onPageScrollStateChanged(int i) {

                }
            });

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JSONResourceException e) {
            e.printStackTrace();
        }
    }

    private void addDays(String... days) {
        for (String day : days)
            this.daysList.add(new CarouselPicker.TextItem(day, 12));
    }

    private void writeInTable(String day, List<List<String>> schedule) {
        for (List<String> scheduleEntry : schedule) {
            if (scheduleEntry.get(0).equals(day))
                for (String scheduleData : scheduleEntry) {
                    String[] splitedScheduleData = scheduleData.split("\\s+");
                    if (splitedScheduleData[1].startsWith("08")) {
                        TextView subject = findViewById(R.id.subject1);
                        subject.setText(splitedScheduleData[3]);
                    } else if (splitedScheduleData[1].startsWith("10")) {
                        TextView subject = findViewById(R.id.subject2);
                        subject.setText(splitedScheduleData[3]);
                    } else if (splitedScheduleData[1].startsWith("12")) {
                        TextView subject = findViewById(R.id.subject3);
                        subject.setText(splitedScheduleData[3]);
                    } else if (splitedScheduleData[1].startsWith("14")) {
                        TextView subject = findViewById(R.id.subject4);
                        subject.setText(splitedScheduleData[3]);
                    } else if (splitedScheduleData[1].startsWith("16")) {
                        TextView subject = findViewById(R.id.subject5);
                        subject.setText(splitedScheduleData[3]);
                    } else if (splitedScheduleData[1].startsWith("18")) {
                        TextView subject = findViewById(R.id.subject6);
                        subject.setText(splitedScheduleData[3]);
                    }
                }
        }
    }
}
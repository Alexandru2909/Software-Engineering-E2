package com.frontend.frontend.Timetable;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.frontend.backend.ARGuide.main.JSONResourceException;
import com.frontend.backend.ARGuide.main.MyApplication;
import com.frontend.frontend.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import in.goodiebag.carouselpicker.CarouselPicker;

import com.frontend.backend.ARGuide.main.ARGuide;

public class TimetableScreen extends AppCompatActivity {
    String roomNumber = new String();
    TextView text;

    String[] days = {"LUNI", "MARTI", "MIERCURI", "JOI", "VINERI", "SAMBATA", "DUMINICA"};

    CarouselPicker dayPicker;
    List<CarouselPicker.PickerItem> daysList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable);

        roomNumber = getIntent().getStringExtra("room");

        dayPicker = (CarouselPicker) findViewById(R.id.dayPicker);
        addDays("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday" , "Sunday");
        CarouselPicker.CarouselViewAdapter dayAdapter = new CarouselPicker.CarouselViewAdapter(this, daysList, 0);
        dayPicker.setAdapter(dayAdapter);

        try {
            ARGuide databaseConn = new ARGuide("faculty_uaic_cs",
                    MyApplication.path + "/faculty.db",
                    MyApplication.path + "/facultySchedule.json",
                    MyApplication.path + "/buildingPlan.json");

            final List<List<String>> schedule = databaseConn.selectClassroomSchedule(roomNumber);


            writeInTable(days[0], schedule);
            dayPicker.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int i, float v, int i1) {

                }

                @Override
                public void onPageSelected(int i) {
                    //System.out.println("\n555555555555555555555555\n" + schedule + "\n555555555555555555555555\n");
                    writeInTable(days[i], schedule);
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
            List<String> newSchedule = new ArrayList<>();
            for(List<String> scheduleList : schedule)
                for(int i=0;i<scheduleList.size();i+=4){
                    newSchedule.add(scheduleList.get(i));
                    if(day.equals(scheduleList.get(i))) {
                        TextView subject = findViewById(R.id.subject1);
                        switch (scheduleList.get(i + 1)) {
                            case ("08:00"):
                                subject = findViewById(R.id.subject1);
                                break;
                            case ("10:00"):
                                subject = findViewById(R.id.subject2);
                                break;
                            case ("12:00"):
                                subject = findViewById(R.id.subject3);
                                break;
                            case ("14:00"):
                                subject = findViewById(R.id.subject4);
                                break;
                            case ("16:00"):
                                subject = findViewById(R.id.subject5);
                                break;
                            case ("18:00"):
                                subject = findViewById(R.id.subject6);
                                break;
                        }
                        subject.setText(scheduleList.get(i + 3));
                    }
        }
    }
}
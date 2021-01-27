package com.example.everyday;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import java.time.*;
import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CalendarActivity extends AppCompatActivity {
    private LocalDate now = LocalDate.now();
    private LocalDate disp = LocalDate.now();
    private TextView[][] monthList = null;
    private int first = 0;
    private int monthLength = 0;
    private String month = "";
    private int year = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        String date = getIntent().getStringExtra("date");
        disp = LocalDate.parse(date);
        // Set days of months

        int dispDay = disp.getDayOfWeek().getValue();
        int dispDays = disp.lengthOfMonth();

        //Set days of month in calendar
        findMonthData();



        monthList= new TextView[][]{
                {findViewById(R.id.b11), findViewById(R.id.b12), findViewById(R.id.b13), findViewById(R.id.b14), findViewById(R.id.b15), findViewById(R.id.b16), findViewById(R.id.b17)},
                {findViewById(R.id.b21), findViewById(R.id.b22), findViewById(R.id.b23), findViewById(R.id.b24), findViewById(R.id.b25), findViewById(R.id.b26), findViewById(R.id.b27)},
                {findViewById(R.id.b31), findViewById(R.id.b32), findViewById(R.id.b33), findViewById(R.id.b34), findViewById(R.id.b35), findViewById(R.id.b36), findViewById(R.id.b37)},
                {findViewById(R.id.b41), findViewById(R.id.b42), findViewById(R.id.b43), findViewById(R.id.b44), findViewById(R.id.b45), findViewById(R.id.b46), findViewById(R.id.b47)},
                {findViewById(R.id.b51), findViewById(R.id.b52), findViewById(R.id.b53), findViewById(R.id.b54), findViewById(R.id.b55), findViewById(R.id.b56), findViewById(R.id.b57)},
                {findViewById(R.id.b61), findViewById(R.id.b62), findViewById(R.id.b63), findViewById(R.id.b64), findViewById(R.id.b65), findViewById(R.id.b66), findViewById(R.id.b67)}
        };
        setMonthData();
    }

    public void findMonthData(){
        first = disp.withDayOfMonth(1).getDayOfWeek().getValue();
        monthLength = disp.lengthOfMonth();
        month = disp.getMonth().toString();
        year = disp.getYear();
        TextView monthView = findViewById(R.id.month);
        monthView.setText(month + " " + year);
    }

    public void setMonthData(){
        int dates = 0;
        for(int i = 0; i < monthList.length; i++){
            for(int j = 0; j < monthList[0].length; j++){
                monthList[i][j].setEnabled(true);
                monthList[i][j].setBackgroundColor(0xffffff);
                monthList[i][j].setTextColor(Color.BLACK);
                if((i == 0 && j ==first - 1) || (dates > 0 && dates < monthLength)){
                    dates++;
                    monthList[i][j].setText(Integer.toString(dates));
                    if(now.getMonth() == disp.getMonth() && now.getDayOfMonth() == dates){
                        monthList[i][j].setTextColor(Color.RED);
                    }
                }else{
                    monthList[i][j].setText("");
                    monthList[i][j].setEnabled(false);
                }

            }
        }
    }

    public void prevCal(View view){
        disp = disp.minusMonths(1);
        findMonthData();
        setMonthData();
    }

    public void resetCal(View view){
        disp = now;
        findMonthData();
        setMonthData();
    }

    public void nextCal(View view){
        disp = disp.plusMonths(1);
        findMonthData();
        setMonthData();
    }

    public void click(View view){
        String mon = Integer.toString(disp.getMonthValue());
        if(disp.getMonthValue() < 10){
            mon = 0 + mon;
        }
        int click = view.getId();
        TextView res = findViewById(click);
        String day = res.getText().toString();
        if(Integer.parseInt(day) < 10){
            day = 0 + day;
        }
        String selected = year + "-" + mon + "-"+ day;
        Intent i = new Intent();
        i.putExtra("date", selected);
        setResult(RESULT_OK, i);
        CalendarActivity.this.finish();

    }
}

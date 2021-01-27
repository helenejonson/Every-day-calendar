package com.example.everyday;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
    private ArrayList<String> readFromFile = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        String date = getIntent().getStringExtra("date");
        disp = LocalDate.parse(date);
        findMonthData();
        readFromFile();
        ArrayList<String> n = readFromFile;
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.setting){
            Intent intent = new Intent("com.example.everyday.SettingsActivity");
            startActivityForResult(intent, 2);
        }
        return super.onOptionsItemSelected(item);
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
                    String mon = Integer.toString(disp.getMonthValue());
                    if(disp.getMonthValue() < 10){
                        mon = 0 + mon;
                    }
                    String day = Integer.toString(dates);
                    if(dates < 10){
                        day = 0 + day;
                    }
                    String selected = year + "-" + mon + "-"+ day;
                    if(readFromFile.contains(selected)){
                        monthList[i][j].setBackgroundColor(Color.parseColor("#B5F49E"));
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

    public void back(View view){
        Intent i = new Intent();
        i.putExtra("date", disp.toString());
        setResult(RESULT_OK, i);
        CalendarActivity.this.finish();
    }

    public void readFromFile(){
        File dir = getFilesDir();
        File file = new File(dir, "myDates.txt");
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        try {
            fileReader = new FileReader(file);
            bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                readFromFile.add(line);
                line = bufferedReader.readLine();
            }
        } catch (Exception e) {
            Log.d("READ", e.toString());
        } finally {
            try {
                if (bufferedReader != null) bufferedReader.close();
                if (fileReader != null) fileReader.close();
            } catch (Exception e) {
                Log.d("READ", "Error while closing bufferreader and filereader.");
            }
        }
    }
}

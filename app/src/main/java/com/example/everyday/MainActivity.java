package com.example.everyday;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {
    private LocalDate now = LocalDate.now();
    private LocalDate disp = LocalDate.now();
    private DateTimeFormatter form = DateTimeFormatter.ofPattern("E MMM dd yyyy");
    private ArrayList<String> readFromFile = new ArrayList<>();
    private TextView res = null;
    private File dir = null;
    private File file = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        res = findViewById(R.id.date);
        res.setText(form.format(now));
        dir = getFilesDir();
        file = new File(dir, "myDates.txt");
        readFromFile();
        valid();
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

    public void resetDay(View view){
        for(int i = 0; i < readFromFile.size();i++){
            if(disp.toString().equals(readFromFile.get(i))){
                readFromFile.remove(i);
                break;
            }
        }
        File file = new File(dir, "myDates.txt");
        PrintWriter writer = null;
        try {
            for(int i = 0; i <= readFromFile.size(); i++){
                if(i == 0){
                    writer = new PrintWriter(file);
                }else{
                    writer = new PrintWriter(new FileWriter(file, true));
                }
                writer.println(readFromFile.get(i));
            }
        } catch (Exception e){
            Log.d("WRITER", e.toString());
        } finally {
            assert writer != null;
            writer.close();
        }
        readFromFile = new ArrayList<>();
        readFromFile();
        valid();
        res.setText(form.format(disp));
    }

    public void cal(View view){
        Intent i = new Intent("com.example.everyday.CalendarActivity");
        i.putExtra("date", disp.toString());
        startActivityForResult(i, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String date = data.getExtras().getString("date");
            disp = LocalDate.parse(date);
            valid();
            res.setText(form.format(disp));
        }else if(requestCode == 2 && resultCode == RESULT_OK){
            disp = now;
            readFromFile = new ArrayList<>();
            readFromFile();
            valid();
            res.setText(form.format(disp));
        }
    }

    public void prev(View view){
        LocalDate prev = disp.minus(1, ChronoUnit.DAYS);
        disp = prev;
        valid();
        res.setText(form.format(prev));
    }

    public void next(View view){
        LocalDate next = disp.plus(1, ChronoUnit.DAYS);
        disp = next;
        valid();
        res.setText(form.format(next));
    }

    public void valid(){
        TextView btn = findViewById(R.id.did);
        TextView resetbtn = findViewById(R.id.resetDay);
        btn.setBackgroundColor(Color.LTGRAY);
        resetbtn.setEnabled(false);
        if(disp.isAfter(now)){
            btn.setEnabled(false);
        }else{
            btn.setEnabled(true);
            resetbtn.setEnabled(false);
            for(int i = 0; i < readFromFile.size(); i++){
                if(readFromFile.contains(disp.toString())){
                    btn.setEnabled(false);
                    resetbtn.setEnabled(true);
                    btn.setBackgroundColor(Color.parseColor("#B5F49E"));
                    break;
                }
            }
        }
    }

    public void reset(View view){
        disp = now;
        valid();
        res.setText(form.format(now));
    }

    public void did(View view){
        TextView btn = findViewById(R.id.did);
        btn.setEnabled(false);
        TextView resetbtn = findViewById(R.id.resetDay);
        resetbtn.setEnabled(true);
        btn.setBackgroundColor(Color.parseColor("#B5F49E"));
        File file = new File(dir, "myDates.txt");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileWriter(file, true));
            writer.println(disp);
        } catch (Exception e){
            Log.d("WRITER", e.toString());
        } finally {
            writer.close();
        }
        readFromFile();
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
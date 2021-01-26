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
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {
    private LocalDate now = LocalDate.now();
    private LocalDate disp = LocalDate.now();
    private DateTimeFormatter form = DateTimeFormatter.ofPattern("E MMM dd yyyy");
    private ArrayList<String> readFromFile = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView res = findViewById(R.id.date);
        res.setText(form.format(now));
        //File file = new File(getFilesDir(), "myDates.txt"); //Creation of file
        //resetFile(); // Resets file with no dates
        readFromFile();
        valid();
    }

    public void cal(View view){
        Intent i = new Intent("com.example.everyday.CalendarActivity");
        i.putExtra("date", disp.toString());
        startActivity(i);
    }

    public void resetFile(){
        File dir = getFilesDir();
        File file = new File(dir, "myDates.txt");
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file);
            writer.print("");

        } catch (Exception e){
            Log.d("WRITER", e.toString());
        } finally {
            writer.close();
        }
    }

    public void prev(View view){
        TextView res = findViewById(R.id.date);
        LocalDate prev = disp.minus(1, ChronoUnit.DAYS);
        disp = prev;
        valid();
        res.setText(form.format(prev));

    }

    public void next(View view){
        LocalDate next = disp.plus(1, ChronoUnit.DAYS);
        disp = next;
        valid();
        TextView res = findViewById(R.id.date);
        res.setText(form.format(next));

    }

    public void valid(){
        int l = readFromFile.size();
        TextView btn = findViewById(R.id.did);
        if(disp.isAfter(now)){
            btn.setEnabled(false);
        }else{
            btn.setEnabled(true);
            for(int i = 0; i < readFromFile.size(); i++){
                if(readFromFile.get(i).equals(disp.toString())){
                    btn.setEnabled(false);
                    break;
                }
            }
        }

    }

    public void reset(View view){
        disp = now;
        valid();
        TextView res = findViewById(R.id.date);
        res.setText(form.format(now));
    }

    public void did(View view){
        TextView btn = findViewById(R.id.did);
        btn.setEnabled(false);
        File dir = getFilesDir();
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
        ArrayList<String> fromFile = new ArrayList<>();
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
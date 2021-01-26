package com.example.everyday;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CalendarActivity extends AppCompatActivity {
    private LocalDate now = LocalDate.now();
    private LocalDate disp = LocalDate.now();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        String date = getIntent().getStringExtra("date");
        LocalDate dat = LocalDate.parse(date);
        if(dat instanceof LocalDate){
            TextView res = findViewById(R.id.test);
            res.setText(dat.toString());
        }

    }
}

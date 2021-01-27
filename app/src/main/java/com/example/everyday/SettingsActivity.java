package com.example.everyday;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        TextView btn = findViewById(R.id.resetFile);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle(R.string.resetTitle);
                builder.setMessage(R.string.resetDesc);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetFile();
                        Intent i = new Intent();
                        i.putExtra("Settings", 1);
                        setResult(RESULT_OK, i);
                        SettingsActivity.this.finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    public void back(View view){
        Intent i = new Intent();
        i.putExtra("Settings", 1);
        setResult(RESULT_OK, i);
        SettingsActivity.this.finish();
    }
}

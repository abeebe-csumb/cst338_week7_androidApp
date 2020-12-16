package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private static String[][] courseList;
    TextView tv_courseList;
    Context mContext;
    TextView tv_courseDesc;
    Button btn_submit;
    EditText et_input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String courseLabelText = "My Courses";
        tv_courseList = findViewById(R.id.tv_courseList);
        tv_courseDesc = findViewById(R.id.tv_courseDesc);
        tv_courseDesc.setVisibility(View.GONE);

        mContext = this.getApplicationContext();
        AssetManager am = mContext.getAssets();
        try {
            InputStream is = am.open(getString(R.string.fileName));
            courseLabelText = loadCourseLibraryFromFile(is);
            tv_courseList.setText(courseLabelText);
        } catch (IOException io) {
            Log.d("MyApplication", io.getMessage());
        }

        et_input = findViewById(R.id.et_input);

        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = et_input.getText().toString().trim();
                int courseNum;
                try {
                    courseNum = Integer.parseInt(input);
                    String courseDesc = findCourseDescById(courseNum);
                    if(courseDesc.equals(null)) {
                        tv_courseDesc.setText("No Courses Found for Course Number: " + courseNum);
                    } else {
                        tv_courseDesc.setText(courseDesc);
                    }
                } catch (Exception e) {
                    Log.d("MyApplication", e.getMessage());
                    tv_courseDesc.setText("Invalid Course Number Entered!");
                }
                tv_courseDesc.setVisibility(View.VISIBLE);
            }
        });
    }

    String findCourseDescById(int id) {
        String courseDesc = "";
        for(int i = 0; i < courseList.length; i++) {
            if(courseList[i][0].contains(id+"")) {
                courseDesc = courseList[i][1];
            }
        }
        return courseDesc;
    }

    String loadCourseLibraryFromFile(InputStream is) {
        courseList = new String[15][2];
        String courseLabelText = "";
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            int index = 0;
            String line;

            while ((line = reader.readLine()) != null) {
                courseList[index][0] = line.substring(1, line.indexOf('|')).trim();
                courseList[index][1] = line.substring(line.indexOf('|') + 1, line.length() - 1).trim();
                courseLabelText = courseLabelText + courseList[index][0] + "\n";
                index++;
            }
            reader.close();
            Log.d("MyApplication", line);
        } catch (Exception e) {
            // do nothing
        }
        return courseLabelText;
    }
}
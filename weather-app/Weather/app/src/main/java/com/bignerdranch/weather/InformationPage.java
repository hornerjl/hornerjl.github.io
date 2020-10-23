package com.bignerdranch.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InformationPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_page);

        TextView dateView = findViewById(R.id.TextViewDate);
        SimpleDateFormat sdf = new SimpleDateFormat("E MM.dd.yyyy");
        String currentDateandTime = sdf.format(new Date());
        dateView.setText(currentDateandTime);


        Intent i = getIntent();
        String data = i.getStringExtra("weatherData");

        String information = null;
        try {
            information = WeatherDataParser.runParser(data);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TextView informationView = findViewById(R.id.textViewInformation);
        informationView.setText(information);

        informationView.setMovementMethod(new ScrollingMovementMethod());
    }






}
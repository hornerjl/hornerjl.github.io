package com.bignerdranch.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private Spinner mMenuChoice;
    private EditText mUserLocation;
    private Button mNextButton;

    Handler mainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message inputMessage) {
            openNewActivity(inputMessage);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUserLocation = (EditText) findViewById(R.id.textInputEditText);

        mMenuChoice = (Spinner) findViewById(R.id.menu_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
            R.array.menu_choice_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMenuChoice.setAdapter(adapter);

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                weatherAPICall();
            }
        });
    }

    public void weatherAPICall() {
        Runnable task = new Runnable() {
            @Override
            public void run() {

                String userLocation = mUserLocation.getText().toString();
                String menuChoice = mMenuChoice.getSelectedItem().toString();

                WeatherAPIs.run(menuChoice, userLocation);

                Message completeMessage = mainHandler.obtainMessage(0, WeatherAPIs.mWeatherData);
                completeMessage.sendToTarget();

            }
        };

        new Thread(task).start();
    }

    public void openNewActivity(Message inputMessage){
        Intent intent = new Intent(this, InformationPage.class);

        intent.putExtra("weatherData", inputMessage.obj.toString());

        startActivity(intent);

    }
}
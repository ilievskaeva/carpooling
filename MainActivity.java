package com.example.carpooling_f;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.carpooling_f.R;

public class MainActivity extends AppCompatActivity {

    private Button btnDriver, btnPassenger, btnPassLog, btnDriLog;


    //@SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDriver = findViewById(R.id.btn_driver);
        btnPassenger = findViewById(R.id.btn_passenger);
        btnPassLog = findViewById(R.id.btn_passenger_log);
        btnDriLog = findViewById(R.id.btn_driver_log);

        if (savedInstanceState == null) {

            btnDriver.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, DriverRegistrationActivity.class);
                startActivity(intent);
            });

            btnPassenger.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, PassengerRegistrationActivity.class);
                startActivity(intent);
            });

            btnPassLog.setOnClickListener(v -> {
                Intent intent = new Intent (MainActivity.this, PassengerLoginActivity2.class);
                startActivity(intent);
            });

            btnDriLog.setOnClickListener(v -> {
                Intent intent = new Intent (MainActivity.this, DriverLoginActivity2.class);
                startActivity(intent);
            });
        }
    }
}
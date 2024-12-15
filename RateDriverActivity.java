package com.example.carpooling_f;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RateDriverActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private Button submitButton;
    private String driverEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_driver);

        driverEmail = getIntent().getStringExtra("driverEmail");

        ratingBar = findViewById(R.id.ratingBar);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(v -> submitRating());
    }

    private void submitRating() {

        float rating = ratingBar.getRating();

        if (rating > 0) {

            DriverDatabaseHelper dbHelper = new DriverDatabaseHelper(this);
            dbHelper.updateDriverRating(driverEmail, rating);

            Toast.makeText(this, "Оценката е успешно поднесена!", Toast.LENGTH_SHORT).show();

            finish();
        } else {
            Toast.makeText(this, "Оцени го возачот!", Toast.LENGTH_SHORT).show();
        }
    }
}
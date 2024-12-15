package com.example.carpooling_f;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DriverProfileActivity extends AppCompatActivity {

    private TextView driverRatingTextView;
    private RatingBar passengerRatingBar;
    private EditText passengerEmailEditText;
    private Button submitRatingButton;
    private String driverEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);

        driverEmail = getIntent().getStringExtra("driver_email");

        driverRatingTextView = findViewById(R.id.driverRatingTextView);
        passengerRatingBar = findViewById(R.id.passengerRatingBar);
        passengerEmailEditText = findViewById(R.id.passengerEmailEditText);
        submitRatingButton = findViewById(R.id.submitRatingButton);

        loadDriverRating();

        submitRatingButton.setOnClickListener(v -> submitPassengerRating());
    }

    private void loadDriverRating() {
        DriverDatabaseHelper dbHelper = new DriverDatabaseHelper(this);
        float rating = dbHelper.getDriverRating(driverEmail);
        driverRatingTextView.setText("Tвојот рејтинг изнесува: " + rating);
    }

    private void submitPassengerRating() {
        String passengerEmail = passengerEmailEditText.getText().toString();
        float rating = passengerRatingBar.getRating();

        if (rating > 0 && !passengerEmail.isEmpty()) {
            PassengerDBHelper dbHelper = new PassengerDBHelper(this);
            dbHelper.updatePassengerRating(passengerEmail,  driverEmail, rating);

            Toast.makeText(this, "Оценката е поднесена успешно!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Не е внесено е-маил или оценка!", Toast.LENGTH_SHORT).show();
        }
    }
}

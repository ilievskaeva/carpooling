package com.example.carpooling_f;
import android.os.Bundle;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.LatLng;

public class DriverLoginActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private EditText emailEditText, passwordEditText, drivingTimeEditText, drivingTimeEditText2, priceEditText, vehicleTypeEditText;
    private Button loginButton;
    private GoogleMap mMap;
    private LatLng currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login2);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        drivingTimeEditText = findViewById(R.id.drivingTimeEditText);
        drivingTimeEditText2 = findViewById(R.id.drivingTimeEditText2);
        priceEditText = findViewById(R.id.priceEditText);
        vehicleTypeEditText = findViewById(R.id.vehicleTypeEditText);
        loginButton = findViewById(R.id.loginButton);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map2);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String drivingTime = drivingTimeEditText.getText().toString().trim();
            String drivingTime2 = drivingTimeEditText2.getText().toString().trim();
            String price = priceEditText.getText().toString().trim();
            String vehicleType = vehicleTypeEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(drivingTime) ||
                    TextUtils.isEmpty(price) || TextUtils.isEmpty(drivingTime2) || TextUtils.isEmpty(vehicleType) || currentLocation == null) {
                Toast.makeText(DriverLoginActivity2.this, "Пополнете ги сите полиња и поставете локација!", Toast.LENGTH_SHORT).show();
                return;
            }

            DriverDatabaseHelper dbHelper = new DriverDatabaseHelper(DriverLoginActivity2.this);

            boolean isLoggedIn = dbHelper.loginDriver(email, password);

            if (isLoggedIn) {
                Toast.makeText(DriverLoginActivity2.this, "Најавата е успешна!", Toast.LENGTH_SHORT).show();

                double latitude = currentLocation.latitude;
                double longitude = currentLocation.longitude;
                boolean isDriverInfoUpdated = dbHelper.updateDriverInfo(email, drivingTime, drivingTime2, price, vehicleType, latitude, longitude);

                if (isDriverInfoUpdated) {
                    Intent intent = new Intent(DriverLoginActivity2.this, DriverProfileActivity.class).putExtra("driver_email", email);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(DriverLoginActivity2.this, "Грешка при ажурирањето на податоците!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(DriverLoginActivity2.this, "Невалиден емаил или лозинка!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(latLng -> {
            if (currentLocation == null) {
                currentLocation = latLng;
                //mMap.clear();
                mMap.addMarker(new MarkerOptions().position(currentLocation).title("Моментална локација"));
            } else {
                Toast.makeText(DriverLoginActivity2.this, "Локацијата е веќе поставена!", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
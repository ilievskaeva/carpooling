package com.example.carpooling_f;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class PassengerLoginActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private PassengerDBHelper dbHelper;
    private DriverDatabaseHelper dbHelper2;

    private static final int REQUEST_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_login2);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        dbHelper2 = new DriverDatabaseHelper(this);
        dbHelper = new PassengerDBHelper(this);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(PassengerLoginActivity2.this, "Пополни ги сите полиња!", Toast.LENGTH_SHORT).show();
            } else {
                if (dbHelper.isUserValid(email, password)) {
                    double[] coordinates = dbHelper.getPassengerCoordinates(email);
                    double startLat = coordinates[0];
                    double startLng = coordinates[1];
                    double destLat = coordinates[2];
                    double destLng = coordinates[3];

                    if (startLat == 0.0 || startLng == 0.0 || destLat == 0.0 || destLng == 0.0) {
                        Toast.makeText(PassengerLoginActivity2.this, "Поставете почетна точка и дестинација на мапата! ", Toast.LENGTH_SHORT).show();
                    } else {

                        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("passenger_email", email);
                        editor.putString("current_lat", String.valueOf(startLat));
                        editor.putString("current_lng", String.valueOf(startLng));
                        editor.apply();

                        Intent intent = new Intent(PassengerLoginActivity2.this, VehicleSearchActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(PassengerLoginActivity2.this, "Невалидни акредитиви", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private LatLng startPoint = null;
    private LatLng destinationPoint = null;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(latLng -> {
            if (startPoint == null) {
                startPoint = latLng;
                mMap.addMarker(new MarkerOptions().position(startPoint).title("Моментална локација"));
            } else if (destinationPoint == null) {
                destinationPoint = latLng;
                mMap.addMarker(new MarkerOptions().position(destinationPoint).title("Дестинација"));
                saveLocations(startPoint.latitude, startPoint.longitude, destinationPoint.latitude, destinationPoint.longitude);
            } else {
                Toast.makeText(PassengerLoginActivity2.this, "Двата маркери се веќе поставени! ", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void saveLocations(double currentLat, double currentLng, double destLat, double destLng)
    {
        String email = emailEditText.getText().toString();
        dbHelper.saveLocations(email, currentLat, currentLng, destLat, destLng);
    }




    private void navigateToVehicleSearch() {
        String email = emailEditText.getText().toString();
        double[] currentLocation = dbHelper.getPassengerCoordinates(email);

        List<Vehicle> vehicles = dbHelper2.getVehiclesNearby(currentLocation[0], currentLocation[1]);

        if (vehicles.isEmpty()) {
            Toast.makeText(this, "Нема возила на оваа локација", Toast.LENGTH_SHORT).show();
        } else {

            Intent intent = new Intent(PassengerLoginActivity2.this, VehicleSearchActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
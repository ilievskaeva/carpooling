package com.example.carpooling_f;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VehicleSearchActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button acceptButton;
    private VehicleAdapter vehicleAdapter;
    private List<com.example.carpooling_f.Vehicle> vehicleList = new ArrayList<>();
    private DriverDatabaseHelper dbHelper;
    private PassengerDBHelper passengerDbHelper;
    private double userLatitude;
    private double userLongitude;

    private Button historyButton;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vehicle_search);

        dbHelper = new DriverDatabaseHelper(this);
        passengerDbHelper = new PassengerDBHelper(this);

        recyclerView = findViewById(R.id.vehicleRecyclerView);
        acceptButton = findViewById(R.id.acceptButton);

        historyButton = findViewById(R.id.historybutton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getUserLocation();

        loadVehicles();

        vehicleAdapter = new VehicleAdapter(vehicleList);
        recyclerView.setAdapter(vehicleAdapter);

        acceptButton.setOnClickListener(v -> acceptFirstVehicle());
        historyButton.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            String passengerEmail = sharedPreferences.getString("passenger_email", null);
            Intent intent = new Intent(VehicleSearchActivity.this, HistoryActivity.class);
            intent.putExtra("passenger_email", passengerEmail);
            startActivity(intent);
        });
    }


    private void acceptFirstVehicle() {
        if (!vehicleList.isEmpty()) {
            com.example.carpooling_f.Vehicle selectedVehicle = vehicleList.get(0);

            Intent intent = new Intent(this, RateDriverActivity.class);
            intent.putExtra("driverEmail", selectedVehicle.getDriverName());
            startActivity(intent);
        }
    }

    private void getUserLocation() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String passengerEmail = sharedPreferences.getString("passenger_email", null);

        if (passengerEmail != null) {

            double[] coordinates = passengerDbHelper.getPassengerCoordinates(passengerEmail);

            userLatitude = coordinates[0];
            userLongitude = coordinates[1];
        } else {
            Toast.makeText(this, "Нема најавен корисник", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadVehicles() {
        List<com.example.carpooling_f.Vehicle> vehicles = dbHelper.getVehiclesNearby(userLatitude, userLongitude);

        Collections.sort(vehicles, (v1, v2) -> Float.compare(v2.getRating(), v1.getRating()));

        recyclerView = findViewById(R.id.vehicleRecyclerView);
        //recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        vehicleList.clear();
        vehicleList.addAll(vehicles);

        vehicleAdapter = new VehicleAdapter(vehicles);

        vehicleAdapter.notifyDataSetChanged();

        recyclerView.setAdapter(vehicleAdapter);




    }


}
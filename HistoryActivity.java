package com.example.carpooling_f;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView historyRecyclerView;
    private List<RideHistory> rideHistoryList;
    private RideHistoryAdapter rideHistoryAdapter;
    private RideHistoryDBHelper rideHistoryDBHelper;
    private String passengerEmail;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        passengerEmail = getIntent().getStringExtra("passenger_email");

        rideHistoryDBHelper = new RideHistoryDBHelper(this);

        historyRecyclerView = findViewById(R.id.historyRecyclerView);
        historyRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadRideHistory();

        if (rideHistoryList.isEmpty()) {
            Toast.makeText(this, "Немате историја на возења", Toast.LENGTH_SHORT).show();
        } else {

            rideHistoryAdapter = new RideHistoryAdapter(rideHistoryList);
            historyRecyclerView.setAdapter(rideHistoryAdapter);
        }

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(HistoryActivity.this, VehicleSearchActivity.class);
            startActivity(intent);
            finish();
        });
    }

    public void loadRideHistory() {

        rideHistoryList = rideHistoryDBHelper.getRidesByPassenger(passengerEmail);

        if (rideHistoryAdapter != null) {
            rideHistoryAdapter.notifyDataSetChanged();
        }
    }
}
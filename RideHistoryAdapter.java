package com.example.carpooling_f;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RideHistoryAdapter extends RecyclerView.Adapter<RideHistoryAdapter.ViewHolder> {

    private List<RideHistory> rideHistoryList;

    public RideHistoryAdapter(List<RideHistory> rideHistoryList) {
        this.rideHistoryList = rideHistoryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ride_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RideHistory rideHistory = rideHistoryList.get(position);

        holder.vehicleTypeTextView.setText("Тип на возило: " + rideHistory.getVehicleType());
        holder.priceTextView.setText("Цена: " + rideHistory.getPrice() + " ден.");
        holder.startLocationTextView.setText("Почетна локација: " + rideHistory.getStartLat()+ ", " + rideHistory.getStartlon());
        holder.endLocationTextView.setText("Крајна локација: " + rideHistory.getEndLat()+ " , "+ rideHistory.getEndlon() );

    }

    @Override
    public int getItemCount() {
        return rideHistoryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView vehicleTypeTextView, priceTextView, startLocationTextView, endLocationTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            vehicleTypeTextView = itemView.findViewById(R.id.vehicleTypeTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            startLocationTextView = itemView.findViewById(R.id.startLocationTextView);
            endLocationTextView = itemView.findViewById(R.id.endLocationTextView);
        }
    }
}

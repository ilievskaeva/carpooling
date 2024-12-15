package com.example.carpooling_f;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.VehicleViewHolder> {
    private List<com.example.carpooling_f.Vehicle> vehicleList;

    public VehicleAdapter(List<com.example.carpooling_f.Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    @Override
    public VehicleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle, parent, false);
        return new VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VehicleViewHolder holder, int position) {
        com.example.carpooling_f.Vehicle vehicle = vehicleList.get(position);
        holder.bind(vehicle);
    }

    @Override
    public int getItemCount() {
        return vehicleList.size();
    }

    public class VehicleViewHolder extends RecyclerView.ViewHolder {
        private TextView driverName, vehicleType, price, rating;

        public VehicleViewHolder(View itemView) {
            super(itemView);
            driverName = itemView.findViewById(R.id.driverName);
            vehicleType = itemView.findViewById(R.id.vehicleType);
            price = itemView.findViewById(R.id.price);
            rating = itemView.findViewById(R.id.rating);
        }

        public void bind(com.example.carpooling_f.Vehicle vehicle) {
            driverName.setText(vehicle.getDriverName());
            vehicleType.setText(vehicle.getVehicleType());
            price.setText(String.valueOf(vehicle.getPrice()));
            rating.setText(String.valueOf(vehicle.getRating()));
        }
    }
}
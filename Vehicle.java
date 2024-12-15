package com.example.carpooling_f;
public class Vehicle {
    private String driverName;
    private String vehicleType;
    private double price;
    private double latitude;
    private double longitude;
    private float rating;

    public Vehicle(String driverName, String vehicleType, double price, double latitude, double longitude, float rating) {
        this.driverName = driverName;
        this.vehicleType = vehicleType;
        this.price = price;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String toString() {
        return "Vehicle{" +
                "driverName='" + driverName + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", price=" + price +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", rating=" + rating +
                '}';
    }
}
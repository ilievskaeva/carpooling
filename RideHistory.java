package com.example.carpooling_f;

public class RideHistory {
    private int id;
    private String vehicleType;
    private String startlat;
    private String startlon;
    private String endlon;
    private String endlat;
    private String driverEmail;
    private String passengerEmail;
    private int price;

    public RideHistory(int id, String vehicleType, String startlat, String startlon,String endlat, String endlon, String driverEmail, String passengerEmail) {
        this.id = id;
        this.vehicleType = vehicleType;
        this.endlat=endlat;
        this.endlon=endlon;
        this.startlat=startlat;
        this.startlon=startlon;
        this.driverEmail = driverEmail;
        this.passengerEmail = passengerEmail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getStartLat() {
        return startlat;
    }


    public String getEndLat() {
        return endlat;
    }

    public String getEndlon(){
        return endlon;
    }

    public String getStartlon(){
        return startlon;
    }

    public String getDriverEmail() {
        return driverEmail;
    }

    public void setDriverEmail(String driverEmail) {
        this.driverEmail = driverEmail;
    }

    public String getPassengerEmail() {
        return passengerEmail;
    }

    public int getPrice(){
        return price;
    }
    public void setPassengerEmail(String passengerEmail) {
        this.passengerEmail = passengerEmail;
    }
}


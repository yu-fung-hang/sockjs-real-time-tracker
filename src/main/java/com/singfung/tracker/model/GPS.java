package com.singfung.tracker.model;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "gps")
public class GPS
{
    private String vehicleId;
    private double lat;
    private double lng;

    public String getVehicleId()
    { return vehicleId; }

    public void setVehicleId(String vehicleId)
    { this.vehicleId = vehicleId; }

    public double getLat()
    { return lat; }

    public void setLat(double lat)
    { this.lat = lat; }

    public double getLng()
    { return lng; }

    public void setLng(double lng)
    { this.lng = lng; }
}

package com.singfung.tracker.task;

import com.singfung.tracker.model.GPS;
import com.singfung.tracker.service.GPSService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import java.util.TimerTask;

public class GPSTimerTask extends TimerTask
{
    private GPSService gpsService;
    private SimpMessagingTemplate template;
    private String vehicleId;

    public GPSTimerTask(GPSService gpsService, SimpMessagingTemplate template, String vehicleId)
    {
        this.gpsService = gpsService;
        this.template = template;
        this.vehicleId = vehicleId;
    }

    @Override
    public void run()
    {
        updateGPS(this.vehicleId);
        GPS gps = gpsService.getGPSByVehicleId(this.vehicleId);
        if (gps != null)
        {
            double lat = gps.getLat();
            double lng = gps.getLng();
            String gpsMessage = "{\"lat\": " + String.format("%.2f", lat) + ", \"lng\": " + String.format("%.2f", lng) + "}";
            this.template.convertAndSend("/gps/"+this.vehicleId, gpsMessage);
        }
    }

    public void updateGPS(String vehicleId)
    {
        GPS gps = gpsService.getGPSByVehicleId(vehicleId);

        if (gps != null)
        {
            double lng = gps.getLng() + 0.01;
            double lat = gps.getLat() + 0.01;

            gps.setLng(lng);
            gps.setLat(lat);

            gpsService.saveGPS(gps);
        }
    }
}
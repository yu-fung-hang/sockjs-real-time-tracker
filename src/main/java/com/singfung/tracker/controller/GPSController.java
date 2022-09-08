package com.singfung.tracker.controller;

import com.singfung.tracker.model.GPS;
import com.singfung.tracker.service.GPSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gps")
public class GPSController
{
    private GPSService gpsService;

    @Autowired
    public GPSController(GPSService gpsService)
    { this.gpsService = gpsService; }

    //insert or update
    @PostMapping()
    public void saveGPS(@RequestBody GPS gps)
    { gpsService.saveGPS(gps); }

    @GetMapping("/{vehicleId}")
    public GPS getGPSByVehicleId(@PathVariable String vehicleId)
    { return gpsService.getGPSByVehicleId(vehicleId); }

    @DeleteMapping("/{vehicleId}")
    public void deleteGPS(@PathVariable String vehicleId)
    { gpsService.deleteGPS(vehicleId); }
}
package com.singfung.tracker.controller;

import com.singfung.tracker.model.GPS;
import com.singfung.tracker.service.GPSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/gps")
public class GPSController
{
    private GPSService GPSService;

    @Autowired
    public GPSController(GPSService GPSService)
    { this.GPSService = GPSService; }

    //insert or update
    @PostMapping()
    public void saveGPS(@RequestBody GPS gps)
    { GPSService.saveGPS(gps); }

    @GetMapping("/{vehicleId}")
    public GPS getGPSByVehicleId(@PathVariable String vehicleId)
    { return GPSService.getGPSByVehicleId(vehicleId); }

    @DeleteMapping("/{vehicleId}")
    public void deleteGPS(@PathVariable String vehicleId)
    { GPSService.deleteGPS(vehicleId); }
}
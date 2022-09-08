package com.singfung.tracker.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "gps")
public class GPS
{
    private String vehicleId;
    private double lat;
    private double lng;
}

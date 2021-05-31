package com.singfung.tracker.mapper;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientMessage
{
    private String vehicleId;

    public ClientMessage() {}
}

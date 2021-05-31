package com.singfung.tracker.controller;

import com.singfung.tracker.mapper.ClientMessage;
import com.singfung.tracker.service.GPSService;
import com.singfung.tracker.task.GPSTimerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

@Controller
public class WebSocketController
{
    @Autowired
    private GPSService gpsService;
    @Autowired
    private SimpMessagingTemplate template;

    private static Map<String, GPSTimerTask> gpsTimerTaskMap = new HashMap<>();
    private Timer timer = new Timer();

    @MessageMapping("/track")
    public void trackTrip(ClientMessage message)
    {
        String vehicleId = HtmlUtils.htmlEscape(message.getVehicleId());

        if(gpsTimerTaskMap.containsKey(vehicleId) == false)
        {
            GPSTimerTask gpsTimerTask = new GPSTimerTask(this.gpsService, this.template, vehicleId);
            gpsTimerTaskMap.put(vehicleId, gpsTimerTask);
            this.timer.scheduleAtFixedRate(gpsTimerTask, 0, 2000);
        }
    }

    @MessageMapping("/end")
    public void endTracking(ClientMessage message)
    {
        String vehicleId = HtmlUtils.htmlEscape(message.getVehicleId());

        if(gpsTimerTaskMap.containsKey(vehicleId) == true)
        {
            GPSTimerTask gpsTimerTask = gpsTimerTaskMap.get(vehicleId);
            gpsTimerTask.cancel();
            gpsTimerTaskMap.remove(vehicleId);
        }
    }
}

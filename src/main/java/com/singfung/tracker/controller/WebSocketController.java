package com.singfung.tracker.controller;

import com.singfung.tracker.mapper.ClientMessage;
import com.singfung.tracker.service.GPSService;
import com.singfung.tracker.task.GPSTimerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
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

//    @MessageMapping("/track")
//    public void trackTrip(ClientMessage message)
//    {
//        String vehicleId = HtmlUtils.htmlEscape(message.getVehicleId());
//
//        if(gpsTimerTaskMap.containsKey(vehicleId) == false)
//        {
//            GPSTimerTask gpsTimerTask = new GPSTimerTask(this.gpsService, this.template, vehicleId);
//            gpsTimerTaskMap.put(vehicleId, gpsTimerTask);
//            this.timer.scheduleAtFixedRate(gpsTimerTask, 0, 2000);
//        }
//    }

    @EventListener
    public void onSubscribeEvent(SessionSubscribeEvent event)
    {
        String sessionId = (String) event.getMessage().getHeaders().get("simpSessionId");

        if(gpsTimerTaskMap.containsKey(sessionId) == false)
        {
            String destination = (String) event.getMessage().getHeaders().get("simpDestination");
            String[] temp = destination.split("/");

            GPSTimerTask gpsTimerTask = new GPSTimerTask(this.gpsService, this.template, temp[2]);
            gpsTimerTaskMap.put(sessionId, gpsTimerTask);
            this.timer.scheduleAtFixedRate(gpsTimerTask, 0, 1500);
        }
    }

    @EventListener
    public void onDisconnectEvent(SessionDisconnectEvent event)
    {
        String sessionId = event.getSessionId();

        if(gpsTimerTaskMap.containsKey(sessionId) == true)
        {
            GPSTimerTask gpsTimerTask = gpsTimerTaskMap.get(sessionId);
            gpsTimerTask.cancel();
            gpsTimerTaskMap.remove(sessionId);
        }
    }
}

package com.singfung.tracker.service;

import com.singfung.tracker.model.GPS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Transactional
@Service
public class GPSService
{
    private MongoTemplate mongoTemplate;

    @Autowired
    public GPSService(MongoTemplate mongoTemplate)
    { this.mongoTemplate = mongoTemplate; }

    public void saveGPS(GPS gps)
    {
        String vehicleId = gps.getVehicleId();

        if(vehicleId == null || vehicleId.length()==0)
        { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "vehicleId could not be null!"); }

        Query query = Query.query(Criteria.where("vehicleId").is(vehicleId));
        List<GPS> gpsList = mongoTemplate.find(query, GPS.class);

        //insert
        if(gpsList.size()==0)
        {
            gps = mongoTemplate.insert(gps);
            if (gps == null)
            { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed. Please contact the system administrator."); }
        } else { //update
            query = Query.query(Criteria.where("vehicleId").is(gps.getVehicleId()));
            Update update = new Update();
            update.set("lat", gps.getLat());
            update.set("lng", gps.getLng());
            mongoTemplate.updateFirst(query, update, GPS.class);
        }
    }

    public GPS getGPSByVehicleId(String vehicleId)
    {
        Query query = Query.query(Criteria.where("vehicleId").is(vehicleId));
        List<GPS> location = mongoTemplate.find(query, GPS.class);

        if(location.size() == 0)
        { return null; }

        return location.get(0);
    }

    public void deleteGPS(String vehicleId)
    {
        Query query = Query.query(Criteria.where("vehicleId").is(vehicleId));
        mongoTemplate.remove(query, GPS.class);
    }
}
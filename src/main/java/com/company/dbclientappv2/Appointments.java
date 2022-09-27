package com.company.dbclientappv2;

import java.sql.Time;
import java.sql.Timestamp;

public class Appointments {
    public int appointmentId;
    public String title;
    public String description;
    public String location;
    public String type;
    public Timestamp startTime;
    public Timestamp endTime;
    public int customerId;
    public int userID;
    public int contactId;

    public Appointments(int appointmentId, String title, String description, String location, String type, Timestamp startTime, Timestamp endTime, int customerId, int userId, int contactId){
        this.appointmentId=appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerId = customerId;
        this.userID = userId;
        this.contactId = contactId;
    }
    public String getTitle(){
        return title;
    }
    public String getDescription(){
        return description;
    }
    public String getLocation(){
        return location;
    }
    public String getType(){
        return type;
    }
    public Timestamp getStartTime(){
        return startTime;
    }

    public Timestamp getEndTime(){
        return endTime;
    }

    public void setTitle(String title){
        this.title = title;
    }
    public void setDescription(String description){
        this.description = description;
    }
    public void setType(String type){
        this.type = type;
    }

}

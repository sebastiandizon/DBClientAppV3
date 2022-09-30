package com.company.dbclientappv2;

import java.sql.Timestamp;

public class Appointment {
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

    public Appointment(int appointmentId, String title, String description, String location, String type, Timestamp startTime, Timestamp endTime, int customerId, int contactId){
        this.appointmentId=appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerId = customerId;
        this.userID = Users.userID;
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
    public int getUserID() { return userID; }

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

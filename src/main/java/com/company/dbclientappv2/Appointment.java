package com.company.dbclientappv2;

import java.time.Instant;

public class Appointment {
    public int appointmentId;
    public String title;
    public String description;
    public String location;
    public String type;
    public Instant startTime;
    public Instant endTime;
    public int customerId;
    public int userId;
    public int contactId;

    public Appointment(int appointmentId, String title, String description, String location, String type, Instant startTime, Instant endTime, int customerId, int contactId){
        this.appointmentId=appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerId = customerId;
        this.userId = Users.userId;
        this.contactId = contactId;
    }
    public int getAppointmentId() { return appointmentId; }
    public String getTitle(){
        return title;
    }
    public String getDescription(){ return description; }
    public String getLocation(){
        return location;
    }
    public String getType(){
        return type;
    }
    public Instant getStartTime(){
        return startTime;
    }

    public Instant getEndTime(){
        return endTime;
    }
    public int getUserId() { return userId; }

    public int getContactId() { return contactId; }

    public int getCustomerId() {
        return customerId;
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

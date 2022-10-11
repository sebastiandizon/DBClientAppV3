package com.company.dbclientappv2;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Appointment {
    public int appointmentId;
    public String title;
    public String description;
    public String location;
    public String type;
    public ZonedDateTime startTime;
    public ZonedDateTime endTime;
    public int customerId;
    public int userId;
    public int contactId;
    ObjectModify appointmentModifyRecord;

    public Appointment(int appointmentId, String title, String description, String location, String type, ZonedDateTime startTime, ZonedDateTime endTime, int customerId, int contactId){
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
        this.appointmentModifyRecord = new ObjectModify();
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public ObjectModify getAppointmentModifyRecord() {
        return appointmentModifyRecord;
    }

    public void setAppointmentModifyRecord(ObjectModify appointmentModifyRecord) {
        this.appointmentModifyRecord = appointmentModifyRecord;
    }
}

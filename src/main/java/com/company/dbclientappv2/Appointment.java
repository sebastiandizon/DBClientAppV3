package com.company.dbclientappv2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
    /**default constructor value of Appointment. Auto generates author & date created / last updated data*/
    public Appointment(int appointmentId, String title, String description, String location, String type, ZonedDateTime startTime, ZonedDateTime endTime, int customerId, int contactId, int userId){
        this.appointmentId=appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
        this.appointmentModifyRecord = new ObjectModify();
    }
    /**@return int appointmentId*/
    public int getAppointmentId() {
        return appointmentId;
    }
    /**@param appointmentId updates appointmentId with specified int*/
    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }
    /**@return String title */
    public String getTitle() {
        return title;
    }
    /**@param title updates String for title field */
    public void setTitle(String title) {
        this.title = title;
    }
    /**@return String description*/
    public String getDescription() {
        return description;
    }
    /**@param description updates String for description field*/
    public void setDescription(String description) {
        this.description = description;
    }
    /**@return String location*/
    public String getLocation() {
        return location;
    }
    /**@param location */
    public void setLocation(String location) {
        this.location = location;
    }
    /**@return String type*/
    public String getType() {
        return type;
    }
    /**@param type */
    public void setType(String type) {
        this.type = type;
    }
    /**@return ZonedDateTime startTime*/
    public ZonedDateTime getStartTime() {
        return startTime;
    }
    /**@param startTime */
    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }
    /**@return ZonedDateTime endTime*/
    public ZonedDateTime getEndTime() {
        return endTime;
    }
    /**@param endTime */
    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }
    /**@return int customerId*/
    public int getCustomerId() {
        return customerId;
    }
    /**@param customerId */
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    /**@return int userId*/
    public int getUserId() {
        return userId;
    }
    /**@param userId */
    public void setUserId(int userId) {
        this.userId = userId;
    }
    /**@return int contactId*/
    public int getContactId() {
        return contactId;
    }
    /**@param contactId */
    public void setContactId(int contactId) {
        this.contactId = contactId;
    }
    /**@return ObjectModify modifyRecord with submethods that return values describing creation and modification times*/
    public ObjectModify getModifyRecord(){
        return this.appointmentModifyRecord;
    }
    /**@param appointmentModifyRecord  sets modify record with ObjectModify type */
    public void setAppointmentModifyRecord(ObjectModify appointmentModifyRecord) {
        this.appointmentModifyRecord = appointmentModifyRecord;
    }
    /**@return returns all corresponding */
    public ObservableList<String> getAttributes(Appointment appointment){
        ObservableList<String> attributes = FXCollections.observableArrayList();
        attributes.add(String.valueOf(Integer.valueOf(appointment.getAppointmentId())));
        attributes.add(appointment.getTitle());
        attributes.add(appointment.getDescription());
        attributes.add(appointment.getLocation());
        attributes.add(appointment.getType());
        attributes.add(String.valueOf(appointment.getStartTime().toInstant()));
        attributes.add(String.valueOf(appointment.getEndTime().toInstant()));
        attributes.add(String.valueOf(appointment.getModifyRecord().getCreateDate()));
        attributes.add(String.valueOf(appointment.getModifyRecord().getCreatedBy()));
        attributes.add(String.valueOf(appointment.getModifyRecord().getLastUpdate()));
        attributes.add(String.valueOf(appointment.getModifyRecord().getLastUpdateBy()));
        attributes.add(String.valueOf(appointment.getCustomerId()));
        attributes.add(String.valueOf(appointment.getUserId()));
        attributes.add(String.valueOf(appointment.getContactId()));
        return attributes;
    }
}

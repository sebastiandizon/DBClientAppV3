package com.company.dbclientappv2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.Instant;
import java.time.LocalDateTime;


// id, title, deesc, location, type, start, end, createdate, createby, lastupdate, lastupdateby, customerid, userid, contactId
public class Appointment {
    private int Appointment_ID;
    private String Title;
    private String Description;
    private String Location;
    private String Type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int customerId;
    private int userId;
    private int contactId;
    ObjectModify appointmentModifyRecord;
    /**default constructor value of Appointment. Auto generates author & date created / last updated data*/
    public Appointment(){
        this.Appointment_ID = Appointment_ID;
        this.Title = Title;
        this.Description = Description;
        this.Location = Location;
        this.Type = Type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
        this.appointmentModifyRecord = new ObjectModify();
    }
    /**@return int appointmentId*/
    public int getAppointment_ID() {
        return Appointment_ID;
    }
    /**@param appointment_ID updates appointmentId with specified int*/
    public void setAppointment_ID(int appointment_ID) {
        this.Appointment_ID = appointment_ID;
    }
    /**@return String title */
    public String getTitle() {
        return Title;
    }
    /**@param title updates String for title field */
    public void setTitle(String title) {
        this.Title = title;
    }
    /**@return String description*/
    public String getDescription() {
        return Description;
    }
    /**@param description updates String for description field*/
    public void setDescription(String description) {
        this.Description = description;
    }
    /**@return String location*/
    public String getLocation() {
        return Location;
    }
    /**@param location */
    public void setLocation(String location) {
        this.Location = location;
    }
    /**@return String type*/
    public String getType() {
        return Type;
    }
    /**@param type */
    public void setType(String type) {
        this.Type = type;
    }
    /**@return ZonedDateTime startTime*/
    public LocalDateTime getStartTime() {
        return startTime;
    }
    /**@param startTime */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    /**@return ZonedDateTime endTime*/
    public LocalDateTime getEndTime() {
        return endTime;
    }
    /**@param endTime */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    public Instant getCreateDate(){
        return appointmentModifyRecord.getCreateDate();
    }
    public void setCreateDate(Instant createDate){
        appointmentModifyRecord.setCreateDate(createDate);
    }
    public void setCreatedBy(String createdBy){
        appointmentModifyRecord.setCreatedBy(createdBy);
    }
    public void setLastUpdate(Instant lastUpdate){
        appointmentModifyRecord.setLastUpdate(lastUpdate);
    }
    public void setLastUpdateBy(String LastUpdateBy){
        appointmentModifyRecord.setLastUpdateBy(LastUpdateBy);
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
//    public ObservableList<String> getAttributes(Appointment appointment){
//        ObservableList<String> attributes = FXCollections.observableArrayList();
//        attributes.add(String.valueOf(Integer.valueOf(appointment.getAppointment_ID())));
//        attributes.add(appointment.getTitle());
//        attributes.add(appointment.getDescription());
//        attributes.add(appointment.getLocation());
//        attributes.add(appointment.getType());
//        attributes.add(String.valueOf(appointment.getStartTime().toInstant()));
//        attributes.add(String.valueOf(appointment.getEndTime().toInstant()));
//        attributes.add(String.valueOf(appointment.getModifyRecord().getCreateDate()));
//        attributes.add(String.valueOf(appointment.getModifyRecord().getCreatedBy()));
//        attributes.add(String.valueOf(appointment.getModifyRecord().getLastUpdate()));
//        attributes.add(String.valueOf(appointment.getModifyRecord().getLastUpdateBy()));
//        attributes.add(String.valueOf(appointment.getCustomerId()));
//        attributes.add(String.valueOf(appointment.getUserId()));
//        attributes.add(String.valueOf(appointment.getContactId()));
//        return attributes;
//    }
}

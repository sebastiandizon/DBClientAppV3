package model;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;


public class Appointment {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private int appointmentId;
    private String title;
    private String description;
    private String location;
    private String type;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private int customerId;
    private int userId;
    private int contactId;
    ModifyRecord appointmentModifyRecord;
    /**default constructor value of Appointment. Auto generates author & date created / last updated data*/
    public Appointment(){
        this.appointmentId = appointmentId;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.customerId = customerId;
        this.userId = userId;
        this.contactId = contactId;
        this.appointmentModifyRecord = new ModifyRecord();
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

    public String getStartQueryFormat() {
        Instant startInstant = startTime.toInstant();
        Date date = Date.from(startInstant);
        return simpleDateFormat.format(date);
    }

    /**@param startTime */
    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }
    /**@return ZonedDateTime endTime*/
    public ZonedDateTime getEndTime() {
        return endTime;
    }
    public String getEndQueryFormat() {
        Instant endInstant = endTime.toInstant();
        Date date = Date.from(endInstant);
        return simpleDateFormat.format(date);
    }
    /**@param endTime */
    public void setEndTime(ZonedDateTime endTime) {
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
    public ModifyRecord getModifyRecord(){
        return this.appointmentModifyRecord;
    }
    /**@param appointmentModifyRecord  sets modify record with ObjectModify type */
    public void setAppointmentModifyRecord(ModifyRecord appointmentModifyRecord) {
        this.appointmentModifyRecord = appointmentModifyRecord;
    }
}

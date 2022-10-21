package model;
/**@author Sebastian Dizon*/

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

public class ModifyRecord {
    Instant createDate;
    String createdBy;
    Instant lastUpdate;
    String lastUpdateBy;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**Object used to store creation & last update data**/
    public ModifyRecord() {
        this.createDate = Instant.now();
        this.createdBy = Users.userName;
        this.lastUpdate = Instant.now();
        this.lastUpdateBy = Users.userName;
    }
    /**@return  Instant value of date created*/
    public Instant getCreateDate() {
        return createDate;
    }
    public String getSimpleCreateDate(){
        Date createDate = Date.from(getCreateDate());
        String formattedDate = formatter.format(createDate);
        return formattedDate;

    }
    /**@param createDate Sets Instant value for date created*/

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }
    /**@return returns String value of author of entry*/
    public String getCreatedBy() {
        return createdBy;
    }
    /**@param createdBy sets String value for author of entry*/
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    /**@return Instant value of author of date & time last updated*/
    public Instant getLastUpdate() {
        return lastUpdate;
    }
    /**@param lastUpdate sets Instant value of author of date & time last updated*/
    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    public String getSimpleLastUpdate(){
        Date lastUpdate = Date.from(getLastUpdate());
        String formattedDate = formatter.format(lastUpdate);
        return formattedDate;

    }
    /**@return String value of author of entry*/
    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    /**@param lastUpdateBy sets String value of author of entry*/
    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }



}

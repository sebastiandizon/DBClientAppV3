package com.company.dbclientappv2;

import java.time.Instant;

public class ObjectModify {
    Instant createDate;
    String createdBy;
    Instant lastUpdate;
    String lastUpdateBy;

    public ObjectModify() {
        this.createDate = Instant.now();
        this.createdBy = Users.userName;
        this.lastUpdate = Instant.now();
        this.lastUpdateBy = Users.userName;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Instant lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(String lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }



}

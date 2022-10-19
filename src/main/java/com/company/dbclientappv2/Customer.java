package com.company.dbclientappv2;

public class Customer {
    int customerId;
    String customerName;
    String address;
    String postalCode;
    String phone;
    int divisionId;
    ObjectModify objectModify;

    public Customer(int customerId, String customerName, String address, String postalCode, String phone, int divisionId){
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.divisionId = divisionId;
        this.objectModify = new ObjectModify();
    }
    /**@return int getCustomerId*/
    public int getCustomerId() {
        return customerId;
    }
    /**@param customerId  updates customerId field with specified integer*/
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
    /**@return String customer name*/
    public String getCustomerName() {
        return customerName;
    }
    /**@param customerName */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    /**@return String address*/
    public String getAddress() {
        return address;
    }
    /**@param address updates address field with specified String*/
    public void setAddress(String address) {
        this.address = address;
    }
    /**@return String postal code field*/
    public String getPostalCode() {
        return postalCode;
    }
    /**@param postalCode updates postalCold field with specified string*/
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    /**@return String phone number*/
    public String getPhone() {
        return phone;
    }
    /**@param phone updates phone number field with specified String*/
    public void setPhone(String phone) {
        this.phone = phone;
    }
    /**@return integer Division ID*/
    public int getDivisionId() {
        return divisionId;
    }
    /**@param divisionId updates divisionId with specified integer*/
    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }
    /**@return modify record of customer object*/
    public ObjectModify getObjectModify() {
        return objectModify;
    }

}

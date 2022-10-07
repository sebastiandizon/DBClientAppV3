package com.company.dbclientappv2;

public class Customer {
    int customerId;
    String customerName;
    String address;
    int postalCode;
    int divisionId;

    public Customer(int customerId, String customerName, String address, int postalCode, int divisionId){
        this.customerId = customerId;
        this.customerName = customerName;
        this.address = address;
        this.postalCode = postalCode;
        this.divisionId = divisionId;

    }

    public int getCustomerId() {
        return customerId;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public int getPostalCode() {
        return postalCode;
    }

    public String getAddress() {
        return address;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public void setPostalCode(int postalCode) {
        this.postalCode = postalCode;
    }
}

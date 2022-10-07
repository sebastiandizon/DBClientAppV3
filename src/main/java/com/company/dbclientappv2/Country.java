package com.company.dbclientappv2;

public class Country {
    int countryId;
    String country;

    public Country(int countryId, String country){
        this.countryId = countryId;
        this.country = country;
    }

    public int getCountryId() {
        return countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}

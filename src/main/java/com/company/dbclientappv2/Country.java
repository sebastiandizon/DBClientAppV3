package com.company.dbclientappv2;

public class Country {
    int countryId;
    String country;
    ObjectModify countryModifyRecord;

    public Country(int countryId, String country){
        this.countryId = countryId;
        this.country = country;
        this.countryModifyRecord = new ObjectModify();
    }
    /**@return int countryId*/
    public int getCountryId() {
        return countryId;
    }
    /**@return String name of country*/
    public String getCountry() {
        return country;
    }

}

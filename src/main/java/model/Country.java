package model;

public class Country {
    int countryId;
    String country;
    ModifyRecord countryModifyRecord;



    public Country(){
        this.countryId = countryId;
        this.country = country;
        this.countryModifyRecord = new ModifyRecord();
    }
    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public ModifyRecord getCountryModifyRecord() {
        return countryModifyRecord;
    }

    public void setCountryModifyRecord(ModifyRecord countryModifyRecord) {
        this.countryModifyRecord = countryModifyRecord;
    }
}

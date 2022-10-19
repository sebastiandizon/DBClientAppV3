package com.company.dbclientappv2;

public class FirstLevelDivision {
    int divisionId;
    String Division;
    int countryId;
    /**default constructor for First Level Division data, specifies id value int and division name as String  */
    public FirstLevelDivision(int divisionId, String division, int countryId) {
        this.divisionId = divisionId;
        Division = division;
        this.countryId = countryId;
        ObjectModify firstLevelModifyRecord = new ObjectModify();
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivision() {
        return Division;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}

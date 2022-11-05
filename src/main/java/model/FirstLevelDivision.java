package model;

public class FirstLevelDivision {
    private int divisionId;
    private String division;
    private int countryId;
    private ModifyRecord firstLevelModifyRecord;

    public FirstLevelDivision() {
        this.divisionId = divisionId;
        this.division = division;
        this.countryId = countryId;
        this.firstLevelModifyRecord = new ModifyRecord();
    }
    /**@return integer of Division ID*/
    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }
    /**@return String name of division*/
    public String getDivision() {
        return division;
    }
    /**@param division Sets division to given string*/
    public void setDivision(String division) {
        this.division = division;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public ModifyRecord getModifyRecord(){
        return firstLevelModifyRecord;
    }
}

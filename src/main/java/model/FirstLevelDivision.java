package model;

public class FirstLevelDivision {
    private int divisionId;
    private String division;
    private int countryId;
    private ModifyRecord firstLevelModifyRecord;
    /**default constructor for First Level Division data, specifies id value int and division name as String  */
    public FirstLevelDivision() {
        this.divisionId = divisionId;
        this.division = division;
        this.countryId = countryId;
        this.firstLevelModifyRecord = new ModifyRecord();
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivision() {
        return division;
    }

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

package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Country;
import model.FirstLevelDivision;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static helper.JDBC.connection;

public class LocationDAO {

    public ObservableList<Country> getCountry() throws SQLException{
        ObservableList<Country> countries = FXCollections.observableArrayList();
        String query = "SELECT * FROM client_schedule.countries";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);

        while(rs.next()){
            Country country = new Country();
            country.setCountryId(rs.getInt(1));
            country.setCountry(rs.getString(2));
            country.getCountryModifyRecord().setCreateDate(rs.getTimestamp(3).toInstant());
            country.getCountryModifyRecord().setCreatedBy(rs.getString(4));
            country.getCountryModifyRecord().setLastUpdate(rs.getTimestamp(5).toInstant());
            country.getCountryModifyRecord().setLastUpdateBy(rs.getString(6));
            countries.add(country);
        }
        return countries;
    }
    public ObservableList getCountryNames() throws SQLException{
        ObservableList<String> names = FXCollections.observableArrayList();
        for(Country country : getCountry()){
            names.add(country.getCountry());
        }
        return names;
    }
    public int getCountryId(String name) throws SQLException{
        for(Country country : getCountry()){
            if(country.getCountry().equals(name)){
                return country.getCountryId();
            }
        }
        return 0;
    }

    public ObservableList<FirstLevelDivision> getDivisions() throws SQLException{
        ObservableList<FirstLevelDivision> divisions = FXCollections.observableArrayList();
        String query = "SELECT * FROM client_schedule.first_level_divisions";
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(query);
        while(rs.next()){
            FirstLevelDivision division = new FirstLevelDivision();
            division.setDivisionId(rs.getInt(1));
            division.setDivision(rs.getString(2));
            division.getModifyRecord().setCreateDate(rs.getTimestamp(3).toInstant());
            division.getModifyRecord().setCreatedBy(rs.getString(4));
            division.getModifyRecord().setLastUpdate(rs.getTimestamp(5).toInstant());
            division.getModifyRecord().setLastUpdateBy(rs.getString(6));
            division.setCountryId(rs.getInt(7));
            divisions.add(division);
        }
        return divisions;
    }
    public FirstLevelDivision getDivision(int divisionId) throws SQLException{
        for(FirstLevelDivision division : getDivisions()) {
            if (division.getDivisionId() == divisionId) {
                return division;
            }
        }
        return null;
    }


    public ObservableList<String> getMatchingDivisions(String countryName) throws SQLException{
        ObservableList<String> matchingDivisions = FXCollections.observableArrayList();
        for(FirstLevelDivision division : getDivisions()){
            if(division.getCountryId() == getCountryId(countryName)){
                matchingDivisions.add(division.getDivision());
            }
        }
        return matchingDivisions;
    }

    public int getDivisionId(String divisionName) throws SQLException{
        int i = 0;
        for(FirstLevelDivision division : getDivisions()){
            if(division.getDivision().equals(divisionName)){
                i = division.getDivisionId();
            }
        }
        return i;
    }
    public String getDivisionName(int divisionId) throws SQLException{
        for(FirstLevelDivision division : getDivisions()){
            if(division.getDivisionId() == divisionId){
                return division.getDivision();
            }
        }
        return null;
    }
    public String getCountryName(int divisionId) throws SQLException{
        for(Country country : getCountry()){
            if(country.getCountryId() == getDivision(divisionId).getCountryId()){
                return country.getCountry();
            }
        }
        return null;
    }


}

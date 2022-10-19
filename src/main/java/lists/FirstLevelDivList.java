package lists;

import com.company.dbclientappv2.FirstLevelDivision;
import helper.DatabaseQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FirstLevelDivList {
    static ObservableList<FirstLevelDivision> allDivisions = FXCollections.observableArrayList();
    /**gets first level divisions from database and stores it in allDivisions list*/
    public static void retrieveDivisions() {
        try {
            ResultSet rs = DatabaseQueries.retrieveTable("first_level_divisions");
            while(rs.next()){
                int divisionId = rs.getInt("Division_ID");
                String Division = rs.getString("Division");
                int countryId = rs.getInt("Country_ID");
                FirstLevelDivision newDivision = new FirstLevelDivision(divisionId, Division, countryId);
                allDivisions.add(newDivision);
            }
        }catch (SQLException e){}
    }

    public static ObservableList<FirstLevelDivision> getAllDivisions() {
        return allDivisions;
    }
    public static ObservableList<String> getDivisionNames() {
        ObservableList<String> divisionNames = FXCollections.observableArrayList();
        for(FirstLevelDivision divisions : allDivisions){
            divisionNames.add(divisions.getDivision());
        }
        return divisionNames;
    }
    /**@return list of strings of division names
     * @param divisionsList list of divisions to get names frmo*/
    public static ObservableList<String> getDivisionNames(ObservableList<FirstLevelDivision> divisionsList) {
        ObservableList<String> divisionNames = FXCollections.observableArrayList();
        for(FirstLevelDivision divisions : divisionsList){
            divisionNames.add(divisions.getDivision());
        }
        return divisionNames;
    }

    public static FirstLevelDivision getDivision(int divisionId) {
        for(FirstLevelDivision division : allDivisions){
            if(division.getDivisionId() == divisionId){
                return division;
            }
        }
        return null;
    }
    /**@return int id of given division
     * @param divisionName string name of division*/
    public static int getDivisionId(String divisionName){
        for(FirstLevelDivision division : allDivisions){
            if(division.getDivision().equals(divisionName)){
                return division.getDivisionId();
            }
        }
        return 0;
    }
    /**@return list of FirstLevelDivisions found with matching countryId
     * @param countryId id of country to find corresponding divisions frmo*/
    public static ObservableList<FirstLevelDivision> getMatchingDivisions(int countryId){
        ObservableList<FirstLevelDivision> matchingDivisions = FXCollections.observableArrayList();
            for(FirstLevelDivision division : allDivisions){
                if(division.getCountryId() == countryId){
                    matchingDivisions.add(division);
                    System.out.println(division.getDivision() + " Addded");
                }
            }
        return matchingDivisions;
    }

}

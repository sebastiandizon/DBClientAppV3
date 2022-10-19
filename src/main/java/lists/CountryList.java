package lists;

import com.company.dbclientappv2.Country;
import com.company.dbclientappv2.FirstLevelDivision;
import helper.DatabaseQueries;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class CountryList implements Initializable {
    public static ObservableList<Country> allCountries = FXCollections.observableArrayList();
    /**pulls all countries from country table in database and stores it in allCountries list*/
    public static void retrieveCountries(){
        try {
            ResultSet rs = DatabaseQueries.retrieveTable("countries");
            while(rs.next()){
               int countryId = rs.getInt("Country_ID");
               String countryName = rs.getString("Country");
               Country newCountry = new Country(countryId,countryName);
               allCountries.add(newCountry);
            }
        }catch(SQLException e){}

    }
    public static ObservableList<Country> getAllCountries(){
        return allCountries;
    }
    /**gets names of all countries
     * @return list of strings of all names*/
    public static ObservableList<String> getCountryNames(){
        ObservableList<String> countryNames = FXCollections.observableArrayList();
        for(Country country : allCountries){
            countryNames.add(country.getCountry());
        }
        return countryNames;
    }

    public static Country getCountry(FirstLevelDivision division){
        for(Country country : allCountries){
            if(country.getCountryId() == division.getCountryId()){
                return country;
            }
        }
        return null;
    }
    /**@param countryName name used to find corresponding id
     * @return int value of country id using corresponding name*/
    public static int getCountryId(String countryName){
        for(Country country : allCountries){
            if(country.getCountry().equals(countryName)){
                return country.getCountryId();
            }
        }
        return 0;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}

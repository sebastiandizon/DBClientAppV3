package lists;

import com.company.dbclientappv2.FirstLevelDivisions;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FirstLevelDivList {
    ObservableList<FirstLevelDivisions> firstLevelDivList = FXCollections.observableArrayList();

    public int getMatchingCountryId(int id){
        for(FirstLevelDivisions firstLevelDivision : firstLevelDivList){
            if(firstLevelDivision.getCountryId() == id){
                return firstLevelDivision.getCountryId();
            }
        }
        return 0;
    }

}

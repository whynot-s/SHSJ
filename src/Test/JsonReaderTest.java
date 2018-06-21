package Test;

import DBTools.DBRegion;
import Model.City;
import Service.JsonReader;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class JsonReaderTest {

    @Test
    public void testJson(){
        List<City> cities = new ArrayList<>();
        try {
            DBRegion.SelectProCity(cities);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JsonReader jsonReader = new JsonReader("/Users/wangjiaruijue/Documents/SocialPractice/ProvCity.json");
        jsonReader.readCity(cities);
        jsonReader = new JsonReader("/Users/wangjiaruijue/Documents/SocialPractice/ProvCenter.json");
        jsonReader.readAbbr(cities);
        for(City city : cities) {
            try {
                DBRegion.UpdateProCity(city);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
package Service;

import Model.City;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;

public class JsonReader {

    private JSONArray jsonArray;

    public JsonReader(String filepath){
        try {
            JSONTokener jsonTokener = new JSONTokener(new FileReader(filepath));
            jsonArray = new JSONArray(jsonTokener);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void readCity(List<City> cities){
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject city = jsonArray.getJSONObject(i);
            String name = city.getString("name");
            JSONArray geoCoord = city.getJSONArray("geoCoord");
            for(City city1 : cities)
                if((city1.addGeo(name, geoCoord.getDouble(1), geoCoord.getDouble(0))) == 0)
                    break;
        }
    }

    public void readAbbr(List<City> cities){
        for(int i = 0; i < jsonArray.length(); i++){
            JSONObject city = jsonArray.getJSONObject(i);
            for(City city1 : cities)
                if((city1.addAbbr(city.getString("name"), city.getString("capital"), city.getString("abbreviation"))) == 0)
                    break;
        }
    }
}

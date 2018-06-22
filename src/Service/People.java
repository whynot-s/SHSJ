package Service;

import DBTools.DBPeople;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class People {

    public static String getPeopleMap(String curDate){
        Map<String, Double[]> Coords = new HashMap<>();
        Map<Integer, Map<String, Integer>> Counts = new HashMap<>();
        try {
            DBPeople.selectMemberMap(curDate, Coords, Counts);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONObject memberMap = new JSONObject();
        memberMap.put("Coordinates", Coords);
        List<Object> maps = new ArrayList<>();
        for(Map.Entry<Integer, Map<String, Integer>> count : Counts.entrySet()){
            Map<String, Object> deps = new HashMap<>();
            deps.put("dep", count.getKey());
            List<Object> data = new ArrayList<>();
            for(Map.Entry<String, Integer> c : count.getValue().entrySet()){
                Map<String, Object> ele = new HashMap<>();
                ele.put("name", c.getKey());
                ele.put("value", c.getValue());
                data.add(ele);
            }
            deps.put("data", new JSONArray(data));
            maps.add(deps);
        }
        memberMap.put("Count", new JSONArray(maps));
        return memberMap.toString();
    }

}

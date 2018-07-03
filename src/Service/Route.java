package Service;

import DBTools.DBRoute;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Route {

    public static String getRouteJSON(String startDate, String endDate){
        Map<Integer, Map<String, Map<String, Integer>>> routes = new HashMap<>();
        Map<String, Double[]> coords = new HashMap<>();
        try {
            DBRoute.selectRoute(startDate, endDate, routes, coords);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JSONObject route = new JSONObject();
        route.put("Coordinates", new JSONObject(coords));
        Map<Integer, Object> Routes = new HashMap<>();
        for(Map.Entry<Integer, Map<String, Map<String, Integer>>> r : routes.entrySet()){
            List<Object> tmp = new ArrayList<>();
            for(Map.Entry<String, Map<String, Integer>> sd : r.getValue().entrySet()){
                JSONObject s = new JSONObject();
                s.put("name", sd.getKey());
                for(Map.Entry<String, Integer> sdt : sd.getValue().entrySet()){
                    JSONObject d = new JSONObject();
                    d.put("name", sdt.getKey());
                    d.put("value", sdt.getValue());
                    List<Object> tt = new ArrayList<>();
                    tt.add(s);
                    tt.add(d);
                    tmp.add(new JSONArray(tt));
                }
            }
            Routes.put(r.getKey(), new JSONArray(tmp));
        }
        route.put("Routes", new JSONObject(Routes));
        return route.toString();
    }
}

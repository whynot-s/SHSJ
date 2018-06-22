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
        Map<Integer, List<Double[]>> points = new HashMap<>();
        Map<Integer, Integer> deps = new HashMap<>();
        try {
            DBRoute.selectRoute(startDate, endDate, points, deps);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<Object> routes = new ArrayList<>();
        for(Map.Entry<Integer, List<Double[]>> point : points.entrySet()){
            int key = point.getKey();
            List<Double[]> value = point.getValue();
            Map<String, Object> route = new HashMap<>();
            route.put("dep", deps.get(key));
            route.put("pid", key);
            List<Object> pairs = new ArrayList<>();
            for(int i = 0; i < value.size() - 1; i++){
                List<Object> latlon = new ArrayList<>();
                JSONObject p1 = new JSONObject();
                JSONObject p2 = new JSONObject();
                p1.put("Coord", value.get(i + 1));
                p2.put("Coord", value.get(i));
                latlon.add(p1);
                latlon.add(p2);
                pairs.add(latlon);
            }
            route.put("data", pairs);
            routes.add(route);
        }
        return (new JSONArray(routes)).toString();
    }
}

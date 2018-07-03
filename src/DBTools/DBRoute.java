package DBTools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBRoute {

    final private static String RouteSQL = "SELECT Schedule.project_id AS pid, Project.depNo as dep, " +
            "LALONG.latitude AS lat, LALONG.longtitude AS lon, LALONG.City AS city, praDate " +
            "FROM Schedule, LALONG, Project WHERE praDate >= \'%s\' AND " +
            "praDate <= \'%s\' AND Schedule.praProvince = LALONG.Province " +
            "AND Schedule.praCity = LALONG.City AND project_id = Project.id " +
            "ORDER BY Schedule.project_id, praDate";

    public static int selectRoute(String startDate, String endDate, Map<Integer, Map<String, Map<String, Integer>>> routes, Map<String, Double[]> coords) throws SQLException {
        Connection conn = DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(String.format(RouteSQL, startDate, endDate));
        boolean flag = true;
        boolean end = false;
        boolean first = true;
        while(!end){
            if(flag) if(!resultSet.next()) break;
            end = true;
            flag = false;
            int pid = resultSet.getInt("pid");
            int dep = resultSet.getInt("dep");
            String start = resultSet.getString("City");
            double lat = resultSet.getDouble("lat");
            double lon = resultSet.getDouble("lon");
            while(resultSet.next()){
                int pid2 = resultSet.getInt("pid");
                if(pid != pid2){
                    end = false;
                    break;
                }
                String destination = resultSet.getString("City");
                if(start.equals(destination)) continue;
                Map<String, Map<String, Integer>> route = routes.get(dep);
                if(route == null){
                    route = new HashMap<>();
                    Map<String, Integer> des = new HashMap<>();
                    des.put(destination, 1);
                    route.put(start, des);
                    routes.put(dep, route);
                }else{
                    Map<String, Integer> des = route.get(start);
                    if(des == null){
                        des = new HashMap<>();
                        des.put(destination, 1);
                        route.put(start, des);
                    }else{
                        Integer times = des.get(destination);
                        if(times == null){
                            des.put(destination, 1);
                        }else{
                            des.put(destination, times + 1);
                        }
                    }
                }
                if(first){
                    Double[] coord = coords.get(start);
                    if(coord == null){
                        coord = new Double[2];
                        coord[1] = lat;
                        coord[0] = lon;
                        coords.put(start, coord);
                    }
                }
                lat = resultSet.getDouble("lat");
                lon = resultSet.getDouble("lon");
                Double[] coord = coords.get(destination);
                if(coord == null){
                    coord = new Double[2];
                    coord[1] = lat;
                    coord[0] = lon;
                    coords.put(destination, coord);
                }
                start = destination;
                first = false;
            }
        }
        return 0;
    }

}

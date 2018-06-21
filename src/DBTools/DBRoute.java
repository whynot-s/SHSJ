package DBTools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DBRoute {

    final private static String RouteSQL = "SELECT Schedule.project_id AS pid, Project.depNo as dep, " +
            "LALONG.latitude AS lat, LALONG.longtitude AS lon " +
            "FROM Schedule, LALONG, Project WHERE praDate >= \'%s\' AND " +
            "praDate <= \'%s\' AND Schedule.praProvince = LALONG.Province " +
            "AND Schedule.praCity = LALONG.City AND project_id = Project.id";

    public static int selectRoute(String startDate, String endDate, Map<Integer, List<Double[]>> points, Map<Integer, Integer> deps) throws SQLException {
        Connection conn = DBtools.DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(String.format(RouteSQL, startDate, endDate));
        while(resultSet.next()){
            int pid = resultSet.getInt("pid");
            if(deps.get(pid) == null){
                deps.put(pid, resultSet.getInt("dep"));
                points.put(pid, new ArrayList<>());
            }
            List<Double[]> point = points.get(pid);
            Double[] latlon = new Double[2];
            latlon[0] = resultSet.getDouble("lat");
            latlon[1] = resultSet.getDouble("lon");
            point.add(latlon);
            points.put(pid, point);
        }
        return 0;
    }

}

package DBTools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class DBPeople {

    final private static String MemberMapSQL = "SELECT Project.id AS pid, Project.depNo AS dep, " +
            "Schedule.praProvince AS pro, Schedule.praCity AS cit, " +
            "LALONG.latitude AS lat, LALONG.longtitude AS lon, Project.memberNum AS num FROM Project, Schedule, LALONG " +
            "WHERE Schedule.praDate = \'%s\' AND Project.id = Schedule.project_id " +
            "AND Schedule.praProvince = LALONG.Province AND Schedule.praCity = LALONG.City";

    public static int selectMemberMap(String curDate, Map<String, Double[]> Coords, Map<Integer, Map<String, Integer>> Counts) throws SQLException {
        Connection conn = DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(String.format(MemberMapSQL, curDate));
        while(resultSet.next()){
            String city = resultSet.getString("cit");
            Double[] latlon = Coords.get(city);
            if(latlon == null){
                latlon = new Double[2];
                latlon[0] = resultSet.getDouble("lon");
                latlon[1] = resultSet.getDouble("lat");
                Coords.put(city, latlon);
            }
            int dep = resultSet.getInt("dep");
            Map<String, Integer> count = Counts.get(dep);
            if(count == null) Counts.put(dep, new HashMap<>());
            count = Counts.get(dep);
            Integer time = count.get(city);
            if(time == null) count.put(city, resultSet.getInt("num"));
            else count.put(city, time + resultSet.getInt("num"));
        }
        DBUtil.Close();
        return 0;
    }

}

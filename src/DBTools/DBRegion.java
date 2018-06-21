package DBTools;


import Model.City;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class DBRegion {

    final private static String RegionSQL_SELECT = "SELECT Province, City FROM LALONG ";

    final private static String RegionSQL_INSERT = "INSERT INTO LALONG(Province, City) VALUES%s";

    final private static String RegionSQL_UPDATE = "UPDATE LALONG SET %s WHERE Province = \'%s\' AND City = \'%s\'";

    public static int InsertProCity(List<String> procities) throws SQLException {
        Connection conn = DBtools.DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        String values = "";
        for(int i = 0; i < procities.size(); i++){
            values += procities.get(i);
            if(i != procities.size() - 1)values += ",";
        }
        stmt.execute(String.format(RegionSQL_INSERT, values));
        DBtools.DBUtil.Close();
        return 0;
    }

    public static void SelectProCity(List<City> cities) throws SQLException {
        Connection conn = DBtools.DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet resultset = stmt.executeQuery(RegionSQL_SELECT);
        while(resultset.next())
            cities.add(new City(resultset.getString("Province"), resultset.getString("City")));
        DBtools.DBUtil.Close();
    }

    public static void UpdateProCity(City city) throws SQLException {
        Connection conn = DBtools.DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        if(!city.changed())return;
        String sql = String.format(RegionSQL_UPDATE, city.toUpdateSQL(), city.getProv(), city.getCity());
        stmt.execute(sql);
        DBtools.DBUtil.Close();
    }
}

package Model;

import DBTools.DBUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Location {


    private List<String> Domestic;

    public Location(){
        Domestic = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT DISTINCT(Province) FROM LALONG WHERE `foreign` = 0");
            while(rs.next()) Domestic.add(rs.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public String[] parsePlace(String places){
        String[] result = new String[2];
        String[] levels = places.split("-");
        for(String pro : Domestic){
            if(pro.startsWith(levels[0])){
                result[0] = pro;
                break;
            }
        }
        if(result[0].contains("市")) result[1] = levels[2];
        else result[1] = levels[1];
        return result;
    }

}

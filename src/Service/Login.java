package Service;

import DBTools.DBUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Login {

    final private static String lg = "SELECT userName FROM SocUser WHERE userName=\'%s\' AND pwd=\'%s\'";

    public static String login(String username, String password) throws SQLException {
        Connection conn = DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(String.format(lg, username, password));
        if(resultSet.next()){
            String name = resultSet.getString("userName");
            if(resultSet.next())return null;
            else return name;
        }else return null;
    }

}

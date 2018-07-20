package Service;

import DBTools.DBUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Register {

    private Statement stmt;

    public Register() throws SQLException {
        Connection connection = DBUtil.getConnection();
        stmt = connection.createStatement();
    }

    public boolean exists(String userName) throws SQLException {
        stmt.executeQuery(String.format("SELECT userName FROM SocUser WHERE userName=\'%s\'", userName));
        if(stmt.getResultSet().next()) return true;
        else return false;
    }

    public int valid(String invitation) throws SQLException {
        stmt.executeQuery(String.format("SELECT level FROM invs WHERE invitation=\'%s\'", invitation));
        ResultSet resultSet = stmt.getResultSet();
        if(resultSet.next()) return resultSet.getInt("level");
        else return -1;
    }

    public boolean register(String userName, String pwd, String invitation, int level) {
        try {
            stmt.execute(String.format("INSERT INTO SocUser VALUES(\'%s\',\'%s\',\'%s\', %d)"
                    , userName, pwd, invitation, level));
            stmt.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}

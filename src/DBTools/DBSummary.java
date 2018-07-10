package DBTools;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBSummary {

    final private static String teamSQL = "SELECT COUNT(*) AS team FROM Project";
    final private static String peopleSQL = "SELECT COUNT(*) AS people FROM Member";
    final private static String placeSQL = "SELECT COUNT(DISTINCT(praCity)) AS place FROM Schedule";
    final private static String daySQL = "SELECT COUNT(DISTINCT(praDate)) AS days FROM Schedule";

    public static int getTeam() throws SQLException {
        Connection conn = DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(teamSQL);
        resultSet.next();
        return resultSet.getInt("team");
    }

    public static int getPeople() throws SQLException {
        Connection conn = DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(peopleSQL);
        resultSet.next();
        return resultSet.getInt("people");
    }

    public static int getPlace() throws SQLException {
        Connection conn = DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(placeSQL);
        resultSet.next();
        return resultSet.getInt("place");
    }

    public static int getDay() throws SQLException {
        Connection conn = DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(daySQL);
        resultSet.next();
        return resultSet.getInt("days");
    }

}

package DBTools;

import Model.Schedule;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBSchedule {

    final private static String ScheduleSQL_Exist = "SELECT * FROM Schedule WHERE project_id=%d LIMIT 1";

    final private static String ScheduleSQL_Delete = "DELETE FROM Schedule WHERE project_id = %d";

    final private static String ProjectSQL_Name = "SELECT id FROM Project WHERE teamName=\'%s\'";

    final private static String ScheduleSQL = "INSERT INTO Schedule(project_id, dayNo, praDate, praProvince, praCity) VALUES%s";

    public static int InsertSchedule(Schedule schedule) throws SQLException {
        int re = 0;
        Connection conn = DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(String.format(ProjectSQL_Name, schedule.getTeamName()));
        int teamId = -1;
        if(resultSet.next()){
            teamId = resultSet.getInt("id");
            if(resultSet.next())return -1;
        }
        resultSet = stmt.executeQuery(String.format(ScheduleSQL_Exist, teamId));
        if(resultSet.next()){
            re = 1;
            stmt.execute(String.format(ScheduleSQL_Delete, teamId));
        }
        String sql = String.format(ScheduleSQL, schedule.toInsertSQL(teamId));
        System.out.println(sql);
        stmt.execute(sql);
        DBUtil.Close();
        return re;
    }

}

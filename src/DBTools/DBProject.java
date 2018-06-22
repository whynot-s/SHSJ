package DBTools;

import Model.Project;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBProject {

    final private static String ProjectSQL_Duplicate = "SELECT id FROM Project WHERE %s";

    final private static String ProjectSQL_Insert = "iNSERT INTO Project VALUES(%d,%s)";

    final private static String ProjectSQL = "INSERT INTO Project(depNo, teamName, " +
            "teamLeaderId, teamLeaderPhone, teamTeacher, teacherPhone) VALUES(%s)";

    final private static String ProjectSQL_Delete = "DELETE FROM Project WHERE id=%d";

    public static int InsertProject(Project project) throws SQLException {
        int re = 0;
        Connection conn = DBtools.DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        String sql = String.format(ProjectSQL_Duplicate, project.toInfoSQL_Dupilicate());
        ResultSet resultSet = stmt.executeQuery(sql);
        if(resultSet.next()){
            re = 1;
            int id = resultSet.getInt("id");
            stmt.execute(String.format(ProjectSQL_Delete, id));
            sql = String.format(ProjectSQL_Insert, id, project.toInfoSQL(false));
        }else {
            sql = String.format(ProjectSQL, project.toInfoSQL(false));
        }
        stmt.execute(sql);
        sql = String.format(ProjectSQL_Duplicate, project.toInfoSQL_Dupilicate());
        resultSet = stmt.executeQuery(sql);
        if(resultSet.next())
            re = resultSet.getInt("id");
        else
            re = -1;
        DBtools.DBUtil.Close();
        return re;
    }
}

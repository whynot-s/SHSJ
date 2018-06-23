package DBTools;

import Model.Project;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBProject {

    final private static String ProjectSQL_Duplicate = "SELECT id FROM Project WHERE %s";

    final private static String ProjectSQL_Insert = "iNSERT INTO Project VALUES(%d,%s)";

    final private static String ProjectSQL = "INSERT INTO Project(depNo, teamName, " +
            "teamLeaderId, teamLeaderPhone, teamTeacher, teacherPhone, memberNum) VALUES(%s)";

    final private static String ProjectSQL_Delete = "DELETE FROM Project WHERE id=%d";

    final private static String ProjectSQL_SELECT = "SELECT depNo AS dep, teamName AS tna, Member.stuName AS lna, " +
            "teamTeacher AS tte, memberNum AS men FROM Project, Member WHERE Member.stuNo = Project.teamLeaderId AND id = %d";

    final private static String ProjectSQL_SELECT_FULL = "SELECT depNo AS dep, teamName AS tna, Member.stuName AS lna, " +
            "teamTeacher AS tte, memberNum AS men, teamLeaderPhone AS lpn, teacherPhone AS ttp " +
            "FROM Project, Member WHERE Member.stuNo = Project.teamLeaderId AND id = %d";

    final private static String[] heads = {"dep", "tna", "lna", "tte", "men"};

    final private static String[] full_heads = {"dep", "tna", "lna", "lpn", "tte", "ttp", "men"};

    public static int InsertProject(Project project) throws SQLException {
        int re = 0;
        Connection conn = DBUtil.getConnection();
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
        DBUtil.Close();
        return re;
    }

    public static JSONObject SelectProject(Connection conn , int project_id, boolean full) throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = full ? ProjectSQL_SELECT_FULL : ProjectSQL_SELECT;
        ResultSet resultSet = stmt.executeQuery(String.format(sql, project_id));
        resultSet.next();
        Map<String, Object> project = new HashMap<>();
        if(!full)for(String head : heads) project.put(head, resultSet.getObject(head));
        else for(String head : full_heads) project.put(head, resultSet.getObject(head));
        return new JSONObject(project);
    }

    public static List<Integer> SearchProject(Connection conn, String condition) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(String.format(ProjectSQL_Duplicate, condition));
        List<Integer> pids = new ArrayList<>();
        while(resultSet.next())
            pids.add(resultSet.getInt("id"));
        return pids;
    }

}

package DBTools;

import org.json.JSONArray;
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

    final private static String ProjectSQL_SELECT = "SELECT uid, depNo AS dep, teamName AS tna, Member.stuName AS lna, " +
            "teamTeacher AS tte, memberNum AS men FROM Project, Member WHERE Member.stuNo = Project.teamLeaderId AND id = %d";

    final private static String ProjectSQL_SELECT_FULL = "SELECT uid, depNo AS dep, teamName AS tna, Member.stuName AS lna, " +
            "teamTeacher AS tte, memberNum AS men, teamLeaderPhone AS lpn, teacherPhone AS ttp " +
            "FROM Project, Member WHERE Member.stuNo = Project.teamLeaderId AND id = %d";

    final private static String ProjectNumber = "SELECT id FROM Project, Member WHERE Project.teamLeaderId = " +
            "Member.stuNo AND teamName = \"%s\" AND Member.stuName = \"%s\" AND Project.teamTeacher = \"%s\"";

    final private static String ProjectUpdateNumber = "UPDATE Project SET uid = \'%s\' WHERE id = %d";

    final private static String[] heads = {"uid", "dep", "tna", "lna", "tte", "men"};

    final private static String[] full_heads = {"uid", "dep", "tna", "lna", "lpn", "tte", "ttp", "men"};

    final private static String[] full_head_show = {"编号", "系号", "队名", "队长", "队长电话", "指导教师", "指导教师电话", "人数"};

    public static JSONObject SelectProject(Connection conn , int project_id) throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = ProjectSQL_SELECT;
        ResultSet resultSet = stmt.executeQuery(String.format(sql, project_id));
        resultSet.next();
        Map<String, Object> project = new HashMap<>();
        for(String head : heads) project.put(head, resultSet.getObject(head));
        return new JSONObject(project);
    }

    public static JSONArray SelectProjectFull(Connection conn, int project_id) throws SQLException {
        Statement stmt = conn.createStatement();
        String sql = ProjectSQL_SELECT_FULL;
        ResultSet resultSet = stmt.executeQuery(String.format(sql, project_id));
        resultSet.next();
        List<Object> infos = new ArrayList<>();
        for(int i = 0; i < full_heads.length; i++){
            JSONObject info = new JSONObject();
            info.put("key", full_head_show[i]);
            info.put("value", resultSet.getObject(full_heads[i]));
            infos.add(info);
        }
        return new JSONArray(infos);
    }

    public static List<Integer> SearchProject(Connection conn, String condition) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(String.format(ProjectSQL_Duplicate, condition));
        List<Integer> pids = new ArrayList<>();
        while(resultSet.next())
            pids.add(resultSet.getInt("id"));
        return pids;
    }

    public static List<Integer> ProjectNumber(Connection conn, String tname, String lname, String teacher) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(String.format(ProjectNumber, tname, lname, teacher));
        List<Integer> result = new ArrayList<>();
        while(resultSet.next()){
            result.add(resultSet.getInt("id"));
        }
        return result;
    }

    public static void UpdateNumber(Connection conn, String uid, int id) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute(String.format(ProjectUpdateNumber, uid, id));
    }

}

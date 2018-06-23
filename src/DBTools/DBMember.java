package DBTools;

import Model.Project;
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

public class DBMember {

    final private static String MemberSQL_INSERT = "INSERT INTO Member VALUES%s ON DUPLICATE KEY UPDATE stuName = VALUES(stuName)";

    final private static String ScheMemberSQL_DELETE = "DELETE FROM SchMember WHERE project_id = %d";

    final private static String SchMemberSQL_INSERT = "INSERT INTO SchMember VALUES%s";

    final private static String MemberSQL_Search = "SELECT SchMember.project_id AS pid FROM SchMember, Member WHERE SchMember.member_id = Member.stuNo AND %s";

    final private static String MemberSQL_SELECT = "SELECT Member.stuNo AS sno, Member.stuName AS sna FROM Member, SchMember " +
            "WHERE Member.stuNo = SchMember.member_id AND project_id = %d";

    final private static String[] heads = {"sno", "sna"};

    public static int InsertProjectMember(int pid, Project project) throws SQLException {
        Connection conn = DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute(String.format(MemberSQL_INSERT, project.toMemberSQL()));
        stmt.execute(String.format(ScheMemberSQL_DELETE, pid));
        stmt.execute(String.format(SchMemberSQL_INSERT, project.toSchMemberSQL(pid)));
        DBUtil.Close();
        return 0;
    }

    public static List<Integer> searchProject(Connection conn, String condition) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(String.format(MemberSQL_Search, condition));
        List<Integer> pids = new ArrayList<>();
        while(resultSet.next())
            pids.add(resultSet.getInt("pid"));
        return pids;
    }

    public static JSONArray SelectProjectMember(Connection conn, int project_id) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet resultSet = stmt.executeQuery(String.format(MemberSQL_SELECT, project_id));
        List<Object> members = new ArrayList<>();
        Map<String, String> member = new HashMap<>();
        while(resultSet.next()){
            member.put(heads[0], resultSet.getString(heads[0]));
            member.put(heads[1], resultSet.getString(heads[1]));
            members.add(new JSONObject(member));
        }
        return new JSONArray(members);
    }

}

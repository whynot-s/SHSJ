package DBTools;

import Model.Project;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBMember {

    final private static String MemberSQL_INSERT = "INSERT INTO Member VALUES%s ON DUPLICATE KEY UPDATE stuName = VALUES(stuName)";

    final private static String ScheMemberSQL_DELETE = "DELETE FROM SchMember WHERE project_id = %d";

    final private static String SchMemberSQL_INSERT = "INSERT INTO SchMember VALUES%s";

    final private static String MemberSQL_Search = "SELECT SchMember.project_id AS pid FROM SchMember, Member WHERE SchMember.member_id = Member.stuNo AND %s";

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

}

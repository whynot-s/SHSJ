package DBTools;

import Model.Project;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBMember {

    final private static String MemberSQL_INSERT = "INSERT INTO Member VALUES%s ON DUPLICATE KEY UPDATE stuName = VALUES(stuName)";

    final private static String ScheMemberSQL_DELETE = "DELETE FROM SchMember WHERE project_id = %d";

    final private static String SchMemberSQL_INSERT = "INSERT INTO SchMember VALUES%s";

    public static int InsertProjectMember(int pid, Project project) throws SQLException {
        Connection conn = DBUtil.getConnection();
        Statement stmt = conn.createStatement();
        stmt.execute(String.format(MemberSQL_INSERT, project.toMemberSQL()));
        stmt.execute(String.format(ScheMemberSQL_DELETE, pid));
        stmt.execute(String.format(SchMemberSQL_INSERT, project.toSchMemberSQL(pid)));
        DBUtil.Close();
        return 0;
    }

}

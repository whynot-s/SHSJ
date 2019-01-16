package Service;

import DBTools.DBUtil;
import Model.Project;
import Model.Season;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InsertProject {

    public static int insert(List<Project> projects){
        Connection conn = DBUtil.getConnection();
        try {
            Statement stmt = conn.createStatement();
            String Insert_Project_sql = "INSERT INTO Project(depNo, teamName, teamLeaderId, teamLeaderPhone, teamTeacher, teacherPhone," +
                    "memberNum, season, direction, teamTeacher2, teacher2Phone) VALUES(%d, \'%s\', \'%s\', \'%s\'," +
                    " \'%s\', \'%s\', %d, \'%s\', \'%s\', \'%s\', \'%s\')";
            String Update_Member_sql = "INSERT INTO Member VALUES(\'%s\', \'%s\', \'%s\') ON DUPLICATE KEY UPDATE stuName = values(stuName), stuPhone = values(stuPhone)";
            String Insert_SchMember_sql = "INSERT INTO SchMember VALUES(%d, \'%s\')";
            for(Project project : projects)
                stmt.addBatch(String.format(Insert_Project_sql, project.getDepNo(), project.getTeamName(), project.getTeamLeader(),
                        project.getTeamLeaderPhone(), project.getTeacher(), project.getTeacherPhone(), project.getMembers().size(),
                        project.getSeason(), project.getDirection(), project.getTeacher2(), project.getTeacher2Phone()));
            stmt.executeBatch();
            Map<String, Integer> pids = new HashMap<>();
            for(Project project : projects){
                ResultSet rs = stmt.executeQuery(String.format("SELECT id FROM Project WHERE teamName = \'%s\' AND season = \'%s\'", project.getTeamName(), project.getSeason()));
                int pid = -1;
                while(rs.next()) pid = rs.getInt("id");
                if(pid == -1) {
                    delete_projects(projects, stmt);
                    conn.close();
                    return -1;
                } else{
                    pids.put(project.getTeamName(), pid);
                }
            }
            for(Project project : projects) {
                Map<String, String> members = project.getMembers();
                Map<String, String> phones = project.getPhones();
                int pid = pids.get(project.getTeamName());
                for (Map.Entry<String, String> member : members.entrySet())
                    stmt.addBatch(String.format(Update_Member_sql, member.getKey(), member.getValue(), phones.get(member.getKey())));
                stmt.executeBatch();
                for (Map.Entry<String, String> member : members.entrySet())
                    stmt.addBatch(String.format(Insert_SchMember_sql, pid, member.getKey()));
                stmt.executeBatch();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static void delete_projects(List<Project> projects, Statement stmt) throws SQLException {
        String Delete_sql = "DELETE FROM Project WHERE depNo=%d AND teamName=\'%s\' AND teamLeaderId=\'%s\' AND season = \'%s\'";
        for(Project project : projects)
            stmt.addBatch(String.format(Delete_sql, project.getDepNo(), project.getTeamName(), project.getTeamLeader(), Season.CurrentSeason()));
        stmt.executeBatch();
    }

}

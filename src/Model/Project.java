package Model;

import java.util.HashMap;
import java.util.Map;

public class Project {

    private String teamName;
    private int depNo;
    private String teamLeader;
    private String teamLeaderPhone;
    private String teacher;
    private String teacherPhone;
    private Map<String, String> members;

    public Project(String teamname, int depno, String teamleader, String teamleaderPhone, String Teacher, String teacherphone){
        teamName = teamname;
        depNo = depno;
        teamLeader = teamleader;
        teamLeaderPhone = teamleaderPhone;
        teacher = Teacher;
        teacherPhone = teacherphone;
        members = new HashMap<>();
    }

    public int putMember(String no, String name){
        if(members.containsKey(no))
            return -1;
        members.put(no, name);
        return 0;
    }

    public String toString(){
        String result = String.format("%02d %s %s %s %s %s\n", depNo, teamName, members.get(teamLeader), teamLeaderPhone, teacher, teacherPhone);
        for(Map.Entry<String, String> member : members.entrySet())
            result += String.format(" %s %s\n", member.getKey(), member.getValue());
        return result;
    }

    public String toInfoSQL_Dupilicate(){
        return String.format("depNo=%d AND teamName=\'%s\' AND teamLeaderId=\'%s\'",
                depNo, teamName, teamLeader);
    }

    public String toInfoSQL(boolean comma){
        String sql = String.format("%d, \'%s\', \'%s\', \'%s\', \'%s\', \'%s\', %d",
                depNo, teamName, teamLeader, teamLeaderPhone, teacher, teacherPhone, members.size());
        if(comma)return sql + ",";
        else return sql;
    }

    public String toMemberSQL(){
        String sql = "";
        for(Map.Entry<String, String> entry : members.entrySet()){
            sql += String.format("(\'%s\',\'%s\'),", entry.getKey(), entry.getValue());
        }
        return sql.substring(0, sql.length() - 1);
    }

    public String toSchMemberSQL(int pid){
        String sql = "";
        for(Map.Entry<String, String> entry : members.entrySet()){
            sql += String.format("(%d, \'%s\'),", pid, entry.getKey());
        }
        return sql.substring(0, sql.length() - 1);
    }

}

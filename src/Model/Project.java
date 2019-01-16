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

    public String toString(){
        String result = String.format("%02d %s %s %s %s %s\n", depNo, teamName, members.get(teamLeader), teamLeaderPhone, teacher, teacherPhone);
        for(Map.Entry<String, String> member : members.entrySet())
            result += String.format(" %s %s\n", member.getKey(), member.getValue());
        return result;
    }

}

package Model;

import java.util.ArrayList;
import java.util.Map;

public class Project {

    private String teamName;
    private int depNo;
    private String teamLeader;
    private String teamLeaderName;
    private String teamLeaderPhone;
    private String teacher;
    private String teacherPhone;
    private String teacher2;
    private String teacher2Phone;
    private String direction;
    private String season;
    private Map<String, String> members;
    private Map<String, String> phones;
    private ArrayList<Schedule> schedules;

    public Project(String teamname, int depno, String teamleader, String teamleaderName, String teamleaderPhone, String Teacher, String TeacherPhone,
                   String Teacher2, String Teacher2Phone, String Direction, String Season, Map<String, String> Members, Map<String, String> Phones, ArrayList<Schedule> Schedules){
        teamName = teamname;
        depNo = depno;
        teamLeader = teamleader;
        teamLeaderName = teamleaderName;
        teamLeaderPhone = teamleaderPhone;
        teacher = Teacher;
        teacherPhone = TeacherPhone;
        teacher2 = Teacher2;
        teacher2Phone = Teacher2Phone;
        direction = Direction;
        season = Season;
        members = Members;
        phones = Phones;
        members.put(teamleader, teamleaderName);
        phones.put(teamleader, teamleaderPhone);
        schedules = Schedules;
    }

    public String getTeamName() {
        return teamName;
    }

    public int getDepNo() {
        return depNo;
    }

    public String getTeamLeader() {
        return teamLeader;
    }

    public String getTeamLeaderPhone() {
        return teamLeaderPhone;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getTeacherPhone() {
        return teacherPhone;
    }

    public String getTeacher2() {
        return teacher2;
    }

    public String getTeacher2Phone() {
        return teacher2Phone;
    }

    public String getDirection() {
        return direction;
    }

    public String getSeason() {
        return season;
    }

    public Map<String, String> getMembers() {
        return members;
    }

    public Map<String, String> getPhones() {
        return phones;
    }

    public ArrayList<Schedule> getSchedules() {
        return schedules;
    }

    public static String ParseDirection(int did){
        switch (did){
            case 1: return "经济发展";
            case 2: return "科学创新";
            case 3: return "教育发展";
            case 4: return "社会现象";
            case 5: return "民俗文化";
            case 6: return "返乡宣传";
            default: return "其他";
        }
    }
}

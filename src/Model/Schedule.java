package Model;

import java.util.ArrayList;
import java.util.List;

public class Schedule {

    private String teamName;
    private List<ScheduleDay> teamSchedule;

    public Schedule(String teamName){
        this.teamName = teamName;
        teamSchedule = new ArrayList<>();
    }

    public void append(String date, String province, String city){
        teamSchedule.add(new ScheduleDay(date, province, city));
    }

    public int size(){
        return teamSchedule.size();
    }

    public String toString(){
        String result = String.format("%s\n", teamName);
        for(int i = 0; i < teamSchedule.size(); i++)
            result += String.format(" Day %02d-%s\n", i + 1, teamSchedule.get(i).toString());
        return result;
    }

    public String getTeamName(){
        return teamName;
    }

    public String toInsertSQL(int project_id){
        String pattern = "(%d,%d,%s)";
        String sql = "";
        for(int i = 0; i < teamSchedule.size(); i++){
            ScheduleDay sd = teamSchedule.get(i);
            sql += String.format(pattern, project_id, i + 1, sd.toInserSQL());
            if(i != teamSchedule.size() - 1)sql += ",";
        }
        return sql;
    }

}

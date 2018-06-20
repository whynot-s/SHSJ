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

}

package Test;

import DBTools.DBMember;
import DBTools.DBProject;
import DBTools.DBRegion;
import DBTools.DBSchedule;
import Model.Project;
import Model.Schedule;
import Service.ExcelReader;
import org.junit.Test;

import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReaderTest {

    @Test
    public void test1() {
        File directory = new File("/Users/wangjiaruijue/Documents/tw/data2");
        String[] readpp = {"03", "04", "06", "10", "12", "13", "07", "17", "团委", "14", "01",
        "15", "29", "73", "校会", "74", "75", "76", "79", "30", "24", "18", "19", "蓝协",
        "23", "09", "35", "21", "20", "08"};
        for(File file : directory.listFiles()) {
            boolean flag = false;
            for(String pp : readpp){
                if(file.getAbsolutePath().contains(String.format("%s.xls", pp))){
                    flag = true;
                    break;
                }
            }
            if(flag) continue;
            System.out.println(file.getAbsolutePath());
            ExcelReader excelReader = new ExcelReader(file.getAbsolutePath());
            List<Project> projects = new ArrayList<>();
            System.out.println(excelReader.readProject(projects));
            for (Project project : projects) {
//                System.out.println(project.toString());
                try {
                    int pid = DBProject.InsertProject(project);
                    DBMember.InsertProjectMember(pid, project);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            List<Schedule> schedules = new ArrayList<>();
            System.out.println(excelReader.readSchedule(schedules));
            for (Schedule schedule : schedules) {
//                System.out.println(schedule.toString());
                try {
                    DBSchedule.InsertSchedule(schedule);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            break;
        }
    }
}
package Test;

import Model.Project;
import Model.Schedule;
import Service.ExcelReader;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ExcelReaderTest {

    @Test
    public void test1() {
        ExcelReader excelReader = new ExcelReader("/Users/wangjiaruijue/Documents/SocialPractice/test.xls");
        List<Schedule> schedules = new ArrayList<>();
        System.out.println(excelReader.readSchedule(schedules));
        for(Schedule schedule : schedules)
            System.out.println(schedule.toString());
        List<Project> projects = new ArrayList<>();
        System.out.println(excelReader.readProject(projects));
        for(Project project : projects)
            System.out.println(project.toString());
    }
}
package Test;

import Model.Schedule;
import Service.ExcelReader;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ExcelReaderTest {

    @Test
    public void test1() {
        ExcelReader excelReader = new ExcelReader("/Users/wangjiaruijue/Documents/SocialPractice/test.xls");
        List<Schedule> schedules = new ArrayList<Schedule>();
        excelReader.readSchedule(schedules);
        for(Schedule schedule : schedules)
            System.out.println(schedule.toString());
    }
}
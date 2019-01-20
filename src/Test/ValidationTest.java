package Test;

import DBTools.DBUtil;
import Model.Project;
import Model.Schedule;
import Model.Season;
import Service.ExcelReader;
import Service.ExcelWriter;
import Service.InsertProject;
import Service.Validation;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.json.JSONObject;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ValidationTest {

    @Test
    public void validate(){
        try {
            Validation validation = new Validation(8, true);
            JSONObject message = new JSONObject();
            JSONObject data = new JSONObject();
            int status = validation.validate(message, data);
            System.out.println(status);
            System.out.println(message.toString());
            System.out.println(data.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void InsertTest(){
        try {
            boolean isWinter = true;
            Validation validation = new Validation(8, isWinter);
            JSONObject message = new JSONObject();
            JSONObject data = new JSONObject();
            int status = validation.validate(message, data);
            System.out.println(status);
            System.out.println(message.toString());
            System.out.println(data.toString());
            if(status == 0){
                List<Project> projects = new ArrayList<>();
                ExcelReader.readFile(validation.getFilepath(), projects, isWinter);
                status = InsertProject.insert(projects);
                System.out.println(status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void WriterTest(){
        ExcelWriter excelWriter = new ExcelWriter("/Users/whynot/Documents/TW/TEST_33816082_2_2019年寒假返乡实践实践队信息统计_1226_1224_TEST.xls");
        String filepath = "/Users/whynot/Documents/TW/33816082_2_2019年寒假返乡实践实践队信息统计_1226_1224.xls";
        try {
            HSSFSheet sheet = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(filepath))).getSheet("sheet1");
            excelWriter.appendLine(sheet.getRow(1));
            excelWriter.save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void ScheduleTest(){
        try {
            boolean isWinter = true;
            Validation validation = new Validation(8, isWinter);
            JSONObject message = new JSONObject();
            JSONObject data = new JSONObject();
            int status = validation.validate(message, data);
            System.out.println(status);
            System.out.println(message.toString());
            System.out.println(data.toString());
            if(status != 0){
                List<Project> projects = new ArrayList<>();
                ExcelReader.readFile(validation.getFilepath(), projects, isWinter);
                Connection conn = DBUtil.getConnection();
                Statement stmt = conn.createStatement();
                for(Project project : projects){
                    ArrayList<Schedule> schedules = project.getSchedules();
                    for(Schedule schedule : schedules){
                        String province = schedule.getPraProvince();
                        String city = schedule.getPraCity();
                        ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM LALONG WHERE Province = \'%s\' AND City = \'%s\'", province, city));
                        if(!rs.next()) System.out.printf("%s %s\n", province, city);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
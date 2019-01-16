package Test;

import Model.Project;
import Service.ExcelReader;
import Service.InsertProject;
import Service.Validation;
import org.json.JSONObject;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ValidationTest {

    @Test
    public void validate(){
        try {
            Validation validation = new Validation(4, true);
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

}
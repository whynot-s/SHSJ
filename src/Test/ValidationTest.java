package Test;

import Service.Validation;
import org.json.JSONObject;
import org.junit.Test;

import java.sql.SQLException;

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

}
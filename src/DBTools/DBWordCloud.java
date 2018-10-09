package DBTools;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBWordCloud {

    private Connection connection;
    private String updateNull = "UPDATE WordCLoud SET %s = null WHERE fileName = \'%s\'";

    public DBWordCloud(){
        connection = DBUtil.getConnection();
    }

    public void insert(String fileName, String fileType,String direction, String field, String plan) throws SQLException {
        plan = plan.replaceAll("\'", "\"");
        Statement stmt = connection.createStatement();
        stmt.execute(String.format("INSERT INTO WordCloud(fileName,fileType,direction,field,plan) VALUES(\'%s\',\'%s\',\'%s\',\'%s\',\'%s\') " +
                        "ON DUPLICATE KEY UPDATE direction=\'%s\',fileType=\'%s\',field=\'%s\',plan=\'%s\'",
                fileName, fileType, direction, field, plan, direction, fileType, field, plan));
        if(direction == null || direction.trim().equals("")) stmt.execute(String.format(updateNull, "direction", fileName));
        if(field == null || field.trim().equals("")) stmt.execute(String.format(updateNull, "field", fileName));
        if(plan == null || plan.trim().equals("")) stmt.execute(String.format(updateNull, "plan", fileName));
        stmt.close();
    }

}

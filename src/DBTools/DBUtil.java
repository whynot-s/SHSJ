package DBTools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static String url="jdbc:mysql://localhost:3306/Socpra?useUnicode=true&characterEncoding=gb2312";
    private static String driverClass="com.mysql.jdbc.Driver";
//    private static String username = "webuser";
//    private static String password = "zxbl0000";
    private static String username="root";
    private static String password="zxbl1111";
    private static Connection conn;
    //装载驱动
    static{
        try{
            Class.forName(driverClass);
        }
        catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    //获取数据库连接
    public static Connection getConnection(){
        try{
            conn=DriverManager.getConnection(url,username,password);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return conn;
    }

    //关闭数据库连接
    public static void Close(){
        if(conn!=null){
            try{
                conn.close();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }
    }
}

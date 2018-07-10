package Test;

import Service.NumberImporter;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class NumberImporterTest {

    @Test
    public void projectTest(){
        NumberImporter numberImporter = new NumberImporter("/Users/wangjiaruijue/Documents/tw/副本2018暑期实践队安全报备汇总表.xls");
        Map<Integer, String> numbers = new HashMap<>();
        try {
            int r = numberImporter.readNumber(numbers);
            System.out.println(r);
            if(r == 0)
                numberImporter.updateNumber(numbers);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
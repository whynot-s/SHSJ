package Service;

import DBTools.DBProject;
import DBTools.DBUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class NumberImporter {

    private HSSFWorkbook wb;

    public NumberImporter(String filePath){
        try {
            InputStream stream = new FileInputStream(filePath);
            POIFSFileSystem fs =  new POIFSFileSystem(stream);
            wb = new HSSFWorkbook(fs);
        }catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public int readNumber(Map<Integer, String> numbers) throws SQLException {
        HSSFSheet sheet = wb.getSheet("Feuil1");
        if(sheet == null) return -1;
        Connection conn = DBUtil.getConnection();
        int r = 0;
        for(int rowNum = 3; rowNum <= sheet.getLastRowNum(); rowNum++){
            HSSFRow row = sheet.getRow(rowNum);
            if(row == null) continue;
            String number = ExcelReader.readCell(row.getCell(0));
            String teamName = ExcelReader.readCell(row.getCell(1));
            String teamLeader = ExcelReader.readCell(row.getCell(2));
            String teacher =  ExcelReader.readCell(row.getCell(4));
            List<Integer> ids = DBProject.ProjectNumber(conn, teamName, teamLeader, teacher);
            if(ids.size() != 1) {
                System.out.printf("%s : %d\n", teamName, ids.size());
                r = -2;
            }else{
                numbers.put(ids.get(0), number);
            }
        }
        conn.close();
        return 0;
    }

    public int updateNumber(Map<Integer, String> numbers) throws SQLException {
        Connection conn = DBUtil.getConnection();
        for(Map.Entry<Integer, String> number : numbers.entrySet())
            DBProject.UpdateNumber(conn, number.getValue(), number.getKey());
        conn.close();
        return 0;
    }

}

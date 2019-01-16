package Service;

import Model.ExcelColumn;
import Model.Project;
import Model.Season;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelReader {

    public static String readCell(HSSFCell cell){
        if(cell == null) return "";
        else if(cell.getCellTypeEnum() == CellType.NUMERIC){
            if(HSSFDateUtil.isCellDateFormatted(cell)){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
            }else{
                DecimalFormat format = new DecimalFormat("#");
                Number value = cell.getNumericCellValue();
                return format.format(value);
            }
        }else if(cell.getCellTypeEnum() == CellType.STRING){
            return cell.getStringCellValue();
        }
        return "";
    }

    public static int readFile(String filepath, List<Project> projects, boolean isWinter){
        try {
            HSSFSheet sheet = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(filepath))).getSheet("sheet1");
            for(int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                HSSFRow row = sheet.getRow(rowNum);
                Map<String, String> members = new HashMap<>();
                Map<String, String> phones = new HashMap<>();
                for (int i = 0; i < ((ExcelColumn.STU_END.Index(isWinter) - ExcelColumn.STU_START.Index(isWinter) + 1) / 3); i++) {
                    String stuNo = ExcelReader.readCell(row.getCell(ExcelColumn.STU_START.Index(isWinter) + 3 * i + 1)).trim();
                    String stuName = ExcelReader.readCell(row.getCell(ExcelColumn.STU_START.Index(isWinter) + 3 * i)).trim();
                    String stuPhone = ExcelReader.readCell(row.getCell(ExcelColumn.STU_START.Index(isWinter) + 3 * i + 2)).trim();
                    if(stuNo.equals("") || stuName.equals("")) continue;
                    members.put(stuNo, stuName);
                    phones.put(stuNo, stuPhone);
                }
                String direction = null;
                try{
                    direction = Project.ParseDirection(Integer.parseInt(ExcelReader.readCell(row.getCell(ExcelColumn.DIRECTION.Index(isWinter))).trim()));
                }catch (Exception e){
                    direction = ExcelReader.readCell(row.getCell(ExcelColumn.DIRECTION.Index(isWinter))).trim();
                }
                projects.add(new Project(
                    ExcelReader.readCell(row.getCell(ExcelColumn.TEAM_NAME.Index(isWinter))).trim(),
                    Integer.parseInt(ExcelReader.readCell(row.getCell(ExcelColumn.DEP.Index(isWinter))).trim()),
                    ExcelReader.readCell(row.getCell(ExcelColumn.LEADER_ID.Index(isWinter))).trim(),
                    ExcelReader.readCell(row.getCell(ExcelColumn.LEADER_NAME.Index(isWinter))).trim(),
                    ExcelReader.readCell(row.getCell(ExcelColumn.LEADER_PHONE.Index(isWinter))).trim(),
                    ExcelReader.readCell(row.getCell(ExcelColumn.TEACHER1_NAME.Index(isWinter))).trim(),
                    ExcelReader.readCell(row.getCell(ExcelColumn.TEACHER1_PHONE.Index(isWinter))).trim(),
                    ExcelReader.readCell(row.getCell(ExcelColumn.TEACHER2_NAME.Index(isWinter))).trim(),
                    ExcelReader.readCell(row.getCell(ExcelColumn.TEACHER2_PHONE.Index(isWinter))).trim(),
                    direction, Season.CurrentSeason(), members, phones
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}

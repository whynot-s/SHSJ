package Service;

import Model.Schedule;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;

import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    private HSSFWorkbook wb;

    public ExcelReader(String filePath){
        try {
            InputStream stream = new FileInputStream(filePath);
            POIFSFileSystem fs =  new POIFSFileSystem(stream);
            wb = new HSSFWorkbook(fs);
        }catch (Exception ex){
            System.out.println(ex.toString());
        }
    }

    public int readSchedule(List<Schedule> schedules){
        HSSFSheet sheet = wb.getSheet("实践时间及地点统计");
        if(sheet == null)return -1;
        int rowNum = 2;
        HSSFRow row = sheet.getRow(rowNum);
        if(row == null) return -1;
        List<String> dates = new ArrayList<String>();
        for(int cellNum = 1; cellNum < row.getLastCellNum(); cellNum+=2)
            dates.add(readCell(row.getCell(cellNum)));
        for(rowNum = 4; rowNum <= sheet.getLastRowNum(); rowNum++){
            row = sheet.getRow(rowNum);
            if(row == null)continue;
            String teamName = readCell(row.getCell(0));
            if(teamName.equals(""))continue;
            Schedule schedule = new Schedule(teamName);
            for(int cellNum = 1; cellNum <= row.getLastCellNum(); cellNum+=2){
                HSSFCell provCell = row.getCell(cellNum);
                HSSFCell cityCell = row.getCell(cellNum + 1);
                if(provCell == null || cityCell == null)continue;
                String province = readCell(provCell);
                String city = readCell(cityCell);
                if(province.equals("") || city.equals(""))continue;
                schedule.append(dates.get((cellNum - 1)/2), province, city);
            }
            if(schedule.size() > 0)schedules.add(schedule);
        }
        return 0;
    }

    private String readCell(HSSFCell cell){
        if(cell == null) return "";
        else if(cell.getCellTypeEnum() == CellType.NUMERIC){
            if(HSSFDateUtil.isCellDateFormatted(cell)){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue())).toString();
            }
        }else if(cell.getCellTypeEnum() == CellType.STRING){
            return cell.getStringCellValue();
        }
        return "";
    }
}

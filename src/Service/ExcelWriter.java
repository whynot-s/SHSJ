package Service;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ExcelWriter {

    private String filepath;
    private Workbook wb;
    private Sheet sheet;
    private int rowNum;

    public ExcelWriter(String filePath){
        filepath = filePath;
        wb = new HSSFWorkbook();
        sheet = wb.createSheet("Sheet1");
        rowNum = 0;
    }

    public int appendLine(Row row){
        Row newRow = sheet.createRow(rowNum++);
        for(int i = 0; i < row.getLastCellNum(); i++){
            Cell cell = row.getCell(i);
            if(cell == null){
                newRow.createCell(i).setCellValue("");
            } else if(cell.getCellTypeEnum().equals(CellType.STRING)){
                newRow.createCell(i).setCellValue(cell.getStringCellValue());
            }else if(cell.getCellTypeEnum().equals(CellType.NUMERIC)){
                newRow.createCell(i).setCellValue(cell.getNumericCellValue());
            }
        }
        return 0;
    }

    public int save(){
        try {
            FileOutputStream fos = new FileOutputStream(filepath);
            wb.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        return 1;
    }

}

package Service;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellType;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

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
}

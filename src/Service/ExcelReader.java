package Service;

import Model.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

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
            Location location = new Location();
            HSSFSheet sheet = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(filepath))).getSheet("sheet1");
            for(int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                HSSFRow row = sheet.getRow(rowNum);
                Map<String, String> members = new HashMap<>();
                Map<String, String> phones = new HashMap<>();
                for (int i = 0; i < ((ExcelColumn.STU_END.Index(isWinter) - ExcelColumn.STU_START.Index(isWinter) + 1) / 3); i++) {
                    String stuNo = ExcelReader.readCell(row.getCell(ExcelColumn.STU_START.Index(isWinter) + 3 * i + 1)).trim();
                    String stuName = ExcelReader.readCell(row.getCell(ExcelColumn.STU_START.Index(isWinter) + 3 * i)).trim();
                    String stuPhone = ExcelReader.readCell(row.getCell(ExcelColumn.STU_START.Index(isWinter) + 3 * i + 2)).trim();
                    if(stuNo.equals("") || stuName.equals("") || stuNo.equals("(空)") || stuName.equals("(空)")) continue;
                    members.put(stuNo, stuName);
                    phones.put(stuNo, stuPhone);
                }
                String direction = null;
                try{
                    direction = Project.ParseDirection(Integer.parseInt(ExcelReader.readCell(row.getCell(ExcelColumn.DIRECTION.Index(isWinter))).trim()));
                }catch (Exception e){
                    direction = ExcelReader.readCell(row.getCell(ExcelColumn.DIRECTION.Index(isWinter))).trim();
                }
                String t1 = ExcelReader.readCell(row.getCell(ExcelColumn.TEACHER1_NAME.Index(isWinter))).trim();
                String t1p = ExcelReader.readCell(row.getCell(ExcelColumn.TEACHER1_PHONE.Index(isWinter))).trim();
                String t2 = ExcelReader.readCell(row.getCell(ExcelColumn.TEACHER2_NAME.Index(isWinter))).trim();
                String t2p = ExcelReader.readCell(row.getCell(ExcelColumn.TEACHER2_PHONE.Index(isWinter))).trim();

                ArrayList<Schedule> schedules = new ArrayList<>();
                int ob = Integer.parseInt(ExcelReader.readCell(row.getCell(ExcelColumn.ONBROAD.Index(isWinter))).trim());
                if(ob == 1){
                    for(int i = 0; i < (ExcelColumn.SCH_GLO_END.Index(isWinter) - ExcelColumn.SCH_GLO_START.Index(isWinter) + 1) / 3; i++){
                        String startTime = ExcelReader.readCell(row.getCell(ExcelColumn.SCH_GLO_START.Index(isWinter) + 3 * i)).trim();
                        String endTime = ExcelReader.readCell(row.getCell(ExcelColumn.SCH_GLO_START.Index(isWinter) + 3 * i + 1)).trim();
                        String place = ExcelReader.readCell(row.getCell(ExcelColumn.SCH_GLO_START.Index(isWinter) + 3 * i + 2)).trim();
                        if(startTime.equals("") || endTime.equals("") || place.equals("") ||
                           startTime.equals("(空)") || endTime.equals("(空)") || place.equals("(空)")) continue;
                        schedules.addAll(processSchedule(startTime, endTime, place, location, false));
                    }
                }else{
                    for(int i = 0; i < (ExcelColumn.SCH_DOM_END.Index(isWinter) - ExcelColumn.SCH_DOM_START.Index(isWinter) + 1) / 3; i++){
                        String startTime = ExcelReader.readCell(row.getCell(ExcelColumn.SCH_DOM_START.Index(isWinter) + 3 * i)).trim();
                        String endTime = ExcelReader.readCell(row.getCell(ExcelColumn.SCH_DOM_START.Index(isWinter) + 3 * i + 1)).trim();
                        String place = ExcelReader.readCell(row.getCell(ExcelColumn.SCH_DOM_START.Index(isWinter) + 3 * i + 2)).trim();
                        if(startTime.equals("") || endTime.equals("") || place.equals("") ||
                           startTime.equals("(空)") || endTime.equals("(空)") || place.equals("(空)")) continue;
                        schedules.addAll(processSchedule(startTime, endTime, place, location, true));
                    }
                }
                projects.add(new Project(
                    ExcelReader.readCell(row.getCell(ExcelColumn.TEAM_NAME.Index(isWinter))).trim(),
                    Integer.parseInt(ExcelReader.readCell(row.getCell(ExcelColumn.DEP.Index(isWinter))).trim()),
                    ExcelReader.readCell(row.getCell(ExcelColumn.LEADER_ID.Index(isWinter))).trim(),
                    ExcelReader.readCell(row.getCell(ExcelColumn.LEADER_NAME.Index(isWinter))).trim(),
                    ExcelReader.readCell(row.getCell(ExcelColumn.LEADER_PHONE.Index(isWinter))).trim(),
                    t1.equals("(空)") ? "" : t1,
                    t1p.equals("(空)") ? "" : t1p,
                    t2.equals("(空)") ? "" : t2,
                    t2p.equals("(空)") ? "" : t2p,
                    direction, Season.CurrentSeason(), members, phones, schedules
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static List<Schedule> processSchedule(String startTime, String endTime, String places, Location location, boolean domestic){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        List<Schedule> schedules = new ArrayList<>();
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.set(Integer.parseInt(startTime.split("-")[0]), Integer.parseInt(startTime.split("-")[1]) - 1, Integer.parseInt(startTime.split("-")[2]));
        c2.set(Integer.parseInt(endTime.split("-")[0]), Integer.parseInt(endTime.split("-")[1]) - 1, Integer.parseInt(endTime.split("-")[2]));
        if(c1.after(c2)){
            c2.set(Integer.parseInt(startTime.split("-")[0]), Integer.parseInt(startTime.split("-")[1]) - 1, Integer.parseInt(startTime.split("-")[2]));
            c1.set(Integer.parseInt(endTime.split("-")[0]), Integer.parseInt(endTime.split("-")[1]) - 1, Integer.parseInt(endTime.split("-")[2]));
        }
        if(domestic) {
            String[] loc = location.parsePlace(places);
            while (!c1.after(c2)) {
                schedules.add(new Schedule(df.format(c1.getTime()), loc[0], loc[1]));
                c1.add(Calendar.DATE, 1);
            }
        }else{
            try {
                String contry = places.split("-")[0];
                String city = places.split("-")[1];
                while (!c1.after(c2)) {
                    schedules.add(new Schedule(df.format(c1.getTime()), contry, city));
                    c1.add(Calendar.DATE, 1);
                }
            }catch (Exception e){
                return schedules;
            }
        }
        return schedules;
    }

}

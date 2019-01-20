package Service;

import DBTools.DBUtil;
import Model.ExcelColumn;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Validation {

    private String filepath;
    private Statement stmt;
    private boolean isWinter;
    private int fid;
    private String newFilePath;

    public Validation(int id, boolean isWinter) throws SQLException {
        fid = id;
        Connection conn = DBUtil.getConnection();
        stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(String.format("SELECT filePath FROM Uploaded WHERE id = %d", id));
        rs.next();
        filepath = rs.getString(1);
        this.isWinter = isWinter;
    }

    public void summary() {
        try {
            HSSFSheet sheet = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(newFilePath))).getSheet("sheet1");
            int projects = sheet.getLastRowNum();
            if(projects > 0){
                String firstSubmit = ExcelReader.readCell(sheet.getRow(1).getCell(ExcelColumn.SUBMIT.Index(isWinter))).trim();
                String lastSubmit = ExcelReader.readCell(sheet.getRow(projects + 1).getCell(ExcelColumn.SUBMIT.Index(isWinter))).trim();
                Set<Integer> deps = new HashSet<>();
                Set<String> stuNos = new HashSet<>();
                Set<String> teachers = new HashSet<>();
                for(int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    HSSFRow row = sheet.getRow(rowNum);
                    if(row == null) continue;
                    deps.add(Integer.parseInt(ExcelReader.readCell(row.getCell(ExcelColumn.DEP.Index(isWinter))).trim()));
                    teachers.add(ExcelReader.readCell(row.getCell(ExcelColumn.TEACHER1_NAME.Index(isWinter))).trim());
                    teachers.add(ExcelReader.readCell(row.getCell(ExcelColumn.TEACHER2_NAME.Index(isWinter))).trim());
                    for(int i =  -1; i < ((ExcelColumn.STU_END.Index(isWinter) - ExcelColumn.STU_START.Index(isWinter) + 1) / 3); i++){
                        String stuNo;
                        if(i == -1)  stuNo = ExcelReader.readCell(row.getCell(ExcelColumn.LEADER_ID.Index(isWinter))).trim();
                        else stuNo = ExcelReader.readCell(row.getCell(ExcelColumn.STU_START.Index(isWinter) + 3 * i + 1)).trim();
                        stuNos.add(stuNo);
                    }
                }
                Connection conn = DBUtil.getConnection();
                Statement stmt = conn.createStatement();
                stmt.execute(String.format("UPDATE Uploaded SET projects=%d, firstSubmit=\'%s\', lastSubmit=\'%s\'," +
                                "depCount=%d, students=%d, teachers=%d WHERE id=%d",
                        projects, firstSubmit, lastSubmit, deps.size(), stuNos.size(), teachers.size(), fid));
            } else return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int validate(JSONObject message, JSONObject data){
        List<Integer> rids = new ArrayList<>();
        if(filepath.endsWith("xls")) newFilePath = filepath.substring(0, filepath.lastIndexOf("xls")) + "_Sorted.xls";
        else newFilePath = filepath.substring(0, filepath.lastIndexOf("xlsx")) + "_Sorted.xls";
        ExcelWriter excelWriter = new ExcelWriter(newFilePath);
        int value = 0;
        try {
            ArrayList<String> keys = new ArrayList<>();
            Map<String, JSONObject> conflicts = new HashMap<>();
            InputStream stream = new FileInputStream(filepath);
            POIFSFileSystem fs =  new POIFSFileSystem(stream);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFSheet sheet = wb.getSheet("Sheet1");
            ArrayList<String> names = new ArrayList<>();
            Map<String, List<Integer>> InsideTNmapper = new HashMap<>();
            Map<String, String> InsideMapper = new HashMap<>();
            Map<String, Map<Integer, String>> InsideSNmapper = new HashMap<>();
            Map<String, List<Integer>> InsideRowMapper = new HashMap<>();
            Map<String, String> ProjectName = new HashMap<>();
            excelWriter.appendLine(sheet.getRow(0));
            for(int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++){
                HSSFRow row = sheet.getRow(rowNum);
                if(row == null) continue;
                String teamName = ExcelReader.readCell(row.getCell(ExcelColumn.TEAM_NAME.Index(isWinter))).trim();
                names.add(teamName);
                //表内队名冲突
                if(!InsideTNmapper.containsKey(teamName)) {
                    InsideTNmapper.put(teamName, new ArrayList<>());
                    ProjectName.put(teamName, ExcelReader.readCell(row.getCell(ExcelColumn.LEADER_NAME.Index(isWinter))).trim());
                    rids.add(rowNum);
                } else {
                    String ln = ExcelReader.readCell(row.getCell(ExcelColumn.LEADER_NAME.Index(isWinter))).trim();
                    String lnn = ProjectName.get(teamName);
                    if(lnn.equals(ln)) continue;
                    else rids.add(rowNum);
                }
                InsideTNmapper.get(teamName).add(rowNum);
                //历史队名冲突
                JSONObject result = existGlobalTeamName(teamName);
                if(result != null){
                    keys.add(String.format("第%d行 - %s", rowNum, names.get(rowNum - 1)));
                    conflicts.put(String.format("第%d行 - %s", rowNum, names.get(rowNum - 1)), new JSONObject());
                    JSONObject[] ct = new JSONObject[2];
                    ct[0] = inTableTeam(sheet, rowNum);
                    ct[1] = result;
                    JSONObject wh = new JSONObject();
                    List<JSONArray> tt = new ArrayList<>();
                    wh.put("head", headers("TeamName_History"));
                    tt.add(new JSONArray(ct));
                    wh.put("data", new JSONArray(tt));
                    conflicts.get(String.format("第%d行 - %s", rowNum, names.get(rowNum - 1))).put("与历史队名冲突", wh);
                    value += 1;
                }
                //历史及队内学号姓名冲突
                Map<String, String> teamMember = new HashMap<>();
                List<JSONArray> cts = new ArrayList<>();
                List<JSONArray> cts2 = new ArrayList<>();
                List<JSONArray> cts3 = new ArrayList<>();
                for(int i =  -1; i < ((ExcelColumn.STU_END.Index(isWinter) - ExcelColumn.STU_START.Index(isWinter) + 1) / 3); i++){
                    String stuNo = null;
                    String stuName = null;
                    if(i == -1){
                        stuNo = ExcelReader.readCell(row.getCell(ExcelColumn.LEADER_ID.Index(isWinter))).trim();
                        stuName = ExcelReader.readCell(row.getCell(ExcelColumn.LEADER_NAME.Index(isWinter))).trim();
                    }else{
                        stuNo = ExcelReader.readCell(row.getCell(ExcelColumn.STU_START.Index(isWinter) + 3 * i + 1)).trim();
                        stuName = ExcelReader.readCell(row.getCell(ExcelColumn.STU_START.Index(isWinter) + 3 * i)).trim();
                    }
                    //未填学号
                    if((stuNo.equals("") || stuNo.equals("(空)")) && !(stuName.equals("") || stuName.equals("(空)"))){
                        JSONObject[] tmp = new JSONObject[1];
                        tmp[0] = new JSONObject();
                        tmp[0].put("rowId", rowNum);
                        tmp[0].put("stuName", stuName);
                        cts3.add(new JSONArray(tmp));
                    }
                    if(stuNo.equals("") || stuName.equals("") || stuNo.equals("(空)") || stuName.equals("(空)")) continue;
                    boolean flag = false;
                    //队内学号姓名冲突
                    if(!teamMember.containsKey(stuNo)) teamMember.put(stuNo, stuName);
                    else if(teamMember.get(stuNo).equals(stuName)) continue;
                    else{
                        JSONObject[] ct = new JSONObject[2];
                        ct[0] = new JSONObject();
                        ct[0].put("rowId", rowNum);
                        ct[0].put("stuNo", stuNo);
                        ct[0].put("stuName", stuName);
                        ct[1] = new JSONObject();
                        ct[1].put("rowId", rowNum);
                        ct[1].put("stuNo", stuNo);
                        ct[1].put("stuName", teamMember.get(stuNo));
                        cts2.add(new JSONArray(ct));
                        value += 1;
                        flag = true;
                    }
                    //表内学号姓名冲突
                    if(!InsideMapper.containsKey(stuNo)) {
                        InsideMapper.put(stuNo, stuName);
                        InsideRowMapper.put(stuNo, new ArrayList<>());
                        InsideRowMapper.get(stuNo).add(rowNum);
                    } else if(InsideMapper.get(stuNo).equals(stuName)){
                        InsideRowMapper.get(stuNo).add(rowNum);
                    } else if(!flag){
                        if(!InsideSNmapper.containsKey(stuNo)) {
                            InsideSNmapper.put(stuNo, new HashMap<>());
                            InsideSNmapper.get(stuNo).put(rowNum, stuName);
                            InsideSNmapper.get(stuNo).put(InsideRowMapper.get(stuNo).get(0), InsideMapper.get(stuNo));
                        }else{
                            InsideSNmapper.get(stuNo).put(rowNum, stuName);
                        }
                        InsideMapper.put(stuNo, stuName);
                    }
                    //历史学号姓名冲突
                    result = existGlobalStuNo(stuNo, stuName);
                    if(result != null){
                        JSONObject tmp = new JSONObject();
                        tmp.put("rowId", rowNum);
                        tmp.put("stuNo", stuNo);
                        tmp.put("stuName", stuName);
                        JSONObject[] ct = new JSONObject[2];
                        ct[0] = tmp;
                        ct[1] = result;
                        cts.add(new JSONArray(ct));
                        value += 1;
                    }
                }
                if(cts.size() != 0){
                    if(!conflicts.containsKey(String.format("第%d行 - %s", rowNum, names.get(rowNum - 1)))) {
                        keys.add(String.format("第%d行 - %s", rowNum, names.get(rowNum - 1)));
                        conflicts.put(String.format("第%d行 - %s", rowNum, names.get(rowNum - 1)), new JSONObject());
                    }
                    JSONObject wh = new JSONObject();
                    wh.put("head", headers("stuNo_History"));
                    wh.put("data", new JSONArray(cts));
                    conflicts.get(String.format("第%d行 - %s", rowNum, names.get(rowNum - 1))).put("与历史学号姓名不匹配", wh);
                }
                if(cts2.size() != 0){
                    if(!conflicts.containsKey(String.format("第%d行 - %s", rowNum, names.get(rowNum - 1)))) {
                        keys.add(String.format("第%d行 - %s", rowNum, names.get(rowNum - 1)));
                        conflicts.put(String.format("第%d行 - %s", rowNum, names.get(rowNum - 1)), new JSONObject());
                    }
                    JSONObject wh = new JSONObject();
                    wh.put("head", headers("stuNo_Team"));
                    wh.put("data", new JSONArray(cts2));
                    conflicts.get(String.format("第%d行 - %s", rowNum, names.get(rowNum - 1))).put("实践队伍内学号姓名不匹配", wh);
                }
                if(cts3.size() != 0){
                    if(!conflicts.containsKey(String.format("第%d行 - %s", rowNum, names.get(rowNum - 1)))) {
                        keys.add(String.format("第%d行 - %s", rowNum, names.get(rowNum - 1)));
                        conflicts.put(String.format("第%d行 - %s", rowNum, names.get(rowNum - 1)), new JSONObject());
                    }
                    JSONObject wh = new JSONObject();
                    wh.put("head", headers("stuNo_Miss"));
                    wh.put("data", new JSONArray(cts3));
                    conflicts.get(String.format("第%d行 - %s", rowNum, names.get(rowNum - 1))).put("未填写学号", wh);
                }
            }
            //表内队名冲突
            for(Map.Entry<String, List<Integer>> entry : InsideTNmapper.entrySet()){
                if(entry.getValue().size() == 1) continue;
                else{
                    List<JSONObject> tmp = new ArrayList<>();
                    for(int id : entry.getValue()) tmp.add(inTableTeam(sheet, id));
                    for(int id : entry.getValue()){
                        if(!conflicts.containsKey(String.format("第%d行 - %s", id, names.get(id - 1)))) {
                            keys.add(String.format("第%d行 - %s", id, names.get(id - 1)));
                            conflicts.put(String.format("第%d行 - %s", id, names.get(id - 1)), new JSONObject());
                        }
                        JSONObject wh = new JSONObject();
                        List<JSONArray> tt = new ArrayList<>();
                        wh.put("head", headers("TeamName_Table"));
                        tt.add(new JSONArray(tmp));
                        wh.put("data", new JSONArray(tt));
                        conflicts.get(String.format("第%d行 - %s", id, names.get(id - 1))).put("表格内队名冲突", wh);
                        value += 1;
                    }
                }
            }
            //表内学号姓名冲突
            Map<Integer, List<JSONArray>> inTableSN = new HashMap<>();
            for(Map.Entry<String, Map<Integer, String>> entry : InsideSNmapper.entrySet()){
                String stuNo = entry.getKey();
                List<JSONObject> tmp = new ArrayList<>();
                for(Map.Entry<Integer, String> entry1 : entry.getValue().entrySet()){
                    JSONObject tmp1 = new JSONObject();
                    tmp1.put("rowId", entry1.getKey());
                    tmp1.put("stuNo", stuNo);
                    tmp1.put("stuName", entry1.getValue());
                    tmp.add(tmp1);
                    value += 1;
                }
                for(Integer id : entry.getValue().keySet()){
                    if(!inTableSN.containsKey(id)) inTableSN.put(id, new ArrayList<>());
                    inTableSN.get(id).add(new JSONArray(tmp));
                }
            }
            for(Map.Entry<Integer, List<JSONArray>> entry : inTableSN.entrySet()){
                int rowNum = entry.getKey();
                if(!conflicts.containsKey(String.format("第%d行 - %s", rowNum, names.get(rowNum - 1)))){
                    keys.add(String.format("第%d行 - %s", rowNum, names.get(rowNum - 1)));
                    conflicts.put(String.format("第%d行 - %s", rowNum, names.get(rowNum - 1)), new JSONObject());
                }
                JSONObject wh = new JSONObject();
                wh.put("head", headers("stuNo_Table"));
                wh.put("data", new JSONArray(entry.getValue()));
                conflicts.get(String.format("第%d行 - %s", rowNum, names.get(rowNum - 1))).put("与表格内学号姓名不匹配", wh);
            }
            data.put("data", new JSONObject(conflicts));
            data.put("keys", keys);
            for(int rid : rids) {
                excelWriter.appendLine(sheet.getRow(rid));
            }
            stream.close();
        }catch (Exception ex){
            ex.printStackTrace();
            message.put("message", ex.getMessage());
            data.put("data", new JSONArray());
            data.put("keys", new JSONArray());
        }
        if(value == 0)
            message.put("message", "Passed");
        else if(message.isNull("message"))
            message.put("message", "Failed");
        try {
            excelWriter.save();
            stmt.execute(String.format("UPDATE Uploaded SET sortedFile = \'%s\' WHERE id = %d", newFilePath, fid));
        } catch (Exception e) {
            e.printStackTrace();
            message.put("message", e.getMessage());
            data.put("data", new JSONArray());
            data.put("keys", new JSONArray());
            return -1;
        }
        return value;
    }

    private JSONObject existGlobalTeamName(String name) throws SQLException {
        ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM Project WHERE teamName = \'%s\'", name));
        if(rs.next()){
            JSONObject result = new JSONObject();
            result.put("rowId", rs.getString("uid"));
            result.put("dep", rs.getInt("depNo"));
            result.put("teamName", rs.getString("teamName"));
            result.put("teamLeaderId", rs.getString("teamLeaderId"));
            result.put("teamTeacher", rs.getString("teamTeacher"));
            return result;
        }else
            return null;
    }

    private JSONObject existGlobalStuNo(String stuNo, String stuName) throws SQLException {
        ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM Member WHERE stuNo = \'%s\'", stuNo));
        if(rs.next()) {
            String name = rs.getString("stuName").trim();
            if(name.equals(stuName) || name.contains("?")) return null;
            else{
                JSONObject result = new JSONObject();
                result.put("stuNo", stuNo);
                result.put("stuName", name);
                result.put("rowId", 0);
                return result;
            }
        }else
            return null;
    }

    private JSONObject inTableTeam(HSSFSheet sheet, int rowId){
        HSSFRow row = sheet.getRow(rowId);
        JSONObject tmp = new JSONObject();
        tmp.put("rowId", rowId);
        tmp.put("dep", Integer.parseInt(ExcelReader.readCell(row.getCell(ExcelColumn.DEP.Index(isWinter))).trim()));
        tmp.put("teamName",  ExcelReader.readCell(row.getCell(ExcelColumn.TEAM_NAME.Index(isWinter))).trim());
        tmp.put("teamLeaderId", ExcelReader.readCell(row.getCell(ExcelColumn.LEADER_ID.Index(isWinter))).trim());
        tmp.put("teamTeacher", ExcelReader.readCell(row.getCell(ExcelColumn.TEACHER1_NAME.Index(isWinter))).trim());
        return tmp;
    }

    private JSONArray headers(String type){
        List<JSONObject> tmp = new ArrayList<>();
        if(type.equals("TeamName_History") || type.equals("TeamName_Table")){
            tmp.add(head("行号", "rowId", false));
            tmp.add(head("系号", "dep", false));
            tmp.add(head("队名", "teamName", true));
            tmp.add(head("队长学号", "teamLeaderId", false));
            tmp.add(head("指导教师", "teamTeacher", false));
        }else if(type.equals("stuNo_History") || type.equals("stuNo_Team") || type.equals("stuNo_Table")){
            tmp.add(head("行号", "rowId", false));
            tmp.add(head("学号", "stuNo", false));
            tmp.add(head("姓名", "stuName", true));
        }else if(type.equals("stuNo_Miss")){
            tmp.add(head("行号", "rowId", false));
            tmp.add(head("姓名", "stuName", true));
        }
        return new JSONArray(tmp);
    }

    private JSONObject head(String title, String key, boolean colored){
        JSONObject result = new JSONObject();
        result.put("title", title);
        result.put("key", key);
        if(colored) result.put("colored", 1);
        else result.put("colored", 0);
        return result;
    }

    public String getFilepath() {
        return filepath;
    }

    public String getNewFilePath(){
        return newFilePath;
    }

}

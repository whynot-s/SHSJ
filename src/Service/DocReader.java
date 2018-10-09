package Service;

import DBTools.DBWordCloud;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

public class DocReader {

    private String directory;
    private DBWordCloud dbWordCloud;

    public DocReader(String dir){
        directory = dir;
        dbWordCloud = new DBWordCloud();
    }

    public void readDocs() throws IOException {
        File dir = new File(directory);
        for(File file : dir.listFiles()){
            try {
                if(file.isHidden()) continue;
                if(!file.getName().endsWith(".docx") && !file.getName().endsWith(".doc")) continue;
                if(file.getName().endsWith(".docx")) readDocx(file.getAbsolutePath());
                else if(file.getName().endsWith(".doc")) readDoc(file.getAbsolutePath());
            }catch (Exception e){
                System.out.println(file.getName());
                System.out.println(e.toString());
            }
//            break;
        }
    }

    private void readDoc(String filePath) throws IOException, SQLException {
//        System.out.println(filePath);
        InputStream inputStream = new FileInputStream(filePath);
        HWPFDocument hwpfDocument = new HWPFDocument(inputStream);
        Range range = hwpfDocument.getRange();
        String plan = printInfo(range, "团队实践方案", "团队安全应急预案");
        String direction = readTable(range, "选题方向");
        String field = readTable(range, "实践课题");
        dbWordCloud.insert(filePath.substring(filePath.lastIndexOf("/") + 1),
                filePath.substring(filePath.lastIndexOf(".") + 1), direction, field, plan);
    }

    private void readDocx(String filePath) throws IOException, SQLException {
//        System.out.println(filePath);
        InputStream inputStream = new FileInputStream(filePath);
        XWPFDocument xwpfDocument = new XWPFDocument(inputStream);
        String[] endKeys = {"成员情况", "团队安全应急预案"};
        String plan = readTable(xwpfDocument.getTables(), "团队实践方案", endKeys);
        String direction = readTable(xwpfDocument.getTables(), "选题方向");
        String field = readTable(xwpfDocument.getTables(), "实践课题");
        dbWordCloud.insert(filePath.substring(filePath.lastIndexOf("/") + 1),
                filePath.substring(filePath.lastIndexOf(".") + 1), direction, field, plan);
    }

    private String readTable(Range range, String key) {
        boolean flag = false;
        TableIterator tableIter = new TableIterator(range);
        Table table;
        TableRow row;
        TableCell cell;
        while (tableIter.hasNext()) {
            table = tableIter.next();
            int rowNum = table.numRows();
            for (int j=0; j<rowNum; j++) {
                row = table.getRow(j);
                int cellNum = row.numCells();
                for (int k=0; k<cellNum; k++) {
                    cell = row.getCell(k);
                    if(flag) return cell.text().trim();
                    if(cell.text().trim().equals(key)) flag = true;
                }
            }
        }
        return null;
    }

    private String readTable(List<XWPFTable> tables, String key){
        boolean flag = false;
        List<XWPFTableRow> rows;
        List<XWPFTableCell> cells;
        for (XWPFTable table : tables) {
            rows = table.getRows();
            for (XWPFTableRow row : rows) {
                cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {
                    if(flag) return cell.getText().trim();
                    if(cell.getText().equals(key)) flag = true;
                }
            }
        }
        return null;
    }

    private String readTable(List<XWPFTable> tables, String key, String[] endKeys){
        StringBuilder info = new StringBuilder();
        boolean flag = false;
        List<XWPFTableRow> rows;
        List<XWPFTableCell> cells;
        for (XWPFTable table : tables) {
            rows = table.getRows();
            for (XWPFTableRow row : rows) {
                cells = row.getTableCells();
                for (XWPFTableCell cell : cells) {
                    if(flag) {
                        for (String K : endKeys)
                            if (cell.getText().startsWith(K))
                                return info.toString();
                        info.append(cell.getText().trim() + "\n");
                    }
                    if(cell.getText().contains(key)){
                        flag = true;
                        info.append(cell.getText().trim() + "\n");
                    }
                }
            }
        }
        return info.toString();
    }

    private String printInfo(Range range, String startKey, String endKey) {
        StringBuilder info = new StringBuilder();
        boolean flag = false;
        for (int i = 0; i < range.numParagraphs(); i++) {
            String text = range.getParagraph(i).text();
            if(text.startsWith(endKey)) break;
            if(text.startsWith(startKey)){
                flag = true;
                continue;
            }
            if(flag && !text.trim().equals("")) {
                info.append(text.trim() + "\n");
            }
        }
        return info.toString();
    }
}

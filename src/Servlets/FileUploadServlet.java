package Servlets;

import DBTools.DBUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

@WebServlet(name = "FileUploadServlet", urlPatterns = "/Upload")
public class FileUploadServlet extends HttpServlet {

    private long lMaxFileLength=400*1024*1024;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=GB2312");
        response.addHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Expires", "0");
        PrintWriter pw = response.getWriter();
        HttpSession session = request.getSession();
        Object tmp = session.getAttribute("userName");
        JSONObject result = new JSONObject();
        if(!tmp.toString().equals("dev")){
            result.put("Status", 4);
            result.put("Message", "Access Denied");
        }else {
            DiskFileItemFactory dfit = new DiskFileItemFactory();
            dfit.setSizeThreshold(4096);
            String strFolderPath = getServletContext().getRealPath("/TempFile");
            dfit.setRepository(new File(strFolderPath));
            ServletFileUpload upload = new ServletFileUpload(dfit);
            upload.setSizeMax(lMaxFileLength);
            try {
                List items = upload.parseRequest(request);
                Iterator itor = items.iterator();
                while (itor.hasNext()) {
                    FileItem fi = (FileItem) itor.next();
                    if (!fi.isFormField()) {
                        int status = processUploadFile(fi, result);
                        request.setAttribute("fid", status);
                    } else {
                        result.put("Status", 1);
                        result.put("Message", "Not a file request");
                    }
                }
            } catch (Exception e) {
                result.put("Status", 3);
                result.put("Message", e.getMessage());
            }
        }
        pw.print(result.toString());
        pw.flush();
        pw.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private int processUploadFile(FileItem fi, JSONObject result){
        try{
            String strFileName = fi.getName();
            strFileName = strFileName.substring(strFileName.lastIndexOf('\\') + 1, strFileName.length());
            long lFileSize = fi.getSize();
            if(lFileSize == 0 || strFileName.trim().length() == 0)
                return -3;
            String filepath = String.format("%s/%d_%s", getServletContext().getRealPath("/UploadFile"), System.currentTimeMillis(), strFileName);
            File flUpload = new File(filepath);
            fi.write(flUpload);
            Connection conn = DBUtil.getConnection();
            Statement stmt = conn.createStatement();
            stmt.execute(String.format("INSERT INTO Uploaded(filepath) VALUES(\'%s\')", filepath));
            ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID()");
            if(rs.next()) {
                int tmp = rs.getInt(1);
                result.put("Status", 0);
                result.put("Message", tmp);
                return tmp;
            }else{
                result.put("Status", 5);
                result.put("Message", "Unexpected Error");
                return -1;
            }
        }
        catch(Exception e){
            result.put("Status", 2);
            result.put("Message", e.getMessage());
        }
        return -2;
    }
}

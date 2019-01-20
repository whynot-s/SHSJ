package Servlets;

import DBTools.DBUtil;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(name = "DownloadServlet", urlPatterns = "/Download")
public class DownloadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            response.setContentType("text/html;charset=GB2312");
            response.addHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Expires", "0");
            JSONObject result = new JSONObject();
            HttpSession session = request.getSession();
            Object tmp = session.getAttribute("userName");
            if(tmp == null || !tmp.toString().equals("dev")){
                result.put("Status", -1);
                result.put("Message", "Access Denied");
                result.put("Data", new JSONObject());
                PrintWriter pw = response.getWriter();
                pw.print(result.toString());
                pw.flush();
                pw.close();
                return;
            }
            int id = Integer.parseInt(request.getParameter("fid"));
            Connection conn = DBUtil.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(String.format("SELECT sortedFile FROM Uploaded WHERE id = %d", id));
            rs.next();
            String filename = rs.getString("sortedFile");
            response.setContentType(getServletContext().getMimeType(filename.substring(filename.lastIndexOf("/") + 1)));
            response.setHeader("Content-Disposition", "attachment;filename="+filename.substring(filename.lastIndexOf("/")+ 1));
            InputStream in = new FileInputStream(filename);
            OutputStream out = response.getOutputStream();
            int b;
            while((b=in.read())!= -1) {
                out.write(b);
            }
            in.close();
            out.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

}

package Servlets;

import DBTools.DBUtil;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@WebServlet(name = "UploadSummaryServlet", urlPatterns = "/UploadSummary")
public class UploadSummaryServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=GB2312");
        response.addHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Expires", "0");
        PrintWriter pw = response.getWriter();
        HttpSession session = request.getSession();
        Object tmp = session.getAttribute("userName");
        int id = Integer.parseInt(request.getParameter("fid"));
        JSONObject result = new JSONObject();
        if(!tmp.toString().equals("dev")){
            result.put("Status", -1);
            result.put("Message", "Access Denied");
            result.put("Data", new JSONObject());
        }else{
            try {
                Connection conn = DBUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(String.format("SELECT projects, firstSubmit, lastSubmit, depCount, students, teachers, places FROM Uploaded WHERE id = %d", id));
                JSONObject summary = new JSONObject();
                if(!rs.next()){
                    result.put("Status", -3);
                    result.put("Message", "Error");
                    result.put("Data", new JSONObject());
                }else{
                    summary.put("projects", rs.getInt("projects"));
                    summary.put("firstSubmit", rs.getString("firstSubmit"));
                    summary.put("lastSubmit", rs.getString("lastSubmit"));
                    summary.put("depCount", rs.getInt("depCount"));
                    summary.put("students", rs.getInt("students"));
                    summary.put("teachers", rs.getInt("teachers"));
                    summary.put("places", rs.getInt("places"));
                    result.put("Status", 0);
                    result.put("Message", "Summary");
                    result.put("Data", summary);
                }
            } catch (SQLException e) {
                result.put("Status", -2);
                result.put("Message", e.getMessage());
                result.put("Data", new JSONObject());
            }
        }
        pw.print(result.toString());
        pw.flush();
        pw.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}

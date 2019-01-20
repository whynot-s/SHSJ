package Servlets;

import DBTools.DBUtil;
import Model.Project;
import Model.Season;
import Service.ExcelReader;
import Service.InsertProject;
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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "InsertServlet", urlPatterns = "/Insert")
public class InsertServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=GB2312");
        response.addHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Expires", "0");
        PrintWriter pw = response.getWriter();
        JSONObject result = new JSONObject();
        HttpSession session = request.getSession();
        Object tmp = session.getAttribute("userName");
        try {
            int id = Integer.parseInt(request.getParameter("fid"));
            if (tmp == null || !tmp.toString().equals("dev")) {
                result.put("Status", -1);
                result.put("Message", "Access Denied");
            } else {
                Connection conn = DBUtil.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet pwdset = stmt.executeQuery("SELECT pwd FROM SocUser WHERE userName = \'dev\'");
                pwdset.next();
                String p = pwdset.getString("pwd");
                if(!p.equals(request.getParameter("pwd"))){
                    result.put("Status", -1);
                    result.put("Message", "Access Denied");
                }else {
                    List<Project> projects = new ArrayList<>();
                    ResultSet rs = stmt.executeQuery(String.format("SELECT sortedFile FROM Uploaded WHERE id = %d", id));
                    rs.next();
                    String fp = rs.getString("sortedFile");
                    ExcelReader.readFile(fp, projects, Season.IsWinter());
                    int status = InsertProject.insert(projects);
                    if (status == 0) {
                        result.put("Status", 0);
                        result.put("Message", "Success");
                    } else {
                        result.put("Status", -3);
                        result.put("Message", "Internal Error, Contact Admins Or Retry");
                    }
                }
            }
        } catch (Exception e){
            result.put("Status", -2);
            result.put("Message", e.getMessage());
        }
        pw.print(result.toString());
        pw.flush();
        pw.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}

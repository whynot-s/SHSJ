package Servlets;

import Model.Season;
import Service.Validation;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ValidateServlet", urlPatterns = "/Validate")
public class ValidateServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=GB2312");
        response.addHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Expires", "0");
        PrintWriter pw = response.getWriter();
        HttpSession session = request.getSession();
        Object tmp = session.getAttribute("userName");
//        int status_global = (Integer) session.getAttribute("fid");
        int id = Integer.parseInt(request.getParameter("fid"));
        JSONObject result = new JSONObject();
        if(!tmp.toString().equals("dev")){
            result.put("Status", -1);
            result.put("Message", "Access Denied");
            result.put("Data", new JSONObject());
        }else{
            try {
                boolean isWinter = false;
                if(Season.IsWinter())
                    isWinter = true;
                JSONObject message = new JSONObject();
                JSONObject data = new JSONObject();
                Validation validation = new Validation(id, isWinter);
                int status = validation.validate(message, data);
                result.put("Status", status);
                result.put("Message", message.getString("message"));
                result.put("Data", data);
                validation.summary();
            }catch (Exception e){
                result.put("Status", -2);
                result.put("Message", "Param Errors");
                result.put("Data", new JSONObject());
            }
        }
        pw.print(result.toString());
        pw.flush();
        pw.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }
}

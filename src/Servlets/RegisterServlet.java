package Servlets;

import Model.RegisterParams;
import Service.Register;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "RegisterServlet", urlPatterns = "/Register")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> params = new HashMap<>();
        for(RegisterParams param : RegisterParams.values()){
            if(param.IsInt()) {
                try {
                    String tmp = request.getParameter(param.getKey());
                    if(tmp == null || tmp.equals("")){
                        tmp = null;
                        params.put(param.getKey(), tmp);
                    }else {
                        params.put(param.getKey(), Integer.parseInt(tmp));
                    }
                } catch(NumberFormatException ex){
                    params.put(param.getKey(), null);
                }
            }else {
                String tmp = request.getParameter(param.getKey());
                if(tmp == null || tmp.equals("")) tmp = null;
                params.put(param.getKey(), tmp);
            }
        }
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Register register = new Register();
            int level = register.valid((String)params.get(RegisterParams.INVITATION.getKey()));
            if(level == -1)
                out.println(0);
            else{
                if(register.exists((String)params.get(RegisterParams.USERNAME.getKey())))
                    out.println(1);
                else{
                    boolean status = register.register(
                            (String) params.get(RegisterParams.USERNAME.getKey()),
                            (String) params.get(RegisterParams.PWD.getKey()),
                            (String) params.get(RegisterParams.INVITATION.getKey()),
                            level
                    );
                    if(!status) out.println(2);
                    else out.println(3);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        out.flush();
        out.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
        return;
    }
}

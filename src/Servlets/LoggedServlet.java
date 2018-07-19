package Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoggedServlet", urlPatterns = "/Logged")
public class LoggedServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object tmp = session.getAttribute("userName");
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        if(tmp == null) out.println(0);
        else out.printf("%d\n%s", 1, tmp);
        out.flush();
        out.close();
    }
}

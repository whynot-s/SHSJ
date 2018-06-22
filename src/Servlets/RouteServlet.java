package Servlets;

import Service.Route;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "RouteServlet")
public class RouteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String startdate = request.getParameter("startdate");
        String enddate = request.getParameter("enddate");
        PrintWriter out = response.getWriter();
        out.println(Route.getRouteJSON(startdate, enddate));
        out.flush();
        out.close();
    }
}

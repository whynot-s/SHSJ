package Servlets;

import DBTools.DBSummary;
import Service.Summary;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(name = "SummaryServlet")
public class SummaryServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            Summary summary = new Summary();
            summary.addEle("team", DBSummary.getTeam());
            summary.addEle("people", DBSummary.getPeople());
            summary.addEle("place", DBSummary.getPlace());
            summary.addEle("day", DBSummary.getDay());
            out.println(summary.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        out.flush();
        out.close();
    }
}

package Servlets;

import Model.SearchParams;
import Service.Search;

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

@WebServlet(name = "SearchServlet")
public class SearchServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> params = new HashMap<>();
        for(SearchParams param : SearchParams.values()){
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
            Search search = new Search(params);
            out.println(search.getResult());
            search.endSearch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        out.flush();
        out.close();
    }
}

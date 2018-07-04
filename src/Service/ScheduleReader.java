package Service;

import DBTools.DBSchedule;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class ScheduleReader {

    public static boolean searchProject(Connection conn, Set<Integer> result, String date, String pro, String cit, boolean first) throws SQLException {
        String condition = "";
        boolean AND = false;
        if(date != null){
            condition += String.format("praDate = \'%s\' ", date);
            AND = true;
        }
        if(pro != null){
            if(AND) condition += "AND ";
            condition += String.format("praProvince LIKE \'%s%%\' ", pro);
            AND = true;
        }
        if(cit != null){
            if(AND) condition += "AND ";
            condition += String.format("praCity LIKE \'%s%%\' ", cit);
            AND = true;
        }
        if(!AND) return false;
        List<Integer> pids = DBSchedule.searchProject(conn, condition);
        if(!first)result.retainAll(pids);
        else result.addAll(pids);
        return true;
    }
}

package Service;

import DBTools.DBMember;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class MemberReader {

    public static boolean searchProject(Connection conn, Set<Integer> result, String stno, String stname, boolean first) throws SQLException {
        String condition = "";
        boolean AND = false;
        if(stno != null){
            condition += String.format("Member.stuNo LIKE \'%s%%\' ", stno);
            AND = true;
        }
        if(stname != null){
            if(AND) condition += "AND ";
            condition += String.format("Member.stuName LIKE \'%s%%\' ", stname);
            AND = true;
        }
        if(!AND) return false;
        List<Integer> pids = DBMember.searchProject(conn, condition);
        if(!first)result.retainAll(pids);
        else result.addAll(pids);
        return true;
    }

}

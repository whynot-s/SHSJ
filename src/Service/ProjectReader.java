package Service;

import DBTools.DBProject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class ProjectReader {

    public static boolean searchProject(Connection conn, Set<Integer> result, Integer dep, String tname, String teacher, boolean first) throws SQLException {
        String condition = "";
        boolean AND = false;
        if(dep != null){
            condition += String.format("depNo = %d ", dep);
            AND = true;
        }
        if(tname != null){
            if(AND) condition += "AND ";
            condition += String.format("teamName LIKE \'%s%%\' ", tname);
            AND = true;
        }
        if(teacher != null){
            if(AND) condition += "AND ";
            condition += String.format("teamTeacher LIKE \'%s%%\' ", teacher);
            AND = true;
        }
        if(!AND) return false;
        List<Integer> pids = DBProject.SearchProject(conn, condition);
        if(!first)result.retainAll(pids);
        else result.addAll(pids);
        return true;
    }

}

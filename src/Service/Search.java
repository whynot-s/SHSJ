package Service;

import DBTools.DBMember;
import DBTools.DBProject;
import DBTools.DBSchedule;
import DBTools.DBUtil;
import Model.SearchParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class Search {

    final private static String[] keys = {"ProjectList", "Projects", "Information", "Member", "Schedule"};
    private Connection conn;
    private Map<String, Object> params;
    private JSONObject result;

    public Search(Map<String, Object> params) throws SQLException {
        conn = DBUtil.getConnection();
        this.params = params;
        result = new JSONObject();
        search();
    }

    private int search_pid(Set<Integer> pids) throws SQLException {
        boolean re = ProjectReader.searchProject(conn, pids,
                (Integer) params.get(SearchParams.DEPARTMENT.getKey()),
                (String)  params.get(SearchParams.TEAMNAME.getKey()),
                (String)  params.get(SearchParams.TEACHER.getKey()),
                true
        );
        re = ScheduleReader.searchProject(conn, pids,
                (String)  params.get(SearchParams.DATE.getKey()),
                ! re
        );
        re = MemberReader.searchProject(conn, pids,
                (String)  params.get(SearchParams.STUDENTNO.getKey()),
                (String)  params.get(SearchParams.STUDENTNAME.getKey()),
                ! re
        );
        return 0;
    }


    private void search() throws SQLException {
        Set<Integer> pids = new HashSet<>();
        search_pid(pids);
        List<Object> projectInfo = new ArrayList<>();
        List<Object> projects = new ArrayList<>();
        for(Integer pid : pids) {
            projectInfo.add(DBProject.SelectProject(conn, pid));
            JSONObject project = new JSONObject();
            project.put(keys[2], DBProject.SelectProjectFull(conn, pid));
            project.put(keys[3], DBMember.SelectProjectMember(conn, pid));
            project.put(keys[4], DBSchedule.SelectProjectSchedule(conn, pid));
            projects.add(project);
        }
        result.put(keys[0], new JSONArray(projectInfo));
        result.put(keys[1], new JSONArray(projects));
    }

    public String getResult(){
        return result.toString();
    }

    public void endSearch() throws SQLException {
        conn.close();
    }

}

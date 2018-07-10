package Test;

import DBTools.DBSummary;
import Service.Summary;
import org.junit.Test;

import java.sql.SQLException;

import static org.junit.Assert.*;

public class SummaryTest {

    @Test
    public void test(){
        try {
            Summary summary = new Summary();
            summary.addEle("team", DBSummary.getTeam());
            summary.addEle("people", DBSummary.getPeople());
            summary.addEle("place", DBSummary.getPlace());
            summary.addEle("day", DBSummary.getDay());
            System.out.println(summary.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
package Test;

import Model.SearchParams;
import Service.Search;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SearchTest {

    @Test
    public void testSearch(){
        Map<String, Object> params = new HashMap<>();
//        params.put(SearchParams.DEPARTMENT.getKey(), 6);
//        params.put(SearchParams.TEAMNAME.getKey(),"天");
//        params.put(SearchParams.TEACHER.getKey(), "吴塔");
//        params.put(SearchParams.DATE.getKey(), "2018-07-07");
//        params.put(SearchParams.STUDENTNO.getKey(), "3546");
//        params.put(SearchParams.STUDENTNAME.getKey(), "李艳");
//        params.put(SearchParams.PROVINCE.getKey(), "山西省");
//        params.put(SearchParams.CITY.getKey(), "海淀区");
        try {
            Search search = new Search(params);
            String result = search.getResult();
            System.out.println(result);
            search.endSearch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
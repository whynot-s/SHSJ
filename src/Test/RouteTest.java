package Test;

import Service.Route;
import org.junit.Test;

public class RouteTest {

    @Test
    public void testRoute(){
        String json = Route.getRouteJSON("2018-07-06", "2018-07-14");
        System.out.println(json);
    }

}
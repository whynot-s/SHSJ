package Test;

import Service.Route;
import org.junit.Test;

public class RouteTest {

    @Test
    public void testRoute(){
        Route.getRouteJSON("2018-07-07", "2018-07-14");
    }

}
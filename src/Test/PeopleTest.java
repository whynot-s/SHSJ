package Test;

import Service.People;
import org.junit.Test;

public class PeopleTest {

    @Test
    public void testPeopleMap(){
        String json = People.getPeopleMap("2018-07-08");
        System.out.println(json);
    }

}
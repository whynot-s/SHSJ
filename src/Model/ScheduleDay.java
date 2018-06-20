package Model;

public class ScheduleDay {

    private String SchDate;
    private String Province;
    private String City;

    public ScheduleDay(String date, String province, String city){
        SchDate = date;
        Province = province;
        City = city;
    }

    public String toString(){
        return String.format("%s:%s-%s", SchDate, Province, City);
    }

    public String toInserSQL(){
        return String.format("\'%s\',\'%s\',\'%s\'", SchDate, Province, City);
    }

}

package Model;

public class Schedule {

    private String praDate;
    private String praProvince;
    private String praCity;

    public Schedule(String date, String province, String city){
        praDate = date;
        praProvince = province;
        praCity = city;
    }

    public String getPraCity() {
        return praCity;
    }

    public String getPraDate() {
        return praDate;
    }

    public String getPraProvince() {
        return praProvince;
    }

}

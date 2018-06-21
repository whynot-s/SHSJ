package Model;

public class City {

    private String province;
    private String city;
    private String Abbreviation;
    private int center;
    private double latitude;
    private double longtitude;
    private boolean[] modified;

    public City(String pro, String cit){
        province = pro;
        city = cit;
        modified = new boolean[2];
        modified[0] = false;
        modified[1] = false;
    }

    public int addGeo(String name, double lat, double lon){
        if(city.startsWith(name)){
            latitude = lat;
            longtitude = lon;
            modified[0] = true;
            center = 0;
            return 0;
        }else {
            return -1;
        }
    }

    public int addAbbr(String pro, String cit, String abbr){
        if(pro.equals(province) && cit.equals(city)){
            center = 1;
            Abbreviation = abbr;
            modified[1] = true;
            return 0;
        }else if(pro.equals(province)){
            Abbreviation = abbr;
            modified[1] = true;
            return 1;
        }else
            return -1;
    }

    public String toUpdateSQL(){
        String condition = "";
        if(modified[0])
            condition += String.format("latitude = %f, longtitude = %f, IsCenter = %d", latitude, longtitude, center);
        if(modified[1]){
            if(modified[0])condition += ", ";
            condition += String.format("Abbreviation = \'%s\'", Abbreviation);
        }
        return condition;
    }

    public String getProv(){
        return province;
    }

    public String getCity(){
        return city;
    }

    public boolean changed(){
        return modified[0] || modified[1];
    }

}

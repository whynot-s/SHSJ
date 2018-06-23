package Model;

public enum SearchParams {

    DEPARTMENT("dep"),
    TEAMNAME("tname"),
    TEACHER("teacher"),
    DATE("date"),
    STUDENTNO("stno"),
    STUDENTNAME("stname");

    private String abbr;

    SearchParams(String abbr){
        this.abbr = abbr;
    }

    public String getKey(){
        return this.abbr;
    }

}

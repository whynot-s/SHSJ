package Model;

public enum SearchParams {

    DEPARTMENT("dep", true),
    TEAMNAME("tname", false),
    TEACHER("teacher", false),
    DATE("date", false),
    STUDENTNO("stno", false),
    STUDENTNAME("stname", false),
    PROVINCE("pro", false),
    CITY("cit", false),
    UID("tno",false);

    private String abbr;
    private boolean isInt;

    SearchParams(String abbr, boolean isInt){
        this.abbr = abbr;
        this.isInt = isInt;
    }

    public String getKey(){
        return this.abbr;
    }

    public boolean IsInt(){
        return this.isInt;
    }

}

package Model;

public enum RegisterParams {

    USERNAME("username", false),
    PWD("pwd", false),
    INVITATION("inv", false);

    private String abbr;
    private boolean isInt;

    RegisterParams(String abbr, boolean isInt){
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

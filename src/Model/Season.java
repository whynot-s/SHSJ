package Model;

import java.util.Calendar;

public class Season {

    public static String CurrentSeason(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        if(month <= 4 || month >= 12) return String.format("%d寒假", year);
        else return String.format("%d暑假", year);
    }

    public static boolean IsWinter(){
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH) + 1;
        if(month <= 4 || month >= 12) return true;
        else return false;
    }

}

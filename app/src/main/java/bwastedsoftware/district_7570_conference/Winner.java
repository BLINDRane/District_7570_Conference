package bwastedsoftware.district_7570_conference;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by wyatt_ofn2t9u on 11/28/2017.
 */

public class Winner
{
    private String userName;
    private String completionTime;

    Winner(){

    }


    Winner(String userName, String completionTime){
        this.userName = userName;
        this.completionTime = completionTime;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getCompletionTime(){
        return completionTime;
    }

    public void setCompletionTime(String completionTime){
        this.completionTime = completionTime;
    }

    public Calendar getDateFormated() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
           Calendar calendar = Calendar.getInstance();
            calendar.setTime(format.parse(this.completionTime));
            return calendar;

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}

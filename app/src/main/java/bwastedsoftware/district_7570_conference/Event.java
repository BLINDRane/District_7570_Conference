package bwastedsoftware.district_7570_conference;

import android.provider.CalendarContract;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static java.text.DateFormat.LONG;
import static java.text.DateFormat.SHORT;

/**
 * Event class handling all information regarding an event.
 * <p>
 * TO DO: Create Class handling time and date (for formatting purposes).
 * Created by Boston on 10/16/2017.
 */

@IgnoreExtraProperties
public class Event {
    private String title;
    private String location;
    private String date;
    private String time;
    private String details;
    private ArrayList<Speaker> speakers;

    Event() {

    }

    Event(String title, String location, String date, String time, String details, Speaker speaker) {
        this(title, location, date, time, details);
        this.addSpeaker(speaker);
    }

    Event(String title, String location, String date, String time, String details) {
        this.title = title;
        this.location = location;
        this.date = date;
        this.time = time;
        this.details = details;
        speakers = new ArrayList<>();
    }

    public void addSpeaker(Speaker speaker) {
        speakers.add(speaker);
    }

    public int getNumberOfSpeakers() {
        return speakers.size();
    }

    public ArrayList<Speaker> getSpeakers() {
        return speakers;
    }

    public Speaker getSpeakerByName(String name) {
        for (int i = 0; i <= getNumberOfSpeakers(); i++) {
            if (speakers.get(i).getName().equals(name)) {
                return speakers.get(i);
            }
        }
        return null;
    }

    public Speaker getSpeaker() //returns first speaker
    {
        return speakers.get(0);
    }

    public String getSpeakerString() {
        String list = null;
        if (speakers.size() > 1) {
            for (int i = 0; i < speakers.size(); i++) {
                list = list + speakers.get(i) + ", ";
            }
            return list;
        } else {
            return getSpeaker().getName();
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("location", location);
        result.put("date", date);
        result.put("time", time);
        result.put("speakers", speakers);
        result.put("details", details);
        //result.put("speakers", getSpeakerString());

        return result;
    }

    public String getDetails() {
        return details;
    }

    public Calendar getCalendarStartTime() {
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy hh:mm a", Locale.ENGLISH);
        try {
            Date date = format.parse(this.date + " " + this.getStartTime());
           // Log.v("Start date is", date.toString());
           // Log.v("Start time is", this.getStartTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getStartTime() {
        String tim = this.time;
        String[] out = tim.split(" to");

        String res = out[0];

        return res.replace("From ", "");
    }

    public String getEndTime() {
        String tim = this.time;
        String[] out = tim.split(" to");

        String res = out[1];
        //Log.v("END TIME IS",   res);
        return res;
    }


    public boolean isCurrent() {
            long compare = getCalendarEndTime().getTimeInMillis() - getCalendarRightNow().getTimeInMillis();
       // Log.v("LOOK HERE", "compare = " + compare + " " + "Duration is: " + getDuration() + "="  + (compare < getDuration()));
            if(compare > 0 && compare < getDuration()){
                    return true;
            }
        return false;
    }

    public boolean isOver() {
        if(!isCurrent()) {
            long compare = getCalendarRightNow().compareTo(getCalendarEndTime());
           // Log.v("LOOK HERE", "Compare = " + compare);
            if (compare == 0) {
                return false;
            } else if (compare < 0) {
                return false;
            } else if (compare > 0) {
                return true;
            }
        }
        return false;
    }

    public Calendar getCalendarEndTime() {
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy hh:mm a", Locale.ENGLISH);
        try {
            Date date = format.parse(this.date + " " + this.getEndTime());
            //Log.v("END Date IS: ", date.toString());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Calendar getCalendarRightNow() {
        Calendar rightNow = Calendar.getInstance();
        return rightNow;
    }


    public long getDuration(){
       long duration = getCalendarEndTime().getTimeInMillis() - getCalendarStartTime().getTimeInMillis();
        return duration;
    }


}
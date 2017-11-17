package bwastedsoftware.district_7570_conference;

import android.provider.CalendarContract;

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
public class Event
{
    private String title;
    private String location;
    private String date;
    private String time;
    private String details;
    private ArrayList<Speaker> speakers;

    Event()
    {

    }

    Event(String title, String location, String date, String time, String details, Speaker speaker)
    {
        this(title, location, date, time, details);
        this.addSpeaker(speaker);
    }

    Event(String title, String location, String date, String time, String details)
    {
        this.title = title;
        this.location = location;
        this.date = date;
        this.time = time;
        this.details = details;
        speakers = new ArrayList<>();
    }

    public void addSpeaker(Speaker speaker)
    {
        speakers.add(speaker);
    }

    public int getNumberOfSpeakers()
    {
        return speakers.size();
    }

    public ArrayList<Speaker> getSpeakers()
    {
        return speakers;
    }

    public Speaker getSpeakerByName(String name)
    {
        for(int i = 0; i <= getNumberOfSpeakers(); i++)
        {
            if(speakers.get(i).getName().equals(name))
            {
                return speakers.get(i);
            }
        }
        return null;
    }

    public Speaker getSpeaker() //returns first speaker
    {
        return speakers.get(0);
    }

    public String getSpeakerString()
    {
        String list = null;
        if(speakers.size() > 1)
        {
            for (int i = 0; i < speakers.size(); i++)
            {
                list = list + speakers.get(i) + ", ";
            }
            return list;
        }
        else
        {
            return getSpeaker().getName();
        }
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
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

    public String getDetails()
    {
        return details;
    }

    public Calendar getCalendarObject()
    {
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy HH:mm a", Locale.ENGLISH);
        try {
            Date date = format.parse(this.date + " " + this.getStartTime());
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

        return res;
    }

    public boolean isCurrent(){
        try
        {
          String eDate = this.getDate();
          String eStartTime = this.getStartTime();
          String eEndTime = this.getEndTime();

            SimpleDateFormat formatter = new SimpleDateFormat("MMMM DD, yyyy HH:MM a");
            Date eventStart = formatter.parse(eDate + " " + eStartTime);
            Date eventEnd = formatter.parse(eDate + " " + eEndTime);

            if (eventEnd.after(new Date()) && eventStart.before(new Date())) {
                return true;
            }

        } catch (ParseException e)
        {
            e.printStackTrace();
        }



        return false;
    }

    public boolean isOver(){
        try
        {
            String eDate = this.getDate();
            String eEndTime = this.getEndTime();
            SimpleDateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy HH:MM a");
            Date eventEnd = formatter.parse(eDate + " " + eEndTime);

            if (eventEnd.before(new Date())){
                return true;
            }
        } catch (ParseException e)
        {
            e.printStackTrace();
        }

        return false;
    }
}
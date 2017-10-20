package bwastedsoftware.district_7570_conference;

import com.google.firebase.database.Exclude;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Event class handling all information regarding an event.
 * <p>
 * TO DO: Create Class handling time and date (for formatting purposes).
 * Created by Boston on 10/16/2017.
 */

public class Event
{
    private String title;
    private String location;
    private String date;
    private String time;
    private String details;
    private ArrayList<Speaker> speakers;

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
        result.put("speakers", getSpeakerString());

        return result;
    }
}

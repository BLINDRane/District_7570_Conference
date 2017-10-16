package bwastedsoftware.district_7570_conference;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
    private ArrayList<Speaker> speakers;

    Event(String title, String location, String date, String time, Speaker speaker)
    {
        this(title, location, date, time);
        this.addSpeaker(speaker);
    }

    Event(String title, String location, String date, String time)
    {
        this.title = title;
        this.location = location;
        this.date = date;
        this.time = time;
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
}

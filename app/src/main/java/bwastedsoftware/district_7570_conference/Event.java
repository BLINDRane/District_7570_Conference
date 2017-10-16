package bwastedsoftware.district_7570_conference;

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
    private Speaker[] speakers;
    private int pos;

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
        pos = 0;
    }

    public void addSpeaker(Speaker speaker)
    {
        speakers[pos] = speaker;
        pos++;
    }

    public int getNumberOfSpeakers()
    {
        return speakers.length;
    }

    public Speaker[] getSpeakers()
    {
        return speakers;
    }

    public Speaker getSpeakerByName(String name)
    {
        for(int i = 0; i <= getNumberOfSpeakers(); i++)
        {
            if(speakers[i].getName().equals(name))
            {
                return speakers[i];
            }
        }
        return null;
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

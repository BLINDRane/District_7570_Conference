package bwastedsoftware.district_7570_conference;

import android.widget.ImageView;

/**
 * Class to handle speakers.
 * Created by Boston on 10/16/2017.
 */

public class Speaker
{
    private String name;
    private int photo; //need a better way to manage photos of speakers

    Speaker()
    {

    }

    Speaker(String name, int photo)
    {
        this.name = name;
        this.photo = photo;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getPhoto()
    {
        return photo;
    }

    public void setPhoto(int photo)
    {
        this.photo = photo;
    }
}

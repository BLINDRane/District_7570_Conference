package bwastedsoftware.district_7570_conference;

/**
 * Class to handle speakers.
 * Created by Boston on 10/16/2017.
 */

public class Speaker
{
    private String name;
    private String photo; //need a better way to manage photos of speakers

    Speaker(String name, String photo)
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

    public String getPhoto()
    {
        return photo;
    }

    public void setPhoto(String photo)
    {
        this.photo = photo;
    }
}

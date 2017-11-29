package bwastedsoftware.district_7570_conference;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to handle speakers.
 * Created by Boston on 10/16/2017.
 */

public class Speaker
{
    private String name;
    private String title;

    public String getWebpage()
    {
        return webpage;
    }

    public void setWebpage(String webpage)
    {
        this.webpage = webpage;
    }

    private String webpage;
    //private ImageView photo; //need a better way to manage photos of speakers
    private String photoURL;

    Speaker()
    {

    }

    //Return a full speaker
    Speaker(String name, String title, String webpage, String photoURL)
    {
        this.name = name;
        this.title = title;
        this.webpage = webpage;
        this.photoURL = photoURL;

    }

    //Return a speaker for event purposes (name and photo only) //why do we even have this year what
    //Speaker(String name, String photoURL){
    //    this.name = name;
    //    //this.photo = photo;
    //}
//
    public String getName() {return name;}

    public void setName(String name)
    {
        this.name = name;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }


    public void setPhotoURL(String photo)
    {
        this.photoURL = photo;
    }

    //public void setPhoto(ImageView photo)
    //{
    //    this.photo = photo;
    //}

    public String getPhotoURL()
    {
        return photoURL;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("title", title);
        result.put("webpage", webpage);
        result.put("photoURL", getPhotoURL() );

        return result;
    }

}

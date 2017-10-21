package bwastedsoftware.district_7570_conference;

import android.net.Uri;
import android.widget.ImageView;

import com.google.firebase.database.Exclude;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Attributes;

/**
 * Class to handle speakers.
 * Created by Boston on 10/16/2017.
 */

public class Speaker
{
    private String name;
    private String bio;
    private String photo; //need a better way to manage photos of speakers

    Speaker()
    {

    }
    //Return a full speaker
    Speaker(String name, String bio, String photo)
    {
        this.name = name;
        this.bio = bio;
        this.photo = photo;

    }
    //Return a speaker for event purposes (name and photo only)
    Speaker(String name, String photo){
        this.name = name;
        this.photo = photo;
    }

    public String getName() {return name;}

    public void setName(String name)
    {
        this.name = name;
    }

    public String getBio()
    {
        return bio;
    }

    public void setBio(String bio)
    {
        this.bio = bio;
    }

    public String getPhoto()
    {
        return photo;
    }

    public void setPhoto(String photo)
    {
        this.photo = photo;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("Name", name);
        result.put("Bio", bio);
        result.put("Image", photo );

        return result;
    }
}

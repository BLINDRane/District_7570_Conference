package bwastedsoftware.district_7570_conference;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.firebase.database.Exclude;
import com.squareup.picasso.Picasso;

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
    //private ImageView photo; //need a better way to manage photos of speakers
    private String photoURL;

    Speaker()
    {

    }

    //Return a full speaker
    Speaker(String name, String bio, String photoURL)
    {
        this.name = name;
        this.bio = bio;
        //this.photo = photo;
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

    public String getBio()
    {
        return bio;
    }

    public void setBio(String bio)
    {
        this.bio = bio;
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
        result.put("bio", bio);
        result.put("photo", getPhotoURL() );

        return result;
    }
}

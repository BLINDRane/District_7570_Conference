package bwastedsoftware.district_7570_conference;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * This class builds a LOCAL database on the actual device, for use offline.
 */

public class UserLocalStore {



    public static final String SP_Name = "userDetails";
    SharedPreferences userLocalDatabase;



    //creates local database (on device)
    public UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_Name, 0);
    }




    //Stores basic user info
    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("first name", user.Fname);
        spEditor.putString("last name", user.Lname);
        spEditor.putString("email", user.email);
        spEditor.putString("password", user.password);
        spEditor.commit();

        //TODO: Add the array lists of events to this stored list.
    }




    //Method to retrieve information about the current user
    public User getLoggedInUser(){

        String Fname = userLocalDatabase.getString("first name","");
        String Lname = userLocalDatabase.getString("last name", "");
        String email = userLocalDatabase.getString("email","");
        String password = userLocalDatabase.getString("password","");

        User storedUser = new User(Fname, Lname,  email, password);

        return storedUser;
    }




    //A boolean to set if a user is logged in, or logged out.
    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("LoggedIn", loggedIn);
        spEditor.commit();
    }



    //This tells if a user is logged in. This is called after it is set using the prev. method
    public boolean getUserLoggedIn(){
        if(userLocalDatabase.getBoolean("LoggedIn", false)== true){
            return true;
        }else {
            return false;
        }
    }




    //Clears user data. (duh)
    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }
}

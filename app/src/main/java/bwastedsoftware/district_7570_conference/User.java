package bwastedsoftware.district_7570_conference;


import java.util.ArrayList;

/**
 * This class will define what a user is, giving them all necessary attributes.
 */

public class User {
    //the following strings make up a user

    String Fname, Lname, email, password;


    /* a user object must also have a list of events they have RSVP'd to, as well as a list of
     events that they have attended. The RSVP list will enable us to show only events they have
     RSVP'd to, as well as the inverse. The second list will enable us to ask for a review score
     after an event has ended.
      */
    //TODO: add two array lists to USER class so that the above comment can be fulfilled.

    public User(String Fname, String Lname, String email, String password) {

      /*here we extract the data that will be passed in upon creation of a user (the data to be
        received is listed in the parentheses above) and then apply it to the User object that this
        particular method creates.
       */
        this.Fname = Fname;
        this.Lname = Lname;
        this.email = email;
        this.password = password;

    }

    /*this is a method to create a user without getting a name, for example, someone tries an
    email and password before going to the register page. They will instead be added to the database
    as a user with a name that is empty
     */

    public User(String email, String password) {

        this.Fname = "";
        this.Lname = "";
        this.email = email;
        this.password = password;
    }


}

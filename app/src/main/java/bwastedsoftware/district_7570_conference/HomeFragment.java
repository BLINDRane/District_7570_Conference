package bwastedsoftware.district_7570_conference;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class HomeFragment extends Fragment implements View.OnClickListener{
    Button btnCurrent, btnUpcoming;
    TextView welcomeText;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference userEvents;
    private String user_id;
    private ArrayList<Event> eventList;
    private ArrayList<Event> tempEvents;
    boolean currentFound;
    boolean upcomingFound;

    public HomeFragment(){
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Link the layout pieces with their Java counterparts
        btnCurrent = (Button) view.findViewById(R.id.current_button);
        btnUpcoming = (Button) view.findViewById(R.id.upcoming_button);
        welcomeText = (TextView) view.findViewById(R.id.welcomeTxtView);
        //Give abilities to the layout parts
        btnUpcoming.setOnClickListener(this);
        btnCurrent.setOnClickListener(this);

        tempEvents = new ArrayList<>();
        eventList = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        userEvents = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("userEvents");
        personilizeWelcomeText();
        getUserEvents();

        return view;
    }

    @Override
    public void onClick(View v) {
if(v.getId() == R.id.current_button){

    getCurrentEvent(eventList);

} else if(v.getId() == R.id.upcoming_button){

    getUpcomingEvent(eventList);

}
    }

    //This method changes the welcome text, giving it a personal (and professional) touch.
    private void personilizeWelcomeText(){
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String current_user = dataSnapshot.child(user_id).child("Fname").getValue(String.class);
                welcomeText.setText("Welcome to District Conference, " + current_user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void sendNotification(){

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext());

//Create the intent that’ll fire when the user taps the notification//

        Intent intent = new Intent(getContext(), HomePage.class);
        intent.putExtra("From", "notifyFrag");
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

        mBuilder.setContentIntent(pendingIntent);

        mBuilder.setSmallIcon(R.drawable.homestead_fall);
        mBuilder.setContentTitle("Rate Event");
        mBuilder.setContentText("Please take a moment to rate that last event!");

        NotificationManager mNotificationManager =

                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());


    }

    private void getUserEvents(){
        userEvents.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childrenSnapShot : dataSnapshot.getChildren()) {
                    Event event = childrenSnapShot.getValue(Event.class);
                    tempEvents.add(new Event(event.getTitle(), event.getLocation(), event.getDate(), event.getTime(), event.getDetails(), event.getSpeaker()));
                    //Log.w("GETTING CARDS", "value is" + event.getDate() + event.getLocation() + childrenSnapShot.getKey());
                }
                //Log.d("FIREBASE", "Value is: " + post);
                addEvents(tempEvents);


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addEvents(ArrayList<Event> events){
        eventList.addAll(events);
    }

    private void getUpcomingEvent(ArrayList<Event> events){
        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return e1.getCalendarStartTime().compareTo(e2.getCalendarStartTime());
            }
        });
        
       for(int i = 0; i<events.size(); i++){
           Log.v("EVENT AT I", events.get(i).getTitle());
           if(!events.get(i).isOver() && !events.get(i).isCurrent()){
               loadEventDetails(events.get(i));
               upcomingFound = true;
               break;
           }
       }
       if(!upcomingFound){
           Toast.makeText(getContext(), "No upcoming event found.", Toast.LENGTH_LONG).show();
       }
    }

    private void getCurrentEvent(ArrayList<Event> events){
        for(int i = 0; i < events.size(); i++){
            if(events.get(i).isCurrent()){
                loadEventDetails(events.get(i));
                currentFound = true;
                break;
            }
        }
       if(!currentFound) {
            Toast.makeText(getContext(), "No current event found.", Toast.LENGTH_LONG).show();
        }
    }

    public void loadEventDetails(Event event) {
        FragmentTransaction t = this.getFragmentManager().beginTransaction();
        t.addToBackStack("Event");
        EventFragment mFrag = new EventFragment();
        mFrag.passEvent(getActivity(), event);
        t.replace(R.id.main_container, mFrag);
        t.commit();
    }
    //this will enable using the back button to pop the stack, which will go to previous fragment instead of the login screen.
    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    mAuth.signOut();
                                    startActivity(new Intent(getContext(), Login.class));
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Are you sure you want to log out?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                    return true;

                }

                return false;
            }
        });
    }

}
package bwastedsoftware.district_7570_conference;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
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
    private  ArrayList<Event> eventList;
    private ArrayList<Event> tempEvents;
    boolean currentFound;
    boolean upcomingFound;
    boolean isAdmin;

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



        ImageView picture = (ImageView) view.findViewById(R.id.homestead_view);
        Picasso.with(getActivity()).load(R.drawable.homepageimage).placeholder(R.drawable.homestead_fall).fit().centerCrop().into(picture);

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


    private void getUserEvents(){
        userEvents.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childrenSnapShot : dataSnapshot.getChildren()) {
                    Event event = childrenSnapShot.getValue(Event.class);
                    tempEvents.add(new Event(event.getTitle(), event.getLocation(), event.getDate(), event.getTime(), event.getDetails(), event.getSpeaker(), 0 , 0));
                }

                addEvents(tempEvents);
                getRateableEvents(tempEvents);

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

    private void getRateableEvents(ArrayList<Event> events) {
        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event e1, Event e2) {
                return e1.getCalendarStartTime().compareTo(e2.getCalendarStartTime());
            }
        });
        ArrayList<Event> rateable = new ArrayList<>();
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).isOver()) {
                rateable.add(events.get(i));
            }
        }
                if (rateable.size() > 0) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    Bundle bundle = new Bundle();
                                    ScheduleFragment Schedule = new ScheduleFragment();
                                    bundle.putBoolean("IS_MY_SCHEDULE", true);
                                    bundle.putBoolean("IS_ADMIN", isAdmin);
                                    Schedule.setArguments(bundle);
                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.addToBackStack("Schedule");
                                    fragmentTransaction.replace(R.id.main_container, Schedule);
                                    fragmentTransaction.commit();
                                    ((HomePage) getActivity()).getSupportActionBar().setTitle("Schedule");
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("You have " + rateable.size() + " " + "event(s) that need your feedback. Would you like to rate them now?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                }
            }


    public void loadEventDetails(Event event) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("IS_MINE", true);
        FragmentTransaction t = this.getFragmentManager().beginTransaction();
        t.addToBackStack("Event");
        EventFragment mFrag = new EventFragment();
        mFrag.setArguments(bundle);
        mFrag.passEvent(getActivity(), event);
        t.replace(R.id.main_container, mFrag);
        ((HomePage)getActivity()).getSupportActionBar().setTitle("Event");
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
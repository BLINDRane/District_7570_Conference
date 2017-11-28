package bwastedsoftware.district_7570_conference;


import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;



public class HomePage extends AppCompatActivity {

    //Initializes the buttons that will be used on the home page

    Toolbar Toolbar;
    //UserLocalStore userLocalStore;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FragmentTransaction fragmentTransaction;
    NavigationView navView;
    FirebaseAuth mAuth;
    String user_id;
    Bundle inBundle;
    Boolean isAdmin;
    MenuItem createEvent;
    MenuItem createSpeaker;

    //Sets up the activity screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        inBundle = getIntent().getExtras();
        isAdmin = inBundle.getBoolean("IS_ADMIN");
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        setContentView(R.layout.activity_homepage);
        Toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(Toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Initialize that dank action bar
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, Toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navView = (NavigationView) findViewById(R.id.navigation_view);

        createEvent =  navView.getMenu().findItem(R.id.create_event_id);
        createSpeaker = navView.getMenu().findItem(R.id.create_speaker_id);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, new HomeFragment());
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Home");


        final Bundle bundle = new Bundle();
        final ScheduleFragment Schedule = new ScheduleFragment();
        final ScheduleFragment mySchedule = new ScheduleFragment();
        final Create_EventFragment createEventFrag = new Create_EventFragment();
        final SpeakerListFragment speakerList = new SpeakerListFragment();
        final createSpeakerFragment speakerCreate = new createSpeakerFragment();
        speakerCreate.setArguments(bundle);
        speakerList.setArguments(bundle);
        mySchedule.setArguments(bundle);
        Schedule.setArguments(bundle);
        createEventFrag.setArguments(bundle);

        final AboutPageFragment aboutPage = new AboutPageFragment();
        aboutPage.setArguments(bundle);


        if(isAdmin){
            //createEvent.setVisible(true);
            //createSpeaker.setVisible(true);
            //Toast.makeText(HomePage.this, "Admin Detected", Toast.LENGTH_LONG).show();
        }

        //This is like onCreate() but if the activity was called from a push notification
        String type = getIntent().getStringExtra("From");
        if (type != null) {
            switch (type) {
                case "notifyFrag":

                    Bundle bundle2 = new Bundle();
                    ScheduleFragment mySchedule2 = new ScheduleFragment();
                    mySchedule2.setArguments(bundle2);
                    bundle2.putBoolean("IS_MY_SCHEDULE", true);
                    bundle2.putBoolean("IS_ADMIN",isAdmin);
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack("My Schedule");
                    fragmentTransaction.replace(R.id.main_container, mySchedule2);
                    fragmentTransaction.commit();
                    getSupportActionBar().setTitle("My Schedule");
                    navView.setCheckedItem(R.id.my_schedule_id);
                    drawerLayout.closeDrawers();
                    break;
            }
        }

        //userLocalStore = new UserLocalStore(this);

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_id:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.addToBackStack("Home");
                        fragmentTransaction.replace(R.id.main_container, new HomeFragment());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Home");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.schedule_id:
                        bundle.putBoolean("IS_MY_SCHEDULE", false);
                        bundle.putBoolean("IS_ADMIN",isAdmin);
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.addToBackStack("Schedule");
                        fragmentTransaction.replace(R.id.main_container, Schedule);
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Schedule");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.speakers_id:
                        bundle.putBoolean("IS_ADMIN", isAdmin);
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.addToBackStack("Speakers");
                        fragmentTransaction.replace(R.id.main_container, speakerList);
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Speakers");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.my_schedule_id:
                        bundle.putBoolean("IS_MY_SCHEDULE", true);
                        bundle.putBoolean("IS_ADMIN",isAdmin);
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.addToBackStack("My Schedule");
                        fragmentTransaction.replace(R.id.main_container, mySchedule);
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("My Schedule");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.create_event_id:
                        bundle.putBoolean("IS_ADMIN", isAdmin);
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.addToBackStack("Event Creation");
                        fragmentTransaction.replace(R.id.main_container, createEventFrag);
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Event Creation");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.create_speaker_id:
                        bundle.putBoolean("IS_ADMIN", isAdmin);
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.addToBackStack("Speaker Creation");
                        fragmentTransaction.replace(R.id.main_container, speakerCreate);
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Speaker Creation");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.scav_id:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.addToBackStack("Scavenger Hunt");
                        fragmentTransaction.replace(R.id.main_container, new scavengerHunt());
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("Scavenger Hunt");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.about_id:
                        bundle.putBoolean("IS_ADMIN", isAdmin);
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.addToBackStack("About Page");
                        fragmentTransaction.replace(R.id.main_container, aboutPage);
                        fragmentTransaction.commit();
                        getSupportActionBar().setTitle("About");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;
                }
                return false;
            }
        });


    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }



}


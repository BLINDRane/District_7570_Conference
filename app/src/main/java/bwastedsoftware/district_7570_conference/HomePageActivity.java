package bwastedsoftware.district_7570_conference;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class HomePageActivity extends AppCompatActivity {

    //Initializes the buttons that will be used on the home page

    Toolbar Toolbar;
    //UserLocalStore userLocalStore;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FragmentTransaction fragmentTransaction;
    NavigationView navView;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    String user_id;
    Bundle inBundle;
    Boolean isAdmin;
    MenuItem scavengerHunt;
    String ScavDate;
    Calendar endHunt = Calendar.getInstance();
    Calendar now = Calendar.getInstance();
    String[] permissions = new String[]{
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };;

    //Sets up the activity screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        inBundle = getIntent().getExtras();
        isAdmin = inBundle.getBoolean("IS_ADMIN");
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("ScavDate");
        user_id = mAuth.getCurrentUser().getUid();
        setContentView(R.layout.activity_homepage);
        Toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(Toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        //Initialize that dank action bar
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, Toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        navView = (NavigationView) findViewById(R.id.navigation_view);

        scavengerHunt = navView.getMenu().findItem(R.id.scav_id);

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, new HomeFragment());
        fragmentTransaction.commit();
        checkPermissions();
        getSupportActionBar().setTitle("Home");

        //set the date and time when the scavenger hunt ends
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ScavDate = dataSnapshot.getValue().toString();

                DateFormat format = new SimpleDateFormat("MMMM d, yyyy hh:mm a", Locale.ENGLISH);
                try {
                    Date date = format.parse(ScavDate);

                    endHunt.setTime(date);


                } catch (ParseException e) {
                    e.printStackTrace();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final Bundle bundle = new Bundle();
        final ScavengerHuntFragment scav = new ScavengerHuntFragment();
        final ScheduleFragment Schedule = new ScheduleFragment();
        final ScheduleFragment mySchedule = new ScheduleFragment();
        final CreateEventFragment createEventFrag = new CreateEventFragment();
        final SpeakerListFragment speakerList = new SpeakerListFragment();
        final CreateSpeakerFragment speakerCreate = new CreateSpeakerFragment();
        speakerCreate.setArguments(bundle);
        speakerList.setArguments(bundle);
        mySchedule.setArguments(bundle);
        Schedule.setArguments(bundle);
        createEventFrag.setArguments(bundle);

        final AboutPageFragment aboutPage = new AboutPageFragment();
        aboutPage.setArguments(bundle);



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
                        if(endHunt.compareTo(now) < 0) {
                            fragmentTransaction.replace(R.id.main_container, new WinnersCircleFragment());
                        } else {
                            fragmentTransaction.replace(R.id.main_container, scav);
                        }
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

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 100);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(HomePageActivity.this, "Thanks, all set.", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

}


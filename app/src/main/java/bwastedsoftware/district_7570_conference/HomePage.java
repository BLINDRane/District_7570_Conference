package bwastedsoftware.district_7570_conference;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;


public class HomePage extends AppCompatActivity {

    //Initializes the buttons that will be used on the home page

    Toolbar Toolbar;
    UserLocalStore userLocalStore;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    FragmentTransaction fragmentTransaction;
    NavigationView navView;
    Drawable drawable;


    //Sets up the activity screen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(Toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawable = ContextCompat.getDrawable(this, R.drawable.banner);
        //Initialize that dank action bar
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, Toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, new HomeFragment());
        fragmentTransaction.commit();
        getSupportActionBar().setTitle("Home");

        userLocalStore = new UserLocalStore(this);
        navView = (NavigationView) findViewById(R.id.navigation_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home_id:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new HomeFragment());
                        fragmentTransaction.commit();
                        Toolbar.setBackgroundColor((Color.parseColor("#303F9F")));
                        getSupportActionBar().setTitle("Home");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.schedule_id:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new ScheduleFragment());
                        fragmentTransaction.commit();
                        Toolbar.setBackground(drawable);
                        getSupportActionBar().setTitle("Schedule");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.speakers_id:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new SpeakerListFragment());
                        fragmentTransaction.commit();
                        Toolbar.setBackground(drawable);
                        getSupportActionBar().setTitle("Speakers, Leaders, and Sponsors");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.map_id:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new MapFragment());
                        fragmentTransaction.commit();
                        Toolbar.setBackground(drawable);
                        getSupportActionBar().setTitle("Map");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.event_id:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new EventFragment());
                        fragmentTransaction.commit();
                        Toolbar.setBackground(drawable);
                        getSupportActionBar().setTitle("Events");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.create_event_id:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new Create_EventFragment());
                        fragmentTransaction.commit();
                        Toolbar.setBackground(drawable);
                        getSupportActionBar().setTitle("Event Creation");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.create_speaker_id:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new createSpeakerFragment());
                        fragmentTransaction.commit();
                        Toolbar.setBackground(drawable);
                        getSupportActionBar().setTitle("Speaker Creation");
                        item.setChecked(true);
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.forum_id:
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.main_container, new ForumFragment());
                        fragmentTransaction.commit();
                        Toolbar.setBackground(drawable);
                        getSupportActionBar().setTitle("Forum");
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

    //Checks that a user has been authenticated, then sets up the welcome texts.
    protected void onStart() {
        super.onStart();

        if (authenticate() == true) {
            customizeWelcomeText();
        }
    }

    //Will return the value of the boolean in the local database that says a user is logged in
    private boolean authenticate() {
        return userLocalStore.getUserLoggedIn();
    }


    /*This statement will add a little flair to the welcome statement, personalizing it,
 provided the user has given a name.
  */
    private void customizeWelcomeText() {
        User user = userLocalStore.getLoggedInUser();
    }

}

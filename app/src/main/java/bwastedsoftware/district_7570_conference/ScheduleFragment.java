package bwastedsoftware.district_7570_conference;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

import static android.content.ContentValues.TAG;


public class ScheduleFragment extends Fragment
{


    public ScheduleFragment()
    {
        // Required empty public constructor
    }

    View v;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<ArrayList<Event>> days;
    ViewPager viewPager;
    PagerAdapter adapter;
    Boolean isMine;
    Boolean isAdmin;
    FirebaseAuth mAuth;
    String user_id;
    DatabaseReference myRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_schedule, container, false);
        Bundle args = getArguments();
        isMine = args.getBoolean("IS_MY_SCHEDULE");
        isAdmin = args.getBoolean("IS_ADMIN");
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        myRef = mDatabase.getReference().child("Users").child(user_id).child("userEvents");

        if (isAdmin && !isMine)
        {
            FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab_createEvent);
            fab.setVisibility(View.VISIBLE);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    //go to create event fragment
                    final Bundle bundle = new Bundle();
                    final Create_EventFragment createEventFrag = new Create_EventFragment();
                    createEventFrag.setArguments(bundle);
                    bundle.putBoolean("IS_ADMIN", isAdmin);
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack("Event Creation");
                    fragmentTransaction.replace(R.id.main_container, createEventFrag);
                    fragmentTransaction.commit();
                    //v.findViewById(R.id.toolbar).setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.banner));
                    ((HomePage) getActivity()).getSupportActionBar().setTitle("Event Creation");
                }}
            );
        }

        initializeData();
        //initializeAdapters(rv);
        if (!isMine)
        {
            refreshData();
        } else
        {
            refreshMyData();
        }
        // Locate the ViewPager in viewpager_main.xml
        viewPager = (ViewPager) v.findViewById(R.id.pager);
        // Pass results to ViewPagerAdapter Class
        initializeAdapter();

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                // Refresh items
                if (!isMine)
                {
                    refreshData();
                } else
                {
                    refreshMyData();
                }
            }
        });

        viewPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                mSwipeRefreshLayout.setEnabled(false);
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_UP:
                        mSwipeRefreshLayout.setEnabled(true);
                        break;
                }
                return false;
            }
        });

        return v;
    }


    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference allRef = mDatabase.getReference().child("Events");


    private void initializeData()
    {
        days = new ArrayList<>();
    }

    private void initializeAdapter()
    {
        adapter = new ViewPagerAdapter(getActivity(), days, ScheduleFragment.this);
        // Binds the Adapter to the ViewPager
        viewPager.setAdapter(adapter);
    }

    private void refreshData()
    {
        //days(0).add(new Event("EVENT  1", "LOCATION", "DATE", "TIME", "DETAILS", new Speaker("Billy", "Bio", "http://www.munkurious.com/sharex/2017.10/ghanaTempleStainedGlass_100x.png")));
        //events.add(new Event("EVENT TITLE 2", "LOCATION", "DATE", "TIME", "DETAILS", new Speaker("Sue", "Bio", "Photo")));


        final ArrayList<Event> newevents = new ArrayList<>();

        // Read from the database
        allRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for (DataSnapshot childrenSnapShot : dataSnapshot.getChildren())
                {
                    Event event = childrenSnapShot.getValue(Event.class);
                    newevents.add(new Event(event.getTitle(), event.getLocation(), event.getDate(), event.getTime(), event.getDetails(), event.getSpeaker()));
                }
                addEvents(newevents);

            }

            @Override
            public void onCancelled(DatabaseError error)
            {
                // Failed to read value
                Log.w("FIREBASE", "Failed to read value.", error.toException());
            }
        });

    }

    private void refreshMyData()
    {
        final ArrayList<Event> newevents = new ArrayList<>();

        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                //Event = dataSnapshot.getValue(Post.class);
                //Post post = dataSnapshot.getValue(Post.class);
                for (DataSnapshot childrenSnapShot : dataSnapshot.getChildren())
                {
                    Event event = childrenSnapShot.getValue(Event.class);
                    newevents.add(new Event(event.getTitle(), event.getLocation(), event.getDate(), event.getTime(), event.getDetails(), event.getSpeaker()));
                    //Log.w("GETTING CARDS", "value is" + event.getDate() + event.getLocation() + childrenSnapShot.getKey());
                }
                //Log.d("FIREBASE", "Value is: " + post);
                addEvents(newevents);

            }

            @Override
            public void onCancelled(DatabaseError error)
            {
                // Failed to read value
                Log.w("FIREBASE", "Failed to read value.", error.toException());
            }
        });

    }

    private void addEvents(ArrayList<Event> newevents)
    {
        //events.addAll(newevents);
        //Log.w("PROBLEM HERE", "LIST #" + events.size());
        //newevents.add(new Event("EVENT TITLE 3", "LOCATION", "DATE", "TIME", "DETAILS", new Speaker("Aaron's Little Helper", "bio", "Photo")));

        days.clear();

        for (Event e : newevents)
        {
            //Log.w("LOOK HERE", "HEY TRYING AN EVENT");
            if (days.size() == 0) //make first day
            {
                ArrayList<Event> day2 = new ArrayList<>();
                day2.add(e);
                days.add(day2);
                //Log.w("LOOK HERE", "INIT DAY, MAKING NEW ARRAY LIST." + days.size());
            } else
            {
                boolean sorted = false;
                for (int i = 0; i < days.size(); i++) //check each day in the thing
                {
                    if (days.get(i) != null && getDateFromString(days.get(i).get(0).getDate()).compareTo(getDateFromString(e.getDate())) == 0)
                    {
                        days.get(i).add(e);
                        sorted = true;
                        break;
                        //Log.w("LOOK HERE", "SAME DAY, ADDED");
                    }
                }

                if (!sorted)
                {
                    ArrayList<Event> day2 = new ArrayList<>();
                    day2.add(e);
                    days.add(day2);
                }
            }
        }
        //Log.w("LOOK HERE", "Did Events! " + days.size() + days.get(0).size());

        sortEventsByDay();

        sortEventsByTime();
        onItemsLoadComplete();
    }

    void sortEventsByDay()
    {
        Collections.sort(days, new Comparator<ArrayList<Event>>()
        {
            @Override
            public int compare(ArrayList<Event> o1, ArrayList<Event> o2)
            {
                if (getDateFromString(o1.get(0).getDate()) != null && getDateFromString(o1.get(0).getDate()) != null)
                {
                    return getDateFromString(o1.get(0).getDate()).compareTo(getDateFromString(o2.get(0).getDate()));
                }

                return 0;
            }
        });
    }

    private Date getDateFromString(String str)
    {
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        try
        {
            Date date = format.parse(str);
            return date;
        } catch (ParseException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    void sortEventsByTime()
    {
        for (int i = 0; i < days.size(); i++)
        {
            Collections.sort(days.get(i), new Comparator<Event>()
            {
                @Override
                public int compare(Event o1, Event o2)
                {
                    try
                    {
                        return new SimpleDateFormat("hh:mm a").parse(o1.getStartTime()).compareTo(new SimpleDateFormat("hh:mm a").parse(o2.getStartTime()));
                    } catch (ParseException e)
                    {
                        return 0;
                    }
                }
            });
        }
    }


    void onItemsLoadComplete()
    {
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void loadEventDetails(Event event)
    {
        FragmentTransaction t = this.getFragmentManager().beginTransaction();
        t.addToBackStack("Event");
        EventFragment mFrag = new EventFragment();
        mFrag.passEvent(getActivity(), event);
        t.replace(R.id.main_container, mFrag);
        t.commit();
    }

    public void removeEvent(final Event event)
    {
        //remove an event a specific user
        if (isMine)
        {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

            Query titleQuery = userRef.child("userEvents").orderByChild("title").equalTo(event.getTitle());

            titleQuery.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    for (DataSnapshot titleSnapshot : dataSnapshot.getChildren())
                    {
                        titleSnapshot.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    Log.e(TAG, "onCancelled", databaseError.toException());
                }
            });
        } else if (!isMine && isAdmin)
        {
            //Delete the event for any user who has RSVP'd to it.
            DatabaseReference fullRef = FirebaseDatabase.getInstance().getReference();

            Query fullTitleQuery = fullRef.child("Events").orderByChild("title").equalTo(event.getTitle());
            Query userQuery = fullRef.child("Users");

            //This is a bit of a complex bit here. First, get a snapshot of the database at "Users."
            userQuery.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    //For every datasnapshot taken (one for each user) do block
                    for (DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        //Get yet another database reference pointing to each snapshot, which should be a user.
                        DatabaseReference temp = FirebaseDatabase.getInstance().getReference().child("Users").child(snapshot.getKey());
                        //Query that reference as with any other, and remove any matching events.
                        Query userEventQuery = temp.child("userEvents").orderByChild("title").equalTo(event.getTitle());
                        userEventQuery.addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot)
                            {
                                for (DataSnapshot userEventSnapshot : dataSnapshot.getChildren())
                                {
                                    userEventSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError)
                            {
                                Log.e(TAG, "onCancelled", databaseError.toException());
                            }
                        });

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                }
            });
            //Delete the event from the schedule.
            fullTitleQuery.addListenerForSingleValueEvent(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    for (DataSnapshot titleSnapshot : dataSnapshot.getChildren())
                    {
                        titleSnapshot.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {
                    Log.e(TAG, "onCancelled", databaseError.toException());
                }
            });
        } else
        {
            Toast.makeText(getContext(), "Admin permissions Required", Toast.LENGTH_LONG).show();
        }
        adapter.notifyDataSetChanged();
        FragmentTransaction refresh = getFragmentManager().beginTransaction();
        refresh.detach(this).attach(this).commit();
    }

    @Override
    public void onResume()
    {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK)
                {

                    getActivity().getSupportFragmentManager().popBackStack();
                    ((HomePage) getActivity()).getSupportActionBar().setTitle("Home");
                    return true;

                }

                return false;
            }
        });
    }


}
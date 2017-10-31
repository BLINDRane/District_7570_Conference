package bwastedsoftware.district_7570_conference;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ScheduleFragment extends Fragment {


    public ScheduleFragment() {
        // Required empty public constructor
    }

    View v;
    SwipeRefreshLayout mSwipeRefreshLayout;
    ArrayList<ArrayList<Event>> days;
    ViewPager viewPager;
    PagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_schedule, container, false);


        initializeData();
        //initializeAdapters(rv);

        // Locate the ViewPager in viewpager_main.xml
        viewPager = (ViewPager) v.findViewById(R.id.pager);
        // Pass results to ViewPagerAdapter Class


        refreshData();
        initializeAdapter();

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshData();
            }
        });

        return v;
    }


    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = mDatabase.getReference().child("Events");

    private void initializeData() {
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
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot childrenSnapShot : dataSnapshot.getChildren())
                {
                    Event event = childrenSnapShot.getValue(Event.class);
                    newevents.add(new Event(event.getTitle(), event.getLocation(), event.getDate(), event.getTime(), event.getDetails(), event.getSpeaker()));
                }
                addEvents(newevents);

            }

            @Override
            public void onCancelled(DatabaseError error) {
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

        for(Event e : newevents)
        {
            //Log.w("LOOK HERE", "HEY TRYING AN EVENT");
            if(days.size() == 0)
            {
                ArrayList<Event> day2 = new ArrayList<>();
                day2.add(e);
                days.add(day2);
                Log.w("LOOK HERE", "INIT DAY, MAKING NEW ARRAY LIST." + days.size());
            }
            else
            {
                for (int i = 0; i < days.size(); i++)
                {
                    if (days.get(i) != null && days.get(i).get(0).getDate().equals(e.getDate()))
                    {
                        days.get(i).add(e);
                        Log.w("LOOK HERE", "SAME DAY, ADDED");
                    }
                    else
                    {
                        ArrayList<Event> day2 = new ArrayList<>();
                        day2.add(e);
                        days.add(day2);
                        Log.w("LOOK HERE", "NEW DAY, MAKING NEW ARRAY LIST." + days.size());
                    }
                }
            }
        }
        //Log.w("LOOK HERE", "Did Events! " + days.size() + days.get(0).size());

        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void loadEventDetails(Event event)
    {
        FragmentTransaction t = this.getFragmentManager().beginTransaction();
        EventFragment mFrag = new EventFragment();
        mFrag.passEvent(getActivity(),event);
        t.replace(R.id.main_container, mFrag);
        t.commit();
    }

}


package bwastedsoftware.district_7570_conference;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class ScheduleFragment extends Fragment {


    public ScheduleFragment() {
        // Required empty public constructor
    }

    View v;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_schedule, container, false);

        ////followed tutorial on creating cards, found here: https://code.tutsplus.com/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465
        RecyclerView rv = (RecyclerView)v.findViewById(R.id.rv); // !!! THIS IS NOT GOOD PRACTICE; NEED TO FIND A BETTER WAY TO HANDLE THIS
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        initializeData();
        initializeAdapter(rv);

        return v;
    }

    private void initializeAdapter(RecyclerView rv)
    {
        RVAdapter adapter = new RVAdapter(events);
        rv.setAdapter(adapter);
    }


    private ArrayList<Event> events;


    private void initializeData(){
        events = new ArrayList<>();
        events.add(new Event("EVENT TITLE 1", "LOCATION", "DATE", "TIME", new Speaker("Billy", R.drawable.ic_account_circle_black_24dp)));
        events.add(new Event("EVENT TITLE 2", "LOCATION", "DATE", "TIME", new Speaker("Sue", R.drawable.ic_account_circle_black_24dp)));
        events.add(new Event("EVENT TITLE 3", "LOCATION", "DATE", "TIME", new Speaker("Aaron's Little Helper", R.drawable.ic_account_circle_black_24dp)));
    }

}


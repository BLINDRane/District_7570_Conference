
package bwastedsoftware.district_7570_conference;

        import android.os.Bundle;
        import android.support.v4.app.Fragment;
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


public class SpeakersFragment extends Fragment {

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView rv;


    public SpeakersFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_speakers, container, false);

        ////followed tutorial on creating cards, found here: https://code.tutsplus.com/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465
        rv = (RecyclerView)view.findViewById(R.id.rv); // !!! THIS IS NOT GOOD PRACTICE; NEED TO FIND A BETTER WAY TO HANDLE THIS
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        initializeData();
        initializeAdapter(rv);
        refreshData();

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshData();
            }
        });

        return view;
    }

    speakerRVAdapter adapter;

    private void initializeAdapter(RecyclerView rv)
    {
        adapter = new speakerRVAdapter(speakers);
        rv.setAdapter(adapter);
    }




    private ArrayList<Speaker> speakers = new ArrayList<>();
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = mDatabase.getReference().child("Speakers");

    private void initializeData() {
        speakers = new ArrayList<>();
    }

    private void refreshData()
    {
        adapter.clear();
        speakers.add(new Speaker("Speaker  1", "Photo"));


        final ArrayList<Speaker> newspeakers = new ArrayList<>();

        // Read from the database
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue(String.class);
                //Event = dataSnapshot.getValue(Post.class);
                //Post post = dataSnapshot.getValue(Post.class);
                for(DataSnapshot childrenSnapShot : dataSnapshot.getChildren())
                {
                    Speaker speaker = childrenSnapShot.getValue(Speaker.class);
                    newspeakers.add(new Speaker(speaker.getName(), speaker.getBio(), speaker.getPhoto()));
                    Log.w("GETTING CARDS", "value is" + speaker.getName() + speaker.getBio() + childrenSnapShot.getKey());
                }
                //Log.d("FIREBASE", "Value is: " + post);
                addSpeakers(newspeakers);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FIREBASE", "Failed to read value.", error.toException());
            }
        });

        //events.addAll(newevents);
        //Log.w("PROBLEM HERE", "LIST #" + events.size());
        //newevents.add(new Event("EVENT TITLE 3", "LOCATION", "DATE", "TIME", "DETAILS", new Speaker("Aaron's Little Helper", R.drawable.ic_account_circle_black_24dp)));

        //onItemsLoadComplete();
    }

    private void addSpeakers(ArrayList<Speaker> newspeakers)
    {
        speakers.addAll(newspeakers);
        Log.w("PROBLEM HERE", "LIST #" + speakers.size());
        newspeakers.add(new Speaker("Aaron's Little Helper", "Bio", "Photo"));
        onItemsLoadComplete();

    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

}

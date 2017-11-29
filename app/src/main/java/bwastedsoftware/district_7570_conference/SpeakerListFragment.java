package bwastedsoftware.district_7570_conference;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * Created by Boston on 10/23/2017.
 */

public class SpeakerListFragment extends Fragment
{
    public SpeakerListFragment() {
        // Required empty public constructor
    }

    View v;
    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView rv;
    Boolean isAdmin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((HomePageActivity)getActivity()).getSupportActionBar().setTitle("Speaker List");

        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_speakerlist, container, false);
        Bundle args = getArguments();
        isAdmin = args.getBoolean("IS_ADMIN");
        ////followed tutorial on creating cards, found here: https://code.tutsplus.com/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465
        rv = (RecyclerView)v.findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);

        initializeData();
        initializeAdapter(rv);
        refreshData();

        if (isAdmin)
        {
            FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab_createSpeaker);
            fab.setVisibility(View.VISIBLE);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    //go to create event fragment
                    final Bundle bundle = new Bundle();
                    final createSpeakerFragment createSpeakerFrag = new createSpeakerFragment();
                    createSpeakerFrag.setArguments(bundle);
                    bundle.putBoolean("IS_ADMIN", isAdmin);
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack("Speaker Creation");
                    fragmentTransaction.replace(R.id.main_container, createSpeakerFrag);
                    fragmentTransaction.commit();
                    //v.findViewById(R.id.toolbar).setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.banner));
                    ((HomePageActivity) getActivity()).getSupportActionBar().setTitle("Speaker Creation");
                }}
            );
        }

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

    speakerRVAdapter adapter;

    private void initializeAdapter(RecyclerView rv)
    {
        adapter = new speakerRVAdapter(speakers, getActivity(), this);
        rv.setAdapter(adapter);
    }


    ArrayList<Speaker> speakers;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = mDatabase.getReference().child("Speakers");

    private void initializeData() {
        speakers = new ArrayList<>();
    }

    private void refreshData()
    {
        adapter.clear();
        //speakers.add(new Speaker("Billy", "Bio", "http://www.munkurious.com/sharex/2017.10/ghanaTempleStainedGlass_100x.png"));


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
                    newspeakers.add(new Speaker(speaker.getName(), speaker.getTitle(), speaker.getWebpage(), speaker.getPhotoURL()));
                    //Log.w("GETTING CARDS", "value is" + event.getCompletionTime() + event.getLocation() + childrenSnapShot.getKey());
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

    }

    private void addSpeakers(ArrayList<Speaker> newspeakers)
    {
        speakers.addAll(newspeakers);
        //Log.w("PROBLEM HERE", "LIST #" + speakers.size());
        //newevents.add(new Event("EVENT TITLE 3", "LOCATION", "DATE", "TIME", "DETAILS", new Speaker("Aaron's Little Helper", "bio", "Photo")));
        onItemsLoadComplete();

    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        adapter.notifyDataSetChanged();
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void loadSpeakerDetails(Speaker speaker)
    {
        FragmentTransaction t = this.getFragmentManager().beginTransaction();
        t.addToBackStack("Speaker");
        SpeakerFragment mFrag = new SpeakerFragment();
        mFrag.passSpeaker(getActivity(),speaker);
        t.replace(R.id.main_container, mFrag);
        t.commit();
    }

    public void removeSpeaker(final Speaker speaker) {

      if (isAdmin) {

          //Remove the speaker's photo from storage

          StorageReference speakerStorage = FirebaseStorage.getInstance().getReferenceFromUrl(speaker.getPhotoURL());
          speakerStorage.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
              @Override
              public void onSuccess(Void aVoid) {
                  // File deleted successfully
                  Log.d(TAG, "onSuccess: deleted file");
              }
          }).addOnFailureListener(new OnFailureListener() {
              @Override
              public void onFailure(@NonNull Exception exception) {
                  // Uh-oh, an error occurred!
                  Log.d(TAG, "onFailure: did not delete file");
              }
          });

            //Delete a speaker from the database list
            DatabaseReference speakerRef = FirebaseDatabase.getInstance().getReference();

            Query speakerQuery = speakerRef.child("Speakers").orderByChild("name").equalTo(speaker.getName());

            speakerQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot titleSnapshot : dataSnapshot.getChildren()) {
                        titleSnapshot.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.e(TAG, "onCancelled", databaseError.toException());
                }
            });
        } else {
            Toast.makeText(getContext(), "Admin permissions Required", Toast.LENGTH_LONG).show();
        }
       adapter.notifyDataSetChanged();
        FragmentTransaction refresh = getFragmentManager().beginTransaction();
        refresh.detach(this).attach(this).commit();




    }

    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    getActivity().getSupportFragmentManager().popBackStack();
                    //((HomePageActivity)getActivity()).getSupportActionBar().setTitle("Home");
                    return true;

                }

                return false;
            }
        });
    }

}

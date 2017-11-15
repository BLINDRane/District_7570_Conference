package bwastedsoftware.district_7570_conference;



import android.content.Context;

import android.content.Intent;

import android.media.Image;

import android.net.Uri;

import android.os.Bundle;

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

import android.widget.Button;

import android.widget.ImageButton;

import android.widget.Switch;

import android.widget.TextView;

import android.widget.Toast;



import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;

import com.google.firebase.database.DatabaseError;

import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;

import com.google.firebase.storage.StorageReference;

import com.google.firebase.storage.UploadTask;


import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;




public class scavengerHunt extends Fragment {




    public scavengerHunt() {

        // Required empty public constructor

    }

    SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView rv;
    FirebaseAuth mAuth;
    String user_id;
    View view;
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scavenger_hunt, container, false);
        // Inflate the layout for this fragment

        rv = (RecyclerView)view.findViewById(R.id.rv);
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

    clueRVAdapter adapter;
    private void initializeAdapter(RecyclerView rv){
        adapter = new clueRVAdapter(clues, getActivity(), this);
        rv.setAdapter(adapter);
    }


    ArrayList<Clue> clues;
    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    DatabaseReference myRef = mDatabase.getReference().child("Speakers");

    public void loadClue(Clue clue){
        FragmentTransaction t = this.getFragmentManager().beginTransaction();
        t.addToBackStack("Speaker");
        clueFragment mFrag = new clueFragment();
        mFrag.passClue(getActivity(),clue);
        t.replace(R.id.main_container, mFrag);
        t.commit();
    }

    private void initializeData(){
clues = new ArrayList<>();
    }


    private void refreshData(){
        adapter.clear();
        clues.add(new Clue("First Clue", "Grab a drink with the Presidents of the United States"));
        clues.add(new Clue("Second Clue", "Turns out Lincoln was a bit clingy. Go somewhere he won't want to be"));
        final ArrayList<Clue> newClues = new ArrayList<>();

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
                    Clue clue = childrenSnapShot.getValue(Clue.class);
                    newClues.add(new Clue(clue.getTitle(), clue.getInstruction()));
                    //Log.w("GETTING CARDS", "value is" + event.getDate() + event.getLocation() + childrenSnapShot.getKey());
                }
                //Log.d("FIREBASE", "Value is: " + post);
                addClues(newClues);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FIREBASE", "Failed to read value.", error.toException());
            }
        });
    }

    private void addClues(ArrayList<Clue> newClues)
    {
        clues.addAll(newClues);
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
                    ((HomePage)getActivity()).getSupportActionBar().setTitle("Home");
                    ((HomePage)getActivity()).toolbarBackground(false);
                    return true;

                }

                return false;
            }
        });
    }

}


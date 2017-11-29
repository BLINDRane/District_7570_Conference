package bwastedsoftware.district_7570_conference;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.jinatonic.confetti.CommonConfetti;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class WinnersCircleFragment extends Fragment {

    TextView first;
    TextView second;
    TextView third;
    TextView userParticipation;
    private FirebaseAuth mAuth;
    private String user_id;
    private ArrayList<Winner> tempWinners;
    private ArrayList<Winner> winnerList;
    private DatabaseReference winners;

    public WinnersCircleFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((HomePageActivity)getActivity()).getSupportActionBar().setTitle("Winners Circle");

        View view = inflater.inflate(R.layout.fragment_winners_circle, container, false);

        first = (TextView) view.findViewById(R.id.firstPlace);
        second = (TextView) view.findViewById(R.id.secondPlace);
        third = (TextView) view.findViewById(R.id.thirdPlace);
        userParticipation = (TextView) view.findViewById(R.id.participatedCheck);

        tempWinners = new ArrayList<>();
        winnerList = new ArrayList<>();
        winners = FirebaseDatabase.getInstance().getReference().child("Users who have Completed the Scavenger Hunt");

        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();

        CommonConfetti.rainingConfetti(container, new int[] { Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW}).stream(1000);

        getWinners();
        // Inflate the layout for this fragment
        return view;
    }

    private void addWinners(ArrayList<Winner> Winners){
        winnerList.addAll(Winners);
    }

    private void getWinners(){
        winners.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childrenSnapShot : dataSnapshot.getChildren()) {
                    Winner Winner = childrenSnapShot.getValue(bwastedsoftware.district_7570_conference.Winner.class);
                    tempWinners.add(new Winner(Winner.getUserName(), Winner.getCompletionTime()));
                }

                addWinners(tempWinners);
                getTopThree(tempWinners);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getTopThree(final ArrayList<Winner> Winners){
        Collections.sort(Winners, new Comparator<Winner>() {
            @Override
            public int compare(Winner e1, Winner e2) {
                return e1.getDateFormated().compareTo(e2.getDateFormated());
            }
        });

        for(int i = 0; i<3; i++){
            if(i==0){
                Log.v("FIRST PLACE", Winners.get(i).getUserName());
                first.setText(Winners.get(i).getUserName());
            } else if(i==1){
                Log.v("SECOND PLACE", Winners.get(i).getUserName());
                second.setText(Winners.get(i).getUserName());
            } else if(i==2){
                Log.v("THIRD PLACE", Winners.get(i).getUserName());
                third.setText(Winners.get(i).getUserName());
            }

        }
             DatabaseReference users = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

            users.addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("scavProgress").getValue(int.class) !=0){
                        userParticipation.setText("You participated");
                    } else {
                        userParticipation.setText("You didn't participate.");
                    }
               }

               @Override
               public void onCancelled(DatabaseError databaseError) {

               }
           });

        }

public  void onPause(){
    super.onPause();
}
    }



package bwastedsoftware.district_7570_conference;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment implements View.OnClickListener{
    Button btnCurrent, btUpcoming;
    TextView welcomeText;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String user_id;


    public HomeFragment(){
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Link the layout pieces with their Java counterparts
        btnCurrent = (Button) view.findViewById(R.id.current_button);
        welcomeText = (TextView) view.findViewById(R.id.welcomeTxtView);
        //Give abilities to the layout parts
        btnCurrent.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        personilizeWelcomeText();


        return view;
    }

    @Override
    public void onClick(View v) {

    }

    //This method changes the welcome text, giving it a personal (and professional) touch.
    private void personilizeWelcomeText(){
        user_id = mAuth.getCurrentUser().getUid();
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String current_user = dataSnapshot.child(user_id).child("Fname").getValue(String.class);
                welcomeText.setText("Welcome to District Conference, " + current_user);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
package bwastedsoftware.district_7570_conference;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
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
if(v.getId() == R.id.current_button){
    sendNotification();
}
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


    public void sendNotification(){

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getContext());

//Create the intent that’ll fire when the user taps the notification//

        Intent intent = new Intent(getContext(), HomePage.class);
        intent.putExtra("From", "notifyFrag");
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);

        mBuilder.setContentIntent(pendingIntent);

        mBuilder.setSmallIcon(R.drawable.homestead_fall);
        mBuilder.setContentTitle("Rate Event");
        mBuilder.setContentText("Please take a moment to rate that last event!");

        NotificationManager mNotificationManager =

                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(001, mBuilder.build());


    }

    //this will enable using the back button to pop the stack, which will go to previous fragment instead of the login screen.
    @Override
    public void onResume() {

        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    mAuth.signOut();
                                    startActivity(new Intent(getContext(), Login.class));
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Are you sure you want to log out?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();

                    return true;

                }

                return false;
            }
        });
    }

}
package bwastedsoftware.district_7570_conference;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;


public class EventFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton rsvp;
    private Event mEvent;
    private Context mContext;
    private String user_id;
    protected View mView;
    private FirebaseAuth mAuth;
    private DatabaseReference nDatabase;

    public EventFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_event, container, false);

        mAuth = FirebaseAuth.getInstance();
        nDatabase = FirebaseDatabase.getInstance().getReference();
        rsvp = (FloatingActionButton) mView.findViewById(R.id.eventView_attendingButton);
        rsvp.setOnClickListener(this);


        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        //this.mView = view;
        updateEventDetails();
    }

    public void passEvent(Context context, Event event) {
        mContext = context;
        mEvent = event;
    }

    private void updateEventDetails()
    {
        TextView mTitleView = (TextView) mView.findViewById(R.id.eventView_titleText);
        TextView mLocationView = (TextView) mView.findViewById(R.id.eventView_locationText);
        TextView mTimeView = (TextView) mView.findViewById(R.id.eventView_timeText);

        //add speakers code here
        RelativeLayout mSpeakerList = (RelativeLayout) mView.findViewById(R.id.eventView_speakerLayout);
        LinearLayout sLinearLayout = (LinearLayout) mView.findViewById(R.id.speakerLayout_linear);
        ImageView sImageView = (ImageView) mView.findViewById(R.id.speakerLayout_avatarView);
        LinearLayout sTextLayout = (LinearLayout) mView.findViewById(R.id.speakerLayout_textViewContainer);
        TextView sSpeakerName = (TextView) mView.findViewById(R.id.speakerLayout_textViewContainer_titleText);
        TextView sSpeakerBio = (TextView) mView.findViewById(R.id.speakerLayout_textViewContainer_subTitleText);

        Picasso.with(mContext).load(mEvent.getSpeaker().getPhotoURL()).fit().placeholder(R.drawable.ic_account_circle_black_24dp).into(sImageView);
        sSpeakerName.setText(mEvent.getSpeaker().getName());
        sSpeakerBio.setText(mEvent.getSpeaker().getBio());


        TextView mBodyView = (TextView) mView.findViewById(R.id.eventView_detailsBody);

        mTitleView.setText(mEvent.getTitle());
        mLocationView.setText(mEvent.getLocation());
        mTimeView.setText(mEvent.getTime());
        mBodyView.setText(mEvent.getDetails());
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.eventView_attendingButton){

            user_id = mAuth.getCurrentUser().getUid();
           FirebaseDatabase mydb = FirebaseDatabase.getInstance();
            DatabaseReference mDatabase = mydb.getReference().child("Users").child(user_id);

            String key = nDatabase.child("Speakers").push().getKey();

            Map<String, Object> eventValues = mEvent.toMap();

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/userEvents/" + key, eventValues);
            //childUpdates.put("/user-posts/" + title + "/" + key, postValues);

            mDatabase.updateChildren(childUpdates);
            /*
            DatabaseReference findEvent = mDatabase.child("Events");
            findEvent.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean found;
                    String search = mEvent.getTitle();
                    for(DataSnapshot childrenSnapShot : dataSnapshot.getChildren()){
                       String title = childrenSnapShot.child("title").getValue(String.class);
                        found = title.contains(search);
                        Log.d("TAG", title + " / " + found);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            //DatabaseReference current_user_db = mDatabase.child(user_id);
            //current_user_db.child("userEvents").setValue();
*/

        } else {

        }
    }
}
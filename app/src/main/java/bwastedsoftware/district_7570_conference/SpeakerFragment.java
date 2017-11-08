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
import android.widget.RatingBar;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class SpeakerFragment extends Fragment implements View.OnClickListener {


    private Speaker mSpeaker;
    private Context mContext;
    protected View mView;
    RatingBar rate;

    public SpeakerFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_speaker, container, false);

        rate = (RatingBar) mView.findViewById(R.id.eventRater);

        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //this.mView = view;
        updateEventDetails();
    }

    public void passSpeaker(Context context, Speaker speaker) {
        mContext = context;
        mSpeaker = speaker;
    }

    private void updateEventDetails()
    {
        ImageView speakerPic = (ImageView) mView.findViewById(R.id.speakerPic);
        TextView name = (TextView) mView.findViewById(R.id.speakerName);
        TextView bio = (TextView) mView.findViewById(R.id.bioLink);
        Picasso.with(mContext).load(mSpeaker.getPhotoURL()).fit().placeholder(R.drawable.ic_account_circle_black_24dp).transform(new PicassoCircleTransform()).into(speakerPic);
        name.setText(mSpeaker.getName());
        bio.setText(mSpeaker.getBio());
    }

    @Override
    public void onClick(View v) {

    }
}
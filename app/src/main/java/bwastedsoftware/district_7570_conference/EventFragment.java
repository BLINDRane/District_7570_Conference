package bwastedsoftware.district_7570_conference;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.vision.text.Line;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;


public class EventFragment extends Fragment {


    private Event mEvent;
    private Context mContext;
    protected View mView;

    public EventFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_event, container, false);
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

}
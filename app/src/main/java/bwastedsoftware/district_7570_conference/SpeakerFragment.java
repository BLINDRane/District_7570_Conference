package bwastedsoftware.district_7570_conference;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class SpeakerFragment extends Fragment implements View.OnClickListener {


    private Speaker mSpeaker;
    private Context mContext;
    protected View mView;


    public SpeakerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((HomePage)getActivity()).getSupportActionBar().setTitle("Speaker Details");

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_speaker, container, false);


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
        TextView webpage = (TextView) mView.findViewById(R.id.webPage);
        Picasso.with(mContext).load(mSpeaker.getPhotoURL()).fit().centerCrop().placeholder(R.drawable.ic_account_circle_black_24dp).transform(new PicassoCircleTransform()).into(speakerPic);
        name.setText(mSpeaker.getName());
        bio.setText(mSpeaker.getTitle());
        webpage.setText(mSpeaker.getWebpage());
    }

    @Override
    public void onClick(View v) {

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
                    //((HomePage)getActivity()).getSupportActionBar().setTitle("Schedule");
                    return true;

                }

                return false;
            }
        });
    }
}
package bwastedsoftware.district_7570_conference;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.view.menu.MenuView;
import android.util.Log;
import android.view.KeyEvent;
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

import bwastedsoftware.district_7570_conference.Clue;
import bwastedsoftware.district_7570_conference.Event;
import bwastedsoftware.district_7570_conference.HomePage;
import bwastedsoftware.district_7570_conference.R;


public class clueFragment extends Fragment implements View.OnClickListener {

    FloatingActionButton rsvp;
    private Clue mClue;
    private Context mContext;
    private String user_id;
    protected View mView;
    private FirebaseAuth mAuth;
    private DatabaseReference nDatabase;
    RatingBar rate;

    public clueFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_clue, container, false);


        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        //this.mView = view;
        updateClueDetails();
    }

    public void passClue(Context context, Clue clue) {
        mContext = context;
        mClue = clue;
    }

    private void updateClueDetails()
    {
        TextView mTitleView = (TextView) mView.findViewById(R.id.clue_Number);
        TextView mInstructionView = (TextView) mView.findViewById(R.id.instruction_View);

        mTitleView.setText(mClue.getTitle());
        mInstructionView.setText(mClue.getInstruction());


    }

    @Override
    public void onClick(View v) {
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

                    getActivity().getSupportFragmentManager().popBackStack();
                    ((HomePage)getActivity()).getSupportActionBar().setTitle("Scavenger Hunt");
                    ((HomePage)getActivity()).toolbarBackground(true);
                    return true;

                }

                return false;
            }
        });
    }

}

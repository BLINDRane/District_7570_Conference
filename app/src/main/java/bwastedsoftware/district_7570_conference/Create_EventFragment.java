package bwastedsoftware.district_7570_conference;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import bwastedsoftware.district_7570_conference.dummy.cEvent;


public class Create_EventFragment extends Fragment implements View.OnClickListener {

    EditText etTitle, etLocation, etSpeakers, etWhen, etDetails, etDate;
    Button saveEvent;
    private DatabaseReference mDatabase;


    public Create_EventFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create__event, container, false);
        saveEvent = (Button) view.findViewById(R.id.btn_Save_Event);
        etTitle = (EditText) view.findViewById(R.id.edit_Title);
        etLocation = (EditText) view.findViewById(R.id.edit_Location);
        etSpeakers = (EditText) view.findViewById(R.id.edit_Speakers);
        etWhen = (EditText) view.findViewById(R.id.edit_When);
        etDate = (EditText) view.findViewById(R.id.edit_Date);
        etDetails = (EditText) view.findViewById(R.id.edit_Details);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        saveEvent.setOnClickListener(this);

        return view;
    }

    private void createEvent(cEvent event) {

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_Save_Event){

            //create a new User using information put in by the *ahem* user.

            String Title = etTitle.getText().toString().trim();
            String Location = etLocation.getText().toString().trim();
            String Speakers = etSpeakers.getText().toString().trim();
            String Time = etWhen.getText().toString().trim();
            String Date = etDate.getText().toString().trim();
            String Details = etDetails.getText().toString().trim();

            String key = mDatabase.child("posts").push().getKey();
            Event event = new Event(Title, Location, Date, Time, Details, new Speaker(Speakers, R.drawable.ic_account_circle_black_24dp));
            Map<String, Object> eventValues = event.toMap();

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/events/" + key, eventValues);
            //childUpdates.put("/user-posts/" + title + "/" + key, postValues);

            mDatabase.updateChildren(childUpdates);

        }


    }
}

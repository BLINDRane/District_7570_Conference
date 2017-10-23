package bwastedsoftware.district_7570_conference;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bwastedsoftware.district_7570_conference.dummy.cEvent;


public class Create_EventFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    EditText etTitle, etLocation, etWhen, etDetails, etDate;
    Spinner speakerPick;
    Button saveEvent;
    ArrayList<Speaker> speakers;
    ArrayAdapter<Speaker> sAdapter;
    String cName, cBio, cPhoto;
    Speaker chosenOne = new Speaker(cName, cBio, cPhoto);
    private DatabaseReference mDatabase;
    private DatabaseReference mRef;
    private StorageReference mStorage;

    public Create_EventFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_create__event, container, false);
        saveEvent = (Button) view.findViewById(R.id.btn_Save_Event);
        etTitle = (EditText) view.findViewById(R.id.edit_speakerName);
        etLocation = (EditText) view.findViewById(R.id.edit_Location);
        etWhen = (EditText) view.findViewById(R.id.edit_When);
        etDate = (EditText) view.findViewById(R.id.edit_Date);
        etDetails = (EditText) view.findViewById(R.id.edit_Details);
        speakerPick = (Spinner) view.findViewById(R.id.spinner_Speakers);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mRef = FirebaseDatabase.getInstance().getReference().child("Speakers");

        speakers = new ArrayList<Speaker>();

        speakerPick.setOnItemSelectedListener(this);
        saveEvent.setOnClickListener(this);

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot childrenSnapshot : dataSnapshot.getChildren()){
                    Speaker newSpeaker = childrenSnapshot.getValue(Speaker.class);

                    sAdapter = new ArrayAdapter<Speaker>(getActivity(), android.R.layout.select_dialog_item, speakers);
                    sAdapter.setDropDownViewResource(android.R.layout.select_dialog_item);
                    speakers.add(new Speaker(newSpeaker.getName(), newSpeaker.getBio(), newSpeaker.getPhotoURL()));
                }
                speakerPick.setAdapter(sAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        chosenOne = sAdapter.getItem(position);
        Toast.makeText(view.getContext(), "Selected Speaker: " + chosenOne.getName() + chosenOne.getBio(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_Save_Event){

            //create a new User using information put in by the *ahem* user.

            String Title = etTitle.getText().toString().trim();
            String Location = etLocation.getText().toString().trim();
            String Time = etWhen.getText().toString().trim();
            String Date = etDate.getText().toString().trim();
            String Details = etDetails.getText().toString().trim();
            String key = mDatabase.child("Speakers").push().getKey();

            Event event = new Event(Title, Location, Date, Time, Details, chosenOne);

            Map<String, Object> eventValues = event.toMap();

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/Events/" + key, eventValues);
            //childUpdates.put("/user-posts/" + title + "/" + key, postValues);

            mDatabase.updateChildren(childUpdates);

        }


    }


}

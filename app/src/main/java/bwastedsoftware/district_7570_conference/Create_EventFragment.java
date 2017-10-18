package bwastedsoftware.district_7570_conference;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import bwastedsoftware.district_7570_conference.dummy.cEvent;


public class Create_EventFragment extends Fragment implements View.OnClickListener {

    EditText etTitle, etLocation, etSpeakers, etWhen, etDetails;
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
            String When = etWhen.getText().toString().trim();
            String Details = etDetails.getText().toString().trim();

            HashMap<String, String> dataMap = new HashMap<String, String>();
            dataMap.put("Title", Title);
            dataMap.put("Location", Location);
            dataMap.put("Speakers", Speakers);
            dataMap.put("When", When);
            dataMap.put("Details", Details);


            //store data in firebase
            mDatabase.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                    if(task.isSuccessful()){



                    } else {


                    }
                }
            });




            cEvent event = new cEvent(Title, Location, When, Speakers, Details);
            createEvent(event);
        }


    }
}

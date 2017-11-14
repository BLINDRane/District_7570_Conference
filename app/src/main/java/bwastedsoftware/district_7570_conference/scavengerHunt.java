package bwastedsoftware.district_7570_conference;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;


public class scavengerHunt extends Fragment implements View.OnClickListener {

Button letsGo, submit;
ImageButton uploadImage;
TextView welcomeScav;
TextView instructions;
FirebaseAuth mAuth;
DatabaseReference mDatabase;
StorageReference mStorage;
private String user_id;
private String winner;
private static final int PICK_IMAGE = 1;
private Uri imageURI;

    public scavengerHunt() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scavenger_hunt, container, false);
        //initialization code
        letsGo = (Button) view.findViewById(R.id.scav_Button);
        submit = (Button) view.findViewById(R.id.submit_Image);
        uploadImage = (ImageButton) view.findViewById(R.id.scav_Pic);
        winner = "complete";
        welcomeScav = (TextView) view.findViewById(R.id.welcome_scav_Text);
        instructions = (TextView) view.findViewById(R.id.instruction_Scav_Text);
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mStorage = FirebaseStorage.getInstance().getReference();


        letsGo.setOnClickListener(this);
        uploadImage.setOnClickListener(this);
        submit.setOnClickListener(this);
        submit.setClickable(false);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.scav_Button){

                letsGo.setText("Find it!");
                letsGo.setClickable(false);
                scavengerHunt();

        } else if(v.getId() == R.id.scav_Pic){

            openGallery();
            submit.setClickable(true);

        } else if(v.getId() == R.id.submit_Image){
            StorageReference filepath = mStorage.child("Scavenger Hunt Pictures").child(imageURI.getLastPathSegment());
            filepath.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getContext(), "Your offering has been accepted.", Toast.LENGTH_LONG).show();
                    letsGo.setText("Next Clue");
                    letsGo.setClickable(true);
                    submit.setClickable(false);
                    //@SuppressWarnings("VisibleForTests")

                    //The following commented out code is a template for saving the photo URL.
                    // Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    //DatabaseReference newSpeaker = mDatabase.push();
                    //newSpeaker.child("name").setValue(name);
                    //newSpeaker.child("bio").setValue(bio);
                    //newSpeaker.child("photoURL").setValue(downloadUrl.toString());
                }
            });
        }
    }

    public void scavengerHunt() {
        //Get a snapshot of the database to see where the user is at on the clues.

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                    //get the current value of scavenger hunt instructions
                    String currentInstruction = dataSnapshot.child("Scavenger Hunt Instructions").getValue(String.class);

                        //if this is the first time the user has started the scavenger hunt
                    if (dataSnapshot.child("Scavenger Hunt Instructions").getValue(String.class) == null) {

                        mDatabase.child("Scavenger Hunt Instructions").setValue("Start");
                        mDatabase.child("Scavenger Hunt Instructions").setValue(getInstruction("Start"));


                        updateInstructions();

                    } else

                        //if the user has finished the hunt
                    if (dataSnapshot.child("Scavenger Hunt Instructions").getValue(String.class) == "Winner") {
                        Toast.makeText(getContext(), "You have completed the challenge!", Toast.LENGTH_LONG).show();
                        //exit the fragment
                    } else {

                        //this is normal operation

                        //Change the database value to the next instruction
                        mDatabase.child("Scavenger Hunt Instructions").setValue(getInstruction(currentInstruction));
                        updateInstructions();
                    }
                }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO: Error catch
            }
        });
//maybe something needs to go here
    }

    private void updateInstructions(){
        //Take a new (and freshly updated) snapshot
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Change the instruction text to the instruction the user needs to see.
                instructions.setText(dataSnapshot.child("Scavenger Hunt Instructions").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO: Error catch
            }
        });
    }


    public String getInstruction(String current){
        String instruction = current;
      switch (current){
          case "Start":
              instruction =  "Grab a drink with the Presidents of the United States";
                break;
          case "Grab a drink with the Presidents of the United States":
              instruction = "Turns out Lincoln is a bit clingy. Go somewhere he wouldn't want to go.";
                break;
          case "Turns out Lincoln is a bit clingy. Go somewhere he wouldn't want to go.":
              instruction = "Winner";
              break;
        }
       return instruction;
    }

    private void openGallery(){
        //Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        //startActivityForResult(gallery, PICK_IMAGE);
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageURI = data.getData();
            uploadImage.setImageURI(imageURI);
        }
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
                    ((HomePage)getActivity()).getSupportActionBar().setTitle("Home");
                    ((HomePage)getActivity()).toolbarBackground(false);
                    return true;

                }

                return false;
            }
        });
    }

}



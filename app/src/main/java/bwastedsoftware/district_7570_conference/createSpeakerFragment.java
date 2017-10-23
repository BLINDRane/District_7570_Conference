package bwastedsoftware.district_7570_conference;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import bwastedsoftware.district_7570_conference.dummy.cEvent;

import static android.app.Activity.RESULT_OK;


public class createSpeakerFragment extends Fragment implements View.OnClickListener {

    EditText etSpeakerName, etSpeakerBio;
    Button saveSpeaker;
    ImageButton speakerPic;
    private DatabaseReference mDatabase;
    private static final int PICK_IMAGE = 1;
    private Uri imageURI;
    private StorageReference mStorage;

    public createSpeakerFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_speaker, container, false);
        saveSpeaker = (Button) view.findViewById(R.id.btn_Save_Speaker);
        etSpeakerName = (EditText) view.findViewById(R.id.edit_speakerName);
        etSpeakerBio = (EditText) view.findViewById(R.id.edit_SpeakerBio);
        speakerPic = (ImageButton) view.findViewById(R.id.speaker_image);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Speakers");
        mStorage = FirebaseStorage.getInstance().getReference();
        speakerPic.setOnClickListener(this);
        saveSpeaker.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_Save_Speaker){
            //create a new User using information put in by the *ahem* user.

            final String name = etSpeakerName.getText().toString().trim();
            final String bio = etSpeakerBio.getText().toString().trim();

            StorageReference filepath = mStorage.child("SpeakerPics").child(imageURI.getLastPathSegment());
            //Check for success.
            //TODO: Check for failure.
            filepath.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    @SuppressWarnings("VisibleForTests")
            //Succeeding here means we have first saved the picture to the STORAGE, and then
            //also saved the new speaker with name, bio, and image URL string to the database.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newSpeaker = mDatabase.push();
                    newSpeaker.child("name").setValue(name);
                    newSpeaker.child("bio").setValue(bio);
                    newSpeaker.child("photoURL").setValue(downloadUrl.toString());
                    //After creating a speaker, the user should be returned to the homeFragment.
                }
            });


            String key = mDatabase.child("Speakers").push().getKey();
            //create a new speaker object
            Speaker speaker = new Speaker(name, bio, imageURI.toString());



        } else if(v.getId() == R.id.speaker_image){
            openGallery();
        }


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
            speakerPic.setImageURI(imageURI);
        }
    }
}

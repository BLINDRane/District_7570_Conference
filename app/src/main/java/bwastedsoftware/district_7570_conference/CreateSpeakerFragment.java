package bwastedsoftware.district_7570_conference;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static android.app.Activity.RESULT_OK;


public class CreateSpeakerFragment extends Fragment implements View.OnClickListener {

    EditText etSpeakerName, etSpeakerBio, etSpeakerWebPage;
    Button saveSpeaker;
    ImageButton speakerPic;
    private DatabaseReference mDatabase;
    private static final int PICK_IMAGE = 1;
    private Uri imageURI;
    private StorageReference mStorage;
    Boolean isAdmin;

    public CreateSpeakerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        ((HomePageActivity)getActivity()).getSupportActionBar().setTitle("Create New Speaker");

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_speaker, container, false);
        Bundle args = getArguments();
        isAdmin = args.getBoolean("IS_ADMIN");
        saveSpeaker = (Button) view.findViewById(R.id.btn_submit_photo);
        etSpeakerName = (EditText) view.findViewById(R.id.edit_);
        etSpeakerBio = (EditText) view.findViewById(R.id.edit_SpeakerBio);
        etSpeakerWebPage = (EditText) view.findViewById(R.id.edit_speakerWebPage);
        speakerPic = (ImageButton) view.findViewById(R.id.speaker_image);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Speakers");
        mStorage = FirebaseStorage.getInstance().getReference();
        speakerPic.setOnClickListener(this);
        saveSpeaker.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_submit_photo){
            //create a new User using information put in by the *ahem* user.

            uploadSpeaker();



        } else if(v.getId() == R.id.speaker_image){
            openGallery();
        }


    }

    private void uploadSpeaker()
    {
        final String name = etSpeakerName.getText().toString().trim();
        final String bio = etSpeakerBio.getText().toString().trim();
        final String webpage = etSpeakerWebPage.getText().toString().trim();

        StorageReference filepath = mStorage.child("SpeakerPics").child(imageURI.getLastPathSegment());

        Bitmap bitmap = null;
        try
        {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),imageURI);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        //resize the bitmap
        Bitmap resized = null;
        try
        {
            int height;
            if (bitmap != null)
            {
                height = Math.round((300 / (float) bitmap.getWidth()) * (float) bitmap.getHeight());
            }
            else
            {
                height = 300;
            }

            resized = Bitmap.createScaledBitmap(rotateImageIfRequired(bitmap, getActivity(), imageURI), 300, height, false);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = filepath.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                //noinspection VisibleForTests
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                DatabaseReference newSpeaker = mDatabase.push();
                newSpeaker.child("name").setValue(name);
                newSpeaker.child("title").setValue(bio);
                newSpeaker.child("webpage").setValue(webpage);
                newSpeaker.child("photoURL").setValue(downloadUrl.toString());
                goToSpeakerPage(name);
            }
        });

        //    //Check for success.
        //    //TODO: Check for failure.
        //    filepath.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        //        @Override
        //        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
        //            @SuppressWarnings("VisibleForTests")
        //    //Succeeding here means we have first saved the picture to the STORAGE, and then
        //    //also saved the new speaker with name, bio, and image URL string to the database.
        //            Uri downloadUrl = taskSnapshot.getDownloadUrl();
        //            DatabaseReference newSpeaker = mDatabase.push();
        //            newSpeaker.child("name").setValue(name);
        //            newSpeaker.child("bio").setValue(bio);
        //            newSpeaker.child("photoURL").setValue(downloadUrl.toString());
        //            //After creating a speaker, the user should be returned to the homeFragment.
        //        }
        //    });


        //String key = mDatabase.child("Speakers").push().getKey();
        //create a new speaker object
        //Speaker speaker = new Speaker(name, bio, imageURI.toString());
    }

    private void goToSpeakerPage(String name)
    {
        getActivity().getSupportFragmentManager().popBackStack();

        //There seems to be an issue created by this way of loading the fragment. Saving commented code in case we need it back before release.
        
        //Bundle bundle = new Bundle();
        //bundle.putBoolean("IS_ADMIN", isAdmin);
        //SpeakerListFragment mFrag = new SpeakerListFragment();
        //mFrag.setArguments(bundle);
        //FragmentTransaction t = this.getFragmentManager().beginTransaction();
        //t.replace(R.id.main_container, mFrag);
        //t.commit();

        Snackbar.make(getActivity().findViewById(android.R.id.content), "Speaker Created: " + name, Snackbar.LENGTH_LONG).show();
    }


    //so samsung likes to take pictures with an exif showing their rotation instead of the pixels; dumb right?
    //anyway thanks to some code from online (https://teamtreehouse.com/community/how-to-rotate-images-to-the-correct-orientation-portrait-by-editing-the-exif-data-once-photo-has-been-taken)
    //we can fix this rotation if required
    public static Bitmap rotateImageIfRequired(Bitmap img, Context context, Uri selectedImage) throws IOException {

        if (selectedImage.getScheme().equals("content")) {
            String[] projection = { MediaStore.Images.ImageColumns.ORIENTATION };
            Cursor c = context.getContentResolver().query(selectedImage, projection, null, null, null);
            if (c.moveToFirst()) {
                final int rotation = c.getInt(0);
                c.close();
                return rotateImage(img, rotation);
            }
            return img;
        } else {
            ExifInterface ei = new ExifInterface(selectedImage.getPath());
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
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

                    return true;

                }

                return false;
            }
        });
    }


}

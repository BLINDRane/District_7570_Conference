package bwastedsoftware.district_7570_conference;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;


public class clueFragment extends Fragment implements View.OnClickListener {


    private Clue mClue;
    private Context mContext;
    protected View mView;
    private static final int PICK_IMAGE = 1;
    private Uri imageURI;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference completed;
    private DatabaseReference winnerList;
    private DatabaseReference nameGetter;
    ImageButton cluePic;
    Button submit;
    String user_id;
    String Fname;
    String Lname;

    public clueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((HomePageActivity)getActivity()).getSupportActionBar().setTitle("Clue");

        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_clue, container, false);

        cluePic = (ImageButton) mView.findViewById(R.id.clue_Image);
        submit = (Button) mView.findViewById(R.id.btn_submit_photo);
        mStorage = FirebaseStorage.getInstance().getReference();
        submit.setOnClickListener(this);
        cluePic.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        user_id = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("completedClues");
        completed = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id).child("scavProgress");
        winnerList = FirebaseDatabase.getInstance().getReference().child("Users who have Completed the Scavenger Hunt");
        nameGetter = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        nameGetter.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Fname = dataSnapshot.child("Fname").getValue(String.class);
                Lname = dataSnapshot.child("Lname").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //TODO: catch this error
            }
        });
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
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
        if (v.getId() == R.id.clue_Image) {
            openImageOptions();
        } else if (v.getId() == R.id.btn_submit_photo) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                uploadPhoto();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Is this what you want to submit? You will not be able to re-enter this clue once you do.").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        }



    private void uploadPhoto()
    {


        StorageReference filepath = mStorage.child("Scavenger Hunt Pictures").child(user_id).child(mClue.getTitle()).child(imageURI.getLastPathSegment());
        mDatabase.child(mClue.getTitle()).setValue("");

        completed.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int numCompletions;
                if(dataSnapshot.getValue() == null){
                    numCompletions = 1;
                    completed.setValue(numCompletions);
                } else if(dataSnapshot.getValue(int.class) != 11) {
                    numCompletions = (dataSnapshot.getValue(int.class) + 1);
                    completed.setValue(numCompletions);
                } else if(dataSnapshot.getValue(int.class) == 11){ //if user finishes twelve clues, save their completion time under their name
                    numCompletions = (dataSnapshot.getValue(int.class) + 1);
                    completed.setValue(numCompletions);
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                    String formattedDate = df.format(c.getTime());
                    winnerList.child(user_id).child("completionTime").setValue(formattedDate);
                    winnerList.child(user_id).child("userName").setValue(Fname + " " + Lname);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
            resized = Bitmap.createScaledBitmap(rotateImageIfRequired(bitmap, getActivity(), imageURI), 300, 300, false);
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

            }
        });

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
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        return rotatedImg;
    }



    private void openImageOptions(){
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
            cluePic.setImageURI(imageURI);

        }
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
                    //((HomePageActivity)getActivity()).getSupportActionBar().setTitle("Scavenger Hunt");
                    return true;

                }

                return false;
            }
        });
    }

}

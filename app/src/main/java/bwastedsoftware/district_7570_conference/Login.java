package bwastedsoftware.district_7570_conference;

import android.content.Context;
import android.media.Image;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Login extends AppCompatActivity implements View.OnClickListener {

    //Creates the buttons and text boxes on the page

    Button btnLogin, btnRegisterT;
    EditText etEmail, etPassword;
    UserLocalStore userLocalStore;
    ImageView btnWheel;
    RelativeLayout overlayLayout;

    private Boolean isLoggingIn = false;

    //This creates a database instance for Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    Bundle bundle;
    Boolean isAdmin;
    ArrayList<String> adminEmails = new ArrayList<>();
    //Here android is setting up the activity that will be displayed
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.splash_fadein, R.anim.splash_fadeout);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        /*This tells  the created buttons and fields where to get their input from in the
         matching XML file.
          */

        etEmail = (EditText) findViewById(R.id.edit_txt_useremail);
        etPassword = (EditText) findViewById(R.id.edit_txt_password);
        btnWheel = (ImageView) findViewById(R.id.login_rotary_button);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnRegisterT = (Button) findViewById(R.id.btn_register_transfer);
        overlayLayout = (RelativeLayout) findViewById(R.id.logging_in_overlay);


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        //Set a click listener for the buttons

        btnLogin.setOnClickListener(this);
        btnRegisterT.setOnClickListener(this);
        btnWheel.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);

        //populate the list of admins
        adminEmails.add("wyatt.karnes@gmail.com");
        adminEmails.add("george.karnes@yahoo.com");
        adminEmails.add("bostonc@gmail.com");

        //enter listener to login
        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(event.getAction() == KeyEvent.KEYCODE_ENTER)
                {
                    checkLogin();
                    return true;
                }
                return false;
            }
        });

    }

    private void checkAdminStatus(){
        if(adminEmails.contains(etEmail.getText().toString().trim())){
            isAdmin = true;
        } else {
            isAdmin = false;
        }
    }

    // Defines what happens when a button is clicked
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
                 checkLogin();

        } else if (v.getId() == R.id.btn_register_transfer) {

            startActivity(new Intent(this, Register.class));

        } else if (v.getId() == R.id.login_rotary_button) {
          rotateWheel();

        }
    }

    private void rotateWheel(){
        if (btnWheel.getRotation() == 0) {
            btnWheel.animate().rotation(360).start();
        } else {
            btnWheel.animate().rotation(360-(btnWheel.getRotation()));
        }
    }

    private void checkLogin(){
        if(!isLoggingIn)
        {
            //hide keyboard
            // Check if no view has focus:
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }


            isLoggingIn = true;
            overlayLayout.setVisibility(View.VISIBLE);
            String Email = etEmail.getText().toString().trim();
            String Password = etPassword.getText().toString().trim();
            if (!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Password))
            {
                checkAdminStatus();
                mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {

                        if (task.isSuccessful())
                        {

                            checkUserInDatabase();

                        } else
                        {
                            Toast.makeText(Login.this, "Login Error", Toast.LENGTH_LONG).show();
                        }

                        isLoggingIn = false;
                        overlayLayout.setVisibility(View.INVISIBLE);
                    }
                });

            } else
            {
                Toast.makeText(Login.this, "Login Error", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void checkUserInDatabase(){

        final String user_id = mAuth.getCurrentUser().getUid();

        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(user_id)){
                    bundle = new Bundle();
                    bundle.putBoolean("IS_ADMIN", isAdmin);
                    Intent mIntent = new Intent(Login.this, HomePage.class);
                    mIntent.putExtras(bundle);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mIntent);

                }else{
                    Toast.makeText(Login.this, "You need to set up an account", Toast.LENGTH_LONG);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}

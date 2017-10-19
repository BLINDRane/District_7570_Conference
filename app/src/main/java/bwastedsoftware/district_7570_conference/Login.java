package bwastedsoftware.district_7570_conference;

import android.media.Image;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.ImageView;
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

public class Login extends AppCompatActivity implements View.OnClickListener {

    //Creates the buttons and text boxes on the page

    Button btnLogin, btnRegisterT;
    EditText etEmail, etPassword;
    UserLocalStore userLocalStore;
    ImageView btnWheel;

    //This creates a database instance for Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


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


        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        //Set a click listener for the buttons

        btnLogin.setOnClickListener(this);
        btnRegisterT.setOnClickListener(this);
        btnWheel.setOnClickListener(this);
        userLocalStore = new UserLocalStore(this);
    }


    // Defines what happens when a button is clicked
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_login) {
                 checkLogin();
        } else if (v.getId() == R.id.btn_register_transfer) {

            startActivity(new Intent(this, Register.class));

        } else if (v.getId() == R.id.login_rotary_button) {
            if (btnWheel.getRotation() == 0) {
                btnWheel.animate().rotation(360).start();
            } else {
                btnWheel.animate().rotation(360-(btnWheel.getRotation()));
            }

        }
    }

    private void checkLogin(){
        String Email = etEmail.getText().toString().trim();
        String Password = etPassword.getText().toString().trim();

        if(!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Password)){

            mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if(task.isSuccessful()){

                        checkUserInDatabase();

                    } else {
                        Toast.makeText(Login.this, "Login Error", Toast.LENGTH_LONG).show();
                    }
                }
            });

        } else {
            Toast.makeText(Login.this, "Login Error", Toast.LENGTH_LONG).show();
        }
    }

    private void checkUserInDatabase(){

        final String user_id = mAuth.getCurrentUser().getUid();

        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(user_id)){

                    Intent mIntent = new Intent(Login.this, HomePage.class);
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

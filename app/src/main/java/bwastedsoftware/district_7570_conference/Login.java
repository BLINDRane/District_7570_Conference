package bwastedsoftware.district_7570_conference;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.ImageView;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity implements View.OnClickListener {

    //Creates the buttons and text boxes on the page

    Button btnLogin, btnRegisterT;
    EditText etEmail, etPassword;
    UserLocalStore userLocalStore;
    Button btnFirebase;
    ImageView btnWheel;

    //This creates a database instance for Firebase
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


        //Firebase test setup
        btnFirebase = (Button) findViewById(R.id.firebase_btn);
        btnFirebase.setOnClickListener(this);
        mDatabase = FirebaseDatabase.getInstance().getReference();

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
            //TODO: make it so that a person HAS to register in order to log in.
            User user = new User(null, null);
            userLocalStore.storeUserData(user);
            userLocalStore.setUserLoggedIn(true);

            startActivity(new Intent(this, HomePage.class));

        } else if (v.getId() == R.id.btn_register_transfer) {

            startActivity(new Intent(this, Register.class));

        } else if (v.getId() == R.id.firebase_btn) {

            //Create a child in the root object, then assign a value to that child
            String email = etEmail.getText().toString().trim();
            mDatabase.child("Email").setValue(email);


        } else if (v.getId() == R.id.login_rotary_button) {
            if (btnWheel.getRotation() == 0) {
                btnWheel.animate().rotation(360).start();
            } else {
                btnWheel.animate().rotation(360-(btnWheel.getRotation()));
            }
<<<<<<< HEAD

=======
//pretend line of code for github
>>>>>>> test commit

        }
    }
}

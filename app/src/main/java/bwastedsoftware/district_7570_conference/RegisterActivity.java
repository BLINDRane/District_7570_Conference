package bwastedsoftware.district_7570_conference;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    //Initializes the buttons that will be used on the register page

    Button btnRegister;
    EditText etFName, etLName, etEmail, etPassword, etReEnterPassword;

    //For firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    //Progress dialogue shows the user whats going on
    private ProgressDialog mProgress;
    //email validation
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    //Sets up the activity screen
    @Override
     protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        //Assign the XML objects to the Java ones we just made

        etFName = (EditText) findViewById(R.id.edit_txt_userFName);
        etLName = (EditText) findViewById(R.id.edit_txt_userLName);
        etEmail = (EditText) findViewById(R.id.edit_txt_useremail);
        etPassword = (EditText) findViewById(R.id.edit_txt_password);
        etReEnterPassword = (EditText) findViewById(R.id.edit_txt_reenter_password);
        btnRegister = (Button) findViewById(R.id.btn_register);

        //Set up a progress dialog
        mProgress = new ProgressDialog(this);

        //Set a click listener for the button
        btnRegister.setOnClickListener(this);

        //Get an instance of Firebase authorization and database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

    }


    @Override
    protected  void onStart(){
        super.onStart();

    }

    //Tells the click listener what to do
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_register) {

            if(etFName.getText().length()==0) {
                Toast.makeText(getApplicationContext(), "Please Enter A First Name", Toast.LENGTH_SHORT).show();
                return;}

            if(etLName.getText().length()==0) {
                Toast.makeText(getApplicationContext(), "Please Enter A Last Name", Toast.LENGTH_SHORT).show();
                return;}

            if (etEmail.getText().length() == 0) {
                Toast.makeText(getApplicationContext(), "Please Enter an Email Address", Toast.LENGTH_SHORT).show();

                 return;}

                if (!isEmailValid(etEmail.getText())) {
                    Toast.makeText(getApplicationContext(), "Please Enter a Valid Email Address", Toast.LENGTH_SHORT).show();
                      return;}

                    if (!(etPassword.getText().length() >= 6)) {
                        Toast.makeText(getApplicationContext(), "Password Must Be At Least Six Characters", Toast.LENGTH_SHORT).show();
                          return;}

                        if(!etPassword.getText().toString().equals(etReEnterPassword.getText().toString())){
                            Toast.makeText(getApplicationContext(), "Passwords Do Not Match", Toast.LENGTH_SHORT).show();
                             return;}
                                    startRegister();
                        }
                    }

    private void startRegister(){
        final String Fname = etFName.getText().toString().trim();
        final String Lname = etLName.getText().toString().trim();
        String Email = etEmail.getText().toString().trim();
        final String Password = etPassword.getText().toString().trim();

        if(!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Password)){
            //Notify user of what is going on
            mProgress.setMessage("Registering new user....");
            mProgress.show();

            mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        String user_id = mAuth.getCurrentUser().getUid();
                         DatabaseReference current_user_db = mDatabase.child(user_id);
                        current_user_db.child("Fname").setValue(Fname);
                        current_user_db.child("Lname").setValue(Lname);
                        current_user_db.child("scavProgress").setValue(0);
                        //remove progress message, so the user knows they are registered.
                        mProgress.setMessage("User Registered");
                        mProgress.dismiss();
                        Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);
                    } else{




                    }
                }
            });
        }


    }

}

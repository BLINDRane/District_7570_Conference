package bwastedsoftware.district_7570_conference;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gms.gcm.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity implements View.OnClickListener {

    //Initializes the buttons that will be used on the register page

    Button btnRegister;
    EditText etFName, etLName, etEmail, etPassword, etReEnterPassword;

    //For firebase
    private DatabaseReference mDatabase;


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
        mDatabase = FirebaseDatabase.getInstance().getReference();


        //Set a click listener for the button
        btnRegister.setOnClickListener(this);
    }



    //Registers a new user with Firebase
    private void registerUser(User user){

    }


    //Tells the click listener what to do
    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btn_register){

            //create a new User using information put in by the *ahem* user.

            String Fname = etFName.getText().toString().trim();
            String Lname = etLName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            HashMap<String, String> dataMap = new HashMap<String, String>();
            dataMap.put("Fname", Fname);
            dataMap.put("Lname", Lname);
            dataMap.put("Email", email);
            dataMap.put("Password", password);


            //store data in firebase
            mDatabase.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull com.google.android.gms.tasks.Task<Void> task) {
                    if(task.isSuccessful()){

                        Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_LONG).show();

                    } else {

                        Toast.makeText(Register.this, "Error: Registration Incomplete", Toast.LENGTH_LONG).show();
                    }
                }
            });




            User user = new User(Fname, Lname, email, password);
            registerUser(user);

            startActivity(new Intent(this, Login.class));
        }


    }
}

package bwastedsoftware.district_7570_conference;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements View.OnClickListener{
    Button btnCurrent;
    TextView welcomeText;




    public HomeFragment(){
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        // Link the layout pieces with their Java counterparts
        btnCurrent = (Button) view.findViewById(R.id.current_button);
        welcomeText = (TextView) view.findViewById(R.id.welcomeTxtView);

        //Give abilities to the layout parts
        btnCurrent.setOnClickListener(this);



        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
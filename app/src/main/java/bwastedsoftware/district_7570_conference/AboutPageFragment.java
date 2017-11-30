package bwastedsoftware.district_7570_conference;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


public class AboutPageFragment extends Fragment implements View.OnClickListener {
    Element mGitElement;
    ViewGroup mContainer;
    public AboutPageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((HomePageActivity)getActivity()).getSupportActionBar().setTitle("About");

        mContainer = container;

        getSponsors();

        Uri uri = Uri.parse("https://github.com/BLINDRane/District_7570_Conference");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);

        mGitElement = new Element()
                .setIconDrawable(R.drawable.about_icon_github)
                .setTitle("Read more on GitHub")
                .setIntent(browserIntent);


        return createAboutPage().addGroup("About This Project")
                .addItem(mGitElement)
                .create();
    }

    private AboutPage createAboutPage()
    {
        return new AboutPage(getActivity())
                .setImage(R.drawable.ic_rotary_international_logo)
                .setDescription(getString(R.string.about_string))
                .addGroup("About Rotary")
                .addWebsite("http://www.rotary.org")
                .addGroup("About Rotary District 7570")
                .addWebsite("http://www.rotary7570.org/districtconference");
    }

    private void addSponsors(ArrayList<Sponsor> sponsors)
    {
        AboutPage about = createAboutPage().addGroup("Sponsors");

        for(Sponsor sponsor : sponsors)
        {
            about.addItem(createSponsorElement(sponsor));
        }

        View aboutPage = about.addGroup("About This Project")
            .addItem(mGitElement)
            .create();

        mContainer.removeAllViews();
        mContainer.addView(aboutPage);
    }

    private Element createSponsorElement(Sponsor sponsor)
    {
        return new Element()
                .setIconDrawable(R.drawable.about_icon_link)
                .setTitle(sponsor.getName())
                .setIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(sponsor.getWebsite())));
    }

    private ArrayList<Sponsor> getSponsors()
    {
        final ArrayList<Sponsor> sponsors = new ArrayList<>();

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Sponsors");
        mRef.addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                for(DataSnapshot childrenSnapShot : dataSnapshot.getChildren())
                {
                    Sponsor sponsor = childrenSnapShot.getValue(Sponsor.class);
                    sponsors.add(sponsor);
                }
                addSponsors(sponsors);
            }

            @Override
            public void onCancelled(DatabaseError error)
            {
                // Failed to read value
            }
        });
        return sponsors;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {

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
                    return true;

                }

                return false;
            }
        });
    }

    @Override
    public void onClick(View v)
    {

    }
}
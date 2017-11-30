package bwastedsoftware.district_7570_conference;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


public class AboutPageFragment extends Fragment implements View.OnClickListener {

    protected View mView;

    public AboutPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ((HomePageActivity)getActivity()).getSupportActionBar().setTitle("About");

        Uri uri = Uri.parse("https://github.com/BLINDRane/District_7570_Conference");
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, uri);

        Element gitElement = new Element();
        gitElement.setIconDrawable(R.drawable.about_icon_github);
        gitElement.setTitle("Read more on GitHub");
        gitElement.setIntent(browserIntent);

        View aboutPage = new AboutPage(getActivity())
                .setImage(R.drawable.ic_rotary_international_logo)
                .setDescription(getString(R.string.about_string))
                .addGroup("About Rotary")
                .addWebsite("http://www.rotary.org")
                .addGroup("About Rotary District 7570")
                .addWebsite("http://www.rotary7570.org/districtconference")
                .addGroup("About This Project")
                .addItem(gitElement)
                .create();

        return aboutPage;
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
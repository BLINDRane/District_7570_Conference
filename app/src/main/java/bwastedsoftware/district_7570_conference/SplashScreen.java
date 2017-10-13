package bwastedsoftware.district_7570_conference;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.splash_fadein, R.anim.splash_fadeout);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                Intent mInHome = new Intent(SplashScreen.this, Login.class);
                SplashScreen.this.startActivity(mInHome);
                SplashScreen.this.finish();
            }
        }, 2000);
    }
}


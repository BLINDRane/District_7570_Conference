package bwastedsoftware.district_7570_conference;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.splash_fadein, R.anim.splash_fadeout);
        setContentView(R.layout.activity_splash_screen);

        ImageView splashImage = (ImageView) findViewById(R.id.splashScreenImage);

        Picasso.with(this).load(R.drawable.ic_splashscreen_newtwo).placeholder(R.mipmap.ic_splashscreen).into(splashImage);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                Intent mInHome = new Intent(SplashScreen.this, LoginActivity.class);
                SplashScreen.this.startActivity(mInHome);
                SplashScreen.this.finish();
            }
        }, 1000);
    }
}


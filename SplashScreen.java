package com.example.user.emergencyapps;

import android.content.Intent;
import android.media.Image;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
    private ImageView Logo;
    private int timeout =3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Logo = findViewById(R.id.logo);


        Animation myanim = AnimationUtils.loadAnimation(SplashScreen.this,R.anim.mysplashanimation);
        Logo.startAnimation(myanim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashScreen.this,Loginpage.class);
                startActivity(intent);
                finish();
            }
        },timeout);
    }
}

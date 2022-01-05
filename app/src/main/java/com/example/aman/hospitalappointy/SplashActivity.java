package com.example.aman.hospitalappointy;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.aman.hospitalappointy.home.HomeActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView mSplashText = (ImageView) findViewById(R.id.splash_logo1);
        ImageView mSplashLogo = (ImageView) findViewById(R.id.splash_logo);
        Animation uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        Animation downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup);

        mSplashText.setAnimation(downtoup);
        mSplashLogo.setAnimation(uptodown);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                HomeActivity.launch(SplashActivity.this);
                finish();
            }
        }, 3000);
    }

}

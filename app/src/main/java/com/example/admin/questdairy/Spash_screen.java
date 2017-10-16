package com.example.admin.questdairy;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Spash_screen extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);


        ImageView iv = (ImageView)findViewById(R.id.iv);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.zoom);
        iv.startAnimation(animation);

        TextView tvsp = (TextView)findViewById(R.id.tvsp);
        Animation animation1 = AnimationUtils.loadAnimation(this,R.anim.blink);
        tvsp.startAnimation(animation1);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(Spash_screen.this, Login.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}

package com.example.xupr44dlb.maquinariausada;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends Activity {
    private static final long SPLASH_SCREEN_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

      TimerTask tarea=new TimerTask() {
            @Override
            public void run() {
                Intent mainIntent = new Intent().setClass(
                        SplashActivity.this, MainActivity.class);
                startActivity(mainIntent);
                finish();

            }
        };

        Timer timer=new Timer();
        timer.schedule(tarea, SPLASH_SCREEN_DELAY );


    }
}

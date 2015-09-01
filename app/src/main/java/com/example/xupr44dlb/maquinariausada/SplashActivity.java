package com.example.xupr44dlb.maquinariausada;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import java.util.Timer;
import java.util.TimerTask;


public class SplashActivity extends Activity {
    private static final long SPLASH_SCREEN_DELAY = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

      TimerTask tarea=new TimerTask() {
            @Override
            public void run() {
                SharedPreferences prefs=getSharedPreferences("loginUsuarios", Context.MODE_PRIVATE);
                Boolean res=prefs.getBoolean("session",false);
                if (res)
                {
                    Log.i("SPLASH VERDADERO",res.toString());
                    Intent mainIntent = new Intent().setClass(
                            SplashActivity.this, MenuActivity.class);
                    Bundle b=new Bundle();
                    b.putBoolean("descargaInfo",true);
                    mainIntent.putExtras(b);
                    startActivity(mainIntent);
                    finish();
                }
                else
                {
                    Log.i("SPLASH FALSO",res.toString());
                    Intent mainIntent = new Intent().setClass(
                            SplashActivity.this, MainActivity.class);
                    startActivity(mainIntent);
                    finish();
                }


            }
        };

        Timer timer=new Timer();
        timer.schedule(tarea, SPLASH_SCREEN_DELAY );


    }
}

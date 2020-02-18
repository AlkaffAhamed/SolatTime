package com.paiya.alkaff.solattime;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity
{
    Handler handler;
    private static int SPLASH_TIMEOUT = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        handler = new Handler();
        handler.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent intent = new Intent(SplashScreen.this, SolatTime.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}

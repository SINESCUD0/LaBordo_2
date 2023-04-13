package com.example.labordo.splash_screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.labordo.R;
import com.example.labordo.activity.LoginActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);



        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };

        Timer tiempo = new Timer();
        tiempo.schedule(tarea, 5000);
    }
}
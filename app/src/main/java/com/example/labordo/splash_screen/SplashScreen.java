package com.example.labordo.splash_screen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.labordo.R;
import com.example.labordo.activity.LoginActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Animation animacionAbajo = AnimationUtils.loadAnimation(this, R.anim.desplazamiento_abajo);

        TextView texto1 = findViewById(R.id.titulo);
        TextView texto2 = findViewById(R.id.nombre1);
        TextView texto3 = findViewById(R.id.nombre2);
        ImageView imagen = findViewById(R.id.icono);

        texto1.setAnimation(animacionAbajo);
        texto2.setAnimation(animacionAbajo);
        texto3.setAnimation(animacionAbajo);
        imagen.setAnimation(animacionAbajo);

        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        };

        Timer tiempo = new Timer();
        tiempo.schedule(tarea, 6000);
    }
}
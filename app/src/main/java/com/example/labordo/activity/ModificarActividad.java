package com.example.labordo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.labordo.R;

public class ModificarActividad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_actividad);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
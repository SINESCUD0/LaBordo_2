package com.example.labordo.activity;


import android.app.Activity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.labordo.R;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        findViewById(R.id.botonAceptarSetting).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et1 = (EditText)findViewById(R.id.correoUsuarioRecuperar1);;
                EditText et2 = (EditText)findViewById(R.id.correoUsuarioRecuperar2);

                String pass1 = et1.getText().toString();
                String pass2 = et2.getText().toString();

                if(pass1.equals(pass2)){
                    Toast.makeText(view.getContext(), "Contraseña cambiada", Toast.LENGTH_LONG).show();
                    // Hacer cosas
                    finish();
                }else{
                    Toast.makeText(view.getContext(), "No coinciden. Inténtelo de nuevo", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
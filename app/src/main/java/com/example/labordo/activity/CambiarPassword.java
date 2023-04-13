package com.example.labordo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.labordo.R;

public class CambiarPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cambiar_password);

        findViewById(R.id.botonAceptarPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et1 = (EditText)findViewById(R.id.passwordNueva);;
                EditText et2 = (EditText)findViewById(R.id.passwordNueva2);

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
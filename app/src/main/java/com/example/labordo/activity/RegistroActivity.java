package com.example.labordo.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labordo.R;
import com.example.labordo.base_datos.BaseDatosGeneral;
import com.example.labordo.base_datos.RegistroDatos;

public class RegistroActivity extends Activity {

    EditText correo;
    EditText DNI;
    EditText password;
    RadioGroup tipo;
    RadioButton tipoProfesor, tipoAlumno;
    Button crear;
    String tipoUsuario;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
        correo = (EditText) findViewById(R.id.correo_Usuario);
        DNI = (EditText) findViewById(R.id.DNIUsuario);
        password = (EditText) findViewById(R.id.password_Usuario);
        tipo = (RadioGroup) findViewById(R.id.radioTipo);
        tipoProfesor = (RadioButton) findViewById(R.id.profesor);
        tipoAlumno = (RadioButton) findViewById(R.id.alumno);
        crear = (Button) findViewById(R.id.crearUsuario);
        BaseDatosGeneral dbHelper = new BaseDatosGeneral(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUsuario(v);
                Intent i = new Intent(RegistroActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });
    }

    public void saveUsuario(View view){
        RegistroDatos dbRegistro = new RegistroDatos(RegistroActivity.this);
        boolean boton1 = tipoProfesor.isChecked();
        boolean boton2 = tipoAlumno.isChecked();
        if(boton1 != false){
            tipoUsuario = "Profesor";
        } else if (boton2 != false) {
            tipoUsuario = "Alumno";
        }
        dbRegistro.insertarDatos(correo.getText().toString(), DNI.getText().toString(), password.getText().toString(),
                tipoUsuario);
        Toast.makeText(this,"REGISTRO GUARDADO", Toast.LENGTH_LONG).show();
    }
}

package com.example.labordo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.labordo.R;
import com.example.labordo.base_datos.BaseDatosGeneral;
import com.example.labordo.splash_screen.SplashScreen;
import com.example.labordo.usuarios.Main_Alumnado;
import com.example.labordo.usuarios.Main_Profesorado;

import java.util.Timer;
import java.util.TimerTask;

public class LoginActivity extends Activity {

    EditText correoUsuario;
    EditText passwordUsuario;
    Cursor cv;
    String datos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        correoUsuario = (EditText) findViewById(R.id.correoUsuario);
        passwordUsuario = (EditText) findViewById(R.id.passwordUsuario);
    }

    public void iniciarSesion(View view){
        BaseDatosGeneral dbHelper = new BaseDatosGeneral(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String correo = correoUsuario.getText().toString();
        String password = passwordUsuario.getText().toString();
        cv = db.rawQuery("SELECT correoUsuario, passwordUsuario, tipoCuenta FROM t_usuarios WHERE correoUsuario = '"
                +correo+"' AND passwordUsuario = '"+password+"'",null);
        try {
            if (cv.moveToFirst()) {
                String corr = cv.getString(0);
                String pass = cv.getString(1);
                String tipo = cv.getString(2);
                if (correo.equals(corr) && password.equals(pass)) {

                    switch(tipo){
                        case "Profesor": startActivity(new Intent(this, Main_Profesorado.class));break;
                        case "Alumno": startActivity(new Intent(this, Main_Alumnado.class));break;
                    }
                    //Intent i = new Intent(this, Main_Profesorado.class);
                    //startActivity(i);
                    datos = corr;
                    correoUsuario.setText("");
                    passwordUsuario.setText("");
                }
            }
            else {
                Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void registrarse(View view){
        Intent i = new Intent(this, RegistroActivity.class);
        startActivity(i);
    }
}
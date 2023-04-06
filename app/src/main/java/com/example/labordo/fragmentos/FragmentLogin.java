package com.example.labordo.fragmentos;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.labordo.MainActivity;
import com.example.labordo.R;
import com.example.labordo.base_datos.BaseDatosGeneral;
import com.example.labordo.registro.RegistroDatos;

public class FragmentLogin extends Fragment {

    Button inicio;
    Button crearCuenta;
    EditText correoUsuario;
    EditText passwordUsuario;
    Cursor cv;
    String datos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_login, container, false);
        correoUsuario = (EditText) vista.findViewById(R.id.correoUsuario);
        passwordUsuario = (EditText) vista.findViewById(R.id.passwordUsuario);
        inicio = (Button) vista.findViewById(R.id.login);
        crearCuenta = (Button) vista.findViewById(R.id.crearCuenta);
        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iniciarSesion();
            }
        });
        crearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarse();
            }
        });

        return vista;
    }

    public void iniciarSesion(){
        BaseDatosGeneral dbHelper = new BaseDatosGeneral(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String correo = correoUsuario.getText().toString();
        String password = passwordUsuario.getText().toString();
        cv = db.rawQuery("SELECT correoUsuario, passwordUsuario FROM t_usuarios WHERE correoUsuario = '"
                +correo+"' AND passwordUsuario = '"+password+"'",null);
        try {
            if (cv.moveToFirst()) {
                String corr = cv.getString(0);
                String pass = cv.getString(1);
                if (correo.equals(corr) && password.equals(pass)) {
                    Intent i = new Intent(getContext(), MainActivity.class);
                    startActivity(i);
                    datos = corr;
                    correoUsuario.setText("");
                    passwordUsuario.setText("");
                }
            }
            else {
                Toast.makeText(getContext(), "Datos incorrectos", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            Toast.makeText(getContext(), "Error" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void registrarse(){
        Intent i = new Intent(getContext(), RegistroDatos.class);
        startActivity(i);
    }
}
package com.example.labordo.fragmentos;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.labordo.R;
import com.example.labordo.base_datos.BaseDatosGeneral;
import com.example.labordo.registro.RegistroDatos;

public class FragmentRegistro extends Fragment {

    EditText correo;
    EditText DNI;
    EditText password;
    RadioGroup tipo;
    RadioButton tipoProfesor, tipoAlumno;
    Button crear;
    String tipoUsuario;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_registro, container, false);
        correo = (EditText) vista.findViewById(R.id.correo_Usuario);
        DNI = (EditText) vista.findViewById(R.id.DNIUsuario);
        password = (EditText) vista.findViewById(R.id.password_Usuario);
        tipo = (RadioGroup) vista.findViewById(R.id.radioTipo);
        tipoProfesor = (RadioButton) vista.findViewById(R.id.profesor);
        tipoAlumno = (RadioButton) vista.findViewById(R.id.alumno);
        crear = (Button) vista.findViewById(R.id.crearUsuario);
        BaseDatosGeneral dbHelper = new BaseDatosGeneral(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUsuario(v);
            }
        });

        return vista;
    }

    public void saveUsuario(View view){
        RegistroDatos dbRegistro = new RegistroDatos(getContext());
        boolean boton1 = tipoProfesor.isChecked();
        boolean boton2 = tipoAlumno.isChecked();
        if(boton1 != false){
            tipoUsuario = "Profesor";
        } else if (boton2 != false) {
            tipoUsuario = "Alumno";
        }
        dbRegistro.insertarDatos(correo.getText().toString(), DNI.getText().toString(), password.getText().toString(),
                tipoUsuario);
        Toast.makeText(getContext(),"REGISTRO GUARDADO", Toast.LENGTH_LONG).show();
    }
}
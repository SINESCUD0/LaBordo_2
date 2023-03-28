package com.example.labordo.fragmentos;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.labordo.R;
import com.example.labordo.objetos.ActividadesVo;
import com.example.labordo.recyclerview.AdapterDatos;

import java.util.ArrayList;

public class Tab1Fragment extends Fragment{

    ArrayList<ActividadesVo> listDatos;
    RecyclerView recycler;
    Button add;

    String nombreTarea;
    String descripcion;

    int i = 0;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_tab1, container, false);
        listDatos = new ArrayList<>();
        recycler = (RecyclerView) vista.findViewById(R.id.TareasAsignadas);
        add = (Button) vista.findViewById(R.id.AddTarea);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pedirInformacion();
            }
        });


        return vista;
    }



    private void pedirInformacion() {

        LinearLayout linearLayout = new LinearLayout(this.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setTitle("Meta informoacion:");

        EditText editTextNombreActividad = new EditText(this.getContext());
        editTextNombreActividad.setHint("Nombre de la Actividad");
        editTextNombreActividad.setInputType(InputType.TYPE_CLASS_TEXT);


        EditText editTextDescripcion = new EditText(this.getContext());
        editTextDescripcion.setHint("Descripción de la Actividad");
        editTextDescripcion.setInputType(InputType.TYPE_CLASS_TEXT);

        linearLayout.addView(editTextNombreActividad);
        linearLayout.addView(editTextDescripcion);

        alertDialogBuilder.setView(linearLayout);
        alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                nombreTarea = editTextNombreActividad.getText().toString();
                descripcion = editTextDescripcion.getText().toString();

                listDatos.add(new ActividadesVo(nombreTarea, descripcion, R.drawable.goku_prueba));
                AdapterDatos adapter = new AdapterDatos(listDatos);
                recycler.setAdapter(adapter);
            }

        });

        alertDialogBuilder.setNegativeButton("Cancelar", null);

        alertDialogBuilder.show();
    }
}
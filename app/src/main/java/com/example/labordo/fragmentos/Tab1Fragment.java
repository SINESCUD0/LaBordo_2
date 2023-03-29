package com.example.labordo.fragmentos;

import android.annotation.SuppressLint;
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
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    String precio;
    String fecha = "";
    int actividad;
    String fechaElegida;

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



    @SuppressLint("ResourceType")
    public void pedirInformacion() {

        LinearLayout linearLayout = new LinearLayout(this.getContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder.setTitle("Introduce la información:");

        EditText editTextNombreActividad = new EditText(this.getContext());
        editTextNombreActividad.setHint("Nombre de la Actividad");
        editTextNombreActividad.setInputType(InputType.TYPE_CLASS_TEXT);

        EditText editTextDescripcion = new EditText(this.getContext());
        editTextDescripcion.setHint("Descripción de la Actividad");
        editTextDescripcion.setInputType(InputType.TYPE_CLASS_TEXT);

        EditText editTextPrecioActividad = new EditText(this.getContext());
        editTextPrecioActividad.setHint("Precio de la Actividad");
        editTextPrecioActividad.setInputType(InputType.TYPE_CLASS_NUMBER);

        LinearLayout horizontal = new LinearLayout(this.getContext());
        horizontal.setOrientation(LinearLayout.HORIZONTAL);

        TextView addFecha = new TextView(this.getContext());
        addFecha.setText("Añade la fecha limite:");

        Button botonFecha = new Button(this.getContext());
        botonFecha.setText("Añade la fecha");

        TextView fechaFinal = new TextView(this.getContext());

        RadioGroup radioGroupActividad = new RadioGroup(this.getContext());

        RadioButton radioActiva = new RadioButton(radioGroupActividad.getContext());
        radioActiva.setText("Asignada");
        radioActiva.setId(0);

        RadioButton radioInactiva = new RadioButton(radioGroupActividad.getContext());
        radioInactiva.setText("Inactiva");
        radioInactiva.setId(1);

        RadioButton radioSinAsignar = new RadioButton(radioGroupActividad.getContext());
        radioSinAsignar.setText("Sin asignar");
        radioSinAsignar.setId(2);

        linearLayout.addView(editTextNombreActividad);
        linearLayout.addView(editTextDescripcion);
        linearLayout.addView(editTextPrecioActividad);
        linearLayout.addView(horizontal);
        horizontal.addView(addFecha);
        horizontal.addView(botonFecha);
        linearLayout.addView(fechaFinal);
        linearLayout.addView(radioGroupActividad);
        radioGroupActividad.addView(radioActiva);
        radioGroupActividad.addView(radioInactiva);
        radioGroupActividad.addView(radioSinAsignar);


        alertDialogBuilder.setView(linearLayout);

        botonFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pedirFecha();
            }
        });
        alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                nombreTarea = editTextNombreActividad.getText().toString();
                descripcion = editTextDescripcion.getText().toString();
                precio = editTextPrecioActividad.getText().toString();
                actividad = radioGroupActividad.getCheckedRadioButtonId();

                int circulo;
                switch (actividad){
                    case 0:
                        circulo = R.drawable.asignada;
                        break;
                    case 1:
                        circulo = R.drawable.inactivo;
                        break;
                    case 2:
                        circulo = R.drawable.pendiente;
                        break;
                    default:
                        circulo = R.drawable.inactivo;
                        break;
                }

                if(!nombreTarea.equals("") && !descripcion.equals("") && !precio.equals("") /* && !fecha.equals("")*/) {
                    listDatos.add(new ActividadesVo(nombreTarea, descripcion, R.drawable.goku_prueba, precio,fecha, circulo));
                    AdapterDatos adapter = new AdapterDatos(listDatos);
                    recycler.setAdapter(adapter);
                } else if (nombreTarea.equals("") || descripcion.equals("") || precio.equals("") /*|| fecha.equals("")*/) {
                    Toast.makeText(getContext(), "Introduce todos los datos", Toast.LENGTH_SHORT).show();
                }


            }

        });

        alertDialogBuilder.setNegativeButton("Cancelar", null);

        alertDialogBuilder.show();
    }
    @SuppressLint("ResourceType")
    private void pedirFecha() {
        LinearLayout linearLayout2 = new LinearLayout(this.getContext());
        linearLayout2.setOrientation(LinearLayout.VERTICAL);

        AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(this.getContext());
        alertDialogBuilder2.setTitle("Introduce la fecha:");

        CalendarView calendarViewPrueba = new CalendarView(this.getContext());

        linearLayout2.addView(calendarViewPrueba);
        alertDialogBuilder2.setView(linearLayout2);

        calendarViewPrueba.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                String dia = dayOfMonth + "/" + month + "/" + year;

                fechaElegida = dia;
            }
        });
        alertDialogBuilder2.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                fecha = fechaElegida;
                Toast.makeText(getContext(), "Fecha cambiada", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder2.setNegativeButton("Cancelar", null);

        alertDialogBuilder2.show();
    }
}
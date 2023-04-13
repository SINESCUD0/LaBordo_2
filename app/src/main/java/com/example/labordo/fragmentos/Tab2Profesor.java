package com.example.labordo.fragmentos;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labordo.R;
import com.example.labordo.objetos.ActividadesVo;
import com.example.labordo.recyclerview.AdapterDatos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Tab2Profesor extends Fragment {

    ActivityResultContracts.PickVisualMedia selectorImagen;
    ArrayList<ActividadesVo> listDatos;
    RecyclerView recycler;
    FloatingActionButton add;
    String nombre;
    String descripcion;
    String precio;
    String fecha = "";
    int actividad;
    String fechaElegida;

    Uri imagenUri;
    Button botonFecha, botonFoto;
    EditText nombreTarea, descripcionTarea, precioTarea;
    TextView fechaFinal;
    ImageView fotoTarea;
    RadioGroup grupo;
    RadioButton asignada1, inactiva1, sinAsignar1;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingPermission")
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: " + uri);
                    fotoTarea.setImageURI(uri);
                    imagenUri = uri;
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.tab2_profesor, container, false);
        listDatos = new ArrayList<>();
        recycler = (RecyclerView) vista.findViewById(R.id.TareasAsignadas2);
        add = (FloatingActionButton) vista.findViewById(R.id.botonTarea1);
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

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this.getContext());

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.ventana_alerta, null);
        dialogBuilder.setView(dialogView);

        botonFecha = (Button) dialogView.findViewById(R.id.botonFecha);
        nombreTarea = (EditText) dialogView.findViewById(R.id.editTextNombreTarea);
        descripcionTarea = (EditText) dialogView.findViewById(R.id.editTextDescripcionTarea);
        precioTarea = (EditText) dialogView.findViewById(R.id.editTextPrecio);
        fechaFinal = (TextView) dialogView.findViewById(R.id.fecha);
        fotoTarea = (ImageView) dialogView.findViewById(R.id.imagenTarea);
        botonFoto = (Button) dialogView.findViewById(R.id.botonImagen);
        grupo = (RadioGroup) dialogView.findViewById(R.id.radioGroup);
        asignada1 = (RadioButton) dialogView.findViewById(R.id.asignada);
        inactiva1 = (RadioButton) dialogView.findViewById(R.id.inactiva);
        sinAsignar1 = (RadioButton) dialogView.findViewById(R.id.sinAsignar);


        dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nombre = nombreTarea.getText().toString();
                descripcion = descripcionTarea.getText().toString();
                precio = precioTarea.getText().toString();
                actividad = grupo.getCheckedRadioButtonId();
                fechaElegida = fechaFinal.toString();

                boolean asignada = asignada1.isChecked();
                boolean inactiva = inactiva1.isChecked();
                boolean sinAsignar = sinAsignar1.isChecked();
                int circulo = 0;

                if (asignada != false) {
                    circulo = R.drawable.asignada;
                } else if (inactiva != false) {
                    circulo = R.drawable.inactivo;
                } else if (sinAsignar != false) {
                    circulo = R.drawable.pendiente;
                }

                if (!nombre.equals("") && !descripcion.equals("") && !precio.equals("") && !fechaElegida.equals("")) {
                    listDatos.add(new ActividadesVo(nombre, descripcion, imagenUri, precio, fecha, circulo));
                    AdapterDatos adapter = new AdapterDatos(listDatos);
                    recycler.setAdapter(adapter);
                } else if (nombre.equals("") || descripcion.equals("") || precio.equals("") || fechaElegida.equals("")) {
                    Toast.makeText(getContext(), "Introduce todos los datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancelar", null);
        botonFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout2 = new LinearLayout(getContext());
                linearLayout2.setOrientation(LinearLayout.VERTICAL);

                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(getContext());
                alertDialogBuilder2.setTitle("Introduce la fecha:");

                CalendarView calendarViewPrueba = new CalendarView(getContext());

                linearLayout2.addView(calendarViewPrueba);
                alertDialogBuilder2.setView(linearLayout2);

                calendarViewPrueba.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                    @Override
                    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                        String dia = dayOfMonth + "/" + month + "/" + year;

                        fecha = dia;
                    }
                });
                alertDialogBuilder2.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fechaFinal.setText(fecha);
                        Toast.makeText(getContext(), "Fecha cambiada", Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialogBuilder2.setNegativeButton("Cancelar", null);

                alertDialogBuilder2.show();
            }
        });
        botonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent permisosMultimedia = new Intent(MediaStore.ACTION_PICK_IMAGES);
                mStartForResult.launch(permisosMultimedia);
                Toast.makeText(getContext(), "asd", Toast.LENGTH_SHORT).show();
                fotoTarea.setImageURI(permisosMultimedia.getData());*/
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }
    public void eliminar(){

    }

}
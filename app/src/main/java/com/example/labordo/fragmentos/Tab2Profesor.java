package com.example.labordo.fragmentos;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
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
import com.example.labordo.objetos.Profesorado;
import com.example.labordo.recyclerview.AdapterDatos;
import com.example.labordo.recyclerview.AdapterProfesorado;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Tab2Profesor extends Fragment {

    //PARA CONECTARTE A LA BASE DE DATOS CAMBIAR CADA VEZ QUE SE ENCIENDA EL SERVIDOR LA IP
    private static final String DATABASE_URL = "jdbc:mysql://192.168.1.41:3306/labordo?useUnicode=true&characterEncoding=UTF-8\"";

    //USUARIO PARA INICIAR SESION EN LA BASE DE DATOS
    private static final String USER = "root";

    //CONTRASEÑA PARA INICIAR SESION EN EL USUARIO ROOT
    private static final String PASSWORD = "L4b0rd0#";

    ActivityResultContracts.PickVisualMedia selectorImagen;
    ArrayList<ActividadesVo> listDatos;
    RecyclerView recycler;
    FloatingActionButton add;
    String nombre, descripcion, precio, fechaElegida;
    String fecha = "";
    int actividad;

    Uri imagenUri;
    byte[] imagenBytes;
    Button botonFoto;
    EditText nombreTarea, descripcionTarea, precioTarea;
    TextView fechaFinal, botonFecha;
    ImageView fotoTarea;
    RadioGroup grupo;
    RadioButton asignada1, inactiva1, sinAsignar1;

    //AYMAN AQUI TENEMOS QUE RECIBIR EL DNI DEL PROFESOR Y AÑADIRLO EN ESTA VARIABLE
    String dniProfesor;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        RecibirLabores recibirLabores= new RecibirLabores();
        recibirLabores.execute();
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

        botonFecha = (TextView) dialogView.findViewById(R.id.textFechaEntrega);
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
                    //listDatos.add(new ActividadesVo(nombre, descripcion, imagenUri, precio, fecha));
                    //AdapterDatos adapter = new AdapterDatos(listDatos);
                    //recycler.setAdapter(adapter);
                    EnvioLabores labores = new EnvioLabores();
                    labores.execute();
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
                        final int mesActual = month + 1;
                        //FORMATEO EL DIA Y EL MES SI ES MENOR DE 10, ES DECIR, SI ELEGIMOS EL MES DE FEBRERO
                        //NOS MOSTRARA EL MES FORMATEADO EN VEZ DE SOLO 2 NOS SALDRA COMO 02, ESTO SE APLICA IGUAL
                        //AL DIA
                        String diaFormateado = (dayOfMonth < 10)? "0" + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                        String mesFormateado = (mesActual < 10)? "0" + String.valueOf(mesActual):String.valueOf(mesActual);

                        String dia = diaFormateado + "/" + mesFormateado + "/" + year;
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
    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    //PARA AÑADIR LAS LABORES A LA BASE DE DATOS
    class EnvioLabores extends AsyncTask<Void, Void, Void> {

        //ESTA VARIABLE (MSG) LA UTILIZAMOS PARA EN CASO DE FALLO TE MUESTRE EN EL TOAST EL FALLO QUE DA
        String msg = "";

        @Override
        protected void onPreExecute(){
            Toast.makeText(getContext(),"Añadiendo", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Class.forName("com.mysql.jdbc.Driver"); //PILLAMOS LA INFORMACION DEL PAQUETE
                Connection conn = DriverManager.getConnection(DATABASE_URL,USER, PASSWORD); //NOS CONECTAMOS A LA BASE DE DATOS
                if(conn == null){
                    //SI NO CONSIGUES CONECTARTE A LA BASE DE DATOS
                    msg = "Se ha perdido la conexion";
                }else{
                    //SI CONSIGUE CONECTARSE A LA BASE DE DATOS QUE EJECUTE LA SIGUIENTE SENTENCIA
                    String query = "INSERT INTO labores (nombreActividad, precio, descripcion, imagenTarea, fechaLimite, estado)" +
                            " VALUES(?, ?, ?, ?, ?, 'LIBRE')";
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = formatter.parse(fecha);
                    java.sql.Date fechaElegidaSQL = new java.sql.Date(date.getTime());
                    ContentResolver contentResolver = getActivity().getContentResolver();
                    InputStream inputStream = contentResolver.openInputStream(imagenUri);
                    imagenBytes = getBytes(inputStream);
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, nombre);
                    statement.setString(2, precio);
                    statement.setString(3, descripcion);
                    statement.setBytes(4, imagenBytes);
                    statement.setString(5, String.valueOf(fechaElegidaSQL));
                    statement.executeUpdate();

                    listDatos.add(new ActividadesVo(nombre, descripcion, imagenUri, precio, fecha));

                    msg = "Tarea añadida";

                    statement.close();
                    inputStream.close();
                }

                conn.close();

            }catch (Exception e){
                //SI FALLA ALGO EN EL CODIGO SALDRA LA SIGUIENTE EXCEPTION
                msg = e.getMessage();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            AdapterDatos adapterDatos = new AdapterDatos(listDatos);
            recycler.setAdapter(adapterDatos);
        }
    }

    //PARA RECIBIR LAS LABORES QUE TIENE EL PROFESOR
    class RecibirLabores extends AsyncTask<Void, Void, Void> {

        //ESTA VARIABLE (MSG) LA UTILIZAMOS PARA EN CASO DE FALLO TE MUESTRE EN EL TOAST EL FALLO QUE DA
        String msg = "";

        @Override
        protected void onPreExecute(){
            Toast.makeText(getContext(),"Actualizando", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Class.forName("com.mysql.jdbc.Driver"); //PILLAMOS LA INFORMACION DEL PAQUETE
                Connection conn = DriverManager.getConnection(DATABASE_URL,USER, PASSWORD); //NOS CONECTAMOS A LA BASE DE DATOS
                if(conn == null){
                    //SI NO CONSIGUES CONECTARTE A LA BASE DE DATOS
                    msg = "Se ha perdido la conexion";
                }else{
                    //SI CONSIGUE CONECTARSE A LA BASE DE DATOS QUE EJECUTE LA SIGUIENTE SENTENCIA
                    String query = "SELECT nombreActividad, precio, descripcion, imagenTarea, fechaLimite" +
                            " FROM labores WHERE dni_profesor = ?";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, dniProfesor);
                    ResultSet rs = statement.executeQuery();

                }

                conn.close();

            }catch (Exception e){
                //SI FALLA ALGO EN EL CODIGO SALDRA LA SIGUIENTE EXCEPTION
                msg = e.getMessage();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            AdapterDatos adapterDatos = new AdapterDatos(listDatos);
            recycler.setAdapter(adapterDatos);
        }
    }

}
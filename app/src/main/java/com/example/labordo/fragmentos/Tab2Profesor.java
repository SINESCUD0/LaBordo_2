package com.example.labordo.fragmentos;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labordo.R;
import com.example.labordo.activity.Perfil;
import com.example.labordo.objetos.ActividadesVo;
import com.example.labordo.objetos.LoginInfo;
import com.example.labordo.recyclerview.AdapterDatos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

    ImageView fotoPerfil;
    ArrayList<ActividadesVo> listDatos;
    RecyclerView recycler;
    FloatingActionButton add;
    String nombre, descripcion, precio, fechaElegida;
    String fecha = "";

    Uri imagenUri;
    byte[] imagenBytes;
    Button botonFoto;
    EditText nombreTarea, descripcionTarea, precioTarea;
    TextView fechaFinal, botonFecha;
    ImageView fotoTarea;
    SwipeRefreshLayout refresh;
    LoginInfo profesor = new LoginInfo();




    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        RecibirLabores recibirLabores= new RecibirLabores();
        recibirLabores.execute();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.tab2_profesor, container, false);
        listDatos = new ArrayList<>();
        recycler = vista.findViewById(R.id.TareasAsignadas2);
        add = vista.findViewById(R.id.botonTarea1);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        refresh = vista.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listDatos.clear();
                RecibirLabores objSend = new RecibirLabores();
                objSend.execute();
                refresh.setRefreshing(false);
            }
        });
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

        botonFecha = dialogView.findViewById(R.id.textFechaEntrega);
        nombreTarea = dialogView.findViewById(R.id.editTextNombreTarea);
        descripcionTarea = dialogView.findViewById(R.id.editTextDescripcionTarea);
        precioTarea = dialogView.findViewById(R.id.editTextPrecio);
        fechaFinal = dialogView.findViewById(R.id.fecha);
        fotoTarea = dialogView.findViewById(R.id.imagenTarea);
        botonFoto = dialogView.findViewById(R.id.botonImagen);


        dialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nombre = nombreTarea.getText().toString();
                descripcion = descripcionTarea.getText().toString();
                precio = precioTarea.getText().toString();
                fechaElegida = fechaFinal.toString();

                if (!nombre.equals("") && !descripcion.equals("") && !precio.equals("") && !fechaElegida.equals("")) {
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
        protected Void doInBackground(Void... voids) {
            try{
                Class.forName("com.mysql.jdbc.Driver"); //PILLAMOS LA INFORMACION DEL PAQUETE
                Connection conn = DriverManager.getConnection(getResources().getString(R.string.DATABASE_URL),
                        getResources().getString(R.string.USER),
                        getResources().getString(R.string.PASSWORD)); //NOS CONECTAMOS A LA BASE DE DATOS
                if(conn == null){
                    //SI NO CONSIGUES CONECTARTE A LA BASE DE DATOS
                    msg = "Se ha perdido la conexion";
                }else{
                    //SI CONSIGUE CONECTARSE A LA BASE DE DATOS QUE EJECUTE LA SIGUIENTE SENTENCIA
                    String dni = profesor.getDni();
                    String institutoProfesor = profesor.getInstitutoLogin();
                    String query = "INSERT INTO labores (nombreActividad, precio, descripcion, imagenTarea, fechaLimite, estado, instituto, dni_profesor)" +
                            " VALUES(?, ?, ?, ?, ?, 'LIBRE', ?, ?)";
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
                    statement.setString(6, institutoProfesor);
                    statement.setString(7, dni);
                    statement.executeUpdate();
                    Uri imageUri = Uri.parse("android.resource://com.example.labordo/" + R.drawable.inactivo);

                    listDatos.add(new ActividadesVo(nombre, descripcion, imagenUri, precio, fecha, imageUri));

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
            //Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            AdapterDatos adapterDatos = new AdapterDatos(listDatos);
            recycler.setAdapter(adapterDatos);
        }
    }

    //PARA RECIBIR LAS LABORES QUE TIENE EL PROFESOR
    class RecibirLabores extends AsyncTask<Void, Void, Void> {

        //ESTA VARIABLE (MSG) LA UTILIZAMOS PARA EN CASO DE FALLO TE MUESTRE EN EL TOAST EL FALLO QUE DA
        String msg = "";

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Class.forName("com.mysql.jdbc.Driver"); //PILLAMOS LA INFORMACION DEL PAQUETE
                Connection conn = DriverManager.getConnection(getResources().getString(R.string.DATABASE_URL),
                        getResources().getString(R.string.USER),
                        getResources().getString(R.string.PASSWORD)); //NOS CONECTAMOS A LA BASE DE DATOS
                if(conn == null){
                    //SI NO CONSIGUES CONECTARTE A LA BASE DE DATOS
                    msg = "Se ha perdido la conexion";
                }else{
                    String dni = profesor.getDni();
                    int numero = 1;
                    //SI CONSIGUE CONECTARSE A LA BASE DE DATOS QUE EJECUTE LA SIGUIENTE SENTENCIA
                    String query = "SELECT * FROM labores WHERE dni_profesor = ?";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, dni);
                    ResultSet rs = statement.executeQuery();

                    while(rs.next()){
                        String nombre = rs.getString("nombreActividad");
                        String precio = String.valueOf(rs.getInt("precio"));
                        String descripcion = rs.getString("descripcion");
                        Blob imageValue = rs.getBlob("imagenTarea");
                        String fechaLimite = String.valueOf(rs.getDate("fechaLimite"));
                        String estado = rs.getString("estado");
                        numero++;
                        if(imageValue == null){
                            Uri imageUri = Uri.parse("android.resource://com.example.labordo/" + R.drawable.sin_foto);
                            if(estado.equals("LIBRE")){
                                Uri imageUri2 = Uri.parse("android.resource://com.example.labordo/" + R.drawable.inactivo);
                                listDatos.add(new ActividadesVo(nombre, descripcion, imageUri, precio, fechaLimite, imageUri2));
                            } else if (estado.equals("RESUELTA")) {
                                Uri imageUri2 = Uri.parse("android.resource://com.example.labordo/" + R.drawable.asignada);
                                listDatos.add(new ActividadesVo(nombre, descripcion, imageUri, precio, fechaLimite, imageUri2));
                            } else if (estado.equals("CONFIRMADA")) {
                                Uri imageUri2 = Uri.parse("android.resource://com.example.labordo/" + R.drawable.pendiente);
                                listDatos.add(new ActividadesVo(nombre, descripcion, imageUri, precio, fechaLimite, imageUri2));
                            }
                        }else{
                            // Obtener la URI del archivo temporal
                            byte[] blobBytes = imageValue.getBytes(1, (int) imageValue.length());

                            Bitmap bitmap = BitmapFactory.decodeByteArray(blobBytes, 0, blobBytes.length);

                            File file = File.createTempFile("image"+numero, ".jpg", getContext().getCacheDir());
                            FileOutputStream fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                            fos.close();

                            Uri uri = Uri.fromFile(file);
                            if(estado.equals("LIBRE")){
                                Uri imageUri2 = Uri.parse("android.resource://com.example.labordo/" + R.drawable.inactivo);
                                listDatos.add(new ActividadesVo(nombre, descripcion, uri, precio, fechaLimite, imageUri2));
                            } else if (estado.equals("RESUELTA")) {
                                Uri imageUri2 = Uri.parse("android.resource://com.example.labordo/" + R.drawable.asignada);
                                listDatos.add(new ActividadesVo(nombre, descripcion, uri, precio, fechaLimite, imageUri2));
                            } else if (estado.equals("CONFIRMADA")) {
                                Uri imageUri2 = Uri.parse("android.resource://com.example.labordo/" + R.drawable.pendiente);
                                listDatos.add(new ActividadesVo(nombre, descripcion, uri, precio, fechaLimite, imageUri2));
                            }
                        }
                    }


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
            //Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            AdapterDatos adapterDatos = new AdapterDatos(listDatos);
            recycler.setAdapter(adapterDatos);
        }
    }
}
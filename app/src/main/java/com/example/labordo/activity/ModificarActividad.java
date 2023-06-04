package com.example.labordo.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labordo.R;
import com.example.labordo.objetos.LoginInfo;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ModificarActividad extends AppCompatActivity {

    String fechaSeleccionada;

    LoginInfo logininfo = new LoginInfo();
    ImageView imagenTarea;
    EditText titulo;
    EditText precio;
    TextView fecha;
    EditText descripcion;

    Button boton;

    String desc;



    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {

                if (uri != null){

                    Log.d("PhotoPicker", "Selected URI: " + uri);

                    File archivo = new File(uri.getPath());

                    // Miramos si la imagen elegida cumple con el tamaño recomendado (512 x 512 pixeles)
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(archivo.getAbsolutePath(), options);
                    int imageHeight = options.outHeight;
                    int imageWidth = options.outWidth;

                    //Vemos cuanto pesa la imagen
                    AssetFileDescriptor descriptorArchivo;
                    try {
                        descriptorArchivo = getApplicationContext().getContentResolver().openAssetFileDescriptor(uri, "r");
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }


                    if(((imageHeight <= 650 || imageHeight >= 150) && (imageWidth <= 650 || imageWidth >= 150))
                            && (descriptorArchivo.getLength()/1024) < 65){

                        imagenTarea.setImageURI(uri);

                        //ActualizarInfo actualizarInfo = new ActualizarInfo();
                        //actualizarInfo.execute();
                    }else {
                        Toast.makeText(this, "No es una imagen válida", Toast.LENGTH_LONG).show();
                    }


                }




            });




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_actividad);

        imagenTarea = ((ImageView) findViewById(R.id.modifImagenTarea));
        titulo = ((EditText) findViewById(R.id.modifTitulo));
        precio = ((EditText) findViewById(R.id.modifPrecio));
        fecha = ((TextView) findViewById(R.id.modifFecha));
        descripcion = ((EditText) findViewById(R.id.modifDesc));
        boton = ((Button) findViewById(R.id.modifBoton));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.azulClaro)));


        // Cojo Bytes de la Imagen y los transformo
        byte[] datosImagen = getIntent().getByteArrayExtra("imagen");
        Bitmap bitmap = BitmapFactory.decodeByteArray(datosImagen, 0, datosImagen.length);


        imagenTarea.setImageBitmap(bitmap);
        titulo.setText(getIntent().getStringExtra("titulo"));
        precio.setText(getIntent().getStringExtra("precio"));
        fecha.setText(getIntent().getStringExtra("fecha"));

        desc = getIntent().getStringExtra("descripcion"); // Lo guardo en un String para que no se altere el valor de ninguna forma

        descripcion.setText(desc);




        fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LinearLayout linearLayout2 = new LinearLayout(getApplicationContext());
                linearLayout2.setOrientation(LinearLayout.VERTICAL);

                AlertDialog.Builder alertDialogBuilder2 = new AlertDialog.Builder(ModificarActividad.this);
                alertDialogBuilder2.setTitle("Introduce la fecha:");

                CalendarView calendarViewPrueba = new CalendarView(ModificarActividad.this);

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

                        String dia = year+"-"+mesFormateado + "-"+diaFormateado;
                        fechaSeleccionada = dia;
                    }
                });
                alertDialogBuilder2.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fecha.setText(fechaSeleccionada);
                    }
                });
                alertDialogBuilder2.setNegativeButton("Cancelar", null);

                alertDialogBuilder2.show();
            }
        });

        imagenTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActualizarInfo actualizarInfo = new ActualizarInfo();
                actualizarInfo.execute();
            }
        });

    }

    //PARA CREAR EL MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setTitle("");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_resto, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //PARA ELEGIR LAS OPCIONES DEL MENU
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.back:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }










    public class ActualizarInfo extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {



            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(getResources().getString(R.string.DATABASE_URL),
                        getResources().getString(R.string.USER),
                        getResources().getString(R.string.PASSWORD)); //NOS CONECTAMOS A LA BASE DE DATOS

                if(conn != null){

                    Bitmap fotoBitmap = ((BitmapDrawable) imagenTarea.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    fotoBitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);
                    byte[] byteArray = stream.toByteArray();


                    Blob blob = conn.createBlob();
                    blob.setBytes(1, byteArray);

                    // Estoy buscando por Descripcion... habria que buscar por Clave Primaria
                    String query = "update labores set nombreActividad = ?, precio = ?, descripcion = ?, imagenTarea = ?, fechaLimite = ?  where descripcion = ?";



                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, titulo.getText().toString());
                    statement.setString(2, precio.getText().toString());
                    statement.setString(3, descripcion.getText().toString());
                    statement.setBlob(4, blob);
                    statement.setString(5, fecha.getText().toString());
                    statement.setString(6, desc);
                    /*statement.setBlob(1, blob);
                    statement.setString(2, logininfo.getDni());
                     */
                    statement.executeUpdate();

                    statement.close();
                    finish();
                }

                conn.close();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            return null;
        }
    }
}
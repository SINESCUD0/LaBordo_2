package com.example.labordo.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labordo.R;
import com.example.labordo.objetos.Alumnado;
import com.example.labordo.objetos.LoginInfo;
import com.example.labordo.objetos.Profesorado;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Perfil extends AppCompatActivity {

    Button botonFoto;
    ImageView fotoPerfil;
    Uri imagenUri;
    TextView tipoDeCuenta, dni, correo;

    LoginInfo logininfo = new LoginInfo();

    @SuppressLint("MissingPermission")
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {

                if (uri != null){

                    Log.d("PhotoPicker", "Selected URI: " + uri);

                    File archivo = new File(uri.getPath());

                    // Miramos si la imagen elegida cumple con el tamaño recomendado
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


                    if((imageHeight <= 650 || imageHeight >= 150) && (imageWidth <= 650 || imageWidth >= 150)
                        && (descriptorArchivo.getLength()/1024) <= 66){

                        fotoPerfil.setImageURI(uri);

                        ActualizarFoto fotoRenovada = new ActualizarFoto();
                        fotoRenovada.execute();
                    }else {
                        Toast.makeText(this, "No es una imagen válida", Toast.LENGTH_LONG).show();
                    }


                }




            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);
        this.getSupportActionBar().hide();

        botonFoto = findViewById(R.id.botonFoto);
        fotoPerfil = (ImageView) findViewById(R.id.fotoPerfilUsuario);
        tipoDeCuenta = (TextView)findViewById(R.id.tipoDeCuenta);
        dni = (TextView) findViewById(R.id.DNIdelUsuario);
        correo = (TextView) findViewById(R.id.correoDelUsuario);


        if(logininfo.isTipoCuenta()){
            tipoDeCuenta.setText("Profesor");
        }else {
            tipoDeCuenta.setText("Alumno");
        }

        dni.setText(logininfo.getDni());
        correo.setText(logininfo.getCorreo());


        if(logininfo.getImagenPerfil() != null){
            byte[] byteArray;
            Bitmap bm;

            try {
                byteArray = logininfo.getImagenPerfil().getBytes(1, (int)logininfo.getImagenPerfil().length());
                bm = BitmapFactory.decodeByteArray(byteArray,0 , byteArray.length);
                fotoPerfil.setImageBitmap(bm);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


        Toast.makeText(Perfil.this, "a", Toast.LENGTH_SHORT).show();


        botonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Perfil.this, "Foto", Toast.LENGTH_SHORT).show();
                subirFoto();
            }
        });
    }

    private void subirFoto() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }






    public class ActualizarFoto extends AsyncTask<Void, Void, Void>{


        @Override
        protected Void doInBackground(Void... voids) {



            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(getResources().getString(R.string.DATABASE_URL),
                        getResources().getString(R.string.USER),
                        getResources().getString(R.string.PASSWORD)); //NOS CONECTAMOS A LA BASE DE DATOS

                if(conn != null){

                    Bitmap fotoBitmap = ((BitmapDrawable) fotoPerfil.getDrawable()).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    fotoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();


                    Blob blob = conn.createBlob();
                    blob.setBytes(1, byteArray);

                    logininfo.setImagenPerfil(blob);

                    String query;

                    if(logininfo.isTipoCuenta()){
                        query = "update profesor set fotoPerfil = ? where dni = ?;";
                    }else {
                        query = "update estudiante set fotoPerfil = ? where dni = ?;";
                    }

                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setBlob(1, blob);
                    statement.setString(2, logininfo.getDni());
                    statement.executeUpdate();

                    statement.close();

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
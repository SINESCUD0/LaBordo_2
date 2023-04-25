package com.example.labordo.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
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
import java.sql.SQLException;

public class Perfil extends AppCompatActivity {

    //PARA CONECTARTE A LA BASE DE DATOS CAMBIAR CADA VEZ QUE SE ENCIENDA EL SERVIDOR LA IP
    private static final String DATABASE_URL = "jdbc:mysql://192.168.1.45:3306/labordo?useUnicode=true&characterEncoding=UTF-8\"";

    //USUARIO PARA INICIAR SESION EN LA BASE DE DATOS
    private static final String USER = "root";

    //CONTRASEÑA PARA INICIAR SESION EN EL USUARIO ROOT
    private static final String PASSWORD = "L4b0rd0#";

    Button botonFoto;
    ImageView fotoPerfil;
    Uri imagenUri;
    TextView tipoDeCuenta, dni, correo;

    /*
    Alumnado alumno = new Alumnado();
    Profesorado profesor = new Profesorado();
     */

    LoginInfo logininfo = new LoginInfo();

    @SuppressLint("MissingPermission")
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
            registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    Log.d("PhotoPicker", "Selected URI: " + uri);

                    fotoPerfil.setImageURI(uri);
                    byte[] bArray;


                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        bArray = bos.toByteArray();


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                    try {
                        Class.forName("com.mysql.jdbc.Driver"); //PILLAMOS LA INFORMACION DEL PAQUETE
                        Connection conn = DriverManager.getConnection(DATABASE_URL,USER, PASSWORD); //NOS CONECTAMOS A LA BASE DE DATOS
                        String query;

                        if(logininfo.isTipoCuenta()){
                            query = "update profesor set fotoPerfil = ? where dni = '"+logininfo.getDni()+"'";
                            PreparedStatement statement = conn.prepareStatement(query);
                            statement.setBinaryStream(1, new ByteArrayInputStream(bArray), bArray.length);

                            statement.executeUpdate();
                            conn.close();
                        }

                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                        // Peta aquí
                    }


                    /*
                    Drawable fotoDrawable = fotoPerfil.getDrawable();
                    Bitmap fotoBitmap = ((BitmapDrawable) fotoDrawable).getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    fotoBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    fotoBitmap.recycle();


                    try {
                        logininfo.getImagenPerfil().setBytes(1, byteArray);
                        fotoPerfil.setImageBitmap(fotoBitmap);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }*/
                } else {
                    Log.d("PhotoPicker", "No media selected");
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



        botonFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Perfil.this, "Foto", Toast.LENGTH_SHORT).show();
                subirFoto();
            }
        });
    }

    private void subirFoto(){
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }
}
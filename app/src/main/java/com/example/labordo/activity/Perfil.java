package com.example.labordo.activity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
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

import java.io.InputStream;
import java.sql.SQLException;

public class Perfil extends AppCompatActivity {

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
                    imagenUri = uri;
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);

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
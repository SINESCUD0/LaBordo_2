package com.example.labordo.activity;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.labordo.R;
import com.example.labordo.objetos.LoginInfo;

import java.sql.SQLException;

public class Setting extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        this.getSupportActionBar().hide();

        LoginInfo loginInfo = new LoginInfo();
        ImageView fotoPerfil = findViewById(R.id.fotoPerfil);

        if(loginInfo.getImagenPerfil() != null){
            byte[] byteArray;
            Bitmap bm;

            try {
                byteArray = loginInfo.getImagenPerfil().getBytes(1, (int)loginInfo.getImagenPerfil().length());
                bm = BitmapFactory.decodeByteArray(byteArray,0 , byteArray.length);
                fotoPerfil.setImageBitmap(bm);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        findViewById(R.id.perfil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setting.this, Perfil.class));
            }
        });

        findViewById(R.id.cambiarPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Setting.this, CambiarPassword.class));
            }
        });


        }
}
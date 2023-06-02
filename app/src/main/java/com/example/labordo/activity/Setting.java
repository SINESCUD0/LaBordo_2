package com.example.labordo.activity;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.labordo.R;
import com.example.labordo.objetos.LoginInfo;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

public class Setting extends AppCompatActivity {

    LoginInfo loginInfo = new LoginInfo();
    TextView saldoCuenta;

    @Override
    protected void onResume() {
        super.onResume();


        // Te muestra el saldo si eres Estudiante. Si eres profe te pone el "Hola"
        if (!loginInfo.isTipoCuenta()) {
            VerSaldoActual vsa = new VerSaldoActual();

            vsa.execute();

            saldoCuenta = findViewById(R.id.saldoUsuario);
            saldoCuenta.setText(String.valueOf(loginInfo.getSaldoCuenta()));
        }


        LoginInfo loginInfo = new LoginInfo();
        ImageView fotoPerfil = findViewById(R.id.fotoPerfil);

        if (loginInfo.getImagenPerfil() != null) {
            byte[] byteArray;
            Bitmap bm;

            try {
                byteArray = loginInfo.getImagenPerfil().getBytes(1, (int) loginInfo.getImagenPerfil().length());
                bm = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.azulClaro)));

        //this.getSupportActionBar().hide();
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

    public class VerSaldoActual extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(getResources().getString(R.string.DATABASE_URL),
                        getResources().getString(R.string.USER),
                        getResources().getString(R.string.PASSWORD)); //NOS CONECTAMOS A LA BASE DE DATOS

                if (conn != null) {


                    String query = "SELECT puntos FROM estudiante WHERE dni = ? LIMIT 1";

                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, loginInfo.getDni());

                    ResultSet rs = statement.executeQuery();
                    int saldo = 0;
                    while (rs.next()) {
                        saldo = rs.getInt(1);
                    }

                    loginInfo.setSaldoCuenta(saldo);
                    /*
                    PreparedStatement statement = conn.prepareStatement(query);
                    ResultSet rs = statement.executeQuery();

                     */

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
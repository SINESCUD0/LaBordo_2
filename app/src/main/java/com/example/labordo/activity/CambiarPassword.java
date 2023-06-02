package com.example.labordo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.labordo.R;
import com.example.labordo.objetos.LoginInfo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CambiarPassword extends AppCompatActivity {

    String pass1;
    String pass2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cambiar_password);
        //this.getSupportActionBar().hide();
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.azulClaro)));



        findViewById(R.id.botonAceptarPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeTeclado();
                EditText et1 = (EditText) findViewById(R.id.passwordNueva);
                ;
                EditText et2 = (EditText) findViewById(R.id.passwordNueva2);

                pass1 = et1.getText().toString();
                pass2 = et2.getText().toString();

                if (pass1.equals(pass2)) {
                    Toast.makeText(view.getContext(), "Contraseña cambiada", Toast.LENGTH_LONG).show();

                    Send send = new Send();
                    send.execute();

                    finish();
                } else {
                    Toast.makeText(view.getContext(), "No coinciden. Inténtelo de nuevo", Toast.LENGTH_LONG).show();
                }
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


    class Send extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {

            Send send = new Send();
            try {
                Class.forName("com.mysql.jdbc.Driver"); //PILLAMOS LA INFORMACION DEL PAQUETE
                Connection conn = DriverManager.getConnection(getResources().getString(R.string.DATABASE_URL),
                        getResources().getString(R.string.USER),
                        getResources().getString(R.string.PASSWORD)); //NOS CONECTAMOS A LA BASE DE DATOS

                if (conn != null) {

                    LoginInfo info = new LoginInfo();
                    if (info.isTipoCuenta()) {
                        String update = "UPDATE profesor SET contrasenia = \"" + pass1 + "\" WHERE correo = ? AND dni = ?";
                        PreparedStatement statement = conn.prepareStatement(update);
                        statement.setString(1, info.getCorreo());
                        statement.setString(2, info.getDni());
                        statement.executeUpdate();
                    }

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

    public void closeTeclado() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
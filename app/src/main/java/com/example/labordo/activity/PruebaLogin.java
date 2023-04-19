package com.example.labordo.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labordo.R;
import com.example.labordo.usuarios.Main_Alumnado;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PruebaLogin extends AppCompatActivity {
    private static final String DATABASE_URL = "jdbc:mysql://192.168.1.36:3306/labordo?useUnicode=true&characterEncoding=UTF-8\"";
    private static final String USER = "root";
    private static final String PASSWORD = "L4b0rd0#";
    EditText correoUsuario, passwordUsuario;
    Button iniciar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        correoUsuario = (EditText) findViewById(R.id.correoUsuario);
        passwordUsuario = (EditText) findViewById(R.id.passwordUsuario);
        iniciar = findViewById(R.id.login);
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send objSend = new Send();
                objSend.execute();
            }
        });
    }

    class Send extends AsyncTask<Void, Void, Void> {

        String msg = "";
        String correo1 = correoUsuario.getText().toString();
        String password1 = passwordUsuario.getText().toString();

        @Override
        protected void onPreExecute(){
            Toast.makeText(PruebaLogin.this,"Iniciando Sesion", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DATABASE_URL,USER, PASSWORD);
                if(conn == null){
                    msg = "La conexion va mal";
                }else{
                    String query = "SELECT * FROM estudiante WHERE correo = ? AND contrasenia = ?";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, correo1);
                    statement.setString(2, password1);
                    ResultSet rs = statement.executeQuery();
                    msg = "¡Inicio de sesión exitoso!";
                    Intent i = new Intent(PruebaLogin.this, Main_Alumnado.class);
                    startActivity(i);
                    statement.close();
                }

                conn.close();

            }catch (Exception e){
                msg = "La conexion va mal excepcion" + e.getMessage();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            Toast.makeText(PruebaLogin.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void registrarse(View view){
        Intent i = new Intent(this, PruebaRegistro.class);
        startActivity(i);
    }
}

package com.example.labordo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.labordo.R;

import java.sql.*;

public class PruebaRegistro extends AppCompatActivity {

    private static final String DATABASE_URL = "jdbc:mysql://192.168.1.36:3306/labordo?useUnicode=true&characterEncoding=UTF-8\"";
    private static final String USER = "root";
    private static final String PASSWORD = "L4b0rd0#";
    EditText correo, password, dni;
    Button crear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        correo = findViewById(R.id.correo_Usuario);
        dni = findViewById(R.id.DNIUsuario);
        password = findViewById(R.id.password_Usuario);
        crear = findViewById(R.id.crearUsuario);

        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send objSend = new Send();
                objSend.execute("");
            }
        });
    }

    class Send extends AsyncTask<String, String, String>{

        String msg = "";
        String correo1 = correo.getText().toString();
        String dni1 = dni.getText().toString();
        String password1 = password.getText().toString();

        @Override
        protected void onPreExecute(){
            Toast.makeText(PruebaRegistro.this,"Creando usuario", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DATABASE_URL,USER, PASSWORD);
                if(conn == null){
                    msg = "La conexion va mal";
                }else{
                    String query = "INSERT INTO estudiante(correo, dni, contrasenia) VALUES(?, ?, ?)";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, correo1);
                    statement.setString(2, dni1);
                    statement.setString(3, password1);
                    statement.executeUpdate();
                    msg = "Usuario creado correctamente";
                    statement.close();
                }

                conn.close();

            }catch (Exception e){
                msg = "La conexion va mal excepcion" + e.getMessage();
                e.printStackTrace();
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String msg){
            Toast.makeText(PruebaRegistro.this,msg, Toast.LENGTH_SHORT).show();
        }
    }
}

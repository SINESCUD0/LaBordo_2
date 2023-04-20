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
import com.example.labordo.usuarios.Main_Profesorado;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginActivity extends AppCompatActivity {
    //PARA CONECTARTE A LA BASE DE DATOS
    private static final String DATABASE_URL = "jdbc:mysql://192.168.1.38:3306/labordo?useUnicode=true&characterEncoding=UTF-8\"";

    //USUARIO PARA INICIAR SESION EN LA BASE DE DATOS
    private static final String USER = "root";

    //CONTRASEÑA PARA INICIAR SESION EN EL USUARIO ROOT
    private static final String PASSWORD = "L4b0rd0#";

    //EDIT TEXT PARA PODER RECOGER LO QUE INTRODUCE EL USUARIO DENTRO DEL LAYOUT LOGIN
    EditText correoUsuario, passwordUsuario;
    Button iniciar;

    String tipo1 = "profesor";
    String tipo2 = "estudiante";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //INDICO QUE ES CORREO, PASSWORD Y EL BOTON DENTRO DEL LAYOUT LOGIN
        correoUsuario = findViewById(R.id.correoUsuario);
        passwordUsuario = findViewById(R.id.passwordUsuario);
        iniciar = findViewById(R.id.login);

        //CUANDO DEMOS AL BOTON DE INICIAR SE EJECUTARA EL SIGUIENTE METODO
        iniciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send objSend = new Send();
                objSend.execute();
                correoUsuario.setText("");
                passwordUsuario.setText("");
            }
        });
    }

    class Send extends AsyncTask<Void, Void, Void> {

        //ESTA VARIABLE (MSG) LA UTILIZAMOS PARA EN CASO DE FALLO TE MUESTRE EN EL TOAST EL FALLO QUE DA
        String msg = "";

        //COGEMOS LOS VALORES DE LOS EDIT TEXT Y LOS PASAMOS A STRING ESOS VALORES
        String correo1 = correoUsuario.getText().toString();
        String password1 = passwordUsuario.getText().toString();

        @Override
        protected void onPreExecute(){
            Toast.makeText(LoginActivity.this,"Iniciando Sesion", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Class.forName("com.mysql.jdbc.Driver"); //PILLAMOS LA INFORMACION DEL PAQUETE
                Connection conn = DriverManager.getConnection(DATABASE_URL,USER, PASSWORD); //NOS CONECTAMOS A LA BASE DE DATOS
                if(conn == null){
                    //SI NO CONSIGUES CONECTARTE A LA BASE DE DATOS
                    msg = "La conexion va mal";
                }else{
                    //SI CONSIGUE CONECTARSE A LA BASE DE DATOS QUE EJECUTE LA SIGUIENTE SENTENCIA
                    String query = "SELECT 'estudiante' as tipo FROM estudiante WHERE correo = ? " +
                            "UNION " +
                            "SELECT 'profesor' FROM profesor WHERE correo = ?";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, correo1);
                    statement.setString(2, correo1);
                    ResultSet rs = statement.executeQuery();
                    //LEE LA CONSULTA Y MIRA DE QUE TIPO ES EL CORREO
                    if (rs.next()) {
                        String tipo = rs.getString("tipo");
                        //SI EL CORREO INTRODUCIDO ES DE UN PROFESOR HARA LO SIGUIENTE
                        if(tipo.equals(tipo1)){
                            String query2 = "SELECT * FROM profesor WHERE correo = ? AND contrasenia = ?";
                            PreparedStatement statement2 = conn.prepareStatement(query2);
                            statement2.setString(1, correo1);
                            statement2.setString(2, password1);
                            ResultSet rs2 = statement2.executeQuery();

                            msg = "¡Inicio de sesión exitoso!";

                            Intent i = new Intent(LoginActivity.this, Main_Profesorado.class);
                            startActivity(i);

                            statement2.close();
                            rs2.close();

                        }
                        else{
                            msg = "Contraseña incorrecta";
                        }
                        //SI EL CORREO INTRODUCIDO ES DE UN ESTUDIANTE HARA LO SIGUIENTE
                        if (tipo.equals(tipo2)) {
                            String query2 = "SELECT * FROM estudiante WHERE correo = ? AND contrasenia = ?";
                            PreparedStatement statement2 = conn.prepareStatement(query2);
                            statement2.setString(1, correo1);
                            statement2.setString(2, password1);
                            ResultSet rs2 = statement2.executeQuery();

                            if(rs2.next()){
                                msg = "¡Inicio de sesión exitoso!";

                                Intent i = new Intent(LoginActivity.this, Main_Alumnado.class);
                                startActivity(i);
                            }
                            statement2.close();
                            rs2.close();
                        }
                        else{
                            msg = "Contraseña incorrecta";
                        }
                    } else {
                        //SI INTRODUCES MAL LOS DATOS SALDRA EL SIGUIENTE MENSAJE
                        msg = "No has introducido bien los datos";
                    }

                    statement.close();
                    rs.close();
                }

                conn.close();

            }catch (Exception e){
                //SI FALLA ALGO EN EL CODIGO SALDRA LA SIGUIENTE EXCEPTION
                msg = "La conexion va mal excepcion" + e.getMessage();
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid){
            Toast.makeText(LoginActivity.this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    //SI AUN NO TIENES UNA CUENTA AL DAR AL BOTON REGISTRAR HARA LO SIGUIENTE
    public void registrarse(View view){
        Intent i = new Intent(this, RegistroActivity.class);
        startActivity(i);
    }
}

package com.example.labordo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.labordo.R;
import com.example.labordo.objetos.LoginInfo;
import com.example.labordo.usuarios.Main_Alumnado;
import com.example.labordo.usuarios.Main_Profesorado;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    //EDIT TEXT PARA PODER RECOGER LO QUE INTRODUCE EL USUARIO DENTRO DEL LAYOUT LOGIN
    EditText correoUsuario, passwordUsuario;
    Button iniciar;

    String tipo1 = "profesor";
    String tipo2 = "estudiante";
    int color = R.color.prueba;

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
                closeTeclado();
                Toast.makeText(LoginActivity.this, "Inciciando sesion ...", Toast.LENGTH_SHORT).show();
                Handler delay = new Handler();
                delay.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Send objSend = new Send();
                        objSend.execute();
                    }
                }, 2000);
            }
        });

    }

    class Send extends AsyncTask<Void, Void, Void> {

        //ESTA VARIABLE (MSG) LA UTILIZAMOS PARA EN CASO DE FALLO TE MUESTRE EN EL TOAST EL FALLO QUE DA
        String msg = "";

        //COGEMOS LOS VALORES DE LOS EDIT TEXT Y LOS PASAMOS A STRING ESOS VALORES
        String correo1 = correoUsuario.getText().toString();
        String password1 = passwordUsuario.getText().toString();

        /*@Override
        protected void onPreExecute(){
            Toast.makeText(LoginActivity.this,"Iniciando Sesion", Toast.LENGTH_SHORT).show();
        }*/

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Class.forName("com.mysql.jdbc.Driver"); //PILLAMOS LA INFORMACION DEL PAQUETE
                Connection conn = DriverManager.getConnection(getResources().getString(R.string.DATABASE_URL),
                                                            getResources().getString(R.string.USER),
                                                            getResources().getString(R.string.PASSWORD)); //NOS CONECTAMOS A LA BASE DE DATOS
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
                            //String query2 = "SELECT * FROM profesor WHERE correo = ? AND contrasenia = ? AND acceso = 0";
                            String query2 = "SELECT * FROM profesor WHERE correo = ? AND contrasenia = ?";
                            PreparedStatement statement2 = conn.prepareStatement(query2);
                            statement2.setString(1, correo1);
                            statement2.setString(2, password1);
                            ResultSet rs2 = statement2.executeQuery();
                            if(rs2.next()){
                                msg = "¡Inicio de sesión exitoso!";
                                String update = "UPDATE profesor SET acceso = 1 WHERE correo = ? AND contrasenia = ?";
                                PreparedStatement statement4 = conn.prepareStatement(update);
                                statement4.setString(1, correo1);
                                statement4.setString(2, password1);
                                statement4.executeUpdate();
                                statement4.close();
                                Intent i = new Intent(LoginActivity.this, Main_Profesorado.class);
                                startActivity(i);
                                String dni = rs2.getString("dni");
                                String nombre = rs2.getString("nombre");
                                String apellidos = rs2.getString("apellidos");
                                String correo = rs2.getString("correo");
                                String password = rs2.getString("contrasenia");
                                String instituto = rs2.getString("instituto");
                                Blob imagen = rs2.getBlob("fotoPerfil");
                                boolean tipoCuenta = true;
                                LoginInfo info = new LoginInfo(dni, nombre, apellidos, correo, password, instituto, imagen, tipoCuenta);
                                Log.e("TAMAÑO: ", info.getInstitutoLogin());
                                correoUsuario.setText("");
                                passwordUsuario.setText("");
                            }
                            else{
                                msg = "Contraseña o correo incorrecta";
                                //color = 0xfff00000;
                            }
                            statement2.close();
                            rs2.close();

                        }
                        //SI EL CORREO INTRODUCIDO ES DE UN ESTUDIANTE HARA LO SIGUIENTE
                        else if (tipo.equals(tipo2)) {
                            //String query2 = "SELECT * FROM estudiante WHERE correo = ? AND contrasenia = ? AND acceso = 0";
                            String query2 = "SELECT * FROM estudiante WHERE correo = ? AND contrasenia = ?";
                            PreparedStatement statement2 = conn.prepareStatement(query2);
                            statement2.setString(1, correo1);
                            statement2.setString(2, password1);
                            ResultSet rs2 = statement2.executeQuery();

                            if(rs2.next()){
                                msg = "¡Inicio de sesión exitoso!";
                                String update = "UPDATE estudiante SET acceso = 1 WHERE correo = ? AND contrasenia = ?";
                                PreparedStatement statement4 = conn.prepareStatement(update);
                                statement4.setString(1, correo1);
                                statement4.setString(2, password1);
                                statement4.executeUpdate();
                                statement4.close();
                                Intent i = new Intent(LoginActivity.this, Main_Alumnado.class);
                                startActivity(i);

                                String dni = rs2.getString("dni");
                                String nombre = rs2.getString("nombre");
                                String apellidos = rs2.getString("apellidos");
                                String correo = rs2.getString("correo");
                                String password = rs2.getString("contrasenia");
                                String instituto = rs2.getString("instituto");
                                int saldoCuenta = rs2.getInt("puntos");
                                Blob imagen = rs2.getBlob("fotoPerfil");
                                boolean tipoCuenta = false;
                                new LoginInfo(dni, nombre, apellidos, correo, password, instituto, imagen, tipoCuenta, saldoCuenta);

                                correoUsuario.setText("");
                                passwordUsuario.setText("");
                            }
                            else{
                                msg = "Contraseña o correo incorrecta";
                                //color = 0xfff00000;
                            }
                            statement2.close();
                            rs2.close();
                        }
                    } else {
                        //SI INTRODUCES MAL LOS DATOS SALDRA EL SIGUIENTE MENSAJE
                        msg = "No has introducido bien los datos";
                        //color = 0xfff00000;
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

    public void closeTeclado(){
        View view = this.getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

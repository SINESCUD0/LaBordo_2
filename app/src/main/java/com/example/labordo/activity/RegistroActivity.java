package com.example.labordo.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labordo.R;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RegistroActivity extends AppCompatActivity {

    Send objSend;

    List<String> listaInstitutos = new ArrayList<>();
    List<Integer> listaIdInstituto = new ArrayList<>();

    int institutoSeleccionado = 1; // 1 por si no seleccionas nada
    Spinner institutos, curso;
    EditText correo, password, dni, nombre, apellido;
    TextView fecha, fechaFinal;
    Button crear;

    //CREO LAS VARIABLES PARA PODER UTILIZARLAS EN EL METODO FECHANACIMIENTO
    int anio = 0, mes = 0, dia = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        Recieve_Institutos recieve_institutos = new Recieve_Institutos();
        try {
            recieve_institutos.execute("").get();
            // antes de seguir hacia abajo, el .get() lo que hace es esperar hasta que termine
            // de recoger los datos del Servidor (institutos) y meterlos en el array.
            // Una vez hecho, continua hacia abajo, rellenando esta vez BIEN el Spinner
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        //BUSCO LOS IDS DENTRO DEL LAYOUT REGISTRO
        nombre = findViewById(R.id.nombre_Usuario);
        apellido = findViewById(R.id.apellido_Usuario);
        correo = findViewById(R.id.correo_Usuario);
        dni = findViewById(R.id.DNIUsuario);
        password = findViewById(R.id.password_Usuario);
        fecha = findViewById(R.id.FechaNacimiento);
        fechaFinal = findViewById(R.id.FechaNacimientoRegistro);
        curso = findViewById(R.id.curso_Usuario);
        institutos = (Spinner) findViewById(R.id.instituto_spinner);
        crear = findViewById(R.id.crearUsuario);


        // cojo la informacion por el servidor. Me dio problemas, pero funciona (el fondo es blanco, feo)
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, listaInstitutos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Toast.makeText(this, listaInstitutos.size()+"", Toast.LENGTH_LONG).show();
        institutos.setAdapter(adapter);
        institutos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String palabra = adapter.getItem(i).toString()+"";
                //Toast.makeText(getApplicationContext(), palabra, Toast.LENGTH_LONG).show();
                institutos.setSelection(i);
                institutoSeleccionado = i; // guarda la Posicion que has pulsado
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        //cojo la info del XML cursos
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.cursos,
                android.R.layout.simple_spinner_item);
        curso.setAdapter(adapter2);







        //CUANDO DES AL BOTON CREAR SALTARA AL METODO Send()
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objSend = new Send();
                objSend.execute("");
                finish();
            }
        });

        //CUANDO DAS CLICK AL TEXTO FECHA NACIMIENTO SALTE EL METODO fechaNacimiento()
        fechaFinal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fechaNacimiento();
            }
        });
    }

    //CODIGO PARA PODER ELEGIR LA FECHA DE NACIMIENTO MAS FACIL AL SELECCIONAR EL AÑO
    private void fechaNacimiento(){
        Calendar ca = Calendar.getInstance();
        //RECOJO LOS VALORES DE AÑO, MES Y DIA DEL CALENDARIO
        anio = ca.get(Calendar.YEAR);
        mes = ca.get(Calendar.MONTH);
        dia = ca.get(Calendar.DAY_OF_MONTH);
        //CREO EL ALERT DIALOG DEL CALENDARIO
        DatePickerDialog recogerFecha = new DatePickerDialog(RegistroActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        final int mesActual = month + 1;
                        //FORMATEO EL DIA Y EL MES SI ES MENOR DE 10, ES DECIR, SI ELEGIMOS EL MES DE FEBRERO
                        //NOS MOSTRARA EL MES FORMATEADO EN VEZ DE SOLO 2 NOS SALDRA COMO 02, ESTO SE APLICA IGUAL
                        //AL DIA
                        String diaFormateado = (dayOfMonth < 10)? "0" + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                        String mesFormateado = (mesActual < 10)? "0" + String.valueOf(mesActual):String.valueOf(mesActual);
                        fechaFinal.setText(diaFormateado+"/"+mesFormateado+"/"+year);
                    }
                },anio, mes, dia);
        //MUESTRO EL ALERT DIALOG
        recogerFecha.show();
    }




    class Recieve_Institutos extends AsyncTask<String, String, String>{


        @Override
        protected String doInBackground(String... strings) {
            recogerInstitutos();
            return null;
        }


        protected void recogerInstitutos() {
            try {
                Class.forName("com.mysql.jdbc.Driver"); //PILLAMOS LA INFORMACION DEL PAQUETE
                Connection conn = DriverManager.getConnection(getResources().getString(R.string.DATABASE_URL),
                        getString(R.string.USER), getString(R.string.PASSWORD)); //NOS CONECTAMOS A LA BASE DE DATOS

                if(conn != null){
                    String query = "SELECT id, nombre, pais FROM instituto";
                    PreparedStatement statement = conn.prepareStatement(query);
                    ResultSet rs = statement.executeQuery();

                    while(rs.next()){
                        String nombre, pais;
                        int id;
                        id = rs.getInt(1);
                        nombre = rs.getString(2);
                        pais = rs.getString(3);
                        listaInstitutos.add(nombre+", "+pais);
                        listaIdInstituto.add(id);
                    }
                }

                conn.close();
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }

        }


    }

    class Send extends AsyncTask<String, String, String>{

        //ESTA VARIABLE (MSG) LA UTILIZAMOS PARA EN CASO DE FALLO TE MUESTRE EN EL TOAST EL FALLO QUE DA
        String msg = "";

        //COGEMOS LOS VALORES DE LOS EDITTEXT, TEXTVIEW, SPINNER Y LOS PASAMOS A STRING ESOS VALORES
        String correo1 = correo.getText().toString();
        String nombre1 = nombre.getText().toString();
        String apellido1 = apellido.getText().toString();
        String fechaNacimiento = fechaFinal.getText().toString();
        String curso1 = curso.getSelectedItem().toString();
        String instituto1 = institutos.getAdapter().toString();
        String dni1 = dni.getText().toString();
        String password1 = password.getText().toString();

        String string_instituto;




        @Override
        protected String doInBackground(String... strings) {
            try{
                Class.forName("com.mysql.jdbc.Driver"); //PILLAMOS LA INFORMACION DEL PAQUETE
                Connection conn = DriverManager.getConnection(getResources().getString(R.string.DATABASE_URL),
                        getString(R.string.USER), getString(R.string.PASSWORD)); //NOS CONECTAMOS A LA BASE DE DATOS
                if(conn == null){
                    //SI NO CONSIGUES CONECTARTE A LA BASE DE DATOS
                    msg = "La conexion va mal";
                }else{
                    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = formatter.parse(fechaNacimiento);
                    java.sql.Date fechaNacimientoSQL = new java.sql.Date(date.getTime());
                    //SI CONSIGUE CONECTARSE A LA BASE DE DATOS INTRODUCIRA LOS SIGUIENTES VALORES
                    String query = "INSERT INTO estudiante(nombre, apellidos, correo, dni, contrasenia, fecha_nacimiento," +
                            " curso, instituto) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, nombre1);
                    statement.setString(2, apellido1);
                    statement.setString(3, correo1);
                    statement.setString(4, dni1);
                    statement.setString(5, password1);
                    statement.setString(6, String.valueOf(fechaNacimientoSQL));
                    statement.setString(7, curso1);
                    statement.setString(8, String.valueOf(listaIdInstituto.get(institutoSeleccionado)));
                    //statement.setString(8, instituto1);
                    statement.executeUpdate();
                    msg = "Usuario creado correctamente";
                    statement.close();
                }
                //CERRAMOS LA CONEXION
                conn.close();

            }catch (Exception e){
                //SI DA UN FALLO DE CODIGO SALTARA ESTA EXCEPTION
                msg = "La conexion va mal excepcion" + e.getMessage();
                e.printStackTrace();
            }
            //DEVOLVEMOS EL MENSAJE
            return msg;
        }

        @Override
        protected void onPostExecute(String msg){
            //DESPUES DE LA EJECUCION SALTARA EL SIGUIENTE TOAST
            //Toast.makeText(RegistroActivity.this,msg, Toast.LENGTH_SHORT).show();
        }
    }
}

package com.example.labordo.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.labordo.R;

import java.sql.*;
import java.util.Calendar;

public class PruebaRegistro extends AppCompatActivity {

    //PARA CONECTARTE A LA BASE DE DATOS
    private static final String DATABASE_URL = "jdbc:mysql://192.168.1.36:3306/labordo?useUnicode=true&characterEncoding=UTF-8\"";

    //USUARIO PARA INICIAR SESION EN LA BASE DE DATOS
    private static final String USER = "root";

    //CONTRASEÑA PARA INICIAR SESION EN EL USUARIO ROOT
    private static final String PASSWORD = "L4b0rd0#";

    //CREO LAS VARIABLES CON LA QUE VAMOS A RECOGER LOS DATOS DEL LAYOUT
    Spinner institutos;
    EditText correo, password, dni, nombre, apellido, curso;
    TextView fecha, fechaFinal;
    Button crear;

    //CREO LAS VARIABLES PARA PODER UTILIZARLAS EN EL METODO FECHANACIMIENTO
    int anio = 0, mes = 0, dia = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        //BUSCO LOS IDS DENTRO DEL LAYOUT REGISTRO
        nombre = findViewById(R.id.nombre_Usuario);
        apellido = findViewById(R.id.apellido_Usuario);
        correo = findViewById(R.id.correo_Usuario);
        dni = findViewById(R.id.DNIUsuario);
        password = findViewById(R.id.password_Usuario);
        fecha = findViewById(R.id.FechaNacimiento);
        fechaFinal = findViewById(R.id.FechaNacimientoRegistro);
        curso = findViewById(R.id.curso_Usuario);
        institutos = findViewById(R.id.instituto_spinner);
        crear = findViewById(R.id.crearUsuario);

        //CREO EL ARRAYADAPTER PARA AÑADIR LA LISTA DE INSTITUTOS AL SPINNER
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.institutos_list,
                android.R.layout.simple_spinner_item);
        //AÑADO EL ADAPTER DENTRO DEL SPINNER
        institutos.setAdapter(adapter);

        //CUANDO DES AL BOTON CREAR SALTARA AL METODO Send()
        crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Send objSend = new Send();
                objSend.execute("");
            }
        });

        //CUANDO DAS CLICK AL TEXTO FECHA NACIMIENTO SALTE EL METODO fechaNacimiento()
        fecha.setOnClickListener(new View.OnClickListener() {
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
        DatePickerDialog recogerFecha = new DatePickerDialog(PruebaRegistro.this,
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

    class Send extends AsyncTask<String, String, String>{

        //ESTA VARIABLE (MSG) LA UTILIZAMOS PARA EN CASO DE FALLO TE MUESTRE EN EL TOAST EL FALLO QUE DA
        String msg = "";

        //COGEMOS LOS VALORES DE LOS EDITTEXT, TEXTVIEW, SPINNER Y LOS PASAMOS A STRING ESOS VALORES
        String correo1 = correo.getText().toString();
        String nombre1 = nombre.getText().toString();
        String apellido1 = apellido.getText().toString();
        String fechaNacimiento = fechaFinal.getText().toString();
        String curso1 = curso.getText().toString();
        String instituto1 = institutos.getAdapter().toString();
        String dni1 = dni.getText().toString();
        String password1 = password.getText().toString();

        @Override
        protected void onPreExecute(){
            Toast.makeText(PruebaRegistro.this,"Creando usuario", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                Class.forName("com.mysql.jdbc.Driver"); //PILLAMOS LA INFORMACION DEL PAQUETE
                Connection conn = DriverManager.getConnection(DATABASE_URL,USER, PASSWORD); //NOS CONECTAMOS A LA BASE DE DATOS
                if(conn == null){
                    //SI NO CONSIGUES CONECTARTE A LA BASE DE DATOS
                    msg = "La conexion va mal";
                }else{
                    //SI CONSIGUE CONECTARSE A LA BASE DE DATOS INTRODUCIRA LOS SIGUIENTES VALORES
                    String query = "INSERT INTO estudiante(nombre, apellidos, correo, dni, contrasenia, fecha_nacimiento," +
                            " curso, instituto) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, nombre1);
                    statement.setString(2, apellido1);
                    statement.setString(3, correo1);
                    statement.setString(4, dni1);
                    statement.setString(5, password1);
                    statement.setString(6, fechaNacimiento);
                    statement.setString(7, curso1);
                    statement.setString(8, instituto1);
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
            Toast.makeText(PruebaRegistro.this,msg, Toast.LENGTH_SHORT).show();
        }
    }
}

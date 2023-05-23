//SAMALA HAMALA

package com.example.labordo.usuarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.labordo.R;
import com.example.labordo.activity.Setting;
import com.example.labordo.objetos.Alumnado;
import com.example.labordo.objetos.LoginInfo;
import com.example.labordo.visualizador_pantalla.MiVisualizadorDePantallaProfesor;
import com.google.android.material.tabs.TabLayout;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main_Profesorado extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MiVisualizadorDePantallaProfesor miVisualizadorDePantalla;
    LoginInfo usuario = new LoginInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_profesorado);

        verificarPermisos();

        tabLayout = findViewById(R.id.Profesor);
        viewPager2 = findViewById(R.id.Vista_Pagina);
        miVisualizadorDePantalla = new MiVisualizadorDePantallaProfesor(this);
        viewPager2.setAdapter(miVisualizadorDePantalla);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public void onBackPressed(){
        cerrarSesion();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                startActivity(new Intent(this, Setting.class));
                break;
            case R.id.logout:
                cerrarSesion();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //SOLICITUD DE PERMISOS POR PANTALLA
    public void verificarPermisos(){
        int permisosAlmacenamiento = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION);

        if(permisosAlmacenamiento == PackageManager.PERMISSION_GRANTED){
            //Toast.makeText(this, "Permisos Concedidos", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Acepta los permisos", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_MEDIA_LOCATION}, 200);
            }
        }
    }

    public void cerrarSesion(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.ventana_alerta_logout, null);
        dialogBuilder.setView(dialogView);

        dialogBuilder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Send cerrar = new Send();
                cerrar.execute();
                finish();
            }
        });

        dialogBuilder.setNegativeButton("NO", null);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }

    class Send extends AsyncTask<Void, Void, Void> {

        //ESTA VARIABLE (MSG) LA UTILIZAMOS PARA EN CASO DE FALLO TE MUESTRE EN EL TOAST EL FALLO QUE DA
        String msg = "";

        //COGEMOS LOS VALORES DE LOS EDIT TEXT Y LOS PASAMOS A STRING ESOS VALORES

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
                    String correo1 = usuario.getCorreo();
                    String dni = usuario.getDni();
                    //SI EL CORREO INTRODUCIDO ES DE UN ESTUDIANTE HARA LO SIGUIENTE
                    String query2 = "SELECT * FROM profesor WHERE correo = ? AND dni = ? AND acceso = 1";
                    PreparedStatement statement2 = conn.prepareStatement(query2);
                    statement2.setString(1, correo1);
                    statement2.setString(2, dni);
                    ResultSet rs2 = statement2.executeQuery();

                    if(rs2.next()){
                        msg = "¡Cerrar sesión exitoso!";
                        String update = "UPDATE profesor SET acceso = 0 WHERE correo = ? AND dni = ?";
                        PreparedStatement statement4 = conn.prepareStatement(update);
                        statement4.setString(1, correo1);
                        statement4.setString(2, dni);
                        statement4.executeUpdate();
                        statement4.close();
                    }
                    statement2.close();
                    rs2.close();
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
            Toast.makeText(Main_Profesorado.this, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
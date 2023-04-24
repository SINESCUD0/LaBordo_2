package com.example.labordo.usuarios;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.example.labordo.R;
import com.example.labordo.activity.LoginActivity;
import com.example.labordo.activity.Setting;
import com.example.labordo.objetos.Alumnado;
import com.example.labordo.visualizador_pantalla.MiVisualizadorDePantallaAlumno;
import com.google.android.material.tabs.TabLayout;

public class Main_Alumnado extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MiVisualizadorDePantallaAlumno miVisualizadorDePantalla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_alumnado);

        verificarPermisos();

        tabLayout = findViewById(R.id.Alumno);
        viewPager2 = findViewById(R.id.Vista_Pagina);
        miVisualizadorDePantalla = new MiVisualizadorDePantallaAlumno(this);
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

    //PARA CREAR EL MENU
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed(){
        cerrarSesion();
    }

    //PARA ELEGIR LAS OPCIONES DEL MENU
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                cerrarSesion();
                break;
            case R.id.settings:
                startActivity(new Intent(this, Setting.class));;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //SOLICITUD DE PERMISOS POR PANTALLA
    public void verificarPermisos(){
        int permisosAlmacenamiento = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION);

        if(permisosAlmacenamiento == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permisos Concedidos", Toast.LENGTH_SHORT).show();
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
                finish();
            }
        });

        dialogBuilder.setNegativeButton("NO", null);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
}

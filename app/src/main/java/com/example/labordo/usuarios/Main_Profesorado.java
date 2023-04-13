//SAMALA HAMALA

package com.example.labordo.usuarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.labordo.R;
import com.example.labordo.activity.LoginActivity;
import com.example.labordo.activity.Setting;
import com.example.labordo.visualizador_pantalla.MiVisualizadorDePantallaProfesor;
import com.google.android.material.tabs.TabLayout;

public class Main_Profesorado extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MiVisualizadorDePantallaProfesor miVisualizadorDePantalla;

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

    public void onBackPressed() {
        preguntarCerrarSesion();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                startActivity(new Intent(this, Setting.class));
                break;
            case R.id.logout:
                preguntarCerrarSesion();
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


    public void preguntarCerrarSesion(){
        AlertDialog.Builder dialogoBuilder = new AlertDialog.Builder(this).setMessage("¿Desea cerrar sesión?").setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).setNegativeButton("Cancelar", null);
        AlertDialog dialogo = dialogoBuilder.create();
        dialogo.show();
    }
}
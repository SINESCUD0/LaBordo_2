//SAMALA HAMALA

package com.example.labordo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MiVisualizadorDePantalla miVisualizadorDePantalla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_profesorado);

        verificarPermisos();

        tabLayout = findViewById(R.id.Tab_Layout);
        viewPager2 = findViewById(R.id.Vista_Pagina);
        miVisualizadorDePantalla = new MiVisualizadorDePantalla(this);
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
}
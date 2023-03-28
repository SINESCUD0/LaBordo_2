//SAMALA HAMALA

package com.example.labordo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.labordo.fragmentos.Tab1Fragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static String usuario;
    private static String contraseña;
    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MiVisualizadorDePantalla miVisualizadorDePantalla;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alertDialogConInput();

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


    private void alertDialogConInput() {

        LinearLayout linearLayout = new LinearLayout(this.getApplicationContext());
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Inicio de sesión:");

        EditText input_usuario = new EditText(this);
        input_usuario.setHint("Usuario");
        input_usuario.setInputType(InputType.TYPE_CLASS_TEXT);


        EditText input_contrasena = new EditText(this);
        input_contrasena.setHint("Contraseña");
        input_contrasena.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        linearLayout.addView(input_usuario);
        linearLayout.addView(input_contrasena);

        alertDialogBuilder.setView(linearLayout);
        alertDialogBuilder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                usuario = input_usuario.getText().toString();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancelar", null);

        alertDialogBuilder.show();
    }
}
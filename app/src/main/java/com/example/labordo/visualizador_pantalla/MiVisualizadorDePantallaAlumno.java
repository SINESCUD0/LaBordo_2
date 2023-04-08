package com.example.labordo.visualizador_pantalla;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.labordo.fragmentos.Tab1Alumno;
import com.example.labordo.fragmentos.Tab2Alumno;
import com.example.labordo.fragmentos.Tab3Alumno;

public class MiVisualizadorDePantallaAlumno extends FragmentStateAdapter {

    public MiVisualizadorDePantallaAlumno(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    //Este createFragment sirve para poder cambiar de pestaña al desplazar la pantalla
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Tab1Alumno();
            case 1:
                return new Tab2Alumno();
            case 2:
                return new Tab3Alumno();
            default:
                return new Tab1Alumno();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

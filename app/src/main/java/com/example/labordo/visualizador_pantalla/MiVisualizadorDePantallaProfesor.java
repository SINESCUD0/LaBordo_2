package com.example.labordo.visualizador_pantalla;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.labordo.fragmentos.Tab1Profesor;
import com.example.labordo.fragmentos.Tab2Profesor;
import com.example.labordo.fragmentos.Tab3Profesor;

public class MiVisualizadorDePantallaProfesor extends FragmentStateAdapter  {
    public MiVisualizadorDePantallaProfesor(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    //Este createFragment sirve para poder cambiar de pestaña al desplazar la pantalla
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Tab1Profesor();
            case 1:
                return new Tab2Profesor();
            case 2:
                return new Tab3Profesor();
            default:
                return new Tab1Profesor();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

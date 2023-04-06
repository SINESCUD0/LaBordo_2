package com.example.labordo;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.labordo.fragmentos.Tab1Fragment;
import com.example.labordo.fragmentos.Tab2Fragment;
import com.example.labordo.fragmentos.Tab3Fragment;

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
                return new Tab1Fragment();
            case 1:
                return new Tab2Fragment();
            case 2:
                return new Tab3Fragment();
            default:
                return new Tab1Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

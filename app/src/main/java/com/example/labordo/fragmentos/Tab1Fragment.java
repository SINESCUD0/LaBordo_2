package com.example.labordo.fragmentos;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.labordo.R;
import com.example.labordo.objetos.ActividadesVo;
import com.example.labordo.recyclerview.AdapterDatos;

import java.util.ArrayList;

public class Tab1Fragment extends Fragment{

    ArrayList<ActividadesVo> listDatos;
    RecyclerView recycler;
    Button add;

    int i = 0;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_tab1, container, false);
        listDatos = new ArrayList<>();
        recycler = (RecyclerView) vista.findViewById(R.id.TareasAsignadas);
        add = (Button) vista.findViewById(R.id.AddTarea);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listDatos.add(new ActividadesVo("Goku", "Este es goku y es un poco tonto", R.drawable.goku_prueba));
                AdapterDatos adapter = new AdapterDatos(listDatos);
                recycler.setAdapter(adapter);
            }
        });


        return vista;
    }
}
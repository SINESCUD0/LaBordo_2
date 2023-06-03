package com.example.labordo.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labordo.R;
import com.example.labordo.objetos.Profesorado;

import java.util.ArrayList;

public class AdapterProfesorado extends RecyclerView.Adapter<AdapterProfesorado.ViewHolderDatos> implements View.OnClickListener{
    ArrayList<Profesorado> listProfesores;
    private View.OnClickListener listener;

    public AdapterProfesorado(ArrayList<Profesorado> listProfesores) {
        this.listProfesores = listProfesores;
    }

    @NonNull
    @Override
    public AdapterProfesorado.ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profesorado_item, null, false);
        view.setOnClickListener(this);

        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.nombreProfesor.setText(listProfesores.get(position).getNombreProfesor());
        holder.apellidos.setText(listProfesores.get(position).getApellidosProfesor());
        holder.fotoProfesor.setImageURI(listProfesores.get(position).getFotoProfesor());
        holder.DNI.setText(listProfesores.get(position).getDNI());
        holder.instituto.setText(listProfesores.get(position).getInstituto());
        holder.correo.setText(listProfesores.get(position).getCorreo());
    }

    @Override
    public int getItemCount() {
        return listProfesores.size();
    }


    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }


    public class ViewHolderDatos extends RecyclerView.ViewHolder{

        TextView nombreProfesor, apellidos, DNI, correo;
        TextView instituto;
        ImageView fotoProfesor;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            nombreProfesor = itemView.findViewById(R.id.nombreProfesor);
            apellidos = itemView.findViewById(R.id.apellidos_profesor);
            fotoProfesor = itemView.findViewById(R.id.fotoProfesor);
            DNI = itemView.findViewById(R.id.dni_profesor);
            instituto = itemView.findViewById(R.id.instituto_profesor);
            correo = itemView.findViewById(R.id.correo_profesor);

            /*itemView.findViewById(R.id.layout_lista_profesorado).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(itemView.getContext(), "Posicion "+getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });*/
        }
    }
}

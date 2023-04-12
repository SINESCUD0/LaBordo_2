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
        holder.curso.setText(listProfesores.get(position).getCursoProfesor());
        holder.fotoProfesor.setImageURI(listProfesores.get(position).getFotoProfesor());
        holder.DNI.setText(listProfesores.get(position).getDNI());
        holder.fechaNacimiento.setText(listProfesores.get(position).getFechaNacimiento());
        holder.actividad.setImageResource(listProfesores.get(position).getImagenActividad());

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

        TextView nombreProfesor;
        TextView curso;
        ImageView fotoProfesor;
        TextView DNI;
        TextView fechaNacimiento;
        ImageView actividad;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            nombreProfesor = (TextView) itemView.findViewById(R.id.nombreProfesor);
            curso = (TextView) itemView.findViewById(R.id.curso);
            fotoProfesor = (ImageView) itemView.findViewById(R.id.fotoProfesor);
            DNI = (TextView) itemView.findViewById(R.id.dni);
            fechaNacimiento = (TextView) itemView.findViewById(R.id.fechaNacimiento);
            actividad = (ImageView) itemView.findViewById(R.id.actividad);

            itemView.findViewById(R.id.layout_lista_profesorado).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(itemView.getContext(), "Posicion "+getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

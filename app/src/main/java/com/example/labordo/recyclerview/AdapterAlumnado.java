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
import com.example.labordo.objetos.Alumnado;

import java.util.ArrayList;

public class AdapterAlumnado /*extends RecyclerView.Adapter<AdapterAlumnado.ViewHolderDatos> implements View.OnClickListener*/{
    /*ArrayList<Alumnado> listAlumnos;
    private View.OnClickListener listener;

    public AdapterAlumnado(ArrayList<Alumnado> listAlumnos) {
        this.listAlumnos = listAlumnos;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alumnado_item, null, false);
        view.setOnClickListener(this);

        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.nombreAlumno.setText(listAlumnos.get(position).getNombreAlumno());
        holder.curso.setText(listAlumnos.get(position).getCursoAlumno());
        holder.fotoAlumno.setImageURI(listAlumnos.get(position).getFotoAlumno());
        holder.DNI.setText(listAlumnos.get(position).getDNI());
        holder.fechaNacimiento.setText(listAlumnos.get(position).getFechaNacimiento());
        holder.actividad.setImageResource(listAlumnos.get(position).getImagenActividad());

    }

    @Override
    public int getItemCount() {
        return listAlumnos.size();
    }


    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }


    public class ViewHolderDatos extends RecyclerView.ViewHolder{

        TextView nombreAlumno;
        TextView curso;
        ImageView fotoAlumno;
        TextView DNI;
        TextView fechaNacimiento;
        ImageView actividad;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            nombreAlumno = (TextView) itemView.findViewById(R.id.nombreAlumno);
            curso = (TextView) itemView.findViewById(R.id.curso);
            fotoAlumno = (ImageView) itemView.findViewById(R.id.fotoAlumno);
            DNI = (TextView) itemView.findViewById(R.id.dni);
            fechaNacimiento = (TextView) itemView.findViewById(R.id.fechaNacimiento);
            actividad = (ImageView) itemView.findViewById(R.id.actividad);

            itemView.findViewById(R.id.layout_actividades_asignadas).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(itemView.getContext(), "Posicion "+getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

     */
}

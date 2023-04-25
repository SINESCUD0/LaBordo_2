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

public class AdapterAlumnado extends RecyclerView.Adapter<AdapterAlumnado.ViewHolderDatos> implements View.OnClickListener{
    ArrayList<Alumnado> listAlumnos;
    private View.OnClickListener listener;

    public AdapterAlumnado(ArrayList<Alumnado> listAlumnos) {this.listAlumnos = listAlumnos;}

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
        holder.correo.setText(listAlumnos.get(position).getCorreo());
        holder.apellidos.setText(listAlumnos.get(position).getApellidos());
        holder.instituto.setText(listAlumnos.get(position).getInstituto());
        holder.puntos.setText(listAlumnos.get(position).getPuntos());
    }

    @Override
    public int getItemCount() {return listAlumnos.size();}

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder{

        TextView nombreAlumno, curso, DNI, fechaNacimiento, apellidos, instituto, puntos, correo;
        ImageView fotoAlumno;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            nombreAlumno = (TextView) itemView.findViewById(R.id.nombreAlumno);
            curso = (TextView) itemView.findViewById(R.id.curso_alumno);
            fotoAlumno = (ImageView) itemView.findViewById(R.id.fotoAlumno);
            DNI = (TextView) itemView.findViewById(R.id.dni_alumno);
            fechaNacimiento = (TextView) itemView.findViewById(R.id.fechaNacimiento_alumno);
            apellidos = (TextView) itemView.findViewById(R.id.apellidos_alumno);
            instituto = (TextView) itemView.findViewById(R.id.instituto_alumno);
            puntos = (TextView) itemView.findViewById(R.id.puntos_alumno);
            correo = (TextView) itemView.findViewById(R.id.correo_alumno);

            itemView.findViewById(R.id.layout_lista_alumnado).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(itemView.getContext(), "Posicion "+getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}

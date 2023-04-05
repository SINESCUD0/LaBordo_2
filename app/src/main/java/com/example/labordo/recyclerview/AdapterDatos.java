package com.example.labordo.recyclerview;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labordo.MainActivity;
import com.example.labordo.R;
import com.example.labordo.objetos.ActividadesVo;

import java.util.ArrayList;
import java.util.List;

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos> implements View.OnClickListener{

    ArrayList<ActividadesVo> listDatos;
    private View.OnClickListener listener;

    public AdapterDatos(ArrayList<ActividadesVo> listDatos) {
        this.listDatos = listDatos;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.actividades_asignadas_item, null, false);
        view.setOnClickListener(this);

        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.nombreActividad.setText(listDatos.get(position).getNombreTarea());
        holder.descripcion.setText(listDatos.get(position).getDescripcion());
        holder.imagenTarea.setImageURI(listDatos.get(position).getImagenTarea());
        holder.precio.setText(listDatos.get(position).getPrecio());
        holder.fecha.setText(listDatos.get(position).getFecha());
        holder.actividad.setImageResource(listDatos.get(position).getImagenActividad());

    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }


    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }


    public class ViewHolderDatos extends RecyclerView.ViewHolder{

        TextView nombreActividad;
        TextView descripcion;
        ImageView imagenTarea;
        TextView precio;
        TextView fecha;
        ImageView actividad;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            nombreActividad = (TextView) itemView.findViewById(R.id.nombreTarea);
            descripcion = (TextView) itemView.findViewById(R.id.informacionTarea);
            imagenTarea = (ImageView) itemView.findViewById(R.id.imageView);
            precio = (TextView) itemView.findViewById(R.id.precio);
            fecha = (TextView) itemView.findViewById(R.id.fechaLimite);
            actividad = (ImageView) itemView.findViewById(R.id.actividad);

            itemView.findViewById(R.id.layout_lista_alumnado).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(itemView.getContext(), "Posicion "+getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}

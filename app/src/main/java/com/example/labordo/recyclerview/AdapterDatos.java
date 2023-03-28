package com.example.labordo.recyclerview;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labordo.R;
import com.example.labordo.objetos.ActividadesVo;

import java.util.ArrayList;

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
        holder.avatar.setImageResource(listDatos.get(position).getImagenId());
    }

    @Override
    public int getItemCount() {
        return listDatos.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView nombreActividad;
        TextView descripcion;
        ImageView avatar;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            nombreActividad = (TextView) itemView.findViewById(R.id.nombreTarea);
            descripcion = (TextView) itemView.findViewById(R.id.informacionTarea);
            avatar = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }
}

package com.example.labordo.recyclerview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labordo.R;
import com.example.labordo.objetos.ActividadesVo;
import com.example.labordo.objetos.LoginInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class AdapterDatosTab2Alumno extends RecyclerView.Adapter<AdapterDatosTab2Alumno.ViewHolderDatos> implements View.OnClickListener {


    LoginInfo usuario = new LoginInfo();
    String titulo = "";
    String fechaT = "";
    String precioT = "";
    String descripcionT = "";
    ArrayList<ActividadesVo> listDatos;

    private Context mContext;
    private View.OnClickListener listener;

    public AdapterDatosTab2Alumno(ArrayList<ActividadesVo> listDatos) {
        this.listDatos = listDatos;
    }

    // Crear nueva Activity para mostrar la Info y modificarla


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
        holder.actividad.setImageURI(listDatos.get(position).getImagenActividad());
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


    public class ViewHolderDatos extends RecyclerView.ViewHolder implements Serializable {

        public TextView getNombreActividad() {
            return nombreActividad;
        }

        public TextView getDescripcion() {
            return descripcion;
        }

        public ImageView getImagenTarea() {
            return imagenTarea;
        }

        public TextView getPrecio() {
            return precio;
        }

        public TextView getFecha() {
            return fecha;
        }

        public ImageView getActividad() {
            return actividad;
        }

        File file;
        View dialogView;
        Uri foto;




        TextView nombreActividad;
        TextView descripcion;
        ImageView imagenTarea;
        TextView precio;
        TextView fecha;
        ImageView actividad;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            int numero = 1;
            nombreActividad = (TextView) itemView.findViewById(R.id.nombreTarea);
            descripcion = (TextView) itemView.findViewById(R.id.informacionTarea);
            //PARA QUE PUEDA HACER UN SCROLLBAR
            /*
            descripcion.setMovementMethod(new ScrollingMovementMethod());
            descripcion.setOnTouchListener((v, event) -> {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            });
             */
            imagenTarea = (ImageView) itemView.findViewById(R.id.imageView);
            precio = (TextView) itemView.findViewById(R.id.precio);
            fecha = (TextView) itemView.findViewById(R.id.fechaLimite);
            actividad = (ImageView) itemView.findViewById(R.id.actividad);
            //CUANDO EL USUARIO PULSE EN LA ACTIVIDAD MOSTRARA LA INFORMACION DE ESTA ACTIVIDAD
            itemView.findViewById(R.id.layout_actividades_asignadas).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (usuario.isTipoCuenta() == false) {

                        Toast.makeText(itemView.getContext(), "PULSACION CORTA", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(itemView.getContext());

                        LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
                        View dialogView = inflater.inflate(R.layout.ventana_alerta_tareas_alumno, null);
                        dialogBuilder.setView(dialogView);

                        Drawable drawable = imagenTarea.getDrawable();
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                        File file = new File(itemView.getContext().getExternalCacheDir(), "image.png");
                        try{
                            OutputStream outputStream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                            outputStream.flush();
                            outputStream.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }

                        Uri foto = Uri.fromFile(file);
                        titulo = nombreActividad.getText().toString();
                        fechaT = fecha.getText().toString();
                        precioT = precio.getText().toString();
                        descripcionT = descripcion.getText().toString();

                        ImageView fotoLabor = dialogView.findViewById(R.id.imagenLabor);
                        TextView tituloLabor = dialogView.findViewById(R.id.tituloLabor);
                        TextView fechaLimite = dialogView.findViewById(R.id.FechaEntrega);
                        TextView precioLabor = dialogView.findViewById(R.id.precioLabor);
                        TextView descripcionL = dialogView.findViewById(R.id.descripcionLabor);
                        descripcionL.setMovementMethod(new ScrollingMovementMethod());
                        descripcionL.setOnTouchListener((a, event) -> {
                            a.getParent().requestDisallowInterceptTouchEvent(true);
                            return false;
                        });

                        tituloLabor.setText(titulo);
                        fechaLimite.setText(fechaT);
                        precioLabor.setText(precioT);
                        descripcionL.setText(descripcionT);
                        fotoLabor.setImageURI(foto);

                        dialogBuilder.setNegativeButton("OK", null);


                        AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.show();
                    }
                }
            });

        }
    }
}

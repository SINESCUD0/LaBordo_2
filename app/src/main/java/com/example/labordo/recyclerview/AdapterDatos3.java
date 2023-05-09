package com.example.labordo.recyclerview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labordo.R;
import com.example.labordo.activity.ModificarActividad;
import com.example.labordo.objetos.ActividadesVo;
import com.example.labordo.objetos.LoginInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class AdapterDatos3 extends RecyclerView.Adapter<AdapterDatos3.ViewHolderDatos> implements View.OnClickListener{


    LoginInfo usuario = new LoginInfo();
    ArrayList<ActividadesVo> listDatos;
    private View.OnClickListener listener;

    public AdapterDatos3(ArrayList<ActividadesVo> listDatos) {
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
        String titulo;
        String fechaT;
        String precioT;
        String descripcionT;



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

            itemView.findViewById(R.id.layout_actividades_asignadas).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Toast.makeText(itemView.getContext(), "Posicion "+getAdapterPosition(), Toast.LENGTH_SHORT).show();

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(itemView.getContext());

                    LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
                    if(usuario.isTipoCuenta()){
                        dialogView = inflater.inflate(R.layout.ventana_alerta_tareas_profe, null);
                    }else if(!usuario.isTipoCuenta()){
                        dialogView = inflater.inflate(R.layout.ventana_alerta_tareas_alumno, null);
                    }

                    dialogBuilder.setView(dialogView);

                    Drawable drawable = imagenTarea.getDrawable();
                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                    file = new File(itemView.getContext().getExternalCacheDir(), "image.png");
                    try{
                        OutputStream outputStream = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                        outputStream.flush();
                        outputStream.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }


                    if(usuario.isTipoCuenta()){

                        // Imagen, Titulo, Precio, Descripcion, Fecha
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageInByte = baos.toByteArray();

                        Intent intent = new Intent(view.getContext(), ModificarActividad.class);
                        intent.putExtra("imagen", imageInByte);
                        intent.putExtra("titulo", nombreActividad.getText());
                        intent.putExtra("precio", precio.getText());
                        intent.putExtra("fecha", fecha.getText());
                        intent.putExtra("descripcion", descripcion.getText());
                        view.getContext().startActivity(intent);
                        //verDatosProfe();
                    }else if(!usuario.isTipoCuenta()){
                        verDatosEstudiante();
                        dialogBuilder.setNegativeButton("OK", null);

                        AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.show();
                    }

                    return false;
                }
            });

        }



        public void verDatosProfe(){
            ImageView fotoLabor = dialogView.findViewById(R.id.imagenLabor);
            TextView tituloLabor = dialogView.findViewById(R.id.tituloLabor);
            TextView fechaLimite = dialogView.findViewById(R.id.FechaEntrega);
            EditText precioLabor = dialogView.findViewById(R.id.precioLabor);
            EditText descripcionL = dialogView.findViewById(R.id.descripcionLabor);

            foto = Uri.fromFile(file);
            titulo = nombreActividad.getText().toString();
            fechaT = fecha.getText().toString();
            precioT = precio.getText().toString();
            descripcionT = descripcion.getText().toString();

            descripcionL.setMovementMethod(new ScrollingMovementMethod());
            descripcionL.setOnTouchListener((a, event) -> {
                a.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            });

            /*
            fechaLimite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout linearLayout = new LinearLayout(itemView.getContext());
                    linearLayout.setOrientation(LinearLayout.VERTICAL);

                    CalendarView calendarView = new CalendarView(itemView.getContext());
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(itemView.getContext());
                    alertDialogBuilder.setTitle("Introduce la fecha:");


                    linearLayout.addView(calendarView);
                    alertDialogBuilder.setView(linearLayout);

                    calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                        @Override
                        public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {

                            final int mesActual = month + 1;
                            //FORMATEO EL DIA Y EL MES SI ES MENOR DE 10, ES DECIR, SI ELEGIMOS EL MES DE FEBRERO
                            //NOS MOSTRARA EL MES FORMATEADO EN VEZ DE SOLO 2 NOS SALDRA COMO 02, ESTO SE APLICA IGUAL
                            //AL DIA
                            String diaFormateado = (dayOfMonth < 10)? "0" + String.valueOf(dayOfMonth):String.valueOf(dayOfMonth);
                            String mesFormateado = (mesActual < 10)? "0" + String.valueOf(mesActual):String.valueOf(mesActual);

                            String dia = diaFormateado + "/" + mesFormateado + "/" + year;
                            fechaT = dia;
                        }
                    });

                    alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            fechaLimite.setText(fechaT);
                            Toast.makeText(dialogView.getContext(), "Fecha cambiada", Toast.LENGTH_SHORT).show();
                        }
                    });
                    alertDialogBuilder.setNegativeButton("Cancelar", null);

                    AlertDialog alertDialog = alertDialogBuilder.create();

                    alertDialog.show();


                }
            });

            fotoLabor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    view.getContext().startActivity(intent);
                }
            });

            */


            tituloLabor.setText(titulo);
            fechaLimite.setText(fechaT);
            precioLabor.setText(precioT);
            descripcionL.setText(descripcionT);
            fotoLabor.setImageURI(foto);
        }

        public void verDatosEstudiante(){


            ImageView fotoLabor = dialogView.findViewById(R.id.imagenLabor);
            TextView tituloLabor = dialogView.findViewById(R.id.tituloLabor);
            TextView fechaLimite = dialogView.findViewById(R.id.FechaEntrega);
            TextView precioLabor = dialogView.findViewById(R.id.precioLabor);
            TextView descripcionL = dialogView.findViewById(R.id.descripcionLabor);

            foto = Uri.fromFile(file);
            titulo = nombreActividad.getText().toString();
            fechaT = fecha.getText().toString();
            precioT = precio.getText().toString();
            descripcionT = descripcion.getText().toString();

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
        }

    }

}

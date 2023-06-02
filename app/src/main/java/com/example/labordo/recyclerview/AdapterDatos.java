package com.example.labordo.recyclerview;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos> implements View.OnClickListener{


    LoginInfo usuario = new LoginInfo();
    String titulo = "";
    String fechaT = "";
    String precioT = "";
    String descripcionT = "";
    ArrayList<ActividadesVo> listDatos;

    private Context mContext;
    private View.OnClickListener listener;

    public AdapterDatos(ArrayList<ActividadesVo> listDatos) {
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
                    if(usuario.isTipoCuenta() == true){
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

                        dialogBuilder.setPositiveButton("TAREA TERMINADA", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(itemView.getContext(), "ACTIVIDAD ACEPTADA", Toast.LENGTH_SHORT).show();
                                TerminarLabor terminarLabor = new TerminarLabor();
                                terminarLabor.execute();
                            }
                        });

                        dialogBuilder.setNegativeButton("OK", null);

                        AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.show();
                    }
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

                        dialogBuilder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(itemView.getContext(), "ACTIVIDAD ACEPTADA", Toast.LENGTH_SHORT).show();
                                AsignarLabor nuevaLabor = new AsignarLabor();
                                nuevaLabor.execute();
                            }
                        });

                        dialogBuilder.setNegativeButton("CANCELAR", null);


                        AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.show();
                    }
                }
            });
            itemView.findViewById(R.id.layout_actividades_asignadas).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if(usuario.isTipoCuenta()){
                        Toast.makeText(itemView.getContext(), "PULSACION LARGA", Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(itemView.getContext());

                        LayoutInflater inflater = LayoutInflater.from(itemView.getContext());

                        dialogView = inflater.inflate(R.layout.ventana_alerta_tareas_profe, null);


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
                    }

                    return false;
                }
            });

        }

        //PARA QUE EL ALUMNO PUEDA ACEPTAR UNA ACTIVIDAD Y SE LE ASIGNE AL ALUMNO
        class AsignarLabor extends AsyncTask<Void, Void, Void> {

            //ESTA VARIABLE (MSG) LA UTILIZAMOS PARA EN CASO DE FALLO TE MUESTRE EN EL TOAST EL FALLO QUE DA
            String msg = "";

            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    Class.forName("com.mysql.jdbc.Driver"); //PILLAMOS LA INFORMACION DEL PAQUETE
                    Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.1.36:3306/labordo?useUnicode=true&characterEncoding=utf8",
                            "root",
                            "L4b0rd0#"); //NOS CONECTAMOS A LA BASE DE DATOS
                    if(conn == null){
                        //SI NO CONSIGUES CONECTARTE A LA BASE DE DATOS
                        msg = "Se ha perdido la conexion";
                    }else{
                        //SI CONSIGUE CONECTARSE A LA BASE DE DATOS QUE EJECUTE LA SIGUIENTE SENTENCIA
                        String dni_Estudiante = usuario.getDni();
                        String instituto = usuario.getInstitutoLogin();
                        String query2 = "SELECT id_labor FROM labores WHERE nombreActividad = ? AND precio = ? AND descripcion = ?" +
                                " AND fechaLimite = ? AND instituto = ? ";
                        PreparedStatement statement2 = conn.prepareStatement(query2);
                        statement2.setString(1, titulo);
                        statement2.setString(2, precioT);
                        statement2.setString(3, descripcionT);
                        statement2.setString(4, fechaT);
                        statement2.setString(5, instituto);
                        ResultSet rs = statement2.executeQuery();
                        while(rs.next()){
                            String query = "INSERT INTO estudiante_labores_asignados (dni_estudiante, id_labor) VALUES (?, ?)";
                            PreparedStatement statement = conn.prepareStatement(query);
                            String idLabor = rs.getString("id_labor");
                            statement.setString(1, dni_Estudiante);
                            statement.setString(2, idLabor);
                            statement.executeUpdate();
                            String query3 = "UPDATE labores SET estado = ? WHERE id_labor = ?";
                            PreparedStatement statement3 = conn.prepareStatement(query3);
                            String confirmada = "CONFIRMADA";
                            statement3.setString(1, confirmada);
                            statement3.setString(2, idLabor);
                            statement3.executeUpdate();
                            statement.close();
                            statement3.close();
                        }

                        msg = "TAREA ASIGNADA";

                        statement2.close();
                    }

                    conn.close();

                }catch (Exception e){
                    //SI FALLA ALGO EN EL CODIGO SALDRA LA SIGUIENTE EXCEPTION
                    msg = e.getMessage();
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid){
                //Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        }

        //MODIFICAR ESTA CLASE PARA QUE CUANDO EL PROFESOR DE A TERMINAR TAREA LE AUMENTE EL SALDO DEL ALUMNO QUE TENIA ASIGNADA
        //LA TAREA
        class TerminarLabor extends AsyncTask<Void, Void, Void> {

            //ESTA VARIABLE (MSG) LA UTILIZAMOS PARA EN CASO DE FALLO TE MUESTRE EN EL TOAST EL FALLO QUE DA
            String msg = "";

            @Override
            protected Void doInBackground(Void... voids) {
                try{
                    Class.forName("com.mysql.jdbc.Driver"); //PILLAMOS LA INFORMACION DEL PAQUETE
                    Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.1.36:3306/labordo?useUnicode=true&characterEncoding=utf8",
                            "root",
                            "L4b0rd0#"); //NOS CONECTAMOS A LA BASE DE DATOS
                    if(conn == null){
                        //SI NO CONSIGUES CONECTARTE A LA BASE DE DATOS
                        msg = "Se ha perdido la conexion";
                    }else{
                        //SI CONSIGUE CONECTARSE A LA BASE DE DATOS QUE EJECUTE LA SIGUIENTE SENTENCIA
                        String dni_Profesor = usuario.getDni();
                        String instituto = usuario.getInstitutoLogin();
                        String query2 = "SELECT id_labor FROM labores WHERE nombreActividad = ? AND precio = ? AND descripcion = ?" +
                                " AND fechaLimite = ? AND instituto = ? AND dni_profesor = ?";
                        PreparedStatement statement2 = conn.prepareStatement(query2);
                        statement2.setString(1, titulo);
                        statement2.setString(2, precioT);
                        statement2.setString(3, descripcionT);
                        statement2.setString(4, fechaT);
                        statement2.setString(5, instituto);
                        statement2.setString(6, dni_Profesor);
                        ResultSet rs = statement2.executeQuery();
                        while(rs.next()){
                            String query = "SELECT dni_estudiante FROM estudiante_labores_asignados WHERE id_labor = ?";
                            //ACTUALIZAMOS LA TAREA A ESTADO TERMINADA
                            String idLabor = rs.getString("id_labor");
                            PreparedStatement statement = conn.prepareStatement(query);
                            statement.setString(1, idLabor);
                            ResultSet rs2 = statement.executeQuery();
                            String query3 = "UPDATE labores SET estado = ? WHERE id_labor = ?";
                            PreparedStatement statement3 = conn.prepareStatement(query3);
                            String confirmada = "RESUELTA";
                            statement3.setString(1, confirmada);
                            statement3.setString(2, idLabor);
                            statement3.executeUpdate();
                            statement3.close();
                            while(rs2.next()){
                                String query1 = "UPDATE estudiante SET puntos = ? + puntos WHERE dni = ?";
                                String dniEstudiante = rs2.getString("dni_estudiante");
                                PreparedStatement statement1 = conn.prepareStatement(query1);
                                statement1.setString(1, precioT);
                                statement1.setString(2, dniEstudiante);
                                statement1.executeUpdate();
                                statement1.close();
                            }


                        }

                        msg = "TAREA TERMINADA";

                        statement2.close();
                    }

                    conn.close();

                }catch (Exception e){
                    //SI FALLA ALGO EN EL CODIGO SALDRA LA SIGUIENTE EXCEPTION
                    msg = e.getMessage();
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid){
                //Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        }

    }

}

package com.example.labordo.recyclerview;

import android.content.Context;
import android.content.DialogInterface;
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
import com.example.labordo.objetos.ActividadesVo;
import com.example.labordo.objetos.LoginInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class AdapterDatos2 extends RecyclerView.Adapter<AdapterDatos2.ViewHolderDatos> implements View.OnClickListener{

    ArrayList<ActividadesVo> listDatos;
    private Context mContext;
    private View.OnClickListener listener;
    String Titulo, Precio, Descripcion, Fecha;
    LoginInfo tipoUsuario = new LoginInfo();
    EditText tituloLaborProfe;
    EditText precioLaborProfe;
    EditText descripcionLaborProfe;
    EditText fechaLimiteProfe;

    public AdapterDatos2(ArrayList<ActividadesVo> listDatos, Context mContext) {
        this.listDatos = listDatos;
        this.mContext = mContext;
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


    public class ViewHolderDatos extends RecyclerView.ViewHolder{

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
            /*descripcion.setMovementMethod(new ScrollingMovementMethod());
            descripcion.setOnTouchListener((v, event) -> {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            });*/
            imagenTarea = (ImageView) itemView.findViewById(R.id.imageView);
            precio = (TextView) itemView.findViewById(R.id.precio);
            fecha = (TextView) itemView.findViewById(R.id.fechaLimite);
            actividad = (ImageView) itemView.findViewById(R.id.actividad);


            itemView.findViewById(R.id.layout_actividades_asignadas).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(tipoUsuario.isTipoCuenta() == true){
                        Toast.makeText(itemView.getContext(), "Posicion "+getAdapterPosition(), Toast.LENGTH_SHORT).show();

                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(itemView.getContext());

                        LayoutInflater inflater = LayoutInflater.from(itemView.getContext());
                        View dialogView = inflater.inflate(R.layout.ventana_alerta_tareas_profe, null);
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
                        String titulo = nombreActividad.getText().toString();
                        String fechaT = fecha.getText().toString();
                        String precioT = precio.getText().toString();
                        String descripcionT = descripcion.getText().toString();

                        ImageView fotoLabor = dialogView.findViewById(R.id.imagenLabor);
                        tituloLaborProfe = dialogView.findViewById(R.id.tituloLabor);
                        fechaLimiteProfe = dialogView.findViewById(R.id.FechaEntrega);
                        precioLaborProfe = dialogView.findViewById(R.id.precioLabor);
                        descripcionLaborProfe = dialogView.findViewById(R.id.descripcionLabor);
                        /*descripcionL.setMovementMethod(new ScrollingMovementMethod());
                        descripcionL.setOnTouchListener((a, event) -> {
                            a.getParent().requestDisallowInterceptTouchEvent(true);
                            return false;
                        });*/

                        Titulo = titulo;
                        Fecha = fechaT;
                        Precio = precioT;
                        Descripcion = descripcionT;
                        tituloLaborProfe.setText(titulo);
                        fechaLimiteProfe.setText(fechaT);
                        precioLaborProfe.setText(precioT);
                        descripcionLaborProfe.setText(descripcionT);
                        fotoLabor.setImageURI(foto);

                        dialogBuilder.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActualizarLabores actualizarLabores = new ActualizarLabores();
                                actualizarLabores.execute();
                            }
                        });

                        dialogBuilder.setNegativeButton("CANCELAR", null);

                        AlertDialog alertDialog = dialogBuilder.create();
                        alertDialog.show();

                    } else if (tipoUsuario.isTipoCuenta() == false) {
                        Toast.makeText(itemView.getContext(), "Posicion "+getAdapterPosition(), Toast.LENGTH_SHORT).show();

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
                        String titulo = nombreActividad.getText().toString();
                        String fechaT = fecha.getText().toString();
                        String precioT = precio.getText().toString();
                        String descripcionT = descripcion.getText().toString();

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

    class ActualizarLabores extends AsyncTask<Void, Void, Void> {

        //ESTA VARIABLE (MSG) LA UTILIZAMOS PARA EN CASO DE FALLO TE MUESTRE EN EL TOAST EL FALLO QUE DA
        String msg = "";
        String tituloLabor1 = tituloLaborProfe.getText().toString();
        String fechaLimite1 = fechaLimiteProfe.getText().toString();
        String precioLabor1 = precioLaborProfe.getText().toString();
        String descripcionLabor1 = descripcionLaborProfe.getText().toString();

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Class.forName("com.mysql.jdbc.Driver"); //PILLAMOS LA INFORMACION DEL PAQUETE
                Connection conn = DriverManager.getConnection(mContext.getResources().getString(R.string.DATABASE_URL),
                        mContext.getResources().getString(R.string.USER),
                        mContext.getResources().getString(R.string.PASSWORD)); //NOS CONECTAMOS A LA BASE DE DATOS
                if(conn == null){
                    //SI NO CONSIGUES CONECTARTE A LA BASE DE DATOS
                    msg = "Se ha perdido la conexion";
                }else{
                    //SI CONSIGUE CONECTARSE A LA BASE DE DATOS QUE EJECUTE LA SIGUIENTE SENTENCIA
                    String dni = tipoUsuario.getDni();
                    String institutoProfesor = tipoUsuario.getInstitutoLogin();
                    String query = "UPDATE labores SET nombreActividad = ?, precio = ?, descripcion = ?," +
                            " fechaLimite = ? WHERE dni_profesor = ? AND instituto = ? AND nombreActividad = ?" +
                            " AND precio = ? AND descripcion = ? AND fechaLimite = ?";
                    //SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    //Date date = formatter.parse(fechaT);
                    //java.sql.Date fechaElegidaSQL = new java.sql.Date(date.getTime());
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, tituloLabor1);
                    statement.setString(2, precioLabor1);
                    statement.setString(3, descripcionLabor1);
                    //statement.setBytes(4, fotoT);
                    statement.setString(4, fechaLimite1);
                    statement.setString(5, dni);
                    statement.setString(6, institutoProfesor);
                    statement.setString(7, Titulo);
                    statement.setString(8, Precio);
                    statement.setString(9, Descripcion);
                    statement.setString(10, Fecha);
                    statement.executeUpdate();

                    msg = "Tarea Actualizada";

                    statement.close();
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
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        }
    }

}

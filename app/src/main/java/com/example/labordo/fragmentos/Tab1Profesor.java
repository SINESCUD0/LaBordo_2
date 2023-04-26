package com.example.labordo.fragmentos;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.labordo.R;
import com.example.labordo.objetos.ActividadesVo;
import com.example.labordo.objetos.LoginInfo;
import com.example.labordo.recyclerview.AdapterDatos;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Tab1Profesor extends Fragment{

    //PARA CONECTARTE A LA BASE DE DATOS
    private static final String DATABASE_URL = "jdbc:mysql://192.168.1.38:3306/labordo?useUnicode=true&characterEncoding=UTF-8";

    //USUARIO PARA INICIAR SESION EN LA BASE DE DATOS
    private static final String USER = "root";

    //CONTRASEÑA PARA INICIAR SESION EN EL USUARIO ROOT
    private static final String PASSWORD = "L4b0rd0#";

    ArrayList<ActividadesVo> listDatos;
    RecyclerView recycler;
    LoginInfo profesor = new LoginInfo();
    SwipeRefreshLayout refresh;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        RecibirLabores recibirLabores= new RecibirLabores();
        recibirLabores.execute();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.tab1_profesor, container, false);
        listDatos = new ArrayList<>();
        recycler = vista.findViewById(R.id.TareasAsignadas);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        refresh = vista.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listDatos.clear();
                RecibirLabores objSend = new RecibirLabores();
                objSend.execute();
                refresh.setRefreshing(false);
            }
        });
        return vista;
    }

    //PARA RECIBIR LAS LABORES QUE ESTAN YA ASIGNADAS
    class RecibirLabores extends AsyncTask<Void, Void, Void> {

        //ESTA VARIABLE (MSG) LA UTILIZAMOS PARA EN CASO DE FALLO TE MUESTRE EN EL TOAST EL FALLO QUE DA
        String msg = "";

        @Override
        protected void onPreExecute(){
            Toast.makeText(getContext(),"Actualizando", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Class.forName("com.mysql.jdbc.Driver"); //PILLAMOS LA INFORMACION DEL PAQUETE
                Connection conn = DriverManager.getConnection(DATABASE_URL,USER, PASSWORD); //NOS CONECTAMOS A LA BASE DE DATOS
                if(conn == null){
                    //SI NO CONSIGUES CONECTARTE A LA BASE DE DATOS
                    msg = "Se ha perdido la conexion";
                }else{
                    //RECOJO EL NUMERO DEL INSTITUTO DEL ALUMNO QUE HA INICIADO SESION
                    String instituto_alumno = profesor.getInstitutoLogin();
                    int numero = 1;
                    //SI CONSIGUE CONECTARSE A LA BASE DE DATOS QUE EJECUTE LA SIGUIENTE SENTENCIA
                    String query = "SELECT * FROM labores WHERE instituto = ? AND estado = 'CONFIRMADA'";
                    PreparedStatement statement = conn.prepareStatement(query);
                    statement.setString(1, instituto_alumno);
                    ResultSet rs = statement.executeQuery();

                    while(rs.next()){
                        String nombre = rs.getString("nombreActividad");
                        String precio = String.valueOf(rs.getInt("precio"));
                        String descripcion = rs.getString("descripcion");
                        Blob imageValue = rs.getBlob("imagenTarea");
                        String fechaLimite = String.valueOf(rs.getDate("fechaLimite"));
                        numero++;
                        Uri imageUri2 = Uri.parse("android.resource://com.example.labordo/" + R.drawable.pendiente);
                        if(imageValue == null){
                            Uri imageUri = Uri.parse("android.resource://com.example.labordo/" + R.drawable.sin_foto);
                            listDatos.add(new ActividadesVo(nombre, descripcion, imageUri, precio, fechaLimite, imageUri2));
                        }else{
                            // Obtener la URI del archivo temporal
                            byte[] blobBytes = imageValue.getBytes(1, (int) imageValue.length());

                            Bitmap bitmap = BitmapFactory.decodeByteArray(blobBytes, 0, blobBytes.length);

                            File file = File.createTempFile("fotoQuest"+numero, ".jpg", getContext().getCacheDir());
                            FileOutputStream fos = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                            fos.close();

                            Uri uri = Uri.fromFile(file);
                            listDatos.add(new ActividadesVo(nombre, descripcion, uri, precio, fechaLimite, imageUri2));
                        }
                    }


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
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            AdapterDatos adapterDatos = new AdapterDatos(listDatos);
            recycler.setAdapter(adapterDatos);
        }
    }

}
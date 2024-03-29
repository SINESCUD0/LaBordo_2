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

import com.example.labordo.R;
import com.example.labordo.objetos.ActividadesVo;
import com.example.labordo.objetos.LoginInfo;
import com.example.labordo.recyclerview.AdapterDatosTab2;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Tab2Alumno extends Fragment {


    ArrayList<ActividadesVo> listDatos;
    RecyclerView recycler;
    LoginInfo alumno = new LoginInfo();
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
        View vista = inflater.inflate(R.layout.tab2_alumno, container, false);
        listDatos = new ArrayList<>();
        recycler = vista.findViewById(R.id.TareasTuyas);
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

    //PARA RECIBIR LAS LABORES QUE TIENE EL ALUMNO
    class RecibirLabores extends AsyncTask<Void, Void, Void> {

        //ESTA VARIABLE (MSG) LA UTILIZAMOS PARA EN CASO DE FALLO TE MUESTRE EN EL TOAST EL FALLO QUE DA
        String msg = "";

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                Class.forName("com.mysql.jdbc.Driver"); //PILLAMOS LA INFORMACION DEL PAQUETE
                Connection conn = DriverManager.getConnection(getResources().getString(R.string.DATABASE_URL),
                        getResources().getString(R.string.USER),
                        getResources().getString(R.string.PASSWORD)); //NOS CONECTAMOS A LA BASE DE DATOS
                if(conn == null){
                    //SI NO CONSIGUES CONECTARTE A LA BASE DE DATOS
                    msg = "Se ha perdido la conexion";
                }else{
                    //RECOJO EL NUMERO DEL INSTITUTO DEL ALUMNO QUE HA INICIADO SESION
                    String dni_alumno = alumno.getDni();
                    int numero = 1;
                    //SI CONSIGUE CONECTARSE A LA BASE DE DATOS QUE EJECUTE LA SIGUIENTE SENTENCIA
                    String query2 = "SELECT * FROM estudiante_labores_asignados WHERE dni_estudiante = ?";
                    PreparedStatement statement2 = conn.prepareStatement(query2);
                    statement2.setString(1, dni_alumno);
                    ResultSet rs2 = statement2.executeQuery();
                    while (rs2.next()){
                        String idLabor = rs2.getString("id_labor");
                        String query = "SELECT * FROM labores WHERE id_labor = ?";
                        PreparedStatement statement = conn.prepareStatement(query);
                        statement.setString(1, idLabor);
                        ResultSet rs = statement.executeQuery();
                        while(rs.next()){
                            Uri imageUri2;
                            String nombre = rs.getString("nombreActividad");
                            String precio = String.valueOf(rs.getInt("precio"));
                            String descripcion = rs.getString("descripcion");
                            Blob imageValue = rs.getBlob("imagenTarea");
                            String fechaLimite = String.valueOf(rs.getDate("fechaLimite"));
                            String estado = rs.getString("estado");
                            numero++;
                            if(estado.equals("RESUELTA")){
                                imageUri2 = Uri.parse("android.resource://com.example.labordo/" + R.drawable.asignada);
                            } else if (estado.equals("LIBRE")) {
                                imageUri2 = Uri.parse("android.resource://com.example.labordo/" + R.drawable.inactivo);
                            } else{
                                imageUri2 = Uri.parse("android.resource://com.example.labordo/" + R.drawable.pendiente);
                            }

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


                        statement.close();
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
            //Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            AdapterDatosTab2 adapterDatos = new AdapterDatosTab2(listDatos);
            recycler.setAdapter(adapterDatos);
        }
    }
}
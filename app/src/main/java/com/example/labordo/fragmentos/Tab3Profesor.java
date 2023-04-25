package com.example.labordo.fragmentos;

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
import com.example.labordo.objetos.Alumnado;
import com.example.labordo.objetos.Profesorado;
import com.example.labordo.recyclerview.AdapterProfesorado;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class Tab3Profesor extends Fragment {

    //PARA CONECTARTE A LA BASE DE DATOS
    private static final String DATABASE_URL = "jdbc:mysql://192.168.1.41:3306/labordo?useUnicode=true&characterEncoding=UTF-8\"";

    //USUARIO PARA INICIAR SESION EN LA BASE DE DATOS
    private static final String USER = "root";

    //CONTRASEÑA PARA INICIAR SESION EN EL USUARIO ROOT
    private static final String PASSWORD = "L4b0rd0#";
    ArrayList<Alumnado> listAlumnos;
    RecyclerView recycler;
    SwipeRefreshLayout refresh;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Send objsend = new Send();
        objsend.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.tab3_profesor, container, false);
        listAlumnos = new ArrayList<>();
        recycler = vista.findViewById(R.id.Alumnos);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        refresh = vista.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listAlumnos.clear();
                Send objSend = new Send();
                objSend.execute();
                refresh.setRefreshing(false);
            }
        });
        return vista;
    }

    //PARA RECIBIR LA LISTA DE PROFESORES
    class Send extends AsyncTask<Void, Void, Void> {

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
                    //SI CONSIGUE CONECTARSE A LA BASE DE DATOS QUE EJECUTE LA SIGUIENTE SENTENCIA
                    String query = "SELECT * FROM estudiante";
                    PreparedStatement statement = conn.prepareStatement(query);
                    ResultSet rs = statement.executeQuery();

                    while(rs.next()) {
                        String dni = rs.getString("dni");
                        String nombre = rs.getString("nombre");
                        String apellidos = rs.getString("apellidos");
                        String correo = rs.getString("correo");
                        String instituto = String.valueOf(rs.getInt("instituto"));
                        String puntos = rs.getString("puntos");
                        String fechaNacimiento = String.valueOf(rs.getDate("fecha_nacimiento"));
                        String curso = rs.getString("curso");
                        String fotoPerfil = (rs.getBlob("foto_perfil"));

                        String query2 = "SELECT nombre FROM instituto WHERE id = ?";
                        PreparedStatement statement2 = conn.prepareStatement(query2);
                        statement2.setString(1, instituto);
                        ResultSet rs2 = statement2.executeQuery();
                        if(rs2.next()){
                            String nombreInstituto = rs2.getString("nombre");
                            listAlumnos.add(new Alumnado(nombre, nombreInstituto, dni, apellidos,correo));
                        }
                        rs2.close();
                        statement2.close();
                    }
                    msg = "Actualizado";

                    statement.close();
                    rs.close();
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
            AdapterProfesorado adapter = new AdapterProfesorado(listProfesores);
            recycler.setAdapter(adapter);
        }
    }
}
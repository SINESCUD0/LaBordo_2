package com.example.labordo.base_datos;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.Nullable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class BaseDatosMySQL extends AsyncTask<String, Void, String> {
    String res = "";
    private static final String url = "jdbc:mysql://192.168.0.192:3306/myDB";
    private static final String user = "hitesh";
    private static final String pass = "1234";
    private static final String DATABASE_NAME = "usuarios.db";
    public static final String TABLE_USUARIOS = "t_usuarios";
    private static final int DATABASE_VERSION = 1;

    public BaseDatosMySQL(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pass);
            System.out.println("Databaseection success");

            String result = "Database Connection Successful\n";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("select distinct Country from tblCountries");
            ResultSetMetaData rsmd = rs.getMetaData();

            while (rs.next()) {
                result += rs.getString(1).toString() + "\n";
            }
            res = result;
        } catch (Exception e) {
            e.printStackTrace();
            res = e.toString();
        }
        return res;
    }
}

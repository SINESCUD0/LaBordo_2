package com.example.labordo.registro;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.labordo.base_datos.BaseDatosGeneral;

public class RegistroDatos extends BaseDatosGeneral {
    Context context;

    public RegistroDatos(@Nullable Context context) {
        super(context);
        this.context = context;
    }

    public long insertarDatos(String correo, String dni, String password, String tipo){
        long insertar = 0;
        try {
            BaseDatosGeneral dbHelper = new BaseDatosGeneral(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("correoUsuario", correo);
            values.put("DNIUsuario", dni);
            values.put("passwordUsuario", password);
            values.put("tipoCuenta", tipo);

            insertar = db.insert(TABLE_USUARIOS, null, values);
        }catch (Exception ex){
            ex.toString();
        }
        return insertar;
    }
}

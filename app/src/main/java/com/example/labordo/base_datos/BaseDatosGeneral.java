package com.example.labordo.base_datos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class BaseDatosGeneral extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "usuarios.db";
    public static final String TABLE_USUARIOS = "t_usuarios";


    public BaseDatosGeneral(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_USUARIOS + "(" +
                "correoUsuario TEXT PRIMARY KEY," +
                "DNIUsuario TEXT," +
                "passwordUsuario TEXT,"+
                "tipoCuenta TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_USUARIOS);
        onCreate(db);
    }
}

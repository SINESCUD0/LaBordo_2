package com.example.labordo.objetos;

import android.widget.Toast;

import java.io.File;
import java.sql.Blob;

public class LoginInfo {

    private static String dni;
    private static String nombre, apellidos;
    private static String correo;
    private static String pass;
    private static String contrasenia;
    private static String instituto;
    private static Blob imagenPerfil;
    private static boolean tipoCuenta;

    private static int saldoCuenta;


    public LoginInfo(){

    }

    public LoginInfo(String correo, String contrasenia, Blob imagenPerfil){
        this.correo = correo;
        this.contrasenia = contrasenia;
        this.imagenPerfil = imagenPerfil;
    }

    public LoginInfo(String dni, String nombre, String apellidos, String correo, String pass, String instituto, Blob imagenPerfil, boolean tipoCuenta, int saldoCuenta){
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.pass = pass;
        this.instituto = instituto;
        this.tipoCuenta = tipoCuenta;
        this.imagenPerfil = imagenPerfil;
        this.saldoCuenta = saldoCuenta;
    }

    public LoginInfo(String dni, String nombre, String apellidos, String correo, String pass, String instituto, Blob imagenPerfil, boolean tipoCuenta){
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.pass = pass;
        this.instituto = instituto;
        this.tipoCuenta = tipoCuenta;
        this.imagenPerfil = imagenPerfil;
        this.saldoCuenta = saldoCuenta;
    }

    public LoginInfo(String dni, String nombre, String apellidos, String correo, String pass, String instituto, boolean tipoCuenta){
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
        this.pass = pass;
        this.instituto = instituto;
        this.tipoCuenta = tipoCuenta;
    }

    public static int getSaldoCuenta() {
        return saldoCuenta;
    }



    public Blob getImagenPerfil() {
        return imagenPerfil;
    }

    public void setImagenPerfil(Blob imagenPerfil) {
        this.imagenPerfil = imagenPerfil;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        LoginInfo.correo = correo;
    }

    public boolean isTipoCuenta() {
        return tipoCuenta;
    }


    public String getDni() {
        return dni;
    }

    public void setSaldoCuenta(int saldoCuenta){this.saldoCuenta = saldoCuenta;};

    public void setDni(String dni) {
        LoginInfo.dni = dni;
    }

    public String getInstitutoLogin() {return instituto;}

    public static String getNombre() {return nombre;}
    public static void setNombre(String nombre) {LoginInfo.nombre = nombre;}

    public static String getApellidos2() {return apellidos;}
    public static void setApellidos2(String apellidos) {LoginInfo.apellidos = apellidos;}
}


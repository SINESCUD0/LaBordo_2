package com.example.labordo.objetos;

import android.net.Uri;

public class Profesorado extends LoginInfo {
    private String nombreProfesor;
    private String apellidosProfesor;
    private String instituto;
    private Uri fotoProfesor; //Imagen de la tarea
    private String DNI;
    private String correo;
    private String pass;
    private static boolean tipoCuenta;
    private int imagenActividad; //Actividad de la tarea

    public Profesorado(){

    }

    public Profesorado(String dni, String nombre, String apellidos, String correo, String pass, String instituto) {
        super(dni, nombre, apellidos, correo, pass, instituto, true);
        DNI = dni;
        nombreProfesor = nombre+" "+apellidos;
        this.correo = correo;
        this.pass = pass;
        this.instituto = instituto;
        this.tipoCuenta = tipoCuenta;
    }

    public Profesorado(String nombreProfesor, String instituto, String DNI,
                       String apellidosProfesor, String correo) {
        this.nombreProfesor = nombreProfesor;
        this.instituto = instituto;
        this.DNI = DNI;
        this.apellidosProfesor = apellidosProfesor;
        this.correo = correo;
    }

    public String getNombreProfesor() {return nombreProfesor;}
    public void setNombreProfesor(String nombreProfesor) {this.nombreProfesor = nombreProfesor;}

    public Uri getFotoProfesor() {return fotoProfesor;}
    public void setFotoProfesor(Uri fotoProfesor) {this.fotoProfesor = fotoProfesor;}

    public String getDNI() {return DNI;}
    public void setDNI(String DNI) {this.DNI = DNI;}

    public int getImagenActividad() {return imagenActividad;}
    public void setImagenActividad(int imagenActividad) {this.imagenActividad = imagenActividad;}

    public String getApellidosProfesor() {return apellidosProfesor;}
    public void setApellidosProfesor(String apellidosProfesor) {this.apellidosProfesor = apellidosProfesor;}

    public String getInstituto() {return instituto;}
    public void setInstituto(String instituto) {this.instituto = instituto;}


    public boolean isTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(boolean tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}

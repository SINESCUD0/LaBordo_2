package com.example.labordo.objetos;

import android.net.Uri;

public class Alumnado extends LoginInfo {
    //public static Object toBeDestroyed;
    private String nombreAlumno;
    private String cursoAlumno;
    private String correo;
    private String pass;
    private String instituto;

    private Uri fotoAlumno; //Imagen de la tarea
    private String DNI;
    private String fechaNacimiento;
    private int imagenActividad; //Actividad de la tarea

    public Alumnado() {

    }

    public Alumnado(String dni, String nombre, String apellidos, String correo, String pass, String instituto) {
        super(dni, nombre, apellidos, correo, pass, instituto, false);
        DNI = dni;
        nombreAlumno = nombre+" "+apellidos;
        this.correo = correo;
        this.pass = pass;
        this.instituto = instituto;
    }

    public Alumnado(String nombreAlumno, String cursoAlumno, Uri fotoAlumno, String DNI, String fechaNacimiento, int imagenActividad) {
        this.nombreAlumno = nombreAlumno;
        this.cursoAlumno = cursoAlumno;
        this.fotoAlumno = fotoAlumno;
        this.DNI = DNI;
        this.fechaNacimiento = fechaNacimiento;
        this.imagenActividad = imagenActividad;
    }


    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }

    public String getCursoAlumno() {
        return cursoAlumno;
    }

    public void setCursoAlumno(String cursoAlumno) {
        this.cursoAlumno = cursoAlumno;
    }


    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getInstituto() {
        return instituto;
    }

    public void setInstituto(String instituto) {
        this.instituto = instituto;
    }

    public Uri getFotoAlumno() {
        return fotoAlumno;
    }

    public void setFotoAlumno(Uri fotoAlumno) {
        this.fotoAlumno = fotoAlumno;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getImagenActividad() {
        return imagenActividad;
    }

    public void setImagenActividad(int imagenActividad) {
        this.imagenActividad = imagenActividad;
    }
}

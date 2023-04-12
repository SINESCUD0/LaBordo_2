package com.example.labordo.objetos;

import android.net.Uri;

public class Profesorado {
    private String nombreProfesor;
    private String cursoProfesor;
    private Uri fotoProfesor; //Imagen de la tarea
    private String DNI;
    private String fechaNacimiento;
    private int imagenActividad; //Actividad de la tarea

    public Profesorado(String nombreProfesor, String cursoAlumno, Uri fotoAlumno, String DNI, String fechaNacimiento, int imagenActividad) {
        this.nombreProfesor = nombreProfesor;
        this.cursoProfesor = cursoAlumno;
        this.fotoProfesor = fotoAlumno;
        this.DNI = DNI;
        this.fechaNacimiento = fechaNacimiento;
        this.imagenActividad = imagenActividad;
    }

    public String getNombreProfesor() {return nombreProfesor;}
    public void setNombreProfesor(String nombreProfesor) {this.nombreProfesor = nombreProfesor;}

    public String getCursoProfesor() {return cursoProfesor;}
    public void setCursoProfesor(String cursoProfesor) {this.cursoProfesor = cursoProfesor;}

    public Uri getFotoProfesor() {return fotoProfesor;}
    public void setFotoProfesor(Uri fotoProfesor) {this.fotoProfesor = fotoProfesor;}

    public String getDNI() {return DNI;}
    public void setDNI(String DNI) {this.DNI = DNI;}

    public String getFechaNacimiento() {return fechaNacimiento;}
    public void setFechaNacimiento(String fechaNacimiento) {this.fechaNacimiento = fechaNacimiento;}

    public int getImagenActividad() {return imagenActividad;}
    public void setImagenActividad(int imagenActividad) {this.imagenActividad = imagenActividad;}
}

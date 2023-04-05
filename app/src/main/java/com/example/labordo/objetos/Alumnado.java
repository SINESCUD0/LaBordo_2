package com.example.labordo.objetos;

import android.net.Uri;

public class Alumnado {
    private String nombreAlumno;
    private String cursoAlumno;
    private Uri fotoAlumno; //Imagen de la tarea
    private String DNI;
    private String fechaNacimiento;
    private int imagenActividad; //Actividad de la tarea

    public Alumnado(String nombreAlumno, String cursoAlumno, Uri fotoAlumno, String DNI, String fechaNacimiento, int imagenActividad) {
        this.nombreAlumno = nombreAlumno;
        this.cursoAlumno = cursoAlumno;
        this.fotoAlumno = fotoAlumno;
        this.DNI = DNI;
        this.fechaNacimiento = fechaNacimiento;
        this.imagenActividad = imagenActividad;
    }

    public String getNombreAlumno() {return nombreAlumno;}
    public void setNombreAlumno(String nombreAlumno) {this.nombreAlumno = nombreAlumno;}

    public String getCursoAlumno() {return cursoAlumno;}
    public void setCursoAlumno(String cursoAlumno) {this.cursoAlumno = cursoAlumno;}

    public Uri getFotoAlumno() {return fotoAlumno;}
    public void setFotoAlumno(Uri fotoAlumno) {this.fotoAlumno = fotoAlumno;}

    public String getDNI() {return DNI;}
    public void setDNI(String DNI) {this.DNI = DNI;}

    public String getFechaNacimiento() {return fechaNacimiento;}
    public void setFechaNacimiento(String fechaNacimiento) {this.fechaNacimiento = fechaNacimiento;}

    public int getImagenActividad() {return imagenActividad;}
    public void setImagenActividad(int imagenActividad) {this.imagenActividad = imagenActividad;}
}

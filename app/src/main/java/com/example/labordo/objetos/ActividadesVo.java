package com.example.labordo.objetos;

import android.net.Uri;

public class ActividadesVo {

    private String nombreTarea;
    private String descripcion;
    private Uri imagenTarea; //Imagen de la tarea
    private String precio;
    private String fecha;
    private Uri imagenActividad; //Actividad de la tarea

    public ActividadesVo(String nombreTarea, String descripcion, Uri imagenTarea, String precio, String fecha, Uri imagenActividad) {
        this.nombreTarea = nombreTarea;
        this.descripcion = descripcion;
        this.imagenTarea = imagenTarea;
        this.precio = precio;
        this.fecha = fecha;
        this.imagenActividad = imagenActividad;
    }

    public String getNombreTarea() {return nombreTarea;}
    public void setNombreTarea(String nombreTarea) {this.nombreTarea = nombreTarea;}

    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    public Uri getImagenTarea() {return imagenTarea;}
    public void setImagenTarea(Uri imagenTarea) {this.imagenTarea = imagenTarea;}

    public String getPrecio() {return precio;}
    public void setPrecio(String precio) {this.precio = precio;}

    public String getFecha() {return fecha;}
    public void setFecha(String fecha) {this.fecha = fecha;}

    public Uri getImagenActividad() {return imagenActividad;}
    public void setImagenActividad(Uri imagenActividad) {this.imagenActividad = imagenActividad;}
}

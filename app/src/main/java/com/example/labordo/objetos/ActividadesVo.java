package com.example.labordo.objetos;

public class ActividadesVo {

    private String nombreTarea;
    private String descripcion;
    private int imagenId;

    public ActividadesVo(String nombreTarea, String descripcion, int imagenId) {
        this.nombreTarea = nombreTarea;
        this.descripcion = descripcion;
        this.imagenId = imagenId;
    }

    public String getNombreTarea() {return nombreTarea;}
    public void setNombreTarea(String nombreTarea) {this.nombreTarea = nombreTarea;}

    public String getDescripcion() {return descripcion;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}

    public int getImagenId() {return imagenId;}
    public void setImagenId(int imagenId) {this.imagenId = imagenId;}
}

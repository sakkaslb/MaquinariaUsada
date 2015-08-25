package com.example.xupr44dlb.maquinariausada;

import java.util.Date;

/**
 * Created by Stefanie on 22/08/2015.
 */
public class Imagen {
    int id;
    String descripcion, url;
    Date fecha_modificacion;

    public Imagen(int id, String descripcion, String url, Date fecha_modificacion) {
        this.id = id;
        this.descripcion = descripcion;
        this.url = url;
        this.fecha_modificacion = fecha_modificacion;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getFecha_modificacion() {
        return fecha_modificacion;
    }

    public void setFecha_modificacion(Date fecha_modificacion) {
        this.fecha_modificacion = fecha_modificacion;
    }



}

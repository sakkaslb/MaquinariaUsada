package com.example.xupr44dlb.maquinariausada;

import java.util.Date;

public class Maquina {
    int id;
    String descripcion, familia, localizacion, modelo, serie, horas, garantia, anio;
    float precioSin, precioCertificado;
    Date fecha_modificacion;

    public Maquina(Integer id, String descripcion, String familia, String estado, String status, String localizacion, String modelo, String serie, String horas, String garantia, String anio, float precioSin, float precioCertificado, Date fecha_modificacion) {
        this.id = id;
        this.descripcion = descripcion;
        this.familia = familia;
        this.localizacion = localizacion;
        this.modelo = modelo;
        this.serie = serie;
        this.horas = horas;
        this.garantia = garantia;
        this.anio = anio;
        this.precioSin = precioSin;
        this.precioCertificado = precioCertificado;
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

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(String localizacion) {
        this.localizacion = localizacion;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getHoras() {
        return horas;
    }

    public void setHoras(String horas) {
        this.horas = horas;
    }

    public String getGarantia() {
        return garantia;
    }

    public void setGarantia(String garantia) {
        this.garantia = garantia;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public float getPrecioSin() {
        return precioSin;
    }

    public void setPrecioSin(float precioSin) {
        this.precioSin = precioSin;
    }

    public float getPrecioCertificado() {
        return precioCertificado;
    }

    public void setPrecioCertificado(float precioCertificado) {
        this.precioCertificado = precioCertificado;
    }

    public Date getFecha_modificacion() {
        return fecha_modificacion;
    }

    public void setFecha_modificacion(Date fecha_modificacion) {
        this.fecha_modificacion = fecha_modificacion;
    }



}

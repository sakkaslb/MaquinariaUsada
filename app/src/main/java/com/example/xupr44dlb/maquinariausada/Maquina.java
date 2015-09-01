package com.example.xupr44dlb.maquinariausada;

import java.util.Date;

public class Maquina {
    int id;
    String descripcion;
    String familia;
    String localizacion;
    String modelo;
    String serie;
    Integer horas;
    String garantia;
    Integer anio;
    String link;
    float precioSin;
    float precioCertificado;
    float preciocredito;
    String fecha_modificacion;

    public Maquina()
    {

    }
    public Maquina(Integer id, String descripcion, String familia, String estado, String status, String localizacion, String modelo, String serie, Integer horas, String garantia, Integer anio, float precioSin, float precioCertificado, String fecha_modificacion) {
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
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
    public float getPreciocredito() {
        return preciocredito;
    }

    public void setPreciocredito(float preciocredito) {
        this.preciocredito = preciocredito;
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

    public Integer getHoras() {
        return horas;
    }

    public void setHoras(Integer horas) {
        this.horas = horas;
    }

    public String getGarantia() {
        return garantia;
    }

    public void setGarantia(String garantia) {
        this.garantia = garantia;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
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

    public String getFecha_modificacion() {
        return fecha_modificacion;
    }

    public void setFecha_modificacion(String fecha_modificacion) {
        this.fecha_modificacion = fecha_modificacion;
    }



}

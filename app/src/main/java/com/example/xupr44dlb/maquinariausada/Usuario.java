package com.example.xupr44dlb.maquinariausada;


public class Usuario {
    public Usuario(String usuario, String password, String email, String telefono) {
        this.usuario = usuario;
        this.password = password;
        this.email = email;
        this.telefono = telefono;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    String usuario, password, email, telefono;
}

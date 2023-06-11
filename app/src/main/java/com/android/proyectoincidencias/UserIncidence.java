package com.android.proyectoincidencias;

public class UserIncidence {
    private String id;
    private String descripcion;
    private String imagen;
    private String fecha;
    private String estado;
    private String nombre;
    private String token;

    public UserIncidence(String id, String descripcion, String imagen, String fecha, String estado, String nombre, String token) {
        this.id = id;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.fecha = fecha;
        this.estado = estado;
        this.nombre = nombre;
        this.token = token;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

package com.dominio;

public class Permiso {
    private Integer idPermiso;
    private String nombre;
    private String descripcion;

    public Permiso(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public boolean esValido() {
        return nombre != null && !nombre.trim().isEmpty() 
            && nombre.length() >= 3 && nombre.length() <= 50
            && descripcion != null && !descripcion.trim().isEmpty()
            && descripcion.length() >= 10 && descripcion.length() <= 200;
    }

    public Integer getIdPermiso() { return idPermiso; }
    public void setIdPermiso(Integer idPermiso) { this.idPermiso = idPermiso; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}
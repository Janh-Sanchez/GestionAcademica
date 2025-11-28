package com.dominio;

import java.util.HashSet;
import java.util.Set;

public class Rol {
    private Integer idRol;
    private String nombre;
    private Set<Permiso> permisos;

    public Rol(String nombre) {
        this.nombre = nombre;
        this.permisos = new HashSet<>();
    }

    // Validar rol
    public boolean esValido() {
        return nombre != null && 
               !nombre.trim().isEmpty() && 
               nombre.length() >= 3 && 
               nombre.length() <= 50 &&
               !permisos.isEmpty();
    }

    public Integer getIdRol() { return idRol; }
    public void setIdRol(Integer idRol) { this.idRol = idRol; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Set<Permiso> getPermisos() { return new HashSet<>(permisos); }
    public void setPermisos(Set<Permiso> permisos) { this.permisos = permisos; }
}
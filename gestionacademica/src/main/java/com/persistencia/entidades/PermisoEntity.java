package com.persistencia.entidades;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permisos")
public class PermisoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_permiso")
    private Integer idPermiso;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "descripcion", nullable = false, length = 200)
    private String descripcion;

    @ManyToMany(mappedBy = "permisos")
    private Set<RolEntity> roles = new HashSet<>();

    public PermisoEntity() {}

    // Getters y Setters
    public Integer getIdPermiso() { return idPermiso; }
    public void setIdPermiso(Integer idPermiso) { this.idPermiso = idPermiso; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Set<RolEntity> getRoles() { return roles; }
    public void setRoles(Set<RolEntity> roles) { this.roles = roles; }
}
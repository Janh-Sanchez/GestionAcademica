package com.persistencia.entidades;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class RolEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "rol_permiso",
        joinColumns = @JoinColumn(name = "id_rol"),
        inverseJoinColumns = @JoinColumn(name = "id_permiso")
    )
    private Set<PermisoEntity> permisos = new HashSet<>();

    @OneToMany(mappedBy = "rol")
    private Set<TokenUsuarioEntity> tokens = new HashSet<>();

    public RolEntity() {}

    // Getters y Setters
    public Integer getIdRol() { return idRol; }
    public void setIdRol(Integer idRol) { this.idRol = idRol; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Set<PermisoEntity> getPermisos() { return permisos; }
    public void setPermisos(Set<PermisoEntity> permisos) { this.permisos = permisos; }
    public Set<TokenUsuarioEntity> getTokens() { return tokens; }
    public void setTokens(Set<TokenUsuarioEntity> tokens) { this.tokens = tokens; }
}
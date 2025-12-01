package com.persistencia.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "tokens_usuario")
public class TokenUsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_token")
    private Integer idToken;

    @Column(name = "nombre_usuario", nullable = false, unique = true, length = 50)
    private String nombreUsuario;

    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol", nullable = false)
    private RolEntity rol;

    @OneToOne(mappedBy = "tokenAccess", fetch = FetchType.LAZY)
    private UsuarioEntity usuario;

    public TokenUsuarioEntity() {}

    // Getters y Setters
    public Integer getIdToken() { return idToken; }
    public void setIdToken(Integer idToken) { this.idToken = idToken; }
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public RolEntity getRol() { return rol; }
    public void setRol(RolEntity rol) { this.rol = rol; }
    public UsuarioEntity getUsuario() { return usuario; }
    public void setUsuario(UsuarioEntity usuario) { this.usuario = usuario; }
}
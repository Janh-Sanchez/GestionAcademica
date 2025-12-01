package com.persistencia.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity(name = "usuario")
@Inheritance(strategy = InheritanceType.JOINED)
public class UsuarioEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Basic
    @Size(min = 2, max = 30)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ]+$", message = "El nombre no debe contener números")
    @Column(nullable = false, length = 30)
    private String primerNombre;

    @Size(min = 2, max = 30)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ]+$", message = "El nombre no debe contener números")
    @Column(nullable = true, length = 30)
    private String segundoNombre;

    @Size(min = 2, max = 30)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ]+$", message = "El nombre no debe contener números")
    @Column(nullable = false, length = 30)
    private String primerApellido;

    @Size(min = 2, max = 30)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ]+$", message = "El nombre no debe contener números")
    @Column(nullable = true, length = 30)
    private String segundoApellido;

    @Email
    @Column(nullable = false, unique = true)
    private String correoElectronico;

    @Min(18)
    @Max(80)
    @Column(nullable = false)
    private int edad;

    @Pattern(regexp = "^[0-9]{10}$", message = "Debe tener 10 dígitos numéricos")
    @Column(nullable = false, unique = true, length = 10)
    private String telefono;

    @OneToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "tokenAccess", referencedColumnName = "id_token", nullable = true)
    private TokenUsuarioEntity tokenAccess;

    // ✅ SOLO getters y setters
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String primerNombre) { this.primerNombre = primerNombre; }
    public String getSegundoNombre() { return segundoNombre; }
    public void setSegundoNombre(String segundoNombre) { this.segundoNombre = segundoNombre; }
    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }
    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public TokenUsuarioEntity getTokenAccess() { return tokenAccess; }
    public void setTokenAccess(TokenUsuarioEntity tokenAccess) { this.tokenAccess = tokenAccess; }
}
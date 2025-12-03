package com.dominio;

public class Usuario {
    private Integer idUsuario;
    private String nuipUsuario;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private Integer edad;
    private String correoElectronico;
    private String telefono;
    private TokenUsuario tokenAccess;

    public Usuario(Integer idUsuario, String nuipUsuario, String primerNombre, String segundoNombre, String primerApellido,
            String segundoApellido, int edad, String correoElectronico, String telefono,
            TokenUsuario tokenAccess){
        this.idUsuario = idUsuario;
        this.nuipUsuario = nuipUsuario;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.edad = edad;
        this.correoElectronico = correoElectronico;
        this.telefono = telefono;
        this.tokenAccess = tokenAccess;
    }

    public Usuario() {
    }

	public String obtenerNombreCompleto() {
        StringBuilder nombre = new StringBuilder(primerNombre);
        if (segundoNombre != null && !segundoNombre.isEmpty()) {
            nombre.append(" ").append(segundoNombre);
        }
        nombre.append(" ").append(primerApellido);
        if (segundoApellido != null && !segundoApellido.isEmpty()) {
            nombre.append(" ").append(segundoApellido);
        }
        return nombre.toString();
    }

    // Getters y Setters
    public Integer getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Integer idUsuario) { this.idUsuario = idUsuario; }
    public String getNuipUsuario() { return nuipUsuario; }
    public void setNuipUsuario(String nuipUsuario) { this.nuipUsuario = nuipUsuario; }
    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String primerNombre) { this.primerNombre = primerNombre; }
    public String getSegundoNombre() { return segundoNombre; }
    public void setSegundoNombre(String segundoNombre) { this.segundoNombre = segundoNombre; }
    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }
    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }
    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }
    public String getCorreoElectronico() { return correoElectronico; }
    public void setCorreoElectronico(String correoElectronico) { this.correoElectronico = correoElectronico; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public TokenUsuario getTokenAccess() { return tokenAccess; }
    public void setTokenAccess(TokenUsuario tokenAccess) { this.tokenAccess = tokenAccess; }
}

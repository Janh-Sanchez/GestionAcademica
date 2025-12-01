package com.dominio;

public class TokenUsuario {
    private Integer idToken;
    private String nombreUsuario;
    private String contrasena;
    private Rol rol;

    public TokenUsuario(Integer idToken, String nombreUsuario, String contrasena, Rol rol) {
        this.idToken = idToken;
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
        this.rol = rol;
    }

    public TokenUsuario() {
    }

    // Verificar credenciales
    public boolean verificarCredenciales(String contrasenaPrueba) {

        if (contrasenaPrueba == null || contrasenaPrueba.isEmpty()) {
            return false;
        }
        
        boolean credencialesCorrectas = this.contrasena.equals(contrasenaPrueba);
        return credencialesCorrectas;
    }

    // Getters y Setters
    public Integer getIdToken() { return idToken; }
    public void setIdToken(Integer idToken) { this.idToken = idToken; }
    
    public String getNombreUsuario() { return nombreUsuario; }
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    
    public String getContrasena() { return contrasena; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }

}
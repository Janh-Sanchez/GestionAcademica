package com.dominio;

public abstract class Usuario {

	private String correoElectronico;
	private int edad;
	private Integer idUsuario;
	private String primerApellido;
	private String primerNombre;
	private String segundoApellido;
	private String segundoNombre;
	private String telefono;
	private TokenUsuario tokenAccess;

	public Usuario(){

	}

	public void finalize() throws Throwable {

	}
	/**
	 * 
	 * @param usuario
	 * @param contrasena
	 */
	public boolean iniciarSesion(String usuario, String contrasena){
		return false;
	}
}//end Usuario
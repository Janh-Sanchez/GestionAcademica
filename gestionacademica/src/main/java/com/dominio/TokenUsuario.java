package com.dominio;

public class TokenUsuario {

	private String contrasena;
	private boolean estado;
	private Integer idToken;
	private String nombreUsuario;
	private Rol rol;

	public TokenUsuario(){

	}

	public void finalize() throws Throwable {

	}
	/**
	 * 
	 * @param usuario
	 * @param contrasena
	 */
	public boolean validarCredenciales(String usuario, String contrasena){
		return false;
	}
}//end TokenUsuario
package com.dominio;

import java.util.Set;

public class Rol {

	private Integer idRol;
	private String nombre;
	private Set<Permiso> permisos;

	public Rol(){

	}

	public void finalize() throws Throwable {

	}
	/**
	 * 
	 * @param permiso
	 */
	public void agregarPermiso(Permiso permiso){

	}

	/**
	 * 
	 * @param permiso
	 */
	public boolean tienePermiso(Permiso permiso){
		return false;
	}
}//end Rol
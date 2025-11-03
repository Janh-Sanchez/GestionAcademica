package com.dominio;

import java.util.HashMap;
import java.util.Map;

public class Grado {

	private HashMap<Integer, BibliotecaLogros> bibliotecaLogros;
	private HashMap<Integer, Grupo> grupos;
	private Integer idGrado;
	private String nombreGrado;

	public Grado(){

	}

	public void finalize() throws Throwable {

	}
	/**
	 * 
	 * @param grupo
	 */
	public void agregarGrupo(Grupo grupo){

	}

	/**
	 * 
	 * @param grupo
	 */
	public void eliminarGrupo(Grupo grupo){

	}

	public Map<String, Grupo> obtenerGrupos(){
		return null;
	}
}//end Grado
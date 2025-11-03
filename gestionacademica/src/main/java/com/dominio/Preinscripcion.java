package com.dominio;

import java.time.LocalDate;
import java.util.SortedSet;

public class Preinscripcion {

	private Acudiente acudiente;
	private Estado estado = Estado.Pendiente;
	private SortedSet<Estudiante> estudiante;
	private LocalDate fechaRegistro;
	private Integer idPreinscripcion;

	public Preinscripcion(){

	}

	public void finalize() throws Throwable {

	}
	/**
	 * 
	 * @param nuevoEstado
	 */
	public void cambiarEstado(Estado nuevoEstado){

	}

	public boolean validarDatos(){
		return false;
	}
}//end Preinscripcion
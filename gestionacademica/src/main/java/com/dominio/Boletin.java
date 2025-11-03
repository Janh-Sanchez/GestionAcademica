package com.dominio;

import java.time.LocalDate;
import java.util.HashMap;

public class Boletin {

	private Estudiante estudiante;
	private LocalDate fechaGeneracion;
	private Integer idBoletin;
	private HashMap<Integer, LogroEstudiante> logrosEstudiante;
	private String periodo;

	public Boletin(){

	}

	public void finalize() throws Throwable {

	}
	/**
	 * 
	 * @param logroEstudiante
	 */
	public void añadirLogroEstudiante(LogroEstudiante logroEstudiante){

	}

	/**
	 * 
	 * @param logroEstudiante
	 */
	public void eliminarCalificacion(LogroEstudiante calificacion){

	}

	public void generarBoletin(){

	}
}//end Boletin
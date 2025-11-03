package com.dominio;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.SortedSet;

public class Grupo {

	private boolean estado;
	private SortedSet<Estudiante> estudiantes;
	private Integer idGrupo;
	private int maxEstudiantes = 10;
	private int minEstudiantes = 5;
	private String nombreGrupo;
	private Profesor profesor;

	public Grupo(){

	}

	public void finalize() throws Throwable {

	}
	/**
	 * 
	 * @param estudiante
	 */
	public void agregarEstudiante(Estudiante estudiante){

	}

	public boolean tieneEstudiantesSuficientes(){
		return false;
	}
}//end Grupo
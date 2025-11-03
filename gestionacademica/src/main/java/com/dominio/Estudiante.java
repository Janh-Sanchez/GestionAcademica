package com.dominio;

import java.time.LocalDate;
import java.util.HashMap;

public class Estudiante {

	private Acudiente acudiente;
	private HashMap<Integer, Boletin> boletines;
	private int edad;
	private Estado estado = Estado.Pendiente;
	private Grado gradoAspira;
	private Grupo grupo;
	private HojaVida hojaDeVida;
	private Integer idEstudiante;
	private HashMap<Integer, LogroEstudiante> logrosAsignados;
	private String nuip;
	private Observador observador;
	private String primerApellido;
	private String primerNombre;
	private String segundoApellido;
	private String segundoNombre;
	private LogroEstudiante logrosCalificados;
	private Boletin boletin;

	public Estudiante(){

	}

	public void finalize() throws Throwable {

	}
}//end Estudiante
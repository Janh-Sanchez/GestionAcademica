package com.dominio;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Set;

/**
 * @author lynna
 * @version 1.0
 * @created 03-nov.-2025 5:01:43 p.�m.
 */
public class Citacion {

	private HashMap<Integer, Acudiente> acudiente;
	private LocalDate fechaCitacion;
	private Integer idCitacion;
	private String motivo;
	private Set<Notificacion> notificaciones;

	public Citacion(){

	}

	public void finalize() throws Throwable {

	}
	/**
	 * 
	 * @param acudiente
	 */
	public void agregarAcudiente(Acudiente acudiente){

	}

	/**
	 * 
	 * @param acudientes
	 */
	public void generarNotificacion(Acudiente acudientes){

	}
}//end Citacion
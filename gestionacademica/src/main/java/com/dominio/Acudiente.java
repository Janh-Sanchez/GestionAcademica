package com.dominio;

import java.util.Set;

public class Acudiente extends Usuario {

	private Estado estadoAprobacion = Estado.Pendiente;
	private Set<Estudiante> estudiantes;

	public Acudiente(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
	public void agregarEstudiante(){

	}
}//end Acudiente
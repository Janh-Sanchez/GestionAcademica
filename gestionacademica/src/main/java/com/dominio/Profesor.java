package com.dominio;

public class Profesor extends Usuario {

	private Grupo grupoAsignado;
	public LogroEstudiante m_LogroEstudiante;

	public Profesor(){

	}

	public void finalize() throws Throwable {
		super.finalize();
	}
}//end Profesor
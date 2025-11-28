package com.dominio;

import java.util.Set;

public class Acudiente extends Usuario {

	private Estado estadoAprobacion = Estado.Pendiente;
	private Set<Estudiante> estudiantes;

	public Acudiente(Integer idUsuario, String primerNombre, String segundoNombre, String primerApellido, 
        String segundoApellido, int edad, String correoElectronico, String telefono, TokenUsuario tokenAccess, Estado estadoAprobacion,  Set<Estudiante> estudiantes){
		super(idUsuario, primerNombre, segundoNombre, primerApellido, segundoApellido, edad, correoElectronico, telefono, tokenAccess);
		this.estadoAprobacion = estadoAprobacion;
		this.estudiantes = estudiantes;
	}

	public Acudiente() {
		super();
    }

    public void agregarEstudiante(Estudiante estudiante) throws Exception{
		if(this.estudiantes.size() >= 5){
			throw new Exception("Un acudiente no puede tener mas de 5 estudiantes");
		}
		if(estudiante == null){
			throw new IllegalArgumentException("Estudiante no puede ser nulo");
		}
		estudiantes.add(estudiante);
	}

	public Estado getEstadoAprobacion(){
		return estadoAprobacion;
	}

	public void setEstadoAprobacion(Estado estado){
		this.estadoAprobacion = estado;
	}

	public Set<Estudiante> getEstudiantes(){
		return estudiantes;
	}
}
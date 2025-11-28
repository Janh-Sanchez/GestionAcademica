package com.persistencia.entidades;

import jakarta.persistence.*;

@Entity(name = "profesor")
public class ProfesorEntity extends UsuarioEntity {

	@OneToOne(mappedBy = "profesor", fetch = FetchType.LAZY, optional = true)
	private GrupoEntity grupoAsignado;

	public GrupoEntity getGrupoAsignado(){
		return grupoAsignado;
	}

	public void setGrupoAsignado(GrupoEntity grupo){
		this.grupoAsignado = grupo;
	}
}
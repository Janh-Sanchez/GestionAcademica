package com.dominio;

public class Profesor extends Usuario{
    private Grupo grupoAsignado;
	public Profesor(Integer idUsuario, String primerNombre, String segundoNombre, String primerApellido, 
        String segundoApellido, String correoElectronico, int edad, String telefono, TokenUsuario tokenAccess,
        Grupo grupoAsignado){
		super(idUsuario, primerNombre, segundoNombre, primerApellido, segundoApellido, edad, correoElectronico, telefono, tokenAccess);
        this.grupoAsignado = grupoAsignado;
	}

    public Profesor(){
        super();
    }

    public Grupo getGrupo(){
        return grupoAsignado;
    }

    public boolean tieneGrupoAsignado(Grupo grupo){
        if(grupoAsignado != null){
            return true;
        }
        return false;
    }

    public void setGrupo(Grupo grupo){
        if(tieneGrupoAsignado(grupo)){
            System.out.println("El profesor ya tiene un grupo asignado");
        }
        else{
            this.grupoAsignado = grupo;
        }
    }
}

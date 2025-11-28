package com.dominio;

public class Administrador extends Usuario{
	public Administrador(Integer idUsuario, String primerNombre, String segundoNombre, String primerApellido, 
        String segundoApellido, String correoElectronico, int edad, String telefono, TokenUsuario tokenAccess){
		super(idUsuario, primerNombre, segundoNombre, primerApellido, segundoApellido, edad, correoElectronico, telefono, tokenAccess);
    }
}

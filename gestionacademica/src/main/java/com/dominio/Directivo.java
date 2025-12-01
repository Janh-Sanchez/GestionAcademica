package com.dominio;

public class Directivo extends Usuario{
    public Directivo(Integer idUsuario, String primerNombre, String segundoNombre, 
                    String primerApellido, String segundoApellido, int edad, 
                    String correoElectronico, String telefono, TokenUsuario tokenAccess) {
        super(idUsuario, primerNombre, segundoNombre, primerApellido, segundoApellido, 
              edad, correoElectronico, telefono, tokenAccess);
    }

    public Directivo() {
        //TODO Auto-generated constructor stub
    }
}

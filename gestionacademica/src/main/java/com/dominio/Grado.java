package com.dominio;

import java.util.Set;

public class Grado{
    private Integer idGrado;
    private String nombreGrado;
    private Set<BibliotecaLogros> bibliotecaLogros;
	private Set<Grupo> grupos;

    public Grado(Integer idGrado, String nombreGrado, Set<BibliotecaLogros> bibliotecaLogros, Set<Grupo> grupos) {
        this.idGrado = idGrado;
        this.nombreGrado = nombreGrado;
        this.bibliotecaLogros = bibliotecaLogros;
        this.grupos = grupos;
    }

    public Integer getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(Integer idGrado) {
        this.idGrado = idGrado;
    }

    public String getNombreGrado() {
        return nombreGrado;
    }

    public void setNombreGrado(String nombreGrado) {
        this.nombreGrado = nombreGrado;
    }

    public Set<BibliotecaLogros> getBibliotecaLogros() {
        return bibliotecaLogros;
    }

    public void setBibliotecaLogros(Set<BibliotecaLogros> bibliotecaLogros) {
        this.bibliotecaLogros = bibliotecaLogros;
    }

    public Set<Grupo> getGrupos() {
        return grupos;
    }

    public void setGrupos(Set<Grupo> grupos) {
        this.grupos = grupos;
    }


    public void agregarGrupo(Grupo grupo){
        grupos.add(grupo);
    }

    public void eliminarGrupo(Grupo grupo){
        grupos.remove(grupo);
    }
}

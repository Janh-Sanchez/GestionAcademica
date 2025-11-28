package com.dominio;

import java.util.Set;

public class BibliotecaLogros {
    private Integer idBibliotecaLogros;
    private String categoria;
    private Grado grado;
    private Set<Logro> logros;

    public BibliotecaLogros(Integer idBibliotecaLogros, String categoria, Grado grado, Set<Logro> logros) {
        this.idBibliotecaLogros = idBibliotecaLogros;
        this.categoria = categoria;
        this.grado = grado;
        this.logros = logros;
    }

    public Integer getIdBibliotecaLogros() {
        return idBibliotecaLogros;
    }

    public void setIdBibliotecaLogros(Integer idBibliotecaLogros) {
        this.idBibliotecaLogros = idBibliotecaLogros;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Grado getGrado() {
        return grado;
    }

    public void setGrado(Grado grado) {
        this.grado = grado;
    }

    public Set<Logro> getLogros() {
        return logros;
    }

    public void setLogros(Set<Logro> logros) {
        this.logros = logros;
    }


    public void añadirLogro(Logro logro){
        logros.add(logro);
    }
}

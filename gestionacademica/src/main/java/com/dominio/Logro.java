package com.dominio;

public class Logro {
    private Integer idLogro;
    private String descripcion;
    private BibliotecaLogros bibliotecaLogros;

    public Logro(Integer idLogro, String descripcion, BibliotecaLogros bibliotecaLogros) {
        this.idLogro = idLogro;
        this.descripcion = descripcion;
        this.bibliotecaLogros = bibliotecaLogros;
    }

    public Integer getIdLogro() {
        return idLogro;
    }

    public void setIdLogro(Integer idLogro) {
        this.idLogro = idLogro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BibliotecaLogros getBibliotecaLogros() {
        return bibliotecaLogros;
    }

    public void setBibliotecaLogros(BibliotecaLogros bibliotecaLogros) {
        this.bibliotecaLogros = bibliotecaLogros;
    }

    public boolean esValida(){
        return (descripcion.length() >= 10 && descripcion.length() <= 200);
    }
}
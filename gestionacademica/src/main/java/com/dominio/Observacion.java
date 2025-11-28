package com.dominio;

import java.time.LocalDate;

public class Observacion {
    private Integer idObservacion;
    private String descripcion;
    private LocalDate fechaObservacion;
    private Observador observador;
    private Profesor profesor;

    public Observacion(Integer idObservacion, String descripcion, LocalDate fechaObservacion, Observador observador, Profesor profesor){
        this.idObservacion = idObservacion;
        this.descripcion = descripcion;
        this.fechaObservacion = fechaObservacion;
        this.observador = observador;
        this.profesor = profesor;
    }

    // getters y setters
    public Integer getIdObservacion() {
        return idObservacion;
    }

    public void setIdObservacion(Integer idObservacion) {
        this.idObservacion = idObservacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDate getFechaObservacion() {
        return fechaObservacion;
    }

    public void setFechaObservacion(LocalDate fechaObservacion) {
        this.fechaObservacion = fechaObservacion;
    }

    public Observador getObservador() {
        return observador;
    }

    public void setObservador(Observador observador) {
        this.observador = observador;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public boolean esValida(){
        return (descripcion.length() >= 10 && descripcion.length() <= 200);
    }
}

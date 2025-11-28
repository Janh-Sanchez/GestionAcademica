package com.dominio;

import java.util.SortedSet;

public class Observador {
    private Integer idObservador;
    private Estudiante estudiante;
    private SortedSet<Observacion> observaciones;

    public Observador(Integer idObservador, Estudiante estudiante, SortedSet<Observacion> observaciones){
        this.idObservador = idObservador;
        this.estudiante = estudiante;
        this.observaciones = observaciones;
    }

    public Integer getIdObservador() {
        return idObservador;
    }

    public void setIdObservador(Integer idObservador) {
        this.idObservador = idObservador;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public SortedSet<Observacion> getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(SortedSet<Observacion> observaciones) {
        this.observaciones = observaciones;
    }

    public void agregarObservacion(Observacion observacion){
        observaciones.add(observacion);
    }
}
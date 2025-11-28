package com.dominio;

import java.time.LocalDate;

public class LogroEstudiante {
    private Integer idLogroEstudiante;
    private LocalDate fechaCalificacion;
    private Estudiante estudiante;
    private Boletin boletin;
    private Logro logro;
    private Profesor profesor;

    public LogroEstudiante(Integer idLogroEstudiante, LocalDate fechaCalificacion, Estudiante estudiante,
            Boletin boletin, Logro logro, Profesor profesor) {
        this.idLogroEstudiante = idLogroEstudiante;
        this.fechaCalificacion = fechaCalificacion;
        this.estudiante = estudiante;
        this.boletin = boletin;
        this.logro = logro;
        this.profesor = profesor;
    }

    public Integer getIdLogroEstudiante() {
        return idLogroEstudiante;
    }

    public void setIdLogroEstudiante(Integer idLogroEstudiante) {
        this.idLogroEstudiante = idLogroEstudiante;
    }

    public LocalDate getFechaCalificacion() {
        return fechaCalificacion;
    }

    public void setFechaCalificacion(LocalDate fechaCalificacion) {
        this.fechaCalificacion = fechaCalificacion;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Boletin getBoletin() {
        return boletin;
    }

    public void setBoletin(Boletin boletin) {
        this.boletin = boletin;
    }

    public Logro getLogro() {
        return logro;
    }

    public void setLogro(Logro logro) {
        this.logro = logro;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }
}
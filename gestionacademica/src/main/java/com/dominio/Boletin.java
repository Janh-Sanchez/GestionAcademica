package com.dominio;
import java.time.LocalDate;
import java.util.Set;

public class Boletin {
    private Integer idBoletin;
    private String periodo;
    private LocalDate fechaGeneracion;
    private Estudiante estudiante;
    private Set<LogroEstudiante> logrosEstudiante;

    public Boletin(Integer idBoletin, String periodo, LocalDate fechaGeneracion, Estudiante estudiante, Set<LogroEstudiante> logrosEstudiante) {
        this.idBoletin = idBoletin;
        this.periodo = periodo;
        this.fechaGeneracion = fechaGeneracion;
        this.estudiante = estudiante;
        this.logrosEstudiante = logrosEstudiante;
    }

    public Integer getIdBoletin() {
        return idBoletin;
    }

    public void setIdBoletin(Integer idBoletin) {
        this.idBoletin = idBoletin;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Set<LogroEstudiante> getLogrosEstudiante() {
        return logrosEstudiante;
    }

    public void setLogrosEstudiante(Set<LogroEstudiante> logrosEstudiante) {
        this.logrosEstudiante = logrosEstudiante;
    }

    public void añadirLogroEstudiante(LogroEstudiante logroEstudiante){
        if (logrosEstudiante != null) {
            logrosEstudiante.add(logroEstudiante);
        }
    }

    public void eliminarCalificacion(LogroEstudiante calificacion){
        if (logrosEstudiante != null) {
            logrosEstudiante.remove(calificacion);
        }
    }

    public void generarBoletin(){
        // Lógica de generación pendiente
    }
}
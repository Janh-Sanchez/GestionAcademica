package com.dominio;

import java.time.LocalDate;
import java.util.SortedSet;

public class Preinscripcion{
    private Integer idPreinscripcion;
    private LocalDate fechaRegistro;
    private Estado estado = Estado.Pendiente;
    private Acudiente acudiente;
	private SortedSet<Estudiante> estudiantes;

    public Preinscripcion(Integer idPreinscripcion, LocalDate fechaRegistro, Estado estado, Acudiente acudiente, SortedSet<Estudiante> estudiantes) {
        this.idPreinscripcion = idPreinscripcion;
        this.fechaRegistro = fechaRegistro;
        this.estado = estado;
        this.acudiente = acudiente;
        this.estudiantes = estudiantes;
    }

    public Integer getIdPreinscripcion() {
        return idPreinscripcion;
    }

    public void setIdPreinscripcion(Integer idPreinscripcion) {
        this.idPreinscripcion = idPreinscripcion;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Acudiente getAcudiente() {
        return acudiente;
    }

    public void setAcudiente(Acudiente acudiente) {
        this.acudiente = acudiente;
    }

    public SortedSet<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(SortedSet<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    public void cambiarEstado(Estado nuevoEstado){
        
    }

    public boolean validarDatos(){
        return false;
    }
}
package com.dominio;

import java.util.Set;

public class Grupo {
    private final int MINESTUDIANTES = 5;
    private final int MAXESTUDIANTES = 10;

    private Integer idGrupo;
    private String nombreGrupo;
    private boolean estado;
    private Grado grado;
	private Profesor profesor;
    private Set<Estudiante> estudiantes;

    public Grupo(Integer idGrupo, String nombreGrupo, boolean estado, Grado grado, Profesor profesor, Set<Estudiante> estudiantes) {
        this.idGrupo = idGrupo;
        this.nombreGrupo = nombreGrupo;
        this.estado = estado;
        this.grado = grado;
        this.profesor = profesor;
        this.estudiantes = estudiantes;
    }

    public int getMINESTUDIANTES() {
        return MINESTUDIANTES;
    }

    public int getMAXESTUDIANTES() {
        return MAXESTUDIANTES;
    }

    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    public String getNombreGrupo() {
        return nombreGrupo;
    }

    public void setNombreGrupo(String nombreGrupo) {
        this.nombreGrupo = nombreGrupo;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public Grado getGrado() {
        return grado;
    }

    public void setGrado(Grado grado) {
        this.grado = grado;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public Set<Estudiante> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(Set<Estudiante> estudiantes) {
        this.estudiantes = estudiantes;
    }

    public boolean tieneEstudiantesSuficientes(){
        return (estudiantes.size() >= MINESTUDIANTES);
    }

    public boolean tieneDisponibilidad(){
        return (estudiantes.size() < MAXESTUDIANTES);
    }
}
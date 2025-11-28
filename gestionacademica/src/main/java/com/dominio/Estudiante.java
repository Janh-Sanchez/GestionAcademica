package com.dominio;

import java.util.Set;

public class Estudiante {

    private Integer idEstudiante;
    private String primerNombre;
    private String segundoNombre;
    private String primerApellido;
    private String segundoApellido;
    private String nuip;
    private int edad;
    private Estado estado = Estado.Pendiente;
    private Acudiente acudiente;
    private Grado gradoAspira;
    private Grupo grupo;
    private HojaVida hojaDeVida;
    private Observador observador;
    private Set<LogroEstudiante> logrosCalificados;
    private Set<Boletin> boletines;
    private Preinscripcion preinscripcion;

    public Estudiante() {
    }

    public Estudiante(Integer idEstudiante, String primerNombre, String segundoNombre, String primerApellido,
        String segundoApellido, String nuip, int edad, Estado estado, Acudiente acudiente,
        Grado gradoAspira, Grupo grupo, HojaVida hojaDeVida, Observador observador,
        Set<LogroEstudiante> logrosCalificados, Set<Boletin> boletines, Preinscripcion preinscripcion) {
        this.idEstudiante = idEstudiante;
        this.primerNombre = primerNombre;
        this.segundoNombre = segundoNombre;
        this.primerApellido = primerApellido;
        this.segundoApellido = segundoApellido;
        this.nuip = nuip;
        this.edad = edad;
        this.estado = estado;
        this.acudiente = acudiente;
        this.gradoAspira = gradoAspira;
        this.grupo = grupo;
        this.hojaDeVida = hojaDeVida;
        this.observador = observador;
        this.logrosCalificados = logrosCalificados;
        this.boletines = boletines;
        this.preinscripcion = preinscripcion;
    }

    public Integer getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(Integer idEstudiante) { this.idEstudiante = idEstudiante; }
    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String primerNombre) { this.primerNombre = primerNombre; }
    public String getSegundoNombre() { return segundoNombre; }
    public void setSegundoNombre(String segundoNombre) { this.segundoNombre = segundoNombre; }
    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }
    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }
    public String getNuip() { return nuip; }
    public void setNuip(String nuip) { this.nuip = nuip; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public Acudiente getAcudiente() { return acudiente; }
    public void setAcudiente(Acudiente acudiente) { this.acudiente = acudiente; }
    public Grado getGradoAspira() { return gradoAspira; }
    public void setGradoAspira(Grado gradoAspira) { this.gradoAspira = gradoAspira; }
    public Grupo getGrupo() { return grupo; }
    public void setGrupo(Grupo grupo) { this.grupo = grupo; }
    public HojaVida getHojaDeVida() { return hojaDeVida; }
    public void setHojaDeVida(HojaVida hojaDeVida) { this.hojaDeVida = hojaDeVida; }
    public Observador getObservador() { return observador; }
    public void setObservador(Observador observador) { this.observador = observador; }
    public Set<LogroEstudiante> getLogrosCalificados() { return logrosCalificados; }
    public void setLogrosCalificados(Set<LogroEstudiante> logrosCalificados) { this.logrosCalificados = logrosCalificados; }
    public Set<Boletin> getBoletines() { return boletines; }
    public void setBoletines(Set<Boletin> boletines) { this.boletines = boletines; }
    public Preinscripcion getPreinscripcion() { return preinscripcion; }
    public void setPreinscripcion(Preinscripcion preinscripcion) { this.preinscripcion = preinscripcion; }

    public void agregarBoletin(Boletin boletin){
        boletines.add(boletin);
    }

    public void agregarLogrosEstudiante(LogroEstudiante logroCalificado){
        logrosCalificados.add(logroCalificado);
    }
}
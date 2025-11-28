package com.persistencia.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity(name = "logros_estudiante")
public class LogroEstudianteEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_logro_estudiante")
    private Integer idLogroEstudiante;

    @NotNull
    @Column(name = "fecha_calificacion", nullable = false)
    private LocalDate fechaCalificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante", referencedColumnName = "id_estudiante")
    private EstudianteEntity estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boletin", referencedColumnName = "id_boletin")
    private BoletinEntity boletin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "logro", referencedColumnName = "id_logro")
    private LogroEntity logro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor", referencedColumnName = "id_usuario")
    private ProfesorEntity profesor;

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

    public EstudianteEntity getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(EstudianteEntity estudiante) {
        this.estudiante = estudiante;
    }

    public BoletinEntity getBoletin() {
        return boletin;
    }

    public void setBoletin(BoletinEntity boletin) {
        this.boletin = boletin;
    }

    public LogroEntity getLogro() {
        return logro;
    }

    public void setLogro(LogroEntity logro) {
        this.logro = logro;
    }

    public ProfesorEntity getProfesor() {
        return profesor;
    }

    public void setProfesor(ProfesorEntity profesor) {
        this.profesor = profesor;
    }
}
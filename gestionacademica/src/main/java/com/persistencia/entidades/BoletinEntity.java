package com.persistencia.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;

@Entity(name = "boletin")
public class BoletinEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_boletin")
    private Integer idBoletin;

    @NotBlank
    @Size(min = 3, max = 20)
    @Column(nullable = false, length = 20)
    private String periodo;

    @NotNull
    @Column(nullable = false)
    private LocalDate fechaGeneracion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante", referencedColumnName = "id_estudiante")
    private EstudianteEntity estudiante;

	@OneToMany(mappedBy = "boletin", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<LogroEstudianteEntity> logrosEstudiante;

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

    public EstudianteEntity getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(EstudianteEntity estudiante) {
        this.estudiante = estudiante;
    }

    public Set<LogroEstudianteEntity> getLogrosEstudiante() {
        return logrosEstudiante;
    }

    public void setLogrosEstudiante(Set<LogroEstudianteEntity> logrosEstudiante) {
        this.logrosEstudiante = logrosEstudiante;
    }
}
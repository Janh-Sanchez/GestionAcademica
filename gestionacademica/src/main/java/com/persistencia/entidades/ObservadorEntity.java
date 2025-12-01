package com.persistencia.entidades;

import jakarta.persistence.*;
import java.util.SortedSet;

import org.hibernate.annotations.SortNatural;

@Entity
public class ObservadorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_observador")
    private Integer idObservador;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante", unique = true)
    private EstudianteEntity estudiante;

    @OneToMany(mappedBy = "observador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@SortNatural
    private SortedSet<ObservacionEntity> observaciones;

    public Integer getIdObservador() {
        return idObservador;
    }

    public void setIdObservador(Integer idObservador) {
        this.idObservador = idObservador;
    }

    public EstudianteEntity getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(EstudianteEntity estudiante) {
        this.estudiante = estudiante;
    }

    public SortedSet<ObservacionEntity> getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(SortedSet<ObservacionEntity> observaciones) {
        this.observaciones = observaciones;
    }

}
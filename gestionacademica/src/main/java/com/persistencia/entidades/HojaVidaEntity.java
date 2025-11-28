package com.persistencia.entidades;

import jakarta.persistence.*;

@Entity(name = "hoja_vida")
public class HojaVidaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "hoja_vida")
    private Integer idHojaVida;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante", unique = true)
    private EstudianteEntity estudiante;

    @ElementCollection
    private String alergias;

    @ElementCollection
    private String aspectosRelevantes;

    @ElementCollection
    private String enfermedades;

    public Integer getIdHojaVida() {
        return idHojaVida;
    }

    public void setIdHojaVida(Integer idHojaVida) {
        this.idHojaVida = idHojaVida;
    }

    public EstudianteEntity getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(EstudianteEntity estudiante) {
        this.estudiante = estudiante;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getAspectosRelevantes() {
        return aspectosRelevantes;
    }

    public void setAspectosRelevantes(String aspectosRelevantes) {
        this.aspectosRelevantes = aspectosRelevantes;
    }

    public String getEnfermedades() {
        return enfermedades;
    }

    public void setEnfermedades(String enfermedades) {
        this.enfermedades = enfermedades;
    }
}
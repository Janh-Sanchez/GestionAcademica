package com.persistencia.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set; // Cambiar de SortedSet a Set

import com.dominio.Estado;

@Entity(name = "preinscripcion")
public class PreinscripcionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_preinscripcion")
    private Integer idPreinscripcion;

    @NotNull
    @Column(nullable = false)
    private LocalDate fechaRegistro;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Estado estado = Estado.Pendiente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acudiente", referencedColumnName = "id_usuario")
    private AcudienteEntity acudiente;

    @OneToMany(mappedBy = "preinscripcion", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<EstudianteEntity> estudiantes;

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

    public AcudienteEntity getAcudiente() {
        return acudiente;
    }

    public void setAcudiente(AcudienteEntity acudiente) {
        this.acudiente = acudiente;
    }

    public Set<EstudianteEntity> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(Set<EstudianteEntity> estudiantes) {
        this.estudiantes = estudiantes;
    }
}
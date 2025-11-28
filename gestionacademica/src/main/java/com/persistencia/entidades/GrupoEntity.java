package com.persistencia.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Set;

@Entity(name = "grupo")
public class GrupoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_grupo")
    private Integer idGrupo;

    @NotBlank
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[A-Za-z0-9 ÁÉÍÓÚáéíóúñÑ-]+$", message = "Solo letras, números y espacios")
    @Column(nullable = false, length = 50)
    private String nombreGrupo;

    @Column(nullable = false)
    private boolean estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grado", referencedColumnName = "id_grado")
    private GradoEntity grado;

	@OneToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "profesor", referencedColumnName = "id_usuario")
	private ProfesorEntity profesor;

    @OneToMany(mappedBy = "grupo", fetch = FetchType.LAZY)
    private Set<EstudianteEntity> estudiantes;

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

    public GradoEntity getGrado() {
        return grado;
    }

    public void setGrado(GradoEntity grado) {
        this.grado = grado;
    }

    public ProfesorEntity getProfesor() {
        return profesor;
    }

    public void setProfesor(ProfesorEntity profesor) {
        this.profesor = profesor;
    }

    public Set<EstudianteEntity> getEstudiantes() {
        return estudiantes;
    }

    public void setEstudiantes(Set<EstudianteEntity> estudiantes) {
        this.estudiantes = estudiantes;
    }
}
package com.persistencia.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Set;

@Entity(name = "grado")
public class GradoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_grado")
    private Integer idGrado;

    @NotBlank
    @Size(min = 1, max = 50)
    @Pattern(regexp = "^[A-Za-z0-9 ÁÉÍÓÚáéíóúñÑ-]+$", message = "Solo letras, números y espacios")
    @Column(nullable = false, length = 50)
    private String nombreGrado;

	@OneToMany
    private Set<BibliotecaLogrosEntity> bibliotecaLogros;

    @OneToMany(mappedBy = "grado", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<GrupoEntity> grupos;

        public Integer getIdGrado() {
        return idGrado;
    }

    public void setIdGrado(Integer idGrado) {
        this.idGrado = idGrado;
    }

    public String getNombreGrado() {
        return nombreGrado;
    }

    public void setNombreGrado(String nombreGrado) {
        this.nombreGrado = nombreGrado;
    }

    public Set<BibliotecaLogrosEntity> getBibliotecaLogros() {
        return bibliotecaLogros;
    }

    public void setBibliotecaLogros(Set<BibliotecaLogrosEntity> bibliotecaLogros) {
        this.bibliotecaLogros = bibliotecaLogros;
    }

    public Set<GrupoEntity> getGrupos() {
        return grupos;
    }

    public void setGrupos(Set<GrupoEntity> grupos) {
        this.grupos = grupos;
    }
}
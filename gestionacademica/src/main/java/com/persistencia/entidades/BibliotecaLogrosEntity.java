package com.persistencia.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.Set;

@Entity(name = "biblioteca_logros")
public class BibliotecaLogrosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_biblioteca_logros")
    private Integer idBibliotecaLogros;

    @NotBlank
    @Size(min = 10, max = 200)
    @Column(nullable = false, length = 200)
    private String categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grado", referencedColumnName = "id_grado")
    private GradoEntity grado;

    @OneToMany(mappedBy = "bibliotecaLogros", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<LogroEntity> logros;

    public Integer getIdBibliotecaLogros() {
        return idBibliotecaLogros;
    }

    public void setIdBibliotecaLogros(Integer idBibliotecaLogros) {
        this.idBibliotecaLogros = idBibliotecaLogros;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public GradoEntity getGrado() {
        return grado;
    }

    public void setGrado(GradoEntity grado) {
        this.grado = grado;
    }

    public Set<LogroEntity> getLogros() {
        return logros;
    }

    public void setLogros(Set<LogroEntity> logros) {
        this.logros = logros;
    }
}
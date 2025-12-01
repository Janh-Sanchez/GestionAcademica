package com.persistencia.entidades;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity(name = "logro")
public class LogroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_logro")
    private Integer idLogro;

    @NotBlank
    @Size(min = 10, max = 200)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "biblioteca_logros", referencedColumnName = "id_biblioteca_logros")
    private BibliotecaLogrosEntity bibliotecaLogros;

    public Integer getIdLogro() {
        return idLogro;
    }

    public void setIdLogro(Integer idLogro) {
        this.idLogro = idLogro;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BibliotecaLogrosEntity getBibliotecaLogros() {
        return bibliotecaLogros;
    }

    public void setBibliotecaLogros(BibliotecaLogrosEntity bibliotecaLogros) {
        this.bibliotecaLogros = bibliotecaLogros;
    }
}
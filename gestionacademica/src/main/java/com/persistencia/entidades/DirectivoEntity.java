package com.persistencia.entidades;

import jakarta.persistence.*;

@Entity(name = "directivo")
@PrimaryKeyJoinColumn(name = "id_usuario")
public class DirectivoEntity extends UsuarioEntity {
    // No necesita campos adicionales
}
package com.persistencia.entidades;

import jakarta.persistence.*;

@Entity(name = "administrador")
@PrimaryKeyJoinColumn(name = "id_usuario")
public class AdministradorEntity extends UsuarioEntity {
}
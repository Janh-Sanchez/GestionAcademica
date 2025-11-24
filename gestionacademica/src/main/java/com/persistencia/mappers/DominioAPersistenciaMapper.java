package com.persistencia.mappers;

import com.dominio.*;
import com.persistencia.entidades.*;
import java.util.stream.Collectors;

public class DominioAPersistenciaMapper {
    
    public static PermisoEntity toEntity(Permiso permiso) {
        if (permiso == null) return null;
        PermisoEntity entity = new PermisoEntity();
        entity.setIdPermiso(permiso.getIdPermiso());
        entity.setNombre(permiso.getNombre());
        entity.setDescripcion(permiso.getDescripcion());
        return entity;
    }

    public static Permiso toDomain(PermisoEntity entity) {
        if (entity == null) return null;
        Permiso permiso = new Permiso(entity.getNombre(), entity.getDescripcion());
        permiso.setIdPermiso(entity.getIdPermiso());
        return permiso;
    }

    public static RolEntity toEntity(Rol rol) {
        if (rol == null) return null;
        RolEntity entity = new RolEntity();
        entity.setIdRol(rol.getIdRol());
        entity.setNombre(rol.getNombre());
        entity.setPermisos(
            rol.getPermisos().stream()
                .map(DominioAPersistenciaMapper::toEntity)
                .collect(Collectors.toSet())
        );
        return entity;
    }

    public static Rol toDomain(RolEntity entity) {
        if (entity == null) return null;
        Rol rol = new Rol(entity.getNombre());
        rol.setIdRol(entity.getIdRol());
        entity.getPermisos().forEach(p -> 
            rol.getPermisos().add(toDomain(p))
        );
        return rol;
    }

    public static TokenUsuarioEntity toEntity(TokenUsuario token) {
        if (token == null) return null;
        TokenUsuarioEntity entity = new TokenUsuarioEntity();
        entity.setIdToken(token.getIdToken());
        entity.setNombreUsuario(token.getNombreUsuario());
        entity.setContrasena(token.getContrasena());
        entity.setRol(toEntity(token.getRol()));
        return entity;
    }

    public static TokenUsuario toDomain(TokenUsuarioEntity entity) {
        if (entity == null) return null;
        TokenUsuario token = new TokenUsuario(
            entity.getNombreUsuario(),
            entity.getContrasena(),
            toDomain(entity.getRol())
        );
        token.setIdToken(entity.getIdToken());
        return token;
    }

    public static UsuarioEntity toEntity(Usuario usuario) {
        if (usuario == null) return null;
        UsuarioEntity entity = new UsuarioEntity();
        entity.setIdUsuario(usuario.getIdUsuario());
        entity.setPrimerNombre(usuario.getPrimerNombre());
        entity.setSegundoNombre(usuario.getSegundoNombre());
        entity.setPrimerApellido(usuario.getPrimerApellido());
        entity.setSegundoApellido(usuario.getSegundoApellido());
        entity.setEdad(usuario.getEdad());
        entity.setCorreoElectronico(usuario.getCorreoElectronico());
        entity.setTelefono(usuario.getTelefono());
        entity.setTokenAccess(toEntity(usuario.getTokenAccess()));
        return entity;
    }

    public static Usuario toDomain(UsuarioEntity entity) {
        if (entity == null) return null;
        Usuario usuario = new Usuario(
            entity.getIdUsuario(),
            entity.getPrimerNombre(),
            entity.getSegundoNombre(),
            entity.getPrimerApellido(),
            entity.getSegundoApellido(),
            entity.getEdad(),
            entity.getCorreoElectronico(),
            entity.getTelefono(),
            toDomain(entity.getTokenAccess())
        );
        return usuario;
    }
}
package com.persistencia.mappers;

import com.dominio.*;
import com.persistencia.entidades.*;

import java.util.Set;
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
        entity.getPermisos().forEach(permiso -> 
            rol.getPermisos().add(toDomain(permiso))
        );
        return rol;
    }

    // ✅ CORRECCIÓN CRÍTICA: No convertir el usuario para evitar recursión infinita
    public static TokenUsuarioEntity toEntity(TokenUsuario token) {
        if (token == null) return null;
        TokenUsuarioEntity entity = new TokenUsuarioEntity();
        entity.setIdToken(token.getIdToken());
        entity.setNombreUsuario(token.getNombreUsuario());
        entity.setContrasena(token.getContrasena());
        entity.setRol(toEntity(token.getRol()));
        return entity;
    }

    // ✅ CORRECCIÓN CRÍTICA: No convertir el usuario para evitar recursión infinita
    public static TokenUsuario toDomain(TokenUsuarioEntity entity) {
        if (entity == null) return null;
        TokenUsuario token = new TokenUsuario(
            entity.getIdToken(),
            entity.getNombreUsuario(),
            entity.getContrasena(),
            toDomain(entity.getRol())
        );
        return token;
    }

    private static void mapUsuarioToEntity(Usuario usuario, UsuarioEntity entity) {
        if (usuario == null || entity == null) return;
        
        entity.setIdUsuario(usuario.getIdUsuario());
        entity.setPrimerNombre(usuario.getPrimerNombre());
        entity.setSegundoNombre(usuario.getSegundoNombre());
        entity.setPrimerApellido(usuario.getPrimerApellido());
        entity.setSegundoApellido(usuario.getSegundoApellido());
        entity.setEdad(usuario.getEdad());
        entity.setCorreoElectronico(usuario.getCorreoElectronico());
        entity.setTelefono(usuario.getTelefono());
        entity.setTokenAccess(toEntity(usuario.getTokenAccess()));
    }

    private static void mapEntityToUsuario(UsuarioEntity entity, Usuario usuario) {
        if (entity == null || usuario == null) return;
        
        usuario.setIdUsuario(entity.getIdUsuario());
        usuario.setPrimerNombre(entity.getPrimerNombre());
        usuario.setSegundoNombre(entity.getSegundoNombre());
        usuario.setPrimerApellido(entity.getPrimerApellido());
        usuario.setSegundoApellido(entity.getSegundoApellido());
        usuario.setEdad(entity.getEdad());
        usuario.setCorreoElectronico(entity.getCorreoElectronico());
        usuario.setTelefono(entity.getTelefono());
        // ✅ Esto es seguro porque toDomain(TokenUsuarioEntity) ya no convierte el usuario
        usuario.setTokenAccess(toDomain(entity.getTokenAccess()));
    }
    
    // ==================== ACUDIENTE ====================
    public static AcudienteEntity toEntity(Acudiente acudiente) {
        if (acudiente == null) return null;
        
        AcudienteEntity entity = new AcudienteEntity();
        mapUsuarioToEntity(acudiente, entity); // ← ✅ Ya mapea todos los campos básicos
        entity.setEstadoAprobacion(acudiente.getEstadoAprobacion());
        
        if (acudiente.getEstudiantes() != null) {
            Set<EstudianteEntity> estudiantesEntities = acudiente.getEstudiantes().stream()
                .map(est -> {
                    EstudianteEntity estEntity = new EstudianteEntity();
                    estEntity.setIdEstudiante(est.getIdEstudiante());
                    return estEntity;
                })
                .collect(Collectors.toSet());
            entity.setEstudiantes(estudiantesEntities);
        }
        
        return entity;
    }

    public static Acudiente toDomain(AcudienteEntity entity) {
        if (entity == null) return null;
        
        Acudiente acudiente = new Acudiente();
        mapEntityToUsuario(entity, acudiente); // ← ✅ Ya mapea todos los campos básicos
        acudiente.setEstadoAprobacion(entity.getEstadoAprobacion());
        
        return acudiente;
    }

    // ✅ CORREGIDO: Usar los métodos auxiliares existentes
    public static AcudienteEntity toEntityShallow(Acudiente acudiente){
        if(acudiente == null) return null;
        
        AcudienteEntity acudienteEntity = new AcudienteEntity();
        mapUsuarioToEntity(acudiente, acudienteEntity); // ← ✅ Reutilizar en lugar de repetir
        acudienteEntity.setEstadoAprobacion(acudiente.getEstadoAprobacion());
        
        return acudienteEntity;
    }

    // ✅ CORREGIDO: Usar los métodos auxiliares existentes  
    public static Acudiente toDomainShallow(AcudienteEntity entity) {
        if (entity == null) return null;
        
        Acudiente acudiente = new Acudiente();
        mapEntityToUsuario(entity, acudiente); // ← ✅ Reutilizar en lugar de repetir
        acudiente.setEstadoAprobacion(entity.getEstadoAprobacion());
        
        return acudiente;
    }

    public static Acudiente toDomainComplete(AcudienteEntity entity) {
        Acudiente acudiente = toDomain(entity);
        
        if (acudiente != null && entity.getEstudiantes() != null) {
            Set<Estudiante> estudiantes = entity.getEstudiantes().stream()
                .map(DominioAPersistenciaMapper::toDomainShallow)
                .collect(Collectors.toSet());
            acudiente.setEstudiantes(estudiantes);
        }
        
        return acudiente;
    }

    // ==================== PROFESOR ====================
    public static ProfesorEntity toEntity(Profesor profesor) {
        if (profesor == null) return null;
        
        ProfesorEntity entity = new ProfesorEntity();
        mapUsuarioToEntity(profesor, entity);
        return entity;
    }

    public static Profesor toDomain(ProfesorEntity entity) {
        if (entity == null) return null;
        
        Profesor profesor = new Profesor();
        mapEntityToUsuario(entity, profesor);
        return profesor;
    }

    public static Profesor toDomainComplete(ProfesorEntity entity) {
        Profesor profesor = toDomain(entity);
        
        if (profesor != null && entity.getGrupoAsignado() != null) {
            profesor.setGrupo(toDomain(entity.getGrupoAsignado()));
        }
        
        return profesor;
    }

    // ==================== ADMINISTRADOR ====================
    public static AdministradorEntity toEntity(Administrador administrador) {
        if (administrador == null) return null;
        
        AdministradorEntity entity = new AdministradorEntity();
        mapUsuarioToEntity(administrador, entity);
        return entity;
    }

    public static Administrador toDomain(AdministradorEntity entity) {
        if (entity == null) return null;
        
        Administrador administrador = new Administrador();
        mapEntityToUsuario(entity, administrador);
        return administrador;
    }

    // ==================== DIRECTIVO ====================
    public static DirectivoEntity toEntity(Directivo directivo) {
        if (directivo == null) return null;
        
        DirectivoEntity entity = new DirectivoEntity();
        mapUsuarioToEntity(directivo, entity);
        return entity;
    }

    public static Directivo toDomain(DirectivoEntity entity) {
        if (entity == null) return null;
        
        Directivo directivo = new Directivo();
        mapEntityToUsuario(entity, directivo);
        return directivo;
    }

    // ==================== ESTUDIANTE ====================
    public static EstudianteEntity toEntity(Estudiante estudiante){
        if(estudiante == null) return null;
            EstudianteEntity estudianteEntity = new EstudianteEntity();
            estudianteEntity.setIdEstudiante(estudiante.getIdEstudiante());
            estudianteEntity.setPrimerNombre(estudiante.getPrimerNombre());
            estudianteEntity.setSegundoNombre(estudiante.getSegundoNombre());
            estudianteEntity.setPrimerApellido(estudiante.getPrimerApellido());
            estudianteEntity.setSegundoApellido(estudiante.getSegundoApellido());
            estudianteEntity.setNuip(estudiante.getNuip());
            estudianteEntity.setEdad(estudiante.getEdad());
            estudianteEntity.setEstado(estudiante.getEstado());
        if(estudiante.getAcudiente() != null) {
            estudianteEntity.setAcudiente(toEntityShallow(estudiante.getAcudiente()));
        }
        if(estudiante.getGrupo() != null) {
            estudianteEntity.setGrupo(toEntity(estudiante.getGrupo()));
        }
        if(estudiante.getHojaDeVida() != null) {
            estudianteEntity.setHojaDeVida(toEntity(estudiante.getHojaDeVida()));
        }
        if(estudiante.getObservador() != null) {
            estudianteEntity.setObservador(toEntity(estudiante.getObservador()));
        }
        if(estudiante.getPreinscripcion() != null) {
            estudianteEntity.setPreinscripcion(toEntity(estudiante.getPreinscripcion()));
        }
        if (estudiante.getBoletines() != null) {
            Set<BoletinEntity> boletinesEntities = estudiante.getBoletines().stream()
            .map(b -> {
            BoletinEntity bEntity = new BoletinEntity();
            bEntity.setIdBoletin(b.getIdBoletin());
            return bEntity;
            })
            .collect(Collectors.toSet());
        estudianteEntity.setBoletines(boletinesEntities);
        }
        return estudianteEntity;
    }

    public static Estudiante toDomain(EstudianteEntity estudianteEntity){
        if(estudianteEntity == null) return null;
        
        Estudiante estudiante = new Estudiante();
        estudiante.setIdEstudiante(estudianteEntity.getIdEstudiante());
        estudiante.setPrimerNombre(estudianteEntity.getPrimerNombre());
        estudiante.setSegundoNombre(estudianteEntity.getSegundoNombre());
        estudiante.setPrimerApellido(estudianteEntity.getPrimerApellido());
        estudiante.setSegundoApellido(estudianteEntity.getSegundoApellido());
        estudiante.setNuip(estudianteEntity.getNuip());
        estudiante.setEdad(estudianteEntity.getEdad());
        estudiante.setEstado(estudianteEntity.getEstado());
        
        if(estudianteEntity.getAcudiente() != null) {
            estudiante.setAcudiente(toDomainShallow(estudianteEntity.getAcudiente()));
        }
        if(estudianteEntity.getGradoAspira() != null) {
            estudiante.setGradoAspira(toDomain(estudianteEntity.getGradoAspira()));
        }
        if(estudianteEntity.getGrupo() != null) {
            estudiante.setGrupo(toDomain(estudianteEntity.getGrupo()));
        }
        if(estudianteEntity.getHojaDeVida() != null) {
            estudiante.setHojaDeVida(toDomain(estudianteEntity.getHojaDeVida()));
        }
        if(estudianteEntity.getObservador() != null) {
            estudiante.setObservador(toDomain(estudianteEntity.getObservador()));
        }
        if(estudianteEntity.getPreinscripcion() != null) {
            estudiante.setPreinscripcion(toDomain(estudianteEntity.getPreinscripcion()));
        }
        
        if(estudianteEntity.getLogrosCalificados() != null) {
            estudiante.setLogrosCalificados(
                estudianteEntity.getLogrosCalificados().stream()
                    .map(DominioAPersistenciaMapper::toDomain)
                    .collect(Collectors.toSet())
            );
        }
        if(estudianteEntity.getBoletines() != null) {
            estudiante.setBoletines(
                estudianteEntity.getBoletines().stream()
                    .map(DominioAPersistenciaMapper::toDomain)
                    .collect(Collectors.toSet())
            );
        }
        
        return estudiante;
    }

    public static Estudiante toDomainShallow(EstudianteEntity entity) {
        if (entity == null) return null;
        
        Estudiante e = new Estudiante();
        e.setIdEstudiante(entity.getIdEstudiante());
        e.setPrimerNombre(entity.getPrimerNombre());
        e.setSegundoNombre(entity.getSegundoNombre());
        e.setPrimerApellido(entity.getPrimerApellido());
        e.setSegundoApellido(entity.getSegundoApellido());
        e.setNuip(entity.getNuip());
        e.setEdad(entity.getEdad());
        e.setEstado(entity.getEstado());
        
        if (entity.getAcudiente() != null) {
            e.setAcudiente(toDomainShallow(entity.getAcudiente()));
        }
        return e;
    }

    // ==================== GRADO ====================
    public static GradoEntity toEntity(Grado grado) {
        if (grado == null) return null;
        
        GradoEntity entity = new GradoEntity();
        entity.setIdGrado(grado.getIdGrado());
        entity.setNombreGrado(grado.getNombreGrado());
        
        if (grado.getBibliotecaLogros() != null) {
            entity.setBibliotecaLogros(
                grado.getBibliotecaLogros().stream()
                    .map(DominioAPersistenciaMapper::toEntity)
                    .collect(Collectors.toSet())
            );
        }
        
        if (grado.getGrupos() != null) {
            entity.setGrupos(
                grado.getGrupos().stream()
                    .map(DominioAPersistenciaMapper::toEntity)
                    .collect(Collectors.toSet())
            );
        }
        
        return entity;
    }

    public static Grado toDomain(GradoEntity entity) {
        if (entity == null) return null;
        
        Set<BibliotecaLogros> bibliotecas = null;
        if (entity.getBibliotecaLogros() != null) {
            bibliotecas = entity.getBibliotecaLogros().stream()
                .map(DominioAPersistenciaMapper::toDomain)
                .collect(Collectors.toSet());
        }
        
        Set<Grupo> grupos = null;
        if (entity.getGrupos() != null) {
            grupos = entity.getGrupos().stream()
                .map(DominioAPersistenciaMapper::toDomain)
                .collect(Collectors.toSet());
        }
        
        return new Grado(
            entity.getIdGrado(),
            entity.getNombreGrado(),
            bibliotecas,
            grupos
        );
    }

    // ==================== GRUPO ====================
    public static GrupoEntity toEntity(Grupo grupo) {
        if (grupo == null) return null;
        
        GrupoEntity entity = new GrupoEntity();
        entity.setIdGrupo(grupo.getIdGrupo());
        entity.setNombreGrupo(grupo.getNombreGrupo());
        entity.setEstado(grupo.isEstado());
        
        if (grupo.getGrado() != null) {
            entity.setGrado(toEntity(grupo.getGrado()));
        }
        
        if (grupo.getProfesor() != null) {
            entity.setProfesor(toEntity(grupo.getProfesor()));
        }
        
        if (grupo.getEstudiantes() != null) {
            entity.setEstudiantes(
                grupo.getEstudiantes().stream()
                    .map(DominioAPersistenciaMapper::toEntity)
                    .collect(Collectors.toSet())
            );
        }
        
        return entity;
    }

    public static Grupo toDomain(GrupoEntity entity) {
        if (entity == null) return null;
        
        Grado grado = entity.getGrado() != null ? toDomain(entity.getGrado()) : null;
        Profesor profesor = entity.getProfesor() != null ? toDomain(entity.getProfesor()) : null;
        
        Set<Estudiante> estudiantes = null;
        if (entity.getEstudiantes() != null) {
            estudiantes = entity.getEstudiantes().stream()
                .map(DominioAPersistenciaMapper::toDomain)
                .collect(Collectors.toSet());
        }
        
        return new Grupo(
            entity.getIdGrupo(),
            entity.getNombreGrupo(),
            entity.isEstado(),
            grado,
            profesor,
            estudiantes
        );
    }

    // ==================== HOJA DE VIDA ====================
    public static HojaVidaEntity toEntity(HojaVida hojaVida) {
        if (hojaVida == null) return null;
        
        HojaVidaEntity entity = new HojaVidaEntity();
        entity.setIdHojaVida(hojaVida.getIdHojaVida());
        entity.setAlergias(hojaVida.getAlergias());
        entity.setAspectosRelevantes(hojaVida.getAspectosRelevantes());
        entity.setEnfermedades(hojaVida.getEnfermedades());
        
        if (hojaVida.getEstudiante() != null) {
            entity.setEstudiante(toEntity(hojaVida.getEstudiante()));
        }
        
        return entity;
    }

    public static HojaVida toDomain(HojaVidaEntity entity) {
        if (entity == null) return null;
        
        Estudiante estudiante = entity.getEstudiante() != null ? toDomain(entity.getEstudiante()) : null;
        
        return new HojaVida(
            entity.getIdHojaVida(),
            estudiante,
            entity.getAlergias(),
            entity.getAspectosRelevantes(),
            entity.getEnfermedades()
        );
    }

    // ==================== OBSERVADOR ====================
    public static ObservadorEntity toEntity(Observador observador) {
        if (observador == null) return null;
        
        ObservadorEntity entity = new ObservadorEntity();
        entity.setIdObservador(observador.getIdObservador());
        
        if (observador.getEstudiante() != null) {
            entity.setEstudiante(toEntity(observador.getEstudiante()));
        }
        
        if (observador.getObservaciones() != null) {
            entity.setObservaciones(
                observador.getObservaciones().stream()
                    .map(DominioAPersistenciaMapper::toEntity)
                    .collect(Collectors.toCollection(java.util.TreeSet::new))
            );
        }
        
        return entity;
    }

    public static Observador toDomain(ObservadorEntity entity) {
        if (entity == null) return null;
        
        Estudiante estudiante = entity.getEstudiante() != null ? toDomain(entity.getEstudiante()) : null;
        
        java.util.SortedSet<Observacion> observaciones = null;
        if (entity.getObservaciones() != null) {
            observaciones = entity.getObservaciones().stream()
                .map(DominioAPersistenciaMapper::toDomain)
                .collect(Collectors.toCollection(java.util.TreeSet::new));
        }
        
        return new Observador(
            entity.getIdObservador(),
            estudiante,
            observaciones
        );
    }

    // ==================== OBSERVACION ====================
    public static ObservacionEntity toEntity(Observacion observacion) {
        if (observacion == null) return null;
        
        ObservacionEntity entity = new ObservacionEntity();
        entity.setIdObservacion(observacion.getIdObservacion());
        entity.setDescripcion(observacion.getDescripcion());
        entity.setFechaObservacion(observacion.getFechaObservacion());
        
        if (observacion.getObservador() != null) {
            entity.setObservador(toEntity(observacion.getObservador()));
        }
        
        if (observacion.getProfesor() != null) {
            entity.setProfesor(toEntity(observacion.getProfesor()));
        }
        
        return entity;
    }

    public static Observacion toDomain(ObservacionEntity entity) {
        if (entity == null) return null;
        
        Observador observador = entity.getObservador() != null ? toDomain(entity.getObservador()) : null;
        Profesor profesor = entity.getProfesor() != null ? toDomain(entity.getProfesor()) : null;
        
        return new Observacion(
            entity.getIdObservacion(),
            entity.getDescripcion(),
            entity.getFechaObservacion(),
            observador,
            profesor
        );
    }

    // ==================== PREINSCRIPCION ====================
    public static PreinscripcionEntity toEntity(Preinscripcion preinscripcion) {
        if (preinscripcion == null) return null;
        
        PreinscripcionEntity entity = new PreinscripcionEntity();
        entity.setIdPreinscripcion(preinscripcion.getIdPreinscripcion());
        entity.setFechaRegistro(preinscripcion.getFechaRegistro());
        entity.setEstado(preinscripcion.getEstado());
        
        if (preinscripcion.getAcudiente() != null) {
            entity.setAcudiente(toEntityShallow(preinscripcion.getAcudiente()));
        }
        
        if (preinscripcion.getEstudiantes() != null) {
            entity.setEstudiantes(
                preinscripcion.getEstudiantes().stream()
                    .map(DominioAPersistenciaMapper::toEntity)
                    .collect(Collectors.toCollection(java.util.TreeSet::new))
            );
        }
        
        return entity;
    }

    public static Preinscripcion toDomain(PreinscripcionEntity entity) {
        if (entity == null) return null;
        
        Acudiente acudiente = entity.getAcudiente() != null ? toDomainShallow(entity.getAcudiente()) : null;
        
        java.util.SortedSet<Estudiante> estudiantes = null;
        if (entity.getEstudiantes() != null) {
            estudiantes = entity.getEstudiantes().stream()
                .map(DominioAPersistenciaMapper::toDomain)
                .collect(Collectors.toCollection(java.util.TreeSet::new));
        }
        
        return new Preinscripcion(
            entity.getIdPreinscripcion(),
            entity.getFechaRegistro(),
            entity.getEstado(),
            acudiente,
            estudiantes
        );
    }

    // ==================== BIBLIOTECA LOGROS ====================
    public static BibliotecaLogrosEntity toEntity(BibliotecaLogros biblioteca) {
        if (biblioteca == null) return null;
        
        BibliotecaLogrosEntity entity = new BibliotecaLogrosEntity();
        entity.setIdBibliotecaLogros(biblioteca.getIdBibliotecaLogros());
        entity.setCategoria(biblioteca.getCategoria());
        
        if (biblioteca.getGrado() != null) {
            entity.setGrado(toEntity(biblioteca.getGrado()));
        }
        
        if (biblioteca.getLogros() != null) {
            entity.setLogros(
                biblioteca.getLogros().stream()
                    .map(DominioAPersistenciaMapper::toEntity)
                    .collect(Collectors.toSet())
            );
        }
        
        return entity;
    }

    public static BibliotecaLogros toDomain(BibliotecaLogrosEntity entity) {
        if (entity == null) return null;
        
        Grado grado = entity.getGrado() != null ? toDomain(entity.getGrado()) : null;
        
        Set<Logro> logros = null;
        if (entity.getLogros() != null) {
            logros = entity.getLogros().stream()
                .map(DominioAPersistenciaMapper::toDomain)
                .collect(Collectors.toSet());
        }
        
        return new BibliotecaLogros(
            entity.getIdBibliotecaLogros(),
            entity.getCategoria(),
            grado,
            logros
        );
    }

    // ==================== LOGRO ====================
    public static LogroEntity toEntity(Logro logro) {
        if (logro == null) return null;
        
        LogroEntity entity = new LogroEntity();
        entity.setIdLogro(logro.getIdLogro());
        entity.setDescripcion(logro.getDescripcion());
        
        if (logro.getBibliotecaLogros() != null) {
            entity.setBibliotecaLogros(toEntity(logro.getBibliotecaLogros()));
        }
        
        return entity;
    }

    public static Logro toDomain(LogroEntity entity) {
        if (entity == null) return null;
        
        BibliotecaLogros biblioteca = entity.getBibliotecaLogros() != null ? 
            toDomain(entity.getBibliotecaLogros()) : null;
        
        return new Logro(
            entity.getIdLogro(),
            entity.getDescripcion(),
            biblioteca
        );
    }

    // ==================== LOGRO ESTUDIANTE ====================
    public static LogroEstudianteEntity toEntity(LogroEstudiante logroEstudiante) {
        if (logroEstudiante == null) return null;
        
        LogroEstudianteEntity entity = new LogroEstudianteEntity();
        entity.setIdLogroEstudiante(logroEstudiante.getIdLogroEstudiante());
        entity.setFechaCalificacion(logroEstudiante.getFechaCalificacion());
        
        if (logroEstudiante.getEstudiante() != null) {
            entity.setEstudiante(toEntity(logroEstudiante.getEstudiante()));
        }
        
        if (logroEstudiante.getBoletin() != null) {
            entity.setBoletin(toEntity(logroEstudiante.getBoletin()));
        }
        
        if (logroEstudiante.getLogro() != null) {
            entity.setLogro(toEntity(logroEstudiante.getLogro()));
        }
        
        if (logroEstudiante.getProfesor() != null) {
            entity.setProfesor(toEntity(logroEstudiante.getProfesor()));
        }
        
        return entity;
    }

    public static LogroEstudiante toDomain(LogroEstudianteEntity entity) {
        if (entity == null) return null;
        
        Estudiante estudiante = entity.getEstudiante() != null ? toDomain(entity.getEstudiante()) : null;
        Boletin boletin = entity.getBoletin() != null ? toDomain(entity.getBoletin()) : null;
        Logro logro = entity.getLogro() != null ? toDomain(entity.getLogro()) : null;
        Profesor profesor = entity.getProfesor() != null ? toDomain(entity.getProfesor()) : null;
        
        return new LogroEstudiante(
            entity.getIdLogroEstudiante(),
            entity.getFechaCalificacion(),
            estudiante,
            boletin,
            logro,
            profesor
        );
    }

    // ==================== BOLETIN ====================
    public static BoletinEntity toEntity(Boletin boletin) {
        if (boletin == null) return null;
        
        BoletinEntity entity = new BoletinEntity();
        entity.setIdBoletin(boletin.getIdBoletin());
        entity.setPeriodo(boletin.getPeriodo());
        entity.setFechaGeneracion(boletin.getFechaGeneracion());
        
        if (boletin.getEstudiante() != null) {
            entity.setEstudiante(toEntity(boletin.getEstudiante()));
        }
        
        if (boletin.getLogrosEstudiante() != null) {
            entity.setLogrosEstudiante(
                boletin.getLogrosEstudiante().stream()
                    .map(DominioAPersistenciaMapper::toEntity)
                    .collect(Collectors.toSet())
            );
        }
        
        return entity;
    }

    public static Boletin toDomain(BoletinEntity entity) {
        if (entity == null) return null;
        
        Estudiante estudiante = entity.getEstudiante() != null ? toDomain(entity.getEstudiante()) : null;
        
        Set<LogroEstudiante> logrosEstudiante = null;
        if (entity.getLogrosEstudiante() != null) {
            logrosEstudiante = entity.getLogrosEstudiante().stream()
                .map(DominioAPersistenciaMapper::toDomain)
                .collect(Collectors.toSet());
        }
        
        return new Boletin(
            entity.getIdBoletin(),
            entity.getPeriodo(),
            entity.getFechaGeneracion(),
            estudiante,
            logrosEstudiante
        );
    }
}
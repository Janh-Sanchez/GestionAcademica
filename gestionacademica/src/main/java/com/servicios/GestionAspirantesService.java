package com.servicios;

import com.dominio.*;
import com.persistencia.entidades.*;
import com.persistencia.mappers.DominioAPersistenciaMapper;
import com.persistencia.repositorios.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar la aprobación y rechazo de aspirantes
 * Implementa RF 3.4 - Aprobar o rechazar aspirante
 */
public class GestionAspirantesService {
    
    private final EntityManager entityManager;
    private final PreinscripcionRepositorio repoPreinscripcion;
    private final RepositorioGenerico<EstudianteEntity> repoEstudiante;
    private final AcudienteRepositorio repoAcudiente;
    private final GrupoRepositorio repoGrupo;
    private final GradoRepositorio repoGrado;
    private final GestionUsuariosService gestionUsuariosService;
    
    public GestionAspirantesService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.repoPreinscripcion = new PreinscripcionRepositorio(entityManager);
        this.repoEstudiante = new RepositorioGenerico<>(entityManager, EstudianteEntity.class);
        this.repoAcudiente = new AcudienteRepositorio(entityManager);
        this.repoGrupo = new GrupoRepositorio(entityManager);
        this.repoGrado = new GradoRepositorio(entityManager);
        this.gestionUsuariosService = new GestionUsuariosService();
    }
    
    /**
     * Clase para encapsular el resultado de operaciones
     */
    public static class ResultadoOperacion {
        private final boolean exitoso;
        private final String mensaje;
        private final Object dato;
        
        private ResultadoOperacion(boolean exitoso, String mensaje, Object dato) {
            this.exitoso = exitoso;
            this.mensaje = mensaje;
            this.dato = dato;
        }
        
        public static ResultadoOperacion exito(String mensaje, Object dato) {
            return new ResultadoOperacion(true, mensaje, dato);
        }
        
        public static ResultadoOperacion exito(String mensaje) {
            return new ResultadoOperacion(true, mensaje, null);
        }
        
        public static ResultadoOperacion error(String mensaje) {
            return new ResultadoOperacion(false, mensaje, null);
        }
        
        public boolean isExitoso() { return exitoso; }
        public String getMensaje() { return mensaje; }
        public Object getDato() { return dato; }
    }
    
    /**
     * DTO para transferir información de aspirantes a la UI
     */
    public static class AspiranteDTO {
        private Integer idPreinscripcion;
        private String nombreCompletoAcudiente;
        private Integer idAcudiente;
        private List<EstudianteDTO> estudiantes;
        
        public AspiranteDTO(Integer idPreinscripcion, String nombreCompletoAcudiente, 
                           Integer idAcudiente, List<EstudianteDTO> estudiantes) {
            this.idPreinscripcion = idPreinscripcion;
            this.nombreCompletoAcudiente = nombreCompletoAcudiente;
            this.idAcudiente = idAcudiente;
            this.estudiantes = estudiantes;
        }
        
        public Integer getIdPreinscripcion() { return idPreinscripcion; }
        public String getNombreCompletoAcudiente() { return nombreCompletoAcudiente; }
        public Integer getIdAcudiente() { return idAcudiente; }
        public List<EstudianteDTO> getEstudiantes() { return estudiantes; }
    }
    
    public static class EstudianteDTO {
        private Integer idEstudiante;
        private String nombreCompleto;
        private String nombreGrado;
        private Estado estado;
        
        public EstudianteDTO(Integer idEstudiante, String nombreCompleto, 
                            String nombreGrado, Estado estado) {
            this.idEstudiante = idEstudiante;
            this.nombreCompleto = nombreCompleto;
            this.nombreGrado = nombreGrado;
            this.estado = estado;
        }
        
        public Integer getIdEstudiante() { return idEstudiante; }
        public String getNombreCompleto() { return nombreCompleto; }
        public String getNombreGrado() { return nombreGrado; }
        public Estado getEstado() { return estado; }
    }
    
    /**
     * Obtiene la lista de aspirantes pendientes ordenados por fecha (REFACTORIZADO)
     */
    public ResultadoOperacion obtenerListaAspirantes() {
        try {
            // Usar el repositorio en lugar de JPQL directo
            List<PreinscripcionEntity> preinscripciones = repoPreinscripcion.buscarPendientes();
            
            if (preinscripciones.isEmpty()) {
                return ResultadoOperacion.error("VACIA");
            }
            
            List<AspiranteDTO> aspirantes = preinscripciones.stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
            
            return ResultadoOperacion.exito("Lista obtenida", aspirantes);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResultadoOperacion.error("Error al acceder a la base de datos, inténtelo nuevamente");
        }
    }
    
    /**
     * Aprueba un estudiante específico
     * RF 3.4 - Aprobar aspirante
     */
    /**
     * Aprueba un estudiante específico
     * RF 3.4 - Aprobar aspirante
     */
    public ResultadoOperacion aprobarEstudiante(Integer idEstudiante) {
        EntityTransaction transaction = entityManager.getTransaction();
        
        try {
            transaction.begin();
            
            // 1. Buscar estudiante
            Optional<EstudianteEntity> estudianteOpt = repoEstudiante.buscarPorId(idEstudiante);
            if (estudianteOpt.isEmpty()) {
                transaction.rollback();
                return ResultadoOperacion.error("Estudiante no encontrado");
            }
            
            EstudianteEntity estudiante = estudianteOpt.get();
            AcudienteEntity acudiente = estudiante.getAcudiente();
            
            if (acudiente == null) {
                transaction.rollback();
                return ResultadoOperacion.error("Acudiente no encontrado");
            }
            
            // 2. Obtener la preinscripción del estudiante
            PreinscripcionEntity preinscripcion = obtenerPreinscripcionPorEstudiante(estudiante);
            
            // 3. Verificar si el acudiente ya tenía estudiantes aprobados previamente
            boolean acudienteTieneEstudiantesAprobados = repoAcudiente.tieneEstudiantesAprobados(acudiente);
            
            // 4. Si el acudiente NO tenía estudiantes aprobados, aprobarlo también y generar token
            if (!acudienteTieneEstudiantesAprobados) {
                acudiente.setEstadoAprobacion(Estado.Aprobada);
                repoAcudiente.guardar(acudiente);
                
                // 5. Generar token para el acudiente (crear usuario)
                generarUsuarioParaAcudiente(acudiente);
                
                System.out.println("Acudiente aprobado. ID: " + acudiente.getIdUsuario() + 
                                ", Correo: " + acudiente.getCorreoElectronico());
            }
            
            // 6. Aprobar estudiante
            estudiante.setEstado(Estado.Aprobada);
            
            // 7. Asignar estudiante a un grupo
            ResultadoOperacion resultadoAsignacion = asignarEstudianteAGrupo(estudiante);
            if (!resultadoAsignacion.isExitoso()) {
                transaction.rollback();
                return resultadoAsignacion;
            }
            
            repoEstudiante.guardar(estudiante);
            
            // 8. Actualizar estado de la preinscripción
            if (preinscripcion != null) {
                actualizarEstadoPreinscripcion(preinscripcion);
            }
            
            transaction.commit();
            return ResultadoOperacion.exito("¡Listo! El estudiante fue aprobado con éxito");
            
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return ResultadoOperacion.error("Error al acceder a la base de datos: " + e.getMessage());
        }
    }

    /**
     * Rechaza un estudiante específico
     * RF 3.4 - Rechazar aspirante
     */
    public ResultadoOperacion rechazarEstudiante(Integer idEstudiante) {
        EntityTransaction transaction = entityManager.getTransaction();
        
        try {
            transaction.begin();
            
            // 1. Buscar estudiante
            Optional<EstudianteEntity> estudianteOpt = repoEstudiante.buscarPorId(idEstudiante);
            if (estudianteOpt.isEmpty()) {
                transaction.rollback();
                return ResultadoOperacion.error("Estudiante no encontrado");
            }
            
            EstudianteEntity estudiante = estudianteOpt.get();
            AcudienteEntity acudiente = estudiante.getAcudiente();
            
            if (acudiente == null) {
                transaction.rollback();
                return ResultadoOperacion.error("Acudiente no encontrado");
            }
            
            // 2. Obtener la preinscripción del estudiante
            PreinscripcionEntity preinscripcion = obtenerPreinscripcionPorEstudiante(estudiante);
            
            // 3. Verificar si el acudiente tiene más estudiantes con estado pendiente
            boolean tieneEstudiantesPendientes = repoAcudiente.tieneEstudiantesPendientes(acudiente, idEstudiante);
            
            // 4. Verificar si el acudiente YA tiene estudiantes aprobados
            boolean tieneEstudiantesAprobados = repoAcudiente.tieneEstudiantesAprobados(acudiente);
            
            // 5. Solo rechazar al acudiente si:
            //    a) NO tiene más estudiantes pendientes
            //    b) NO tiene estudiantes aprobados
            if (!tieneEstudiantesPendientes && !tieneEstudiantesAprobados) {
                acudiente.setEstadoAprobacion(Estado.Rechazada);
                repoAcudiente.guardar(acudiente);
            }
            
            // 6. Rechazar estudiante
            estudiante.setEstado(Estado.Rechazada);
            repoEstudiante.guardar(estudiante);
            
            // 7. Actualizar estado de la preinscripción
            if (preinscripcion != null) {
                actualizarEstadoPreinscripcion(preinscripcion);
            }
            
            transaction.commit();
            return ResultadoOperacion.exito("¡Listo! El estudiante fue rechazado con éxito");
            
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            return ResultadoOperacion.error("Error al acceder a la base de datos, inténtelo nuevamente");
        }
    }

    // En GestionAspirantesService.java, modificar el método generarUsuarioParaAcudiente
    /**
     * Genera y asigna token para un acudiente aprobado
     */
    private void generarUsuarioParaAcudiente(AcudienteEntity acudienteEntity) {
        try {
            // Verificar si el acudiente ya tiene token
            if (acudienteEntity.getTokenAccess() != null) {
                System.out.println("Acudiente ID " + acudienteEntity.getIdUsuario() + 
                                " ya tiene token asignado");
                return;
            }
            
            // Convertir a dominio para usar el método de generación de token
            Acudiente acudiente = DominioAPersistenciaMapper.toDomain(acudienteEntity);
            
            // 1. Generar token usando el servicio existente
            TokenUsuario tokenUsuario = gestionUsuariosService.generarTokenUsuario(acudiente);
            
            // 2. Buscar rol de acudiente
            RolRepositorio rolRepo = new RolRepositorio(entityManager);
            Optional<RolEntity> rolOpt = rolRepo.buscarPorNombreRol("acudiente");
            
            if (rolOpt.isEmpty()) {
                System.err.println("Error: Rol 'acudiente' no encontrado en la BD");
                return;
            }
            
            // 3. Configurar el token con el rol
            Rol rol = DominioAPersistenciaMapper.toDomain(rolOpt.get());
            tokenUsuario.setRol(rol);
            
            // 4. Mapear a entidad y guardar
            TokenUsuarioEntity tokenEntity = new TokenUsuarioEntity();
            tokenEntity.setNombreUsuario(tokenUsuario.getNombreUsuario());
            tokenEntity.setContrasena(tokenUsuario.getContrasena());
            tokenEntity.setRol(rolOpt.get());
            
            // 5. Guardar token
            TokenUsuarioRepositorio tokenRepo = new TokenUsuarioRepositorio(entityManager);
            TokenUsuarioEntity tokenGuardado = tokenRepo.guardar(tokenEntity);
            
            // 6. Asociar token al acudiente
            acudienteEntity.setTokenAccess(tokenGuardado);
            repoAcudiente.guardar(acudienteEntity);
            
            // 7. Enviar email con credenciales (si hay EmailService disponible)
            enviarCredencialesAcudiente(acudienteEntity, tokenUsuario);
            
            System.out.println("Token generado para acudiente ID: " + acudienteEntity.getIdUsuario() + 
                            ", Usuario: " + tokenUsuario.getNombreUsuario());
            
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error al generar usuario para acudiente: " + e.getMessage());
        }
    }

    /**
     * Envía las credenciales al acudiente
     */
    private void enviarCredencialesAcudiente(AcudienteEntity acudiente, TokenUsuario token) {
        try {
            // Usar el EmailService de GestionUsuariosService si está disponible
            // O crear uno propio si no
            
            // Construir nombre completo
            String nombreCompleto = construirNombreCompleto(
                acudiente.getPrimerNombre(),
                acudiente.getSegundoNombre(),
                acudiente.getPrimerApellido(),
                acudiente.getSegundoApellido()
            );
            
            // Usar el EmailService (necesitaríamos tener acceso a él)
            // Por ahora, solo imprimir para debug
            System.out.println("=== CREDENCIALES GENERADAS PARA ACUDIENTE ===");
            System.out.println("Nombre: " + nombreCompleto);
            System.out.println("Correo: " + acudiente.getCorreoElectronico());
            System.out.println("Usuario: " + token.getNombreUsuario());
            System.out.println("Contraseña: " + token.getContrasena());
            System.out.println("===========================================");
            
            // Si quieres enviar email real, necesitarías inyectar EmailService
            // emailService.enviarCredenciales(acudiente.getCorreoElectronico(), token, nombreCompleto);
            
        } catch (Exception e) {
            System.err.println("Error al enviar credenciales: " + e.getMessage());
        }
    }
    
    /**
     * Asigna estudiante a un grupo y actualiza estado del grupo
     */
    private ResultadoOperacion asignarEstudianteAGrupo(EstudianteEntity estudiante) {
        try {
            if (estudiante.getGradoAspira() == null) {
                return ResultadoOperacion.error("El estudiante no tiene grado asignado");
            }
            
            Integer idGrado = estudiante.getGradoAspira().getIdGrado();
            
            // Obtener grupos ordenados
            List<GrupoEntity> grupos = repoGrupo.buscarActivosPorGradoOrdenadosPorEstudiantes(idGrado);
            
            GrupoEntity grupoAsignado = buscarGrupoDisponible(grupos);
            
            // Si no hay grupos con disponibilidad, crear uno nuevo
            if (grupoAsignado == null) {
                grupoAsignado = crearNuevoGrupo(idGrado);
            }
            
            // Asignar estudiante al grupo
            estudiante.setGrupo(grupoAsignado);
            
            // Actualizar estado del grupo según cantidad de estudiantes
            actualizarEstadoGrupo(grupoAsignado);
            
            return ResultadoOperacion.exito("Estudiante asignado a grupo");
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResultadoOperacion.error("Error al asignar grupo: " + e.getMessage());
        }
    }

    /**
     * Busca un grupo disponible considerando reglas de negocio
     */
    private GrupoEntity buscarGrupoDisponible(List<GrupoEntity> grupos) {
        if (grupos == null || grupos.isEmpty()) {
            return null;
        }
        
        // Prioridad 1: Grupos inactivos (estado = false) con menos de 5 estudiantes
        for (GrupoEntity grupo : grupos) {
            if (!grupo.isEstado()) { // Grupo inactivo
                int cantidadEstudiantes = grupo.getEstudiantes() != null ? grupo.getEstudiantes().size() : 0;
                if (cantidadEstudiantes < 5) {
                    return grupo;
                }
            }
        }
        
        // Prioridad 2: Grupos activos con disponibilidad
        for (GrupoEntity grupo : grupos) {
            if (grupo.isEstado()) { // Grupo activo
                int cantidadEstudiantes = grupo.getEstudiantes() != null ? grupo.getEstudiantes().size() : 0;
                if (cantidadEstudiantes < 10) { // Máximo 10 estudiantes
                    return grupo;
                }
            }
        }
        
        return null;
    }

    /**
     * Actualiza el estado del grupo basado en cantidad de estudiantes
     */
    private void actualizarEstadoGrupo(GrupoEntity grupo) {
        if (grupo == null) return;
        
        int cantidadEstudiantes = grupo.getEstudiantes() != null ? grupo.getEstudiantes().size() : 0;
        
        // Regla: Grupo está activo si tiene al menos 5 estudiantes
        if (cantidadEstudiantes >= 5) {
            grupo.setEstado(true); // Activo
        } else {
            grupo.setEstado(false); // Inactivo
        }
        
        // Guardar cambios
        repoGrupo.guardar(grupo);
    }

    /**
     * Crea un nuevo grupo para un grado específico (inicialmente inactivo)
     */
    private GrupoEntity crearNuevoGrupo(Integer idGrado) throws Exception {
        Optional<GradoEntity> gradoOpt = repoGrado.buscarPorId(idGrado);
        if (gradoOpt.isEmpty()) {
            throw new Exception("Grado no encontrado con ID: " + idGrado);
        }
        
        GradoEntity grado = gradoOpt.get();
        
        // Contar grupos existentes usando el repositorio
        Long cantidadGrupos = repoGrupo.contarGruposPorGrado(idGrado);
        
        // Generar nombre del grupo
        String nombreGrupo = generarNombreGrupo(grado, cantidadGrupos);
        
        // Crear nuevo grupo (inicialmente inactivo porque tiene 0 estudiantes)
        GrupoEntity nuevoGrupo = new GrupoEntity();
        nuevoGrupo.setNombreGrupo(nombreGrupo);
        nuevoGrupo.setEstado(false); // Inactivo hasta tener 5+ estudiantes
        nuevoGrupo.setGrado(grado);
        nuevoGrupo.setEstudiantes(new HashSet<>());
        
        return repoGrupo.guardar(nuevoGrupo);
    }

    /**
     * Actualiza el estado de la preinscripción basado en el estado de sus estudiantes
     */
    private void actualizarEstadoPreinscripcion(PreinscripcionEntity preinscripcion) {
        if (preinscripcion == null || preinscripcion.getEstudiantes() == null) {
            return;
        }
        
        // Contar estudiantes por estado
        long pendientes = preinscripcion.getEstudiantes().stream()
            .filter(e -> e.getEstado() == Estado.Pendiente)
            .count();
        
        long aprobados = preinscripcion.getEstudiantes().stream()
            .filter(e -> e.getEstado() == Estado.Aprobada)
            .count();
        
        long rechazados = preinscripcion.getEstudiantes().stream()
            .filter(e -> e.getEstado() == Estado.Rechazada)
            .count();
        
        long total = preinscripcion.getEstudiantes().size();
        
        // Reglas:
        // 1. Si al menos uno está aprobado → Preinscripción Aprobada
        // 2. Si todos están rechazados → Preinscripción Rechazada
        // 3. Si hay pendientes → Preinscripción Pendiente
        // 4. Si hay mezcla (aprobados + rechazados) → Preinscripción Aprobada (parcial)
        
        if (aprobados > 0) {
            // Al menos un estudiante aprobado
            preinscripcion.setEstado(Estado.Aprobada);
        } else if (rechazados == total) {
            // Todos rechazados
            preinscripcion.setEstado(Estado.Rechazada);
        } else if (pendientes > 0) {
            // Aún hay pendientes
            preinscripcion.setEstado(Estado.Pendiente);
        }
        
        // Guardar cambios
        repoPreinscripcion.guardar(preinscripcion);
    }
    
    /**
     * Genera el nombre del grupo basado en el grado y la cantidad existente
     */
    private String generarNombreGrupo(GradoEntity grado, Long cantidadGrupos) {
        return String.format("%s-%d", grado.getNombreGrado(), cantidadGrupos + 1);
    }
    
    /**
     * Método auxiliar para obtener grupos disponibles para un grado
     */
    public List<GrupoEntity> obtenerGruposDisponiblesParaGrado(Integer idGrado) {
        List<GrupoEntity> grupos = repoGrupo.buscarActivosPorGradoOrdenadosPorEstudiantes(idGrado);
        
        return grupos.stream()
            .filter(grupo -> {
                Grupo grupoDomain = DominioAPersistenciaMapper.toDomain(grupo);
                return !grupoDomain.tieneEstudiantesSuficientes() || 
                       grupoDomain.tieneDisponibilidad();
            })
            .collect(Collectors.toList());
    }
    
    /**
     * Convierte una entidad de preinscripción a DTO
     */
    private AspiranteDTO convertirADTO(PreinscripcionEntity preinscripcion) {
        AcudienteEntity acudiente = preinscripcion.getAcudiente();
        String nombreAcudiente = construirNombreCompleto(
            acudiente.getPrimerNombre(),
            acudiente.getSegundoNombre(),
            acudiente.getPrimerApellido(),
            acudiente.getSegundoApellido()
        );
        
        List<EstudianteDTO> estudiantesDTO = preinscripcion.getEstudiantes().stream()
            .filter(e -> e.getEstado() == Estado.Pendiente)
            .map(e -> new EstudianteDTO(
                e.getIdEstudiante(),
                construirNombreCompleto(
                    e.getPrimerNombre(),
                    e.getSegundoNombre(),
                    e.getPrimerApellido(),
                    e.getSegundoApellido()
                ),
                e.getGradoAspira() != null ? e.getGradoAspira().getNombreGrado() : "Sin grado",
                e.getEstado()
            ))
            .collect(Collectors.toList());
        
        return new AspiranteDTO(
            preinscripcion.getIdPreinscripcion(),
            nombreAcudiente,
            acudiente.getIdUsuario(),
            estudiantesDTO
        );
    }
    
    private String construirNombreCompleto(String primer, String segundo, 
                                          String primerAp, String segundoAp) {
        StringBuilder nombre = new StringBuilder(primer);
        if (segundo != null && !segundo.isEmpty()) {
            nombre.append(" ").append(segundo);
        }
        nombre.append(" ").append(primerAp);
        if (segundoAp != null && !segundoAp.isEmpty()) {
            nombre.append(" ").append(segundoAp);
        }
        return nombre.toString();
    }
    
    public void cerrar() {
        if (gestionUsuariosService != null) {
            gestionUsuariosService.cerrar();
        }
    }

    /**
     * Obtiene la preinscripción de un estudiante
     */
    private PreinscripcionEntity obtenerPreinscripcionPorEstudiante(EstudianteEntity estudiante) {
        try {
            String jpql = "SELECT p FROM preinscripcion p JOIN p.estudiantes e WHERE e.idEstudiante = :idEstudiante";
            return entityManager.createQuery(jpql, PreinscripcionEntity.class)
                .setParameter("idEstudiante", estudiante.getIdEstudiante())
                .setMaxResults(1)
                .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

}
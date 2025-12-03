package com.servicios;

import com.dominio.*;
import com.persistencia.entidades.*;
import com.persistencia.mappers.DominioAPersistenciaMapper;
import com.persistencia.repositorios.EstudianteRepositorio;
import com.persistencia.repositorios.GradoRepositorio;
import com.persistencia.repositorios.RepositorioGenerico;
import com.persistencia.repositorios.UsuarioRepositorio;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet; // Cambiado de TreeSet a HashSet
import java.util.regex.Pattern;

/**
 * Servicio para gestionar el proceso completo de preinscripción
 * Implementa RF 3.1, RF 3.2, RF 3.3, RF 3.4
 */
public class PreinscripcionService {
    
    private final RepositorioGenerico<PreinscripcionEntity> repoPreinscripcion;
    private final RepositorioGenerico<AcudienteEntity> repoAcudiente;
    private final RepositorioGenerico<EstudianteEntity> repoEstudiante;
    private final UsuarioRepositorio usuarioRepositorio;
    private final GradoRepositorio gradoRepositorio;
    private final EstudianteRepositorio estudianteRepositorio;
    private final EntityManager entityManager;
    
    // Constantes de validación
    private static final int MIN_EDAD_ACUDIENTE = 18;
    private static final int MAX_EDAD_ACUDIENTE = 80;
    private static final int MIN_EDAD_ESTUDIANTE = 3;
    private static final int MAX_EDAD_ESTUDIANTE = 18;
    private static final Pattern PATTERN_NOMBRE = Pattern.compile("^[A-Za-zÁÉÍÓÚáéíóúñÑ]+$");
    private static final Pattern PATTERN_EMAIL = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PATTERN_TELEFONO = Pattern.compile("^[0-9]{10}$");
    private static final Pattern PATTERN_NUIP = Pattern.compile("^[0-9]{10}$");
    
    public PreinscripcionService(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.repoPreinscripcion = new RepositorioGenerico<>(entityManager, PreinscripcionEntity.class);
        this.repoAcudiente = new RepositorioGenerico<>(entityManager, AcudienteEntity.class);
        this.repoEstudiante = new RepositorioGenerico<>(entityManager, EstudianteEntity.class);
        this.gradoRepositorio = new GradoRepositorio(entityManager);
        this.usuarioRepositorio = new UsuarioRepositorio(entityManager);
        this.estudianteRepositorio = new EstudianteRepositorio(entityManager);
    }
    
    /**
     * Clase interna para encapsular resultados de validación
     */
    public static class ResultadoValidacion {
        private final boolean valido;
        private final String mensaje;
        private final String campo;
        
        public ResultadoValidacion(boolean valido, String mensaje, String campo) {
            this.valido = valido;
            this.mensaje = mensaje;
            this.campo = campo;
        }
        
        public boolean isValido() { return valido; }
        public String getMensaje() { return mensaje; }
        public String getCampo() { return campo; }
        
        public static ResultadoValidacion exitoso() {
            return new ResultadoValidacion(true, "", "");
        }
        
        public static ResultadoValidacion error(String campo, String mensaje) {
            return new ResultadoValidacion(false, mensaje, campo);
        }
    }
    
    /**
     * Valida los datos del acudiente según las reglas de negocio
     * RF 3.1 - Validación de formato
     */
    public ResultadoValidacion validarDatosAcudiente(
            String primerNombre, String segundoNombre,
            String primerApellido, String segundoApellido,
            Integer edad,
            String correoElectronico, String telefono) {
        
        // Validar primer nombre (obligatorio)
        if (primerNombre == null || primerNombre.trim().isEmpty()) {
            return ResultadoValidacion.error("primerNombre", "Campo obligatorio");
        }
        if (primerNombre.length() < 2 || primerNombre.length() > 30) {
            return ResultadoValidacion.error("primerNombre", "Debe tener entre 2 y 30 caracteres");
        }
        if (!PATTERN_NOMBRE.matcher(primerNombre).matches()) {
            return ResultadoValidacion.error("primerNombre", "El nombre no debe contener números");
        }
        
        // Validar segundo nombre (opcional)
        if (segundoNombre != null && !segundoNombre.trim().isEmpty()) {
            if (segundoNombre.length() < 2 || segundoNombre.length() > 30) {
                return ResultadoValidacion.error("segundoNombre", "Debe tener entre 2 y 30 caracteres");
            }
            if (!PATTERN_NOMBRE.matcher(segundoNombre).matches()) {
                return ResultadoValidacion.error("segundoNombre", "El nombre no debe contener números");
            }
        }
        
        // Validar primer apellido (obligatorio)
        if (primerApellido == null || primerApellido.trim().isEmpty()) {
            return ResultadoValidacion.error("primerApellido", "Campo obligatorio");
        }
        if (primerApellido.length() < 2 || primerApellido.length() > 30) {
            return ResultadoValidacion.error("primerApellido", "Debe tener entre 2 y 30 caracteres");
        }
        if (!PATTERN_NOMBRE.matcher(primerApellido).matches()) {
            return ResultadoValidacion.error("primerApellido", "El apellido no debe contener números");
        }
        
        // Validar segundo apellido (opcional)
        if (segundoApellido != null && !segundoApellido.trim().isEmpty()) {
            if (segundoApellido.length() < 2 || segundoApellido.length() > 30) {
                return ResultadoValidacion.error("segundoApellido", "Debe tener entre 2 y 30 caracteres");
            }
            if (!PATTERN_NOMBRE.matcher(segundoApellido).matches()) {
                return ResultadoValidacion.error("segundoApellido", "El apellido no debe contener números");
            }
        }
        
        // Validar edad
        if (edad == null) {
            return ResultadoValidacion.error("edad", "Campo obligatorio");
        }
        if (edad < MIN_EDAD_ACUDIENTE || edad > MAX_EDAD_ACUDIENTE) {
            return ResultadoValidacion.error("edad", 
                "La edad debe estar entre " + MIN_EDAD_ACUDIENTE + " y " + MAX_EDAD_ACUDIENTE + " años");
        }
        
        // Validar correo electrónico
        if (correoElectronico == null || correoElectronico.trim().isEmpty()) {
            return ResultadoValidacion.error("correoElectronico", "Campo obligatorio");
        }
        if (!PATTERN_EMAIL.matcher(correoElectronico).matches()) {
            return ResultadoValidacion.error("correoElectronico", "Formato inválido");
        }
        
        // Validar teléfono
        if (telefono == null || telefono.trim().isEmpty()) {
            return ResultadoValidacion.error("telefono", "Campo obligatorio");
        }
        if (!PATTERN_TELEFONO.matcher(telefono).matches()) {
            return ResultadoValidacion.error("telefono", "Debe tener 10 dígitos numéricos");
        }
        
        return ResultadoValidacion.exitoso();
    }
    
    /**
     * Valida los datos del estudiante según las reglas de negocio
     * RF 3.1 - Validación de formato
     */
    public ResultadoValidacion validarDatosEstudiante(
            String primerNombre, String segundoNombre,
            String primerApellido, String segundoApellido,
            Integer edad, String nuip, String nombreGrado) {
        
        // Validar primer nombre (obligatorio)
        if (primerNombre == null || primerNombre.trim().isEmpty()) {
            return ResultadoValidacion.error("primerNombre", "Campo obligatorio");
        }
        if (primerNombre.length() < 2 || primerNombre.length() > 30) {
            return ResultadoValidacion.error("primerNombre", "Debe tener entre 2 y 30 caracteres");
        }
        if (!PATTERN_NOMBRE.matcher(primerNombre).matches()) {
            return ResultadoValidacion.error("primerNombre", "El nombre no debe contener números");
        }
        
        // Validar segundo nombre (opcional)
        if (segundoNombre != null && !segundoNombre.trim().isEmpty()) {
            if (segundoNombre.length() < 2 || segundoNombre.length() > 30) {
                return ResultadoValidacion.error("segundoNombre", "Debe tener entre 2 y 30 caracteres");
            }
            if (!PATTERN_NOMBRE.matcher(segundoNombre).matches()) {
                return ResultadoValidacion.error("segundoNombre", "El nombre no debe contener números");
            }
        }
        
        // Validar primer apellido (obligatorio)
        if (primerApellido == null || primerApellido.trim().isEmpty()) {
            return ResultadoValidacion.error("primerApellido", "Campo obligatorio");
        }
        if (primerApellido.length() < 2 || primerApellido.length() > 30) {
            return ResultadoValidacion.error("primerApellido", "Debe tener entre 2 y 30 caracteres");
        }
        if (!PATTERN_NOMBRE.matcher(primerApellido).matches()) {
            return ResultadoValidacion.error("primerApellido", "El apellido no debe contener números");
        }
        
        // Validar segundo apellido (opcional)
        if (segundoApellido != null && !segundoApellido.trim().isEmpty()) {
            if (segundoApellido.length() < 2 || segundoApellido.length() > 30) {
                return ResultadoValidacion.error("segundoApellido", "Debe tener entre 2 y 30 caracteres");
            }
            if (!PATTERN_NOMBRE.matcher(segundoApellido).matches()) {
                return ResultadoValidacion.error("segundoApellido", "El apellido no debe contener números");
            }
        }
        
        // Validar edad
        if (edad == null) {
            return ResultadoValidacion.error("edad", "Campo obligatorio");
        }
        if (edad < MIN_EDAD_ESTUDIANTE || edad > MAX_EDAD_ESTUDIANTE) {
            return ResultadoValidacion.error("edad", 
                "La edad debe estar entre " + MIN_EDAD_ESTUDIANTE + " y " + MAX_EDAD_ESTUDIANTE + " años");
        }
        
        // Validar NUIP
        if (nuip == null || nuip.trim().isEmpty()) {
            return ResultadoValidacion.error("nuip", "Campo obligatorio");
        }
        if (!PATTERN_NUIP.matcher(nuip).matches()) {
            return ResultadoValidacion.error("nuip", "Debe tener 10 dígitos numéricos");
        }
        
        // Validar grado al que aspira
        if (nombreGrado == null || nombreGrado.trim().isEmpty()) {
            return ResultadoValidacion.error("gradoAspira", "Campo obligatorio");
        }

        // Verificar que el grado existe en la base de datos
        Optional<GradoEntity> gradoOpt = gradoRepositorio.buscarPornombreGrado(nombreGrado.trim());
        if (gradoOpt.isEmpty()) {
            return ResultadoValidacion.error("gradoAspira", "Grado no válido");
        }
    
        return ResultadoValidacion.exitoso();
    }
    
    /**
     * Registra una nueva preinscripción completa
     * RF 3.1 - Proporcionar formulario de preinscripción
     * RF 3.2 - Preinscribir a más de un estudiante
     */
    public Preinscripcion registrarPreinscripcion(
            Acudiente acudiente, 
            Set<Estudiante> estudiantes) throws Exception {
        
        // Validar límite de estudiantes usando el modelo de dominio
        if (estudiantes == null || estudiantes.isEmpty()) {
            throw new IllegalArgumentException("Debe registrar al menos un estudiante");
        }
        
        // Validar usando el método del modelo de dominio
        if (estudiantes.size() > Acudiente.MAX_ESTUDIANTES) {
            throw new IllegalArgumentException(
                "Solo puede inscribir máximo " + Acudiente.MAX_ESTUDIANTES + " estudiantes");
        }
        
        EntityTransaction transaction = entityManager.getTransaction();
        
        try {
            transaction.begin();
            
            // 1. Guardar acudiente con estado Pendiente
            acudiente.setEstadoAprobacion(Estado.Pendiente);
            AcudienteEntity acudienteEntity = DominioAPersistenciaMapper.toEntity(acudiente);
            acudienteEntity = repoAcudiente.guardar(acudienteEntity);
            
            // 2. Crear y guardar preinscripción PRIMERO
            Preinscripcion preinscripcion = new Preinscripcion();
            preinscripcion.setFechaRegistro(LocalDate.now());
            preinscripcion.setEstado(Estado.Pendiente);
            
            // Convertir acudiente a dominio con ID
            Acudiente acudienteConId = DominioAPersistenciaMapper.toDomain(acudienteEntity);
            preinscripcion.setAcudiente(acudienteConId);
            
            // Guardar preinscripción para obtener ID
            PreinscripcionEntity preinscripcionEntity = 
                DominioAPersistenciaMapper.toEntityForNew(preinscripcion);
            preinscripcionEntity = repoPreinscripcion.guardar(preinscripcionEntity);
            
            // 3. Inicializar colección de estudiantes en la preinscripción
            preinscripcionEntity.setEstudiantes(new HashSet<>());
            
            // 4. Guardar estudiantes y asignarles la preinscripción
            Set<Estudiante> estudiantesGuardados = new HashSet<>();
            
            // Validar cada estudiante usando el modelo de dominio
            for (Estudiante estudiante : estudiantes) {
                try {
                    // Intentar agregar el estudiante al acudiente
                    acudienteConId.agregarEstudiante(estudiante);
                } catch (Exception e) {
                    throw new IllegalArgumentException("Error al agregar estudiante: " + e.getMessage(), e);
                }
                
                estudiante.setEstado(Estado.Pendiente);
                estudiante.setAcudiente(acudienteConId);
                
                // Buscar el grado en la base de datos
                Grado grado = estudiante.getGradoAspira();
                if (grado != null && grado.getNombreGrado() != null) {
                    Optional<GradoEntity> gradoEntityOpt = gradoRepositorio.buscarPornombreGrado(grado.getNombreGrado());
                    if (gradoEntityOpt.isPresent()) {
                        estudiante.setGradoAspira(DominioAPersistenciaMapper.toDomain(gradoEntityOpt.get()));
                    } else {
                        throw new IllegalArgumentException("Grado no encontrado: " + grado.getNombreGrado());
                    }
                }
                
                // Convertir estudiante a entidad
                EstudianteEntity estudianteEntity = DominioAPersistenciaMapper.toEntity(estudiante);
                
                // Asegurar que el acudiente está establecido
                if (estudianteEntity.getAcudiente() == null) {
                    AcudienteEntity acudienteRef = new AcudienteEntity();
                    acudienteRef.setIdUsuario(acudienteEntity.getIdUsuario());
                    estudianteEntity.setAcudiente(acudienteRef);
                }
                
                // Asignar la preinscripción al estudiante
                estudianteEntity.setPreinscripcion(preinscripcionEntity);
                
                // Guardar estudiante
                estudianteEntity = repoEstudiante.guardar(estudianteEntity);
                
                // Agregar estudiante a la colección de la preinscripción
                preinscripcionEntity.getEstudiantes().add(estudianteEntity);
                
                // Convertir de vuelta a dominio (sin preinscripción para mantener modelo limpio)
                Estudiante estudianteDomain = DominioAPersistenciaMapper.toDomain(estudianteEntity);
                estudiantesGuardados.add(estudianteDomain);
            }
            
            // 5. Actualizar objeto de dominio
            preinscripcion.setIdPreinscripcion(preinscripcionEntity.getIdPreinscripcion());
            preinscripcion.setEstudiantes(estudiantesGuardados);
            
            transaction.commit();
            
            System.out.println("NOTIFICACIÓN: Nueva preinscripción registrada - ID: " + 
                preinscripcionEntity.getIdPreinscripcion());
            
            return preinscripcion;
            
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw new Exception("Error al acceder a la base de datos, inténtelo nuevamente", e);
        }
    }

    public ResultadoValidacion validarDatosAcudienteConDuplicados(
            String nuip,
            String primerNombre, String segundoNombre,
            String primerApellido, String segundoApellido,
            Integer edad,
            String correoElectronico, String telefono) {
        
        // Primero validar formato
        ResultadoValidacion validacion = validarDatosAcudiente(
            primerNombre, segundoNombre, primerApellido, segundoApellido,
            edad, correoElectronico, telefono
        );
        
        if (!validacion.isValido()) {
            return validacion;
        }

        if (usuarioRepositorio.existePorNuip(nuip)){
            return ResultadoValidacion.error("nuip", 
                "Ya existe un usuario registrado con este NUIP"
            );
        }
        
        // Verificar duplicados
        if (usuarioRepositorio.existePorCorreo(correoElectronico)) {
            return ResultadoValidacion.error("correoElectronico", 
                "Ya existe un acudiente registrado con este correo electrónico");
        }
        
        if (usuarioRepositorio.existePorTelefono(telefono)) {
            return ResultadoValidacion.error("telefono", 
                "Ya existe un acudiente registrado con este número de teléfono");
        }
        
        return ResultadoValidacion.exitoso();
    }
    
    /**
     * Valida los datos del estudiante incluyendo duplicados
     */
    public ResultadoValidacion validarDatosEstudianteConDuplicados(
            String primerNombre, String segundoNombre,
            String primerApellido, String segundoApellido,
            Integer edad, String nuip, String nombreGrado) {
        
        // Primero validar formato
        ResultadoValidacion validacion = validarDatosEstudiante(
            primerNombre, segundoNombre, primerApellido, segundoApellido,
            edad, nuip, nombreGrado
        );
        
        if (!validacion.isValido()) {
            return validacion;
        }
        
        // Verificar duplicado de NUIP
        if (estudianteRepositorio.existePorNuip(nuip)) {
            return ResultadoValidacion.error("nuip", 
                "Ya existe un estudiante registrado con este NUIP");
        }
        
        return ResultadoValidacion.exitoso();
    }
    
    /**
     * Métodos auxiliares que usan el modelo de dominio
     */
    public boolean puedeAgregarMasEstudiantes(Acudiente acudiente) {
        return acudiente.puedeAgregarMasEstudiantes();
    }
    
    public int obtenerCuposRestantes(Acudiente acudiente) {
        return acudiente.obtenerCuposRestantes();
    }
}
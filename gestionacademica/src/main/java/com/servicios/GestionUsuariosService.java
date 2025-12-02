package com.servicios;

import com.dominio.*;
import com.persistencia.repositorios.*;
import com.persistencia.mappers.DominioAPersistenciaMapper;
import com.persistencia.entidades.*;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Optional;
import java.util.Random;

/**
 * Servicio de gestión de usuarios - Capa de Servicios
 * Responsabilidad: Casos de uso, orquestación transaccional, coordinación de entidades
 * CU 2.1, CU 2.3, CU 2.4
 */
public class GestionUsuariosService {
    
private final EntityManagerFactory emf;
    private final UsuarioRepositorio repositorioUsuario;
    private final RolRepositorio repositorioRol; // ✅ Nuevo repositorio
    private final EmailService emailService;
    
    public GestionUsuariosService() {
        this.emf = Persistence.createEntityManagerFactory("GestionAcademica");
        EntityManager em = emf.createEntityManager();
        
        this.repositorioUsuario = new UsuarioRepositorio(em);
        this.repositorioRol = new RolRepositorio(em); // ✅ Inicializar
        this.emailService = new EmailService();
    }
    
    /**
     * CU 2.3 - Crear usuario
     * Versión modificada para aceptar nombre de rol en lugar de objeto Rol
     */
    public ResultadoOperacion crearUsuario(Usuario usuario, String nombreRol) {
        EntityManager em = emf.createEntityManager();
        
        try {
            em.getTransaction().begin();
            
            // Buscar el rol por nombre
            Optional<RolEntity> rolEntityOpt = repositorioRol.buscarPorNombreRol(nombreRol.toLowerCase());
            System.out.println(rolEntityOpt);
            
            if (rolEntityOpt.isEmpty()) {
                return ResultadoOperacion.error("El rol '" + nombreRol + "' no existe en el sistema");
            }
            
            RolEntity rolEntity = rolEntityOpt.get();
            
            // Validar duplicados de email y teléfono
            if (repositorioUsuario.existePorCorreo(usuario.getCorreoElectronico())) {
                return ResultadoOperacion.error("Ya existe un usuario con ese correo electrónico");
            }
            
            if (repositorioUsuario.existePorTelefono(usuario.getTelefono())) {
                return ResultadoOperacion.error("Ya existe un usuario con ese número de teléfono");
            }
            
            // Generar token de usuario
            TokenUsuario tokenUsuario = generarTokenUsuario(usuario);
            
            Rol rol = DominioAPersistenciaMapper.toDomain(rolEntity);
            tokenUsuario.setRol(rol);
            usuario.setTokenAccess(tokenUsuario);
            
            // Crear tokenEntity con el rol asociado
            TokenUsuarioEntity tokenEntity = new TokenUsuarioEntity();
            tokenEntity.setNombreUsuario(tokenUsuario.getNombreUsuario());
            tokenEntity.setContrasena(tokenUsuario.getContrasena());
            tokenEntity.setRol(rolEntity); // RolEntity ya managed por JPA
            
            em.persist(tokenEntity);
            
            // Crear entidad según el tipo de usuario
            UsuarioEntity usuarioEntity = null;
            String tipoUsuario = usuario.getClass().getSimpleName();
            
            switch (tipoUsuario) {
                case "Profesor":
                    Profesor profesor = (Profesor) usuario;
                    ProfesorEntity profesorEntity = DominioAPersistenciaMapper.toEntity(profesor);
                    
                    // Asignar el tokenEntity ya persistido**
                    profesorEntity.setTokenAccess(tokenEntity);
                    
                    em.persist(profesorEntity);
                    usuarioEntity = profesorEntity;
                    break;
                    
                case "Directivo":
                    Directivo directivo = (Directivo) usuario;
                    DirectivoEntity directivoEntity = DominioAPersistenciaMapper.toEntity(directivo);
                    
                    // Asignar el tokenEntity ya persistido**
                    directivoEntity.setTokenAccess(tokenEntity);
                    
                    em.persist(directivoEntity);
                    usuarioEntity = directivoEntity;
                    break;
            }
            
            em.getTransaction().commit();
            
            // Enviar credenciales por correo
            if (usuario.getCorreoElectronico() != null && !usuario.getCorreoElectronico().isEmpty()) {
                emailService.enviarCredenciales(
                    usuario.getCorreoElectronico(), 
                    tokenUsuario, 
                    usuario.obtenerNombreCompleto()
                );
            }
            
            return ResultadoOperacion.exito("Usuario creado exitosamente", usuario);
            
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
            return ResultadoOperacion.error("Error al crear usuario: " + e.getMessage());
        } finally {
            em.close();
        }
    }

    /**
     * CU 2.4 - Consultar información del usuario autenticado
     * Método específico para el caso de uso 2.4 (Mi Información)
     */
    public ResultadoOperacion consultarMiInformacion(Usuario usuarioAutenticado) {
        if (usuarioAutenticado == null) {
            return ResultadoOperacion.error("Usuario no autenticado");
        }
        
        if (usuarioAutenticado.getIdUsuario() == null) {
            return ResultadoOperacion.error("ID de usuario no válido");
        }
        
        // Usar el ID del usuario autenticado para consultar
        return consultarUsuario(usuarioAutenticado.getIdUsuario());
    }
    
    /**
     * CU 2.4 - Consultar información de usuario
     * Recupera la información completa de un usuario por su ID
     */
    public ResultadoOperacion consultarUsuario(Integer usuarioId) {
        EntityManager em = emf.createEntityManager();
        
        try {
            if (usuarioId == null) {
                return ResultadoOperacion.error("ID de usuario no válido");
            }
            
            Optional<UsuarioEntity> usuarioEntityOpt = repositorioUsuario.buscarPorIdOpt(usuarioId);
            
            if (usuarioEntityOpt.isEmpty()) {
                return ResultadoOperacion.error("Usuario no encontrado");
            }
            
            // Se delega todo al mapperEspecializado
            Usuario usuario = mapearEntidadADominio(usuarioEntityOpt.get());
            
            return ResultadoOperacion.exito("Consulta exitosa", usuario);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResultadoOperacion.error("Error al consultar usuario: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    // Metodo que DELEGA completamente al mapper
    private Usuario mapearEntidadADominio(UsuarioEntity usuarioEntity) {
        if (usuarioEntity == null) {
            return null;
        }
        
        try {
            // Usar el método apropiado del mapper existente
            if (usuarioEntity instanceof ProfesorEntity) {
                return DominioAPersistenciaMapper.toDomainComplete((ProfesorEntity) usuarioEntity);
            } else if (usuarioEntity instanceof DirectivoEntity) {
                return DominioAPersistenciaMapper.toDomain((DirectivoEntity) usuarioEntity);
            } else if (usuarioEntity instanceof AdministradorEntity) {
                return DominioAPersistenciaMapper.toDomain((AdministradorEntity) usuarioEntity);
            } else if (usuarioEntity instanceof AcudienteEntity) {
                return DominioAPersistenciaMapper.toDomainComplete((AcudienteEntity) usuarioEntity);
            }
            
            return null; // No debería llegar aquí
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // ==================== Métodos privados ====================
    private TokenUsuario generarTokenUsuario(Usuario usuario) {
        // Validar campos obligatorios
        if (usuario.getPrimerNombre() == null || usuario.getPrimerApellido() == null) {
            throw new IllegalArgumentException("Nombre y apellido son obligatorios");
        }
        
        StringBuilder nombreUsuarioBuilder = new StringBuilder();
        
        // Primera letra del primer nombre
        if (!usuario.getPrimerNombre().isEmpty()) {
            nombreUsuarioBuilder.append(usuario.getPrimerNombre().charAt(0));
        }
        
        // Primera letra del segundo nombre (si existe)
        if (usuario.getSegundoNombre() != null && !usuario.getSegundoNombre().isEmpty()) {
            nombreUsuarioBuilder.append(usuario.getSegundoNombre().charAt(0));
        }
        
        // Apellido completo
        nombreUsuarioBuilder.append(usuario.getPrimerApellido().toLowerCase().replaceAll("\\s+", ""));
        
        // Primera letra del segundo apellido (si existe)
        if (usuario.getSegundoApellido() != null && !usuario.getSegundoApellido().isEmpty()) {
            nombreUsuarioBuilder.append(usuario.getSegundoApellido().toLowerCase().charAt(0));
        }
        
        // Eliminar tildes y caracteres especiales
        String nombreUsuario = normalizarTexto(nombreUsuarioBuilder.toString());
        
        TokenUsuario token = new TokenUsuario();
        token.setNombreUsuario(nombreUsuario);
        token.setContrasena(generarContrasenaAleatoria());
        
        return token;
    }

    private String normalizarTexto(String texto) {
    if (texto == null || texto.isEmpty()) {
        return texto;
    }
    
    // Convertir a minúsculas
    String normalizado = texto.toLowerCase();
    
    // Reemplazar vocales con tildes
    normalizado = normalizado
        .replace('á', 'a')
        .replace('é', 'e')
        .replace('í', 'i')
        .replace('ó', 'o')
        .replace('ú', 'u')
        .replace('ü', 'u')
        .replace('ñ', 'n');
    
    // Eliminar caracteres especiales, mantener solo letras y números
    normalizado = normalizado.replaceAll("[^a-z0-9]", "");
    
    return normalizado;
}
    
    private String generarContrasenaAleatoria() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(8);
        
        for (int i = 0; i < 8; i++) {
            sb.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        
        return sb.toString();
    }
}


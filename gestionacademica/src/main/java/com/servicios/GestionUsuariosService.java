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
            if (existeUsuarioPorEmail(em, usuario.getCorreoElectronico())) {
                return ResultadoOperacion.error("Ya existe un usuario con ese correo electrónico");
            }
            
            if (existeUsuarioPorTelefono(em, usuario.getTelefono())) {
                return ResultadoOperacion.error("Ya existe un usuario con ese número de teléfono");
            }
            
            // Generar token de usuario
            TokenUsuario tokenUsuario = generarTokenUsuario(usuario);
            
            // **CORRECCIÓN: Asignar rol al token en dominio**
            Rol rol = DominioAPersistenciaMapper.toDomain(rolEntity);
            tokenUsuario.setRol(rol);
            usuario.setTokenAccess(tokenUsuario);
            
            // Validar nombre de usuario único (ya lo hace generarTokenUsuario, pero por seguridad)
            if (existeNombreUsuario(em, tokenUsuario.getNombreUsuario())) {
                return ResultadoOperacion.error("El nombre de usuario ya está en uso");
            }
            
            // **IMPORTANTE: Crear tokenEntity con el rol asociado**
            TokenUsuarioEntity tokenEntity = new TokenUsuarioEntity();
            tokenEntity.setNombreUsuario(tokenUsuario.getNombreUsuario());
            tokenEntity.setContrasena(tokenUsuario.getContrasena());
            tokenEntity.setRol(rolEntity); // RolEntity ya managed por JPA
            
            // **CORRECCIÓN CRÍTICA: Persistir el token UNA sola vez**
            em.persist(tokenEntity);
            
            // Crear entidad según el tipo de usuario
            UsuarioEntity usuarioEntity = null;
            String tipoUsuario = usuario.getClass().getSimpleName();
            
            switch (tipoUsuario) {
                case "Profesor":
                    Profesor profesor = (Profesor) usuario;
                    ProfesorEntity profesorEntity = DominioAPersistenciaMapper.toEntity(profesor);
                    
                    // **CORRECCIÓN: Asignar el tokenEntity ya persistido**
                    profesorEntity.setTokenAccess(tokenEntity);
                    
                    em.persist(profesorEntity);
                    usuarioEntity = profesorEntity;
                    break;
                    
                case "Directivo":
                    Directivo directivo = (Directivo) usuario;
                    DirectivoEntity directivoEntity = DominioAPersistenciaMapper.toEntity(directivo);
                    
                    // **CORRECCIÓN: Asignar el tokenEntity ya persistido**
                    directivoEntity.setTokenAccess(tokenEntity);
                    
                    em.persist(directivoEntity);
                    usuarioEntity = directivoEntity;
                    break;
                    
                // ... otros casos similares
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
            
            Optional<UsuarioEntity> usuarioEntity = repositorioUsuario.buscarPorIdOpt(usuarioId);
            
            if (usuarioEntity == null) {
                return ResultadoOperacion.error("Usuario no encontrado");
            }
            
            // Determinar el tipo de usuario y mapear a dominio
            Usuario usuario = mapearEntidadADominio(usuarioEntity.get());
            
            if (usuario == null) {
                return ResultadoOperacion.error("Error al mapear la entidad a dominio");
            }
            
            return ResultadoOperacion.exito("Consulta exitosa", usuario);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResultadoOperacion.error("Error al consultar usuario: " + e.getMessage());
        } finally {
            em.close();
        }
    }
    
    /**
     * Método auxiliar para mapear entidades a objetos de dominio
     */
    private Usuario mapearEntidadADominio(UsuarioEntity usuarioEntity) {
        if (usuarioEntity == null) {
            return null;
        }
        
        try {
            if (usuarioEntity instanceof ProfesorEntity profesorEntity) {
                return DominioAPersistenciaMapper.toDomainComplete(profesorEntity);
            } else if (usuarioEntity instanceof DirectivoEntity directivoEntity) {
                return DominioAPersistenciaMapper.toDomain(directivoEntity);
            } else if (usuarioEntity instanceof AdministradorEntity adminEntity) {
                return DominioAPersistenciaMapper.toDomain(adminEntity);
            } else if (usuarioEntity instanceof AcudienteEntity acudienteEntity) {
                return DominioAPersistenciaMapper.toDomainComplete(acudienteEntity);
            } else {
                // Es un UsuarioEntity base (no debería ocurrir)
                Usuario usuario = new Usuario();
                usuario.setIdUsuario(usuarioEntity.getIdUsuario());
                usuario.setPrimerNombre(usuarioEntity.getPrimerNombre());
                usuario.setSegundoNombre(usuarioEntity.getSegundoNombre());
                usuario.setPrimerApellido(usuarioEntity.getPrimerApellido());
                usuario.setSegundoApellido(usuarioEntity.getSegundoApellido());
                usuario.setEdad(usuarioEntity.getEdad());
                usuario.setCorreoElectronico(usuarioEntity.getCorreoElectronico());
                usuario.setTelefono(usuarioEntity.getTelefono());
                if (usuarioEntity.getTokenAccess() != null) {
                    usuario.setTokenAccess(DominioAPersistenciaMapper.toDomain(usuarioEntity.getTokenAccess()));
                }
                return usuario;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    // ==================== Métodos privados ====================
    
    private TokenUsuario generarTokenUsuario(Usuario usuario) {
        // Generar nombre de usuario: primera letra del primer nombre + primer apellido
        String nombreUsuario = "";
        if (usuario.getPrimerNombre() != null && usuario.getPrimerApellido() != null) {
            nombreUsuario = (usuario.getPrimerNombre().substring(0, 1) + 
                          usuario.getPrimerApellido()).toLowerCase().replaceAll("\\s+", "");
        } else {
            // Si no hay nombre, generar uno aleatorio
            nombreUsuario = "user" + new Random().nextInt(10000);
        }
        
        // Contraseña temporal: 8 caracteres aleatorios
        String contrasena = generarContrasenaAleatoria();
        
        // Crear token (el ID se asignará al persistir)
        TokenUsuario token = new TokenUsuario();
        token.setNombreUsuario(nombreUsuario);
        token.setContrasena(contrasena);
        // El rol se asignará desde el parámetro en crearUsuario
        
        return token;
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
    
    private boolean existeUsuarioPorEmail(EntityManager em, String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        try {
            String jpql = "SELECT COUNT(u) FROM usuario u WHERE u.correoElectronico = :email";
            Long count = em.createQuery(jpql, Long.class)
                          .setParameter("email", email)
                          .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean existeUsuarioPorTelefono(EntityManager em, String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            return false;
        }
        
        try {
            String jpql = "SELECT COUNT(u) FROM usuario u WHERE u.telefono = :telefono";
            Long count = em.createQuery(jpql, Long.class)
                          .setParameter("telefono", telefono)
                          .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private boolean existeNombreUsuario(EntityManager em, String nombreUsuario) {
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
            return false;
        }
        
        try {
            String jpql = "SELECT COUNT(t) FROM TokenUsuarioEntity t WHERE t.nombreUsuario = :nombreUsuario";
            Long count = em.createQuery(jpql, Long.class)
                          .setParameter("nombreUsuario", nombreUsuario)
                          .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}


package com.servicios;

import com.dominio.*;
import com.persistencia.repositorios.*;
import com.persistencia.mappers.DominioAPersistenciaMapper;
import com.persistencia.entidades.*;
import com.aplicacion.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.Optional;
import java.util.Random;

/**
 * Servicio de gestión de usuarios - Capa de Servicios
 * Responsabilidad: Casos de uso, orquestación transaccional, coordinación de entidades
 */
public class GestionUsuariosService {
    private final EntityManager em;
    private final UsuarioRepositorio repositorioUsuario;
    private final RolRepositorio repositorioRol;
    private final TokenUsuarioRepositorio repositorioTokenUsuario;
    private final EmailService emailService;

    public GestionUsuariosService() {
        EntityManagerFactory emf = JPAUtil.getEntityManagerFactory();
        this.em = emf.createEntityManager();
        this.repositorioUsuario = new UsuarioRepositorio(em);
        this.repositorioRol = new RolRepositorio(em);
        this.repositorioTokenUsuario = new TokenUsuarioRepositorio(em);
        this.emailService = new EmailService();
    }
    
    /**
     * CU 2.3 - Crear usuario con transacción única
     */
    public ResultadoOperacion crearUsuario(Usuario usuario, String nombreRol) {
        try {
            // 1. INICIAR TRANSACCIÓN (única para todo el caso de uso)
            em.getTransaction().begin();
            
            try {
                // 2. Validaciones de negocio (dentro de la transacción)
                
                // 2.1 Validar rol
                Optional<RolEntity> rolEntityOpt = repositorioRol.buscarPorNombreRol(nombreRol.toLowerCase());
                if (rolEntityOpt.isEmpty()) {
                    em.getTransaction().rollback();
                    return ResultadoOperacion.error("El rol '" + nombreRol + "' no existe en el sistema");
                }
                RolEntity rolEntity = rolEntityOpt.get();
                
                // 2.2 Validar duplicados
                if (repositorioUsuario.existePorCorreo(usuario.getCorreoElectronico())) {
                    em.getTransaction().rollback();
                    return ResultadoOperacion.error("Ya existe un usuario con ese correo electrónico");
                }
                
                if (repositorioUsuario.existePorTelefono(usuario.getTelefono())) {
                    em.getTransaction().rollback();
                    return ResultadoOperacion.error("Ya existe un usuario con ese número de teléfono");
                }
                
                // 3. Generar token (lógica de negocio)
                TokenUsuario tokenUsuario = generarTokenUsuario(usuario);
                Rol rol = DominioAPersistenciaMapper.toDomain(rolEntity);
                tokenUsuario.setRol(rol);
                usuario.setTokenAccess(tokenUsuario);
                
                // 4. Mapear a entidades
                TokenUsuarioEntity tokenEntity = new TokenUsuarioEntity();
                tokenEntity.setNombreUsuario(tokenUsuario.getNombreUsuario());
                tokenEntity.setContrasena(tokenUsuario.getContrasena());
                tokenEntity.setRol(rolEntity);
                
                // 5. Persistir usando repositorios (TODO dentro de la misma transacción)
                repositorioTokenUsuario.guardar(tokenEntity);
                
                // 6. Crear y guardar usuario según el tipo
                UsuarioEntity usuarioEntity = null;
                String tipoUsuario = usuario.getClass().getSimpleName();
                
                switch (tipoUsuario) {
                    case "Profesor":
                        Profesor profesor = (Profesor) usuario;
                        ProfesorEntity profesorEntity = DominioAPersistenciaMapper.toEntity(profesor);
                        profesorEntity.setTokenAccess(tokenEntity);
                        repositorioUsuario.guardar(profesorEntity);
                        usuarioEntity = profesorEntity;
                        usuario.setIdUsuario(profesorEntity.getIdUsuario());
                        break;
                        
                    case "Directivo":
                        Directivo directivo = (Directivo) usuario;
                        DirectivoEntity directivoEntity = DominioAPersistenciaMapper.toEntity(directivo);
                        directivoEntity.setTokenAccess(tokenEntity);
                        repositorioUsuario.guardar(directivoEntity);
                        usuarioEntity = directivoEntity;
                        usuario.setIdUsuario(directivoEntity.getIdUsuario());
                        break;
                }
                
                // 7. CONFIRMAR TRANSACCIÓN (todo se persiste aquí)
                em.getTransaction().commit();
                
                // 8. Operaciones fuera de la transacción (envío de email)
                if (usuario.getCorreoElectronico() != null && !usuario.getCorreoElectronico().isEmpty()) {
                    emailService.enviarCredenciales(
                        usuario.getCorreoElectronico(), 
                        tokenUsuario, 
                        usuario.obtenerNombreCompleto()
                    );
                }
                
                return ResultadoOperacion.exito("Usuario creado exitosamente", usuario);
                
            } catch (Exception e) {
                // Rollback si hay error en la lógica de negocio
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw e; // Re-lanzar para manejo en el catch externo
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResultadoOperacion.error("Error al crear usuario: " + e.getMessage());
        }
    }
    
    /**
     * CU 2.4 - Consultar información de usuario (solo lectura)
     */
    public ResultadoOperacion consultarUsuario(Integer usuarioId) {
        try {
            if (usuarioId == null) {
                return ResultadoOperacion.error("ID de usuario no válido");
            }
            
            Optional<UsuarioEntity> usuarioEntityOpt = repositorioUsuario.buscarPorId(usuarioId);
            
            if (usuarioEntityOpt.isEmpty()) {
                return ResultadoOperacion.error("Usuario no encontrado");
            }
            
            Usuario usuario = mapearEntidadADominio(usuarioEntityOpt.get());
            return ResultadoOperacion.exito("Consulta exitosa", usuario);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResultadoOperacion.error("Error al consultar usuario: " + e.getMessage());
        }
    }

    /**
     * CU 2.4 - Consultar información del usuario autenticado
     */
    public ResultadoOperacion consultarMiInformacion(Usuario usuarioAutenticado) {
        if (usuarioAutenticado == null) {
            return ResultadoOperacion.error("Usuario no autenticado");
        }
        
        if (usuarioAutenticado.getIdUsuario() == null) {
            return ResultadoOperacion.error("ID de usuario no válido");
        }
        
        return consultarUsuario(usuarioAutenticado.getIdUsuario());
    }
    
    /**
     * Método para cerrar el EntityManager cuando ya no se necesite
     * Útil para el constructor por defecto
     */
    public void cerrar() {
        if (em != null && em.isOpen()) {
            em.close();
        }
    }
    
    // ==================== MÉTODOS PRIVADOS ====================
    
    private Usuario mapearEntidadADominio(UsuarioEntity usuarioEntity) {
        if (usuarioEntity == null) {
            return null;
        }
        
        try {
            if (usuarioEntity instanceof ProfesorEntity) {
                return DominioAPersistenciaMapper.toDomainComplete((ProfesorEntity) usuarioEntity);
            } else if (usuarioEntity instanceof DirectivoEntity) {
                return DominioAPersistenciaMapper.toDomain((DirectivoEntity) usuarioEntity);
            } else if (usuarioEntity instanceof AdministradorEntity) {
                return DominioAPersistenciaMapper.toDomain((AdministradorEntity) usuarioEntity);
            } else if (usuarioEntity instanceof AcudienteEntity) {
                return DominioAPersistenciaMapper.toDomainComplete((AcudienteEntity) usuarioEntity);
            }
            
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public TokenUsuario generarTokenUsuario(Usuario usuario) {
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

    public String normalizarTexto(String texto) {
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
    
    public String generarContrasenaAleatoria() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$";
        Random random = new Random();
        StringBuilder sb = new StringBuilder(8);
        
        for (int i = 0; i < 8; i++) {
            sb.append(caracteres.charAt(random.nextInt(caracteres.length())));
        }
        
        return sb.toString();
    }
}


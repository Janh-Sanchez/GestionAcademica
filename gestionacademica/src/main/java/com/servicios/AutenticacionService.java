package com.servicios;

import java.util.Optional;
import javax.swing.JOptionPane;

import com.dominio.*;
import com.persistencia.entidades.AcudienteEntity;
import com.persistencia.entidades.AdministradorEntity;
import com.persistencia.entidades.DirectivoEntity;
import com.persistencia.entidades.ProfesorEntity;
import com.persistencia.entidades.TokenUsuarioEntity;
import com.persistencia.entidades.UsuarioEntity;
import com.persistencia.mappers.DominioAPersistenciaMapper;
import com.persistencia.repositorios.AcudienteRepositorio;
import com.persistencia.repositorios.TokenUsuarioRepositorio;
import com.persistencia.repositorios.UsuarioRepositorio;

public class AutenticacionService {
    private final TokenUsuarioRepositorio tokenRepositorio;
    private final UsuarioRepositorio usuarioRepositorio;
    private int intentosFallidos = 0;
    private static final int MAX_INTENTOS = 3;

    public AutenticacionService(
            TokenUsuarioRepositorio tokenRepositorio,
            UsuarioRepositorio usuarioRepositorio) {
        
        this.tokenRepositorio = tokenRepositorio;
        this.usuarioRepositorio = usuarioRepositorio;
    }

    public Optional<Usuario> iniciarSesion(String nombreUsuario, String contrasena) {
        if (intentosFallidos >= MAX_INTENTOS) {
            throw new IllegalStateException("Límite de intentos alcanzado. La opción de inicio de sesión está temporalmente inhabilitada");
        }

        try {
            // 1. Buscar token en BD
            Optional<TokenUsuarioEntity> tokenEntityOpt = 
                tokenRepositorio.buscarPorNombreUsuario(nombreUsuario);
            
            if (tokenEntityOpt.isEmpty()) {
                intentosFallidos++;
                return Optional.empty();
            }

            // 2. Convertir a dominio y verificar credenciales
            TokenUsuario token = DominioAPersistenciaMapper.toDomain(tokenEntityOpt.get());

            if (!token.verificarCredenciales(contrasena)) {
                intentosFallidos++;
                return Optional.empty();
            }

            // 3. Buscar usuario por token - JPA automáticamente carga la entidad específica
            Optional<UsuarioEntity> usuarioEntityOpt = usuarioRepositorio.buscarPorToken(token.getIdToken());
            
            if (usuarioEntityOpt.isEmpty()) {
                intentosFallidos++;
                return Optional.empty();
            }

            // 4. Convertir a dominio (JPA ya determina el tipo específico)
            Usuario usuario = convertirAUsuarioEspecifico(usuarioEntityOpt.get());

            if (usuario == null) {
                intentosFallidos++;
                return Optional.empty();
            }

            intentosFallidos = 0;
            return Optional.of(usuario);

        } catch (Exception e) {
            throw new RuntimeException("Error al acceder a la base de datos: " + e.getMessage(), e);
        }
    }
    
    private Usuario convertirAUsuarioEspecifico(UsuarioEntity usuarioEntity) {
        if (usuarioEntity == null) return null;
        
        switch (usuarioEntity.getClass().getSimpleName()) {
            case "AdministradorEntity":
                return DominioAPersistenciaMapper.toDomain((AdministradorEntity) usuarioEntity);
            case "ProfesorEntity":
                return DominioAPersistenciaMapper.toDomain((ProfesorEntity) usuarioEntity);
            case "DirectivoEntity":
                return DominioAPersistenciaMapper.toDomain((DirectivoEntity) usuarioEntity);
            case "AcudienteEntity":
                // Para acudientes, cargar los estudiantes explícitamente
                AcudienteEntity acudienteEntity = (AcudienteEntity) usuarioEntity;
                try {
                    // Obtener el EntityManager del repositorio
                    AcudienteRepositorio acudienteRepo = new AcudienteRepositorio(
                        usuarioRepositorio.getEntityManager()); // Necesitarás agregar este método
                    
                    // Cargar acudiente con estudiantes
                    AcudienteEntity acudienteConEstudiantes = acudienteRepo.buscarConEstudiantes(
                        acudienteEntity.getIdUsuario());
                    
                    if (acudienteConEstudiantes != null) {
                        return DominioAPersistenciaMapper.toDomainComplete(acudienteConEstudiantes);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // Fallback: usar el mapper normal
                }
                return DominioAPersistenciaMapper.toDomain(acudienteEntity);
            default:
                JOptionPane.showMessageDialog(null,
                    "Tipo de usuario no reconocido: " + usuarioEntity.getClass().getSimpleName(),
                    "Error de autenticación",
                    JOptionPane.ERROR_MESSAGE);
                return null;
        }
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }
    
    public int getIntentosRestantes() {
        return MAX_INTENTOS - intentosFallidos;
    }
}
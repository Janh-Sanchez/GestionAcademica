package com.servicios;

import com.dominio.TokenUsuario;
import com.persistencia.entidades.TokenUsuarioEntity;
import com.persistencia.repositorios.TokenUsuarioRepositorio;
import com.persistencia.mappers.DominioAPersistenciaMapper;
import java.util.Optional;

public class AutenticacionService {
    private final TokenUsuarioRepositorio tokenRepositorio;
    private int intentosFallidos = 0;
    private static final int MAX_INTENTOS = 3;

    public AutenticacionService(TokenUsuarioRepositorio tokenRepositorio) {
        this.tokenRepositorio = tokenRepositorio;
    }

    public ResultadoAutenticacion iniciarSesion(String nombreUsuario, String contrasena) {
        // Validar campos vacíos
        if (nombreUsuario == null || nombreUsuario.trim().isEmpty() ||
            contrasena == null || contrasena.trim().isEmpty()) {
            return new ResultadoAutenticacion(false, null, "Por favor, llene todos los campos");
        }

        // Verificar intentos
        if (intentosFallidos >= MAX_INTENTOS) {
            return new ResultadoAutenticacion(false, null, 
                "Límite de intentos alcanzado. La opción de inicio de sesión está temporalmente inhabilitada");
        }

        try {
            Optional<TokenUsuarioEntity> tokenEntityOpt = tokenRepositorio.buscarPorNombreUsuario(nombreUsuario);
            
            if (tokenEntityOpt.isEmpty()) {
                intentosFallidos++;
                return new ResultadoAutenticacion(false, null, 
                    "Usuario o contraseña incorrectos, inténtelo nuevamente");
            }

            TokenUsuarioEntity tokenEntity = tokenEntityOpt.get();

            if (!contrasena.equals(tokenEntity.getContrasena())) {
                intentosFallidos++;
                return new ResultadoAutenticacion(false, null, 
                    "Usuario o contraseña incorrectos, inténtelo nuevamente");
            }

            // Autenticación exitosa
            intentosFallidos = 0;
            TokenUsuario tokenDomain = DominioAPersistenciaMapper.toDomain(tokenEntity);
            
            return new ResultadoAutenticacion(true, tokenDomain, 
                "Inicio de sesión exitoso con rol: " + tokenDomain.getRol().getNombre());

        } catch (Exception e) {
            return new ResultadoAutenticacion(false, null, 
                "Hubo un error al acceder a la base de datos, inténtelo nuevamente");
        }
    }

    public int getIntentosFallidos() {
        return intentosFallidos;
    }

    public void resetearIntentos() {
        intentosFallidos = 0;
    }

    public static class ResultadoAutenticacion {
        private final boolean exitoso;
        private final TokenUsuario token;
        private final String mensaje;

        public ResultadoAutenticacion(boolean exitoso, TokenUsuario token, String mensaje) {
            this.exitoso = exitoso;
            this.token = token;
            this.mensaje = mensaje;
        }

        public boolean isExitoso() { return exitoso; }
        public TokenUsuario getToken() { return token; }
        public String getMensaje() { return mensaje; }
    }
}
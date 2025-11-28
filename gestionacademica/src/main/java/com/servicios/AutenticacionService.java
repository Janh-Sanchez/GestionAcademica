package com.servicios;

import java.util.Optional;

import com.dominio.TokenUsuario;
import com.persistencia.entidades.TokenUsuarioEntity;
import com.persistencia.mappers.DominioAPersistenciaMapper;
import com.persistencia.repositorios.TokenUsuarioRepositorio;

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
            return new ResultadoAutenticacion(false, null, 
                "Por favor, llene todos los campos");
        }

        // Verificar intentos
        if (intentosFallidos >= MAX_INTENTOS) {
            return new ResultadoAutenticacion(false, null, 
                "Límite de intentos alcanzado. La opción de inicio de sesión está temporalmente inhabilitada");
        }

        try {
            // Buscar token en BD
            Optional<TokenUsuarioEntity> tokenEntityOpt = 
                tokenRepositorio.buscarPorNombreUsuario(nombreUsuario);
            
            if (tokenEntityOpt.isEmpty()) {
                return new ResultadoAutenticacion(false, null, 
                    "Usuario o contraseña incorrectos, inténtelo nuevamente");
            }

            // Convertir a dominio
            TokenUsuario token = DominioAPersistenciaMapper.toDomain(tokenEntityOpt.get());

            // EL OBJETO SE VALIDA A SÍ MISMO
            if (!token.verificarCredenciales(contrasena)) {
                intentosFallidos++;
                String mensaje = ("Usuario o contraseña incorrectos, inténtelo nuevamente");
                return new ResultadoAutenticacion(false, null, mensaje);
            }

            intentosFallidos = 0;
            return new ResultadoAutenticacion(true, token, 
                "Inicio de sesión exitoso con rol: " + token.getRol().getNombre());

        } catch (Exception e) {
            return new ResultadoAutenticacion(false, null, 
                "Hubo un error al acceder a la base de datos, inténtelo nuevamente");
        }
    }

    public int getIntentosFallidos(){
        return intentosFallidos;
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
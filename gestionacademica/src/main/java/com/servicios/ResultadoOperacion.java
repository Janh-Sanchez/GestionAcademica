package com.servicios;

public class ResultadoOperacion {
    private boolean exitoso;
    private String mensaje;
    private Object datos;
    
    private ResultadoOperacion(boolean exitoso, String mensaje, Object datos) {
        this.exitoso = exitoso;
        this.mensaje = mensaje;
        this.datos = datos;
    }
    
    public static ResultadoOperacion exito(String mensaje, Object datos) {
        return new ResultadoOperacion(true, mensaje, datos);
    }
    
    public static ResultadoOperacion error(String mensaje) {
        return new ResultadoOperacion(false, mensaje, null);
    }
    
    public boolean isExitoso() { return exitoso; }
    public String getMensaje() { return mensaje; }
    public Object getDatos() { return datos; }
}

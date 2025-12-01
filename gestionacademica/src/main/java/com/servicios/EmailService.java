package com.servicios;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

/**
 * Servicio para envío de correos electrónicos
 * Utilizado en CU 2.3 para enviar credenciales a usuarios nuevos
 */
public class EmailService {
    
    private final String host;
    private final String port;
    private final String username;
    private final String password;
    private final boolean usarSSL;
    
    public EmailService() {
        // Configuración desde properties o variables de entorno
        this.host = System.getProperty("mail.smtp.host", "smtp.gmail.com");
        this.port = System.getProperty("mail.smtp.port", "587");
        this.username = System.getProperty("mail.username", "sistema@colegio.edu.co");
        this.password = System.getProperty("mail.password", "");
        this.usarSSL = Boolean.parseBoolean(System.getProperty("mail.smtp.ssl", "true"));
    }
    
    /**
     * Envía las credenciales de acceso al nuevo usuario
     */
    public void enviarCredenciales(String destinatario, Object credenciales, String nombreUsuario) {
        try {
            // Obtener credenciales
            String usuario = ((com.servicios.Credenciales) credenciales).getUsuario();
            String contrasena = ((com.servicios.Credenciales) credenciales).getContrasena();
            
            String asunto = "Bienvenido al Sistema de Gestión Académica";
            String cuerpo = construirCuerpoEmail(nombreUsuario, usuario, contrasena);
            
            enviarEmail(destinatario, asunto, cuerpo);
            
            System.out.println("✓ Credenciales enviadas exitosamente a: " + destinatario);
            
        } catch (Exception e) {
            System.err.println("Error al enviar correo: " + e.getMessage());
            // Log del error, pero no falla la operación principal
        }
    }
    
    private String construirCuerpoEmail(String nombre, String usuario, String contrasena) {
        return """
            Estimado/a %s,
            
            ¡Bienvenido/a al Sistema de Gestión Académica!
            
            Sus credenciales de acceso son:
            
            Usuario: %s
            Contraseña: %s
            
            IMPORTANTE: Por seguridad, le recomendamos cambiar su contraseña en el primer inicio de sesión.
            
            Puede acceder al sistema en: [URL del sistema]
            
            Si tiene alguna pregunta o problema, no dude en contactarnos.
            
            Atentamente,
            Equipo de Administración
            Sistema de Gestión Académica
            """.formatted(nombre, usuario, contrasena);
    }
    
    private void enviarEmail(String destinatario, String asunto, String cuerpo) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        
        if (usarSSL) {
            props.put("mail.smtp.ssl.enable", "true");
        }
        
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
        message.setSubject(asunto);
        message.setText(cuerpo);
        
        Transport.send(message);
    }
    
    /**
     * Versión de prueba que simula el envío sin SMTP real
     */
    public void enviarCredencialesModoDebug(String destinatario, Object credenciales, String nombreUsuario) {
        String usuario = ((com.servicios.Credenciales) credenciales).getUsuario();
        String contrasena = ((com.servicios.Credenciales) credenciales).getContrasena();
        
        System.out.println("\n========== EMAIL SIMULADO ==========");
        System.out.println("Para: " + destinatario);
        System.out.println("Asunto: Bienvenido al Sistema de Gestión Académica");
        System.out.println("\nCuerpo:");
        System.out.println(construirCuerpoEmail(nombreUsuario, usuario, contrasena));
        System.out.println("====================================\n");
    }
}
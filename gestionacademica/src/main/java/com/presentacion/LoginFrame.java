package com.presentacion;

import javax.swing.*;
import java.awt.*;
import com.servicios.AutenticacionService;
public class LoginFrame extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIniciarSesion;
    private JButton btnRegistrar;
    private JLabel lblError;
    private AutenticacionService autenticacionService;

    public LoginFrame(AutenticacionService autenticacionService) {
        this.autenticacionService = autenticacionService;
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Sistema de Gestión Académica - Inicio de Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Panel principal con dos columnas
        JPanel panelPrincipal = new JPanel(new GridLayout(1, 2));

        // Panel izquierdo (formulario)
        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setBackground(new Color(255, 243, 227));
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Título
        JLabel lblTitulo = new JLabel("¿Ya tienes una cuenta?");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(58, 46, 46));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSubtitulo = new JLabel("¡Bienvenido de vuelta!");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 18));
        lblSubtitulo.setForeground(new Color(58, 46, 46));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelIzquierdo.add(lblTitulo);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(lblSubtitulo);
        panelIzquierdo.add(Box.createVerticalStrut(40));

        // Campo Usuario
        JLabel lblUsuario = new JLabel("Usuario");
        lblUsuario.setForeground(new Color(138, 127, 127));
        lblUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        txtUsuario = new JTextField(20);
        txtUsuario.setMaximumSize(new Dimension(300, 30));
        txtUsuario.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(58, 46, 46)));
        txtUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelIzquierdo.add(lblUsuario);
        panelIzquierdo.add(txtUsuario);
        panelIzquierdo.add(Box.createVerticalStrut(20));

        // Campo Contraseña
        JLabel lblContrasena = new JLabel("Contraseña");
        lblContrasena.setForeground(new Color(138, 127, 127));
        lblContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        txtContrasena = new JPasswordField(20);
        txtContrasena.setMaximumSize(new Dimension(300, 30));
        txtContrasena.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(58, 46, 46)));
        txtContrasena.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelIzquierdo.add(lblContrasena);
        panelIzquierdo.add(txtContrasena);
        panelIzquierdo.add(Box.createVerticalStrut(30));

        // Botón Iniciar Sesión
        btnIniciarSesion = new JButton("Iniciar sesión");
        btnIniciarSesion.setBackground(new Color(255, 209, 154));
        btnIniciarSesion.setForeground(new Color(58, 46, 46));
        btnIniciarSesion.setFont(new Font("Arial", Font.BOLD, 16));
        btnIniciarSesion.setFocusPainted(false);
        btnIniciarSesion.setMaximumSize(new Dimension(300, 40));
        btnIniciarSesion.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnIniciarSesion.addActionListener(e -> iniciarSesion());

        panelIzquierdo.add(btnIniciarSesion);
        panelIzquierdo.add(Box.createVerticalStrut(15));

        // Label de error
        lblError = new JLabel("");
        lblError.setForeground(Color.RED);
        lblError.setFont(new Font("Arial", Font.PLAIN, 12));
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblError.setVisible(false);

        panelIzquierdo.add(lblError);
        panelIzquierdo.add(Box.createVerticalStrut(30));

        // Sección de registro
        JLabel lblPregunta = new JLabel("¿Te interesa registrar a tus hijos en nuestra institución?");
        lblPregunta.setForeground(new Color(58, 46, 46));
        lblPregunta.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnRegistrar = new JButton("REGISTRARSE");
        btnRegistrar.setBackground(new Color(255, 209, 154));
        btnRegistrar.setForeground(new Color(58, 46, 46));
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setMaximumSize(new Dimension(300, 40));
        btnRegistrar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrar.addActionListener(e -> registrarse());

        JLabel lblFinal = new JLabel("<html>No esperes más <span style='color: red;'>¡Únete ahora!</span></html>");
        lblFinal.setForeground(new Color(58, 46, 46));
        lblFinal.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelIzquierdo.add(lblPregunta);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(btnRegistrar);
        panelIzquierdo.add(Box.createVerticalStrut(10));
        panelIzquierdo.add(lblFinal);

        // Panel derecho (imagen)
        JPanel panelDerecho = new JPanel();
        panelDerecho.setBackground(new Color(255, 220, 180));
        JLabel lblImagen = new JLabel("Imagen: Profesora leyendo con niños", SwingConstants.CENTER);
        lblImagen.setFont(new Font("Arial", Font.ITALIC, 16));
        lblImagen.setForeground(new Color(100, 100, 100));
        panelDerecho.add(lblImagen);

        panelPrincipal.add(panelIzquierdo);
        panelPrincipal.add(panelDerecho);

        add(panelPrincipal);

        // Enter en los campos para iniciar sesión
        txtUsuario.addActionListener(e -> txtContrasena.requestFocus());
        txtContrasena.addActionListener(e -> iniciarSesion());
    }

    private void iniciarSesion() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());

        AutenticacionService.ResultadoAutenticacion resultado = 
            autenticacionService.iniciarSesion(usuario, contrasena);

        if (resultado.isExitoso()) {
            lblError.setVisible(false);
            mostrarMensajeExito(resultado.getMensaje());
            // Aquí se abriría la ventana principal según el rol
        } else {
            mostrarError(resultado.getMensaje());
            
            // Verificar si se alcanzó el límite de intentos
            if (autenticacionService.getIntentosFallidos() >= 3) {
                btnIniciarSesion.setEnabled(false);
                btnIniciarSesion.setBackground(new Color(207, 207, 207));
                btnIniciarSesion.setForeground(new Color(122, 122, 122));
            }
        }
    }

    private void mostrarError(String mensaje) {
        if (mensaje.contains("base de datos")) {
            mostrarDialogoError("ERROR", mensaje);
        } else {
            lblError.setText(mensaje);
            lblError.setVisible(true);
        }
    }

    private void mostrarDialogoError(String titulo, String mensaje) {
        JDialog dialogo = new JDialog(this, titulo, true);
        dialogo.setLayout(new BorderLayout(10, 10));
        dialogo.setSize(400, 200);
        dialogo.setLocationRelativeTo(this);

        JPanel panelContenido = new JPanel(new BorderLayout(10, 10));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Icono de error
        JLabel lblIcono = new JLabel("⊗", SwingConstants.CENTER);
        lblIcono.setFont(new Font("Arial", Font.BOLD, 60));
        lblIcono.setForeground(new Color(255, 77, 77));

        // Mensaje
        JLabel lblMensaje = new JLabel("<html><center>" + mensaje + "</center></html>", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 14));

        // Botón Aceptar
        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.setBackground(new Color(255, 209, 154));
        btnAceptar.setForeground(new Color(58, 46, 46));
        btnAceptar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAceptar.addActionListener(e -> dialogo.dispose());

        panelContenido.add(lblIcono, BorderLayout.NORTH);
        panelContenido.add(lblMensaje, BorderLayout.CENTER);
        panelContenido.add(btnAceptar, BorderLayout.SOUTH);

        dialogo.add(panelContenido);
        dialogo.setVisible(true);
    }

    private void mostrarMensajeExito(String mensaje) {
        JDialog dialogo = new JDialog(this, "Éxito", true);
        dialogo.setLayout(new BorderLayout(10, 10));
        dialogo.setSize(400, 200);
        dialogo.setLocationRelativeTo(this);

        JPanel panelContenido = new JPanel(new BorderLayout(10, 10));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Icono de éxito
        JLabel lblIcono = new JLabel("✓", SwingConstants.CENTER);
        lblIcono.setFont(new Font("Arial", Font.BOLD, 60));
        lblIcono.setForeground(new Color(76, 175, 80));

        // Mensaje
        JLabel lblMensaje = new JLabel("<html><center>" + mensaje + "</center></html>", SwingConstants.CENTER);
        lblMensaje.setFont(new Font("Arial", Font.PLAIN, 14));

        // Botón Aceptar
        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.setBackground(new Color(255, 209, 154));
        btnAceptar.setForeground(new Color(58, 46, 46));
        btnAceptar.setFont(new Font("Arial", Font.BOLD, 14));
        btnAceptar.addActionListener(e -> {
            dialogo.dispose();
            // Aquí se abriría la ventana principal según el rol
        });

        panelContenido.add(lblIcono, BorderLayout.NORTH);
        panelContenido.add(lblMensaje, BorderLayout.CENTER);
        panelContenido.add(btnAceptar, BorderLayout.SOUTH);

        dialogo.add(panelContenido);
        dialogo.setVisible(true);
    }

    private void registrarse() {
        JOptionPane.showMessageDialog(this, 
            "Funcionalidad de registro en desarrollo", 
            "Registro", 
            JOptionPane.INFORMATION_MESSAGE);
    }
}
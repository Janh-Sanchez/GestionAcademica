package com.presentacion;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;

import javax.swing.*;

import com.aplicacion.JPAUtil;
import com.dominio.*;
import com.servicios.AutenticacionService;
import com.servicios.PreinscripcionService;

import jakarta.persistence.EntityManager;

public class LoginFrame extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIniciarSesion;
    private JLabel lblError;
    private AutenticacionService autenticacionService;
    
    private final Color CB=new Color(255,212,160), CBH=new Color(255,230,180), CT=new Color(58,46,46), CF=new Color(255,243,227);

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
        add(new JPanel(new GridLayout(1, 2)) {{
            add(crearPanelIzquierdo());
            add(crearPanelDerecho());
        }});
    }

    private JPanel crearPanelIzquierdo() {
        JPanel p = new JPanel();
        p.setBackground(CF);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        p.add(crearLabel("¿Ya tienes una cuenta?", Font.BOLD, 24));
        p.add(Box.createVerticalStrut(10));
        p.add(crearLabel("¡Bienvenido de vuelta!", Font.PLAIN, 18));
        p.add(Box.createVerticalStrut(40));
        p.add(crearCampoConPlaceholder("Usuario", false));
        p.add(Box.createVerticalStrut(20));
        p.add(crearCampoConPlaceholder("Contraseña", true));
        p.add(Box.createVerticalStrut(30));
        
        btnIniciarSesion = crearBoton("Iniciar sesión", e -> iniciarSesion());
        p.add(btnIniciarSesion);
        p.add(Box.createVerticalStrut(15));

        lblError = new JLabel("");
        lblError.setForeground(Color.RED);
        lblError.setFont(new Font("Arial", Font.PLAIN, 12));
        lblError.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblError.setVisible(false);
        p.add(lblError);
        p.add(Box.createVerticalStrut(30));

        JPanel pr = new JPanel() {{
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(CF);
            setAlignmentX(Component.CENTER_ALIGNMENT);
        }};

        JLabel lp = new JLabel("<html><center>¿Te interesa registrar a tus hijos<br>en nuestra institución?</center></html>", SwingConstants.CENTER);
        lp.setFont(new Font("Arial", Font.BOLD, 16));
        lp.setForeground(CT);
        lp.setAlignmentX(Component.CENTER_ALIGNMENT);

        pr.add(lp);
        pr.add(Box.createVerticalStrut(15));
        pr.add(crearBoton("REGISTRARSE", e -> registrarse()));
        pr.add(Box.createVerticalStrut(15));

        JLabel lf = new JLabel("<html><center>No esperes más <span style='color: red;'>¡Únete ahora!</span></center></html>", SwingConstants.CENTER);
        lf.setFont(new Font("Arial", Font.BOLD, 16));
        lf.setAlignmentX(Component.CENTER_ALIGNMENT);
        pr.add(lf);
        p.add(pr);
        return p;
    }

    private JPanel crearPanelDerecho() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(new Color(255, 220, 180));
        java.net.URL url = getClass().getResource("/imagenes/imagenLogin.jpg");
        JLabel l = url != null ? new JLabel(new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(450, 600, Image.SCALE_SMOOTH))) 
                  : new JLabel("Bienvenido al Sistema Académico", SwingConstants.CENTER) {{
                        setFont(new Font("Arial", Font.BOLD, 18));
                        setForeground(new Color(100, 100, 100));
                    }};
        l.setHorizontalAlignment(SwingConstants.CENTER);
        p.add(l, BorderLayout.CENTER);
        return p;
    }

    private JPanel crearCampoConPlaceholder(String placeholder, boolean esPassword) {
        JPanel pc = new JPanel(new BorderLayout());
        pc.setBackground(CF);
        pc.setMaximumSize(new Dimension(300, 40));

        JComponent c = esPassword ? new JPasswordField() : new JTextField();
        ((JTextField)c).setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0,0,2,0,CT),
            BorderFactory.createEmptyBorder(5,10,5,10)
        ));
        c.setBackground(new Color(255,255,255,200));
        c.setFont(new Font("Arial", Font.PLAIN, 14));
        c.setForeground(CT);

        JLabel lp = new JLabel(placeholder);
        lp.setForeground(new Color(138,127,127,180));
        lp.setFont(new Font("Arial", Font.ITALIC, 14));
        lp.setBorder(BorderFactory.createEmptyBorder(0,12,0,0));
        
        JLayeredPane lyr = new JLayeredPane();
        lyr.setPreferredSize(new Dimension(300,40));
        lyr.setMaximumSize(new Dimension(300,40));
        c.setBounds(0,0,300,40);
        lp.setBounds(0,0,300,40);
        lyr.add(c, JLayeredPane.DEFAULT_LAYER);
        lyr.add(lp, JLayeredPane.PALETTE_LAYER);
        pc.add(lyr, BorderLayout.CENTER);

        if(esPassword) {
            JPasswordField pwd=(JPasswordField)c;
            pwd.addCaretListener(e->lp.setVisible(pwd.getPassword().length==0));
            txtContrasena=pwd;
            pwd.addActionListener(e->iniciarSesion());
        }else{
            JTextField txt=(JTextField)c;
            txt.addCaretListener(e->lp.setVisible(txt.getText().isEmpty()));
            txtUsuario=txt;
            txt.addActionListener(e->txtContrasena.requestFocus());
        }
        return pc;
    }

    private JButton crearBoton(String t, java.awt.event.ActionListener a) {
        JButton b = new JButton(t);
        b.setBackground(CB);
        b.setForeground(CT);
        b.setFont(new Font("Arial", Font.BOLD, 16));
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setMaximumSize(new Dimension(300,40));
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.addActionListener(a);
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e){b.setBackground(CBH);}
            public void mouseExited(MouseEvent e){b.setBackground(CB);}
        });
        return b;
    }

    private JLabel crearLabel(String t, int s, int z) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Arial", s, z));
        l.setForeground(CT);
        l.setAlignmentX(Component.CENTER_ALIGNMENT);
        return l;
    }

    private void iniciarSesion() {
        String usuario = txtUsuario.getText().trim();
        String contrasena = new String(txtContrasena.getPassword());
        
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            mostrarError("Por favor, llene todos los campos");
            return;
        }

        try {
            Optional<Usuario> usuarioBDOpt = autenticacionService.iniciarSesion(usuario, contrasena);
            
            if (usuarioBDOpt.isPresent()) {
                // Login exitoso
                lblError.setVisible(false);
                Usuario usuarioBD = usuarioBDOpt.get();
                this.dispose();
                navegarPorRol(usuarioBD);
            } else {
                // Credenciales incorrectas
                int intentosRestantes = autenticacionService.getIntentosRestantes();
                String mensaje = "Usuario o contraseña incorrectos";
                
                if (intentosRestantes > 0) {
                    mensaje += ". Intentos restantes: " + intentosRestantes;
                }
                
                mostrarError(mensaje);
                verificarBloqueo();
            }
            
        } catch (IllegalStateException e) {
            // Límite de intentos alcanzado
            mostrarError(e.getMessage());
            btnIniciarSesion.setEnabled(false);
            btnIniciarSesion.setBackground(new Color(207, 207, 207));
            
        } catch (RuntimeException e) {
            mostrarError("Error del sistema. Por favor, intente más tarde");
        }
    }

    private void verificarBloqueo() {
        if (autenticacionService.getIntentosFallidos() >= 3) {
            btnIniciarSesion.setEnabled(false);
            btnIniciarSesion.setBackground(new Color(207, 207, 207));
            mostrarError("Cuenta bloqueada temporalmente por seguridad");
        }
    }

    private void navegarPorRol(Usuario usuario) {        
        SwingUtilities.invokeLater(() -> {
            JFrame ventanaDestino = null;
            
            if (usuario instanceof Administrador administrador){
                ventanaDestino = new AdministradorFrame(administrador);
                
            } else if (usuario instanceof Directivo directivo){
                ventanaDestino = new DirectivoFrame(directivo);
                
            } else if (usuario instanceof Acudiente acudiente){
                ventanaDestino = new AcudienteFrame(acudiente);
                
            } else if (usuario instanceof Profesor profesor){
                ventanaDestino = new ProfesorFrame(profesor);
            }
                
            if (ventanaDestino != null) {
                ventanaDestino.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(null, 
                    "Tipo de usuario no soportado: " + usuario.getClass().getSimpleName(),
                    "Error de navegación", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void mostrarError(String m) {
        if(m.contains("campos") || m.contains("vacío")) {
            lblError.setText(m);
            lblError.setVisible(true);
        } else {
            mostrarDialogoGrande("ERROR", m, "⊗", new Color(255,77,77), "/imagenes/icono_error.jpg");
        }
    }

    private void mostrarDialogoGrande(String t, String m, String i, Color c, String rutaImg) {
        JDialog d = new JDialog(this, t, true);
        d.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        d.setUndecorated(true);
        d.setLayout(new BorderLayout(10,10));
        d.setSize(500,400);
        d.setLocationRelativeTo(this);

        JPanel pc = new JPanel(new BorderLayout(10,10));
        pc.setBackground(Color.WHITE);
        pc.setBorder(BorderFactory.createEmptyBorder(30,30,30,30));

        JPanel pImg = new JPanel();
        pImg.setBackground(Color.WHITE);
        boolean tieneImg = false;
        if(rutaImg != null) {
            java.net.URL url = getClass().getResource(rutaImg);
            if(url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image img = icon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                JLabel lImg = new JLabel(new ImageIcon(img));
                pImg.add(lImg);
                tieneImg = true;
            }
        }

        JPanel pCentral = new JPanel();
        pCentral.setLayout(new BoxLayout(pCentral, BoxLayout.Y_AXIS));
        pCentral.setBackground(Color.WHITE);

        JLabel li = new JLabel(i, SwingConstants.CENTER);
        li.setFont(new Font("Arial", Font.BOLD, 100));
        li.setForeground(c);
        li.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lt = new JLabel(t);
        lt.setFont(new Font("Arial", Font.BOLD, 24));
        lt.setForeground(c);
        lt.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lm = new JLabel("<html><center>"+m+"</center></html>");
        lm.setFont(new Font("Arial", Font.BOLD, 16));
        lm.setForeground(CT);
        lm.setAlignmentX(Component.CENTER_ALIGNMENT);

        pCentral.add(pImg);
        if(tieneImg) pCentral.add(Box.createVerticalStrut(15));
        
        if(!tieneImg) {
            pCentral.add(li);
            pCentral.add(Box.createVerticalStrut(10));
        }
        
        pCentral.add(lt);
        pCentral.add(Box.createVerticalStrut(20));
        pCentral.add(lm);
        pCentral.add(Box.createVerticalGlue());

        JPanel pBoton = new JPanel();
        pBoton.setBackground(Color.WHITE);
        pBoton.add(crearBoton("Aceptar", e->d.dispose()));

        pc.add(pCentral, BorderLayout.CENTER);
        pc.add(pBoton, BorderLayout.SOUTH);

        d.add(pc);
        d.setVisible(true);
    }

    private void registrarse() {
        // Cerrar la ventana de login
        this.dispose();
        
        // Crear y mostrar el formulario de preinscripción
        SwingUtilities.invokeLater(() -> {
            try {
                // 1. Obtener EntityManager usando tu fábrica JPAUtil
                EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
                
                // 2. Instanciar el servicio de preinscripción pasando el EntityManager
                PreinscripcionService preinscripcionService = new PreinscripcionService(entityManager);
                
                // 3. Crear y mostrar el frame de preinscripción
                PreinscripcionFrame preinscripcionFrame = new PreinscripcionFrame(preinscripcionService);
                
                // 4. Mostrar el formulario de preinscripción en una ventana contenedora
                crearVentanaPreinscripcion(preinscripcionFrame, entityManager);
                
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, 
                    "Error al abrir el formulario de preinscripción: " + e.getMessage(),
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                
                // Volver a mostrar el login si hay error
                new LoginFrame(autenticacionService).setVisible(true);
            }
        });
    }

    private void crearVentanaPreinscripcion(PreinscripcionFrame preinscripcionFrame, EntityManager entityManager) {
        JFrame frameContenedor = new JFrame("Formulario de Preinscripción");
        frameContenedor.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameContenedor.setSize(900, 600);
        frameContenedor.setLocationRelativeTo(null);
        
        // Agregar WindowListener para cerrar el EntityManager cuando se cierre la ventana
        frameContenedor.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                if (entityManager != null && entityManager.isOpen()) {
                    entityManager.close();
                }
                // Opcional: Volver al login
                new LoginFrame(autenticacionService).setVisible(true);
            }
            
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                // Preguntar si realmente quiere salir
                int respuesta = JOptionPane.showConfirmDialog(
                    frameContenedor,
                    "¿Está seguro que desea salir? Los datos no guardados se perderán.",
                    "Confirmar salida",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
                
                if (respuesta == JOptionPane.YES_OPTION) {
                    frameContenedor.dispose();
                }
            }
        });
        
        frameContenedor.add(crearPanelPreinscripcion(preinscripcionFrame));
        frameContenedor.setVisible(true);
    }

    private JPanel crearPanelPreinscripcion(PreinscripcionFrame preinscripcionFrame) {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Botón para abrir el formulario
        JButton btnAbrirFormulario = new JButton("Comenzar Preinscripción");
        btnAbrirFormulario.addActionListener(e -> {
            preinscripcionFrame.mostrarFormularioPreinscripcion();
        });
        
        // Panel de bienvenida
        JPanel panelBienvenida = new JPanel(new BorderLayout());
        panelBienvenida.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JLabel lblTitulo = new JLabel("<html><h1>Preinscripción para Nuevos Estudiantes</h1></html>", 
                                    SwingConstants.CENTER);
        JLabel lblInstrucciones = new JLabel(
            "<html><center><p>Haz clic en el botón para comenzar el proceso de preinscripción</p>" +
            "<p>Podrás registrar hasta 4 estudiantes por acudiente</p>" +
            "<p><b>Requisitos:</b></p>" +
            "<ul style='text-align: left;'>" +
            "<li>Acudiente mayor de 18 años</li>" +
            "<li>Estudiantes entre 3 y 18 años</li>" +
            "<li>Documentos de identificación válidos</li>" +
            "</ul></center></html>",
            SwingConstants.CENTER
        );
        
        panelBienvenida.add(lblTitulo, BorderLayout.NORTH);
        panelBienvenida.add(lblInstrucciones, BorderLayout.CENTER);
        panelBienvenida.add(btnAbrirFormulario, BorderLayout.SOUTH);
        
        panel.add(panelBienvenida, BorderLayout.CENTER);
        
        // Botón para volver al login
        JButton btnVolver = new JButton("Volver al Login");
        btnVolver.addActionListener(e -> {
            ((JFrame) SwingUtilities.getWindowAncestor(panel)).dispose();
            new LoginFrame(autenticacionService).setVisible(true);
        });
        
        JPanel panelInferior = new JPanel();
        panelInferior.add(btnVolver);
        panel.add(panelInferior, BorderLayout.SOUTH);
        
        return panel;
    }
}
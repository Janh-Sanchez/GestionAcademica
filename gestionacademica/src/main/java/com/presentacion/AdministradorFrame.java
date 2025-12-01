package com.presentacion;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import com.dominio.Administrador;
import com.servicios.GestionUsuariosService;

public class AdministradorFrame extends JFrame {
    private Administrador administrador;
    private GestionUsuariosService gestionService;
    
    private final Color CB = new Color(255, 212, 160);
    private final Color CBH = new Color(255, 230, 180);
    private final Color CT = new Color(58, 46, 46);
    private final Color CF = new Color(255, 243, 227);

    public AdministradorFrame(Administrador administrador, GestionUsuariosService gestionService) {
        this.administrador = administrador;
        this.gestionService = gestionService;
        inicializarComponentes();
    }

    public AdministradorFrame(Administrador administrador) {
        this(administrador, new GestionUsuariosService());
    }

    private void inicializarComponentes() {
        setTitle("Panel de Administrador - Sistema de Gestión Académica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(CF);

        // Panel superior con bienvenida e icono de perfil
        panelPrincipal.add(crearPanelSuperior(), BorderLayout.NORTH);

        // Panel central con imagen y botón
        panelPrincipal.add(crearPanelCentral(), BorderLayout.CENTER);

        add(panelPrincipal);
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CF);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        // Icono de perfil a la izquierda (CU 2.4)
        JLabel lblIconoPerfil = new JLabel("👤");
        lblIconoPerfil.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        lblIconoPerfil.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblIconoPerfil.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                consultarMiInformacion();
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                lblIconoPerfil.setForeground(CB);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                lblIconoPerfil.setForeground(Color.BLACK);
            }
        });
        panel.add(lblIconoPerfil, BorderLayout.WEST);

        // Mensaje de bienvenida en el centro
        JLabel lblBienvenida = new JLabel("¡Bienvenido de nuevo " + 
            administrador.obtenerNombreCompleto().toUpperCase() + "!");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 20));
        lblBienvenida.setForeground(CT);
        lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblBienvenida, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CF);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 50, 50));

        // Cargar imagen
        java.net.URL url = getClass().getResource("/imagenes/imagenLogin.jpg");
        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(400, 250, Image.SCALE_SMOOTH);
            JLabel lblImagen = new JLabel(new ImageIcon(img));
            lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(lblImagen);
        }

        panel.add(Box.createVerticalStrut(20));

        // Frase motivacional
        JLabel lblFrase = new JLabel("<html><center>Cada decisión que tomamos construye el camino para que nuestros<br>niños aprendan, sueñen y prosperen</center></html>");
        lblFrase.setFont(new Font("Arial", Font.PLAIN, 14));
        lblFrase.setForeground(CT);
        lblFrase.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblFrase);

        panel.add(Box.createVerticalStrut(30));

        // Botón principal (CU 2.1)
        JButton btnAdministrar = crearBoton("ADMINISTRAR USUARIOS", e -> administrarUsuarios());
        panel.add(btnAdministrar);

        return panel;
    }

    private JButton crearBoton(String texto, java.awt.event.ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.setBackground(CB);
        boton.setForeground(CT);
        boton.setFont(new Font("Arial", Font.BOLD, 16));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setMaximumSize(new Dimension(300, 45));
        boton.setAlignmentX(Component.CENTER_ALIGNMENT);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.addActionListener(accion);
        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(CBH);
            }
            public void mouseExited(MouseEvent e) {
                boton.setBackground(CB);
            }
        });
        return boton;
    }

    /**
     * CU 2.1 - Administrar usuarios
     * Abre el módulo de gestión de usuarios
     */
    private void administrarUsuarios() {
        this.setVisible(false);
        AdministrarUsuariosFrame frameAdministrar = new AdministrarUsuariosFrame(gestionService, this);
        frameAdministrar.setVisible(true);
    }

    /**
     * CU 2.4 - Consultar mi información
     * Muestra el diálogo con la información del usuario autenticado
     */
    private void consultarMiInformacion() {
        ConsultarInformacionDialog dialogo = new ConsultarInformacionDialog(
            this, administrador, gestionService);
        dialogo.setVisible(true);
    }
}
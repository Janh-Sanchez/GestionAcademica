package com.presentacion;

import com.servicios.GestionUsuariosService;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Pantalla principal de administración de usuarios (CU 2.1)
 * Punto de entrada al módulo de gestión
 */
public class AdministrarUsuariosFrame extends JFrame {
    
    private final Color CB = new Color(255, 212, 160);
    private final Color CBH = new Color(255, 230, 180);
    private final Color CT = new Color(58, 46, 46);
    private final Color CF = new Color(255, 243, 227);
    
    private GestionUsuariosService gestionService;
    private JFrame ventanaPadre;
    
    public AdministrarUsuariosFrame(GestionUsuariosService service, JFrame padre) {
        this.gestionService = service;
        this.ventanaPadre = padre;
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setTitle("Administrar Usuarios - Sistema de Gestión Académica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(ventanaPadre);
        setResizable(false);
        
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(CF);
        
        // Panel superior con título
        panelPrincipal.add(crearPanelSuperior(), BorderLayout.NORTH);
        
        // Panel central con imagen y opciones
        panelPrincipal.add(crearPanelCentral(), BorderLayout.CENTER);
        
        add(panelPrincipal);
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel();
        panel.setBackground(CF);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));
        
        JLabel lblTitulo = new JLabel("Administrar usuarios");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(CT);
        panel.add(lblTitulo);
        
        return panel;
    }
    
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CF);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 40, 50));
        
        // Imagen (la misma del mockup)
        java.net.URL url = getClass().getResource("/imagenes/imagenLogin.jpg");
        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(300, 180, Image.SCALE_SMOOTH);
            JLabel lblImagen = new JLabel(new ImageIcon(img));
            lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(lblImagen);
        }
        
        panel.add(Box.createVerticalStrut(30));
        
        // Botones de opciones
        JButton btnCrear = crearBoton("CREAR USUARIO", e -> abrirCrearUsuario());
        panel.add(btnCrear);
        
        panel.add(Box.createVerticalStrut(15));
        
        JButton btnVolver = crearBoton("VOLVER", e -> volver());
        panel.add(btnVolver);
        
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
            public void mouseEntered(MouseEvent e) { boton.setBackground(CBH); }
            public void mouseExited(MouseEvent e) { boton.setBackground(CB); }
        });
        return boton;
    }
    
    private void abrirCrearUsuario() {
        this.setVisible(false);
        CrearUsuarioFrame frameCrear = new CrearUsuarioFrame(gestionService, this);
        frameCrear.setVisible(true);
    }
    
    private void volver() {
        ventanaPadre.setVisible(true);
        this.dispose();
    }
}
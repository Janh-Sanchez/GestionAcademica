package com.presentacion;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import com.dominio.Administrador;

public class AdministradorFrame extends JFrame {
    private Administrador administrador;
    private final Color CB = new Color(255, 212, 160);
    private final Color CBH = new Color(255, 230, 180);
    private final Color CT = new Color(58, 46, 46);
    private final Color CF = new Color(255, 243, 227);

    public AdministradorFrame(Administrador administrador) {
        this.administrador = administrador;
        inicializarComponentes();
    }

    public AdministradorFrame() {
        inicializarComponentes();
    }

    private void inicializarComponentes() {
        setTitle("Panel de Administrador - Sistema de Gestión Académica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(CF);

        // Panel superior con bienvenida
        panelPrincipal.add(crearPanelSuperior(), BorderLayout.NORTH);

        // Panel central con imagen y botón
        panelPrincipal.add(crearPanelCentral(), BorderLayout.CENTER);

        add(panelPrincipal);
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel();
        panel.setBackground(CF);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JLabel lblBienvenida = new JLabel("¡Bienvenido de nuevo " + administrador.obtenerNombreCompleto().toUpperCase() + "!");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 20));
        lblBienvenida.setForeground(CT);
        panel.add(lblBienvenida);

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

        // Botón principal
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

    private void administrarUsuarios() {
        JOptionPane.showMessageDialog(this,
            "Funcionalidad de administración de usuarios en desarrollo",
            "En desarrollo",
            JOptionPane.INFORMATION_MESSAGE);
    }
}
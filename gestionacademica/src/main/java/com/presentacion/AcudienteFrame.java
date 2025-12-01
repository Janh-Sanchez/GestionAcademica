package com.presentacion;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import com.dominio.Acudiente;
import com.dominio.Estudiante;
import com.servicios.GestionUsuariosService;

public class AcudienteFrame extends JFrame {
    private Acudiente acudiente;
    private GestionUsuariosService gestionService;
    private JComboBox<String> comboEstudiantes;
    
    private final Color CB = new Color(255, 212, 160);
    private final Color CBH = new Color(255, 230, 180);
    private final Color CT = new Color(58, 46, 46);
    private final Color CF = new Color(255, 243, 227);

    public AcudienteFrame(Acudiente acudiente, GestionUsuariosService gestionService) {
        this.acudiente = acudiente;
        this.gestionService = gestionService;
        inicializarComponentes();
    }

    public AcudienteFrame(Acudiente acudiente) {
        this(acudiente, new GestionUsuariosService());
    }

    private void inicializarComponentes() {
        setTitle("Panel de Acudiente - Sistema de Gestión Académica");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(CF);

        panelPrincipal.add(crearPanelSuperior(), BorderLayout.NORTH);
        panelPrincipal.add(crearPanelCentral(), BorderLayout.CENTER);

        add(panelPrincipal);
    }

    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CF);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        // Icono de perfil (CU 2.4)
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

        String nombreCompleto = acudiente != null ? acudiente.obtenerNombreCompleto().toUpperCase() : "USUARIO";
        
        JLabel lblBienvenida = new JLabel("¡Bienvenida de nuevo " + nombreCompleto + "!");
        lblBienvenida.setFont(new Font("Arial", Font.BOLD, 18));
        lblBienvenida.setForeground(CT);
        lblBienvenida.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblBienvenida, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CF);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 30, 50));

        java.net.URL url = getClass().getResource("/imagenes/familia.jpg");
        if (url == null) {
            url = getClass().getResource("/imagenes/imagenLogin.jpg");
        }
        if (url != null) {
            ImageIcon icon = new ImageIcon(url);
            Image img = icon.getImage().getScaledInstance(400, 220, Image.SCALE_SMOOTH);
            JLabel lblImagen = new JLabel(new ImageIcon(img));
            lblImagen.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(lblImagen);
        }

        panel.add(Box.createVerticalStrut(15));

        JLabel lblFrase = new JLabel("<html><center>Tu presencia, amor y guía son el puente que impulsa su camino para<br>aprender y soñar</center></html>");
        lblFrase.setFont(new Font("Arial", Font.PLAIN, 13));
        lblFrase.setForeground(CT);
        lblFrase.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblFrase);

        panel.add(Box.createVerticalStrut(20));

        panel.add(crearSelectorEstudiante());

        panel.add(Box.createVerticalStrut(20));

        JPanel panelBotones = new JPanel(new GridLayout(2, 2, 15, 15));
        panelBotones.setBackground(CF);
        panelBotones.setMaximumSize(new Dimension(550, 150));

        panelBotones.add(crearBotonConIcono("ADMINISTRAR\nHOJA DE VIDA", "📄", e -> administrarHojaVida()));
        panelBotones.add(crearBotonConIcono("CONSULTAR\nOBSERVADOR", "📝", e -> consultarObservador()));
        panelBotones.add(crearBotonConIcono("ADMINISTRAR\nLOGROS", "🏅", e -> administrarLogros()));

        panel.add(panelBotones);

        return panel;
    }

    private JPanel crearSelectorEstudiante() {
        JPanel panel = new JPanel();
        panel.setBackground(CF);
        panel.setMaximumSize(new Dimension(350, 80));

        JPanel panelInterno = new JPanel();
        panelInterno.setLayout(new BoxLayout(panelInterno, BoxLayout.Y_AXIS));
        panelInterno.setBackground(CF);

        DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>();
        
        if (acudiente != null && acudiente.getEstudiantes() != null && !acudiente.getEstudiantes().isEmpty()) {
            for (Estudiante estudiante : acudiente.getEstudiantes()) {
                String nombreCompleto = estudiante.getPrimerNombre() + " " + estudiante.getPrimerApellido();
                if (estudiante.getSegundoNombre() != null && !estudiante.getSegundoNombre().isEmpty()) {
                    nombreCompleto = estudiante.getPrimerNombre() + " " + estudiante.getSegundoNombre() + " " + estudiante.getPrimerApellido();
                }
                if (estudiante.getSegundoApellido() != null && !estudiante.getSegundoApellido().isEmpty()) {
                    nombreCompleto += " " + estudiante.getSegundoApellido();
                }
                modelo.addElement(nombreCompleto);
            }
        } else {
            modelo.addElement("Sin estudiantes registrados");
        }

        comboEstudiantes = new JComboBox<>(modelo);
        comboEstudiantes.setFont(new Font("Arial", Font.PLAIN, 14));
        comboEstudiantes.setBackground(Color.WHITE);
        comboEstudiantes.setForeground(CT);
        comboEstudiantes.setMaximumSize(new Dimension(300, 35));
        comboEstudiantes.setAlignmentX(Component.CENTER_ALIGNMENT);

        comboEstudiantes.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(CB, 2),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        panelInterno.add(comboEstudiantes);

        panel.add(panelInterno);
        return panel;
    }

    private JButton crearBotonConIcono(String texto, String icono, java.awt.event.ActionListener accion) {
        JButton boton = new JButton();
        boton.setLayout(new BorderLayout(10, 5));
        boton.setBackground(CB);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);

        JLabel lblIcono = new JLabel(icono, SwingConstants.CENTER);
        lblIcono.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));

        JLabel lblTexto = new JLabel("<html><center>" + texto.replace("\n", "<br>") + "</center></html>", SwingConstants.CENTER);
        lblTexto.setFont(new Font("Arial", Font.BOLD, 12));
        lblTexto.setForeground(CT);

        boton.add(lblIcono, BorderLayout.CENTER);
        boton.add(lblTexto, BorderLayout.SOUTH);

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

    private void consultarMiInformacion() {
        ConsultarInformacionDialog dialogo = new ConsultarInformacionDialog(
            this, acudiente, gestionService);
        dialogo.setVisible(true);
    }

    private void administrarHojaVida() {
        String estudianteSeleccionado = (String) comboEstudiantes.getSelectedItem();
        mostrarMensajeDesarrollo("Administrar Hoja de Vida para " + estudianteSeleccionado);
    }

    private void consultarObservador() {
        String estudianteSeleccionado = (String) comboEstudiantes.getSelectedItem();
        mostrarMensajeDesarrollo("Consultar Observador para " + estudianteSeleccionado);
    }

    private void administrarLogros() {
        String estudianteSeleccionado = (String) comboEstudiantes.getSelectedItem();
        mostrarMensajeDesarrollo("Administrar Logros para " + estudianteSeleccionado);
    }

    private void mostrarMensajeDesarrollo(String funcionalidad) {
        JOptionPane.showMessageDialog(this,
            "Funcionalidad de " + funcionalidad + " en desarrollo",
            "En desarrollo",
            JOptionPane.INFORMATION_MESSAGE);
    }
}
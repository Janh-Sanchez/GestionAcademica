package com.presentacion;

import com.dominio.Usuario;
import com.servicios.GestionUsuariosService;
import com.servicios.ResultadoOperacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Diálogo para consultar información de usuario (CU 2.4)
 * Se muestra al hacer clic en el icono de perfil
 */
public class ConsultarInformacionDialog extends JDialog {
    
    private final Color CB = new Color(255, 212, 160);
    private final Color CBH = new Color(255, 230, 180);
    private final Color CT = new Color(58, 46, 46);
    private final Color CF = new Color(255, 243, 227);
    
    private Usuario usuarioAutenticado;
    private GestionUsuariosService gestionService;
    private JPanel panelInformacion;
    
    public ConsultarInformacionDialog(JFrame padre, Usuario usuario, GestionUsuariosService service) {
        super(padre, "Mi Información", true);
        this.usuarioAutenticado = usuario;
        this.gestionService = service;
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(getParent());
        setResizable(false);
        setUndecorated(false);
        
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);
        
        // Panel superior con título
        panelPrincipal.add(crearPanelSuperior(), BorderLayout.NORTH);
        
        // Panel central con información (vacío por ahora)
        panelInformacion = crearPanelInformacion();
        panelPrincipal.add(panelInformacion, BorderLayout.CENTER);
        
        // Panel inferior con botón
        panelPrincipal.add(crearPanelInferior(), BorderLayout.SOUTH);
        
        add(panelPrincipal);
        
        // Cargar datos
        cargarInformacion();
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel();
        panel.setBackground(CB);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Mi Información");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(CT);
        panel.add(lblTitulo);
        
        return panel;
    }
    
    private JPanel crearPanelInformacion() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Agregar un mensaje de carga
        JLabel lblCargando = new JLabel("Cargando información...", SwingConstants.CENTER);
        lblCargando.setFont(new Font("Arial", Font.ITALIC, 14));
        lblCargando.setForeground(new Color(150, 150, 150));
        lblCargando.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblCargando);
        
        return panel;
    }
    
    private JPanel crearCampoInfo(String label, String valor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Arial", Font.BOLD, 14));
        lblLabel.setForeground(new Color(100, 100, 100));
        lblLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblLabel);
        
        panel.add(Box.createVerticalStrut(5));
        
        JLabel lblValor = new JLabel(valor != null ? valor : "No disponible");
        lblValor.setFont(new Font("Arial", Font.PLAIN, 16));
        lblValor.setForeground(CT);
        lblValor.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblValor);
        
        // Línea separadora
        JSeparator separador = new JSeparator();
        separador.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        separador.setForeground(new Color(230, 230, 230));
        panel.add(Box.createVerticalStrut(5));
        panel.add(separador);
        
        return panel;
    }
    
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 40, 30, 40));
        
        JButton btnVolver = crearBoton("Volver", e -> dispose());
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
        boton.setPreferredSize(new Dimension(200, 45));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.addActionListener(accion);
        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { boton.setBackground(CBH); }
            public void mouseExited(MouseEvent e) { boton.setBackground(CB); }
        });
        return boton;
    }
    
    private void cargarInformacion() {
        // Paso 2-4 del diagrama ACT_CU2.4
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        ResultadoOperacion resultado = gestionService.consultarMiInformacion(usuarioAutenticado);
        
        setCursor(Cursor.getDefaultCursor());
        
        if (!resultado.isExitoso()) {
            // Paso 6b: Error al acceder a BD
            JOptionPane.showMessageDialog(this,
                resultado.getMensaje(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            dispose();
        } else {
            // Paso 5: Mostrar información del usuario
            Usuario usuarioCompleto = (Usuario) resultado.getDatos();
            mostrarInformacionUsuario(usuarioCompleto);
        }
    }
    
    private void mostrarInformacionUsuario(Usuario usuario) {
        // Limpiar el panel de información
        panelInformacion.removeAll();
        
        // Icono de usuario
        JLabel lblIconoUsuario = new JLabel("👤", SwingConstants.CENTER);
        lblIconoUsuario.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        lblIconoUsuario.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelInformacion.add(lblIconoUsuario);
        
        panelInformacion.add(Box.createVerticalStrut(20));
        
        // Información del usuario
        panelInformacion.add(crearCampoInfo("Nombre completo:", usuario.obtenerNombreCompleto()));
        panelInformacion.add(Box.createVerticalStrut(15));
        
        panelInformacion.add(crearCampoInfo("Edad:", usuario.getEdad() != null ? usuario.getEdad() + " años" : null));
        panelInformacion.add(Box.createVerticalStrut(15));
        
        panelInformacion.add(crearCampoInfo("Correo electrónico:", usuario.getCorreoElectronico()));
        panelInformacion.add(Box.createVerticalStrut(15));
        
        panelInformacion.add(crearCampoInfo("Teléfono:", usuario.getTelefono()));
        panelInformacion.add(Box.createVerticalStrut(15));
        
        if (usuario.getTokenAccess() != null && usuario.getTokenAccess().getRol() != null) {
            panelInformacion.add(crearCampoInfo("Rol:", usuario.getTokenAccess().getRol().getNombre()));
            panelInformacion.add(Box.createVerticalStrut(15));
        }
        
        // Agregar información específica según el tipo de usuario
        if (usuario instanceof com.dominio.Profesor profesor) {
            if (profesor.getGrupo() != null) {
                panelInformacion.add(crearCampoInfo("Grupo asignado:", profesor.getGrupo().getNombreGrupo()));
                panelInformacion.add(Box.createVerticalStrut(15));
            }
        } else if (usuario instanceof com.dominio.Acudiente acudiente) {
            panelInformacion.add(crearCampoInfo("Estado de aprobación:", 
                acudiente.getEstadoAprobacion() != null ? acudiente.getEstadoAprobacion().toString() : null));
            panelInformacion.add(Box.createVerticalStrut(15));
        }
        
        // Actualizar la interfaz
        panelInformacion.revalidate();
        panelInformacion.repaint();
    }
}
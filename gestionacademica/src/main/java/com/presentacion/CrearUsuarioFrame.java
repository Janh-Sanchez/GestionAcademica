package com.presentacion;

import com.dominio.Directivo;
import com.dominio.Profesor;
import com.dominio.Rol;
import com.dominio.Usuario;
import com.servicios.GestionUsuariosService;
import com.servicios.ResultadoOperacion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.Pattern;

/**
 * Formulario de creación de usuarios (CU 2.3)
 * Implementa el diagrama de actividad ACT_CU2.3 con todas sus validaciones
 */
public class CrearUsuarioFrame extends JFrame {
    
    private final Color CB = new Color(255, 212, 160);
    private final Color CBH = new Color(255, 230, 180);
    private final Color CT = new Color(58, 46, 46);
    private final Color CF = new Color(255, 243, 227);
    private final Color ERROR_COLOR = new Color(255, 77, 77);
    
    private GestionUsuariosService gestionService;
    private JFrame ventanaPadre;
    
    // Componentes del formulario
    private JComboBox<String> comboRol;
    private JTextField txtPrimerNombre;
    private JTextField txtSegundoNombre;
    private JTextField txtPrimerApellido;
    private JTextField txtSegundoApellido;
    private JTextField txtEdad;
    private JTextField txtCorreo;
    private JTextField txtTelefono;
    
    // Labels de error
    private JLabel lblErrorPrimerNombre;
    private JLabel lblErrorPrimerApellido;
    private JLabel lblErrorEdad;
    private JLabel lblErrorCorreo;
    private JLabel lblErrorTelefono;
    
    private boolean datosModificados = false;
    
    public CrearUsuarioFrame(GestionUsuariosService service, JFrame padre) {
        this.gestionService = service;
        this.ventanaPadre = padre;
        inicializarComponentes();
    }
    
    private void inicializarComponentes() {
        setTitle("Crear Usuario - Sistema de Gestión Académica");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(700, 750);
        setLocationRelativeTo(ventanaPadre);
        setResizable(false);
        
        // Listener para confirmar cierre
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmarSalida();
            }
        });
        
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(CF);
        
        // Panel superior con flecha de retorno
        panelPrincipal.add(crearPanelSuperior(), BorderLayout.NORTH);
        
        // Panel central con formulario
        JScrollPane scrollPane = new JScrollPane(crearPanelFormulario());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
        
        add(panelPrincipal);
    }
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(CF);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        
        // Botón de retorno (flecha)
        JButton btnVolver = new JButton("←");
        btnVolver.setFont(new Font("Arial", Font.BOLD, 24));
        btnVolver.setForeground(CB);
        btnVolver.setBackground(CF);
        btnVolver.setBorderPainted(false);
        btnVolver.setFocusPainted(false);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVolver.addActionListener(e -> confirmarSalida());
        
        panel.add(btnVolver);
        
        return panel;
    }
    
    private JPanel crearPanelFormulario() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CF);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 30, 50));
        
        // Selector de rol
        panel.add(crearCampoRol());
        panel.add(Box.createVerticalStrut(20));
        
        // Campos obligatorios
        panel.add(crearCampoTexto("Primer Nombre (*)", txtPrimerNombre = new JTextField(), 
                                 lblErrorPrimerNombre = new JLabel(), true));
        panel.add(Box.createVerticalStrut(15));
        
        panel.add(crearCampoTexto("Segundo Nombre", txtSegundoNombre = new JTextField(), 
                                 null, false));
        panel.add(Box.createVerticalStrut(15));
        
        panel.add(crearCampoTexto("Primer Apellido (*)", txtPrimerApellido = new JTextField(), 
                                 lblErrorPrimerApellido = new JLabel(), true));
        panel.add(Box.createVerticalStrut(15));
        
        panel.add(crearCampoTexto("Segundo Apellido", txtSegundoApellido = new JTextField(), 
                                 null, false));
        panel.add(Box.createVerticalStrut(15));
        
        panel.add(crearCampoTexto("Edad (*)", txtEdad = new JTextField(), 
                                 lblErrorEdad = new JLabel(), true));
        panel.add(Box.createVerticalStrut(15));

        panel.add(crearCampoTexto("Correo electrónico (*)", txtCorreo = new JTextField(), 
                                 lblErrorCorreo = new JLabel(), true));
        panel.add(Box.createVerticalStrut(15));
        
        panel.add(crearCampoTexto("Teléfono de contacto (*)", txtTelefono = new JTextField(), 
                                 lblErrorTelefono = new JLabel(), true));
        panel.add(Box.createVerticalStrut(20));
        
        // Nota de campos obligatorios
        JLabel lblNota = new JLabel("(*) Campos Obligatorios");
        lblNota.setFont(new Font("Arial", Font.BOLD, 12));
        lblNota.setForeground(ERROR_COLOR);
        lblNota.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblNota);
        
        panel.add(Box.createVerticalStrut(30));
        
        // Botón Guardar
        JButton btnGuardar = crearBoton("Guardar", e -> intentarGuardar());
        panel.add(btnGuardar);
        
        return panel;
    }
    
    private JPanel crearCampoRol() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CF);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Selector de rol con flecha personalizada
        String[] roles = {"PROFESOR", "DIRECTIVO"};
        comboRol = new JComboBox<>(roles);
        comboRol.setFont(new Font("Arial", Font.PLAIN, 16));
        comboRol.setBackground(Color.WHITE);
        comboRol.setForeground(CT);
        comboRol.setMaximumSize(new Dimension(300, 40));
        comboRol.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Personalizar renderizado
        comboRol.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                                                         int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                return this;
            }
        });
        
        comboRol.addActionListener(e -> datosModificados = true);
        
        panel.add(comboRol);
        
        return panel;
    }
    
    private JPanel crearCampoTexto(String label, JTextField campo, JLabel lblError, boolean obligatorio) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CF);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Label
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 14));
        lbl.setForeground(CT);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        
        panel.add(Box.createVerticalStrut(5));
        
        // Campo de texto
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        // Listener para marcar como modificado y validar en tiempo real
        campo.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                datosModificados = true;
                if (lblError != null) {
                    validarCampo(campo, lblError, label);
                }
            }
        });
        
        campo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(campo);
        
        // Label de error
        if (lblError != null) {
            panel.add(Box.createVerticalStrut(3));
            lblError.setFont(new Font("Arial", Font.ITALIC, 12));
            lblError.setForeground(ERROR_COLOR);
            lblError.setAlignmentX(Component.LEFT_ALIGNMENT);
            lblError.setVisible(false);
            panel.add(lblError);
        }
        
        return panel;
    }
    
    private void validarCampo(JTextField campo, JLabel lblError, String nombreCampo) {
        String valor = campo.getText().trim();
        
        if (nombreCampo.contains("Correo")) {
            if (!valor.isEmpty() && !validarEmail(valor)) {
                mostrarError(lblError, "Formato inválido");
                return;
            }
        } else if (nombreCampo.contains("Edad")) {
            if (!valor.isEmpty() && !validarNumero(valor)) {
                mostrarError(lblError, "Formato inválido");
                return;
            }
        } else if (nombreCampo.contains("Teléfono")) {
            if (!valor.isEmpty() && !validarTelefono(valor)) {
                mostrarError(lblError, "Formato inválido");
                return;
            }
        }
        
        ocultarError(lblError);
    }
    
    private void intentarGuardar() {
        // Paso 4 del diagrama: Validación inicial de formato
        if (!validarFormulario()) {
            return;
        }
        
        // Paso 12: Preparar objeto usuario
        Usuario usuario = recopilarDatos();
        
        // Obtener solo el nombre del rol seleccionado
        String nombreRolSeleccionado = (String) comboRol.getSelectedItem();
        
        // Mostrar indicador de carga
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        
        // Paso 13-14: Llamar al servicio con el nombre del rol
        ResultadoOperacion resultado = gestionService.crearUsuario(
            usuario, 
            nombreRolSeleccionado  // ✅ Solo string, no objeto Rol
        );
        
        setCursor(Cursor.getDefaultCursor());
        
        // Decisión D5: ¿Guardado exitoso?
        if (resultado.isExitoso()) {
            // Paso 16: Mostrar mensaje de éxito
            mostrarMensajeExito();
        } else {
            // Paso 19: Mostrar error de BD
            mostrarMensajeError(resultado.getMensaje());
        }
    }
    
    private boolean validarFormulario() {
        boolean valido = true;
        
        // Validar campos obligatorios
        if (txtPrimerNombre.getText().trim().isEmpty()) {
            mostrarError(lblErrorPrimerNombre, "Campo obligatorio");
            valido = false;
        }
        
        if (txtPrimerApellido.getText().trim().isEmpty()) {
            mostrarError(lblErrorPrimerApellido, "Campo obligatorio");
            valido = false;
        }
        
        if (txtEdad.getText().trim().isEmpty()) {
            mostrarError(lblErrorEdad, "Campo obligatorio");
            valido = false;
        } else if (!validarNumero(txtEdad.getText().trim())) {
            mostrarError(lblErrorEdad, "Formato inválido");
            valido = false;
        }
        
        if (txtCorreo.getText().trim().isEmpty()) {
            mostrarError(lblErrorCorreo, "Campo obligatorio");
            valido = false;
        } else if (!validarEmail(txtCorreo.getText().trim())) {
            mostrarError(lblErrorCorreo, "Formato inválido");
            valido = false;
        }
        
        if (txtTelefono.getText().trim().isEmpty()) {
            mostrarError(lblErrorTelefono, "Campo obligatorio");
            valido = false;
        } else if (!validarTelefono(txtTelefono.getText().trim())) {
            mostrarError(lblErrorTelefono, "Formato inválido");
            valido = false;
        }
        
        return valido;
    }
    
    private Usuario recopilarDatos() {
        // Recopilar datos comunes (sin ID, JPA lo generará al persistir)
        String primerNombre = txtPrimerNombre.getText().trim();
        String segundoNombre = txtSegundoNombre.getText().trim().isEmpty() ? null : txtSegundoNombre.getText().trim();
        String primerApellido = txtPrimerApellido.getText().trim();
        String segundoApellido = txtSegundoApellido.getText().trim().isEmpty() ? null : txtSegundoApellido.getText().trim();
        int edad = Integer.parseInt(txtEdad.getText().trim());
        String correo = txtCorreo.getText().trim();
        String telefono = txtTelefono.getText().trim();
        
        // Crear usuario según el rol seleccionado
        String rolSeleccionado = (String) comboRol.getSelectedItem();
        Usuario usuario;
        
        if ("PROFESOR".equals(rolSeleccionado)) {
            // Crear Profesor (sin ID ni TokenUsuario, el servicio los generará)
            usuario = new Profesor(
                null,  // ID será generado por JPA
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                edad,
                correo,
                telefono,
                null,   // TokenUsuario será generado por el servicio
                null
            );
    
        } else if ("DIRECTIVO".equals(rolSeleccionado)) {
            // Crear Directivo
            usuario = new Directivo(
                null,  // ID será generado por JPA
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                edad,
                correo,
                telefono,
                null   // TokenUsuario será generado por el servicio
            );
        } else {
            // Fallback: crear Usuario base (no debería ocurrir si el combo está bien configurado)
            usuario = new Usuario(
                null,
                primerNombre,
                segundoNombre,
                primerApellido,
                segundoApellido,
                edad,
                correo,
                telefono,
                null
            );
        }
        
        return usuario;
    }
    
    private void confirmarSalida() {
        // Paso 11: Mostrar diálogo de confirmación si hay datos sin guardar
        if (datosModificados) {
            mostrarDialogoConfirmacionSalida();
        } else {
            cerrarVentana();
        }
    }
    
    private void mostrarDialogoConfirmacionSalida() {
        JDialog dialogo = new JDialog(this, "¡Espera!", true);
        dialogo.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialogo.setUndecorated(true);
        dialogo.setSize(450, 300);
        dialogo.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 243, 200));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 193, 7), 3),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        // Icono de advertencia
        JLabel lblIcono = new JLabel("⚠", SwingConstants.CENTER);
        lblIcono.setFont(new Font("Arial", Font.BOLD, 60));
        lblIcono.setForeground(new Color(255, 193, 7));
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblIcono);
        
        panel.add(Box.createVerticalStrut(15));
        
        JLabel lblMensaje = new JLabel("<html><center>La información no se<br>guardará si sales ahora</center></html>");
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 18));
        lblMensaje.setForeground(CT);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblMensaje);
        
        panel.add(Box.createVerticalStrut(25));
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        panelBotones.setBackground(new Color(255, 243, 200));
        
        JButton btnSeguir = crearBotonDialogo("Seguir diligenciando formulario", e -> dialogo.dispose());
        JButton btnSalir = crearBotonDialogo("Salir", e -> {
            dialogo.dispose();
            cerrarVentana();
        });
        
        panelBotones.add(btnSeguir);
        panelBotones.add(btnSalir);
        panel.add(panelBotones);
        
        dialogo.add(panel);
        dialogo.setVisible(true);
    }
    
    private void mostrarMensajeExito() {
        JDialog dialogo = new JDialog(this, "Usuario creado correctamente", true);
        dialogo.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialogo.setUndecorated(true);
        dialogo.setSize(450, 300);
        dialogo.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(76, 175, 80), 3),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        JLabel lblIcono = new JLabel("✓", SwingConstants.CENTER);
        lblIcono.setFont(new Font("Arial", Font.BOLD, 80));
        lblIcono.setForeground(new Color(76, 175, 80));
        lblIcono.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblIcono);
        
        panel.add(Box.createVerticalStrut(15));
        
        JLabel lblMensaje = new JLabel("Usuario creado correctamente");
        lblMensaje.setFont(new Font("Arial", Font.BOLD, 18));
        lblMensaje.setForeground(CT);
        lblMensaje.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblMensaje);
        
        panel.add(Box.createVerticalStrut(25));
        
        JButton btnAceptar = crearBoton("Aceptar", e -> {
            dialogo.dispose();
            cerrarVentana();
        });
        panel.add(btnAceptar);
        
        dialogo.add(panel);
        dialogo.setVisible(true);
    }
    
    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    private void cerrarVentana() {
        ventanaPadre.setVisible(true);
        this.dispose();
    }
    
    private JButton crearBoton(String texto, ActionListener accion) {
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
    
    private JButton crearBotonDialogo(String texto, ActionListener accion) {
        JButton boton = new JButton(texto);
        boton.setBackground(CB);
        boton.setForeground(CT);
        boton.setFont(new Font("Arial", Font.BOLD, 13));
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setPreferredSize(new Dimension(180, 40));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.addActionListener(accion);
        return boton;
    }
    
    private void mostrarError(JLabel lblError, String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
    }
    
    private void ocultarError(JLabel lblError) {
        lblError.setVisible(false);
    }
    
    // Validaciones
    private boolean validarEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return Pattern.matches(regex, email);
    }
    
    private boolean validarNumero(String numero) {
        try {
            int num = Integer.parseInt(numero);
            return num > 0 && num < 150;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private boolean validarTelefono(String telefono) {
        return telefono.matches("\\d{7,15}");
    }
}
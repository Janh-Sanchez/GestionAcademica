package com.presentacion;

import com.dominio.*;
import com.servicios.PreinscripcionService;
import com.servicios.PreinscripcionService.ResultadoValidacion;
import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * Controlador de la interfaz gráfica para el proceso de preinscripción
 * Integra los mockups mostrados en las imágenes
 */
public class PreinscripcionFrame {
    
    private final PreinscripcionService preinscripcionService;
    private final Map<String, String> datosAcudiente;
    private final java.util.List<Map<String, String>> listaEstudiantes;
    private int contadorEstudiantes;
    
    // Colores para mensajes
    private static final Color COLOR_ERROR = new Color(220, 53, 69);
    private static final Color COLOR_EXITO = new Color(40, 167, 69);
    private static final Color COLOR_ADVERTENCIA = new Color(255, 193, 7);
    
    // Colores para campos
    private static final Color COLOR_CAMPO_NORMAL = Color.BLACK;
    private static final Color COLOR_CAMPO_ERROR = Color.RED;
    private static final Color BORDER_CAMPO_NORMAL = new Color(204, 204, 204);
    private static final Color BORDER_CAMPO_ERROR = new Color(220, 53, 69);
    
    // Referencias a los componentes actuales
    private JPanel panelFormularioActual;
    private Map<String, JTextField> mapaCamposActual;
    private Map<String, JLabel> mapaEtiquetasActual;
    private Map<String, JLabel> mapaErroresActual;
    
    public PreinscripcionFrame(PreinscripcionService preinscripcionService) {
        this.preinscripcionService = preinscripcionService;
        this.datosAcudiente = new HashMap<>();
        this.listaEstudiantes = new ArrayList<>();
        this.contadorEstudiantes = 0;
        this.mapaCamposActual = new HashMap<>();
        this.mapaEtiquetasActual = new HashMap<>();
        this.mapaErroresActual = new HashMap<>();
    }
    
    /**
     * Muestra el formulario inicial de preinscripción
     */
    public void mostrarFormularioPreinscripcion() {
        // Solo limpiar si es la primera vez o si reiniciamos
        if (contadorEstudiantes == 0) {
            limpiarDatos();
        }
        
        panelFormularioActual = crearPanelFormularioAcudiente();
        
        // Usar un JDialog personalizado para mantener los datos
        JDialog dialog = new JDialog((Frame) null, "Formulario de registro", true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setSize(600, 700);
        dialog.setLocationRelativeTo(null);
        
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.add(panelFormularioActual, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> {
            int respuesta = JOptionPane.showConfirmDialog(
                dialog,
                "¿Está seguro que desea cancelar? Se perderán todos los datos.",
                "Confirmar cancelación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            if (respuesta == JOptionPane.YES_OPTION) {
                limpiarDatos();
                dialog.dispose();
            }
        });
        
        JButton btnContinuar = new JButton("Continuar");
        btnContinuar.addActionListener(e -> {
            if (procesarDatosAcudiente(dialog)) {
                dialog.dispose();
                mostrarOpcionesPostFormulario();
            }
        });
        
        panelBotones.add(btnCancelar);
        panelBotones.add(btnContinuar);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        dialog.add(panelPrincipal);
        dialog.setVisible(true);
    }
    
    /**
     * Crea el panel del formulario de acudiente
     */
    private JPanel crearPanelFormularioAcudiente() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        mapaCamposActual.clear();
        mapaEtiquetasActual.clear();
        mapaErroresActual.clear();
        
        // Mensaje de bienvenida
        JLabel lblBienvenida = new JLabel(
            "<html><b>¡Gracias por estar interesado en nuestra institución!</b><br>" +
            "Por favor diligencia tus datos personales correctamente.</html>"
        );
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblBienvenida, gbc);
        
        gbc.gridwidth = 1;
        int fila = 1;
        
        // Campos del acudiente
        fila = agregarCampoFormulario(panel, gbc, fila++, "Primer Nombre", "primerNombre", true);
        fila = agregarCampoFormulario(panel, gbc, fila++, "Segundo Nombre", "segundoNombre", false);
        fila = agregarCampoFormulario(panel, gbc, fila++, "Primer Apellido", "primerApellido", true);
        fila = agregarCampoFormulario(panel, gbc, fila++, "Segundo Apellido", "segundoApellido", false);
        fila = agregarCampoFormulario(panel, gbc, fila++, "Edad", "edad", true);
        fila = agregarCampoFormulario(panel, gbc, fila++, "Correo electrónico", "correoElectronico", true);
        fila = agregarCampoFormulario(panel, gbc, fila++, "Teléfono de contacto", "telefono", true);
        
        // Separador
        gbc.gridx = 0;
        gbc.gridy = fila++;
        gbc.gridwidth = 2;
        panel.add(new JSeparator(), gbc);
        
        // Sección de estudiantes
        JLabel lblEstudiante = new JLabel(
            "<html><b>Por favor, diligencia los datos del estudiante</b> que deseas preinscribir.<br>" +
            "Si vas a registrar más de un estudiante, selecciona <b>Siguiente</b> para abrir un nuevo formulario.<br>" +
            "Cuando termines, haz clic en <b>Enviar</b></html>"
        );
        gbc.gridy = fila++;
        panel.add(lblEstudiante, gbc);
        
        gbc.gridwidth = 1;
        
        // Campos del primer estudiante
        fila = agregarCampoFormulario(panel, gbc, fila++, "Primer nombre", "est_primerNombre", true);
        fila = agregarCampoFormulario(panel, gbc, fila++, "Segundo Nombre", "est_segundoNombre", false);
        fila = agregarCampoFormulario(panel, gbc, fila++, "Primer Apellido", "est_primerApellido", true);
        fila = agregarCampoFormulario(panel, gbc, fila++, "Segundo Apellido", "est_segundoApellido", false);
        fila = agregarCampoFormulario(panel, gbc, fila++, "Edad", "est_edad", true);
        fila = agregarCampoFormulario(panel, gbc, fila++, "NUIP", "est_nuip", true);
        
        // Combo de grados
        JLabel lblGrado = new JLabel("Grado al que aspira (*)");
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(lblGrado, gbc);
        
        String[] grados = {"Párvulos", "Caminadores", "Pre-Jardín"};
        JComboBox<String> cmbGrado = new JComboBox<>(grados);
        cmbGrado.setName("est_gradoAspira");
        mapaCamposActual.put("est_gradoAspira", null); // Placeholder para combo
        gbc.gridx = 1;
        panel.add(cmbGrado, gbc);
        
        fila++;
        
        // Nota de campos obligatorios
        gbc.gridx = 0;
        gbc.gridy = fila;
        gbc.gridwidth = 2;
        JLabel lblObligatorios = new JLabel("<html><font color='red'>(*) Campos Obligatorios</font></html>");
        panel.add(lblObligatorios, gbc);
        
        return panel;
    }
    
    /**
     * Agrega un campo de texto al formulario con manejo de errores visual
     */
    private int agregarCampoFormulario(JPanel panel, GridBagConstraints gbc, 
                                      int fila, String label, String nombre, 
                                      boolean obligatorio) {
        JLabel lbl = new JLabel(label + (obligatorio ? " (*)" : ""));
        gbc.gridx = 0;
        gbc.gridy = fila;
        lbl.setForeground(COLOR_CAMPO_NORMAL);
        mapaEtiquetasActual.put(nombre, lbl);
        panel.add(lbl, gbc);
        
        JTextField txt = new JTextField(20);
        txt.setName(nombre);
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_CAMPO_NORMAL, 1),
            BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));
        
        // Restaurar datos si ya existían
        if (nombre.startsWith("est_") && contadorEstudiantes == 1 && listaEstudiantes.size() == 1) {
            Map<String, String> datosEstudiante = listaEstudiantes.get(0);
            String campo = nombre.substring(4); // quitar "est_"
            if (datosEstudiante.containsKey(campo)) {
                txt.setText(datosEstudiante.get(campo));
            }
        } else if (!nombre.startsWith("est_") && !datosAcudiente.isEmpty()) {
            String campo = nombre;
            if (datosAcudiente.containsKey(campo)) {
                txt.setText(datosAcudiente.get(campo));
            }
        }
        
        gbc.gridx = 1;
        panel.add(txt, gbc);
        mapaCamposActual.put(nombre, txt);
        
        // Crear label de error
        JLabel lblError = new JLabel("");
        lblError.setForeground(COLOR_ERROR);
        lblError.setFont(new Font("Arial", Font.PLAIN, 10));
        lblError.setVisible(false);
        gbc.gridx = 0;
        gbc.gridy = fila + 1;
        gbc.gridwidth = 2;
        panel.add(lblError, gbc);
        mapaErroresActual.put(nombre, lblError);
        
        return fila + 2;
    }
    
    /**
     * Procesa los datos del acudiente
     * Retorna true si los datos son válidos
     */
    private boolean procesarDatosAcudiente(JDialog dialog) {
        // Limpiar errores anteriores
        limpiarErrores();
        
        // Extraer datos del acudiente
        for (Map.Entry<String, JTextField> entry : mapaCamposActual.entrySet()) {
            String nombre = entry.getKey();
            JTextField txt = entry.getValue();
            
            if (txt != null && !nombre.startsWith("est_")) {
                datosAcudiente.put(nombre, txt.getText().trim());
            }
        }
        
        // Validar datos del acudiente
        ResultadoValidacion validacion = preinscripcionService.validarDatosAcudiente(
            datosAcudiente.get("primerNombre"),
            datosAcudiente.get("segundoNombre"),
            datosAcudiente.get("primerApellido"),
            datosAcudiente.get("segundoApellido"),
            parseIntSafe(datosAcudiente.get("edad")),
            datosAcudiente.get("correoElectronico"),
            datosAcudiente.get("telefono")
        );
        
        if (!validacion.isValido()) {
            // Mostrar error en campo específico
            mostrarErrorEnCampo(validacion.getCampo(), validacion.getMensaje());
            return false;
        }
        
        // Extraer datos del estudiante
        Map<String, String> datosEstudiante = new HashMap<>();
        for (Map.Entry<String, JTextField> entry : mapaCamposActual.entrySet()) {
            String nombre = entry.getKey();
            JTextField txt = entry.getValue();
            
            if (txt != null && nombre.startsWith("est_")) {
                String campo = nombre.substring(4); // quitar "est_"
                datosEstudiante.put(campo, txt.getText().trim());
            }
        }
        
        // Extraer el combo de grado
        for (Component comp : panelFormularioActual.getComponents()) {
            if (comp instanceof JComboBox && comp.getName() != null && comp.getName().equals("est_gradoAspira")) {
                JComboBox<?> cmb = (JComboBox<?>) comp;
                Object selected = cmb.getSelectedItem();
                datosEstudiante.put("gradoAspira", selected != null ? selected.toString() : "");
                break;
            }
        }
        
        // Validar primer estudiante
        validacion = preinscripcionService.validarDatosEstudiante(
            datosEstudiante.get("primerNombre"),
            datosEstudiante.get("segundoNombre"),
            datosEstudiante.get("primerApellido"),
            datosEstudiante.get("segundoApellido"),
            parseIntSafe(datosEstudiante.get("edad")),
            datosEstudiante.get("nuip"),
            datosEstudiante.get("gradoAspira")
        );
        
        if (!validacion.isValido()) {
            mostrarErrorEnCampo("est_" + validacion.getCampo(), validacion.getMensaje());
            return false;
        }
        
        // Si estamos agregando estudiante adicional, no reemplazar el primero
        if (contadorEstudiantes == 0) {
            listaEstudiantes.clear();
        }
        listaEstudiantes.add(datosEstudiante);
        contadorEstudiantes = listaEstudiantes.size();
        
        return true;
    }
    
    /**
     * Muestra error en un campo específico del formulario
     */
    private void mostrarErrorEnCampo(String campoNombre, String mensaje) {
        // Mostrar etiqueta en rojo
        JLabel etiqueta = mapaEtiquetasActual.get(campoNombre);
        if (etiqueta != null) {
            etiqueta.setForeground(COLOR_CAMPO_ERROR);
        }
        
        // Mostrar borde rojo en campo
        JTextField campo = mapaCamposActual.get(campoNombre);
        if (campo != null) {
            campo.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CAMPO_ERROR, 2),
                BorderFactory.createEmptyBorder(4, 4, 4, 4)
            ));
            campo.requestFocusInWindow();
        }
        
        // Mostrar mensaje de error
        JLabel errorLabel = mapaErroresActual.get(campoNombre);
        if (errorLabel != null) {
            errorLabel.setText(mensaje);
            errorLabel.setVisible(true);
        }
    }
    
    /**
     * Limpia todos los errores visuales
     */
    private void limpiarErrores() {
        for (JLabel etiqueta : mapaEtiquetasActual.values()) {
            if (etiqueta != null) {
                etiqueta.setForeground(COLOR_CAMPO_NORMAL);
            }
        }
        
        for (JTextField campo : mapaCamposActual.values()) {
            if (campo != null) {
                campo.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER_CAMPO_NORMAL, 1),
                    BorderFactory.createEmptyBorder(4, 4, 4, 4)
                ));
            }
        }
        
        for (JLabel error : mapaErroresActual.values()) {
            if (error != null) {
                error.setText("");
                error.setVisible(false);
            }
        }
    }
    
    /**
     * Muestra las opciones después de llenar el primer estudiante
     */
    private void mostrarOpcionesPostFormulario() {
        String[] opciones = {
            "Volver",
            "Agregar otro estudiante",
            "Enviar"
        };
        
        // Deshabilitar "Agregar otro" si ya llegó al límite
        if (contadorEstudiantes >= 4) {
            mostrarAdvertencia(
                "Solo puede inscribir máximo 4 estudiantes",
                "Ha alcanzado el límite máximo de estudiantes por acudiente."
            );
            opciones = new String[]{"Volver", "Enviar"};
        }
        
        int seleccion = JOptionPane.showOptionDialog(
            null,
            "¿Qué desea hacer?",
            "Opciones de preinscripción",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[2] // "Enviar" como opción por defecto
        );
        
        switch (seleccion) {
            case 0: // Volver
                mostrarAdvertenciaSalir();
                break;
            case 1: // Agregar otro estudiante
                if (contadorEstudiantes < 4) {
                    mostrarFormularioEstudianteAdicional();
                }
                break;
            case 2: // Enviar
                enviarFormularioPreinscripcion();
                break;
            default:
                mostrarOpcionesPostFormulario(); // Volver a mostrar opciones
        }
    }
    
    /**
     * Muestra advertencia al intentar salir sin guardar
     */
    private void mostrarAdvertenciaSalir() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Icono de advertencia
        JLabel iconLabel = new JLabel("⚠️", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Dialog", Font.PLAIN, 48));
        iconLabel.setForeground(COLOR_ADVERTENCIA);
        panel.add(iconLabel, BorderLayout.NORTH);
        
        // Mensaje
        JLabel mensaje = new JLabel(
            "<html><center><h2>¡Espera!</h2>" +
            "Tu preinscripción no se guardará si sales ahora</center></html>",
            SwingConstants.CENTER
        );
        panel.add(mensaje, BorderLayout.CENTER);
        
        String[] opciones = {"Seguir diligenciando formulario", "Salir"};
        int seleccion = JOptionPane.showOptionDialog(
            null,
            panel,
            "Advertencia",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.WARNING_MESSAGE,
            null,
            opciones,
            opciones[0]
        );
        
        if (seleccion == 0) {
            mostrarOpcionesPostFormulario(); // Continuar
        } else {
            limpiarDatos(); // Salir y limpiar
        }
    }
    
    /**
     * Muestra formulario para agregar estudiante adicional
     */
    private void mostrarFormularioEstudianteAdicional() {
        int numeroEstudiante = contadorEstudiantes + 1;
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lblTitulo = new JLabel(
            "<html><b>Formulario para el " + 
            (numeroEstudiante == 2 ? "segundo" : 
             numeroEstudiante == 3 ? "tercer" : 
             "cuarto") + " estudiante</b></html>"
        );
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);
        
        gbc.gridwidth = 1;
        int fila = 1;
        
        String prefijo = "est" + numeroEstudiante + "_";
        panelFormularioActual = panel;
        mapaCamposActual.clear();
        mapaEtiquetasActual.clear();
        mapaErroresActual.clear();
        
        fila = agregarCampoFormularioAdicional(panel, gbc, fila++, "Primer nombre", prefijo + "primerNombre", true);
        fila = agregarCampoFormularioAdicional(panel, gbc, fila++, "Segundo Nombre", prefijo + "segundoNombre", false);
        fila = agregarCampoFormularioAdicional(panel, gbc, fila++, "Primer Apellido", prefijo + "primerApellido", true);
        fila = agregarCampoFormularioAdicional(panel, gbc, fila++, "Segundo Apellido", prefijo + "segundoApellido", false);
        fila = agregarCampoFormularioAdicional(panel, gbc, fila++, "Edad", prefijo + "edad", true);
        fila = agregarCampoFormularioAdicional(panel, gbc, fila++, "NUIP", prefijo + "nuip", true);
        
        JLabel lblGrado = new JLabel("Grado al que aspira (*)");
        gbc.gridx = 0;
        gbc.gridy = fila;
        panel.add(lblGrado, gbc);
        
        String[] grados = {"Párvulos", "Caminadores", "Pre-Jardín"};
        JComboBox<String> cmbGrado = new JComboBox<>(grados);
        cmbGrado.setName(prefijo + "gradoAspira");
        mapaCamposActual.put(prefijo + "gradoAspira", null);
        gbc.gridx = 1;
        panel.add(cmbGrado, gbc);
        
        JDialog dialog = new JDialog((Frame) null, "Agregar estudiante", true);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.setSize(500, 500);
        dialog.setLocationRelativeTo(null);
        
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.add(panel, BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dialog.dispose());
        
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(e -> {
            if (procesarEstudianteAdicional(dialog, prefijo)) {
                dialog.dispose();
                mostrarOpcionesPostFormulario();
            }
        });
        
        panelBotones.add(btnCancelar);
        panelBotones.add(btnAgregar);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        dialog.add(panelPrincipal);
        dialog.setVisible(true);
    }
    
    /**
     * Agrega un campo de texto al formulario adicional
     */
    private int agregarCampoFormularioAdicional(JPanel panel, GridBagConstraints gbc, 
                                              int fila, String label, String nombre, 
                                              boolean obligatorio) {
        JLabel lbl = new JLabel(label + (obligatorio ? " (*)" : ""));
        gbc.gridx = 0;
        gbc.gridy = fila;
        lbl.setForeground(COLOR_CAMPO_NORMAL);
        mapaEtiquetasActual.put(nombre, lbl);
        panel.add(lbl, gbc);
        
        JTextField txt = new JTextField(20);
        txt.setName(nombre);
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_CAMPO_NORMAL, 1),
            BorderFactory.createEmptyBorder(4, 4, 4, 4)
        ));
        gbc.gridx = 1;
        panel.add(txt, gbc);
        mapaCamposActual.put(nombre, txt);
        
        // Crear label de error
        JLabel lblError = new JLabel("");
        lblError.setForeground(COLOR_ERROR);
        lblError.setFont(new Font("Arial", Font.PLAIN, 10));
        lblError.setVisible(false);
        gbc.gridx = 0;
        gbc.gridy = fila + 1;
        gbc.gridwidth = 2;
        panel.add(lblError, gbc);
        mapaErroresActual.put(nombre, lblError);
        
        return fila + 2;
    }
    
    /**
     * Procesa el estudiante adicional
     */
    private boolean procesarEstudianteAdicional(JDialog dialog, String prefijo) {
        limpiarErrores();
        
        Map<String, String> datosEstudiante = new HashMap<>();
        
        // Extraer datos de campos de texto
        for (Map.Entry<String, JTextField> entry : mapaCamposActual.entrySet()) {
            String nombre = entry.getKey();
            JTextField txt = entry.getValue();
            
            if (txt != null && nombre.startsWith(prefijo)) {
                String campo = nombre.substring(prefijo.length());
                datosEstudiante.put(campo, txt.getText().trim());
            }
        }
        
        // Extraer el combo de grado
        for (Component comp : panelFormularioActual.getComponents()) {
            if (comp instanceof JComboBox && comp.getName() != null && 
                comp.getName().equals(prefijo + "gradoAspira")) {
                JComboBox<?> cmb = (JComboBox<?>) comp;
                Object selected = cmb.getSelectedItem();
                datosEstudiante.put("gradoAspira", selected != null ? selected.toString() : "");
                break;
            }
        }
        
        // Validar estudiante
        ResultadoValidacion validacion = preinscripcionService.validarDatosEstudiante(
            datosEstudiante.get("primerNombre"),
            datosEstudiante.get("segundoNombre"),
            datosEstudiante.get("primerApellido"),
            datosEstudiante.get("segundoApellido"),
            parseIntSafe(datosEstudiante.get("edad")),
            datosEstudiante.get("nuip"),
            datosEstudiante.get("gradoAspira")
        );
        
        if (!validacion.isValido()) {
            mostrarErrorEnCampo(prefijo + validacion.getCampo(), validacion.getMensaje());
            return false;
        }
        
        listaEstudiantes.add(datosEstudiante);
        contadorEstudiantes = listaEstudiantes.size();
        return true;
    }
    
    /**
     * Envía el formulario completo de preinscripción
     */
    private void enviarFormularioPreinscripcion() {
        try {
            // Crear objeto Acudiente
            Acudiente acudiente = new Acudiente();
            acudiente.setPrimerNombre(datosAcudiente.get("primerNombre"));
            acudiente.setSegundoNombre(datosAcudiente.get("segundoNombre"));
            acudiente.setPrimerApellido(datosAcudiente.get("primerApellido"));
            acudiente.setSegundoApellido(datosAcudiente.get("segundoApellido"));
            acudiente.setEdad(parseIntSafe(datosAcudiente.get("edad")));
            acudiente.setCorreoElectronico(datosAcudiente.get("correoElectronico"));
            acudiente.setTelefono(datosAcudiente.get("telefono"));
            
            // Crear conjunto de estudiantes
            Set<Estudiante> estudiantes = new HashSet<>();
            for (Map<String, String> datosEst : listaEstudiantes) {
                Estudiante estudiante = new Estudiante();
                estudiante.setPrimerNombre(datosEst.get("primerNombre"));
                estudiante.setSegundoNombre(datosEst.get("segundoNombre"));
                estudiante.setPrimerApellido(datosEst.get("primerApellido"));
                estudiante.setSegundoApellido(datosEst.get("segundoApellido"));
                estudiante.setEdad(parseIntSafe(datosEst.get("edad")));
                estudiante.setNuip(datosEst.get("nuip"));
                
                String nombreGrado = datosEst.get("gradoAspira");
                Grado grado = new Grado();
                grado.setNombreGrado(nombreGrado);
                estudiante.setGradoAspira(grado);
                
                estudiantes.add(estudiante);
            }
            
            // Registrar preinscripción
            Preinscripcion preinscripcion = preinscripcionService.registrarPreinscripcion(
                acudiente, estudiantes
            );
            
            // Mostrar mensaje de éxito
            mostrarExito(
                "¡Tu formulario fue enviado correctamente!",
                "Por favor espera hasta que la institución se comunique contigo para la entrevista"
            );
            
            limpiarDatos();
            
        } catch (Exception e) {
            e.printStackTrace();
            mostrarErrorBaseDatos();
        }
    }
    
    /**
     * Muestra mensaje de éxito
     */
    private void mostrarExito(String titulo, String mensaje) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        JLabel iconLabel = new JLabel("✓", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Dialog", Font.BOLD, 64));
        iconLabel.setForeground(COLOR_EXITO);
        iconLabel.setOpaque(true);
        iconLabel.setBackground(COLOR_EXITO);
        iconLabel.setForeground(Color.WHITE);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        iconLabel.setPreferredSize(new Dimension(100, 100));
        panel.add(iconLabel, BorderLayout.NORTH);
        
        JLabel lblMensaje = new JLabel(
            "<html><center><h2>" + titulo + "</h2><br>" +
            mensaje + "</center></html>",
            SwingConstants.CENTER
        );
        panel.add(lblMensaje, BorderLayout.CENTER);
        
        JOptionPane.showMessageDialog(
            null,
            panel,
            "Éxito",
            JOptionPane.PLAIN_MESSAGE
        );
    }
    
    /**
     * Muestra error de base de datos
     */
    private void mostrarErrorBaseDatos() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        JLabel iconLabel = new JLabel(
            "<html><center>" +
            "<span style='font-size:48px'>🗄️</span><br>" +
            "<span style='font-size:32px; color:red'>✗</span>" +
            "</center></html>",
            SwingConstants.CENTER
        );
        panel.add(iconLabel, BorderLayout.NORTH);
        
        JLabel lblMensaje = new JLabel(
            "<html><center><h2>ERROR</h2>" +
            "Hubo un error al acceder a la base de datos,<br>inténtelo nuevamente</center></html>",
            SwingConstants.CENTER
        );
        panel.add(lblMensaje, BorderLayout.CENTER);
        
        JOptionPane.showMessageDialog(
            null,
            panel,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    /**
     * Muestra mensaje de advertencia
     */
    private void mostrarAdvertencia(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(
            null,
            mensaje,
            titulo,
            JOptionPane.WARNING_MESSAGE
        );
    }
    
    /**
     * Convierte String a Integer de forma segura
     */
    private Integer parseIntSafe(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Limpia todos los datos temporales
     */
    private void limpiarDatos() {
        datosAcudiente.clear();
        listaEstudiantes.clear();
        contadorEstudiantes = 0;
        mapaCamposActual.clear();
        mapaEtiquetasActual.clear();
        mapaErroresActual.clear();
        panelFormularioActual = null;
    }
}
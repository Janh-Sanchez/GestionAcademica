package com.example;

import jakarta.persistence.EntityManager;
import com.presentacion.LoginFrame;
import com.servicios.AutenticacionService;
import com.persistencia.repositorios.TokenUsuarioRepositorio;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
public class Main {
    public static void main(String[] args) {
        // Configurar Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Iniciar aplicación
        SwingUtilities.invokeLater(() -> {
            EntityManager entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
            TokenUsuarioRepositorio tokenRepositorio = new TokenUsuarioRepositorio(entityManager);
            AutenticacionService autenticacionService = new AutenticacionService(tokenRepositorio);
            
            LoginFrame loginFrame = new LoginFrame(autenticacionService);
            loginFrame.setVisible(true);
        });

        // Agregar shutdown hook para cerrar recursos
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            JPAUtil.shutdown();
        }));
    }
}

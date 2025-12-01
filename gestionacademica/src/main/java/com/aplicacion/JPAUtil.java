package com.aplicacion;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JPAUtil {
    public JPAUtil(){
    }

    private static final String UNIDAD_DE_PERSISTENCIA = "GestionAcademica";
    private static EntityManagerFactory factory;

    public static EntityManagerFactory getEntityManagerFactory(){
        if (factory == null){
            factory = Persistence.createEntityManagerFactory(UNIDAD_DE_PERSISTENCIA);
        }
        return factory;
    }

    // Con esto cerramos la conexión y reestablecemos el Singleton
    public static void shutdown(){
        if(factory != null){
            factory.close();
            factory = null;
        }
    }
}
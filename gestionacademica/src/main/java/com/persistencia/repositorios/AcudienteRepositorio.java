package com.persistencia.repositorios;

import com.dominio.Estado;
import com.persistencia.entidades.AcudienteEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

public class AcudienteRepositorio extends UsuarioRepositorio{
    private final EntityManager entityManager;
    public AcudienteRepositorio(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
    }

    /**
     * Verifica si un acudiente tiene estudiantes aprobados
     */
    public boolean tieneEstudiantesAprobados(AcudienteEntity acudiente) {
        String jpql = "SELECT COUNT(e) FROM estudiante e " +
                     "WHERE e.acudiente.idUsuario = :idAcudiente " +
                     "AND e.estado = :estado";
        
        Long count = entityManager
            .createQuery(jpql, Long.class)
            .setParameter("idAcudiente", acudiente.getIdUsuario())
            .setParameter("estado", Estado.Aprobada)
            .getSingleResult();
        
        return count > 0;
    }
    
    /**
     * Verifica si un acudiente tiene más estudiantes pendientes (excepto el indicado)
     */
    public boolean tieneEstudiantesPendientes(AcudienteEntity acudiente, Integer idEstudianteExcluir) {
        String jpql = "SELECT COUNT(e) FROM estudiante e " +
                     "WHERE e.acudiente.idUsuario = :idAcudiente " +
                     "AND e.estado = :estado " +
                     "AND e.idEstudiante != :idExcluir";
        
        Long count = entityManager
            .createQuery(jpql, Long.class)
            .setParameter("idAcudiente", acudiente.getIdUsuario())
            .setParameter("estado", Estado.Pendiente)
            .setParameter("idExcluir", idEstudianteExcluir)
            .getSingleResult();
        
        return count > 0;
    }

    /**
     * Verifica si un acudiente ya tiene token de usuario asociado
     */
    public boolean tieneTokenUsuario(AcudienteEntity acudiente) {
        if (acudiente == null || acudiente.getTokenAccess() == null) {
            return false;
        }
        
        try {
            String jpql = "SELECT COUNT(t) FROM TokenUsuarioEntity t " +
                        "WHERE t.idToken = :idToken";
            
            Long count = entityManager
                .createQuery(jpql, Long.class)
                .setParameter("idToken", acudiente.getTokenAccess().getIdToken())
                .getSingleResult();
            
            return count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public AcudienteEntity buscarConEstudiantes(Integer idAcudiente) {
        try {
            String jpql = "SELECT a FROM acudiente a LEFT JOIN FETCH a.estudiantes WHERE a.idUsuario = :id";
            return entityManager.createQuery(jpql, AcudienteEntity.class)
                .setParameter("id", idAcudiente)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}

package com.persistencia.repositorios;

import java.util.Optional;

import com.persistencia.entidades.GradoEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class GradoRepositorio extends RepositorioGenerico<GradoEntity>{
    private final EntityManager entityManager;

    public GradoRepositorio(EntityManager entityManager) {
        super(entityManager, GradoEntity.class);
        this.entityManager = entityManager;
    }

    public Optional<GradoEntity> buscarPornombreGrado(String nombreGrado) {
        String jpql = "SELECT t FROM grado t WHERE t.nombreGrado = :nombreGrado";
        TypedQuery<GradoEntity> query = entityManager.createQuery(jpql, GradoEntity.class);
        query.setParameter("nombreGrado", nombreGrado);
        
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}

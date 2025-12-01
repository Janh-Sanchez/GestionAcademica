package com.persistencia.repositorios;

import java.util.Optional;

import com.persistencia.entidades.RolEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class RolRepositorio extends RepositorioGenerico<RolEntity>{
    private final EntityManager entityManager;

    public RolRepositorio(EntityManager entityManager) {
        super(entityManager, RolEntity.class);
        this.entityManager = entityManager;
    }

    public Optional<RolEntity> buscarPorNombreRol(String nombreRol) {
        String jpql = "SELECT t FROM RolEntity t WHERE t.nombre = :nombre";
        TypedQuery<RolEntity> query = entityManager.createQuery(jpql, RolEntity.class);
        query.setParameter("nombre", nombreRol);
        
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}

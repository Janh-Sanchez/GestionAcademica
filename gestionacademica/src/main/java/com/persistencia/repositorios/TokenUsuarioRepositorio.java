package com.persistencia.repositorios;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import com.persistencia.entidades.TokenUsuarioEntity;
import java.util.Optional;

public class TokenUsuarioRepositorio extends RepositorioGenerico<TokenUsuarioEntity> {
    private final EntityManager entityManager;

    public TokenUsuarioRepositorio(EntityManager entityManager) {
        super(entityManager, TokenUsuarioEntity.class);
        this.entityManager = entityManager;
    }

    public Optional<TokenUsuarioEntity> buscarPorNombreUsuario(String nombreUsuario) {
        String jpql = "SELECT t FROM TokenUsuarioEntity t WHERE t.nombreUsuario = :nombreUsuario";
        TypedQuery<TokenUsuarioEntity> query = entityManager.createQuery(jpql, TokenUsuarioEntity.class);
        query.setParameter("nombreUsuario", nombreUsuario);
        
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}

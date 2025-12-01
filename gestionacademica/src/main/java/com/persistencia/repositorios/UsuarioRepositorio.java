package com.persistencia.repositorios;

import java.util.Optional;

import com.persistencia.entidades.UsuarioEntity;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class UsuarioRepositorio extends RepositorioGenerico<UsuarioEntity>{
    private final EntityManager entityManager;

    public UsuarioRepositorio(EntityManager entityManager){
        super(entityManager, UsuarioEntity.class);
        this.entityManager = entityManager;
    }

    public Optional<UsuarioEntity> buscarPorToken(Integer id_token){
        String jpql = "SELECT u FROM usuario u WHERE u.tokenAccess.idToken = :id_token";
        TypedQuery<UsuarioEntity> query = entityManager.createQuery(jpql, UsuarioEntity.class);
        query.setParameter("id_token", id_token);
        
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    } 
}
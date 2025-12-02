package com.persistencia.repositorios;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class RepositorioGenerico<T> {
    private final EntityManager entityManager;
    private final Class<T> entityClass;

    public RepositorioGenerico(EntityManager entityManager, Class<T> entityClass) {
        this.entityManager = entityManager;
        this.entityClass = entityClass;
    }

    public T guardar(T entidad) {
        try {
            entityManager.persist(entidad);
            entityManager.flush(); // Para obtener el ID generado
            return entidad;
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar entidad", e);
        }
    }

    public Optional<T> buscarPorId(Object id) {
        return Optional.ofNullable(entityManager.find(entityClass, id));
    }

    public List<T> buscarTodos() {
        String jpql = "SELECT e FROM " + entityClass.getSimpleName() + " e";
        TypedQuery<T> query = entityManager.createQuery(jpql, entityClass);
        return query.getResultList();
    }

    public T actualizar(T entity) {
        entityManager.getTransaction().begin();
        try {
            T merged = entityManager.merge(entity);
            entityManager.getTransaction().commit();
            return merged;
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }

    public void eliminar(T entity) {
        entityManager.getTransaction().begin();
        try {
            entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            throw e;
        }
    }
}
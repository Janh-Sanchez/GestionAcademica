package com.repositorio;

import java.util.List;

import jakarta.persistence.EntityManager;

public class RepositorioGenerico<T> {
    private EntityManager entityManager;
    private Class<T> tipo;

    public RepositorioGenerico(EntityManager entityManager, Class<T> tipo){
        this.entityManager = entityManager;
        this.tipo = tipo;
    }

    public T consultar(Object id){
        return entityManager.find(tipo, id);
    }

    public T guardar(T entidad){
        return entityManager.merge(entidad);
    }

    public void eliminar(T entidad){
        T managed = entityManager.contains(entidad) ? entidad : entityManager.merge(entidad);
        entityManager.remove(managed);
    }

    public List<T> recuperarTodos(){
        return entityManager.createQuery("SELECT e FROM " + tipo.getSimpleName() + " e", tipo).getResultList();
    }
}

package com.persistencia.repositorios;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio genérico con patrón Repository y Table Data Gateway
 * Responsabilidad: Mapea objetos de dominio a base de datos
 * Implementa operaciones CRUD genéricas usando JPA
 */
public class RepositorioGenerico<T> {
    
    private final Class<T> tipoEntidad;
    private final EntityManager entityManager;
    
    public RepositorioGenerico(EntityManager entityManager, Class<T> tipoEntidad) {
        this.tipoEntidad = tipoEntidad;
        this.entityManager = entityManager;
    }
    
    /**
     * Crea una nueva entidad en la base de datos
     */
    public T crear(T entidad) {
        entityManager.persist(entidad);
        entityManager.flush(); // Asegurar que se persiste inmediatamente
        return entidad;
    }
    
    /**
     * Busca una entidad por su ID
     */
    public T buscarPorId(Integer id) {
        return entityManager.find(tipoEntidad, id);
    }
    
    /**
     * Busca una entidad por su ID y devuelve Optional
     */
    public Optional<T> buscarPorIdOpt(Integer id) {
        T entidad = buscarPorId(id);
        return Optional.ofNullable(entidad);
    }
    
    /**
     * Actualiza una entidad existente
     */
    public T actualizar(T entidad) {
        return entityManager.merge(entidad);
    }
    
    /**
     * Elimina una entidad por su ID
     */
    public void eliminar(Integer id) {
        T entidad = buscarPorId(id);
        if (entidad != null) {
            entityManager.remove(entidad);
        }
    }
    
    /**
     * Elimina una entidad
     */
    public void eliminar(T entidad) {
        entityManager.remove(entityManager.contains(entidad) ? entidad : entityManager.merge(entidad));
    }
    
    /**
     * Lista todas las entidades
     */
    public List<T> listarTodos() {
        String jpql = "SELECT e FROM " + tipoEntidad.getSimpleName() + " e";
        TypedQuery<T> query = entityManager.createQuery(jpql, tipoEntidad);
        return query.getResultList();
    }
    
    /**
     * Lista todas las entidades con paginación
     */
    public List<T> listarTodos(int inicio, int cantidad) {
        String jpql = "SELECT e FROM " + tipoEntidad.getSimpleName() + " e";
        TypedQuery<T> query = entityManager.createQuery(jpql, tipoEntidad);
        query.setFirstResult(inicio);
        query.setMaxResults(cantidad);
        return query.getResultList();
    }
    
    /**
     * Cuenta todas las entidades
     */
    public Integer contar() {
        String jpql = "SELECT COUNT(e) FROM " + tipoEntidad.getSimpleName() + " e";
        TypedQuery<Integer> query = entityManager.createQuery(jpql, Integer.class);
        return query.getSingleResult();
    }
    
    /**
     * Busca entidades por un criterio específico
     */
    public List<T> buscarPorCriterio(String nombreCampo, Object valor) {
        String jpql = "SELECT e FROM " + tipoEntidad.getSimpleName() + " e WHERE e." + nombreCampo + " = :valor";
        TypedQuery<T> query = entityManager.createQuery(jpql, tipoEntidad);
        query.setParameter("valor", valor);
        return query.getResultList();
    }
    
    /**
     * Busca una única entidad por un criterio específico
     */
    public Optional<T> buscarUnoPorCriterio(String nombreCampo, Object valor) {
        List<T> resultados = buscarPorCriterio(nombreCampo, valor);
        return resultados.isEmpty() ? Optional.empty() : Optional.of(resultados.get(0));
    }
    
    /**
     * Verifica si existe una entidad por ID
     */
    public boolean existe(Integer id) {
        return buscarPorId(id) != null;
    }
    
    /**
     * Verifica si existe al menos una entidad que cumpla el criterio
     */
    public boolean existePorCriterio(String nombreCampo, Object valor) {
        String jpql = "SELECT COUNT(e) FROM " + tipoEntidad.getSimpleName() + " e WHERE e." + nombreCampo + " = :valor";
        TypedQuery<Integer> query = entityManager.createQuery(jpql, Integer.class);
        query.setParameter("valor", valor);
        return query.getSingleResult() > 0;
    }
    
    /**
     * Ejecuta un refresh de la entidad desde la BD
     */
    public void refrescar(T entidad) {
        entityManager.refresh(entidad);
    }
    
    /**
     * Desasocia (detach) una entidad del contexto de persistencia
     */
    public void desasociar(T entidad) {
        entityManager.detach(entidad);
    }
    
    /**
     * Limpia el contexto de persistencia
     */
    public void limpiarContexto() {
        entityManager.clear();
    }
    
    /**
     * Hace flush de las operaciones pendientes
     */
    public void flush() {
        entityManager.flush();
    }
    
    /**
     * Obtiene el EntityManager subyacente para operaciones avanzadas
     */
    public EntityManager getEntityManager() {
        return entityManager;
    }
}
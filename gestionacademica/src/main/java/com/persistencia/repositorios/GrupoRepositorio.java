package com.persistencia.repositorios;

import com.persistencia.entidades.GrupoEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

public class GrupoRepositorio extends RepositorioGenerico<GrupoEntity> {
    private final EntityManager entityManager;

    public GrupoRepositorio(EntityManager entityManager) {
        super(entityManager, GrupoEntity.class);
        this.entityManager = entityManager;
    }

    /**
     * Busca un grupo por su nombre
     */
    public Optional<GrupoEntity> buscarPorNombre(String nombreGrupo) {
        String jpql = "SELECT g FROM grupo g WHERE g.nombreGrupo = :nombreGrupo";
        TypedQuery<GrupoEntity> query = entityManager.createQuery(jpql, GrupoEntity.class);
        query.setParameter("nombreGrupo", nombreGrupo);
        
        try {
            return Optional.of(query.getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    /**
     * Busca todos los grupos de un grado específico
     */
    public List<GrupoEntity> buscarPorGrado(Integer idGrado) {
        String jpql = "SELECT g FROM grupo g WHERE g.grado.idGrado = :idGrado";
        return entityManager.createQuery(jpql, GrupoEntity.class)
            .setParameter("idGrado", idGrado)
            .getResultList();
    }

    /**
     * Busca grupos activos de un grado específico
     */
    public List<GrupoEntity> buscarActivosPorGrado(Integer idGrado) {
        String jpql = "SELECT g FROM grupo g " +
                     "WHERE g.grado.idGrado = :idGrado AND g.estado = true";
        return entityManager.createQuery(jpql, GrupoEntity.class)
            .setParameter("idGrado", idGrado)
            .getResultList();
    }

    /**
     * Busca grupos activos de un grado ordenados por cantidad de estudiantes
     */
    public List<GrupoEntity> buscarActivosPorGradoOrdenadosPorEstudiantes(Integer idGrado) {
        String jpql = "SELECT g FROM grupo g " +
                     "WHERE g.grado.idGrado = :idGrado " +
                     "AND g.estado = true " +
                     "ORDER BY SIZE(g.estudiantes) ASC";
        
        return entityManager.createQuery(jpql, GrupoEntity.class)
            .setParameter("idGrado", idGrado)
            .getResultList();
    }

    /**
     * Cuenta la cantidad de grupos existentes para un grado
     */
    public Long contarGruposPorGrado(Integer idGrado) {
        String jpql = "SELECT COUNT(g) FROM grupo g WHERE g.grado.idGrado = :idGrado";
        return entityManager.createQuery(jpql, Long.class)
            .setParameter("idGrado", idGrado)
            .getSingleResult();
    }
}
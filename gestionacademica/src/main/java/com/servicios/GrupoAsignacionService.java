package com.servicios;

import com.dominio.Grupo;
import com.persistencia.entidades.EstudianteEntity;
import com.persistencia.entidades.GradoEntity;
import com.persistencia.entidades.GrupoEntity;
import com.persistencia.mappers.DominioAPersistenciaMapper;
import com.persistencia.repositorios.GradoRepositorio;
import com.persistencia.repositorios.GrupoRepositorio;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class GrupoAsignacionService {
    
    private final GrupoRepositorio grupoRepositorio;
    private final GradoRepositorio gradoRepositorio;
    private final EntityManager entityManager;
    
    public GrupoAsignacionService(GrupoRepositorio grupoRepositorio, 
                                  GradoRepositorio gradoRepositorio,
                                  EntityManager entityManager) {
        this.grupoRepositorio = grupoRepositorio;
        this.gradoRepositorio = gradoRepositorio;
        this.entityManager = entityManager;
    }
    
    /**
     * Asigna un estudiante a un grupo según las reglas de negocio
     */
    @Transactional
    public ResultadoOperacion asignarEstudianteAGrupo(EstudianteEntity estudiante) {
        try {
            if (estudiante.getGradoAspira() == null) {
                return ResultadoOperacion.error("El estudiante no tiene grado asignado");
            }
            
            Integer idGrado = estudiante.getGradoAspira().getIdGrado();
            
            // Obtener grupos ordenados por número de estudiantes usando el repositorio
            List<GrupoEntity> grupos = grupoRepositorio
                .buscarActivosPorGradoOrdenadosPorEstudiantes(idGrado);
            
            GrupoEntity grupoAsignado = buscarGrupoDisponible(grupos);
            
            // Si no hay grupos con disponibilidad, crear uno nuevo
            if (grupoAsignado == null) {
                grupoAsignado = crearNuevoGrupo(idGrado);
            }
            
            // Asignar estudiante al grupo
            estudiante.setGrupo(grupoAsignado);
            entityManager.merge(estudiante);
            
            return ResultadoOperacion.exito("Estudiante asignado al grupo " + grupoAsignado.getNombreGrupo(), grupoAsignado);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResultadoOperacion.error("Error al asignar grupo: " + e.getMessage());
        }
    }
    
    /**
     * Busca un grupo disponible entre la lista proporcionada
     */
    private GrupoEntity buscarGrupoDisponible(List<GrupoEntity> grupos) {
        if (grupos == null || grupos.isEmpty()) {
            return null;
        }
        
        // Estrategia: Primero llenar grupos hasta el mínimo, luego distribuir
        for (GrupoEntity grupo : grupos) {
            Grupo grupoDomain = DominioAPersistenciaMapper.toDomain(grupo);
            
            // Si el grupo no ha alcanzado el mínimo, asignar ahí
            if (!grupoDomain.tieneEstudiantesSuficientes()) {
                return grupo;
            }
        }
        
        // Si todos tienen el mínimo, buscar uno con disponibilidad
        for (GrupoEntity grupo : grupos) {
            Grupo grupoDomain = DominioAPersistenciaMapper.toDomain(grupo);
            
            if (grupoDomain.tieneDisponibilidad()) {
                return grupo;
            }
        }
        
        return null;
    }
    
    /**
     * Crea un nuevo grupo para un grado específico
     */
    private GrupoEntity crearNuevoGrupo(Integer idGrado) throws Exception {
        Optional<GradoEntity> gradoOpt = gradoRepositorio.buscarPorId(idGrado);
        if (gradoOpt.isEmpty()) {
            throw new Exception("Grado no encontrado con ID: " + idGrado);
        }
        
        GradoEntity grado = gradoOpt.get();
        
        // Contar grupos existentes del grado usando el repositorio
        Long cantidadGrupos = grupoRepositorio.contarGruposPorGrado(idGrado);
        
        // Generar nombre del grupo
        String nombreGrupo = generarNombreGrupo(grado, cantidadGrupos);
        
        // Crear nuevo grupo
        GrupoEntity nuevoGrupo = new GrupoEntity();
        nuevoGrupo.setNombreGrupo(nombreGrupo);
        nuevoGrupo.setEstado(true);
        nuevoGrupo.setGrado(grado);
        nuevoGrupo.setEstudiantes(new HashSet<>());
        
        return grupoRepositorio.guardar(nuevoGrupo);
    }
    
    /**
     * Genera el nombre del grupo basado en el grado y la cantidad existente
     */
    private String generarNombreGrupo(GradoEntity grado, Long cantidadGrupos) {
        return String.format("%s-%d", grado.getNombreGrado(), cantidadGrupos + 1);
    }
    
    /**
     * Método para obtener grupos disponibles para un grado (puede ser útil para otros casos)
     */
    public List<GrupoEntity> obtenerGruposDisponiblesParaGrado(Integer idGrado) {
        List<GrupoEntity> grupos = grupoRepositorio
            .buscarActivosPorGradoOrdenadosPorEstudiantes(idGrado);
        
        return grupos.stream()
            .filter(grupo -> {
                Grupo grupoDomain = DominioAPersistenciaMapper.toDomain(grupo);
                return !grupoDomain.tieneEstudiantesSuficientes() || 
                       grupoDomain.tieneDisponibilidad();
            })
            .toList();
    }
}
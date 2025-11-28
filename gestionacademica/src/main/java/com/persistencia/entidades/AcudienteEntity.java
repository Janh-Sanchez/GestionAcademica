package com.persistencia.entidades;

import java.util.Set;
import com.dominio.Estado;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity(name = "acudiente")
public class AcudienteEntity extends UsuarioEntity{
	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 20)
	private Estado estadoAprobacion = Estado.Pendiente;

	@OneToMany(mappedBy = "acudiente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<EstudianteEntity> estudiantes;

	public AcudienteEntity(){
	}

    public Estado getEstadoAprobacion() { return estadoAprobacion; }
    public void setEstadoAprobacion(Estado estadoAprobacion) { 
        this.estadoAprobacion = estadoAprobacion; 
    }
	
    public Set<EstudianteEntity> getEstudiantes() { return estudiantes; }
    public void setEstudiantes(Set<EstudianteEntity> estudiantes) { 
        this.estudiantes = estudiantes; 
    }
}

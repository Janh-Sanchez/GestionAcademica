package com.persistencia.entidades;

import java.util.Set;

import com.dominio.Estado;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity(name = "estudiante")
public class EstudianteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estudiante")
    private Integer idEstudiante;

    @NotBlank
    @Size(min = 2, max = 30)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ]+$", message = "El nombre no debe contener números")
    @Column(nullable = false, length = 30)
    private String primerNombre;

    @Size(min = 2, max = 30)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ]+$", message = "El nombre no debe contener números")
    @Column(length = 30)
    private String segundoNombre;

    @NotBlank
    @Size(min = 2, max = 30)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ]+$", message = "El nombre no debe contener números")
    @Column(nullable = false, length = 30)
    private String primerApellido;

    @Size(min = 2, max = 30)
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúñÑ]+$", message = "El nombre no debe contener números")
    @Column(length = 30)
    private String segundoApellido;

    @Pattern(regexp = "^[0-9]{6,15}$", message = "Debe tener entre 6 y 15 dígitos numéricos")
    @Column(nullable = false, unique = true, length = 15)
    private String nuip;

    @Min(3)
    @Max(120)
    @Column(nullable = false)
    private int edad;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Estado estado = Estado.Pendiente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "acudiente", nullable = false, referencedColumnName = "id_usuario")
    private AcudienteEntity acudiente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gradoAspira", referencedColumnName = "id_grado")
    private GradoEntity gradoAspira;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo", referencedColumnName = "id_grupo")
    private GrupoEntity grupo;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "hojaDeVida", unique = true, referencedColumnName = "id_hoja_vida")
    private HojaVidaEntity hojaDeVida;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "observador", unique = true)
    private ObservadorEntity observador;

	@OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<LogroEstudianteEntity> logrosCalificados;

	@OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<BoletinEntity> boletines;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "preinscripcion_id", referencedColumnName = "id_preinscripcion")
	private PreinscripcionEntity preinscripcion;

    public Integer getIdEstudiante() { return idEstudiante; }
    public void setIdEstudiante(Integer idEstudiante) { this.idEstudiante = idEstudiante; }
    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String primerNombre) { this.primerNombre = primerNombre; }
    public String getSegundoNombre() { return segundoNombre; }
    public void setSegundoNombre(String segundoNombre) { this.segundoNombre = segundoNombre; }
    public String getPrimerApellido() { return primerApellido; }
    public void setPrimerApellido(String primerApellido) { this.primerApellido = primerApellido; }
    public String getSegundoApellido() { return segundoApellido; }
    public void setSegundoApellido(String segundoApellido) { this.segundoApellido = segundoApellido; }
    public String getNuip() { return nuip; }
    public void setNuip(String nuip) { this.nuip = nuip; }
    public int getEdad() { return edad; }
    public void setEdad(int edad) { this.edad = edad; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public AcudienteEntity getAcudiente() { return acudiente; }
    public void setAcudiente(AcudienteEntity acudiente) { this.acudiente = acudiente; }
    public GradoEntity getGradoAspira() { return gradoAspira; }
    public void setGradoAspira(GradoEntity gradoAspira) { this.gradoAspira = gradoAspira; }
    public GrupoEntity getGrupo() { return grupo; }
    public void setGrupo(GrupoEntity grupo) { this.grupo = grupo; }
    public HojaVidaEntity getHojaDeVida() { return hojaDeVida; }
    public void setHojaDeVida(HojaVidaEntity hojaDeVida) { this.hojaDeVida = hojaDeVida; }
    public ObservadorEntity getObservador() { return observador; }
    public void setObservador(ObservadorEntity observador) { this.observador = observador; }
    public Set<LogroEstudianteEntity> getLogrosCalificados() { return logrosCalificados; }
    public void setLogrosCalificados(Set<LogroEstudianteEntity> logrosCalificados) { this.logrosCalificados = logrosCalificados; }
    public Set<BoletinEntity> getBoletines() { return boletines; }
    public void setBoletines(Set<BoletinEntity> boletines) { this.boletines = boletines; }
    public PreinscripcionEntity getPreinscripcion() { return preinscripcion; }
    public void setPreinscripcion(PreinscripcionEntity preinscripcion) { this.preinscripcion = preinscripcion; }

}
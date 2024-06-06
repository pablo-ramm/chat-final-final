package com.springboot.MyTodoList.model;

import io.swagger.models.auth.In;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "TAREA")
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTarea;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "DESCRIPCION", length = 255)
    private String descripcion;

    @Column(name = "ESTADO")
    private boolean estado;

    @Column(name = "FECHA_INICIO")
    private OffsetDateTime fechaInicio;

    @Column(name = "FECHA_FINAL")
    private OffsetDateTime fechaFinal;

    @Column(name = "PRIORIDAD")
    private Integer prioridad;

    @Column(name = "COMENTARIOS", length = 255)
    private String comentarios;

    @Column(name = "IDDESARROLLADOR")
    private Integer desarrollador;

    @Column(name = "IDPROYECTO")
    private Integer proyecto;

    /*@ManyToOne
    @JoinColumn(name = "IDDESARROLLADOR")
    private Desarrollador desarrollador;

    @ManyToOne
    @JoinColumn(name = "IDPROYECTO")
    private Proyecto proyecto;*/


    // Constructor vacío
    public Tarea() {}

    // Constructor con parámetros
    public Tarea(int idTarea, String nombre, String descripcion, boolean estado, OffsetDateTime fechaInicio,
                 OffsetDateTime fechaFinal, Integer prioridad, String comentarios) {
        this.idTarea = idTarea;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = false;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.prioridad = prioridad;
        this.comentarios = comentarios;
        this.desarrollador = 2;
        this.proyecto = 1;

        /*this.desarrollador = desarrollador;
        this.proyecto = proyecto;*/
    }

    // Getters y setters
    public int getIdTarea() {
        return idTarea;
    }

    public void setIdTarea(int idTarea) {
        this.idTarea = idTarea;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public OffsetDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(OffsetDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public OffsetDateTime getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(OffsetDateTime fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Integer getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(Integer prioridad) {
        this.prioridad = prioridad;
    }

    public String getComentarios() {
        return comentarios;
    }

    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }
    public Integer getDesarrollador() {
        return desarrollador;
    }

    public void setDesarrollador(Integer desarrollador) {
        this.desarrollador = desarrollador;
    }

    public Integer getProyecto() {
        return proyecto;
    }

    public void setProyecto(Integer proyecto) {
        this.proyecto = proyecto;
    }

    /*public Desarrollador getDesarrollador() {
        return desarrollador;
    }

    public void setDesarrollador(Desarrollador desarrollador) {
        this.desarrollador = desarrollador;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }*/

    public void setDefaultValues() {
        this.nombre = ""; // Valor predeterminado para el nombre
        this.descripcion = ""; // Valor predeterminado para la descripción
        this.estado = false; // Valor predeterminado para el estado (false = no completado)
        this.fechaInicio = OffsetDateTime.now(); // Valor predeterminado para la fecha de inicio (actual)
        this.fechaFinal = null; // Valor predeterminado para la fecha de finalización (null)
        this.prioridad = 0; // Valor predeterminado para la prioridad
        this.comentarios = ""; // Valor predeterminado para los comentarios
        this.desarrollador = 2; // Valor predeterminado para el desarrollador (null)
        this.proyecto = 1   ; // Valor predeterminado para el proyecto (null)
    }

    @Override
    public String toString() {
        return "Tarea{" +
                "idTarea=" + idTarea +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", estado=" + estado +
                ", fechaInicio=" + fechaInicio +
                ", fechaFinal=" + fechaFinal +
                ", prioridad=" + prioridad +
                ", comentarios='" + comentarios + '\'' +
                ", desarrollador=" + desarrollador +
                ", proyecto=" + proyecto +
                '}';
    }
}

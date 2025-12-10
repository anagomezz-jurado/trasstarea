package com.example.trasstarea.Modelo;

public class Tarea {

    private String titulo;
    private String fechaCreacion;
    private String fechaObjetivo;
    private int progreso;
    private boolean prioritaria;
    private String descripcion;


    public Tarea(String titulo, String fechaCreacion, String fechaObjetivo,
                 int progreso, boolean prioritaria, String descripcion) {

        this.titulo = titulo;
        this.fechaCreacion = fechaCreacion;
        this.fechaObjetivo = fechaObjetivo;
        this.progreso = progreso;
        this.prioritaria = prioritaria;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getProgreso() {
        return progreso;
    }

    public void setProgreso(int progreso) {
        if (progreso < 0) progreso = 0;
        if (progreso > 100) progreso = 100;
        this.progreso = progreso;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public String getFechaObjetivo() {
        return fechaObjetivo;
    }

    public void setFechaObjetivo(String fechaObjetivo) {
        this.fechaObjetivo = fechaObjetivo;
    }

    public boolean isPrioritaria() {
        return prioritaria;
    }

    public void setPrioritaria(boolean prioritaria) {
        this.prioritaria = prioritaria;
    }

    @Override
    public String toString() {
        return "Tarea{" +
                "titulo='" + titulo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", progreso=" + progreso +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaObjetivo=" + fechaObjetivo +
                ", prioritaria=" + prioritaria +
                '}';
    }
}
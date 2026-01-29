package com.example.trasstarea.Modelo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Tarea {

    private String titulo;
    private String fechaCreacion; // formato: dd/MM/yyyy
    private String fechaObjetivo; // formato: dd/MM/yyyy
    private int progreso;
    private boolean prioritaria;
    private String descripcion;

    // Lista de archivos adjuntos
    private List<String> archivosAdjuntos;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public Tarea(String titulo, String fechaCreacion, String fechaObjetivo,
                 int progreso, boolean prioritaria, String descripcion) {
        this.titulo = titulo;
        this.fechaCreacion = fechaCreacion;
        this.fechaObjetivo = fechaObjetivo;
        setProgreso(progreso);
        this.prioritaria = prioritaria;
        this.descripcion = descripcion;
        this.archivosAdjuntos = new ArrayList<>(); // inicializamos la lista vacía
    }

    // --- Getters y Setters ---
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getProgreso() { return progreso; }
    public void setProgreso(int progreso) {
        if (progreso < 0) progreso = 0;
        if (progreso > 100) progreso = 100;
        this.progreso = progreso;
    }

    public String getFechaCreacion() { return fechaCreacion; }
    public String getFechaObjetivo() { return fechaObjetivo; }
    public void setFechaObjetivo(String fechaObjetivo) { this.fechaObjetivo = fechaObjetivo; }

    public boolean isPrioritaria() { return prioritaria; }
    public void setPrioritaria(boolean prioritaria) { this.prioritaria = prioritaria; }

    // --- Archivos adjuntos ---
    public List<String> getArchivosAdjuntos() { return archivosAdjuntos; }
    public void setArchivosAdjuntos(List<String> archivosAdjuntos) { this.archivosAdjuntos = archivosAdjuntos; }
    public void agregarArchivo(String rutaArchivo) {
        if (archivosAdjuntos == null) archivosAdjuntos = new ArrayList<>();
        archivosAdjuntos.add(rutaArchivo);
    }

    // --- Métodos de utilidad para fechas ---
    public LocalDate getFechaCreacionDate() {
        return LocalDate.parse(fechaCreacion, FORMATTER);
    }

    public LocalDate getFechaObjetivoDate() {
        return LocalDate.parse(fechaObjetivo, FORMATTER);
    }

    public long getDiasRestantes() {
        return ChronoUnit.DAYS.between(LocalDate.now(), getFechaObjetivoDate());
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
                ", archivosAdjuntos=" + archivosAdjuntos +
                '}';
    }
}

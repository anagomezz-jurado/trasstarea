package com.example.trasstarea.Modelo;

import java.io.File;

public class Adjunto {
    private String ruta;
    private String tipo; // "doc", "img", "audio", "video"

    public Adjunto(String ruta, String tipo) {
        this.ruta = ruta;
        this.tipo = tipo;
    }
    public String getRuta() { return ruta; }
    public String getTipo() { return tipo; }
    public String getNombre() { return new File(ruta).getName(); }
}

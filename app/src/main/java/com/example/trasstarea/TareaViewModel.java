package com.example.trasstarea;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class TareaViewModel extends ViewModel {
    public MutableLiveData<String> titulo = new MutableLiveData<>();
    public MutableLiveData<String> fechaCreacion = new MutableLiveData<>();
    public MutableLiveData<String> fechaObjetivo = new MutableLiveData<>();
    public MutableLiveData<Integer> progreso = new MutableLiveData<>();
    public MutableLiveData<Boolean> prioritaria = new MutableLiveData<>();
    public MutableLiveData<String> descripcion = new MutableLiveData<>();
    public MutableLiveData<List<String>> archivosAdjuntos = new MutableLiveData<>(new ArrayList<>());

    // Método helper para agregar archivos de forma segura
    public void agregarArchivo(String rutaArchivo) {
        List<String> archivos = archivosAdjuntos.getValue();
        if (archivos == null) archivos = new ArrayList<>();
        archivos.add(rutaArchivo);
        archivosAdjuntos.setValue(archivos);
    }
}

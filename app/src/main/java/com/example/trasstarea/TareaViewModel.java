package com.example.trasstarea;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TareaViewModel extends ViewModel {
    //Define todos los atributos como objetos de MutableLiveData
    public MutableLiveData<String> titulo = new MutableLiveData<>();
    public MutableLiveData<String> fechaCreacion = new MutableLiveData<>();
    public MutableLiveData<String> fechaObjetivo = new MutableLiveData<>();
    public MutableLiveData<Integer> progreso = new MutableLiveData<>();
    public MutableLiveData<Boolean> prioritaria = new MutableLiveData<>();
    public MutableLiveData<String> descripcion = new MutableLiveData<>();
}

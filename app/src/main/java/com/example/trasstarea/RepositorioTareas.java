package com.example.trasstarea;

import com.example.trasstarea.Modelo.Tarea;

import java.util.ArrayList;


public class RepositorioTareas {
    //Creo un ArrayList estático que guarda la lista de las tareas que hay
    public static ArrayList<Tarea> listaTareas = new ArrayList<>();
    static {
        // Datos de ejemplo con diferentes niveles de progreso y prioridad
        listaTareas.add(new Tarea("Comprar comida", "10/12/2025", "12/12/2025", 0, true, "Comprar leche, pan, huevos y frutas"));
        listaTareas.add(new Tarea("Estudiar matemáticas", "10/12/2025", "15/12/2025", 25, false, "Repasar álgebra y geometría"));
        listaTareas.add(new Tarea("Hacer ejercicio", "10/12/2025", "10/12/2025", 50, true, "Correr 30 minutos y estiramientos"));
        listaTareas.add(new Tarea("Llamar al banco", "10/12/2025", "12/12/2025", 0, false, "Consultar movimientos y pagos pendientes"));
        listaTareas.add(new Tarea("Preparar presentación", "09/12/2025", "14/12/2025", 75, true, "Diapositivas sobre proyecto final"));
        listaTareas.add(new Tarea("Leer libro", "08/12/2025", "20/12/2025", 50, false, "Leer capítulo 5 y 6 del libro de historia"));
        listaTareas.add(new Tarea("Enviar correos", "10/12/2025", "11/12/2025", 100, true, "Responder correos urgentes del trabajo"));
        listaTareas.add(new Tarea("Cocinar cena", "10/12/2025", "10/12/2025", 25, false, "Preparar pasta con verduras"));
    }
}

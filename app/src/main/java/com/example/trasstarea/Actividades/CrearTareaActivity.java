package com.example.trasstarea.Actividades;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.trasstarea.Fragmentos.FragmentoDos;
import com.example.trasstarea.Fragmentos.FragmentoUno;
import com.example.trasstarea.Modelo.Tarea;
import com.example.trasstarea.R;
import com.example.trasstarea.RepositorioTareas;
import com.example.trasstarea.TareaViewModel;

public class CrearTareaActivity extends AppCompatActivity
        implements FragmentoUno.FragmentoUnoListener, FragmentoDos.FragmentoDosListener {

    private TareaViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_tarea);

        vm = new ViewModelProvider(this).get(TareaViewModel.class);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedorFragmento, new FragmentoUno())
                .commit();

    }

    private void cargarFragmentoDos() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedorFragmento, new FragmentoDos())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onDatosIngresados(String titulo, String fechaCreacion, String fechaObjetivo,
                                  int progreso, boolean prioritaria) {

        vm.titulo.setValue(titulo);
        vm.fechaCreacion.setValue(fechaCreacion);
        vm.fechaObjetivo.setValue(fechaObjetivo);
        vm.progreso.setValue(progreso);
        vm.prioritaria.setValue(prioritaria);

        cargarFragmentoDos();
    }

    @Override
    public void onGuardarDescripcion(String descripcion) {

        vm.descripcion.setValue(descripcion);

        Tarea tarea = new Tarea(
                vm.titulo.getValue() != null ? vm.titulo.getValue() : "",
                vm.fechaCreacion.getValue() != null ? vm.fechaCreacion.getValue() : "",
                vm.fechaObjetivo.getValue() != null ? vm.fechaObjetivo.getValue() : "",
                vm.progreso.getValue() != null ? vm.progreso.getValue() : 0,
                vm.prioritaria.getValue() != null ? vm.prioritaria.getValue() : false,
                vm.descripcion.getValue() != null ? vm.descripcion.getValue() : ""
        );

        // Añadir a lista estática
        RepositorioTareas.listaTareas.add(tarea);

        Toast.makeText(this, getString(R.string.msgTareaGuardadaCorrectamente) , Toast.LENGTH_LONG).show();
        finish(); // Volver a ListadoTareasActivity
    }

    @Override
    public void onVolver() {
        getSupportFragmentManager().popBackStack();
    }
}

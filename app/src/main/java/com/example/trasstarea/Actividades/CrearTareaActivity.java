package com.example.trasstarea.Actividades;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.trasstarea.Fragmentos.FragmentoDos;
import com.example.trasstarea.Fragmentos.FragmentoUno;
import com.example.trasstarea.Fragmentos.SettingsFragment;
import com.example.trasstarea.Modelo.Tarea;
import com.example.trasstarea.R;
import com.example.trasstarea.TareaViewModel;

public class CrearTareaActivity extends AppCompatActivity
        implements FragmentoUno.FragmentoUnoListener, FragmentoDos.FragmentoDosListener {


    private TareaViewModel vm; //Creo la instandia de la clase TareaViewModel para compartir datos entre los fragmentos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SettingsFragment.aplicarTema(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_tarea); //es el diseño de la actividad

        vm = new ViewModelProvider(this).get(TareaViewModel.class); //inicializo la instancia

        //cargo el FragmentoUno con el contenedor de la vista donde se debe representar el primer fragmento
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedorFragmento, new FragmentoUno())
                .commit();

    }


    //Reemplazo el FragmentoUno por el FragmentoDos
    private void cargarFragmentoDos() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.contenedorFragmento, new FragmentoDos())
                .addToBackStack(null) // he añadido esto para que pueda darle al botón de retroceso
                .commit();
    }

    @Override
    public void onDatosIngresados(String titulo, String fechaCreacion, String fechaObjetivo,
                                  int progreso, boolean prioritaria) { //Se activa cuando se le da a Siguiente

        //Guardo los valores de cada campo en los objetos de la clase TareaViewModel y llamo al
        //método cargarGramentoDoss
        vm.titulo.setValue(titulo);
        vm.fechaCreacion.setValue(fechaCreacion);
        vm.fechaObjetivo.setValue(fechaObjetivo);
        vm.progreso.setValue(progreso);
        vm.prioritaria.setValue(prioritaria);

        cargarFragmentoDos();
    }

    @Override
    public void onGuardarDescripcion(String descripcion) { //Se activa cuando le da a Guardar

        vm.descripcion.setValue(descripcion);

        Tarea tarea = new Tarea(
                vm.titulo.getValue() != null ? vm.titulo.getValue() : "",
                vm.fechaCreacion.getValue() != null ? vm.fechaCreacion.getValue() : "",
                vm.fechaObjetivo.getValue() != null ? vm.fechaObjetivo.getValue() : "",
                vm.progreso.getValue() != null ? vm.progreso.getValue() : 0,
                vm.prioritaria.getValue() != null ? vm.prioritaria.getValue() : false,
                vm.descripcion.getValue() != null ? vm.descripcion.getValue() : ""
        );


        ListadoTareasActivity.listaTareas.add(tarea);

        Toast.makeText(this, getString(R.string.msgTareaGuardadaCorrectamente) , Toast.LENGTH_LONG).show();
        finish();
    }

    //Este método se usa por si le da al botón volver que se va a FragmentoUno
    @Override
    public void onVolver() {
        getSupportFragmentManager().popBackStack();
    }
}

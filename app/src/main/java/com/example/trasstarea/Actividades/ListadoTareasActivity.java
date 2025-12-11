package com.example.trasstarea.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trasstarea.Modelo.Tarea;
import com.example.trasstarea.R;
import com.example.trasstarea.RepositorioTareas;
import com.example.trasstarea.TareaAdapter;

import java.util.ArrayList;

public class ListadoTareasActivity extends AppCompatActivity {

    private RecyclerView rv; // Esto es lo que contiene la vista para que se desplazce
    private TareaAdapter adaptador; //Instancio el adaptador para visualizar los datos
    private boolean mostrarSoloPrioritarias = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_tareas);

        Toolbar toolbar = findViewById(R.id.toolbarListado);
        setSupportActionBar(toolbar); // Añado el toolbar a la actividad

        rv = findViewById(R.id.rvTareas); // Enlazo el reycler con el reycler de la vista

        // Inicializo el adaptador con una copia de la lista de tareas
        adaptador = new TareaAdapter(new ArrayList<>(RepositorioTareas.listaTareas));
        rv.setAdapter(adaptador);
        rv.setLayoutManager(new LinearLayoutManager(this)); // Establece un layout vertical para la lista.

        // Habilitar la flecha de atrás
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    //Cuando le doy a la felchita de atrás se va a la actividad anterior
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    //Este método refresca la lista cada vez que se edita o se crea un componente
    @Override
    protected void onResume() {
        super.onResume();
        actualizarLista();
    }

    //El método actualiza la lista de las tareas
    private void actualizarLista() {
        ArrayList<Tarea> listaAMostrar;
        if (mostrarSoloPrioritarias) {
            listaAMostrar = new ArrayList<>();
            // Itera para aplicar el filtro de tareas prioritarias.
            for (Tarea t : RepositorioTareas.listaTareas) {
                if (t.isPrioritaria()) listaAMostrar.add(t);
            }
        } else {
            // Muestro  las tareas
            listaAMostrar = new ArrayList<>(RepositorioTareas.listaTareas);
        }

        //Se notifica al adaptador que se han cambiado datos
        adaptador.actualizarDatos(listaAMostrar);
    }

    //Se va a la actividad CrearTarea
    private void abrirCrearTarea() {
        startActivity(new Intent(this, CrearTareaActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu); // Infla el menú de opciones en la Toolbar
        // Asegura que los grupos de ítems del menú sean visibles.
        menu.setGroupVisible(R.id.it_group_actividades, true);
        menu.setGroupVisible(R.id.it_group_add, true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.it_anadir) {
            abrirCrearTarea();  //Llamo al método para irme a la actividad de Crear tarea
        } else if (id == R.id.it_prioritarias) {
            mostrarSoloPrioritarias = !mostrarSoloPrioritarias; //Segun si está activa o no
            actualizarLista(); // Actualizo la lista
            String mensaje = mostrarSoloPrioritarias ? getString(R.string.msg_solo_prioritarias)
                    : getString(R.string.msg_todas_tareas);
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.it_acercade) {
            mostrarAcercaDe(); // LLamo al método donde muestra una alerta
        } else if (id == R.id.it_salir) {
            finishAffinity(); // Cierra la aplciaci´´on
        }
        return super.onOptionsItemSelected(item);
    }

    private void mostrarAcercaDe() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage(getString(R.string.acerca_de_mensaje))
                .setPositiveButton(getString(R.string.btn_aceptar), (dialog, which) -> dialog.dismiss())
                .show();
    }

}
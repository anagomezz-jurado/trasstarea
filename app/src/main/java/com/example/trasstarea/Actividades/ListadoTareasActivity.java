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

    private RecyclerView rv;
    private TareaAdapter adaptador;
    private boolean mostrarSoloPrioritarias = false; // flag para filtrar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_tareas);

        Toolbar toolbar = findViewById(R.id.toolbarListado);
        setSupportActionBar(toolbar);

        rv = findViewById(R.id.rvTareas);

        adaptador = new TareaAdapter(new ArrayList<>(RepositorioTareas.listaTareas));
        rv.setAdapter(adaptador);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Habilitar la flecha de atrás
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Cierra la actividad actual y vuelve a la anterior
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarLista(); // refresca la lista al volver
    }

    private void actualizarLista() {
        ArrayList<Tarea> listaAMostrar;
        if (mostrarSoloPrioritarias) {
            listaAMostrar = new ArrayList<>();
            for (Tarea t : RepositorioTareas.listaTareas) {
                if (t.isPrioritaria()) listaAMostrar.add(t);
            }
        } else {
            listaAMostrar = new ArrayList<>(RepositorioTareas.listaTareas);
        }

        adaptador.actualizarDatos(listaAMostrar);
    }

    private void abrirCrearTarea() {
        startActivity(new Intent(this, CrearTareaActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        menu.setGroupVisible(R.id.it_group_actividades, true);
        menu.setGroupVisible(R.id.it_group_add, true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.it_anadir) {
            abrirCrearTarea();
        } else if (id == R.id.it_prioritarias) {
            mostrarSoloPrioritarias = !mostrarSoloPrioritarias; // alterna filtro
            actualizarLista();
            String mensaje = mostrarSoloPrioritarias ? getString(R.string.msg_solo_prioritarias)
                                                        : getString(R.string.msg_todas_tareas);
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.it_acercade) {
            mostrarAcercaDe();
        } else if (id == R.id.it_salir) {
            finishAffinity();
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
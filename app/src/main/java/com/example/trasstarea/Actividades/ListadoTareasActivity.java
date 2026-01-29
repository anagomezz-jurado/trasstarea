package com.example.trasstarea.Actividades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trasstarea.Fragmentos.SettingsFragment;
import com.example.trasstarea.Modelo.Tarea;
import com.example.trasstarea.R;
import com.example.trasstarea.TareaAdapter;

import java.util.ArrayList;
import java.util.Comparator;

public class ListadoTareasActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    //  VARIABLES DE LA ACTIVIDAD
    public static ArrayList<Tarea> listaTareas = new ArrayList<>();
    private RecyclerView rv;
    private TareaAdapter adaptador;
    private boolean mostrarSoloPrioritarias = false;

    // CICLO DE VIDA
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Aplicar tema antes de super.onCreate
        SettingsFragment.aplicarTema(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_tareas);

        inicializarToolbar();
        inicializarRecyclerView();
        cargarTareas();

        // Registrar listener para cambios en preferencias
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarLista(); // refrescar cada vez que se vuelve a la actividad
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        // Aplicar tamaño de letra al instante
        super.attachBaseContext(SettingsFragment.aplicarTamanoLetra(newBase));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // flecha atrás
        return true;
    }

    // INICIALIZACIÓN DE VISTAS
    private void inicializarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarListado);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // flecha atrás
    }

    private void inicializarRecyclerView() {
        rv = findViewById(R.id.rvTareas);
        adaptador = new TareaAdapter(new ArrayList<>(listaTareas));
        rv.setAdapter(adaptador);
        rv.setLayoutManager(new LinearLayoutManager(this));
    }

    // MÉTODOS DE PREFERENCIAS
    private int obtenerCriterio() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return Integer.parseInt(prefs.getString("criterio", "2")); // por defecto: fecha creación
    }

    private boolean esAscendente() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getBoolean("orden", true); // por defecto: ascendente
    }

    // Listener de cambios en preferencias
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if ("fuente".equals(key)) {
            recreate(); // cambia tamaño de letra al instante
        }
        if ("criterio".equals(key) || "orden".equals(key)) {
            actualizarLista(); // cambia orden al instante
        }
    }


    // CARGAR Y ACTUALIZAR LISTA DE TAREAS
    private void cargarTareas() {
        listaTareas.clear(); // evitar duplicados al recargar
        listaTareas.add(new Tarea("Comprar comida", "10/12/2025", "12/12/2025", 0, true, "Comprar leche, pan, huevos y frutas"));
        listaTareas.add(new Tarea("Estudiar matemáticas", "10/12/2025", "15/12/2025", 25, false, "Repasar álgebra y geometría"));
        listaTareas.add(new Tarea("Hacer ejercicio", "10/12/2025", "10/12/2025", 50, true, "Correr 30 minutos y estiramientos"));
        listaTareas.add(new Tarea("Llamar al banco", "10/12/2025", "12/12/2025", 0, false, "Consultar movimientos y pagos pendientes"));
        listaTareas.add(new Tarea("Preparar presentación", "09/12/2025", "14/12/2025", 75, true, "Diapositivas sobre proyecto final"));
        listaTareas.add(new Tarea("Leer libro", "08/12/2025", "20/12/2025", 50, false, "Leer capítulo 5 y 6 del libro de historia"));
        listaTareas.add(new Tarea("Enviar correos", "10/12/2025", "11/12/2025", 100, true, "Responder correos urgentes del trabajo"));
        listaTareas.add(new Tarea("Cocinar cena", "10/12/2025", "10/12/2025", 25, false, "Preparar pasta con verduras"));

        actualizarLista();
    }

    private void actualizarLista() {
        ArrayList<Tarea> listaAMostrar;

        if (mostrarSoloPrioritarias) {
            listaAMostrar = new ArrayList<>();
            for (Tarea t : listaTareas) {
                if (t.isPrioritaria()) listaAMostrar.add(t);
            }
        } else {
            listaAMostrar = new ArrayList<>(listaTareas);
        }

        ordenarLista(listaAMostrar); // ordenar según preferencias
        adaptador.actualizarDatos(listaAMostrar);
    }

    // ORDENAR LISTA SEGÚN PREFERENCIAS
    private void ordenarLista(ArrayList<Tarea> lista) {
        int criterio = obtenerCriterio();
        boolean asc = esAscendente();

        Comparator<Tarea> comparator;

        switch (criterio) {
            case 1: // Alfabético
                comparator = Comparator.comparing(Tarea::getTitulo, String.CASE_INSENSITIVE_ORDER);
                break;
            case 2: // Fecha de creación
                comparator = Comparator.comparing(Tarea::getFechaCreacionDate);
                break;
            case 3: // Días restantes
                comparator = Comparator.comparingLong(Tarea::getDiasRestantes);
                break;
            case 4: // Progreso
                comparator = Comparator.comparingInt(Tarea::getProgreso);
                break;
            default:
                comparator = Comparator.comparing(Tarea::getFechaCreacionDate);
        }
        if (!asc) comparator = comparator.reversed();
        lista.sort(comparator);

    }

    // MÉTODOS DEL MENU
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
            mostrarSoloPrioritarias = !mostrarSoloPrioritarias;
            actualizarLista();
            String mensaje = mostrarSoloPrioritarias ?
                    getString(R.string.msg_solo_prioritarias) :
                    getString(R.string.msg_todas_tareas);
            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
        } else if (id == R.id.it_preferencias) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
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


    // ABRIR ACTIVIDADES AUXILIARES
    private void abrirCrearTarea() {
        startActivity(new Intent(this, CrearTareaActivity.class));
    }
}

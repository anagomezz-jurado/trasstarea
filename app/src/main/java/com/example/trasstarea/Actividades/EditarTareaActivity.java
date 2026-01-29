package com.example.trasstarea.Actividades;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trasstarea.Fragmentos.SettingsFragment;
import com.example.trasstarea.Modelo.Tarea;
import com.example.trasstarea.R;

import java.io.File;

public class EditarTareaActivity extends AppCompatActivity {

    private int tareaIndex;
    private Tarea tarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SettingsFragment.aplicarTema(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_tarea);

        // Obtengo la posición de la tarea desde el Intent
        tareaIndex = getIntent().getIntExtra("tareaIndex", -1);
        if (tareaIndex == -1) {
            finish();
            return;
        }

        //En tarea guardo los datos de la tarea que indique el index
        tarea = ListadoTareasActivity.listaTareas.get(tareaIndex);

        EditText etTitulo = findViewById(R.id.etTituloEditar);
        EditText etFechaCreacion = findViewById(R.id.etFechaCreacionEditar);
        EditText etFechaObjetivo = findViewById(R.id.etFechaObjetivoEditar);
        Spinner spProgreso = findViewById(R.id.spProgresoEditar);
        CheckBox cbPrioritaria = findViewById(R.id.cbPrioritariaEditar);
        EditText etDescripcion = findViewById(R.id.etDescripcionEditar);
        Button btnGuardar = findViewById(R.id.btnGuardarEditar);

        // Cargo los datos actuales a tarea
        etTitulo.setText(tarea.getTitulo());
        etFechaCreacion.setText(tarea.getFechaCreacion());
        etFechaObjetivo.setText(tarea.getFechaObjetivo());
        etDescripcion.setText(tarea.getDescripcion());
        cbPrioritaria.setChecked(tarea.isPrioritaria());

        //Vuelvo a crear un ArrayAdapter para el spiner mostrando las soluciones que he creado en strings
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.opciones_spinner,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProgreso.setAdapter(adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProgreso.setAdapter(adapter);

        int[] valoresProgreso = {0, 25, 50, 75, 100};
        for (int i = 0; i < valoresProgreso.length; i++) {
            if (valoresProgreso[i] == tarea.getProgreso()) {
                spProgreso.setSelection(i);   //establezco la selección correcta al spiner
                break;
            }
        }

        //Al darle al botón guardar le doy a cada campo su valor y lo guardo en tarea
        btnGuardar.setOnClickListener(v -> {
            tarea.setTitulo(etTitulo.getText().toString());
            tarea.setFechaObjetivo(etFechaObjetivo.getText().toString());
            tarea.setDescripcion(etDescripcion.getText().toString());
            tarea.setPrioritaria(cbPrioritaria.isChecked());
            tarea.setProgreso(valoresProgreso[spProgreso.getSelectedItemPosition()]);

            Toast.makeText(this, getString(R.string.msgTareaActualizada), Toast.LENGTH_SHORT).show();
            finish(); //Una vez guardado salgo de la actividad
        });
// Al final del onCreate, cargamos los archivos adjuntos
        cargarAdjuntosParaEdicion();
    }

    private void cargarAdjuntosParaEdicion() {
        LinearLayout contenedor = findViewById(R.id.contenedorAdjuntosEditar);
        contenedor.removeAllViews(); // Limpia la vista para no duplicar botones al borrar uno

        if (tarea.getArchivosAdjuntos() == null || tarea.getArchivosAdjuntos().isEmpty()) {
            TextView tvVacio = new TextView(this);
            tvVacio.setText("No hay archivos adjuntos.");
            contenedor.addView(tvVacio);
            return;
        }

        for (String ruta : tarea.getArchivosAdjuntos()) {
            Button btnArchivo = new Button(this);
            File archivo = new File(ruta);

            // Ponemos el nombre del archivo en el botón
            btnArchivo.setText("X  " + archivo.getName());
            btnArchivo.setAllCaps(false);

            btnArchivo.setOnClickListener(v -> {
                // Confirmación de borrado
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Eliminar archivo")
                        .setMessage("¿Deseas borrar físicamente este archivo para liberar espacio?")
                        .setPositiveButton("Eliminar", (dialog, which) -> {

                            // 1. ELIMINACIÓN FÍSICA (Gestión de espacio)
                            if (archivo.exists()) {
                                if (archivo.delete()) {
                                    Toast.makeText(this, "Archivo borrado del disco", Toast.LENGTH_SHORT).show();
                                }
                            }

                            // 2. ELIMINACIÓN LÓGICA (Base de datos/Lista)
                            tarea.getArchivosAdjuntos().remove(ruta);

                            // 3. REFRESCAR LA INTERFAZ
                            cargarAdjuntosParaEdicion();
                        })
                        .setNegativeButton("Cancelar", null)
                        .show();
            });

            contenedor.addView(btnArchivo);
        }
    }
}

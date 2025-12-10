package com.example.trasstarea.Actividades;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trasstarea.Modelo.Tarea;
import com.example.trasstarea.R;
import com.example.trasstarea.RepositorioTareas;

public class EditarTareaActivity extends AppCompatActivity {

    private int tareaIndex;
    private Tarea tarea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_tarea);

        // Obtener posición de la tarea desde el Intent
        tareaIndex = getIntent().getIntExtra("tareaIndex", -1);
        if (tareaIndex == -1) {
            finish();
            return;
        }

        tarea = RepositorioTareas.listaTareas.get(tareaIndex);

        EditText etTitulo = findViewById(R.id.etTituloEditar);
        EditText etFechaCreacion = findViewById(R.id.etFechaCreacionEditar);
        EditText etFechaObjetivo = findViewById(R.id.etFechaObjetivoEditar);
        Spinner spProgreso = findViewById(R.id.spProgresoEditar);
        CheckBox cbPrioritaria = findViewById(R.id.cbPrioritariaEditar);
        EditText etDescripcion = findViewById(R.id.etDescripcionEditar);
        Button btnGuardar = findViewById(R.id.btnGuardarEditar);

        // Cargar datos actuales
        etTitulo.setText(tarea.getTitulo());
        etFechaCreacion.setText(tarea.getFechaCreacion());
        etFechaObjetivo.setText(tarea.getFechaObjetivo());
        etDescripcion.setText(tarea.getDescripcion());
        cbPrioritaria.setChecked(tarea.isPrioritaria());

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
                spProgreso.setSelection(i);
                break;
            }
        }

        btnGuardar.setOnClickListener(v -> {
            tarea.setTitulo(etTitulo.getText().toString());
            tarea.setFechaObjetivo(etFechaObjetivo.getText().toString());
            tarea.setDescripcion(etDescripcion.getText().toString());
            tarea.setPrioritaria(cbPrioritaria.isChecked());
            tarea.setProgreso(valoresProgreso[spProgreso.getSelectedItemPosition()]);

            Toast.makeText(this, getString(R.string.msgTareaActualizada), Toast.LENGTH_SHORT).show();
            finish();
        });

    }

}

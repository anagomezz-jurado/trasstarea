package com.example.trasstarea.Actividades;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.trasstarea.Modelo.Tarea;
import com.example.trasstarea.R;
import java.io.File;

public class DescripcionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion);

        // 1. Obtener la tarea (pasamos el índice por el Intent)
        int index = getIntent().getIntExtra("tareaIndex", -1);
        if (index == -1) { finish(); return; }

        Tarea tarea = ListadoTareasActivity.listaTareas.get(index);

        // 2. Referenciar las vistas
        TextView tvDesc = findViewById(R.id.tvDescripcionDetalle);
        TableLayout tabla = findViewById(R.id.tablaAdjuntos);

        // 3. Mostrar la descripción
        tvDesc.setText(tarea.getDescripcion());

        // 4. Llenar la tabla de archivos
        for (String ruta : tarea.getArchivosAdjuntos()) {
            TableRow fila = new TableRow(this);

            // Icono
            ImageView icono = new ImageView(this);
            // Lógica simple: si la ruta contiene ".jpg" pones icono de imagen, etc.
            icono.setImageResource(R.drawable.ic_documento); // Asegúrate de tener este drawable

            // Nombre
            TextView nombre = new TextView(this);
            nombre.setText(new File(ruta).getName());
            nombre.setPadding(20, 10, 10, 10);

            fila.addView(icono);
            fila.addView(nombre);
            tabla.addView(fila);
        }
    }
}
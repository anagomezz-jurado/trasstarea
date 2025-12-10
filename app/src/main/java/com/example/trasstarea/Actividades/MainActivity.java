package com.example.trasstarea.Actividades;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trasstarea.R;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView txtDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Cargar el idioma guardado o usar el idioma del dispositivo
        cargarIdioma();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Enlaces con la vista
        txtDescripcion = findViewById(R.id.txtDescripcion);

        Button btnEmpezar = findViewById(R.id.btnEmpezar);
        btnEmpezar.setOnClickListener(this::irAlListado);

        // Botones para cambiar el idioma
        Button btnEspañol = findViewById(R.id.btnEspañol);
        Button btnIngles = findViewById(R.id.btnIngles);

        btnEspañol.setOnClickListener(v -> establecerIdioma("es"));
        btnIngles.setOnClickListener(v -> establecerIdioma("en"));
    }


    public void irAlListado(View view) {
        Intent intent = new Intent(this, ListadoTareasActivity.class);
        startActivity(intent);
    }

    private void establecerIdioma(String codigoIdioma) {
        Locale locale = new Locale(codigoIdioma);
        Locale.setDefault(locale);

        Resources recursos = getResources();
        Configuration configuracion = recursos.getConfiguration();
        configuracion.setLocale(locale);
        recursos.updateConfiguration(configuracion, recursos.getDisplayMetrics());

        // Guardar idioma seleccionado
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("My_Lang", codigoIdioma);
        editor.apply();

        // Reiniciar actividad para aplicar cambios
        recreate();
    }

    /**
     * Cargar el idioma guardado o usar el idioma del móvil si no hay preferencia
     */
    private void cargarIdioma() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String idiomaGuardado = prefs.getString("My_Lang", "");

        if (idiomaGuardado.isEmpty()) {
            // Tomar el idioma del sistema
            idiomaGuardado = Locale.getDefault().getLanguage();
        }

        Locale locale = new Locale(idiomaGuardado);
        Locale.setDefault(locale);

        Resources recursos = getResources();
        Configuration configuracion = recursos.getConfiguration();
        configuracion.setLocale(locale);
        recursos.updateConfiguration(configuracion, recursos.getDisplayMetrics());
    }
}

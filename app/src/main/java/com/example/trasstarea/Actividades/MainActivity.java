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

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //es el diseño de la actividad

        //inicializo la instancia
        txtDescripcion = findViewById(R.id.txtDescripcion);

        //Al darle al boton de empezar llamo a irAlListado que se va a la actividad ListadoTareasActivity
        Button btnEmpezar = findViewById(R.id.btnEmpezar);
        btnEmpezar.setOnClickListener(this::irAlListado);

        // Botones para cambiar el idioma
        Button btnEspañol = findViewById(R.id.btnEspañol);
        Button btnIngles = findViewById(R.id.btnIngles);

        btnEspañol.setOnClickListener(v -> establecerIdioma("es"));
        btnIngles.setOnClickListener(v -> establecerIdioma("en"));
    }

    //Este es el método donde me envia a la actividad de Listado de tareas
    public void irAlListado(View view) {
        Intent intent = new Intent(this, ListadoTareasActivity.class);
        startActivity(intent);
    }

    private void establecerIdioma(String codigoIdioma) {
        //Creo el objeto Locale  y establezco el predeterminado
        Locale locale = new Locale(codigoIdioma);
        Locale.setDefault(locale);

        //Obtengo los recursos  y la Configuración que establece un nuevo idioa
        Resources recursos = getResources();
        Configuration configuracion = recursos.getConfiguration();
        configuracion.setLocale(locale);
        //Y aplica la nueva configuracion a los recursos de la aplicacion
        recursos.updateConfiguration(configuracion, recursos.getDisplayMetrics());

        //Utilizo el SharedPreferences para guardar el código del idioma y accedo a Setting
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("My_Lang", codigoIdioma); //Gurado el código del idioma como "My_Kan " y guardo los cambios con apply
        editor.apply();

        // Reinicio la actividad para que se hagan los cambios
        recreate();
    }


}

package com.example.trasstarea.Actividades;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trasstarea.Fragmentos.SettingsFragment;
import com.example.trasstarea.R;


public class MainActivity extends AppCompatActivity {

    private TextView txtDescripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SettingsFragment.aplicarTema(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //es el diseño de la actividad

        //inicializo la instancia
        txtDescripcion = findViewById(R.id.txtDescripcion);

        //Al darle al boton de empezar llamo a irAlListado que se va a la actividad ListadoTareasActivity
        Button btnEmpezar = findViewById(R.id.btnEmpezar);
        btnEmpezar.setOnClickListener(this::irAlListado);
    }

    //Este es el método donde me envia a la actividad de Listado de tareas
    public void irAlListado(View view) {
        Intent intent = new Intent(this, ListadoTareasActivity.class);
        startActivity(intent);
    }


}

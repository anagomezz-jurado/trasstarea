package com.example.trasstarea.Actividades;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.example.trasstarea.Fragmentos.SettingsFragment;
import com.example.trasstarea.R;
import com.google.android.material.appbar.MaterialToolbar;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // --- Aplicar tema ANTES de inflar la vista ---
        SettingsFragment.aplicarTema(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        // --- Toolbar como ActionBar ---
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // --- Activar flecha atrás ---
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Preferencias de usuario");
        }

        // --- Cargar fragmento ---
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
    }

    // --- Flecha atrás ---
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // --- Tamaño de letra ---
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(SettingsFragment.aplicarTamanoLetra(newBase));
    }

    // --- Botón restablecer preferencias ---
    public void restablecerPreferencias(View view) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("tema", true);     // Claro
        editor.putString("fuente", "2");     // Tamaño normal
        editor.putString("criterio", "No determinada");
        editor.putBoolean("bd_externa", true);
        editor.putString("nombre", "Por determinar");
        editor.putString("ip", "0.0.0.0");
        editor.putBoolean("orden", true);
        editor.putBoolean("sd", false);
        editor.apply();

        // Recrear para aplicar cambios
        recreate();
    }
}

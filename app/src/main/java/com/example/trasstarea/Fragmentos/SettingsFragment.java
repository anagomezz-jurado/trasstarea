package com.example.trasstarea.Fragmentos;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;
import androidx.preference.PreferenceManager;

import com.example.trasstarea.R;
import com.example.trasstarea.Actividades.SettingsActivity;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        // --- Switch de tema ---
        SwitchPreferenceCompat temaPref = findPreference("tema");
        if (temaPref != null) {
            temaPref.setOnPreferenceChangeListener((preference, newValue) -> {
                boolean claro = (boolean) newValue;
                AppCompatDelegate.setDefaultNightMode(
                        claro ? AppCompatDelegate.MODE_NIGHT_NO
                                : AppCompatDelegate.MODE_NIGHT_YES
                );
                if (getActivity() != null) getActivity().recreate();
                return true;
            });
        }

        // --- Tamaño de letra ---
        ListPreference fuentePref = findPreference("fuente");
        if (fuentePref != null) {
            fuentePref.setOnPreferenceChangeListener((preference, newValue) -> {
                if (getActivity() != null) getActivity().recreate();
                return true;
            });
        }

        // --- Criterio de orden ---
        ListPreference criterioPref = findPreference("criterio");
        if (criterioPref != null) {
            criterioPref.setOnPreferenceChangeListener((preference, newValue) -> true);
        }

        // --- Orden asc/desc ---
        SwitchPreferenceCompat ordenPref = findPreference("orden");
        if (ordenPref != null) {
            ordenPref.setOnPreferenceChangeListener((preference, newValue) -> true);
        }

        // --- Guardar en SD ---
        SwitchPreferenceCompat sdPref = findPreference("sd");
        if (sdPref != null) {
            sdPref.setOnPreferenceChangeListener((preference, newValue) -> true);
        }

        // --- Botón restablecer preferencias ---
        Preference resetPref = findPreference("restablecer");
        if (resetPref != null) {
            resetPref.setOnPreferenceClickListener(preference -> {
                if (getActivity() instanceof SettingsActivity) {
                    ((SettingsActivity) getActivity()).restablecerPreferencias(null);
                }
                return true;
            });
        }
    }

    // --- Aplicar tema ---
    public static void aplicarTema(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean claro = prefs.getBoolean("tema", true);
        AppCompatDelegate.setDefaultNightMode(
                claro ? AppCompatDelegate.MODE_NIGHT_NO
                        : AppCompatDelegate.MODE_NIGHT_YES
        );
    }

    // --- Aplicar tamaño de letra ---
    public static Context aplicarTamanoLetra(Context context) {
        float tamano = obtenerTamanoLetra(context);

        Configuration config = new Configuration(context.getResources().getConfiguration());
        config.fontScale = tamano / 16f; // 16f = tamaño normal

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        metrics.scaledDensity = config.fontScale * metrics.density;

        return context.createConfigurationContext(config);
    }

    // --- Obtener tamaño de letra ---
    public static float obtenerTamanoLetra(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        int valor = 2; // default
        try {
            valor = Integer.parseInt(prefs.getString("fuente", "2"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        switch (valor) {
            case 1: return 12f;
            case 3: return 20f;
            default: return 16f;
        }
    }
}

package com.example.trasstarea.Fragmentos;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import com.example.trasstarea.R;
import com.example.trasstarea.TareaViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FragmentoDos extends Fragment {

    private FragmentoDosListener listener;
    private TareaViewModel vm;

    public interface FragmentoDosListener {
        void onGuardarDescripcion(String descripcion);
        void onVolver();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (FragmentoDosListener) context;
        vm = new ViewModelProvider(requireActivity()).get(TareaViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragmentodos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        EditText etDescripcion = view.findViewById(R.id.etDescripcion);
        Button btnVolver = view.findViewById(R.id.btnVolver);
        Button btnGuardar = view.findViewById(R.id.btnGuardar);

        ImageButton btnDocumento = view.findViewById(R.id.btnDocumento);
        ImageButton btnImagen = view.findViewById(R.id.btnImagen);
        ImageButton btnAudio = view.findViewById(R.id.btnAudio);
        ImageButton btnVideo = view.findViewById(R.id.btnVideo);

        // Guardar descripción
        btnGuardar.setOnClickListener(v -> {
            String descripcion = etDescripcion.getText().toString().trim();
            if (descripcion.isEmpty()) {
                Snackbar.make(view, getString(R.string.msgLaDescripcionEstaVacia), Snackbar.LENGTH_SHORT).show();
                return;
            }
            listener.onGuardarDescripcion(descripcion);
        });

        btnVolver.setOnClickListener(v -> listener.onVolver());

        // Selección de archivos
        btnDocumento.setOnClickListener(v -> abrirSelectorArchivo("application/*"));
        btnImagen.setOnClickListener(v -> abrirSelectorArchivo("image/*"));
        btnAudio.setOnClickListener(v -> abrirSelectorArchivo("audio/*"));
        btnVideo.setOnClickListener(v -> abrirSelectorArchivo("video/*"));
    }

    private void abrirSelectorArchivo(String tipoMime) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType(tipoMime);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.seleccionar_archivo)), 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == getActivity().RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                String nombreArchivo = uri.getLastPathSegment();
                String rutaGuardada = guardarArchivoLocal(uri, nombreArchivo);
                if (rutaGuardada != null) {
                    vm.agregarArchivo(rutaGuardada); // Actualiza el LiveData correctamente
                    Snackbar.make(getView(), "Archivo guardado: " + nombreArchivo, Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(getView(), "Error al guardar el archivo", Snackbar.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String guardarArchivoLocal(Uri uri, String tipoMime) {
        try {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
            boolean usarSD = prefs.getBoolean("preferencia_sd", false);

            File directorio;
            if (usarSD && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                directorio = getContext().getExternalFilesDir(null); // Memoria SD (externa app)
            } else {
                directorio = getContext().getFilesDir(); // Memoria Interna
            }

            File folder = new File(directorio, "adjuntos_tareas");
            if (!folder.exists()) folder.mkdirs();

            // Generar nombre único para evitar sobreescritura
            String nombreOriginal = getFileName(uri);
            File destino = new File(folder, System.currentTimeMillis() + "_" + nombreOriginal);

            // Copiar archivo (Stream)
            try (InputStream in = getContext().getContentResolver().openInputStream(uri);
                 OutputStream out = new FileOutputStream(destino)) {
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
            }

            return destino.getAbsolutePath();
        } catch (IOException e) {
            return null;
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (android.database.Cursor cursor = getContext().getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME);
                    if (nameIndex != -1) {
                        result = cursor.getString(nameIndex);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    private void comprobarPermisosYAbrirSelector(String tipoMime) {
        if (androidx.core.content.ContextCompat.checkSelfPermission(getContext(),
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != android.content.pm.PackageManager.PERMISSION_GRANTED) {

            // Pedir permiso
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
        } else {
            abrirSelectorArchivo(tipoMime);
        }
    }

    // Gestionar la respuesta del usuario al permiso
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 200 && grantResults.length > 0 && grantResults[0] == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            // Permiso concedido, pero como no sabemos qué botón pulsó antes, avisamos al usuario
            Toast.makeText(getContext(), "Permiso concedido. Pulsa de nuevo para elegir archivo", Toast.LENGTH_SHORT).show();
        }
    }
}

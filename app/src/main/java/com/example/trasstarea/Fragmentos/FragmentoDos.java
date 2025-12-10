package com.example.trasstarea.Fragmentos;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.trasstarea.R;
import com.google.android.material.snackbar.Snackbar;

public class FragmentoDos extends Fragment {

    private FragmentoDosListener listener;

    public interface FragmentoDosListener {
        void onGuardarDescripcion(String descripcion);
        void onVolver();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (FragmentoDosListener) context;
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

        btnGuardar.setOnClickListener(v -> {
            String descripcion = etDescripcion.getText().toString().trim();
            if (descripcion.isEmpty()) { Snackbar.make(view, getString(R.string.msgLaDescripcionEstaVacia), Snackbar.LENGTH_SHORT).show(); return; }
            listener.onGuardarDescripcion(descripcion);
        });

        btnVolver.setOnClickListener(v -> listener.onVolver());
    }
}

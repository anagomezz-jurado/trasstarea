package com.example.trasstarea;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trasstarea.Actividades.EditarTareaActivity;
import com.example.trasstarea.Modelo.Tarea;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    private ArrayList<Tarea> datosTarea;

    public TareaAdapter(ArrayList<Tarea> datosTarea) {
        this.datosTarea = datosTarea;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_listado, parent, false);
        return new TareaViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        holder.bindTarea(datosTarea.get(position));
    }

    @Override
    public int getItemCount() {
        return datosTarea.size();
    }

    public void actualizarDatos(ArrayList<Tarea> nuevasTareas) {
        this.datosTarea.clear();
        this.datosTarea.addAll(nuevasTareas);
        notifyDataSetChanged();
    }

    public class TareaViewHolder extends RecyclerView.ViewHolder {
        private TextView tituloTarea;
        private ProgressBar progressBar;
        private TextView txtFecha;
        private TextView txtCantidad;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            tituloTarea = itemView.findViewById(R.id.txtTitutloTarea);
            progressBar = itemView.findViewById(R.id.progressBar);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
        }

        public void bindTarea(Tarea tarea) {
            tituloTarea.setText(tarea.getTitulo());
            progressBar.setProgress(tarea.getProgreso());
            txtFecha.setText(tarea.getFechaObjetivo());

            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate fechaObjetivo = LocalDate.parse(tarea.getFechaObjetivo(), formatter);
                long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), fechaObjetivo);
                txtCantidad.setText(String.valueOf(diasRestantes));
            } catch (Exception e) {
                txtFecha.setText("Fecha inválida");
                txtCantidad.setText("-");
            }


            itemView.setOnClickListener(v -> {
                new AlertDialog.Builder(itemView.getContext())
                        .setTitle(tarea.getTitulo())
                        .setMessage(tarea.getDescripcion())
                        .setPositiveButton("Cerrar", null)
                        .show();
            });

            // Click largo para menú contextual
            itemView.setOnLongClickListener(v -> {
                mostrarMenuContextual(itemView.getContext(), tarea);
                return true;
            });
        }

        private void mostrarMenuContextual(Context context, Tarea tarea) {
            CharSequence opciones[] = new CharSequence[] {"Editar", "Borrar"};
            new AlertDialog.Builder(context)
                    .setTitle(tarea.getTitulo())
                    .setItems(opciones, (dialog, which) -> {
                        if (which == 0) {
                            // Editar
                            Intent i = new Intent(context, EditarTareaActivity.class);
                            i.putExtra("tareaIndex", getAdapterPosition());
                            context.startActivity(i);
                        } else if (which == 1) {
                            // Borrar con confirmación
                            new AlertDialog.Builder(context)
                                    .setTitle("Borrar tarea")
                                    .setMessage("¿Seguro que quieres borrar \"" + tarea.getTitulo() + "\"?")
                                    .setPositiveButton("Borrar", (d, w) -> {
                                        int pos = getAdapterPosition();
                                        RepositorioTareas.listaTareas.remove(pos);
                                        actualizarDatos(new ArrayList<>(RepositorioTareas.listaTareas));
                                        Toast.makeText(context, "Tarea borrada", Toast.LENGTH_SHORT).show();
                                    })
                                    .setNegativeButton("Cancelar", null)
                                    .show();
                        }
                    }).show();
        }
    }
}

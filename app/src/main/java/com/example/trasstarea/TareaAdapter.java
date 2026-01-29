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
import com.example.trasstarea.Actividades.DescripcionActivity;

import com.example.trasstarea.Actividades.EditarTareaActivity;
import com.example.trasstarea.Actividades.ListadoTareasActivity;
import com.example.trasstarea.Modelo.Tarea;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class TareaAdapter extends RecyclerView.Adapter<TareaAdapter.TareaViewHolder> {

    private ArrayList<Tarea> datosTarea;

    public TareaAdapter(ArrayList<Tarea> datosTarea) {
        this.datosTarea = datosTarea; // Constructor que recibe los datos iniciales
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflo el layout de un elemento_listado y creo un ViewHolder
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.elemento_listado, parent, false);
        return new TareaViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        // Asigno los datos de la tarea en la posición al ViewHolder.
        holder.bindTarea(datosTarea.get(position));
    }

    @Override
    public int getItemCount() {
        return datosTarea.size(); // Devuelve los elementos de la tarea
    }

    public void actualizarDatos(ArrayList<Tarea> nuevasTareas) {
        // Método para reemplazar los datos y refrescar la vista.
        this.datosTarea.clear();
        this.datosTarea.addAll(nuevasTareas);
        notifyDataSetChanged(); // Indica al RecyclerView que debe redibujar todos sus elementos.
    }
/// //////////////////////////////////////////////////
    public class TareaViewHolder extends RecyclerView.ViewHolder {
        private TextView tituloTarea;
        private ProgressBar progressBar;
        private TextView txtFecha;
        private TextView txtCantidad;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            // Enlazo los componentes del layout  con las variables
            tituloTarea = itemView.findViewById(R.id.txtTitutloTarea);
            progressBar = itemView.findViewById(R.id.progressBar);
            txtFecha = itemView.findViewById(R.id.txtFecha);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
        }

        public void bindTarea(Tarea tarea) {
            // Relleno los elementos de la fila con los datos de la tarea
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

            //Si hago click corto muestro dialgodo de el titulo y la descripcion de la tarea
            // En TareaAdapter.java -> bindTarea
            itemView.setOnClickListener(v -> {
                Intent i = new Intent(v.getContext(), DescripcionActivity.class);
                i.putExtra("tareaIndex", getAdapterPosition());
                v.getContext().startActivity(i);
            });

            // Si hago click largo muestro las opciones de Editar o Borrar
            itemView.setOnLongClickListener(v -> {
                mostrarMenuContextual(itemView.getContext(), tarea); // Llamo al menú contextual con el método
                return true;
            });
        }

        private void mostrarMenuContextual(Context context, Tarea tarea) {
            CharSequence opciones[] = new CharSequence[] {context.getString(R.string.Editar),
                                                    context.getString(R.string.Borrar)}; //Son las opciones que hay en el menú contextual
            new AlertDialog.Builder(context)
                    .setTitle(tarea.getTitulo())
                    .setItems(opciones, (dialog, which) -> {
                        if (which == 0) { //Si la opción es 0 es editar
                            Intent i = new Intent(context, EditarTareaActivity.class); //   donde inicio la actividad y la dirijo hacia ella
                            i.putExtra("tareaIndex", getAdapterPosition()); // Pasa el índice de la tarea en la lista.
                            context.startActivity(i);
                        } else if (which == 1) { //Si la opicón es 1 es Borrar
                            new AlertDialog.Builder(context)
                                    .setTitle("Borrar tarea")
                                    .setMessage("¿Seguro que quieres borrar \"" + tarea.getTitulo() + "\"?")
                                    .setPositiveButton("Borrar", (d, w) -> {
                                        int pos = getAdapterPosition();
                                        ListadoTareasActivity.listaTareas.remove(pos); // Elimina del repositorio global.
                                        // Refresco el adaptador  actualizando la lista
                                        actualizarDatos(new ArrayList<>(ListadoTareasActivity.listaTareas));
                                        Toast.makeText(context, "Tarea borrada", Toast.LENGTH_SHORT).show();
                                    })
                                    .setNegativeButton("Cancelar", null)
                                    .show();
                        }
                    }).show();
        }
    }
}
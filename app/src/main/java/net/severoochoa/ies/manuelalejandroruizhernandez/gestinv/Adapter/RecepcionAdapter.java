package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Almacen;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Categoria;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Producto;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Recepcion;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Utiles.Util;

import java.util.Calendar;
import java.util.List;

public class RecepcionAdapter extends RecyclerView.Adapter<RecepcionAdapter.RecepcionViewHolder> {
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;
    private DatabaseManager db;
    private List<Recepcion> recepcionList;

    public RecepcionAdapter(Context context, List<Recepcion> recepcionList) {
        db = DatabaseManager.getInstance(context);
        this.recepcionList = recepcionList;
    }

    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void SetOnItemLongClickListener(final OnItemLongClickListener mItemLongClickListener) {
        this.mItemLongClickListener = mItemLongClickListener;
    }

    @NonNull
    @Override
    public RecepcionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_recepcion, parent, false);
        return new RecepcionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecepcionViewHolder holder, int position) {
        Recepcion recepcion = recepcionList.get(position);
        String text = "";
        db.openDB();
        Producto producto = db.getProducto(recepcion.getIdProducto());
        if (producto != null) {
            holder.tvProducto.setText(producto.getNombre());
            Categoria categoria = db.getCategoria(producto.getIdCategoria());
            if (categoria != null) {
                text = " " + categoria.getUnidadMedida();
            }
        }
        Almacen almacen = db.getAlmacen(recepcion.getIdAlmacen());
        holder.tvEstado.setText(db.getEstado(recepcion.getIdEstado()));
        db.closeDB();
        holder.tvCantidad.setText(recepcion.getCantidadRecibida() + text);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(recepcion.getFechaCreacion());
        holder.tvFecha.setText(Util.calendarToNumericString(calendar));
        if (almacen != null)
            holder.tvAlmacen.setText(almacen.getNombre());
    }

    @Override
    public int getItemCount() {
        return recepcionList.size();
    }

    public Recepcion getRecepcion(int position) {
        return recepcionList.get(position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public class RecepcionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvProducto, tvFecha, tvEstado, tvCantidad, tvAlmacen;

        public RecepcionViewHolder(View itemView) {
            super(itemView);
            tvAlmacen = itemView.findViewById(R.id.tvAlmacen);
            tvProducto = itemView.findViewById(R.id.tvProducto);
            tvFecha = itemView.findViewById(R.id.tvFechaCreacion);
            tvEstado = itemView.findViewById(R.id.tvEstado);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (mItemLongClickListener != null) {
                mItemLongClickListener.onItemLongClick(v, getAdapterPosition());
            }
            return true;
        }
    }
}

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
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Transferencia;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Utiles.Util;

import java.util.Calendar;
import java.util.List;

public class TransferenciaAdapter extends RecyclerView.Adapter<TransferenciaAdapter.TransferenciaViewHolder> {

    private List<Transferencia> transferenciaList;
    private DatabaseManager db;
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    public TransferenciaAdapter(Context context, List<Transferencia> transferenciaList) {
        db = DatabaseManager.getInstance(context);
        this.transferenciaList = transferenciaList;
    }

    @NonNull
    @Override
    public TransferenciaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_transferencia, parent, false);
        return new TransferenciaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransferenciaViewHolder holder, int position) {
        Transferencia transferencia = transferenciaList.get(position);
        String text = "";
        db.openDB();
        Producto producto = db.getProducto(transferencia.getIdProducto());
        if (producto != null) {
            holder.tvProducto.setText(producto.getNombre());
            Categoria categoria = db.getCategoria(producto.getIdCategoria());
            if (categoria != null) {
                text = " " + categoria.getUnidadMedida();
            }
        }
        Almacen almacen = db.getAlmacen(transferencia.getIdAlmacen());
        holder.tvEstado.setText(db.getEstado(transferencia.getIdEstado()));
        db.closeDB();
        holder.tvCantidad.setText(transferencia.getCantidadTransferida() + text);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(transferencia.getFechaCreacion());
        holder.tvFecha.setText(Util.calendarToNumericString(calendar));
        if (almacen != null)
            holder.tvAlmacen.setText(almacen.getNombre());
    }

    @Override
    public int getItemCount() {
        return transferenciaList.size();
    }

    public Transferencia getTransferencia(int position) {
        return transferenciaList.get(position);
    }


    public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    public void SetOnItemLongClickListener(final OnItemLongClickListener mItemLongClickListener) {
        this.mItemLongClickListener = mItemLongClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public class TransferenciaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvProducto, tvFecha, tvEstado, tvCantidad, tvAlmacen;

        public TransferenciaViewHolder(View itemView) {
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

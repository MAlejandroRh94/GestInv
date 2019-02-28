package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Almacen;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;

import java.util.List;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;

public class AlmacenAdapter extends RecyclerView.Adapter<AlmacenAdapter.AlmacenViewHolder> {
    private Context context;
    private List<Almacen> almacenList;
    private DatabaseManager db;
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    public AlmacenAdapter(Context context, List<Almacen> almacenList) {
        this.context = context;
        this.almacenList = almacenList;
        db = DatabaseManager.getInstance(context);
    }

    @Override
    public AlmacenAdapter.AlmacenViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_almacen, parent, false);
        return new AlmacenViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AlmacenAdapter.AlmacenViewHolder holder, int position) {
        Almacen almacen = almacenList.get(position);
        holder.tvId.setText(String.valueOf(almacen.getId()));
        holder.tvNombre.setText(almacen.getNombre());
        holder.tvDireccion.setText(almacen.getDireccion());
        db.openDB();
        holder.tvPasillos.setText(db.getPasillosFromAlmacen(almacen.getId()).size() + " Pasillo/s");
        holder.tvSecciones.setText(db.getSeccionesFromAlmacen(almacen.getId()).size() + " Seccion/es");
        db.closeDB();

    }

    @Override
    public int getItemCount() {
        return almacenList.size();
    }

    public Almacen getAlmacen(int position) {
        return almacenList.get(position);
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

    public class AlmacenViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvDireccion, tvNombre, tvId, tvSecciones, tvPasillos;

        public AlmacenViewHolder(View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvSecciones = itemView.findViewById(R.id.tvSecciones);
            tvPasillos = itemView.findViewById(R.id.tvPasillos);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDireccion = itemView.findViewById(R.id.tvDireccion);
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
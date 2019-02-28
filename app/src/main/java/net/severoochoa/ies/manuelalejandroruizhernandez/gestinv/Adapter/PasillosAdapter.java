package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Categoria;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Pasillo;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Producto;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;

import java.util.List;

public class PasillosAdapter extends RecyclerView.Adapter<PasillosAdapter.PasilloViewHolder> {
    private List<Pasillo> pasilloList;
    private DatabaseManager db;
    private OnItemLongClickListener mItemLongClickListener;

    public PasillosAdapter(Context context, List<Pasillo> almacenList) {
        this.pasilloList = almacenList;
        db = DatabaseManager.getInstance(context);
    }

    public void SetOnItemLongClickListener(final OnItemLongClickListener mItemLongClickListener) {
        this.mItemLongClickListener = mItemLongClickListener;
    }

    @NonNull
    @Override
    public PasilloViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_pasillo, parent, false);
        return new PasilloViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PasilloViewHolder holder, int position) {
        Pasillo pasillo = pasilloList.get(position);

        holder.tvId.setText(String.valueOf(pasillo.getId()));
        holder.tvNombre.setText(pasillo.getNombre());

        db.openDB();
        Producto producto = db.getProducto(pasillo.getIdProducto());
        if (producto != null) {
            Categoria categoria = db.getCategoria(producto.getIdCategoria());
            holder.tvProducto.setText(producto.getNombre());
            String text = String.valueOf(pasillo.getCantidadAlmacenada());
            if (categoria != null)
                text = text + " " + categoria.getUnidadMedida();
            holder.tvCant.setText(text);
        }
        db.closeDB();
    }

    @Override
    public int getItemCount() {
        return pasilloList.size();
    }

    public Pasillo getPasillo(int position) {
        return pasilloList.get(position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public class PasilloViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView tvCant, tvNombre, tvId, tvProducto;

        public PasilloViewHolder(View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCant = itemView.findViewById(R.id.tvCant);
            tvProducto = itemView.findViewById(R.id.tvProducto);
            itemView.setOnLongClickListener(this);
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

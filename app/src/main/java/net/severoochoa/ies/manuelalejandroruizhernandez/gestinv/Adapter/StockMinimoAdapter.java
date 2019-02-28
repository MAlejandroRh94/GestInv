package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Categoria;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Producto;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.StockMinimo;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;

import java.util.List;

public class StockMinimoAdapter extends RecyclerView.Adapter<StockMinimoAdapter.StockMinimoViewHolder> {
    private List<StockMinimo> stockMinimoList;
    private DatabaseManager db;
    private OnItemLongClickListener mItemLongClickListener;

    public StockMinimoAdapter(Context context, List<StockMinimo> stockMinimoList) {
        this.stockMinimoList = stockMinimoList;
        db = DatabaseManager.getInstance(context);
    }

    @NonNull
    @Override
    public StockMinimoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_stock_minimo, parent, false);
        return new StockMinimoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StockMinimoViewHolder holder, int position) {
        StockMinimo stockMinimo = stockMinimoList.get(position);
        db.openDB();
        Producto producto = db.getProducto(stockMinimo.getIdProd());
        Categoria categoria = db.getCategoria(producto.getIdCategoria());
        db.closeDB();
        holder.tvProducto.setText(producto.getNombre());
        holder.tvCantMin.setText(stockMinimo.getCantMin() + " " + categoria.getUnidadMedida());
    }

    @Override
    public int getItemCount() {
        return stockMinimoList.size();
    }

    public StockMinimo getStockMinimo(int position) {
        return stockMinimoList.get(position);
    }

    public void SetOnItemLongClickListener(final OnItemLongClickListener mItemLongClickListener) {
        this.mItemLongClickListener = mItemLongClickListener;
    }

    public interface OnItemLongClickListener {
        void onItemLongClickSto(View view, int position);
    }

    public class StockMinimoViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        TextView tvProducto, tvCantMin;

        public StockMinimoViewHolder(View itemView) {
            super(itemView);
            tvProducto = itemView.findViewById(R.id.tvProducto);
            tvCantMin = itemView.findViewById(R.id.tvCantMin);
            itemView.setOnLongClickListener(this);
        }


        @Override
        public boolean onLongClick(View v) {
            if (mItemLongClickListener != null) {
                mItemLongClickListener.onItemLongClickSto(v, getAdapterPosition());
            }
            return true;
        }
    }
}

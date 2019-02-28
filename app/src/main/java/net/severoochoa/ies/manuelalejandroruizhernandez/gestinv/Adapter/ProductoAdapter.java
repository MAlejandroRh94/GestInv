package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Categoria;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Producto;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;

import java.util.List;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {
    private Context context;
    private List<Producto> productoList;
    private DatabaseManager db;
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;


    public ProductoAdapter(Context context, List<Producto> productoList) {
        this.context = context;
        this.productoList = productoList;
        db = DatabaseManager.getInstance(context);
    }

    @Override
    public ProductoAdapter.ProductoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_producto, parent, false);
        return new ProductoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductoAdapter.ProductoViewHolder holder, int position) {
        Producto producto = productoList.get(position);
        holder.tvNombre.setText(producto.getNombre());

        db.openDB();
        Categoria categoria = db.getCategoria(producto.getIdCategoria());
        db.closeDB();
        holder.tvCategoria.setText(categoria.getNombre());
        Picasso.get()
                .load("file://" + categoria.getImagen())
                .error(R.drawable.ic_no_image)
                .placeholder(R.drawable.ic_no_image)
                .fit()
                .centerCrop()
                .into(holder.ivImagen);
    }

    @Override
    public int getItemCount() {
        return productoList.size();
    }

    public Producto getProducto(int position) {
        return productoList.get(position);
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

    public class ProductoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView tvCategoria, tvNombre;
        ImageView ivImagen;

        public ProductoViewHolder(View itemView) {
            super(itemView);
            ivImagen = itemView.findViewById(R.id.ivImagen);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
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

package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Categoria;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Producto;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.ProductoCantidad;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;

import java.util.List;


public class ProductoCantidadAdapter extends RecyclerView.Adapter<ProductoCantidadAdapter.ProductoCantidadViewHolder> {
    private Context context;
    private List<ProductoCantidad> productoCantidadList;
    private DatabaseManager db;

    public ProductoCantidadAdapter(Context context, List<ProductoCantidad> productoCantidadList) {
        this.context = context;
        this.productoCantidadList = productoCantidadList;
        db = DatabaseManager.getInstance(context);
    }

    @NonNull
    @Override
    public ProductoCantidadAdapter.ProductoCantidadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_inventario, parent, false);
        return new ProductoCantidadViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductoCantidadAdapter.ProductoCantidadViewHolder holder, int position) {
        ProductoCantidad productoCantidad = productoCantidadList.get(position);
        db.openDB();
        Producto producto = db.getProducto(productoCantidad.getIdProducto());
        if (producto != null) {
            holder.tvProducto.setText(producto.getNombre());
            Categoria categoria = db.getCategoria(producto.getIdCategoria());
            if (categoria != null) {
                Picasso.get()
                        .load("file://" + categoria.getImagen())
                        .error(R.drawable.ic_no_image)
                        .placeholder(R.drawable.ic_no_image)
                        .fit()
                        .centerCrop()
                        .into(holder.ivImagen);
                holder.tvCant.setText(productoCantidad.getCantidadProducto() + " " + categoria.getUnidadMedida());
            }
        }
        db.closeDB();
    }

    @Override
    public int getItemCount() {
        return productoCantidadList.size();
    }


    public class ProductoCantidadViewHolder extends RecyclerView.ViewHolder {
        TextView tvProducto, tvCant;
        ImageView ivImagen;

        public ProductoCantidadViewHolder(View itemView) {
            super(itemView);
            tvCant = itemView.findViewById(R.id.tvCantUnd);
            tvProducto = itemView.findViewById(R.id.tvProducto);
            ivImagen = itemView.findViewById(R.id.ivImagen);
        }

    }
}

package net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Atributo;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Classes.Categoria;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.DatabaseManager.DatabaseManager;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.R;
import net.severoochoa.ies.manuelalejandroruizhernandez.gestinv.Utiles.Util;

import java.util.Calendar;
import java.util.List;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.CategoriaViewHolder> {

    private Context context;
    private List<Categoria> categoriaList;
    private DatabaseManager db;
    private OnItemClickListener mItemClickListener;
    private OnItemLongClickListener mItemLongClickListener;

    public CategoriaAdapter(Context context, List<Categoria> categoriaList) {
        this.context = context;
        this.categoriaList = categoriaList;
        db = DatabaseManager.getInstance(context);
    }

    @Override
    public CategoriaAdapter.CategoriaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_categoria, parent, false);
        return new CategoriaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoriaAdapter.CategoriaViewHolder holder, int position) {
        Categoria categoria = categoriaList.get(position);
        holder.tvId.setText(String.valueOf(categoria.getId()));
        holder.tvNombre.setText(categoria.getNombre());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(categoria.getFechaAlta());
        holder.tvFechaAlta.setText(Util.calendarToNumericString(calendar));
        db.openDB();
        List<Atributo> listaAtr = db.getAtrFromCat(categoria.getId());
        db.closeDB();
        holder.tvAtributo1.setText(listaAtr.size() >= 1 ? listaAtr.get(0).getValor() : "-");
        holder.tvAtributo2.setText(listaAtr.size() >= 2 ? listaAtr.get(1).getValor() : "-");
        holder.tvAtributo3.setText(listaAtr.size() >= 3 ? listaAtr.get(2).getValor() : "-");

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
        return categoriaList.size();
    }

    public Categoria getCategoria(int position) {
        return categoriaList.get(position);
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

    public class CategoriaViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        ImageView ivImagen;
        TextView tvId, tvNombre, tvFechaAlta;
        TextView tvAtributo1, tvAtributo2, tvAtributo3;

        public CategoriaViewHolder(View itemView) {
            super(itemView);
            ivImagen = itemView.findViewById(R.id.ivImagen);
            tvId = itemView.findViewById(R.id.tvId);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvFechaAlta = itemView.findViewById(R.id.tvFechaAlta);
            tvAtributo1 = itemView.findViewById(R.id.tvAtributo1);
            tvAtributo2 = itemView.findViewById(R.id.tvAtributo2);
            tvAtributo3 = itemView.findViewById(R.id.tvAtributo3);
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
